package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class RangeFloat implements ValidatorHandler<Float> {

	private Float min;
	private Float max;

	/**
	 * 获取实例
	 *
	 * @param min
	 * @return
	 */
	public static ValidatorHandler<Float> getInstance(Float min, Float max) {
		return new RangeFloat(min, max);
	}

	@Override
	public boolean vaild(Float obj) {
		return obj >= min && obj <= max;
	}

	@Override
	public String failMsg() {
		return "值不在" + min + "和" + max + "之间";
	}

}
