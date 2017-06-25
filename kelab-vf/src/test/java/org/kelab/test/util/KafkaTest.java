package org.kelab.test.util;

import org.junit.Test;
import org.kelab.vf.junit.JunitBaseServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by wanhongfei on 2016/12/27.
 */
@ContextConfiguration(locations = {"classpath*:dispatcher-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
public class KafkaTest extends JunitBaseServiceDao {

	@Autowired
	private KafkaTemplate<String, String> myTopicKafkaTemplate;

	@Test
	public void test() {
		for (int i = 0; i < 15; i++) {
			this.myTopicKafkaTemplate.send("oj3-judge", "this is my test3");
		}
	}

}
