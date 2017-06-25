package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class MinDouble implements ValidatorHandler<Double> {

	private Double min;

	/**
	 * 获取实例
	 *
	 * @param min
	 * @return
	 */
	public static ValidatorHandler<Double> getInstance(Double min) {
		return new MinDouble(min);
	}

	@Override
	public boolean vaild(Double obj) {
		return obj >= min;
	}

	@Override
	public String failMsg() {
		return "数值不允许比" + min + "小";
	}

}
