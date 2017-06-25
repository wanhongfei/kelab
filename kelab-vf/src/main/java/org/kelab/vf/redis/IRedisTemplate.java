package org.kelab.vf.redis;

import com.alibaba.fastjson.TypeReference;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.CollectionUtil;
import org.kelab.util.FastJsonUtil;
import org.kelab.vf.result.PaginationResult;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;

import java.util.*;

@Slf4j
public abstract class IRedisTemplate {

	/**
	 * 是否开启缓存：
	 * 调试关闭或者线上开启
	 */
	@Setter
	protected volatile boolean enable = false;

	/**
	 * 初始化函数
	 * 注册zk监听
	 */
	public abstract void init();

	/**
	 * 是否开启缓存
	 *
	 * @return
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * 获取分布式资源
	 *
	 * @return
	 */
	public abstract ShardedJedis getShardedResource();

	/**
	 * 释放分布式资源
	 *
	 * @return
	 */
	public abstract void releaseShardedResource(ShardedJedis shardedJedis);

	/**
	 * 获取资源
	 *
	 * @return
	 */
	public abstract Jedis getResource();

	/**
	 * 释放资源
	 *
	 * @return
	 */
	public abstract void releaseResource(Jedis jedis);

	// ==================== 基本操作 ====================

	/**
	 * 获取对象
	 *
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T getObject(String key, Class<T> clazz) {
		String json = this.get(key);
		return json == null ? null : FastJsonUtil.json2Object(json, clazz);
	}

	/**
	 * 获取数组
	 *
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T[] getArray(String key, Class<T> clazz) {
		String json = this.get(key);
		return json == null ? null : FastJsonUtil.json2Array(json, clazz);
	}

	/**
	 * 获取列表
	 *
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> clazz) {
		String json = this.get(key);
		return json == null ? null : FastJsonUtil.json2List(json, clazz);
	}

	/**
	 * 自定义泛型实体类获取
	 *
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> PaginationResult<T> getPaginationResult(String key, Class<T> clazz) {
		String json = this.get(key);
		return json == null ? null : FastJsonUtil.json2GenericType(json,
				new TypeReference<PaginationResult<T>>(clazz) {
				});
	}

	/**
	 * 获取map
	 *
	 * @param key
	 * @return
	 */
	public <K, T> Map<K, T> getMap(String key, Class<K> keyClz,
	                               Class<T> valueClz) {
		String json = this.get(key);
		return json == null ? null : FastJsonUtil.json2Map(json, keyClz, valueClz);
	}

	/**
	 * 获取string
	 *
	 * @param key
	 * @return
	 */
	public abstract String get(String key);

	/**
	 * 批量获取string
	 *
	 * @param keys
	 * @return
	 */
	public abstract Map<String, String> mget(String... keys);

	/**
	 * 批量获取对象
	 *
	 * @param keys
	 * @return
	 */
	public Map<String, Object> mget(Map<String, Class> keys) {
		List<String> skeys = new ArrayList<>(keys.size());
		for (Map.Entry<String, Class> entry : keys.entrySet()) {
			skeys.add(entry.getKey());
		}
		Map<String, String> values = this.mget(CollectionUtil.list2Array(skeys, String.class));
		Map<String, Object> res = new HashMap<>(values.size());
		for (Map.Entry<String, String> entry : values.entrySet()) {
			if (entry.getValue() != null) {
				Class clazz = keys.get(entry.getKey());
				res.put(entry.getKey(), FastJsonUtil.json2Object(entry.getValue(), clazz));
			}
		}
		return res;
	}

	/**
	 * 设置对象
	 *
	 * @param key
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public abstract IRedisTemplate set(String key, Object obj);

	/**
	 * 批量设置对象
	 *
	 * @param keyAndValues
	 * @return
	 * @throws Exception
	 */
	public abstract IRedisTemplate mset(Map<String, Object> keyAndValues);

	/**
	 * 设置对象并设置时间
	 *
	 * @param key
	 * @param obj
	 * @param second
	 * @return
	 */
	public abstract IRedisTemplate set(String key, Object obj, int second);

	/**
	 * @param key
	 * @return
	 * @author wwhhf
	 * @comment 清除指定键值
	 * @since 2016年6月13日
	 */
	public abstract IRedisTemplate delete(String key);

