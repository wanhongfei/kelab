package org.kelab.vf.zk.lock;

/**
 * Created by hongfei.whf on 2017/3/23.
 */
public interface IDistributedLock {

	void lock();

	void unlock();

	void releaseZk();
}
