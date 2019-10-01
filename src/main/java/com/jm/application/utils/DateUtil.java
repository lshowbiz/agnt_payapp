package com.jm.application.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 处理日期类
 * @author JunGuang
 */
public class DateUtil {
	/**
	 * 获得指定日期的当日开始时间的日期对象，返回的对象为一个新建Date对象。
	 * 
	 * 开始时间为当天的0时0分0秒0毫秒。
	 * 
	 * @param d
	 *            要获得开始时间的Date对象
	 * @return 指定日期的当日开始时间，新建Date对象。
	 */
	public static Date getStartOfDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 获得指定日期的当日结束时间的日期对象，返回的对象为一个新建Date对象。
	 * 
	 * 结束时间为当天的23时59分59秒999毫秒。
	 * 
	 * @param d
	 *            要获得结束时间的Date对象
	 * @return 指定日期的当日结束时间，新建Date对象。
	 */
	public static Date getEndOfDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	/**
	 * date format String
	 * @param date
	 * @param format is "yyyyMMddHHmmss" or "yyyy-MM-dd" or other
	 * @return string
	 */
	public static String formatString(Date date,String format){
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}
	public static Date formatDate(String dateStr,String format) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(dateStr);
	}
}
