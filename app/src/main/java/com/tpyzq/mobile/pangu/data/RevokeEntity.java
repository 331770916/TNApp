package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2016/9/9.
 * 撤单实体类
 */
public class RevokeEntity {
    private String code;
    private List<RevokeData> data;
    private String msg;

    private String Name;
    private String Titm;
    private String Price;
    private String MatchedPrice;
    private String WithdrawnQty;
    private String MatchedQty;
    private String EntrustStatus;
    private String EntrustBs;
    private String EntrusNo;

    public String getEntrusNo() {
        return EntrusNo;
    }

    public void setEntrusNo(String entrusNo) {
        EntrusNo = entrusNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitm() {
        return Titm;
    }

    public void setTitm(String date) {
        Titm = date;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMatchedPrice() {
        return MatchedPrice;
    }

    public void setMatchedPrice(String matchedPrice) {
        MatchedPrice = matchedPrice;
    }

    public String getWithdrawnQty() {
        return WithdrawnQty;
    }

    public void setWithdrawnQty(String withdrawnQty) {
        WithdrawnQty = withdrawnQty;
    }

    public String getMatchedQty() {
        return MatchedQty;
    }

    public void setMatchedQty(String matchedQty) {
        MatchedQty = matchedQty;
    }

    public String getEntrustStatus() {
        return EntrustStatus;
    }

    public void setEntrustStatus(String entrustStatus) {
        EntrustStatus = entrustStatus;
    }

    public String getEntrustBs() {
        return EntrustBs;
    }

    public void setEntrustBs(String entrustBs) {
        EntrustBs = entrustBs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<RevokeData> getData() {
        return data;
    }

    public void setData(List<RevokeData> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class RevokeData {
        private String ACTION_IN;
        private String ENTRUST_BS;
        private String ENTRUST_NO;
        private String ENTRUST_PROP;
        private String ENTRUST_STATUS;
        private String IS_WITHDRAW;
        private String KEY_STR;
        private String MARKET;
        private String MATCHED_PRICE;
        private String MATCHED_QTY;
        private String ORDER_DATE;
        private String ORDER_ID;
        private String ORDER_TIME;
        private String PRICE;
        private String QTY;
        private String SECU_ACC;
        private String SECU_CODE;
        private String SECU_NAME;
        private String STATUS_NAME;
        private String WITHDRAWN_QTY;

        public String getACTION_IN() {
            return ACTION_IN;
        }

        public void setACTION_IN(String ACTION_IN) {
            this.ACTION_IN = ACTION_IN;
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

        public String getENTRUST_PROP() {
            return ENTRUST_PROP;
        }

        public void setENTRUST_PROP(String ENTRUST_PROP) {
            this.ENTRUST_PROP = ENTRUST_PROP;
        }

        public String getENTRUST_STATUS() {
            return ENTRUST_STATUS;
        }

        public void setENTRUST_STATUS(String ENTRUST_STATUS) {
            this.ENTRUST_STATUS = ENTRUST_STATUS;
        }

        public String getIS_WITHDRAW() {
            return IS_WITHDRAW;
        }

        public void setIS_WITHDRAW(String IS_WITHDRAW) {
            this.IS_WITHDRAW = IS_WITHDRAW;
        }

        public String getKEY_STR() {
            return KEY_STR;
        }

        public void setKEY_STR(String KEY_STR) {
            this.KEY_STR = KEY_STR;
        }

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }

        public String getMATCHED_PRICE() {
            return MATCHED_PRICE;
        }

        public void setMATCHED_PRICE(String MATCHED_PRICE) {
            this.MATCHED_PRICE = MATCHED_PRICE;
        }

        public String getMATCHED_QTY() {
            return MATCHED_QTY;
        }

        public void setMATCHED_QTY(String MATCHED_QTY) {
            this.MATCHED_QTY = MATCHED_QTY;
        }

        public String getORDER_DATE() {
            return ORDER_DATE;
        }

        public void setORDER_DATE(String ORDER_DATE) {
            this.ORDER_DATE = ORDER_DATE;
        }

        public String getORDER_ID() {
            return ORDER_ID;
        }

        public void setORDER_ID(String ORDER_ID) {
            this.ORDER_ID = ORDER_ID;
        }

        public String getORDER_TIME() {
            return ORDER_TIME;
        }

        public void setORDER_TIME(String ORDER_TIME) {
            this.ORDER_TIME = ORDER_TIME;
        }

        public String getPRICE() {
            return PRICE;
        }

        public void setPRICE(String PRICE) {
            this.PRICE = PRICE;
        }

        public String getQTY() {
            return QTY;
        }

        public void setQTY(String QTY) {
            this.QTY = QTY;
        }

        public String getSECU_ACC() {
            return SECU_ACC;
        }

        public void setSECU_ACC(String SECU_ACC) {
            this.SECU_ACC = SECU_ACC;
        }

        public String getSECU_CODE() {
            return SECU_CODE;
        }

        public void setSECU_CODE(String SECU_CODE) {
            this.SECU_CODE = SECU_CODE;
        }

        public String getSECU_NAME() {
            return SECU_NAME;
        }

        public void setSECU_NAME(String SECU_NAME) {
            this.SECU_NAME = SECU_NAME;
        }

        public String getSTATUS_NAME() {
            return STATUS_NAME;
        }

        public void setSTATUS_NAME(String STATUS_NAME) {
            this.STATUS_NAME = STATUS_NAME;
        }

        public String getWITHDRAWN_QTY() {
            return WITHDRAWN_QTY;
        }

        public void setWITHDRAWN_QTY(String WITHDRAWN_QTY) {
            this.WITHDRAWN_QTY = WITHDRAWN_QTY;
        }
    }


}
