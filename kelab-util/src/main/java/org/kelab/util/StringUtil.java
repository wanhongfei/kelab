package org.kelab.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hongfei.whf on 2016/12/2.
 */
public class StringUtil {

	/**
	 * 特殊集合
	 *
	 * @return
	 */
	public static final String emptyString = StringUtils.EMPTY;

	/**
	 * 字符串是否为空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 字符串是否为非空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 判断字符串相等
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(String a, String b) {
		if (a == null && b == null) {
			return true;
		} else if (a != null && b != null) {
			return a.trim().equals(b.trim());
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串相等（忽略大小写）
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equalsIgnoreCase(String a, String b) {
		if (a == null && b == null) {
			return true;
		} else if (a != null && b != null) {
			return a.trim().toLowerCase().equals(b.trim().toLowerCase());
		} else {
			return false;
		}
	}

	/**
	 * 将collection用指定分隔符组合成字符串
	 *
	 * @param collection
	 * @param op
	 * @return
	 */
	public static String join(@NonNull Collection collection, @NonNull String op) {
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		for (Object obj : collection) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(op);
			}
			sb.append(obj);
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否包含
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean contains(@NonNull String a, @NonNull String b) {
		if (isBlank(b)) {
			return false;
		} else {
			return a.contains(b);
		}
	}

	/**
	 * 字符串撰文其他基本类型
	 *
	 * @param value
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T string2BaseType(@NonNull String value, @NonNull Class<T> clazz) {
		if (clazz.equals(Integer.class)) {
			return (T) Integer.valueOf(value);
		} else if (clazz.equals(Long.class)) {
			return (T) Long.valueOf(value);
		} else if (clazz.equals(Float.class)) {
			return (T) Float.valueOf(value);
		} else if (clazz.equals(Double.class)) {
			return (T) Double.valueOf(value);
		} else if (clazz.equals(Boolean.class)) {
			return (T) Boolean.valueOf(value);
		} else {
			return (T) value;
		}
	}

	/**
	 * 是不是数值
	 *
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(@NonNull String value) {
		return StringUtils.isNumeric(value);
	}

	/**
	 * 拼装字符串 %s占位符
	 *
	 * @param formatstr
	 * @param args
	 * @return
	 */
	public static String format(@NonNull String formatstr, Object... args) {
		return String.format(formatstr, args);
	}

	/**
	 * 手机号验证
	 *
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(final String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 *
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(final String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 是不是邮箱
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmail(final String str) {
		return str.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	}

	/**
	 * 把html内容转为文本
	 *
	 * @param html
	 * @return
	 */
	public static String filterHtmlTags(@NonNull String html) {
		String htmlStr = html; // 含html标签的字符串
		String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
		String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签
		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		return htmlStr;// 返回文本字符串
	}
}
