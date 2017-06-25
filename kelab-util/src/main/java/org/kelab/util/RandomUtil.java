package org.kelab.util;

import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * @author wwhhf
 * @comment 随机生成数字
 * @since 2016年6月10日
 */
public class RandomUtil {

	/**
	 * @param left
	 * @param right
	 * @return
	 * @author wwhhf
	 * @comment 随机生成指定闭区间的数值
	 * @since 2016年6月10日
	 */
	public static int randomInt(int left, int right) {
		return RandomUtils.nextInt(left, right);
	}

	/**
	 * 随机生成指定闭区间的数值
	 *
	 * @param right
	 * @return
	 */
	public static int randomInt(int right) {
		return randomInt(1, right);
	}

	/**
	 * @param left
	 * @param right
	 * @return
	 * @author wwhhf
	 * @comment 随机生成指定闭区间长度的字符串
	 * @since 2016年6月10日
	 */
	public static String randomString(int left, int right) {
		int randomLen = randomInt(left, right);
		return RandomStringUtils.randomAlphanumeric(randomLen);
	}

	/**
	 * @param right
	 * @return
	 * @author wwhhf
	 * @comment 随机生成指定闭区间长度的字符串
	 * @since 2016年6月10日
	 */
	public static String randomString(int right) {
		return randomString(1, right);
	}

	/**
	 * @param left
	 * @param right
	 * @return
	 * @author wwhhf
	 * @comment 随机生成指定闭区间长度的字符串
	 * @since 2016年6月10日
	 */
	public static String randomString(int left, int right, @NonNull String chars) {
		int randomLen = randomInt(left, right);
		return RandomStringUtils.random(randomLen, chars);
	}

	/**
	 * @param chars
	 * @param right
	 * @return
	 * @author wwhhf
	 * @comment 随机生成指定闭区间长度的字符串
	 * @since 2016年6月10日
	 */
	public static String randomString(int right, @NonNull String chars) {
		int randomLen = randomInt(right);
		return RandomStringUtils.random(randomLen, chars);
	}

	/**
	 * @param len
	 * @return
	 * @author wwhhf
	 * @comment 随机生成指定闭区间长度的字符串
	 * @since 2016年6月10日
	 */
	public static String randomCertainLenString(int len) {
		return RandomStringUtils.randomAlphanumeric(len);
	}

	/**
	 * @param len
	 * @param chars
	 * @return
	 * @author wwhhf
	 * @comment 随机生成指定长度的字符串
	 * @since 2016年6月10日
	 */
	public static String randomCertainLenString(int len, @NonNull String chars) {
		return RandomStringUtils.random(len, chars);
	}

}
