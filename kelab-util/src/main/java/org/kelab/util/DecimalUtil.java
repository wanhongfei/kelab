package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.text.DecimalFormat;

/**
 * Created by hongfei.whf on 2016/12/11.
 */
public class DecimalUtil {

	/**
	 * decimal(double|float)->String
	 *
	 * @param d
	 * @param pattern
	 * @return
	 */
	@SneakyThrows
	public static String decimal2String(@NonNull Object d, @NonNull String pattern) {
		if (d.getClass().equals(Double.class)
				|| d.getClass().equals(Float.class)) {
			DecimalFormat df = new DecimalFormat(pattern);
			return df.format(d);
		} else {
			throw new Exception("d is not double or float");
		}
	}

	/**
	 * String->double
	 *
	 * @param d
	 * @return
	 */
	public static Double string2Double(@NonNull String d) {
		return Double.parseDouble(d);
	}

	/**
	 * String->double
	 *
	 * @param d
	 * @return
	 */
	public static Float string2Float(@NonNull String d) {
		return Float.parseFloat(d);
	}

	/**
	 * Created by hongfei.whf on 2016/12/11.
	 */
	public static class DecimalPreciseConstant {

		public static final String ONE = "#.0";
		public static final String TWO = "#.00";
		public static final String THREE = "#.000";
		public static final String FOUR = "#.0000";
		public static final String FIVE = "#.00000";

	}

}
