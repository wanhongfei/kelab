package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class RangeInteger implements ValidatorHandler<Integer> {

	private Integer min;
	private Integer max;

	/**
	 * 获取实例
	 *
	 * @param min
	 * @return
	 */
	public static ValidatorHandler<Integer> getInstance(Integer min, Integer max) {
		return new RangeInteger(min, max);
	}

	@Override
	public boolean vaild(Integer obj) {
		return obj >= min && obj <= max;
	}

	@Override
	public String failMsg() {
		return "值不在" + min + "和" + max + "之间";
	}

}
