package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * 作者：刘泽鹏 on 2016/8/30 14:19
 * OTC申购页面传值的 实体类
 */
public class OTC_SubscribeEntity implements Serializable{
    private String stcokName;
    private String stockCode;
    private boolean flag;

    public String getStcokName() {
        return stcokName;
    }

    public void setStcokName(String stcokName) {
        this.stcokName = stcokName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
