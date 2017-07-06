package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/6 15:38
 */
public class CurrencyFundEntrustThreeMonthBean {

    /**
     * code : 0
     * msg : (货币基金历史委托查询成功)
     * data : [{"KEY_STR":"20160701021021347830012000001694","ORDER_DATE":"20160701","PRICE":"101.385","SECU_ACC":"A206775891","ENTRUST_STATUS":"8","ORDER_TIME":"102134","MARKET":"1","ENTRUST_PROP":"0","SECU_CODE":"511880","BUSINESS_NAME":"货币基金申赎","ORDER_ID":"1694","MATCHED_QTY":"6500.00","MATCHED_AMT":"659002.50","WITHDRAWN_QTY":"0","IS_WITHDRAW":"0","QTY":"6500.00","ENTRUST_BS":"2","ENTRUST_NO":"1694","SECU_NAME":"银华日利","STOCK_TYPE":"j"},{"KEY_STR":"20160708021027180020012000002045","ORDER_DATE":"20160708","PRICE":"101.438","SECU_ACC":"A206775891","ENTRUST_STATUS":"8","ORDER_TIME":"102718","MARKET":"1","ENTRUST_PROP":"0","SECU_CODE":"511880","BUSINESS_NAME":"货币基金申赎","ORDER_ID":"2045","MATCHED_QTY":"5000.00","MATCHED_AMT":"507190.00","WITHDRAWN_QTY":"0","IS_WITHDRAW":"0","QTY":"5000.00","ENTRUST_BS":"2","ENTRUST_NO":"2045","SECU_NAME":"银华日利","STOCK_TYPE":"j"},{"KEY_STR":"20160713020950107450012000001329","ORDER_DATE":"20160713","PRICE":"101.459","SECU_ACC":"A206775891","ENTRUST_STATUS":"8","ORDER_TIME":"95010","MARKET":"1","ENTRUST_PROP":"0","SECU_CODE":"511880","BUSINESS_NAME":"货币基金申赎","ORDER_ID":"1329","MATCHED_QTY":"5000.00","MATCHED_AMT":"507295.00","WITHDRAWN_QTY":"0","IS_WITHDRAW":"0","QTY":"5000.00","ENTRUST_BS":"2","ENTRUST_NO":"1329","SECU_NAME":"银华日利","STOCK_TYPE":"j"}]
     */

    private String code;
    private String msg;
    /**
     * KEY_STR : 20160701021021347830012000001694
     * ORDER_DATE : 20160701
     * PRICE : 101.385
     * SECU_ACC : A206775891
     * ENTRUST_STATUS : 8
     * ORDER_TIME : 102134
     * MARKET : 1
     * ENTRUST_PROP : 0
     * SECU_CODE : 511880
     * BUSINESS_NAME : 货币基金申赎
     * ORDER_ID : 1694
     * MATCHED_QTY : 6500.00
     * MATCHED_AMT : 659002.50
     * WITHDRAWN_QTY : 0
     * IS_WITHDRAW : 0
     * QTY : 6500.00
     * ENTRUST_BS : 2
     * ENTRUST_NO : 1694
     * SECU_NAME : 银华日利
     * STOCK_TYPE : j
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
        private String MARKET;
        private String ENTRUST_PROP;
        private String SECU_CODE;
        private String BUSINESS_NAME;
        private String ORDER_ID;
        private String MATCHED_QTY;
        private String MATCHED_AMT;
        private String WITHDRAWN_QTY;
        private String IS_WITHDRAW;
        private String QTY;
        private String ENTRUST_BS;
        private String ENTRUST_NO;
        private String SECU_NAME;
        private String STOCK_TYPE;

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

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }

        public String getENTRUST_PROP() {
            return ENTRUST_PROP;
        }

        public void setENTRUST_PROP(String ENTRUST_PROP) {
            this.ENTRUST_PROP = ENTRUST_PROP;
        }

        public String getSECU_CODE() {
            return SECU_CODE;
        }

        public void setSECU_CODE(String SECU_CODE) {
            this.SECU_CODE = SECU_CODE;
        }

        public String getBUSINESS_NAME() {
            return BUSINESS_NAME;
        }

        public void setBUSINESS_NAME(String BUSINESS_NAME) {
            this.BUSINESS_NAME = BUSINESS_NAME;
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

        public String getMATCHED_AMT() {
            return MATCHED_AMT;
        }

        public void setMATCHED_AMT(String MATCHED_AMT) {
            this.MATCHED_AMT = MATCHED_AMT;
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

        public String getSTOCK_TYPE() {
            return STOCK_TYPE;
        }

        public void setSTOCK_TYPE(String STOCK_TYPE) {
            this.STOCK_TYPE = STOCK_TYPE;
        }
    }
}
