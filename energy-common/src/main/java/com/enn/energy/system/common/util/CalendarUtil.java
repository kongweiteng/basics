package com.enn.energy.system.common.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 工作日
 *
 * @author liuch
 * @create 2017年04月10日 10:07
 */

public class CalendarUtil {
    private static List<Calendar> holidayList = new ArrayList<Calendar>();  //节假日列表

    /**
     *
     * <p>Title: addDateByWorkDay </P>
     * <p>Description:计算相加day天，并且排除节假日和周末后的日期</P>
     * @param calendar  当前的日期
     * @param day  相加天数
     * @return
     * return Calendar    返回类型   返回相加day天，并且排除节假日和周末后的日期
     * throws
     * date 2014-11-24 上午10:32:55
     */
    public static Date addDateByWorkDay(Calendar calendar, int day){

        try {
            for (int i = 0; i < day; i++) {

                calendar.add(Calendar.DAY_OF_MONTH, 1);

                if(checkHoliday(calendar)){
                    i--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return  calendar.getTime();
    }

    /**
     *
     * <p>Title: checkHoliday </P>
     * <p>Description:  验证日期是否是节假日</P>
     * @param calendar  传入需要验证的日期
     * @return
     * return boolean    返回类型  返回true是节假日，返回false不是节假日
     * throws
     * date 2014-11-24 上午10:13:07
     */
    public static boolean checkHoliday(Calendar calendar) throws Exception{

        //判断日期是否是周六周日
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return true;
        }
        //判断日期是否是节假日
        for (Calendar ca : holidayList) {
            if(ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)&&
                    ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){
                return true;
            }
        }

        return false;
    }

    /**
     *
     * <p>Title: initHolidayList </P>
     * <p>Description:   把所有节假日放入list，验证前要先执行这个方法</P>
     * @param date  从数据库查 查出来的格式2014-05-09
     * return void    返回类型
     * throws
     * date 2014-11-24 上午10:11:35
     */
    public static  void initHolidayList(List<String> date) {

        for (String string : date) {

            String[] da = string.split("-");

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.valueOf(da[0]));
            calendar.set(Calendar.MONTH, Integer.valueOf(da[1]) - 1);// 月份比正常小1,0代表一月
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da[2]));
            holidayList.add(calendar);
        }

    }
}
