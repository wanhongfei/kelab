package org.kelab.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author wwhhf
 * @comment 读取配置文件
 * @since 2016年6月12日
 */
@Slf4j
public class PropertiesUtil {

	public static final String CFG_FILE_NAME = "config.properties";

	/**
	 * 配置文件管理者
	 */
	private final static ConfigCacheManager manager = new LocalConfigCacheManager();

	/**
	 * 获取classpath的*.properties文件内容
	 *
	 * @param name
	 * @return
	 * @throws IOException
	 */
	@SneakyThrows
	public static String getPropertyByName(@NonNull String name) {
		return manager.getPropertiesByName(name, CFG_FILE_NAME);
	}

	/**
	 * 获取classpath的*.properties文件内容
	 *
	 * @param name
	 * @param filename
	 * @return
	 */
	@SneakyThrows
	public static String getPropertyByName(@NonNull String name, @NonNull String filename) {
		return manager.getPropertiesByName(name, filename);
	}

	/**
	 * 获取classpath的*.properties文件内容
	 *
	 * @param name
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	@SneakyThrows
	public static <T> T getPropertyByName(@NonNull String name, @NonNull Class<T> clazz) {
		return (T) manager.getPropertiesByName(name, CFG_FILE_NAME, clazz);
	}

	/**
	 * 获取classpath的*.properties文件内容
	 *
	 * @param name
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	@SneakyThrows
	public static <T> T getPropertyByName(@NonNull String name, @NonNull String filename, @NonNull Class<T> clazz) {
		return (T) manager.getPropertiesByName(name, filename, clazz);
	}

	/**
	 * 获取classpath的*.properties文件内容
	 *
	 * @param filename
	 * @return
	 * @throws IOException
	 * @author wwhhf
	 * @comment 返回配置文件
	 * @since 2016年6月12日
	 */
	@SneakyThrows
	public static Properties getProperties(@NonNull String filename) {
		String path = PropertiesUtil.class.getClassLoader()
				.getResource(filename).getPath();
		@Cleanup FileInputStream in = new FileInputStream(path);
		Properties prop = new Properties();
		prop.load(in);
		return prop;
	}

	/**
	 * 获取commons-configuration工具类实例
	 *
	 * @param filename
	 * @return
	 * @throws ConfigurationException
	 */
	@SneakyThrows
	public static PropertiesConfiguration getPropertiesConfiguration(@NonNull String filename) {
		return new PropertiesConfiguration(filename);
	}

	/**
	 * 本地缓存抽象类
	 * 基于guava
	 */
	private static abstract class ConfigCacheManager {

		/**
		 * 缓存配置信息
		 * 1.基于大小过期（默认）
		 * 2.基于过期时间过期
		 * 3.基于引用过期
		 * guava refresh:重新加载值的时候，其他线程不会阻塞并且直接返回旧值
		 * guava expire：重新加载值的时候，其他线程会阻塞等待完成
		 * 如果缓存属于高吞吐量，不用担心缓存过期刷新的问题，因为写频繁，永远不会过期
		 * guava每次加载数据都会等到下次查询
		 * 如果缓存属于写少读多的场景，最好自定义线程定时刷新，没有锁开销
		 */
		protected final Cache<String, String> cache = CacheBuilder.newBuilder()
				.maximumSize(getMaxxSize())
				.initialCapacity(getInitCapacity())
				.build();

		/**
		 * 读入缓存
		 */
		public ConfigCacheManager() {
			//初始化延迟0ms开始执行，每隔100ms重新执行一次任务
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(
					new Runnable() {
						@Override
						public void run() {
							cleanUp();
						}
					}, 0, getInterval(), TimeUnit.SECONDS);
		}

		/**
		 * 最大缓存数量
		 *
		 * @return
		 */
		public abstract int getMaxxSize();

		/**
		 * 初始容量
		 *
		 * @return
		 */
		public abstract int getInitCapacity();

		/**
		 * 刷新间隔
		 *
		 * @return
		 */
		public abstract long getInterval();

		/**
		 * 获取配置信息
		 *
		 * @param name
		 * @return
		 */
		public abstract String getPropertiesByName(final String name, final String filename);

		/**
		 * 缓存清理
		 */
		public void cleanUp() {
			cache.cleanUp();
		}

		/**
		 * 获取配置信息
		 *
		 * @param name
		 * @param clazz
		 * @return
		 */
		public <T> T getPropertiesByName(@NonNull String name, @NonNull String filename, @NonNull Class<T> clazz) {
			return cache == null ? null : (T) StringUtil.string2BaseType(getPropertiesByName(name, filename), clazz);
		}

	}

	/**
	 * Created by hongfei.whf on 2016/12/17.
	 */
	private static class LocalConfigCacheManager extends ConfigCacheManager {
		@Override
		public int getMaxxSize() {
			return 100;
		}

		@Override
		public int getInitCapacity() {
			return 20;
		}

		@Override
		public long getInterval() {
			return 5L;
		}

		@Override
		public String getPropertiesByName(final String name, final String filename) {
			while (true) {
				try {
					return cache.get(name, new Callable<String>() {

						@Override
						public String call() throws Exception {
							//webapp
							PropertiesConfiguration propertiesConfiguration = PropertiesUtil.
									getPropertiesConfiguration(filename);
							Iterator<String> iterator = propertiesConfiguration.getKeys();
							while (iterator.hasNext()) {
								String key = iterator.next();
								cache.put(key, propertiesConfiguration.getString(key));
							}
							return cache.getIfPresent(name);
						}

					});
				} catch (ExecutionException e) {
					log.error("LocalConfigCacheManager ExecutionException:{},{}", name, filename);
					// 失败重试
				}
			}
		}
	}
}
