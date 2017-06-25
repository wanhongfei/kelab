package org.kelab.vf.kafka.sender.impl;

import lombok.NonNull;
import org.kelab.util.FastJsonUtil;
import org.kelab.vf.kafka.sender.IKafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Created by wanhongfei on 2017/1/10.
 */
public class KafkaSenderImpl implements IKafkaSender {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void send(@NonNull String topic, @NonNull Object... args) {
		for (int i = 0, len = args.length; i < len; i++) {
			kafkaTemplate.send(topic, FastJsonUtil.object2Json(args[i]));
		}
	}

	@Override
	public void send(@NonNull String topic, @NonNull String key, @NonNull Object... args) {
		for (int i = 0, len = args.length; i < len; i++) {
			kafkaTemplate.send(topic, key, FastJsonUtil.object2Json(args[i]));
		}
	}

	@Override
	public void send(@NonNull String topic, int partition, @NonNull Object... args) {
		for (int i = 0, len = args.length; i < len; i++) {
			kafkaTemplate.send(topic, partition, FastJsonUtil.object2Json(args[i]));
		}
	}
}
