package org.kelab.vf.kafka.sender;

/**
 * Created by wanhongfei on 2017/1/10.
 */
public interface IKafkaSender {

	/**
	 * 发送消息
	 *
	 * @param topic
	 * @param args
	 */
	void send(String topic, Object... args);

	/**
	 * 发送消息
	 *
	 * @param topic
	 * @param key
	 * @param args
	 */
	void send(String topic, String key, Object... args);

	/**
	 * 发送消息
	 *
	 * @param topic
	 * @param partition
	 * @param args
	 */
	void send(String topic, int partition, Object... args);
}
