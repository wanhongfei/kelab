package org.kelab.vf.kafka.listener.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

/**
 * Created by wanhongfei on 2017/1/9.
 */
@Slf4j
public class CustomKafkaProducerListener implements ProducerListener {

	/**
	 * 发送消息成功后调用
	 */
	@Override
	public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
		log.info("========== kafka 发送数据成功（日志开始）==========");
		log.info("---------- topic:{}", topic);
		log.info("---------- partition:{}", partition);
		log.info("---------- key:{}", key);
		log.info("---------- value:{}", value);
		log.info("---------- RecordMetadata:{}", recordMetadata);
		log.info("========== kafka 发送数据成功（日志结束）==========");
	}

	/**
	 * 发送消息错误后调用
	 */
	@Override
	public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
		log.info("========== kafka 发送数据错误（日志开始）==========");
		log.info("---------- topic:{}", topic);
		log.info("---------- partition{}", partition);
		log.info("---------- key:{}", key);
		log.info("---------- value{}", value);
		log.info("---------- Exception:{}", exception);
		log.info("========== kafka 发送数据错误（日志结束）==========");
	}

	/**
	 * 方法返回值代表是否启动kafkaProducer监听器
	 */
	@Override
	public boolean isInterestedInSuccess() {
		return true;
	}
}
