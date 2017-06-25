package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class MinInteger implements ValidatorHandler<Integer> {

	private Integer min;

	/**
	 * 获取实例
	 *
	 * @param min
	 * @return
	 */
	public static ValidatorHandler<Integer> getInstance(Integer min) {
		return new MinInteger(min);
	}

	@Override
	public boolean vaild(Integer obj) {
		return obj >= min;
	}

	@Override
	public String failMsg() {
		return "数值不允许比" + min + "小";
	}

}
