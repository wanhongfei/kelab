package org.kelab.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author wwhhf
 * @comment 时间工具类
 * @since 2016年6月13日
 */
public class DateUtil {

	// 时间戳倍数
	public static final long THOUSAND = 1000L;
	// 某年某月的开始
	private static final String MONTH_BEGIN_PATTERN = "%s-%s-01 00:00:00";
	// 某年某月某日的开始
	private static final String DATE_BEGIN_PATTERN = "%s-%s-%s 00:00:00";

	/**
	 * date->long
	 *
	 * @param date
	 * @return
	 */
	public static long date2Long(@NonNull Date date) {
		return date.getTime();
	}

	/**
	 * long->date
	 *
	 * @param time
	 * @return
	 */
	public static Date long2Date(long time) {
		return new Date(time);
	}

	/**
	 * long->java.sql.Date
	 *
	 * @param time
	 * @return
	 */
	public static java.sql.Date long2SqlDate(long time) {
		return new java.sql.Date(time);
	}

	/**
	 * java.sql.Date->long
	 *
	 * @param date
	 * @return
	 */
	public static long sqlDate2Long(@NonNull java.sql.Date date) {
		return date.getTime();
	}

	/**
	 * long->Time
	 *
	 * @param time
	 * @return
	 */
	public static Time long2Time(long time) {
		return new Time(time);
	}

	/**
	 * time->long
	 *
	 * @param time
	 * @return
	 */
	public static long time2Long(@NonNull Time time) {
		return time.getTime();
	}

