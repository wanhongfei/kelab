package org.kelab.test.util;

import org.junit.Test;
import org.kelab.vf.junit.JunitBaseServiceDao;
import org.kelab.vf.redis.IRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanhongfei on 2016/12/27.
 */
@ContextConfiguration(locations = {"classpath*:dispatcher-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
public class RedisTest extends JunitBaseServiceDao {

	private static final String KEY = "org/kelab/test";
	@Autowired
	private ShardedJedisPool shardedJedisPool;
	@Autowired
	private IRedisTemplate iRedisTemplate;

	@Test
	public void sortsetTest() {
//		System.out.println(KEY);
		iRedisTemplate.zadd(KEY, 10.0, "java");
		iRedisTemplate.zadd(KEY, 8.0, "python");
		Map<String, Double> members = new HashMap<>();
		members.put("a", 1.0);
		members.put("b", 3.0);
		members.put("c", 2.0);
		iRedisTemplate.zadd(KEY, members);
		iRedisTemplate.zset(KEY, 99.0, "c");
//		//total
//		System.out.println(iRedisTemplate.zcard(KEY));
//		// page
//		System.out.println(iRedisTemplate.zrange(KEY, 0, 10));
//		System.out.println(iRedisTemplate.zrank(KEY,
//				CollectionUtil.string2StrList("java,python,haha", ",")));
//		System.out.println(iRedisTemplate.zrevrank(KEY,
//				CollectionUtil.string2StrList("java,python,haha", ",")));
//		// index
//		System.out.println(iRedisTemplate.zrank(KEY, "java"));
//		// score
//		System.out.println(iRedisTemplate.zscore(KEY, "python"));
//		// incr
//		iRedisTemplate.zincrby(KEY, 2.1, "python");
//		System.out.println(iRedisTemplate.zscore(KEY, "python"));
//		System.out.println(iRedisTemplate.zrank(KEY, "python"));
		// page
		System.out.println(iRedisTemplate.zrange(KEY, 0, 3));
		// rem
//		iRedisTemplate.zrem(KEY, "b");
//		System.out.println(iRedisTemplate.zrevrange(KEY, 0, 10));
		iRedisTemplate.zrem(KEY);
		System.out.println(iRedisTemplate.zrange(KEY, 0, 3));
	}

	@Test
	public void test() throws NoSuchFieldException, IllegalAccessException, IOException {
//		Map<String, Object> map = new HashMap<>();
//		SiteSettings siteSettings = new SiteSettings(3, "RESET_PWD_MAIL_EXP", "15");
//		map.put("hello", siteSettings);
//		iRedisTemplate.set("hi", map);
//		Map<String, SiteSettings> map1 = iRedisTemplate.getMap("hi", SiteSettings.class);
//		System.out.println(map1);
		//		iRedisTemplate.rpush("mylist", 1, 2, 3, 4, 5, 6);
//		iRedisTemplate.lpush("mylist", "a", "b", "c", "d");
//		long len = 0;
//		System.out.println(len = iRedisTemplate.llen("mylist"));
//		System.out.println(iRedisTemplate.lrange("mylist", 0, Integer.valueOf(len - 1 + "")));
//		System.out.println(iRedisTemplate.rpop("mylist", Integer.class));

//		this.iRedisTemplate.llen(CacheConstant.JUDGE_QUEUE_KEY);
//		this.iRedisTemplate.delete(CacheConstant.JUDGE_QUEUE_KEY);
//		this.iRedisTemplate.rpush("list",new JudgeTask(1, 2, 3, "", 1, 1));
//		iRedisTemplate.set("hello", "hi");
//		System.out.println(iRedisTemplate.get("hello"));
//		Tags tags = new Tags("haha");
//		iRedisTemplate.set("hello", tags);
//		System.out.println(iRedisTemplate.getObject("hello", Tags.class));
//		Map<String,Object> map = BeanUtil.bean2Map(tags);
//		List<Tags> tagsList = new ArrayList<>();
//		tagsList.add(tags);
//		System.out.println(FastJsonUtil.object2Json(tagsList, true));
//		Map<String, Object> map = new HashMap<>();
//		map.put("hello", "1");
//		map.put("hi", 2);
//		Map<String, Class> map1 = new HashMap<>();
//		map1.put("hello1", String.class);
//		map1.put("hi", Integer.class);
//		ShardedJedisPipeline pipeline = iRedisTemplate.pipeline();
//		pipeline.set("hello", "1");
//		pipeline.set("hi", FastJsonUtil.object2Json(Integer.valueOf(2)));
//		pipeline.sync();
//		String uri = PropertiesUtil.getPropertyByName("redis.uri");
//		Jedis jedis = new Jedis(uri);
//		Transaction transaction = iRedisTemplate.watchAndTransaction(jedis, "hello1", "hello", "hi");
//		transaction.set("hello1", "1");
//		transaction.set("hi", FastJsonUtil.object2Json(1));
//		transaction.get("hello");
//		System.out.println(iRedisTemplate.execTransactionAndUnWatch(jedis, transaction));
//		System.out.println(iRedisTemplate.mget(map1));
//		iRedisTemplate.delete("mylist");
//		iRedisTemplate.rpush("mylist", 1, 2, 3, 4, 5, 6);
//		iRedisTemplate.lpush("mylist", "a", "b", "c", "d");
//		long len = 0;
//		System.out.println(len = iRedisTemplate.llen("mylist"));
//		System.out.println(iRedisTemplate.lrange("mylist", 0, Integer.valueOf(len - 1 + "")));
//		System.out.println(iRedisTemplate.rpop("mylist", Integer.class));
//		iRedisTemplate.sadd("myset", 1, 1, 2, 3, "4", 5, 6);
//		System.out.println(iRedisTemplate.smembers("myset"));
//		iRedisTemplate.hset("myhash", "a", "name");
//		System.out.println(iRedisTemplate.hget("myhash", "a"));
//		System.out.println(iRedisTemplate.hexist("myhash", "a"));
	}

}
