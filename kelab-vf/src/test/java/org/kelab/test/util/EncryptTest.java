package org.kelab.test.util;

import org.junit.Test;
import org.kelab.util.EncryptUtil;

public class EncryptTest {

	@Test
	public void test() {
		String password = "19940921";
		for (int i = 0; i < 10; i++) {
			password = EncryptUtil.base64encode(password);
		}
		System.out.println(password);
	}

//	@Test
//	public void test(){
//		System.out.println(EncryptUtil.PBKDF2_SHA256("swustoj3 2016/6/8/14:52", "a12h3esadv3ygsac", 10000));
//	}
//	
//	@Test
//	public void testsha1(){
//		String string=EncryptUtil.SHA1("123456", "123");
//		System.out.println(string);
//	}

	@Test
	public void testsalt() throws Exception {
		System.out.println(EncryptUtil.salt(5));
	}

}
