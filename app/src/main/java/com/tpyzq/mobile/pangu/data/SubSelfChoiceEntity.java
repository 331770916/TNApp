package com.tpyzq.mobile.pangu.data;

/**
 * Created by zhangwenbo on 2016/9/13.
 */
public class SubSelfChoiceEntity {

    private String UPDATETIME;
    private String PRICE;               //自选时价格
    private String RESERVEDNUMBER2;
    private String RESERVEDNUMBER1;
    private String RESERVEDNUMBER3;
    private String NAME;                //股票名称
    private String RESERVEDCHAR3;
    private String RESERVEDCHAR2;
    private String STATUS;              //状态 1.正常 0.删除
    private String CODE;                //股票编码
    private String RESERVEDCHAR1;
    private String SOURCE;              //来源： 1:用户自选 2.浏览
    private String USERID;              //用户账号ID
    private String CAPITALACCOUNT;
    private String ID;                  //数据唯一标识
    private String CREATETIME;
    private String BINDING;
    private String TYPE;                //类型
    private String SORTRULE;

    private String MINIMUM;                 //最新价底限
    private String RISESIZQCONFINE;         //涨跌幅高限
    private String MAXIMUM;                 //最新价高限
    private String FALLSIZECONFINE;         //涨跌幅底限
    private String remainType;              //提醒类型


    public String getSORTRULE() {
        return SORTRULE;
    }

    public void setSORTRULE(String SORTRULE) {
        this.SORTRULE = SORTRULE;
    }

    public String getRemainType() {
        return remainType;
    }

    public void setRemainType(String remainType) {
        this.remainType = remainType;
    }

    public String getMINIMUM() {
        return MINIMUM;
    }

    public void setMINIMUM(String MINIMUM) {
        this.MINIMUM = MINIMUM;
    }

    public String getRISESIZQCONFINE() {
        return RISESIZQCONFINE;
    }

    public void setRISESIZQCONFINE(String RISESIZQCONFINE) {
        this.RISESIZQCONFINE = RISESIZQCONFINE;
    }

    public String getMAXIMUM() {
        return MAXIMUM;
    }

    public void setMAXIMUM(String MAXIMUM) {
        this.MAXIMUM = MAXIMUM;
    }

    public String getFALLSIZECONFINE() {
        return FALLSIZECONFINE;
    }

    public void setFALLSIZECONFINE(String FALLSIZECONFINE) {
        this.FALLSIZECONFINE = FALLSIZECONFINE;
    }

    public String getUPDATETIME() {
        return UPDATETIME;
    }

    public void setUPDATETIME(String UPDATETIME) {
        this.UPDATETIME = UPDATETIME;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getRESERVEDNUMBER2() {
        return RESERVEDNUMBER2;
    }

    public void setRESERVEDNUMBER2(String RESERVEDNUMBER2) {
        this.RESERVEDNUMBER2 = RESERVEDNUMBER2;
    }

    public String getRESERVEDNUMBER1() {
        return RESERVEDNUMBER1;
    }

    public void setRESERVEDNUMBER1(String RESERVEDNUMBER1) {
        this.RESERVEDNUMBER1 = RESERVEDNUMBER1;
    }

    public String getRESERVEDNUMBER3() {
        return RESERVEDNUMBER3;
    }

    public void setRESERVEDNUMBER3(String RESERVEDNUMBER3) {
        this.RESERVEDNUMBER3 = RESERVEDNUMBER3;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getRESERVEDCHAR3() {
        return RESERVEDCHAR3;
    }

    public void setRESERVEDCHAR3(String RESERVEDCHAR3) {
        this.RESERVEDCHAR3 = RESERVEDCHAR3;
    }

    public String getRESERVEDCHAR2() {
        return RESERVEDCHAR2;
    }

    public void setRESERVEDCHAR2(String RESERVEDCHAR2) {
        this.RESERVEDCHAR2 = RESERVEDCHAR2;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getRESERVEDCHAR1() {
        return RESERVEDCHAR1;
    }

    public void setRESERVEDCHAR1(String RESERVEDCHAR1) {
        this.RESERVEDCHAR1 = RESERVEDCHAR1;
    }

    public String getSOURCE() {
        return SOURCE;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getCAPITALACCOUNT() {
        return CAPITALACCOUNT;
    }

    public void setCAPITALACCOUNT(String CAPITALACCOUNT) {
        this.CAPITALACCOUNT = CAPITALACCOUNT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public String getBINDING() {
        return BINDING;
    }

    public void setBINDING(String BINDING) {
        this.BINDING = BINDING;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }
}