	/**
	 * 自增step
	 *
	 * @param key
	 * @param step
	 * @return
	 */
	public abstract IRedisTemplate incrBy(String key, int step);

	/**
	 * 自减step
	 *
	 * @param key
	 * @param step
	 * @return
	 */
	public abstract IRedisTemplate decrBy(String key, int step);

	/**
	 * 自增1pi
	 *
	 * @param key
	 * @return
	 */
	public IRedisTemplate incr(String key) {
		this.incrBy(key, 1);
		return this;
	}

	/**
	 * 自减1
	 *
	 * @param key
	 * @return
	 */
	public IRedisTemplate decr(String key) {
		this.decrBy(key, 1);
		return this;
	}

	/**
	 * 设置过期时间
	 *
	 * @param key
	 * @param second
	 * @return
	 */
	public abstract IRedisTemplate expire(String key, int second);

	/**
	 * 设置过期时间
	 *
	 * @param timestamp
	 * @return
	 */
	public abstract IRedisTemplate expireAt(String key, long timestamp);

	/**
	 * 拼接
	 *
	 * @param
	 * @return
	 */
	public abstract IRedisTemplate append(String key, String s);

	/**
	 * 是否存在
	 *
	 * @param key
	 * @return
	 */
	public abstract boolean exist(String key);

	// ====================== list 操作 ======================

	/**
	 * list 尾部添加
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public abstract IRedisTemplate rpush(String key, Object... values);

	/**
	 * list 头部添加
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public abstract IRedisTemplate lpush(String key, Object... values);

	/**
	 * list 第index个设置value
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public abstract IRedisTemplate lset(String key, Object values, int index);

	/**
	 * list 第index个设置value
	 *
	 * @param key
	 * @param index
	 * @return
	 */
	public abstract String lindex(String key, int index);

	/**
	 * list 第index个设置value
	 *
	 * @param key
	 * @param index
	 * @return
	 */
	public <T> T lindex(String key, int index, Class<T> clazz) {
		String value = this.lindex(key, index);
		return value == null ? null : FastJsonUtil.json2Object(value, clazz);
	}

	/**
	 * list 尾部弹出
	 *
	 * @param key
	 * @return
	 */
	public abstract String rpop(String key);

	/**
	 * list 头部弹出
	 *
	 * @param key
	 * @return
	 */
	public abstract String lpop(String key);

	/**
	 * list 尾部弹出
	 *
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T rpop(String key, Class<T> clazz) {
		String value = this.rpop(key);
		return value == null ? null : FastJsonUtil.json2Object(value, clazz);
	}

	/**
	 * list 头部弹出
	 *
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T lpop(String key, Class<T> clazz) {
		String value = this.lpop(key);
		return value == null ? null : FastJsonUtil.json2Object(value, clazz);
	}

	/**
	 * 获取list的长度
	 *
	 * @param key
	 * @return
	 */
	public abstract long llen(String key);

	/**
	 * 获取指定范围list
	 * 0~len-1
	 *
	 * @param key
	 * @return
	 */
	public abstract List<String> lrange(String key, int from, int to);

	/**
	 * 获取指定范围list
	 * 0~len-1
	 *
	 * @param key
	 * @return
	 */
	public <T> List<T> lrange(String key, int from, int to, Class<T> clazz) {
		List<String> slist = this.lrange(key, from, to);
		List<T> res = new ArrayList<>();
		for (String value : slist) {
			res.add(FastJsonUtil.json2Object(value, clazz));
		}
		return res;
	}

	// ======================= set 操作  ======================

	/**
	 * set添加
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public abstract IRedisTemplate sadd(String key, Object... values);

	/**
	 * set中所有元素
	 *
	 * @param key
	 * @return
	 */
	public abstract Set<String> smembers(String key);

	/**
	 * set中所有元素
	 *
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> Set<T> smembers(String key, Class<T> clazz) {
		Set<String> slist = this.smembers(key);
		Set<T> res = new HashSet<>();
		for (String value : slist) {
			res.add(FastJsonUtil.json2Object(value, clazz));
		}
		return res;
	}

	// ================ hash 操作  ======================

	/**
	 * 设置hash对象某个field
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public abstract IRedisTemplate hset(String key, String field, Object value);

	/**
	 * 设置hash对象某些field
	 *
	 * @param key
	 * @param fieldAndValues
	 * @return
	 */
	public abstract IRedisTemplate hmset(String key, Map<String, Object> fieldAndValues);

