package org.kelab.vf.zk.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.kelab.util.PropertiesUtil;
import org.kelab.util.StringUtil;
import org.kelab.util.constant.CharsetConstant;
import org.kelab.util.constant.SeparatorConstant;
import org.kelab.vf.zk.IZookeeperTemplate;
import org.kelab.vf.zk.constant.ZooKeeperConstant;
import org.kelab.vf.zk.subscriber.BaseSubscriber;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanhongfei on 2017/1/11.
 * 当客户端断开后，客户端无法得到SESSION_EXPIRED通知，
 * 因为链接断了怎么发送消息？而自动重连是ZK client library自己进行的。
 * 换言之客户端是无法知道自己是否失效的，SESSION_EXPIRED通知只有当重连成功后才能收到。
 */
@Slf4j
public class ZooKeeperTemplate implements IZookeeperTemplate {

	/**
	 * 订阅者集合
	 */
	public static final Map<String/*key*/, List<BaseSubscriber>> subscriberMap = new ConcurrentHashMap<>();
	private static final String ZOOKEEPER_URI = PropertiesUtil.getPropertyByName(ZooKeeperConstant.ZK_URI_CONFIG);
	private static final int ZOOKEEPER_SESSION_TIMEOUT = PropertiesUtil
			.getPropertyByName(ZooKeeperConstant.ZK_SESSION_TIMEOUT_CONFIG, Integer.class);
	private static volatile ZooKeeper zooKeeper = null;
	private static volatile CountDownLatch latch = null;

	@Override
	@SneakyThrows
	public void init() {
		// 第一次连接zookeeper
		if (zooKeeper == null) {
			synchronized (ZooKeeperTemplate.class) {
				if (zooKeeper == null) {
					latch = new CountDownLatch(1);
					// 若超时客户端自动重连
					zooKeeper = new ZooKeeper(
							ZOOKEEPER_URI,
							ZOOKEEPER_SESSION_TIMEOUT,
							new ZooKeeperWatcher());
					// 等待连接建立成功
					latch.await(30, TimeUnit.SECONDS);
					latch = null;
				}
			}
		}
	}

	@Override
	@SneakyThrows
	public void close() {
		if (zooKeeper != null) {
			zooKeeper.close();
			zooKeeper = null;
		}
	}

	@Override
	@SneakyThrows
	public String get(@NonNull String zknode) {
		// 继续监听
		return new String(zooKeeper.getData(zknode, true, null), CharsetConstant.UTF_8);
	}

	@Override
	@SneakyThrows
	public String get(@NonNull String zknode, boolean watch) {
		return new String(zooKeeper.getData(zknode, watch, null), CharsetConstant.UTF_8);
	}

	@Override
	@SneakyThrows
	public boolean isExist(@NonNull String zknode) {
		return zooKeeper.exists(zknode, true) != null;
	}

	@Override
	@SneakyThrows
	public void delete(@NonNull String zknode) {
		Stat stat = zooKeeper.exists(zknode, true);
		if (stat != null) {
			zooKeeper.delete(zknode, stat.getVersion());
		}
	}

	@Override
	@SneakyThrows
	public void setData(@NonNull String zknode, @NonNull String data) {
		// 更新节点字段
		Stat stat = zooKeeper.exists(zknode, true);
		if (stat == null) {
			throw new Exception("zknode is not exist! - " + zknode);
		} else {
			zooKeeper.setData(zknode, data.getBytes(CharsetConstant.UTF_8), stat.getVersion());
		}
	}

	@Override
	@SneakyThrows
	public void createNode(@NonNull String zknodes, @NonNull CreateMode createMode) {
		String[] nodes = zknodes.split(SeparatorConstant.BACK_SLANT);
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = nodes.length; i < len; i++) {
			if (StringUtil.isBlank(nodes[i])) continue;
			sb.append(SeparatorConstant.BACK_SLANT).append(nodes[i]);
			Stat stat = zooKeeper.exists(sb.toString(), true);
			if (stat == null) {
				zooKeeper.create(sb.toString(), null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
						createMode);
				log.info(String.format("正在创建zknode:%s", sb.toString()));
			} else {
				log.info(String.format("zknode:%s已存在", sb.toString()));
			}
		}
	}

	@Override
	public String addSubscriber(@NonNull BaseSubscriber subscriber) {
		synchronized (this) {
			// 加入订阅器中
			String key = subscriber.subscribeKey();
			if (subscriberMap.containsKey(key)) {
				subscriberMap.get(key).add(subscriber);
			} else {
				ArrayList<BaseSubscriber> subscribers = new ArrayList<>();
				subscribers.add(subscriber);
				subscriberMap.put(key, subscribers);
			}
			return this.get(key);
		}
	}

	@Override
	@SneakyThrows
	public List<String> list(String zknode) {
		return zooKeeper.getChildren(zknode, false);
	}

	/**
	 * zookeeperWatcher
	 */
	public class ZooKeeperWatcher implements Watcher {

		/**
		 * zookeeper是一次监听，要实现永久监听需要在process函数重新设置watcher
		 *
		 * @param watchedEvent
		 */
		@Override
		@SneakyThrows
		public void process(WatchedEvent watchedEvent) {
			log.info("===================");
			log.info("time:{}", Calendar.getInstance().getTime());
			log.info("status:{}", watchedEvent.getState());
			log.info("===================");
			if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
				// 阻塞等待连接成功
				if (latch != null) {
					latch.countDown();
				}
			}
			// 分发消息
			String key = watchedEvent.getPath();
			if (StringUtil.isNotBlank(key)) {
				List<BaseSubscriber> subscribers = subscriberMap.get(key);
				if (subscribers == null) return;
				for (BaseSubscriber subscriber : subscribers) {
					if (key.equals(subscriber.subscribeKey())) {
						subscriber.proccess(watchedEvent,
								// 重新设置监听器
								new String(zooKeeper.getData(key, true, null), CharsetConstant.UTF_8));
					}
				}
			}
		}
	}

}
