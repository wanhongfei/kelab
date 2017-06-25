package org.kelab.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.kelab.util.constant.CharsetConstant;

import java.io.UnsupportedEncodingException;

/**
 * @author wwhhf
 * @comment 编码工具
 * @since 2016年6月13日
 */
public class CharsetUtil {

	/**
	 * @param var
	 * @return
	 * @throws UnsupportedEncodingException
	 * @author wwhhf
	 * @comment 修正编码问题
	 * @since 2016年6月13日
	 */
	@SneakyThrows
	public static String fixCharset(String var) {
		if (StringUtils.isEmpty(var)) return StringUtil.emptyString;
		String encode = getEncoding(var);
		if (encode != null) {
			return new String(var.getBytes(encode), CharsetConstant.UTF_8);
		}
		return var;
	}

	/**
	 * @param var
	 * @return
	 * @author wwhhf
	 * @comment 测试编码
	 * @since 2016年6月13日
	 */
	private static String getEncoding(String var) {
		try {
			if (var.equals(new String(var.getBytes(CharsetConstant.GB2312),
					CharsetConstant.GB2312))) {
				return CharsetConstant.GB2312;
			}
		} catch (Exception exception) {
			//
		}
		try {
			if (var.equals(new String(var.getBytes(CharsetConstant.ISO_8859_1),
					CharsetConstant.ISO_8859_1))) {
				return CharsetConstant.ISO_8859_1;
			}
		} catch (Exception exception1) {
			//
		}
		try {
			if (var.equals(new String(var.getBytes(CharsetConstant.UTF_8),
					CharsetConstant.UTF_8))) {
				return CharsetConstant.UTF_8;
			}
		} catch (Exception exception2) {
			//
		}
		try {
			if (var.equals(new String(var.getBytes(CharsetConstant.GBK),
					CharsetConstant.GBK))) {
				return CharsetConstant.GBK;
			}
		} catch (Exception exception3) {
			//
		}
		return null;
	}
}
