package org.kelab.vf.validator.impl;

import org.apache.poi.ss.formula.functions.T;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
public class NotNull implements ValidatorHandler<T> {

	@Override
	public boolean vaild(T obj) {
		return obj == null ? false : true;
	}

	@Override
	public String failMsg() {
		return "值不允许为Null";
	}
}
