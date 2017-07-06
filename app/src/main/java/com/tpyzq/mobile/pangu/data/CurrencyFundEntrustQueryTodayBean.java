package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/8/23 17:53
 */
public class CurrencyFundEntrustQueryTodayBean {

    /**
     * code : 0
     * msg : (货币基金委托查询成功)
     * data : [{"KEY_STR":"20160823021513052440012000000002","ORDER_DATE":"20160823","PRICE":"1.000","SECU_ACC":"0020844754","ENTRUST_STATUS":"9","ORDER_TIME":"151305","MATCHED_PRICE":"0","ENTRUST_PROP":"OFC","MARKET":"2","SECU_CODE":"159001","ORDER_ID":"2","MATCHED_QTY":"0","BUSINESS_NAME":"货币基金申购","WITHDRAWN_QTY":"0","IS_WITHDRAW":"0","ACTION_IN":"","QTY":"100.00","ENTRUST_BS":"1","ENTRUST_NO":"2","SECU_NAME":"保证金"},{"KEY_STR":"20160823021537211640012000000003","ORDER_DATE":"20160823","PRICE":"1.000","SECU_ACC":"0020844754","ENTRUST_STATUS":"9","ORDER_TIME":"153721","MATCHED_PRICE":"0","ENTRUST_PROP":"OFC","MARKET":"2","SECU_CODE":"159001","ORDER_ID":"3","MATCHED_QTY":"0","BUSINESS_NAME":"货币基金申购","WITHDRAWN_QTY":"0","IS_WITHDRAW":"0","ACTION_IN":"","QTY":"100.00","ENTRUST_BS":"1","ENTRUST_NO":"3","SECU_NAME":"保证金"},{"KEY_STR":"20160823021537252150012000000004","ORDER_DATE":"20160823","PRICE":"1.000","SECU_ACC":"0020844754","ENTRUST_STATUS":"9","ORDER_TIME":"153725","MATCHED_PRICE":"0","ENTRUST_PROP":"OFC","MARKET":"2","SECU_CODE":"159001","ORDER_ID":"4","MATCHED_QTY":"0","BUSINESS_NAME":"货币基金申购","WITHDRAWN_QTY":"0","IS_WITHDRAW":"0","ACTION_IN":"","QTY":"100.00","ENTRUST_BS":"1","ENTRUST_NO":"4","SECU_NAME":"保证金"},{"KEY_STR":"20160823021537512050012000000005","ORDER_DATE":"20160823","PRICE":"1.000","SECU_ACC":"A206775891","ENTRUST_STATUS":"2","ORDER_TIME":"153751","MATCHED_PRICE":"0","ENTRUST_PROP":"OFC","MARKET":"1","SECU_CODE":"511880","ORDER_ID":"5","MATCHED_QTY":"0","BUSINESS_NAME":"货币基金申购","WITHDRAWN_QTY":"0","IS_WITHDRAW":"0","ACTION_IN":"","QTY":"100.00","ENTRUST_BS":"1","ENTRUST_NO":"5","SECU_NAME":"银华日利"}]
     */

