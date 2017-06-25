package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class MaxDouble implements ValidatorHandler<Double> {

	private Double max;

	/**
	 * 获取实例
	 *
	 * @param max
	 * @return
	 */
	public static ValidatorHandler<Double> getInstance(Double max) {
		return new MaxDouble(max);
	}

	@Override
	public boolean vaild(Double obj) {
		return obj <= max;
	}

	@Override
	public String failMsg() {
		return "数值不允许比" + max + "大";
	}

}
