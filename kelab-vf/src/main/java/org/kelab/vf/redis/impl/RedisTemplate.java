package org.kelab.vf.redis.impl;

import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.kelab.util.*;
import org.kelab.util.constant.CharsetConstant;
import org.kelab.vf.redis.IRedisTemplate;
import org.kelab.vf.zk.IZookeeperTemplate;
import org.kelab.vf.zk.subscriber.BaseSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.*;

import java.util.*;

/**
 * Created by wanhongfei on 2016/12/27.
 */
@Slf4j
public class RedisTemplate extends IRedisTemplate {

	@Autowired
	private ShardedJedisPool shardedJedisPool;

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private IZookeeperTemplate iZookeeperTemplate;

	@Setter
	private String zknode_is_enable;

	/**
	 * 初始化函数
	 * 注册zk监听
	 */
	public void init() {
		String value = null;
		try {
			value = iZookeeperTemplate.addSubscriber(new BaseSubscriber() {

				@Override
				public String subscribe() {
					return zknode_is_enable;
				}

				@Override
				public void handleEvent(WatchedEvent event, String value) {
					if (!StringUtil.isBlank(value)) {
						enable = Boolean.valueOf(value);
						log.error("=============> redisIsEnable change:{}", enable);
						if (PropertiesUtil.getPropertyByName(ConsoleUtil.SHOW_STARTUP_DETAIL, Boolean.class)) {
							ConsoleUtil.println("当前redis开启情况发生改变：%s", enable);
						}
					}
				}
			});
		} catch (Exception ex) {
			log.error("zookeeper error:{}", ex.getMessage());
		}
		if (value != null) {
			enable = Boolean.valueOf(value);
		} else {
			enable = PropertiesUtil.getPropertyByName(ConsoleUtil.SHOW_STARTUP_DETAIL, Boolean.class);
			log.error("zookeeper 不存在 redis 缓存开关！");
		}
		log.info("当前redis开启情况：{}.", enable);
		ConsoleUtil.println("当前redis开启情况：%s", enable);
	}

	/**
	 * 获取分布式资源
	 *
	 * @return
	 */
	public ShardedJedis getShardedResource() {
		return this.shardedJedisPool.getResource();
	}

	/**
	 * 释放分布式资源
	 *
	 * @return
	 */
	public void releaseShardedResource(@NonNull ShardedJedis shardedJedis) {
		shardedJedis.close();
	}

	/**
	 * 获取资源
	 *
	 * @return
	 */
	public Jedis getResource() {
		return this.jedisPool.getResource();
	}

	/**
	 * 释放资源
	 *
	 * @return
	 */
	public void releaseResource(@NonNull Jedis jedis) {
		jedis.close();
	}

