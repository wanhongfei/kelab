package org.kelab.test.util;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {

	@Test
	public void test() {
		String urlPattern = "[/[\\w\\W]+[:\\d+]*|/api]+(/.+\\.do)";
		Pattern URL_PATTERN = Pattern.compile(urlPattern);
		Matcher matcher = URL_PATTERN.matcher("http://localhost:8080/api/problem/query.do");
		if (matcher.find() == true) {
			// controller接口
			String controllerUrl = matcher.group(1);
			System.out.println(controllerUrl);
		} else {
			// 静态资源
		}
	}

	@Test
	public void test1() {
		String urlPattern = "\\w+\\[\\[([\\w\\W]+)\\]\\]\\w+";
		Pattern URL_PATTERN = Pattern.compile(urlPattern);
		Matcher matcher = URL_PATTERN.matcher("sfdsaf[[在线43_email]]fdsafdsaf");
		if (matcher.find() == true) {
			// controller接口
			String controllerUrl = matcher.group(1);
			System.out.println(controllerUrl);
		} else {
			// 静态资源
		}
	}
}
