package com.enn.energy.system.redis;

/*
 系统常量
 */
public class Constant {
    /**
     * 今日电量
     */
    public static final String REDIS_DAY_ELE="fanneng:energy:electricity:day";
    /**
     * 昨日电量
     */
    public static final String REDIS_YESTERDAY_ELE="fanneng:energy:electricity:yesterday";
    /**
     * 本月电量
     */
    public static final String REDIS_MONTH_ELE="fanneng:energy:electricity:month";
    /**
     * 上月电量
     */
    public static final String REDIS_LASTMONTH_ELE="fanneng:energy:electricity:lastmonth";
    /**
     * 当年电量
     */
    public static final String REDIS_YEAR_ELE="fanneng:energy:electricity:year";
    /**
     * 上年电量
     */
    public static final String REDIS_LASTYEAR_ELE="fanneng:energy:electricity:lastyear";


    /**
     * 一小时
     */
    public static final int REDIS_TIME_HONER=3600;
    /**
     * 一天
     */
    public static final int REDIS_TIME_DAY=86400;

    /**
     * 一个月
     */
    public static final int REDIS_TIME_MONTH=86400;
}
