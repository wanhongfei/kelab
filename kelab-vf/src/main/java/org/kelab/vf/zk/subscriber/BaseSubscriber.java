package org.kelab.vf.zk.subscriber;

import org.apache.zookeeper.WatchedEvent;

/**
 * Created by wanhongfei on 2017/1/18.
 */
public abstract class BaseSubscriber {

	/**
	 * 订阅的路径zknode+配置项名称
	 *
	 * @return
	 */
	public String subscribeKey() {
		return subscribe();
	}

	/**
	 * 配置项的名称
	 *
	 * @return
	 */
	public abstract String subscribe();

	/**
	 * 订阅的key出现更新后的推送
	 *
	 * @param event
	 */
	public void proccess(WatchedEvent event, String value) {
		handleEvent(event, value);
	}

	/**
	 * 处理event
	 *
	 * @param event
	 * @param value
	 */
	public abstract void handleEvent(WatchedEvent event, String value);

}
