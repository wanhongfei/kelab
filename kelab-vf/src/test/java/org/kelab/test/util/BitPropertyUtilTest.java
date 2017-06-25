package org.kelab.test.util;

import org.junit.Test;
import org.kelab.util.BitPropertyUtil;

/**
 * Created by hongfei.whf on 2017/2/11.
 */
public class BitPropertyUtilTest {

	public static final long pro1 = 1 << 31;
	public static final long pro2 = 1 << 11;
	public static final long pro3 = 1 << 12;

	@Test
	public void test() {
		System.out.println("pro1:" + BitPropertyUtil.long2SBinaryString(pro1));
		System.out.println("pro2:" + BitPropertyUtil.long2SBinaryString(pro2));
		System.out.println("pro3:" + BitPropertyUtil.long2SBinaryString(pro3));
		long prop = BitPropertyUtil.DEFAULT_INIT_LONG_VALUE;
		System.out.println("===============");
		System.out.println(BitPropertyUtil.long2SBinaryString(prop));
		prop = BitPropertyUtil.addProps(prop, pro1, pro2, pro3);
		System.out.println(BitPropertyUtil.long2SBinaryString(prop));
		System.out.println("===============");
		System.out.println(BitPropertyUtil.isContain(prop, pro1));
		System.out.println(BitPropertyUtil.isContain(prop, pro2));
		System.out.println(BitPropertyUtil.isContain(prop, pro3));
		System.out.println("===============");
		prop = BitPropertyUtil.removeProps(prop, pro2);
		System.out.println(BitPropertyUtil.isContain(prop, pro1));
		System.out.println(BitPropertyUtil.isContain(prop, pro2));
		System.out.println(BitPropertyUtil.isContain(prop, pro3));
		System.out.println("===============");
		prop = BitPropertyUtil.removeProps(prop, pro3);
		System.out.println(BitPropertyUtil.isContain(prop, pro1));
		System.out.println(BitPropertyUtil.isContain(prop, pro2));
		System.out.println(BitPropertyUtil.isContain(prop, pro3));
		System.out.println("===============");
		prop = BitPropertyUtil.removeProps(prop, pro1);
		System.out.println(BitPropertyUtil.isContain(prop, pro1));
		System.out.println(BitPropertyUtil.isContain(prop, pro2));
		System.out.println(BitPropertyUtil.isContain(prop, pro3));
		System.out.println("===============");
	}

}
