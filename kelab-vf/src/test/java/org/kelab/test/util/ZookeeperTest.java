package org.kelab.test.util;

import lombok.SneakyThrows;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;
import org.kelab.vf.junit.JunitBaseServiceDao;
import org.kelab.vf.zk.lock.impl.DistributedLock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.IOException;

/**
 * Created by wanhongfei on 2017/1/11.
 */
@ContextConfiguration(locations = {"classpath*:dispatcher-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
public class ZookeeperTest extends JunitBaseServiceDao {
//
//	@Autowired
//	private IZookeeperTemplate iZooKeeperTemplate;
//
//	private volatile boolean redisIsEnable = false;

	@Test
	public void setTest() throws ConfigurationException, IOException, InterruptedException {
//		redisIsEnable = Boolean.valueOf(iZooKeeperTemplate.addSubscriber(new BaseSubscriber() {
//
//			@Override
//			public String subscribe() {
//				return "/util/config/redis.isEnable";
//			}
//
//			@Override
//			public void handleEvent(WatchedEvent event, String value) {
//				redisIsEnable = Boolean.valueOf(value);
//				System.out.println("redisIsEnable change:" + redisIsEnable);
//			}
//		}));
//		System.out.println(redisIsEnable);
		DistributedLock lock1 = new DistributedLock("1", "/locks", "locka");
		lock1.lock();
		System.out.println("Thread1 is running.");
		Thread.sleep(50000L);
		lock1.unlock();
		lock1.releaseZk();
	}

	@Test
	@SneakyThrows
	public void test2() {
		DistributedLock lock2 = new DistributedLock("2", "/locks", "locka");
		lock2.lock();
		System.out.println("thread2 is running");
		Thread.sleep(10000L);
		lock2.unlock();
		lock2.releaseZk();
	}

}
