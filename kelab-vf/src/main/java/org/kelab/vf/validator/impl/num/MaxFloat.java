package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class MaxFloat implements ValidatorHandler<Float> {

	private Float max;

	/**
	 * 获取实例
	 *
	 * @param max
	 * @return
	 */
	public static ValidatorHandler<Float> getInstance(Float max) {
		return new MaxFloat(max);
	}

	@Override
	public boolean vaild(Float obj) {
		return obj <= max;
	}

	@Override
	public String failMsg() {
		return "数值不允许比" + max + "大";
	}

}
