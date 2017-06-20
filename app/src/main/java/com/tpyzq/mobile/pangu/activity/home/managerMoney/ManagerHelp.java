package com.tpyzq.mobile.pangu.activity.home.managerMoney;

import android.text.TextUtils;

/**
 * Created by zhangwenbo on 2016/10/18.
 * 智能选理财帮助类
 */
public class ManagerHelp {

    public static final String DISLEVEL = "0";              //默认风险等级 服务器规定
    public static final String NODANGERLEVEL = "1";         //保本风险等级 服务器规定
    public static final String LOWLEVEL = "2";              //低风险等级 服务器规定
    public static final String MIDDLELEVEL = "3";           //中风险等级 服务器规定
    public static final String HIGHTLEVEL = "4";            //高风险等级 服务器规定

    public static final String OTC_L = "1";                  //otc 固定类型 服务器规定
    public static final String OTC_F = "2";                  //otc 浮动类型 服务器规定
    public static final String OTC_FL = "3";                 //otc 固定加浮动 服务器规定

    public static final int OTC_L_INT = 1;                  //otc 固定类型 服务器规定
    public static final int OTC_F_INT = 2;                  //otc 浮动类型 服务器规定
    public static final int OTC_FL_INT = 3;                 //otc 固定加浮动 服务器规定

    public static final String MANAGERMONEYTYPE_FOURTEEN = "3"; //14天类型
    public static final int    MANAGERMONEYTYPE_FOURTEEN_INT = 3;


    public static String getLevel(String riskLevel) {
        String strLevel = "";
        if (!TextUtils.isEmpty(riskLevel)) {
            if (DISLEVEL.equals(riskLevel)) {
                strLevel = "默认";
            } else if (NODANGERLEVEL.equals(riskLevel)) {
                strLevel = "保本";
            } else if (LOWLEVEL.equals(riskLevel)) {
                strLevel = "低风险";
            } else if (MIDDLELEVEL.equals(riskLevel)) {
                strLevel = "中风险";
            } else if (HIGHTLEVEL.equals(riskLevel)) {
                strLevel = "高风险";
            }
        }
        return strLevel;
    }
}
