package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2016/7/28.
 * 委托查询   今日   一周   一月  三月  自定义 实体类
 */
public class TodayEntrustEntity {
    private String code;              //0 成功  -6session失效
    private String msg;

    private String Name;              //股票名称
    private String Num;               //价格
    private String Date;              //委托年月日
    private String Time;              //委托时间
    private String Entrust;           //委托
    private String Succeed;           //成交
    private String Transaction;       //买卖
    private String Status;            //状态

    private List<WtBeanData> data;

    public static class WtBeanData {
        private String KEY_STR;
        private String ORDER_DATE;
        private String PRICE;
        private String SECU_ACC;
        private String ENTRUST_STATUS;
        private String ORDER_TIME;
        private String MARKET;
        private String ENTRUST_PROP;
        private String SECU_CODE;
        private String ORDER_ID;
        private String MATCHED_QTY;
        private String MATCHED_AMT;
        private String WITHDRAWN_QTY;
        private String IS_WITHDRAW;
        private String QTY;
        private String ENTRUST_BS;
        private String ENTRUST_NO;
        private String SECU_NAME;
        private String ORDER_AMT;  //今日委托   金额
        private String MATCHED_PRICE;//今天委托 成交价格
        private String ACTION_IN;//今日委托  状态
        private String STATUS_NAME;

        public String getSTATUS_NAME() {
            return STATUS_NAME;
        }

        public void setSTATUS_NAME(String STATUS_NAME) {
            this.STATUS_NAME = STATUS_NAME;
        }

        public String getACTION_IN() {
            return ACTION_IN;
        }

        public void setACTION_IN(String ACTION_IN) {
            this.ACTION_IN = ACTION_IN;
        }

        public String getORDER_AMT() {
            return ORDER_AMT;
        }

        public void setORDER_AMT(String ORDER_AMT) {
            this.ORDER_AMT = ORDER_AMT;
        }

        public String getMATCHED_PRICE() {
            return MATCHED_PRICE;
        }

        public void setMATCHED_PRICE(String MATCHED_PRICE) {
            this.MATCHED_PRICE = MATCHED_PRICE;
        }

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
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getEntrust() {
        return Entrust;
    }

    public void setEntrust(String entrust) {
        Entrust = entrust;
    }

    public String getSucceed() {
        return Succeed;
    }

    public void setSucceed(String succeed) {
        Succeed = succeed;
    }

    public String getTransaction() {
        return Transaction;
    }

    public void setTransaction(String transaction) {
        Transaction = transaction;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<WtBeanData> getData() {
        return data;
    }

    public void setData(List<WtBeanData> data) {
        this.data = data;
    }

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
}
