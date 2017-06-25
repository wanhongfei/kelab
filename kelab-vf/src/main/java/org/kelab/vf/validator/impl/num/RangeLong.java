package org.kelab.vf.validator.impl.num;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class RangeLong implements ValidatorHandler<Long> {

	private Long min;
	private Long max;

	/**
	 * 获取实例
	 *
	 * @param min
	 * @return
	 */
	public static ValidatorHandler<Long> getInstance(Long min, Long max) {
		return new RangeLong(min, max);
	}

	@Override
	public boolean vaild(Long obj) {
		return obj >= min && obj <= max;
	}

	@Override
	public String failMsg() {
		return "值不在" + min + "和" + max + "之间";
	}

}
