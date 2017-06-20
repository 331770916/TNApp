package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/9 17:32
 */
public class OTC_AffirmBean {

    /**
     * code : 0
     * msg : (查询成功!)
     * data : [{"IS_OUTOFDATE":"0","PRODRISK_LEVEL":"1","RISK_LEVEL_NAME":"积极型","IS_OPEN":"0","PRODRISK_LEVEL_NAME":"低风险等级","OFRISK_FLAG":"1","IS_AGREEMENT":"0","CORP_RISK_LEVEL":"3","IS_OK":"1"}]
     */

    private String code;
    private String msg;
    /**
     * IS_OUTOFDATE : 0
     * PRODRISK_LEVEL : 1
     * RISK_LEVEL_NAME : 积极型
     * IS_OPEN : 0
     * PRODRISK_LEVEL_NAME : 低风险等级
     * OFRISK_FLAG : 1
     * IS_AGREEMENT : 0
     * CORP_RISK_LEVEL : 3
     * IS_OK : 1
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
        private String IS_OUTOFDATE;
        private String PRODRISK_LEVEL;
        private String RISK_LEVEL_NAME;
        private String IS_OPEN;
        private String PRODRISK_LEVEL_NAME;
        private String OFRISK_FLAG;
        private String IS_AGREEMENT;
        private String CORP_RISK_LEVEL;
        private String IS_OK;

        public String getIS_OUTOFDATE() {
            return IS_OUTOFDATE;
        }

        public void setIS_OUTOFDATE(String IS_OUTOFDATE) {
            this.IS_OUTOFDATE = IS_OUTOFDATE;
        }

        public String getPRODRISK_LEVEL() {
            return PRODRISK_LEVEL;
        }

        public void setPRODRISK_LEVEL(String PRODRISK_LEVEL) {
            this.PRODRISK_LEVEL = PRODRISK_LEVEL;
        }

        public String getRISK_LEVEL_NAME() {
            return RISK_LEVEL_NAME;
        }

        public void setRISK_LEVEL_NAME(String RISK_LEVEL_NAME) {
            this.RISK_LEVEL_NAME = RISK_LEVEL_NAME;
        }

        public String getIS_OPEN() {
            return IS_OPEN;
        }

        public void setIS_OPEN(String IS_OPEN) {
            this.IS_OPEN = IS_OPEN;
        }

        public String getPRODRISK_LEVEL_NAME() {
            return PRODRISK_LEVEL_NAME;
        }

        public void setPRODRISK_LEVEL_NAME(String PRODRISK_LEVEL_NAME) {
            this.PRODRISK_LEVEL_NAME = PRODRISK_LEVEL_NAME;
        }

        public String getOFRISK_FLAG() {
            return OFRISK_FLAG;
        }

        public void setOFRISK_FLAG(String OFRISK_FLAG) {
            this.OFRISK_FLAG = OFRISK_FLAG;
        }

        public String getIS_AGREEMENT() {
            return IS_AGREEMENT;
        }

        public void setIS_AGREEMENT(String IS_AGREEMENT) {
            this.IS_AGREEMENT = IS_AGREEMENT;
        }

        public String getCORP_RISK_LEVEL() {
            return CORP_RISK_LEVEL;
        }

        public void setCORP_RISK_LEVEL(String CORP_RISK_LEVEL) {
            this.CORP_RISK_LEVEL = CORP_RISK_LEVEL;
        }

        public String getIS_OK() {
            return IS_OK;
        }

        public void setIS_OK(String IS_OK) {
            this.IS_OK = IS_OK;
        }
    }
}
