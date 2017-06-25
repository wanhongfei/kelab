package org.kelab.util;

/**
 * Created by hongfei.whf on 2017/2/11.
 */
public class BitPropertyUtil {

	public static final int DEFAULT_INIT_INT_VALUE = 0;
	public static final long DEFAULT_INIT_LONG_VALUE = 0L;

	/**
	 * 使用位运算增加属性
	 * 1 << 31 最大
	 *
	 * @param old
	 * @param prop
	 * @return
	 */
	public static int addProp(int old, int prop) {
		if (!isContain(old, prop)) {
			return old | prop;
		} else {
			return old;
		}
	}

	/**
	 * 使用位运算增加属性
	 *
	 * @param old
	 * @param props
	 * @return
	 */
	public static int addProps(int old, int... props) {
		for (int i = 0, len = props.length; i < len; i++) {
			old = addProp(old, props[i]);
		}
		return old;
	}

	/**
	 * 使用位运算删除属性
	 *
	 * @param old
	 * @param prop
	 * @return
	 */
	public static int removeProp(int old, int prop) {
		if (isContain(old, prop)) {
			return old ^ prop;
		} else {
			return old;
		}
	}

	/**
	 * 使用位运算删除属性
	 *
	 * @param old
	 * @param props
	 * @return
	 */
	public static int removeProps(int old, int... props) {
		for (int i = 0, len = props.length; i < len; i++) {
			old = removeProp(old, props[i]);
		}
		return old;
	}

	/**
	 * 使用位运算查询属性
	 *
	 * @param old
	 * @param prop
	 * @return
	 */
	public static boolean isContain(int old, int prop) {
		return (old & prop) != 0;
	}

	/**
	 * 将整形转为2进制
	 *
	 * @param prop
	 * @return
	 */
	public static String int2BinaryString(int prop) {
		return Integer.toBinaryString(prop);
	}

	/**
	 * 将2进制转为整形
	 *
	 * @param s
	 * @return
	 */
	public static int binaryString2int(String s) {
		return Integer.parseInt(s, 2);
	}

	/**
	 * 使用位运算增加属性
	 *
	 * @param old
	 * @param prop
	 * @return
	 */
	public static long addProp(long old, long prop) {
		if (!isContain(old, prop)) {
			return old | prop;
		} else {
			return old;
		}
	}

	/**
	 * 使用位运算增加属性
	 *
	 * @param old
	 * @param props
	 * @return
	 */
	public static long addProps(long old, long... props) {
		for (int i = 0, len = props.length; i < len; i++) {
			old = addProp(old, props[i]);
		}
		return old;
	}

	/**
	 * 使用位运算删除属性
	 *
	 * @param old
	 * @param prop
	 * @return
	 */
	public static long removeProp(long old, long prop) {
		if (isContain(old, prop)) {
			return old ^ prop;
		} else {
			return old;
		}
	}

	/**
	 * 使用位运算删除属性
	 *
	 * @param old
	 * @param props
	 * @return
	 */
	public static long removeProps(long old, long... props) {
		for (int i = 0, len = props.length; i < len; i++) {
			old = removeProp(old, props[i]);
		}
		return old;
	}

	/**
	 * 使用位运算查询属性
	 *
	 * @param old
	 * @param prop
	 * @return
	 */
	public static boolean isContain(long old, long prop) {
		return (old & prop) != 0;
	}

	/**
	 * 将整形转为2进制
	 *
	 * @param prop
	 * @return
	 */
	public static String long2SBinaryString(long prop) {
		return Long.toBinaryString(prop);
	}

	/**
	 * 将2进制转为整形
	 *
	 * @param s
	 * @return
	 */
	public static long binaryString2long(String s) {
		return Long.parseLong(s, 2);
	}

}
