package org.kelab.vf.kafka.listener.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * Created by wanhongfei on 2017/1/10.
 */
@Slf4j
public class CustomMessageListener implements MessageListener<String, String> {

	/**
	 * 监听器自动执行该方法
	 * 消费消息
	 * 自动提交offset
	 * 执行业务代码
	 * （high level api 不提供offset管理，不能指定offset进行消费）
	 */
	@Override
	public void onMessage(ConsumerRecord<String, String> consumerRecord) {
		log.info("============= kafkaConsumer开始消费 =============");
		String topic = consumerRecord.topic();
		String key = consumerRecord.key();
		String value = consumerRecord.value();
		long offset = consumerRecord.offset();
		int partition = consumerRecord.partition();
		log.info("------------- topic:" + topic);
		log.info("------------- value:" + value);
		log.info("------------- key:" + key);
		log.info("------------- offset:" + offset);
		log.info("------------- partition:" + partition);
		log.info("============= kafkaConsumer消费结束 =============");
	}
}
