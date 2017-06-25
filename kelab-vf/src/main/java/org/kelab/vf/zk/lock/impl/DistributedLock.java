package org.kelab.vf.zk.lock.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.kelab.util.CollectionUtil;
import org.kelab.util.PropertiesUtil;
import org.kelab.util.StringUtil;
import org.kelab.vf.zk.constant.ZooKeeperConstant;
import org.kelab.vf.zk.lock.IDistributedLock;
import org.quartz.impl.jdbcjobstore.LockException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by hongfei.whf on 2017/3/23.
 */
@Slf4j
public class DistributedLock implements IDistributedLock, Watcher {

	private static final String ZOOKEEPER_URI = PropertiesUtil.getPropertyByName(ZooKeeperConstant.ZK_URI_CONFIG);
	private static final int ZOOKEEPER_SESSION_TIMEOUT = PropertiesUtil
			.getPropertyByName(ZooKeeperConstant.ZK_SESSION_TIMEOUT_CONFIG, Integer.class);

	// defined lock meta
	private ZooKeeper zooKeeper = null;
	private CountDownLatch syncConnectedLatch = null;
	private CountDownLatch acquiredLockLatch = null;
	private String zknode = null;
	private String lockName = null;
	private String waitLock = null;
	private String currLock = null;
	private String lockId = null;

	/**
	 * 构造函数
	 *
	 * @param zknode
	 * @param lockName
	 */
	@SneakyThrows
	public DistributedLock(String lockId, String zknode, String lockName) {
		this.lockId = lockId;
		this.zknode = zknode;
		this.lockName = lockName;
		this.syncConnectedLatch = new CountDownLatch(1);
		// 若超时客户端自动重连
		this.zooKeeper = new ZooKeeper(ZOOKEEPER_URI, ZOOKEEPER_SESSION_TIMEOUT, this);
		// 等待连接建立成功
		this.syncConnectedLatch.await(30, TimeUnit.SECONDS);
		this.syncConnectedLatch = null;
	}

	@Override
	@SneakyThrows
	public void lock() {
		try {
			// check zknode is exist
			// single zknode, not support mulit zknode
			if (this.zooKeeper.exists(this.zknode, false) == null) {
				this.zooKeeper.create(this.zknode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
				log.error("{} has been create.", this.zknode);
			}
			// create lock zknode
			// for example: /locks/locka0000000000
			this.currLock = this.zooKeeper.create(this.zknode + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			log.info("{} currlock is {}", lockId, this.currLock);
			// get all child node of this zknode
			List<String> children = this.zooKeeper.getChildren(this.zknode, false);
			List<String> locks = new ArrayList<>();
			for (String child : children) {
				// for example: locka0000000000
				if (StringUtil.isNotBlank(child) && child.startsWith(this.lockName)) {
					locks.add(this.zknode + "/" + child);
				}
			}
			// sort the list: asc
			Collections.sort(locks);
			log.info("{}-{} children are {}", lockId, zknode, locks);
			// pending lock
			if (this.currLock.equals(locks.get(0))) {
				// 如果他是最小的，代表其获取到锁
				log.info("{} acquired lock.", lockId);
			} else {
				this.waitLock = locks.get(CollectionUtil.binarySearch(locks, this.currLock, String.class) - 1);
				//	监听wait
				zooKeeper.getData(this.waitLock, true, null);
				log.info("{} waitLock is {}", lockId, waitLock);
				this.acquiredLockLatch = new CountDownLatch(1);
				log.info("{} is waiting.", lockId);
				this.acquiredLockLatch.await();
				// 已经获取到锁
				this.acquiredLockLatch = null;
			}
		} catch (Exception ex) {
			throw new LockException(ex.getMessage());
		}
	}

	@Override
	@SneakyThrows
	public void unlock() {
		if (StringUtil.isNotBlank(this.currLock) && this.zooKeeper != null) {
			this.zooKeeper.delete(this.currLock, -1);
			this.currLock = null;
			this.waitLock = null;
		}
	}

	@Override
	@SneakyThrows
	public void releaseZk() {
		zooKeeper.close();
		zooKeeper = null;
	}

	/**
	 * 监听连接成功
	 *
	 * @param watchedEvent
	 */
	@Override
	@SneakyThrows
	public void process(WatchedEvent watchedEvent) {
		if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
			if (this.syncConnectedLatch != null) {
				this.syncConnectedLatch.countDown();
			}
		}
		if (watchedEvent.getType() == Event.EventType.NodeDeleted
				&& watchedEvent.getPath().equals(this.waitLock)) {
			if (this.acquiredLockLatch != null) {
				this.acquiredLockLatch.countDown();
				log.info("{} acquired lock.", lockId);
			}
		}
		if (StringUtil.isNotBlank(this.waitLock)) {
			//	监听wait
			zooKeeper.getData(this.waitLock, true, null);
		}
	}

}
