package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * anthor:Created by tianchen on 2017/4/6.
 * email:963181974@qq.com
 */

public class StockInfoBean implements Comparable ,Serializable{
    public String stockCode = "";
    public String stockName1 = "";
    public int stockName2 = 0;
    public String yearIncome = "";
    public String wYuanIncome = "";
    public String tenwYuanDayIncome = "";
    public String dj_time = "";//可取日期
    public String dq_time = "";//可用日期
    public String sq_time = "";//行情日期
    public String occupied_days ="";


    @Override
    public int compareTo(Object another) {
        StockInfoBean stockInfoBean = (StockInfoBean) another;
        return this.stockName2 - stockInfoBean.stockName2;
    }
}