    private String code;
    private String msg;
    /**
     * KEY_STR : 20160823021513052440012000000002
     * ORDER_DATE : 20160823
     * PRICE : 1.000
     * SECU_ACC : 0020844754
     * ENTRUST_STATUS : 9
     * ORDER_TIME : 151305
     * MATCHED_PRICE : 0
     * ENTRUST_PROP : OFC
     * MARKET : 2
     * SECU_CODE : 159001
     * ORDER_ID : 2
     * MATCHED_QTY : 0
     * BUSINESS_NAME : 货币基金申购
     * WITHDRAWN_QTY : 0
     * IS_WITHDRAW : 0
     * ACTION_IN :
     * QTY : 100.00
     * ENTRUST_BS : 1
     * ENTRUST_NO : 2
     * SECU_NAME : 保证金
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
        private String KEY_STR;
        private String ORDER_DATE;
        private String PRICE;
        private String SECU_ACC;
        private String ENTRUST_STATUS;
        private String ORDER_TIME;
        private String MATCHED_PRICE;
        private String ENTRUST_PROP;
        private String MARKET;
        private String SECU_CODE;
        private String ORDER_ID;
        private String MATCHED_QTY;
        private String BUSINESS_NAME;
        private String WITHDRAWN_QTY;
        private String IS_WITHDRAW;
        private String ACTION_IN;
        private String QTY;
        private String ENTRUST_BS;
        private String ENTRUST_NO;
        private String SECU_NAME;

        public String getKEY_STR() {
            return KEY_STR;
        }

        public void setKEY_STR(String KEY_STR) {
            this.KEY_STR = KEY_STR;
        }

        public String getORDER_DATE() {
            return ORDER_DATE;
        }

        public void setORDER_DATE(String ORDER_DATE) {
            this.ORDER_DATE = ORDER_DATE;
        }

        public String getPRICE() {
            return PRICE;
        }

        public void setPRICE(String PRICE) {
            this.PRICE = PRICE;
        }

        public String getSECU_ACC() {
            return SECU_ACC;
        }

        public void setSECU_ACC(String SECU_ACC) {
            this.SECU_ACC = SECU_ACC;
        }

        public String getENTRUST_STATUS() {
            return ENTRUST_STATUS;
        }

        public void setENTRUST_STATUS(String ENTRUST_STATUS) {
            this.ENTRUST_STATUS = ENTRUST_STATUS;
        }

        public String getORDER_TIME() {
            return ORDER_TIME;
        }

        public void setORDER_TIME(String ORDER_TIME) {
            this.ORDER_TIME = ORDER_TIME;
        }

        public String getMATCHED_PRICE() {
            return MATCHED_PRICE;
        }

        public void setMATCHED_PRICE(String MATCHED_PRICE) {
            this.MATCHED_PRICE = MATCHED_PRICE;
        }

        public String getENTRUST_PROP() {
            return ENTRUST_PROP;
        }

        public void setENTRUST_PROP(String ENTRUST_PROP) {
            this.ENTRUST_PROP = ENTRUST_PROP;
        }

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }

        public String getSECU_CODE() {
            return SECU_CODE;
        }

        public void setSECU_CODE(String SECU_CODE) {
            this.SECU_CODE = SECU_CODE;
        }

        public String getORDER_ID() {
            return ORDER_ID;
        }

        public void setORDER_ID(String ORDER_ID) {
            this.ORDER_ID = ORDER_ID;
        }

        public String getMATCHED_QTY() {
            return MATCHED_QTY;
        }

        public void setMATCHED_QTY(String MATCHED_QTY) {
            this.MATCHED_QTY = MATCHED_QTY;
        }

        public String getBUSINESS_NAME() {
            return BUSINESS_NAME;
        }

        public void setBUSINESS_NAME(String BUSINESS_NAME) {
            this.BUSINESS_NAME = BUSINESS_NAME;
        }

        public String getWITHDRAWN_QTY() {
            return WITHDRAWN_QTY;
        }

        public void setWITHDRAWN_QTY(String WITHDRAWN_QTY) {
            this.WITHDRAWN_QTY = WITHDRAWN_QTY;
        }

        public String getIS_WITHDRAW() {
            return IS_WITHDRAW;
        }

        public void setIS_WITHDRAW(String IS_WITHDRAW) {
            this.IS_WITHDRAW = IS_WITHDRAW;
        }

        public String getACTION_IN() {
            return ACTION_IN;
        }

        public void setACTION_IN(String ACTION_IN) {
            this.ACTION_IN = ACTION_IN;
        }

        public String getQTY() {
            return QTY;
        }

        public void setQTY(String QTY) {
            this.QTY = QTY;
        }

        public String getENTRUST_BS() {
            return ENTRUST_BS;
        }

        public void setENTRUST_BS(String ENTRUST_BS) {
            this.ENTRUST_BS = ENTRUST_BS;
        }

        public String getENTRUST_NO() {
            return ENTRUST_NO;
        }

        public void setENTRUST_NO(String ENTRUST_NO) {
            this.ENTRUST_NO = ENTRUST_NO;
        }

        public String getSECU_NAME() {
            return SECU_NAME;
        }

        public void setSECU_NAME(String SECU_NAME) {
            this.SECU_NAME = SECU_NAME;
        }
    }
}
