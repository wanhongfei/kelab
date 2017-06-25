package org.kelab.vf.validator.impl.date;

import lombok.AllArgsConstructor;
import org.kelab.vf.validator.ValidatorHandler;

import java.util.Date;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
@AllArgsConstructor
public class Future implements ValidatorHandler<Date> {

	private Date date;

	/**
	 * 获取实例
	 *
	 * @param date
	 * @return
	 */
	public static ValidatorHandler<Date> getInstance(Date date) {
		return new Future(date);
	}

	@Override
	public boolean vaild(Date obj) {
		return obj.getTime() >= date.getTime();
	}

	@Override
	public String failMsg() {
		return "日期不允许比" + date.toString() + "早";
	}

}
