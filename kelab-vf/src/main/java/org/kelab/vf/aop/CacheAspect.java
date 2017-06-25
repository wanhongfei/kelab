package org.kelab.vf.aop;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.kelab.util.DateUtil;
import org.kelab.util.FastJsonUtil;
import org.kelab.util.model.Pair;
import org.kelab.vf.cache.CacheAccess;
import org.kelab.vf.cache.CacheFlush;
import org.kelab.vf.cache.chain.AbstractReturnTypeHandler;
import org.kelab.vf.constant.CacheConstant;
import org.kelab.vf.generator.MybatisMapping;
import org.kelab.vf.redis.IRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存切面管理
 * 每个实体类对应一个版本号，使用memcache惰性淘汰的特性控制版本
 * 在spring-servlet中声明
 */
@Slf4j
public class CacheAspect {

	/**
	 * 控制缓存读写同步
	 */
	private final ConcurrentHashMap<Class, ReentrantReadWriteLock> rwls = new ConcurrentHashMap<>();

	@Autowired
	private IRedisTemplate iRedisTemplate;

	@Autowired
	private MybatisMapping mybatisMapping;

	@Setter
	private List<AbstractReturnTypeHandler> handlers;

	/**
	 * 先进行访问缓存，若命中则直接返回，否则继续执行方法
	 * daoPointcut
	 *
	 * @param joinpoint
	 * @return
	 * @throws Throwable
	 */
	public <T> Object cacheAccess(ProceedingJoinPoint joinpoint) throws Throwable {
		Class clazz = joinpoint.getTarget().getClass();
		String clazzName = clazz.getName();
		String methodName = joinpoint.getSignature().getName();
		Object[] args = joinpoint.getArgs();
		Method method = ((MethodSignature) joinpoint.getSignature()).getMethod();
		boolean hasAnnotation = method.isAnnotationPresent(CacheAccess.class);
		if (!hasAnnotation || !this.iRedisTemplate.isEnable()) {
			return joinpoint.proceed(args);
		} else {
			// 获取泛型类型：
			Pair<Class, Class> entityAndQuery = this.mybatisMapping
					.getEntityAndQueryByDao(joinpoint.getTarget());
			// 获取方法返回类型
			Pair<Class, Class[]> returnAndGenerics = getMethodReturnType(method, entityAndQuery);
			// 缓存中的结果
			Object cache_value = null;
			// 存储版本号的key
			String version_key = getClassVersionKey(clazzName);
			Long version_value = null;
			ReentrantReadWriteLock rwl = this.getReadWriteLock(clazz);
			rwl.readLock().lock();// 上读锁
			try {
				// 获取版本号，不存在则返回NULL
				version_value = this.iRedisTemplate.getObject(version_key, CacheConstant.CLASS_VERSION_TYPE);
				// 版本号不为空
				if (version_value != null) {
					// 从redis中取出存储的json，转为对象，null->null
					String cache_key = this.getCacheKey(returnAndGenerics, clazzName, methodName, args, version_value);
					String json = this.iRedisTemplate.get(cache_key);
					for (AbstractReturnTypeHandler handler : handlers) {
						if (handler.isAccept(method)) {
							cache_value = handler.deserialize(json, returnAndGenerics);
							break;
						}
					}
				}
				// 缓存未命中
				if (cache_value == null) {// 锁升级
					rwl.readLock().unlock();// 解读锁
					rwl.writeLock().lock();// 上写锁
					try {
						// ============== 执行函数 ============
						cache_value = joinpoint.proceed(args);
						// ====================================
						if (cache_value != null) { // 如果结果为null，不缓存，避免转换失败
							// 将函数执行结果存储在redis，对象以json形式保存
							ShardedJedis shardedJedis = this.iRedisTemplate.getShardedResource();
							try {
								ShardedJedisPipeline pipe = shardedJedis.pipelined();
								if (version_value == null) { // 版本号不存在，重新设置版本号
									version_value = getNextVersionValue();
								}
								String cache_key = this.getCacheKey(returnAndGenerics, clazzName, methodName, args, version_value);
								pipe.set(version_key, FastJsonUtil.object2Json(version_value));
								pipe.set(cache_key, FastJsonUtil.object2Json(cache_value));
								pipe.sync();
							} finally {
								this.iRedisTemplate.releaseShardedResource(shardedJedis);
							}
						}
					} finally {// 锁降级
						rwl.readLock().lock();// 上读锁
						rwl.writeLock().unlock(); // 解写锁
					}
				}
				return cache_value;
			} finally {
				rwl.readLock().unlock();
			}
		}
	}

