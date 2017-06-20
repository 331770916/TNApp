package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/30 10:03
 * 场内基金  认购 申购 赎回   查询实体类
 */
public class ChangNeiSubscriptionBean {

    /**
     * code : 0
     * msg : (查询成功)
     * data : [{"STOCK_ACCOUNT":"A768059900","EXCHANGE_TYPE":"1","STOCK_NAME":"红利ETF","ENABLE_AMOUNT":"","ENABLE_BALANCE":"10121612.63","NAV":""}]
     */

    private String code;
    private String msg;
    /**
     * STOCK_ACCOUNT : A768059900
     * EXCHANGE_TYPE : 1
     * STOCK_NAME : 红利ETF
     * ENABLE_AMOUNT :
     * ENABLE_BALANCE : 10121612.63
     * NAV :
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String STOCK_ACCOUNT;
        private String EXCHANGE_TYPE;
        private String STOCK_NAME;
        private String ENABLE_AMOUNT;
        private String ENABLE_BALANCE;
        private String NAV;

        public String getSTOCK_ACCOUNT() {
            return STOCK_ACCOUNT;
        }

        public void setSTOCK_ACCOUNT(String STOCK_ACCOUNT) {
            this.STOCK_ACCOUNT = STOCK_ACCOUNT;
        }

        public String getEXCHANGE_TYPE() {
            return EXCHANGE_TYPE;
        }

        public void setEXCHANGE_TYPE(String EXCHANGE_TYPE) {
            this.EXCHANGE_TYPE = EXCHANGE_TYPE;
        }

        public String getSTOCK_NAME() {
            return STOCK_NAME;
        }

        public void setSTOCK_NAME(String STOCK_NAME) {
            this.STOCK_NAME = STOCK_NAME;
        }

        public String getENABLE_AMOUNT() {
            return ENABLE_AMOUNT;
        }

        public void setENABLE_AMOUNT(String ENABLE_AMOUNT) {
            this.ENABLE_AMOUNT = ENABLE_AMOUNT;
        }

        public String getENABLE_BALANCE() {
            return ENABLE_BALANCE;
        }

        public void setENABLE_BALANCE(String ENABLE_BALANCE) {
            this.ENABLE_BALANCE = ENABLE_BALANCE;
        }

        public String getNAV() {
            return NAV;
        }

        public void setNAV(String NAV) {
            this.NAV = NAV;
        }
    }
}
