package org.kelab.test.util;

import org.junit.Test;
import org.kelab.util.DateUtil;

/**
 * Created by wanhongfei on 2017/1/12.
 */
public class DateTest {

	@Test
	public void test() {
		System.out.println(DateUtil.currentWeekBegin());
		System.out.println(DateUtil.currentMonthBegin());
		System.out.println(DateUtil.currentDateBegin());
		System.out.println(DateUtil.date2Long(DateUtil.currentDateBegin()) / DateUtil.THOUSAND);
	}
}
