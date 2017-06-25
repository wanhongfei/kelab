package org.kelab.vf.validator.impl.str;

import org.kelab.util.StringUtil;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
public class Mobile implements ValidatorHandler<String> {

	/**
	 * 获取实例
	 *
	 * @return
	 */
	public static ValidatorHandler<String> getInstance() {
		return new Mobile();
	}

	@Override
	public boolean vaild(String obj) {
		return StringUtil.isMobile(obj);
	}

	@Override
	public String failMsg() {
		return "字符串不是手机号码";
	}
}
