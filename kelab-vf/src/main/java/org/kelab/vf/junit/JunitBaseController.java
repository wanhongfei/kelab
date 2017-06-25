package org.kelab.vf.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class JunitBaseController<T> {

	@Autowired
	protected T controller;

	protected MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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
