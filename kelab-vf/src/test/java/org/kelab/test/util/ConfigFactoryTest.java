package org.kelab.test.util;

import org.kelab.util.PropertiesUtil;

import java.io.IOException;

/**
 * Created by hongfei.whf on 2016/8/31.
 */
public class ConfigFactoryTest {

	public static void main(String args[]) throws IOException, InterruptedException {
		while (true) {
			System.out.println(PropertiesUtil.getPropertyByName("cros.AccessControlAllowOrigin"));
			Thread.sleep(10000);
		}
	}
}
