package org.kelab.vf.validator.impl.bool;

import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
public class AssertFalse implements ValidatorHandler<Boolean> {
	/**
	 * 获取实例
	 *
	 * @return
	 */
	public static ValidatorHandler<Boolean> getInstance() {
		return new AssertFalse();
	}

	@Override
	public boolean vaild(Boolean obj) {
		return obj.equals(Boolean.FALSE);
	}

	@Override
	public String failMsg() {
		return "布尔值不为false";
	}

}