	/**
	 * date->java.sql.Date
	 *
	 * @param date
	 * @return
	 */
	public static java.sql.Date date2sqlDate(@NonNull Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * date->java.sql.Date
	 *
	 * @param date
	 * @return
	 */
	public static Date sqlDate2Date(@NonNull java.sql.Date date) {
		return new Date(date.getTime());
	}

	/**
	 * date->date
	 *
	 * @param time
	 * @return
	 */
	public static Date time2Date(@NonNull Time time) {
		return new Date(time.getTime());
	}

	/**
	 * java.sql.Date->Time
	 *
	 * @param date
	 * @return
	 */
	public static Time sqlDate2Time(@NonNull java.sql.Date date) {
		return new Time(date.getTime());
	}

	/**
	 * time->java.sql.Date
	 *
	 * @param time
	 * @return
	 */
	public static java.sql.Date time2SqlDate(@NonNull Time time) {
		return new java.sql.Date(time.getTime());
	}

	/**
	 * 改变时间格式(date)
	 *
	 * @param time
	 * @return
	 * @throws ParseException
	 * @author wwhhff11
	 * @since 2016/03/02
	 */
	@SneakyThrows
	public static Date string2Date(@NonNull String time, @NonNull String pattern) {
		return DateUtils.parseDate(time, new String[]{pattern});
	}

	/**
	 * 改变时间格式(date)
	 *
	 * @param time
	 * @return
	 * @throws ParseException
	 * @author wwhhff11
	 * @since 2016/03/02
	 */
	@SneakyThrows
	public static Date string2Date(@NonNull String time) {
		return DateUtils.parseDate(time, DatePatternConstant.DATE_PATTERNS);
	}

	/**
	 * 改变时间格式(String)
	 *
	 * @param date
	 * @author wwhhff11
	 * @returnformat
	 * @since 2016/03/02
	 */
	public static String date2String(@NonNull Date date, @NonNull String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * date=>Time
	 *
	 * @param date
	 * @return
	 */
	public static Time date2Time(@NonNull Date date) {
		return long2Time(date.getTime());
	}

	/**
	 * whf 得到服务器当前的时间
	 *
	 * @return
	 */
	public static Date currentDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * whf 得到服务器当前的时间
	 *
	 * @return
	 */
	public static Time currentTime() {
		return date2Time(Calendar.getInstance().getTime());
	}

	/**
	 * whf 得到服务器当前的时间
	 *
	 * @return
	 */
	public static java.sql.Date currentSqlDate() {
		return date2sqlDate(Calendar.getInstance().getTime());
	}

	/**
	 * whf 得到服务器当前的时间
	 *
	 * @return
	 */
	public static long currentTimestamp() {
		return System.currentTimeMillis() / THOUSAND;
	}

	/**
	 * whf 得到服务器当前的时间
	 *
	 * @return
	 */
	public static long currentLong() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当前月份的开始时间
	 * 在获取月份时，Calendar.MONTH + 1 的原因
	 * Java中的月份遵循了罗马历中的规则：当时一年中的月份数量是不固定的，
	 * 第一个月是JANUARY。而Java中Calendar.MONTH返回的数值其实是当前月距离第一个月有多少个月份的数值，
	 * JANUARY在Java中返回“0”，所以我们需要+1。
	 */
	public static Date currentMonthBegin() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		return string2Date(StringUtil.format(MONTH_BEGIN_PATTERN, year, month + 1));
	}

	/**
	 * 获取当前月份的开始时间
	 * 在获取星期几 Calendar.DAY_OF_WEEK – 1 的原因
	 * Java中Calendar.DAY_OF_WEEK其实表示：一周中的第几天，所以他会受到 第一天是星期几 的影响。
	 * 有些地区以星期日作为一周的第一天，而有些地区以星期一作为一周的第一天，这2种情况是需要区分的。
	 * 看下表的返回值
	 */
	public static Date currentWeekBegin() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int weekBegin = calendar.getFirstDayOfWeek();
		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		calendar.add(Calendar.DATE, weekBegin - day);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当日的开始时间
	 */
	public static Date currentDateBegin() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DATE);
		return string2Date(StringUtil.format(DATE_BEGIN_PATTERN, year, month + 1, date));
	}

	/**
	 * @param timeUnit
	 * @param time
	 * @return
	 * @throws Exception
	 * @author wwhhf
	 * @comment
	 * @since 2016年6月11日
	 */
	@SneakyThrows
	public static Long time2Millisecond(@NonNull TimeUnit timeUnit, @NonNull Long time) {
		if (timeUnit.compareTo(TimeUnit.MILLISECONDS) == 0) {
			// 不需要转换
		} else if (timeUnit.compareTo(TimeUnit.SECONDS) == 0) {
			time = time * 1000;
		} else if (timeUnit.compareTo(TimeUnit.MINUTES) == 0) {
			time = time * 60 * 1000;
		} else if (timeUnit.compareTo(TimeUnit.HOURS) == 0) {
			time = time * 60 * 60 * 1000;
		} else if (timeUnit.compareTo(TimeUnit.DAYS) == 0) {
			time = time * 24 * 60 * 60 * 1000;
		} else {
			throw new Exception("time can't trans to Millisecond");
		}
		return time;
	}

	/**
	 * 日期相加
	 *
	 * @param date
	 * @param field
	 * @param time
	 * @return
	 */
	public static Date add2Date(Date date, int field, long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, Integer.parseInt(time + ""));
		return calendar.getTime();
	}

	/**
	 * 日期相加
	 *
	 * @param date
	 * @param field
	 * @param time
	 * @return
	 */
	public static long add2Long(Date date, int field, int time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, time);
		return calendar.getTime().getTime();
	}

	/**
	 * Created by hongfei.whf on 2016/8/31.
	 */
	public static class DatePatternConstant {

		public static final String LINE_ALL = "yyyy-MM-dd HH:mm:ss";
		public static final String SLASH_ALL = "yyyy/MM/dd HH:mm:ss";
		public static final String LINE_DATE = "yyyy-MM-dd";
		public static final String SLASH_DATE = "yyyy/MM/dd";
		public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";

		public static final String[] DATE_PATTERNS = new String[]{
				"yyyy-MM",
				"yyyy/MM",
				"yyyy-MM-dd",
				"yyyy/MM/dd",
				"yyyy-MM-dd HH:mm:ss",
				"yyyy/MM/dd HH:mm:ss"
		};

	}

}
