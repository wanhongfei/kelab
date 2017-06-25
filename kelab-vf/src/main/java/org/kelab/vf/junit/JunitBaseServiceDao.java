package org.kelab.vf.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class JunitBaseServiceDao extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Before
	public void setUp() throws Exception {
		System.out.println("========== test start ============");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("========== test end ============");
	}

	@Test
	public void baseTest() {
		assert true : "test error";
	}

}
