package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2016/9/2.
 * 股东资料实体类
 */
public class StockHolderInfoEntity {

    private String code;
    private String msg;
    private List<BeanData> data;
    private String CustomerCode;
    private String ShareholderSName;
    private String AccountType;
    private String ShareholderSCode;

    public static class BeanData {
        private String ASSET_PROP;
        private String SECU_CODE;
        private String SECU_RIGHTS;
        private String FUND_ACCOUNT;
        private String APPOINT;
        private String ACC_TEL;
        private String SECU_NAME;
        private String SECU_STATUS;
        private String MARKET;
        private String SECU_PROPERTY;


        public String getASSET_PROP() {
            return ASSET_PROP;
        }

        public void setASSET_PROP(String ASSET_PROP) {
            this.ASSET_PROP = ASSET_PROP;
        }

        public String getSECU_CODE() {
            return SECU_CODE;
        }

        public void setSECU_CODE(String SECU_CODE) {
            this.SECU_CODE = SECU_CODE;
        }

        public String getSECU_RIGHTS() {
            return SECU_RIGHTS;
        }

        public void setSECU_RIGHTS(String SECU_RIGHTS) {
            this.SECU_RIGHTS = SECU_RIGHTS;
        }

        public String getFUND_ACCOUNT() {
            return FUND_ACCOUNT;
        }

        public void setFUND_ACCOUNT(String FUND_ACCOUNT) {
            this.FUND_ACCOUNT = FUND_ACCOUNT;
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
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getShareholderSName() {
        return ShareholderSName;
    }

    public void setShareholderSName(String shareholderSName) {
        ShareholderSName = shareholderSName;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getShareholderSCode() {
        return ShareholderSCode;
    }

    public void setShareholderSCode(String shareholderSCode) {
        ShareholderSCode = shareholderSCode;
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
