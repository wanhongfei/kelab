package org.kelab.vf.zk;

import org.apache.zookeeper.CreateMode;
import org.kelab.vf.zk.subscriber.BaseSubscriber;

import java.util.List;

/**
 * Created by hongfei.whf on 2017/3/23.
 */
public interface IZookeeperTemplate {

	/**
	 * 初始化
	 */
	void init();

	/**
	 * 销毁
	 */
	void close();

	/**
	 * 获取节点data并且继续监听
	 *
	 * @param zknode
	 * @return
	 */
	String get(String zknode);

	/**
	 * 获取节点data
	 *
	 * @param zknode
	 * @param watch
	 * @return
	 */
	String get(String zknode, boolean watch);

	/**
	 * 节点是否存在
	 *
	 * @param zknode
	 * @return
	 */
	boolean isExist(String zknode);

	/**
	 * 删除节点
	 *
	 * @param zknode
	 */
	void delete(String zknode);

	/**
	 * 节点绑定内容
	 *
	 * @param zknode
	 * @param data
	 */
	void setData(String zknode, String data);

	/**
	 * 创造节点
	 *
	 * @param zknodes
	 * @param createMode
	 */
	void createNode(String zknodes, CreateMode createMode);

	/**
	 * 监听节点
	 *
	 * @param subscriber
	 * @return
	 */
	String addSubscriber(BaseSubscriber subscriber);

	/**
	 * 列出该节点下的子节点
	 *
	 * @param zknode
	 * @return
	 */
	List<String> list(String zknode);
}
