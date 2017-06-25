package org.kelab.vf.validator.impl.str;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class Pattern implements ValidatorHandler<String> {

	private String pattern;

	/**
	 * 获取实例
	 *
	 * @param pattern
	 * @return
	 */
	public static ValidatorHandler<String> getInstance(String pattern) {
		return new Pattern(pattern);
	}

	@Override
	public boolean vaild(String obj) {
		return obj.matches(pattern);
	}

	@Override
	public String failMsg() {
		return "字符串不符合正则表达式" + pattern;
	}
}