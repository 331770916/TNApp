package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * 作者：刘泽鹏 on 2016/8/29 17:34
 */
public class OTC_SubscriptionListEntity implements Serializable {
    private String stockName;
    private String stockCode;

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
}
