package org.kelab.util;

/**
 * Created by hongfei.whf on 2017/3/12.
 */
public class ConsoleUtil {

	public static final String SHOW_STARTUP_DETAIL = "showStartupDetail";

	private static boolean enable = PropertiesUtil.getPropertyByName("showStartupDetail", Boolean.class);

	/**
	 * 控制台打印
	 *
	 * @param msg
	 */
	public static void println(String msg) {
		if (enable) {
			System.out.println(msg);
		}
	}

	/**
	 * 控制台打印
	 *
	 * @param pattern
	 * @param args
	 */
	public static void println(String pattern, Object... args) {
		if (enable) {
			System.out.println(StringUtil.format(pattern, args));
		}
	}

}
