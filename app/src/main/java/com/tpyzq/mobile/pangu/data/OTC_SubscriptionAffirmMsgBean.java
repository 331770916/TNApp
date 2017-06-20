package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/8/29 14:05
 */
public class OTC_SubscriptionAffirmMsgBean {

    /**
     * code : 0
     * msg : 337490成功
     * data : [{"LOW_AMOUNT":"0","TRANSMIT_AMOUNT":"0","ERROR_NO":"0","MSG_CODE":"","PROD_CODE":"S46676","LAST_PRICE":"1.0000","ERROR_INFO":"","MSG_TEXT":"","MONEY_TYPE":"0","HAND_FLAG":"0","STOCK_INTEREST":"0","ENABLE_AMOUNT":"0","HIGH_AMOUNT":"100000000.00","ENABLE_BALANCE":"1000003692.62","DOWN_PRICE":"0","PROD_NAME":"彩云月月鑫6号","UP_PRICE":"10000000.000","PRODTA_NO":"CZZ","COST_PRICE":"0"}]
     */

    private String code;
    private String msg;
    /**
     * LOW_AMOUNT : 0
     * TRANSMIT_AMOUNT : 0
     * ERROR_NO : 0
     * MSG_CODE :
     * PROD_CODE : S46676
     * LAST_PRICE : 1.0000
     * ERROR_INFO :
     * MSG_TEXT :
     * MONEY_TYPE : 0
     * HAND_FLAG : 0
     * STOCK_INTEREST : 0
     * ENABLE_AMOUNT : 0
     * HIGH_AMOUNT : 100000000.00
     * ENABLE_BALANCE : 1000003692.62
     * DOWN_PRICE : 0
     * PROD_NAME : 彩云月月鑫6号
     * UP_PRICE : 10000000.000
     * PRODTA_NO : CZZ
     * COST_PRICE : 0
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
        private String LOW_AMOUNT;
        private String TRANSMIT_AMOUNT;
        private String ERROR_NO;
        private String MSG_CODE;
        private String PROD_CODE;
        private String LAST_PRICE;
        private String ERROR_INFO;
        private String MSG_TEXT;
        private String MONEY_TYPE;
        private String HAND_FLAG;
        private String STOCK_INTEREST;
        private String ENABLE_AMOUNT;
        private String HIGH_AMOUNT;
        private String ENABLE_BALANCE;
        private String DOWN_PRICE;
        private String PROD_NAME;
        private String UP_PRICE;
        private String PRODTA_NO;
        private String COST_PRICE;

        public String getLOW_AMOUNT() {
            return LOW_AMOUNT;
        }

        public void setLOW_AMOUNT(String LOW_AMOUNT) {
            this.LOW_AMOUNT = LOW_AMOUNT;
        }

        public String getTRANSMIT_AMOUNT() {
            return TRANSMIT_AMOUNT;
        }

        public void setTRANSMIT_AMOUNT(String TRANSMIT_AMOUNT) {
            this.TRANSMIT_AMOUNT = TRANSMIT_AMOUNT;
        }

        public String getERROR_NO() {
            return ERROR_NO;
        }

        public void setERROR_NO(String ERROR_NO) {
            this.ERROR_NO = ERROR_NO;
        }

        public String getMSG_CODE() {
            return MSG_CODE;
        }

        public void setMSG_CODE(String MSG_CODE) {
            this.MSG_CODE = MSG_CODE;
        }

        public String getPROD_CODE() {
            return PROD_CODE;
        }

        public void setPROD_CODE(String PROD_CODE) {
            this.PROD_CODE = PROD_CODE;
        }

        public String getLAST_PRICE() {
            return LAST_PRICE;
        }

        public void setLAST_PRICE(String LAST_PRICE) {
            this.LAST_PRICE = LAST_PRICE;
        }

        public String getERROR_INFO() {
            return ERROR_INFO;
        }

        public void setERROR_INFO(String ERROR_INFO) {
            this.ERROR_INFO = ERROR_INFO;
        }

        public String getMSG_TEXT() {
            return MSG_TEXT;
        }

        public void setMSG_TEXT(String MSG_TEXT) {
            this.MSG_TEXT = MSG_TEXT;
        }

        public String getMONEY_TYPE() {
            return MONEY_TYPE;
        }

        public void setMONEY_TYPE(String MONEY_TYPE) {
            this.MONEY_TYPE = MONEY_TYPE;
        }

        public String getHAND_FLAG() {
            return HAND_FLAG;
        }

        public void setHAND_FLAG(String HAND_FLAG) {
            this.HAND_FLAG = HAND_FLAG;
        }

        public String getSTOCK_INTEREST() {
            return STOCK_INTEREST;
        }

        public void setSTOCK_INTEREST(String STOCK_INTEREST) {
            this.STOCK_INTEREST = STOCK_INTEREST;
        }

        public String getENABLE_AMOUNT() {
            return ENABLE_AMOUNT;
        }

        public void setENABLE_AMOUNT(String ENABLE_AMOUNT) {
            this.ENABLE_AMOUNT = ENABLE_AMOUNT;
        }

        public String getHIGH_AMOUNT() {
            return HIGH_AMOUNT;
        }

        public void setHIGH_AMOUNT(String HIGH_AMOUNT) {
            this.HIGH_AMOUNT = HIGH_AMOUNT;
        }

        public String getENABLE_BALANCE() {
            return ENABLE_BALANCE;
        }

        public void setENABLE_BALANCE(String ENABLE_BALANCE) {
            this.ENABLE_BALANCE = ENABLE_BALANCE;
        }

        public String getDOWN_PRICE() {
            return DOWN_PRICE;
        }

        public void setDOWN_PRICE(String DOWN_PRICE) {
            this.DOWN_PRICE = DOWN_PRICE;
        }

        public String getPROD_NAME() {
            return PROD_NAME;
        }

        public void setPROD_NAME(String PROD_NAME) {
            this.PROD_NAME = PROD_NAME;
        }

        public String getUP_PRICE() {
            return UP_PRICE;
        }

        public void setUP_PRICE(String UP_PRICE) {
            this.UP_PRICE = UP_PRICE;
        }

        public String getPRODTA_NO() {
            return PRODTA_NO;
        }

        public void setPRODTA_NO(String PRODTA_NO) {
            this.PRODTA_NO = PRODTA_NO;
        }

        public String getCOST_PRICE() {
            return COST_PRICE;
        }

        public void setCOST_PRICE(String COST_PRICE) {
            this.COST_PRICE = COST_PRICE;
        }
    }
}
