package org.kelab.test;

import org.junit.Test;
import org.kelab.vf.generator.MapperGenerator;
import org.kelab.vf.junit.JunitBaseServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by hongfei.whf on 2016/11/26.
 */
@ContextConfiguration(locations = {"classpath*:dispatcher-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
public class MapperGeneratorClient extends JunitBaseServiceDao {

	@Autowired
	private MapperGenerator mapperGenerator;

	@Test
	public void run() {
		mapperGenerator.run();
	}
}
