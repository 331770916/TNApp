package com.tpyzq.mobile.pangu.util.panguutil;

import android.text.TextUtils;

/**
 * Created by 陈新宇 on 2016/9/19.
 * 判断股票代码市场
 */
public class JudgeStockUtils {
    public static String HU_INDEX = "10";      //沪其他
    public static String HU_A= "11";      //沪A
    public static String HU_B= "12";      //沪B
    public static String HU_BOND = "13";      //沪债券
    public static String HU_FUND = "15";      //沪基金
    public static String HU_OTHER= "16";      //沪其他

    public static String SHEN_INDEX = "20";      //深指数
    public static String SHEN_A = "21";          //深A
    public static String SHEN_B = "22";          //深B
    public static String SHEN_BOND = "23";       //深债券
    public static String SHEN_FUND = "24";       //深基金
    public static String SHEN_GEM = "26";        //创业板
    public static String SHEN_SMS_BOARD = "27";  //深中小板
    public static String SHEN_OTHER = "28";      //深其他


    /**
     * 判断股票是否可以交易
     */
    public static boolean getStockWay(String code) {
        boolean flag = false;
        switch (code.substring(0, 2)) {
            case "10":
                flag = false;
                break;
            case "11":
                flag = true;
                break;
            case "12":
                flag = true;
                break;
            case "13":
                flag = false;
                break;
            case "15":
                flag = true;
                break;
            case "16":
                flag = true;
                break;
            case "20":
                flag = false;
                break;
            case "21":
                flag = true;
                break;
            case "22":
                flag = false;
                break;
            case "23":
                flag = true;
                break;
            case "24":
                flag = true;
                break;
            case "26":
                flag = true;
                break;
            case "27":
                flag = true;
                break;
            case "28":
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }
    /**
     * 判断市场     （1. 上海 2.深圳，3.深B 4.沪B）
     */
    /**
     * 判断复杂市场     （1. 上海 2.深圳，3.深B 4.沪B）
     */
    public static String getStockMarketCode(String code) {
        String backNum = "";
        switch (code.substring(0,2)) {
            case "10":
                backNum = "1";
                break;
            case "11":
                backNum = "1";
                break;
            case "12":
                backNum = "4";
                break;
            case "13":
                backNum = "1";
                break;
            case "14":
                backNum = "1";
                break;
            case "16":
                backNum = "1";
                break;
            case "17":
                backNum = "1";
                break;
            case "20":
                backNum = "2";
                break;
            case "21":
                backNum = "2";
                break;
            case "22":
                backNum = "3";
                break;
            case "23":
                backNum = "2";
                break;
            case "24":
                backNum = "2";
                break;
            case "26":
                backNum = "2";
                break;
            case "27":
                backNum = "2";
                break;
            case "28":
                backNum = "2";
                break;
            case "29":
                backNum = "2";
                break;
            default:
                backNum = "";
                break;
        }
        return backNum;
    }

    /**
     * 判断市场     （SH 上海 SZ 深圳）
     */
    public static String getStockMarket(String code) {
        if (TextUtils.isEmpty(code)) {
            return "";
        }
        String backNum = "";
        String market = code.substring(0, 1);
        if ("1".equals(market)) {
            backNum = "SH";
        } else if ("2".equals(market)) {
            backNum = "SZ";
        }
        return backNum;
    }

}
