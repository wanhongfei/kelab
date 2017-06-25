package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class MaxInteger implements ValidatorHandler<Integer> {

	private Integer max;

	/**
	 * 获取实例
	 *
	 * @param max
	 * @return
	 */
	public static ValidatorHandler<Integer> getInstance(Integer max) {
		return new MaxInteger(max);
	}

	@Override
	public boolean vaild(Integer obj) {
		return obj <= max;
	}

	@Override
	public String failMsg() {
		return "数值不允许比" + max + "大";
	}

}
