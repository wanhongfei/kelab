package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class MaxLong implements ValidatorHandler<Long> {

	private Long max;

	/**
	 * 获取实例
	 *
	 * @param max
	 * @return
	 */
	public static ValidatorHandler<Long> getInstance(Long max) {
		return new MaxLong(max);
	}

	@Override
	public boolean vaild(Long obj) {
		return obj <= max;
	}

	@Override
	public String failMsg() {
		return "数值不允许比" + max + "大";
	}

}
