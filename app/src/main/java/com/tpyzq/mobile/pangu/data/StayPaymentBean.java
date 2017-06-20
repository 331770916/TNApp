package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by 刘泽鹏 on 2016/8/12.
 * 待缴款  Fragment 界面的 实体类
 */
public class StayPaymentBean {


    /**
     * code : 0
     * msg : (中签待缴费查询成功)
     * data : [{"TOTAL_MONEY":"0.00","IPO_LIST":[{"STOCK_CODE":"730909","STOCK_STEP":"0","IPO_SHORT_BALANCE":"0","IPO_ACCANCEL_AMOUNT":"0","STOCK_NAME":"华安申购","IPO_INFO_STATUS":"0","TOTAL_AMOUNT":"2000.00","IPO_LUCKY_AMOUNT":"0","LUCKY_BALANCE":"0","IPO_PACANCEL_AMOUNT":"0","INIT_DATE":"20161123","T1":"20161124","ISSUE_PRICE":"6.410","T2":"20161125"},{"STOCK_CODE":"300572","STOCK_STEP":"0","IPO_SHORT_BALANCE":"0","IPO_ACCANCEL_AMOUNT":"0","STOCK_NAME":"安车检测","IPO_INFO_STATUS":"0","TOTAL_AMOUNT":"2000.00","IPO_LUCKY_AMOUNT":"0","LUCKY_BALANCE":"0","IPO_PACANCEL_AMOUNT":"0","INIT_DATE":"20161123","T1":"20161124","ISSUE_PRICE":"13.790","T2":"20161125"}],"TOTAL_SHORT_MONEY":"0.00"}]
     */

    private String code;
    private String msg;
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
        /**
         * TOTAL_MONEY : 0.00
         * IPO_LIST : [{"STOCK_CODE":"730909","STOCK_STEP":"0","IPO_SHORT_BALANCE":"0","IPO_ACCANCEL_AMOUNT":"0","STOCK_NAME":"华安申购","IPO_INFO_STATUS":"0","TOTAL_AMOUNT":"2000.00","IPO_LUCKY_AMOUNT":"0","LUCKY_BALANCE":"0","IPO_PACANCEL_AMOUNT":"0","INIT_DATE":"20161123","T1":"20161124","ISSUE_PRICE":"6.410","T2":"20161125"},{"STOCK_CODE":"300572","STOCK_STEP":"0","IPO_SHORT_BALANCE":"0","IPO_ACCANCEL_AMOUNT":"0","STOCK_NAME":"安车检测","IPO_INFO_STATUS":"0","TOTAL_AMOUNT":"2000.00","IPO_LUCKY_AMOUNT":"0","LUCKY_BALANCE":"0","IPO_PACANCEL_AMOUNT":"0","INIT_DATE":"20161123","T1":"20161124","ISSUE_PRICE":"13.790","T2":"20161125"}]
         * TOTAL_SHORT_MONEY : 0.00
         */

        private String TOTAL_MONEY;
        private String TOTAL_SHORT_MONEY;
        private List<IPOLISTBean> IPO_LIST;

        public String getTOTAL_MONEY() {
            return TOTAL_MONEY;
        }

        public void setTOTAL_MONEY(String TOTAL_MONEY) {
            this.TOTAL_MONEY = TOTAL_MONEY;
        }

        public String getTOTAL_SHORT_MONEY() {
            return TOTAL_SHORT_MONEY;
        }

        public void setTOTAL_SHORT_MONEY(String TOTAL_SHORT_MONEY) {
            this.TOTAL_SHORT_MONEY = TOTAL_SHORT_MONEY;
        }

        public List<IPOLISTBean> getIPO_LIST() {
            return IPO_LIST;
        }

        public void setIPO_LIST(List<IPOLISTBean> IPO_LIST) {
            this.IPO_LIST = IPO_LIST;
        }

        public static class IPOLISTBean {
            /**
             * STOCK_CODE : 730909
             * STOCK_STEP : 0
             * IPO_SHORT_BALANCE : 0
             * IPO_ACCANCEL_AMOUNT : 0
             * STOCK_NAME : 华安申购
             * IPO_INFO_STATUS : 0
             * TOTAL_AMOUNT : 2000.00
             * IPO_LUCKY_AMOUNT : 0
             * LUCKY_BALANCE : 0
             * IPO_PACANCEL_AMOUNT : 0
             * INIT_DATE : 20161123
             * T1 : 20161124
             * ISSUE_PRICE : 6.410
             * T2 : 20161125
             */

