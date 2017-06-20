package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by 刘泽鹏 on 2016/8/13.
 * 申购历史界面    基本信息的 实体类
 */
public class SubscribeHistoryBasicBean {

    /**
     * code : 0
     * msg : (申购历史查询300383)
     * data : [{"STOCK_CODE":"300523","ENTRUST_AMOUNT":"16500.00","OCCUR_AMOUNT":"0","ENTRUST_DATE":"20160713","STOCK_NAME":"辰安科技","INIT_DATE":" ","ENTRUST_PRICE":"21.920"},{"STOCK_CODE":"002806","ENTRUST_AMOUNT":"17000.00","OCCUR_AMOUNT":"0","ENTRUST_DATE":"20160714","STOCK_NAME":"华锋股份","INIT_DATE":" ","ENTRUST_PRICE":"6.200"},{"STOCK_CODE":"300525","ENTRUST_AMOUNT":"17000.00","OCCUR_AMOUNT":"0","ENTRUST_DATE":"20160715","STOCK_NAME":"博思软件","INIT_DATE":" ","ENTRUST_PRICE":"11.680"}]
     */

    private String code;
    private String msg;
    /**
     * STOCK_CODE : 300523
     * ENTRUST_AMOUNT : 16500.00
     * OCCUR_AMOUNT : 0
     * ENTRUST_DATE : 20160713
     * STOCK_NAME : 辰安科技
     * INIT_DATE :
     * ENTRUST_PRICE : 21.920
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
        private String ENTRUST_AMOUNT;
        private String OCCUR_AMOUNT;
        private String ENTRUST_DATE;
        private String STOCK_NAME;
        private String INIT_DATE;
        private String ENTRUST_PRICE;

        public String getSTOCK_CODE() {
            return STOCK_CODE;
        }

        public void setSTOCK_CODE(String STOCK_CODE) {
            this.STOCK_CODE = STOCK_CODE;
        }

        public String getENTRUST_AMOUNT() {
            return ENTRUST_AMOUNT;
        }

        public void setENTRUST_AMOUNT(String ENTRUST_AMOUNT) {
            this.ENTRUST_AMOUNT = ENTRUST_AMOUNT;
        }

        public String getOCCUR_AMOUNT() {
            return OCCUR_AMOUNT;
        }

        public void setOCCUR_AMOUNT(String OCCUR_AMOUNT) {
            this.OCCUR_AMOUNT = OCCUR_AMOUNT;
        }

        public String getENTRUST_DATE() {
            return ENTRUST_DATE;
        }

        public void setENTRUST_DATE(String ENTRUST_DATE) {
            this.ENTRUST_DATE = ENTRUST_DATE;
        }

        public String getSTOCK_NAME() {
            return STOCK_NAME;
        }

        public void setSTOCK_NAME(String STOCK_NAME) {
            this.STOCK_NAME = STOCK_NAME;
        }

        public String getINIT_DATE() {
            return INIT_DATE;
        }

        public void setINIT_DATE(String INIT_DATE) {
            this.INIT_DATE = INIT_DATE;
        }

        public String getENTRUST_PRICE() {
            return ENTRUST_PRICE;
        }

        public void setENTRUST_PRICE(String ENTRUST_PRICE) {
            this.ENTRUST_PRICE = ENTRUST_PRICE;
        }
    }
}
