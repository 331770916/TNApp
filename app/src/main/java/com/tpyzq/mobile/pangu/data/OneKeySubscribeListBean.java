package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by 刘泽鹏 on 2016/8/5.
 * 一键申购界面  下面的 listView 对应的实体类
 */
public class OneKeySubscribeListBean {

    /**
     * code : 0
     * msg : 330303成功
     * data : [{"STOCK_CODE":"780595","LOW_AMOUNT":"1000.00","BUY_UNIT":"1000","STOCK_NAME":"上影申购","HIGH_AMOUNT":"37000.00","DOWN_PRICE":"0","LAST_PRICE":"10.1900","UP_PRICE":"0","MARKET":"1"},{"STOCK_CODE":"300532","LOW_AMOUNT":"0","BUY_UNIT":"500","STOCK_NAME":"今天国际","HIGH_AMOUNT":"8000.00","DOWN_PRICE":"16.320","LAST_PRICE":"16.3200","UP_PRICE":"16.320","MARKET":"2"}]
     */

    private String code;
    private String msg;
    /**
     * STOCK_CODE : 780595
     * LOW_AMOUNT : 1000.00
     * BUY_UNIT : 1000
     * STOCK_NAME : 上影申购
     * HIGH_AMOUNT : 37000.00
     * DOWN_PRICE : 0
     * LAST_PRICE : 10.1900
     * UP_PRICE : 0
     * MARKET : 1
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
        private String STOCK_CODE;
        private String LOW_AMOUNT;
        private String BUY_UNIT;
        private String STOCK_NAME;
        private String HIGH_AMOUNT;
        private String DOWN_PRICE;
        private String LAST_PRICE;
        private String UP_PRICE;
        private String MARKET;

        public String getSTOCK_CODE() {
            return STOCK_CODE;
        }

        public void setSTOCK_CODE(String STOCK_CODE) {
            this.STOCK_CODE = STOCK_CODE;
        }

        public String getLOW_AMOUNT() {
            return LOW_AMOUNT;
        }

        public void setLOW_AMOUNT(String LOW_AMOUNT) {
            this.LOW_AMOUNT = LOW_AMOUNT;
        }

        public String getBUY_UNIT() {
            return BUY_UNIT;
        }

        public void setBUY_UNIT(String BUY_UNIT) {
            this.BUY_UNIT = BUY_UNIT;
        }

        public String getSTOCK_NAME() {
            return STOCK_NAME;
        }

        public void setSTOCK_NAME(String STOCK_NAME) {
            this.STOCK_NAME = STOCK_NAME;
        }

        public String getHIGH_AMOUNT() {
            return HIGH_AMOUNT;
        }

        public void setHIGH_AMOUNT(String HIGH_AMOUNT) {
            this.HIGH_AMOUNT = HIGH_AMOUNT;
        }

        public String getDOWN_PRICE() {
            return DOWN_PRICE;
        }

        public void setDOWN_PRICE(String DOWN_PRICE) {
            this.DOWN_PRICE = DOWN_PRICE;
        }

        public String getLAST_PRICE() {
            return LAST_PRICE;
        }

        public void setLAST_PRICE(String LAST_PRICE) {
            this.LAST_PRICE = LAST_PRICE;
        }

        public String getUP_PRICE() {
            return UP_PRICE;
        }

        public void setUP_PRICE(String UP_PRICE) {
            this.UP_PRICE = UP_PRICE;
        }

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }
    }
}
