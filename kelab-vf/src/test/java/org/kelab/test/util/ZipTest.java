package org.kelab.test.util;

import org.kelab.util.ZipUtil;

/**
 * Created by hongfei.whf on 2016/9/1.
 */
public class ZipTest {

	public static void main(String args[]) throws Exception {
		ZipUtil.zip("C:\\Users\\hongfei.whf\\Desktop\\算法分析",
				"C:\\Users\\hongfei.whf\\Desktop\\算法分析.zip");
		ZipUtil.unzip("C:\\Users\\hongfei.whf\\Desktop\\算法分析.zip",
				"C:\\Users\\hongfei.whf\\Desktop\\算法分析1");
	}

}