            private String STOCK_CODE;
            private String STOCK_STEP;
            private String IPO_SHORT_BALANCE;
            private String IPO_ACCANCEL_AMOUNT;
            private String STOCK_NAME;
            private String IPO_INFO_STATUS;
            private String TOTAL_AMOUNT;
            private String IPO_LUCKY_AMOUNT;
            private String LUCKY_BALANCE;
            private String IPO_PACANCEL_AMOUNT;
            private String INIT_DATE;
            private String T1;
            private String ISSUE_PRICE;
            private String T2;

            public String getSTOCK_CODE() {
                return STOCK_CODE;
            }

            public void setSTOCK_CODE(String STOCK_CODE) {
                this.STOCK_CODE = STOCK_CODE;
            }

            public String getSTOCK_STEP() {
                return STOCK_STEP;
            }

            public void setSTOCK_STEP(String STOCK_STEP) {
                this.STOCK_STEP = STOCK_STEP;
            }

            public String getIPO_SHORT_BALANCE() {
                return IPO_SHORT_BALANCE;
            }

            public void setIPO_SHORT_BALANCE(String IPO_SHORT_BALANCE) {
                this.IPO_SHORT_BALANCE = IPO_SHORT_BALANCE;
            }

            public String getIPO_ACCANCEL_AMOUNT() {
                return IPO_ACCANCEL_AMOUNT;
            }

            public void setIPO_ACCANCEL_AMOUNT(String IPO_ACCANCEL_AMOUNT) {
                this.IPO_ACCANCEL_AMOUNT = IPO_ACCANCEL_AMOUNT;
            }

            public String getSTOCK_NAME() {
                return STOCK_NAME;
            }

            public void setSTOCK_NAME(String STOCK_NAME) {
                this.STOCK_NAME = STOCK_NAME;
            }

            public String getIPO_INFO_STATUS() {
                return IPO_INFO_STATUS;
            }

            public void setIPO_INFO_STATUS(String IPO_INFO_STATUS) {
                this.IPO_INFO_STATUS = IPO_INFO_STATUS;
            }

            public String getTOTAL_AMOUNT() {
                return TOTAL_AMOUNT;
            }

            public void setTOTAL_AMOUNT(String TOTAL_AMOUNT) {
                this.TOTAL_AMOUNT = TOTAL_AMOUNT;
            }

            public String getIPO_LUCKY_AMOUNT() {
                return IPO_LUCKY_AMOUNT;
            }

            public void setIPO_LUCKY_AMOUNT(String IPO_LUCKY_AMOUNT) {
                this.IPO_LUCKY_AMOUNT = IPO_LUCKY_AMOUNT;
            }

            public String getLUCKY_BALANCE() {
                return LUCKY_BALANCE;
            }

            public void setLUCKY_BALANCE(String LUCKY_BALANCE) {
                this.LUCKY_BALANCE = LUCKY_BALANCE;
            }

            public String getIPO_PACANCEL_AMOUNT() {
                return IPO_PACANCEL_AMOUNT;
            }

            public void setIPO_PACANCEL_AMOUNT(String IPO_PACANCEL_AMOUNT) {
                this.IPO_PACANCEL_AMOUNT = IPO_PACANCEL_AMOUNT;
            }

            public String getINIT_DATE() {
                return INIT_DATE;
            }

            public void setINIT_DATE(String INIT_DATE) {
                this.INIT_DATE = INIT_DATE;
            }

            public String getT1() {
                return T1;
            }

            public void setT1(String T1) {
                this.T1 = T1;
            }

            public String getISSUE_PRICE() {
                return ISSUE_PRICE;
            }

            public void setISSUE_PRICE(String ISSUE_PRICE) {
                this.ISSUE_PRICE = ISSUE_PRICE;
            }

            public String getT2() {
                return T2;
            }

            public void setT2(String T2) {
                this.T2 = T2;
            }
        }
    }
}