	/**
	 * 先进行缓存清除，再执行方法
	 * daoPointcut
	 *
	 * @param joinpoint
	 * @return
	 * @throws Throwable
	 */
	public Object cacheFlush(ProceedingJoinPoint joinpoint) throws Throwable {
		Class clazz = joinpoint.getTarget().getClass();
		String clazzName = clazz.getName();
		Object[] args = joinpoint.getArgs();
		Method method = ((MethodSignature) joinpoint.getSignature()).getMethod();
		boolean hasAnnotation = method.isAnnotationPresent(CacheFlush.class);
		if (!hasAnnotation || !this.iRedisTemplate.isEnable()) {
			return joinpoint.proceed(args);
		} else { // 上写锁
			ReentrantReadWriteLock rwl = this.getReadWriteLock(clazz);
			rwl.writeLock().lock();
			try {
				// 更新版本号
				String version_key = getClassVersionKey(clazzName);
				Long version = getNextVersionValue();
				this.iRedisTemplate.set(version_key, version);
				// ============ 执行函数 ============
				return joinpoint.proceed(args);
				// ====================================
			} finally { // 解写锁
				rwl.writeLock().unlock();
			}
		}
	}

	/**
	 * 获取类名对应的缓存版本号
	 *
	 * @param className
	 * @return
	 */
	private String getClassVersionKey(String className) {
		StringBuffer sb = new StringBuffer(className).append(".").append(CacheConstant.CLASS_VERSION_SUFFIX);
		return sb.toString();
	}

	/**
	 * 获取BaseDao的<T,Q>
	 *
	 * @param method
	 * @return
	 */
	private Pair<Class, Class[]> getMethodReturnType(Method method, Pair<Class, Class> entityAndQuery) {
		for (AbstractReturnTypeHandler handler : handlers) {
			if (handler.isAccept(method)) {
				return handler.resolve(method, entityAndQuery);
			}
		}
		return null;
	}

	/**
	 * 获取类名对应的下一次的缓存版本号(时间戳)
	 *
	 * @return
	 */
	private Long getNextVersionValue() {
		return DateUtil.currentLong();
	}

	/**
	 * 生成缓存的key
	 * className.[returnTypes].methodName(args).version
	 *
	 * @param returnAndGenerics
	 * @param clazzName
	 * @param methodName
	 * @param args
	 * @param version_value
	 * @return
	 */
	private String getCacheKey(Pair<Class, Class[]> returnAndGenerics, String clazzName,
	                           String methodName, Object[] args, long version_value) {
		StringBuffer sb = new StringBuffer(clazzName).append(".").append("[")
				.append(returnAndGenerics.getValue1().getCanonicalName());
		Class[] generics = returnAndGenerics.getValue2();
		for (Class clazz : generics) {
			sb.append(",").append(clazz.getCanonicalName());
		}
		sb.append("].").append(methodName).append("(");
		boolean isFirst = true;
		for (Object arg : args) {
			if (isFirst == true) {
				sb.append(arg);
				isFirst = false;
			} else {
				sb.append(",").append(arg);
			}
		}
		sb.append(").").append(version_value);
		return sb.toString();
	}

	/**
	 * 获取读写锁
	 *
	 * @param clazz
	 * @return
	 */
	private ReentrantReadWriteLock getReadWriteLock(Class clazz) {
		if (!rwls.containsKey(clazz)) {
			// double check
			synchronized (rwls) {
				if (!rwls.containsKey(clazz)) {
					// 表锁粒度
					rwls.put(clazz, new ReentrantReadWriteLock());
				}
			}
		}
		return rwls.get(clazz);
	}

}
