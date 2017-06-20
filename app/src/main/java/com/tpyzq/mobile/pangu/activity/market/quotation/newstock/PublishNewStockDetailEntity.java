package com.tpyzq.mobile.pangu.activity.market.quotation.newstock;

import java.util.List;

/**
 * Created by wangqi on 2016/8/1.
 * 新股详情    实体类
 */
public class PublishNewStockDetailEntity {

    private String totalcount;
    private String code;
    private String msg;

    private List<DataBean> data;

    private String seCUABBR;                // 证券简称
    private String seCUCODE;                // 证券代码
    private String appLYMAXONLINE;          // 上网发行申购代码
    private String onLINESTARTDATE;         // 网上申购日期上限(yyyy-MM-dd)
    private String issUERESULTPUBLDATE;     // 中签率公告日（yyyy-MM-dd）
    private String loTRATEONLINE;           // 网上发行中签率
    private String liSTDATE;                // 上市日期
    private String issSUEPRICE;             // 每股发行价(元)
    private String wEIGHTEDPERATIO;         // 发行市盈率(加权平均)(倍)
    private String isSUEVOL;                // 发行量(股)
    private String appLYCODEONLINE;         // 上网发行申购上限(股)
    private String buSINESSMAJOR;           // 经营范围
    private String brIEFINTROTEXT;          // 公司简介
    private String diLUTEDPERATIO;          // 发行市盈率(全面摊薄)(倍)
    private String oLBEFPUTBACK;            // 网上发行数量


    public void setoLBEFPUTBACK(String oLBEFPUTBACK) {
        this.oLBEFPUTBACK = oLBEFPUTBACK;
    }

    public void setwEIGHTEDPERATIO(String wEIGHTEDPERATIO) {
        this.wEIGHTEDPERATIO = wEIGHTEDPERATIO;
    }

    public String getoLBEFPUTBACK() {
        return oLBEFPUTBACK;
    }

    public String getwEIGHTEDPERATIO() {
        return wEIGHTEDPERATIO;
    }

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getIsSUEVOL() {
        return isSUEVOL;
    }

    public void setIsSUEVOL(String isSUEVOL) {
        this.isSUEVOL = isSUEVOL;
    }

    public String getIssSUEPRICE() {
        return issSUEPRICE;
    }

    public void setIssSUEPRICE(String issSUEPRICE) {
        this.issSUEPRICE = issSUEPRICE;
    }

    public String getLoTRATEONLINE() {
        return loTRATEONLINE;
    }

    public void setLoTRATEONLINE(String loTRATEONLINE) {
        this.loTRATEONLINE = loTRATEONLINE;
    }

    public String getSeCUCODE() {
        return seCUCODE;
    }

    public void setSeCUCODE(String seCUCODE) {
        this.seCUCODE = seCUCODE;
    }

    public String getOnLINESTARTDATE() {
        return onLINESTARTDATE;
    }

    public void setOnLINESTARTDATE(String onLINESTARTDATE) {
        this.onLINESTARTDATE = onLINESTARTDATE;
    }

    public String getBuSINESSMAJOR() {
        return buSINESSMAJOR;
    }

    public void setBuSINESSMAJOR(String buSINESSMAJOR) {
        this.buSINESSMAJOR = buSINESSMAJOR;
    }

    public String getSeCUABBR() {
        return seCUABBR;
    }

    public void setSeCUABBR(String seCUABBR) {
        this.seCUABBR = seCUABBR;
    }

    public String getAppLYMAXONLINE() {
        return appLYMAXONLINE;
    }

    public void setAppLYMAXONLINE(String appLYMAXONLINE) {
        this.appLYMAXONLINE = appLYMAXONLINE;
    }

    public String getIssUERESULTPUBLDATE() {
        return issUERESULTPUBLDATE;
    }

    public void setIssUERESULTPUBLDATE(String issUERESULTPUBLDATE) {
        this.issUERESULTPUBLDATE = issUERESULTPUBLDATE;
    }

    public String getLiSTDATE() {
        return liSTDATE;
    }

    public void setLiSTDATE(String liSTDATE) {
        this.liSTDATE = liSTDATE;
    }

    public String getDiLUTEDPERATIO() {
        return diLUTEDPERATIO;
    }

    public void setDiLUTEDPERATIO(String diLUTEDPERATIO) {
        this.diLUTEDPERATIO = diLUTEDPERATIO;
    }

    public String getAppLYCODEONLINE() {
        return appLYCODEONLINE;
    }

    public void setAppLYCODEONLINE(String appLYCODEONLINE) {
        this.appLYCODEONLINE = appLYCODEONLINE;
    }

    public String getBrIEFINTROTEXT() {
        return brIEFINTROTEXT;
    }

