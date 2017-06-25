package org.kelab.test.util;

import org.junit.Test;
import org.kelab.util.DateUtil;
import org.kelab.vf.validator.Validator;
import org.kelab.vf.validator.impl.date.Past;
import org.kelab.vf.validator.impl.num.MaxInteger;
import org.kelab.vf.validator.impl.num.MinInteger;
import org.kelab.vf.validator.impl.num.RangeInteger;
import org.kelab.vf.validator.impl.str.NotBlank;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hongfei.whf on 2017/3/1.
 */
public class ValidatorTest {

	/**
	 *
	 */
	@Test
	public void testStr() {
		String s = "13620446688";
		Validator.<String>getInstance(s, "", String.class).next(NotBlank.getInstance()).vaild();
	}

	/**
	 *
	 */
	@Test
	public void testdate() {
		Date date = Calendar.getInstance().getTime();
		Validator.<Date>getInstance(date, "", Date.class).next(Past.getInstance(
				DateUtil.string2Date("2017-4-1 00:00:00"))).vaild();
	}

	/**
	 *
	 */
	@Test
	public void testNum() {
		Validator.<Integer>getInstance(1, "", Integer.class)
				.next(MinInteger.getInstance(0))
				.next(MaxInteger.getInstance(3))
				.next(RangeInteger.getInstance(0, 0))
				.vaild();
	}

}
