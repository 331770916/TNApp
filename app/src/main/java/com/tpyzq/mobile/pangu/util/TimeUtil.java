package com.tpyzq.mobile.pangu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhangwenbo on 2016/6/16.
 * 时间格式化工具类
 */
public class TimeUtil {
    /**
     * 格式化时间
     *
     * @param seconds 秒
     * @return yy/MM/dd
     */
    public static String dateFormat(long seconds) {
        try {
            Date date = new Date(seconds * 1000);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     * string类型转换为long类型
     * strTime要转换的String类型的时间
     * formatType时间格式
     * strTime的时间格式和formatType的时间格式必须相同
     */
    public static long stringToLong(String strTime, String formatType)  {
        long time = 0;
        try {
            Date date = stringToDate(strTime, formatType); // String类型转成date类型
            if (date == null) {
                time = 0;
            } else {
                time = dateToLong(date); // date类型转成long类型
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    /**
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     * string类型转换为date类型
     * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     *
     * @param date
     * @return
     *  date类型转换为long类型
     *  date要转换的date类型的时间
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }



    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
                    return 29;
                }else{
                    return 28;
                }
            default:
                return  -1;
        }
    }
    /**
     * 返回当前月份1号位于周几
     * @param year
     * 		年份
     * @param month
     * 		月份，传入系统获取的，不需要正常的
     * @return
     * 	日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