    public void setBrIEFINTROTEXT(String brIEFINTROTEXT) {
        this.brIEFINTROTEXT = brIEFINTROTEXT;
    }


    public static class DataBean {
        private String SECUABBR;            // 证券简称
        private String SECUCODE;            // 证券代码
        private String APPLYMAXONLINE;      // 上网发行申购代码
        private String ONLINESTARTDATE;     // 网上申购日期上限(yyyy-MM-dd)
        private String ISSUERESULTPUBLDATE; // 中签率公告日（yyyy-MM-dd）
        private String LOTRATEONLINE;       // 网上发行中签率
        private String LISTDATE;            // 上市日期
        private String ISSUEPRICE;          // 每股发行价(元)
        private String WEIGHTEDPERATIO;     // 发行市盈率(加权平均)(倍)
        private String ISSUEVOL;            // 发行量(股)
        private String APPLYCODEONLINE;     // 上网发行申购上限(股)
        private String BUSINESSMAJOR;       // 经营范围
        private String BRIEFINTROTEXT;      // 公司简介
        private String DILUTEDPERATIO;      // 发行市盈率(全面摊薄)(倍)
        private String OLBEFPUTBACK;        // 网上发行数量

        public void setOLBEFPUTBACK(String OLBEFPUTBACK) {
            this.OLBEFPUTBACK = OLBEFPUTBACK;
        }

        public void setWEIGHTEDPERATIO(String WEIGHTEDPERATIO) {
            this.WEIGHTEDPERATIO = WEIGHTEDPERATIO;
        }

        public String getOLBEFPUTBACK() {
            return OLBEFPUTBACK;
        }

        public String getWEIGHTEDPERATIO() {
            return WEIGHTEDPERATIO;
        }

        public String getISSUEVOL() {
            return ISSUEVOL;
        }

        public void setISSUEVOL(String ISSUEVOL) {
            this.ISSUEVOL = ISSUEVOL;
        }

        public String getISSUEPRICE() {
            return ISSUEPRICE;
        }

        public void setISSUEPRICE(String ISSUEPRICE) {
            this.ISSUEPRICE = ISSUEPRICE;
        }

        public String getLOTRATEONLINE() {
            return LOTRATEONLINE;
        }

        public void setLOTRATEONLINE(String LOTRATEONLINE) {
            this.LOTRATEONLINE = LOTRATEONLINE;
        }

        public String getSECUCODE() {
            return SECUCODE;
        }

        public void setSECUCODE(String SECUCODE) {
            this.SECUCODE = SECUCODE;
        }

        public String getONLINESTARTDATE() {
            return ONLINESTARTDATE;
        }

        public void setONLINESTARTDATE(String ONLINESTARTDATE) {
            this.ONLINESTARTDATE = ONLINESTARTDATE;
        }

        public String getBUSINESSMAJOR() {
            return BUSINESSMAJOR;
        }

        public void setBUSINESSMAJOR(String BUSINESSMAJOR) {
            this.BUSINESSMAJOR = BUSINESSMAJOR;
        }

        public String getSECUABBR() {
            return SECUABBR;
        }

        public void setSECUABBR(String SECUABBR) {
            this.SECUABBR = SECUABBR;
        }

        public String getAPPLYMAXONLINE() {
            return APPLYMAXONLINE;
        }

        public void setAPPLYMAXONLINE(String APPLYMAXONLINE) {
            this.APPLYMAXONLINE = APPLYMAXONLINE;
        }

        public String getISSUERESULTPUBLDATE() {
            return ISSUERESULTPUBLDATE;
        }

        public void setISSUERESULTPUBLDATE(String ISSUERESULTPUBLDATE) {
            this.ISSUERESULTPUBLDATE = ISSUERESULTPUBLDATE;
        }

        public String getLISTDATE() {
            return LISTDATE;
        }

        public void setLISTDATE(String LISTDATE) {
            this.LISTDATE = LISTDATE;
        }

        public String getDILUTEDPERATIO() {
            return DILUTEDPERATIO;
        }

        public void setDILUTEDPERATIO(String DILUTEDPERATIO) {
            this.DILUTEDPERATIO = DILUTEDPERATIO;
        }

        public String getAPPLYCODEONLINE() {
            return APPLYCODEONLINE;
        }

        public void setAPPLYCODEONLINE(String APPLYCODEONLINE) {
            this.APPLYCODEONLINE = APPLYCODEONLINE;
        }

        public String getBRIEFINTROTEXT() {
            return BRIEFINTROTEXT;
        }

        public void setBRIEFINTROTEXT(String BRIEFINTROTEXT) {
            this.BRIEFINTROTEXT = BRIEFINTROTEXT;
        }


    }
}
