/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.enn.energy.system.common.util;

import com.enn.vo.energy.business.resp.DataResp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kongweiteng
 * @Description 时间工具类
 */
public class DateUtil {

	public static final String BASIC_PATTEN = "yyyy-MM-dd HH:mm:00";

	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear(Date date) {
		return formatDate(date, "yyyy");
	}

	/**
	 * 获取YYYY-MM格式
	 *
	 * @return
	 */
	public static String getMonth() {
		return formatDate(new Date(), "yyyy-MM");
	}

	/**
	 * 获取YYYY-MM格式
	 *
	 * @param date
	 * @return
	 */
	public static String getMonth(Date date) {
		return formatDate(date, "yyyy-MM");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay() {
		return formatDate(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays() {
		return formatDate(new Date(), "yyyyMMdd");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays(Date date) {
		return formatDate(date, "yyyyMMdd");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss.SSS格式
	 *
	 * @return
	 */
	public static String getMsTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 获取YYYYMMDDHHmmss格式
	 *
	 * @return
	 */
	public static String getAllTime() {
		return formatDate(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDate(Date date, String pattern) {
		String formatDate = null;
		if (StringUtils.isNotBlank(pattern)) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @Title: compareDate
	 * @Description:(日期比较，如果s>=e 返回true 否则返回false)
	 * @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (parseDate(s) == null || parseDate(e) == null) {
			return false;
		}
		return parseDate(s).getTime() >= parseDate(e).getTime();
	}

	/**
	 * (时间比较，如果s>=e 返回true 否则返回false)
	 *
	 * @param s
	 * @param e
	 * @return
	 */
	public static boolean comparTime(String s, String e) {
		if (parseTime(s) == null || parseTime(e) == null) {
			return false;
		}
		return parseTime(s).getTime() >= parseTime(e).getTime();
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseDate(String date) {
		return parse(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseTime(String date) {
		return parse(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		try {
			return DateUtils.parseDate(date, pattern);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static String format(String time, String pattern) {
		return DateFormatUtils.format(parseTime(time), pattern);
	}

	/**
	 * 把日期转换为Timestamp
	 *
	 * @param date
	 * @return
	 */
	public static Timestamp format(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidTime(String s) {
		return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidTime(String s, String pattern) {
		return parse(s, pattern) != null;
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
					startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	/*
	 *返回----年--月
	 */
	public static String getDateYearMonth(String yearMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy年MM月");
		Date parse = null;
		try {
			parse = sdf.parse(yearMonth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String format = sdfs.format(parse);
		return format;
	}

	public static String stampToDate(String s) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt * 1000L);
		String res = simpleDateFormat.format(date);
		return res;
	}

	/**
	 * 获取上月1号0点0分0秒
	 *
	 * @return
	 */
	public static Date getLastOneDay() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.MONTH, -1);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTime();
	}

	/**
	 * 获取当月有多少天
	 */
	public static int getMonthSum(String yearMonth) {
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Integer.parseInt(yearMonth.substring(0, 4)), Integer.parseInt(yearMonth.substring(5, yearMonth.length())), 0);
		return currentDate.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当天0点0分0秒
	 *
	 * @return
	 */
	public static String getNowDay() {
		//创建当前时间格式化为年月日
		Date date = new Date();
		String s = formatDate(date, "yyyy-MM-dd");
		//拼接字符串
		s = s + " 00:00:00";
		return s;
	}

	/**
	 * 获取当年1号0点0分0秒
	 *
	 * @return
	 */
	public static Date getYearDay() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.YEAR, 0);
		currentDate.set(Calendar.DAY_OF_YEAR, 1);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTime();
	}

	/**
	 * 获取本月1号0点0分0秒
	 *
	 * @return
	 */
	public static Date getOneDay() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.MONTH, 0);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTime();
	}

	/**
	 * 获取本月1号
	 */
	public static String getMouthOneDay() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.MONTH, 0);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		return DateFormatUtils.format(currentDate, "yyyy-MM-dd");
	}

	/**
	 * 根据输入日期的当月1号0点0分0秒
	 */
	public static String getTimeOneDay(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		/*calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);*/
		return formatDateTime(calendar.getTime());
	}

	/***
	 * 根据输入日期获取下月1号0点0分0秒
	 */
	public static String getTimeNextDay(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		/*calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);*/
		return formatDateTime(calendar.getTime());
	}

	/**
	 * 获取下月1号0点0分0秒
	 *
	 * @return
	 */
	public static Date getNextDay() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.MONTH, 1);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTime();
	}

	/**
	 * 给时间减去一月
	 */
	public static String getLastMonth(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(2, -1);//给时间减上一月
		return formatDateTime(calendar.getTime());
	}

	/**
	 * 给时间减去一月
	 */
	public static String getLastMonthForDay(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(2, -1);//给时间减上一月
		return format(calendar.getTime(),"yyyy-MM-dd");
	}

	/**
	 * 给时间加上一个月
	 */
	public static String getNextMonth(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(2, 1);
		return formatDateTime(calendar.getTime());
	}

	/**
	 * 给时间月份加减操作
	 */
	public static String getNextMonth(String time, Integer amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(Calendar.MONTH, amount);
		return formatDateTime(calendar.getTime());
	}

	/**
	 *  给时间月份加减操作
	 */
	public static String getMonthAddDate(String time, Integer amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(time));
		calendar.add(Calendar.MONTH, amount);
		return formatDateTime(calendar.getTime());
	}


	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 给时间减去一年
	 */
	public static String getLastYear(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(1, -1);//给时间减上一月
		return formatDateTime(calendar.getTime());
	}

	/**
	 * 给时间减去一年
	 */
	public static String getLastYearForYear(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(1, -1);//给时间减上一月
		return format(calendar.getTime(),"yyyy-MM");
	}

	/**
	 * 给时间减去一天
	 */
	public static String getLastDay(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(5, -1);//给时间减上一天
		return formatDateTime(calendar.getTime());
	}

	/**
	 * 给时间加一天
	 */
	public static String getAddDay(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(5, 1);//
		return formatDateTime(calendar.getTime());
	}



	/**
	 * 给该时间 计算  小时级别  加减都可以，count可以正负控制加减
	 *
	 * @param time
	 * @param count
	 * @return
	 */
	public static String computingTime(String time, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(10, count);
		return formatDateTime(calendar.getTime());
	}

	/**
	 * 给该时间 计算  小时级别  加减都可以，count可以正负控制加减（24小时制）
	 *
	 * @param time
	 * @param count
	 * @return
	 */
	public static String addHour(String time, int field, int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseTime(time));
		calendar.add(field, count);
		return formatDateTime(calendar.getTime());
	}

	/**
	 * 一天 15分钟级
	 *
	 * @param start
	 * @return
	 */
	public static List<DataResp> getDataResp(String start) {
		List<DataResp> dataResps = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		String kk = start;
		for (int i = 0; i < 97; i++) {
			DataResp dr = new DataResp();
			dr.setTime(kk);
			calendar.setTime(parseTime(kk));
			calendar.add(Calendar.MINUTE, 15);
			kk = formatDateTime(calendar.getTime());
			dataResps.add(dr);
		}
		return dataResps;
	}

	/**
	 * 对日期、时间进行加、减操作。
	 * <pre>
	 *     DateUtil.add(date, Calendar.YEAR, -1); //date减一年
	 *     DateUtil.add(date, Calendar.HOUR, -4); //date减4个小时
	 *     DateUtil.add(date, Calendar.MONTH, 3); //date加3个月
	 * </pre>
	 *
	 * @param date   日期时间。
	 * @param field  执行加减操作的属性，参考{@link Calendar#YEAR}、{@link Calendar#MONTH}、{@link Calendar#HOUR}等。
	 * @param amount 加减数量。
	 * @return 执行加减操作后的日期、时间。
	 */
	public static Date add(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间的前后时间的值
	 */
	public static String getAroundDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DATE, day);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		calendar.add(Calendar.MINUTE, minute);
		calendar.add(Calendar.SECOND, second);
		Date date = calendar.getTime();
		return getTime(date);
	}

	public static String getBeforeHourTime(Calendar calendar, int ihour) {
		String returnstr = "";
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		returnstr = df.format(calendar.getTime());
		return returnstr;
	}

	/**
	 * 取到 hours 以前时间
	 *
	 * @param hours
	 * @return
	 */
	public static String getBeforeHourTime(Date date, int hours, String formate) {
		String returnstr = "";
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR_OF_DAY, hours);
			SimpleDateFormat df = new SimpleDateFormat(formate);
			returnstr = df.format(cal.getTime());
		} catch (Exception e) {
			System.out.println("获取时间之前n" + hours + "小时的时间异常！");
		}
		return returnstr;
	}

	/**
	 * 校验日期是否合法
	 *
	 * @param date
	 * @return
	 */
	public static Boolean isValidDate(String date) {
		return isValidTime(date, "yyyy-MM-dd");
	}

	/**
	 * 校验日期是否合法
	 *
	 * @param yearMonth
	 * @return
	 */
	public static Boolean yearMonth(String yearMonth) {
		return isValidTime(yearMonth, "yyyy-MM");
	}

	/**
	 * 判断日期是否是当前月
	 */
	public static boolean isThisMonth(String time) {
		boolean is;
		boolean year = time.substring(0, 4).equals(getMonth().substring(0, 4));
		boolean month = time.substring(5, 7).equals(getMonth().substring(5, 7));
		if (year && month) {
			is=true;
		} else {
			is=false;
		}
		return is;
	}

    /**
     * 判断日期是否大于等于当前月
     */
    public static boolean isGtThisMonth(String time) {
        boolean is;
        boolean year = Integer.parseInt(time.substring(0, 4)) >= Integer.parseInt(getMonth().substring(0, 4));
        boolean month = Integer.parseInt(time.substring(5, 7)) >=  Integer.parseInt(getMonth().substring(5, 7));
        if (year && month) {
            is=true;
        } else {
            is=false;
        }
        return is;
    }

	/**
	 * 去时间的上个月
	 */
	public static String getLastMonthOfYear(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse(time,"yyyy-MM"));
		calendar.add(2, -1);
		return format(calendar.getTime(),"yyyy-MM");
	}
	/**
	 * 获取当前时间的前天的数据
	 */
	public static String getLastOneday(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		return getDay(date);
	}