	@Override
	public String get(@NonNull String key) {
		String json = null;
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			json = shardedJedis.get(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return json == null ? null : FastJsonUtil.json2Object(json, String.class);
	}

	@Override
	@SneakyThrows
	public Map<String, String> mget(@NonNull String... keys) {
		Map<String, String> res = new HashMap<>();
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			ShardedJedisPipeline pipe = shardedJedis.pipelined();
			for (String key : keys) {
				pipe.get(key.getBytes(CharsetConstant.UTF_8));
			}
			List<Object> values = pipe.syncAndReturnAll();
			for (int i = 0, len = keys.length; i < len; i++) {
				if (values.get(i) == null) continue;
				byte[] tmp = (byte[]) values.get(i);
				res.put(keys[i], FastJsonUtil.json2Object(new String(tmp, CharsetConstant.UTF_8), String.class));
			}
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public IRedisTemplate set(@NonNull String key, Object obj) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.set(key, FastJsonUtil.object2Json(obj));
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate mset(@NonNull Map<String, Object> keyAndValues) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			ShardedJedisPipeline pipe = shardedJedis.pipelined();
			for (Map.Entry<String, Object> entry : keyAndValues.entrySet()) {
				pipe.set(entry.getKey(), FastJsonUtil.object2Json(entry.getValue()));
			}
			pipe.sync();
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate set(@NonNull String key, @NonNull Object obj, int second) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			ShardedJedisPipeline pipe = shardedJedis.pipelined();
			pipe.set(key, FastJsonUtil.object2Json(obj));
			pipe.expire(key, second);
			pipe.sync();
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate delete(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.del(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate incrBy(@NonNull String key, int step) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.incrBy(key, step);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate decrBy(@NonNull String key, int step) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.decrBy(key, step);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate incr(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.incr(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate decr(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.decr(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate expire(@NonNull String key, int second) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.expire(key, second);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate expireAt(@NonNull String key, long timestamp) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.expireAt(key, timestamp);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate append(@NonNull String key, @NonNull String s) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.append(key, s);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public boolean exist(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		boolean res = false;
		try {
			res = shardedJedis.exists(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public IRedisTemplate rpush(@NonNull String key, @NonNull Object... values) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			List<String> list = FastJsonUtil.list2JsonList(CollectionUtil.array2List(values));
			shardedJedis.rpush(key, CollectionUtil.list2Array(list, String.class));
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate lpush(@NonNull String key, @NonNull Object... values) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			List<String> list = FastJsonUtil.list2JsonList(CollectionUtil.array2List(values));
			shardedJedis.lpush(key, CollectionUtil.list2Array(list, String.class));
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate lset(@NonNull String key, @NonNull Object value, int index) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.lset(key, index, FastJsonUtil.object2Json(value));
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public String lindex(@NonNull String key, int index) {
		ShardedJedis shardedJedis = this.getShardedResource();
		String json = null;
		try {
			json = shardedJedis.lindex(key, index);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return json == null ? null : FastJsonUtil.json2Object(json, String.class);
	}

	@Override
	public String rpop(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		String res = null;
		try {
			res = shardedJedis.rpop(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public String lpop(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		String res = null;
		try {
			res = shardedJedis.lpop(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public long llen(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		long res = 0;
		try {
			res = shardedJedis.llen(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public List<String> lrange(@NonNull String key, int from, int to) {
		List<String> res = new ArrayList<>();
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			List<String> values = shardedJedis.lrange(key, from, to);
			if (values == null) return CollectionUtil.emptyList;
			for (String value : values) {
				res.add(FastJsonUtil.json2Object(value, String.class));
			}
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public IRedisTemplate sadd(@NonNull String key, @NonNull Object... values) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			List<String> list = FastJsonUtil.list2JsonList(CollectionUtil.array2List(values));
			shardedJedis.sadd(key, CollectionUtil.list2Array(list, String.class));
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public Set<String> smembers(@NonNull String key) {
		Set<String> res = new HashSet<>();
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			Set<String> values = shardedJedis.smembers(key);
			if (values == null) return CollectionUtil.emptySet;
			for (String value : values) {
				res.add(FastJsonUtil.json2Object(value, String.class));
			}
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public IRedisTemplate hset(@NonNull String key, @NonNull String field, @NonNull Object value) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.hset(key, field, FastJsonUtil.object2Json(value));
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate hmset(@NonNull String key, @NonNull Map<String, Object> fieldAndValues) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.hmset(key, FastJsonUtil.map2JsonMap(fieldAndValues));
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public String hget(@NonNull String key, @NonNull String field) {
		ShardedJedis shardedJedis = this.getShardedResource();
		String json = null;
		try {
			json = shardedJedis.hget(key, field);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return json == null ? null : FastJsonUtil.json2Object(json, String.class);
	}

	@Override
	public Map<String, String> hmget(@NonNull String key, @NonNull String... fields) {
		Map<String, String> res = new HashMap<>();
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			List<String> values = shardedJedis.hmget(key, fields);
			for (int i = 0, len = fields.length; i < len; i++) {
				res.put(fields[i], FastJsonUtil.json2Object(values.get(i), String.class));
			}
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public boolean hexist(@NonNull String key, @NonNull String field) {
		ShardedJedis shardedJedis = this.getShardedResource();
		boolean res = true;
		try {
			res = shardedJedis.hexists(key, field);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return res;
	}

	@Override
	public IRedisTemplate zadd(@NonNull String key, double score, @NonNull String member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.zadd(key, score, member);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate zadd(@NonNull String key, @NonNull Map<String, Double> scoreAndMembers) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.zadd(key, scoreAndMembers);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public double zscore(@NonNull String key, @NonNull String member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			return shardedJedis.zscore(key, member);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public IRedisTemplate zincrby(@NonNull String key, double incrScore, @NonNull String member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.zincrby(key, incrScore, member);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate zset(String key, double score, String member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			double currScore = shardedJedis.zscore(key, member);
			shardedJedis.zincrby(key, score - currScore, member);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate zrem(@NonNull String key, @NonNull String... member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.zrem(key, member);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate zrem(@NonNull String key, long start, long stop) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.zremrangeByRank(key, start, stop);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public IRedisTemplate zrem(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			long total = shardedJedis.zcard(key);
			shardedJedis.zremrangeByRank(key, 0, total - 1);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
		return this;
	}

	@Override
	public long zcard(@NonNull String key) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			return shardedJedis.zcard(key);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public long zrank(@NonNull String key, @NonNull String member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			return shardedJedis.zrank(key, member) + 1;
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public Map<String, Long> zrank(@NonNull String key, @NonNull List<String> member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		ShardedJedisPipeline pipeline = shardedJedis.pipelined();
		try {
			Map<String, Long> res = new HashMap<>();
			for (String s : member) {
				pipeline.zrank(key, s);
			}
			List<Object> data = pipeline.syncAndReturnAll();
			for (int i = 0, len = member.size(); i < len; i++) {
				Object obj = data.get(i);
				if (obj == null) {
					res.put(member.get(i), null);
				} else {
					res.put(member.get(i), Long.parseLong(obj.toString()) + 1);
				}
			}
			return res;
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public long zrevrank(String key, String member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			return shardedJedis.zrevrank(key, member) + 1;
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public Map<String, Long> zrevrank(String key, List<String> member) {
		ShardedJedis shardedJedis = this.getShardedResource();
		ShardedJedisPipeline pipeline = shardedJedis.pipelined();
		try {
			Map<String, Long> res = new HashMap<>();
			for (String s : member) {
				pipeline.zrevrank(key, s);
			}
			List<Object> data = pipeline.syncAndReturnAll();
			for (int i = 0, len = member.size(); i < len; i++) {
				Object obj = data.get(i);
				if (obj == null) {
					res.put(member.get(i), null);
				} else {
					res.put(member.get(i), Long.parseLong(obj.toString()) + 1);
				}
			}
			return res;
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public List<String> zrange(@NonNull String key, long start, long stop) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			Set<String> set = shardedJedis.zrange(key, start, stop);
			if (CollectionUtil.isEmpty(set)) {
				return CollectionUtil.emptyList;
			}
			return new ArrayList<>(set);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public List<Tuple> zrangeWithScores(@NonNull String key, long start, long stop) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			Set<Tuple> set = shardedJedis.zrangeWithScores(key, start, stop);
			if (CollectionUtil.isEmpty(set)) {
				return CollectionUtil.emptyList;
			}
			return new ArrayList<>(set);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public List<String> zrevrange(@NonNull String key, long start, long stop) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			Set<String> set = shardedJedis.zrevrange(key, start, stop);
			if (CollectionUtil.isEmpty(set)) {
				return CollectionUtil.emptyList;
			}
			return new ArrayList<>(set);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public List<Tuple> zrevrangeWithScores(@NonNull String key, long start, long stop) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			Set<Tuple> set = shardedJedis.zrevrangeWithScores(key, start, stop);
			if (CollectionUtil.isEmpty(set)) {
				return CollectionUtil.emptyList;
			}
			return new ArrayList<>(set);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public IRedisTemplate zremrangeByScore(String key, double start, double end) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			shardedJedis.zremrangeByScore(key, start, end);
			return this;
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

	@Override
	public long zcount(String key, double start, double end) {
		ShardedJedis shardedJedis = this.getShardedResource();
		try {
			return shardedJedis.zcount(key, start, end);
		} finally {
			this.releaseShardedResource(shardedJedis);
		}
	}

}
