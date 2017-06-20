package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by 刘泽鹏 on 2016/8/5.
 * 一键申购界面  上面沪A  深A 可购买数量 的实体类
 */
public class OneKeySubscribeBean {

    /**
     * code : 0
     * msg : 333039成功
     * data : [{"STOCK_ACCOUNT":"A334622448","FUND_ACCOUNT":"101000913","ENABLE_AMOUNT":"1000.00","INIT_DATE":"20151231","MARKET":"1"},{"STOCK_ACCOUNT":"0188690802","FUND_ACCOUNT":"101000913","ENABLE_AMOUNT":"3000.00","INIT_DATE":"20151231","MARKET":"2"}]
     */

    private String code;
    private String msg;
    /**
     * STOCK_ACCOUNT : A334622448
     * FUND_ACCOUNT : 101000913
     * ENABLE_AMOUNT : 1000.00
     * INIT_DATE : 20151231
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
        private String STOCK_ACCOUNT;
        private String FUND_ACCOUNT;
        private String ENABLE_AMOUNT;
        private String INIT_DATE;
        private String MARKET;

        public String getSTOCK_ACCOUNT() {
            return STOCK_ACCOUNT;
        }

        public void setSTOCK_ACCOUNT(String STOCK_ACCOUNT) {
            this.STOCK_ACCOUNT = STOCK_ACCOUNT;
        }

        public String getFUND_ACCOUNT() {
            return FUND_ACCOUNT;
        }

        public void setFUND_ACCOUNT(String FUND_ACCOUNT) {
            this.FUND_ACCOUNT = FUND_ACCOUNT;
        }

        public String getENABLE_AMOUNT() {
            return ENABLE_AMOUNT;
        }

        public void setENABLE_AMOUNT(String ENABLE_AMOUNT) {
            this.ENABLE_AMOUNT = ENABLE_AMOUNT;
        }

        public String getINIT_DATE() {
            return INIT_DATE;
        }

        public void setINIT_DATE(String INIT_DATE) {
            this.INIT_DATE = INIT_DATE;
        }

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }
    }
}