	/**
	 * 获取当前时间的前天的数据
	 */
	public static String getLastTwoday(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -2);
		Date date = calendar.getTime();
		return getDay(date);
	}
	/**
	 *
	 * @param date1 <String>
	 * @param date2 <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getMonthSpace(String date1, String date2) {

		int result = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(parseTime(date1));
		c2.setTime(parseTime(date2));

		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

		return result == 0 ? 1 : Math.abs(result);

	}

    /**
     *
     * @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
    public static int getMonthSpaceExt (String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar  from  =  Calendar.getInstance();
        Calendar  to  =  Calendar.getInstance();
        try {
            from.setTime(sdf.parse(date1));
            to.setTime(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //只要年月
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int month = toYear *  12  + toMonth  -  (fromYear  *  12  +  fromMonth);

        return month;
    }

    public static int getYearSpaceExt (String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar  from  =  Calendar.getInstance();
        Calendar  to  =  Calendar.getInstance();
        try {
            from.setTime(sdf.parse(date1));
            to.setTime(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //只要年月
        int fromYear = from.get(Calendar.YEAR);
        int toYear = to.get(Calendar.YEAR);
        int year = toYear  -  fromYear;

        return year;
    }

	/** @param date1 <String>
	 * @param date2 <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getDateSpace(String date1, String date2) {

		int result = 0;

		Calendar calst = Calendar.getInstance();;
		Calendar caled = Calendar.getInstance();

		calst.setTime(parseDate(date1));
		caled.setTime(parseDate(date2));

		//设置时间为0时
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);
		//得到两个日期相差的天数
		int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;

		return days;
	}

	/**
	 * 给时间加一天
	 */
	public static String getAddDay(String time,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(time));
		calendar.add(5, day);
		return format(calendar.getTime(),"yyyy-MM-dd");
	}
	/**
	 * 给时间加一天
	 */
	public static String getAddMonth(String time,int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse(time,"yyyy-MM"));
		calendar.add(2, month);//
		return format(calendar.getTime(),"yyyy-MM");
	}

	/**
	 *  获取两个日期相差的月数
	 * @param d1    较大的日期
	 * @param d2    较小的日期
	 * @return  如果d1>d2返回 月数差 否则返回0
	 */
	public static int getMonthDiff(String d1, String d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(parse(d1,"yyyy-MM"));
		c2.setTime(parse(d2,"yyyy-MM"));
		if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);
		int day1 = c1.get(Calendar.DAY_OF_MONTH);
		int day2 = c2.get(Calendar.DAY_OF_MONTH);
		// 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
		int yearInterval = year1 - year2;
		// 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
		if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;
		// 获取月数差值
		int monthInterval =  (month1 + 12) - month2  ;
		if(day1 < day2) monthInterval --;
		monthInterval %= 12;
		return yearInterval * 12 + monthInterval;
	}

	/**
	 * 上月第一天
	 * @return
	 */
	public static String getLastMonthFirst(){
		Calendar cale = Calendar.getInstance();//获取当前日期
		cale.add(Calendar.MONTH, -1);
		cale.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		return format(cale.getTime(),"yyyy-MM-dd");
	}

	/**
	 * 上月最后一天
	 * @return
	 */
	public static String getLastMonth(){
		Calendar   cale=Calendar.getInstance();//获取当前日期
		cale.set(Calendar.DAY_OF_MONTH,0);
		return format(cale.getTime(),"yyyy-MM-dd");
	}

	/**
	 * 根据月份取每日
	 *
	 * @param date yyyy-MM
	 * @return
	 */
	public static Map<String, String> getMonthDay(String date) {
		Map<String, String> map = new LinkedHashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7)) - 1);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		for (int i = 1; i <= maxDay; i++) {
			if (i < 10) {
				map.put(date + "-0" + i, null);
			} else {
				map.put(date + "-" + i, null);
			}
		}
		return map;
	}
}
