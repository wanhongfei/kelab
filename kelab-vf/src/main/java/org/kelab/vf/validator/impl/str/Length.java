package org.kelab.vf.validator.impl.str;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class Length implements ValidatorHandler<String> {

	private int min;
	private int max;

	/**
	 * 获取实例
	 *
	 * @param min
	 * @return
	 */
	public static ValidatorHandler<String> getInstance(int min, int max) {
		return new Length(min, max);
	}

	@Override
	public boolean vaild(String obj) {
		obj = obj.trim();
		return obj.length() >= min && obj.length() <= max;
	}

	@Override
	public String failMsg() {
		return "字符串的长度不在" + min + "和" + max + "之间";
	}
}
