package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class RangeDouble implements ValidatorHandler<Double> {

	private Double min;
	private Double max;

	/**
	 * 获取实例
	 *
	 * @param min
	 * @return
	 */
	public static ValidatorHandler<Double> getInstance(Double min, Double max) {
		return new RangeDouble(min, max);
	}

	@Override
	public boolean vaild(Double obj) {
		return obj >= min && obj <= max;
	}

	@Override
	public String failMsg() {
		return "值不在" + min + "和" + max + "之间";
	}

}