	/**
	 * 获取hash对象某个field
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public abstract String hget(String key, String field);

	/**
	 * 获取hash对象某个field
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public <T> T hget(String key, String field, Class<T> clazz) {
		String value = this.hget(key, field);
		return value == null ? null : FastJsonUtil.json2Object(value, clazz);
	}

	/**
	 * 获取hash对象某些field
	 *
	 * @param key
	 * @param fields
	 * @return
	 */
	public abstract Map<String, String> hmget(String key, String... fields);

	/**
	 * 获取hash对象某些field
	 *
	 * @param key
	 * @param fields
	 * @return
	 */
	public Map<String, Object> hmget(String key, Map<String, Class> fields) {
		List<String> sfields = new ArrayList<>(fields.size());
		for (Map.Entry<String, Class> entry : fields.entrySet()) {
			sfields.add(entry.getKey());
		}
		Map<String, String> values = this.hmget(key, CollectionUtil.list2Array(sfields, String.class));
		Map<String, Object> res = new HashMap<>(values.size());
		for (Map.Entry<String, String> entry : values.entrySet()) {
			if (entry.getValue() != null) {
				Class clazz = fields.get(entry.getKey());
				res.put(entry.getKey(), FastJsonUtil.json2Object(entry.getValue(), clazz));
			}
		}
		return res;
	}

	/**
	 * 获取hash对象某些field
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public abstract boolean hexist(String key, String field);

	// ================ sorted set 操作  ======================

	/**
	 * 向sortset添加东西
	 * member相当与set集合里的key
	 *
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public abstract IRedisTemplate zadd(String key, double score, String member);

	/**
	 * 向sortset批量添加东西
	 * member相当与set集合里的key
	 *
	 * @param key
	 * @param scoreAndMembers
	 * @return
	 */
	public abstract IRedisTemplate zadd(String key, Map<String, Double> scoreAndMembers);

	/**
	 * 获取sortset某元素的得分
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract double zscore(String key, String member);

	/**
	 * 增加sortset某元素的得分
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract IRedisTemplate zincrby(String key, double incrScore, String member);

	/**
	 * 设置sortset某元素的得分
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract IRedisTemplate zset(String key, double score, String member);

	/**
	 * 删除sortset某元素
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract IRedisTemplate zrem(String key, String... member);

	/**
	 * 删除sortset部分元素
	 *
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public abstract IRedisTemplate zrem(String key, long start, long stop);

	/**
	 * 删除sortset全部元素
	 *
	 * @param key
	 * @return
	 */
	public abstract IRedisTemplate zrem(String key);

	/**
	 * 获取sortset集合元素数
	 *
	 * @param key
	 * @return
	 */
	public abstract long zcard(String key);

	/**
	 * 获取某元素的索引
	 * 升序
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract long zrank(String key, String member);

	/**
	 * 批量获取某元素的索引
	 * 升序
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract Map<String, Long> zrank(String key, List<String> member);

	/**
	 * 获取某元素的索引
	 * 降序
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract long zrevrank(String key, String member);

	/**
	 * 批量获取某元素的索引
	 * 降序
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public abstract Map<String, Long> zrevrank(String key, List<String> member);

	/**
	 * 按照索引起始和结束获取（升序）
	 *
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public abstract List<String> zrange(String key, long start, long stop);

	/**
	 * 按照索引起始和结束获取（升序）
	 *
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public abstract List<Tuple> zrangeWithScores(String key, long start, long stop);

	/**
	 * 按照索引起始和结束获取（降序）
	 *
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public abstract List<String> zrevrange(String key, long start, long stop);

	/**
	 * 按照索引起始和结束获取（降序）
	 *
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public abstract List<Tuple> zrevrangeWithScores(String key, long start, long stop);

	/**
	 * 删除指定范围的分数
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public abstract IRedisTemplate zremrangeByScore(String key, double start, double end);

	/**
	 * 定范围的分数的个数
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public abstract long zcount(String key, double start, double end);

}
