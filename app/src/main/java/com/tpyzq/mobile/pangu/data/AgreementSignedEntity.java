package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2016/9/13.
 * 退市和风险警示协议签署  实体类
 */
public class AgreementSignedEntity {

    private String msg;
    private String code;
    private List<Data> data;
    private String Market;
    private String Secu_code;
    private String Secu_rights;
    private String Secu_name;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMarket() {
        return Market;
    }

    public void setMarket(String market) {
        Market = market;
    }

    public String getSecu_code() {
        return Secu_code;
    }

    public void setSecu_code(String secu_code) {
        Secu_code = secu_code;
    }

    public String getSecu_rights() {
        return Secu_rights;
    }

    public void setSecu_rights(String secu_rights) {
        Secu_rights = secu_rights;
    }

    public String getSecu_name() {
        return Secu_name;
    }

    public void setSecu_name(String secu_name) {
        Secu_name = secu_name;
    }

    public static class Data {

        private String SECU_CODE;
        private String SECU_STATUS;
        private String MARKET;
        private String SECU_PROPERTY;
        private String SECU_RIGHTS;
        private String APPOINT;
        private String ACC_TEL;
        private String SECU_NAME;
        private String FUND_ACCOUNT;
        private String ASSET_PROP;
        private String SHFXJS_SIGN_DATE;
        private String SHTS_SIGN_DATE;
        private String SZTS_SIGN_DATE;

        public String getSECU_CODE() {
            return SECU_CODE;
        }

        public void setSECU_CODE(String SECU_CODE) {
            this.SECU_CODE = SECU_CODE;
        }

        public String getSECU_STATUS() {
            return SECU_STATUS;
        }

        public void setSECU_STATUS(String SECU_STATUS) {
            this.SECU_STATUS = SECU_STATUS;
        }

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }

        public String getSECU_PROPERTY() {
            return SECU_PROPERTY;
        }

        public void setSECU_PROPERTY(String SECU_PROPERTY) {
            this.SECU_PROPERTY = SECU_PROPERTY;
        }

        public String getSECU_RIGHTS() {
            return SECU_RIGHTS;
        }

        public void setSECU_RIGHTS(String SECU_RIGHTS) {
            this.SECU_RIGHTS = SECU_RIGHTS;
        }

        public String getAPPOINT() {
            return APPOINT;
        }

        public void setAPPOINT(String APPOINT) {
            this.APPOINT = APPOINT;
        }

        public String getACC_TEL() {
            return ACC_TEL;
        }

        public void setACC_TEL(String ACC_TEL) {
            this.ACC_TEL = ACC_TEL;
        }

        public String getSECU_NAME() {
            return SECU_NAME;
        }

        public void setSECU_NAME(String SECU_NAME) {
            this.SECU_NAME = SECU_NAME;
        }

        public String getFUND_ACCOUNT() {
            return FUND_ACCOUNT;
        }

        public void setFUND_ACCOUNT(String FUND_ACCOUNT) {
            this.FUND_ACCOUNT = FUND_ACCOUNT;
        }

        public String getASSET_PROP() {
            return ASSET_PROP;
        }

        public void setASSET_PROP(String ASSET_PROP) {
            this.ASSET_PROP = ASSET_PROP;
        }

        public String getSHFXJS_SIGN_DATE() {
            return SHFXJS_SIGN_DATE;
        }

        public void setSHFXJS_SIGN_DATE(String SHFXJS_SIGN_DATE) {
            this.SHFXJS_SIGN_DATE = SHFXJS_SIGN_DATE;
        }

        public String getSHTS_SIGN_DATE() {
            return SHTS_SIGN_DATE;
        }

        public void setSHTS_SIGN_DATE(String SHTS_SIGN_DATE) {
            this.SHTS_SIGN_DATE = SHTS_SIGN_DATE;
        }

        public String getSZTS_SIGN_DATE() {
            return SZTS_SIGN_DATE;
        }

        public void setSZTS_SIGN_DATE(String SZTS_SIGN_DATE) {
            this.SZTS_SIGN_DATE = SZTS_SIGN_DATE;
        }
    }

}