package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2016/7/28.
 * 成交查询     今日   一周   一月  三月  自定义 实体类
 */
public class ClinchDealEntity {
    private String Name;              //名称
    private String Num;               //价格
    private String Date;              //成交年月日
    private String Time;              //成交时间
    private String Transaction;        //买卖
    private String Amount;            //数量
    private String Money;             //金额
    private String code;              //0 成功  -6session失效
    private String msg;

    private List<BeanData> data;

    public static class BeanData {
        private String KEY_STR;
        private String BUSINESS_AMOUNT;
        private String SECU_ACC;
        private String REMARK;
        private String MATCHED_PRICE;
        private String MARKET;              //今日交易市场 1:上海 2：深圳
        private String SECU_CODE;
        private String MATCHED_QTY;
        private String ORDER_ID;
        private String MATCHED_AMT;
        private String MATCHED_SN;
        private String MATCHED_DATE;
        private String MATCHED_TIME;
        private String ENTRUST_BS;
        private String BUSINESS_TYPE;
        private String BUSINESS_FLAG;
        private String SECU_NAME;
        private String TRD_ID;

        private String TRD_DATE;            //成交 今日交易日期
        private String REAL_TYPE;           //成交 今日0:成交 2：撤单
        private String REAL_STATUS;         //成交 今日0：成交 2：废单


        public String getTRD_DATE() {
            return TRD_DATE;
        }

        public void setTRD_DATE(String TRD_DATE) {
            this.TRD_DATE = TRD_DATE;
        }

        public String getREAL_TYPE() {
            return REAL_TYPE;
        }

        public void setREAL_TYPE(String REAL_TYPE) {
            this.REAL_TYPE = REAL_TYPE;
        }

        public String getREAL_STATUS() {
            return REAL_STATUS;
        }

        public void setREAL_STATUS(String REAL_STATUS) {
            this.REAL_STATUS = REAL_STATUS;
        }

        public String getTRD_ID() {
            return TRD_ID;
        }

        public void setTRD_ID(String TRD_ID) {
            this.TRD_ID = TRD_ID;
        }

        public String getKEY_STR() {
            return KEY_STR;
        }

        public void setKEY_STR(String KEY_STR) {
            this.KEY_STR = KEY_STR;
        }

        public String getBUSINESS_AMOUNT() {
            return BUSINESS_AMOUNT;
        }

        public void setBUSINESS_AMOUNT(String BUSINESS_AMOUNT) {
            this.BUSINESS_AMOUNT = BUSINESS_AMOUNT;
        }

        public String getSECU_ACC() {
            return SECU_ACC;
        }

        public void setSECU_ACC(String SECU_ACC) {
            this.SECU_ACC = SECU_ACC;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public String getMATCHED_PRICE() {
            return MATCHED_PRICE;
        }

        public void setMATCHED_PRICE(String MATCHED_PRICE) {
            this.MATCHED_PRICE = MATCHED_PRICE;
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

        public String getMATCHED_QTY() {
            return MATCHED_QTY;
        }

        public void setMATCHED_QTY(String MATCHED_QTY) {
            this.MATCHED_QTY = MATCHED_QTY;
        }

        public String getORDER_ID() {
            return ORDER_ID;
        }

        public void setORDER_ID(String ORDER_ID) {
            this.ORDER_ID = ORDER_ID;
        }

        public String getMATCHED_AMT() {
            return MATCHED_AMT;
        }

        public void setMATCHED_AMT(String MATCHED_AMT) {
            this.MATCHED_AMT = MATCHED_AMT;
        }

        public String getMATCHED_SN() {
            return MATCHED_SN;
        }

        public void setMATCHED_SN(String MATCHED_SN) {
            this.MATCHED_SN = MATCHED_SN;
        }

        public String getMATCHED_DATE() {
            return MATCHED_DATE;
        }

        public void setMATCHED_DATE(String MATCHED_DATE) {
            this.MATCHED_DATE = MATCHED_DATE;
        }

        public String getMATCHED_TIME() {
            return MATCHED_TIME;
        }

        public void setMATCHED_TIME(String MATCHED_TIME) {
            this.MATCHED_TIME = MATCHED_TIME;
        }

        public String getENTRUST_BS() {
            return ENTRUST_BS;
        }

        public void setENTRUST_BS(String ENTRUST_BS) {
            this.ENTRUST_BS = ENTRUST_BS;
        }

        public String getBUSINESS_TYPE() {
            return BUSINESS_TYPE;
        }

        public void setBUSINESS_TYPE(String BUSINESS_TYPE) {
            this.BUSINESS_TYPE = BUSINESS_TYPE;
        }

        public String getBUSINESS_FLAG() {
            return BUSINESS_FLAG;
        }

        public void setBUSINESS_FLAG(String BUSINESS_FLAG) {
            this.BUSINESS_FLAG = BUSINESS_FLAG;
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

    public String getTransaction() {
        return Transaction;
    }

    public void setTransaction(String transaction) {
        Transaction = transaction;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public List<BeanData> getData() {
        return data;
    }

    public void setData(List<BeanData> data) {
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
