package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wangqi on 2016/8/6.
 * 新股
 */
public class NewStockEnitiy implements Parcelable {

    private String code;                     //0表示成功 -1参数不正确 -4数据库异常
    private String totalcount;               //查询总条数
    private String msg;                      //错误的原因
    //今日
    private String secuCODE;                //证券代码
    private String onlIneStarTdate;         //上市时间
    private String IsSueNameBbrOnlInr;      //股票名称
    private String appLyMaXonnlIne;         //上网发行申购上限(股)
    private String isToday;                 //W N
    private String diLuTedpeRatto;          //发行市盈率(全面摊薄)(倍)
    private String applyCodeonlIne;         //上网发行申购代码
    private String isSuepRice;              //每股发行价(元)
    private String weIghtedPeraioO;         //发行市盈率(加权平均)(倍)
    private String appLymaxonlIneMoney;     //顶格申购市值（ ISSUEPRICE* APPLYMAXONLINE）

    //待上市
    private String loTrateonlIne;               //网上发行中签率
    private String secuCode;                    //证券代码
    private String isSueNameBbrOnlIne;          //上网发行申购简称
    private String lIstaAte;                    //上市日期
    private String APPLYCODEONLINE;             //上网发行申购代码
    private String IssueprICE;                  //每股发行价(元)
    private List<NewStockEnitiy.DataBeanToday> data;
    private String typeTag;                     //数据源类型


    private String Name;                //股票名称
    private String Number;              //股票代码
    private String connectTime;           //
    private String mTime;                //上市时间
    private String newPrice;            //最新价格
    private String amountOfIncrease;    //累计涨幅

    private String adapterType;
    private int    newStockSize;    //今日发行几只新股

    private String zdz ;            //涨跌值

    public String getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(String connectTime) {
        this.connectTime = connectTime;
    }

    public void setNewStockSize(int newStockSize) {
        this.newStockSize = newStockSize;
    }

    public int getNewStockSize() {
        return newStockSize;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getAmountOfIncrease() {
        return amountOfIncrease;
    }

    public void setAmountOfIncrease(String amountOfIncrease) {
        this.amountOfIncrease = amountOfIncrease;
    }

    public String getZdz() {
        return zdz;
    }

    public void setZdz(String zdz) {
        this.zdz = zdz;
    }

    public void setTypeTag(String typeTag) {
        this.typeTag = typeTag;
    }

    public String getTypeTag() {
        return typeTag;
    }

    public void setAdapterType(String adapterType) {
        this.adapterType = adapterType;
    }

    public String getAdapterType() {
        return adapterType;
    }

    public static class DataBeanToday implements Parcelable{
        private String LOTRATEONLINE;               //网上发行中签率
        private String SECUCODE;                    //证券代码
        private String ONLINESTARTDATE;             //上市时间
        private String ISSUENAMEABBR_ONLINE;        //股票名称 上网发行申购简称
        private String LISTDATE;                    //上市日期
        private String APPLYMAXONLINEMONEY;         //顶格申购市值（ ISSUEPRICE* APPLYMAXONLINE）
        private String ISTODAY;                     //W N
        private String APPLYCODEONLINE;             //上网发行申购代码
        private String PREPAREDLISTEXCHANGE;        //
        private String DILUTEDPERATIO;              //发行市盈率(全面摊薄)(倍)
        private String ISSUEPRICE;                  //每股发行价(元)
        private String APPLYMAXONLINE;              //上网发行申购上限(股)
        private String WEIGHTEDPERATIO;             //发行市盈率(加权平均)(倍)
        private String SOURCETYPE;                  //listview布局类型


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(LOTRATEONLINE);
            dest.writeString(SECUCODE);
            dest.writeString(ONLINESTARTDATE);
            dest.writeString(ISSUENAMEABBR_ONLINE);
            dest.writeString(LISTDATE);
            dest.writeString(APPLYMAXONLINEMONEY);
            dest.writeString(ISTODAY);
            dest.writeString(APPLYCODEONLINE);
            dest.writeString(PREPAREDLISTEXCHANGE);
            dest.writeString(DILUTEDPERATIO);
            dest.writeString(ISSUEPRICE);
            dest.writeString(APPLYMAXONLINE);
            dest.writeString(WEIGHTEDPERATIO);
            dest.writeString(SOURCETYPE);
        }

        public static final Creator<DataBeanToday> CREATOR = new Creator<DataBeanToday>(){
            @Override
            public DataBeanToday createFromParcel(Parcel source) {
                DataBeanToday macdEntity = new DataBeanToday();

                macdEntity.LOTRATEONLINE = source.readString();
                macdEntity.SECUCODE = source.readString();
                macdEntity.ONLINESTARTDATE = source.readString();
                macdEntity.ISSUENAMEABBR_ONLINE = source.readString();
                macdEntity.LISTDATE = source.readString();
                macdEntity.APPLYMAXONLINEMONEY = source.readString();
                macdEntity.ISTODAY = source.readString();
                macdEntity.APPLYCODEONLINE = source.readString();
                macdEntity.PREPAREDLISTEXCHANGE = source.readString();
                macdEntity.DILUTEDPERATIO = source.readString();
                macdEntity.ISSUEPRICE = source.readString();
                macdEntity.APPLYMAXONLINE = source.readString();
                macdEntity.WEIGHTEDPERATIO = source.readString();
                macdEntity.SOURCETYPE = source.readString();

                return macdEntity;
            }

            @Override
            public DataBeanToday[] newArray(int size) {
                return new DataBeanToday[size];
            }
        };

        public String getSOURCETYPE() {
            return SOURCETYPE;
        }

        public void setSOURCETYPE(String SOURCETYPE) {
            this.SOURCETYPE = SOURCETYPE;
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

        public String getPREPAREDLISTEXCHANGE() {
            return PREPAREDLISTEXCHANGE;
        }

        public void setPREPAREDLISTEXCHANGE(String PREPAREDLISTEXCHANGE) {
            this.PREPAREDLISTEXCHANGE = PREPAREDLISTEXCHANGE;
        }

        public String getISSUENAMEABBR_ONLINE() {
            return ISSUENAMEABBR_ONLINE;
        }

        public void setISSUENAMEABBR_ONLINE(String ISSUENAMEABBR_ONLINE) {
            this.ISSUENAMEABBR_ONLINE = ISSUENAMEABBR_ONLINE;
        }

        public String getLISTDATE() {
            return LISTDATE;
        }

        public void setLISTDATE(String LISTDATE) {
            this.LISTDATE = LISTDATE;
        }

        public String getAPPLYMAXONLINEMONEY() {
            return APPLYMAXONLINEMONEY;
        }

        public void setAPPLYMAXONLINEMONEY(String APPLYMAXONLINEMONEY) {
            this.APPLYMAXONLINEMONEY = APPLYMAXONLINEMONEY;
        }

        public String getISTODAY() {
            return ISTODAY;
        }

        public void setISTODAY(String ISTODAY) {
            this.ISTODAY = ISTODAY;
        }

        public String getAPPLYCODEONLINE() {
            return APPLYCODEONLINE;
        }

        public void setAPPLYCODEONLINE(String APPLYCODEONLINE) {
            this.APPLYCODEONLINE = APPLYCODEONLINE;
        }

        public String getDILUTEDPERATIO() {
            return DILUTEDPERATIO;
        }

        public void setDILUTEDPERATIO(String DILUTEDPERATIO) {
            this.DILUTEDPERATIO = DILUTEDPERATIO;
        }

        public String getISSUEPRICE() {
            return ISSUEPRICE;
        }

        public void setISSUEPRICE(String ISSUEPRICE) {
            this.ISSUEPRICE = ISSUEPRICE;
        }

        public String getAPPLYMAXONLINE() {
            return APPLYMAXONLINE;
        }

        public void setAPPLYMAXONLINE(String APPLYMAXONLINE) {
            this.APPLYMAXONLINE = APPLYMAXONLINE;
        }

        public String getWEIGHTEDPERATIO() {
            return WEIGHTEDPERATIO;
        }

        public void setWEIGHTEDPERATIO(String WEIGHTEDPERATIO) {
            this.WEIGHTEDPERATIO = WEIGHTEDPERATIO;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSecuCODE() {
        return secuCODE;
    }

    public void setSecuCODE(String secuCODE) {
        this.secuCODE = secuCODE;
    }

    public String getOnlIneStarTdate() {
        return onlIneStarTdate;
    }

    public void setOnlIneStarTdate(String onlIneStarTdate) {
        this.onlIneStarTdate = onlIneStarTdate;
    }

    public String getIsSueNameBbrOnlInr() {
        return IsSueNameBbrOnlInr;
    }

    public void setIsSueNameBbrOnlInr(String isSueNameBbrOnlInr) {
        IsSueNameBbrOnlInr = isSueNameBbrOnlInr;
    }

    public String getAppLyMaXonnlIne() {
        return appLyMaXonnlIne;
    }

    public void setAppLyMaXonnlIne(String appLyMaXonnlIne) {
        this.appLyMaXonnlIne = appLyMaXonnlIne;
    }

    public String getIsToday() {
        return isToday;
    }

    public void setIsToday(String isToday) {
        this.isToday = isToday;
    }

    public String getDiLuTedpeRatto() {
        return diLuTedpeRatto;
    }

    public void setDiLuTedpeRatto(String diLuTedpeRatto) {
        this.diLuTedpeRatto = diLuTedpeRatto;
    }

    public String getApplyCodeonlIne() {
        return applyCodeonlIne;
    }

    public void setApplyCodeonlIne(String applyCodeonlIne) {
        this.applyCodeonlIne = applyCodeonlIne;
    }

    public String getIsSuepRice() {
        return isSuepRice;
    }

    public void setIsSuepRice(String isSuepRice) {
        this.isSuepRice = isSuepRice;
    }

    public String getWeIghtedPeraioO() {
        return weIghtedPeraioO;
    }

    public void setWeIghtedPeraioO(String weIghtedPeraioO) {
        this.weIghtedPeraioO = weIghtedPeraioO;
    }

    public String getAppLymaxonlIneMoney() {
        return appLymaxonlIneMoney;
    }

    public void setAppLymaxonlIneMoney(String appLymaxonlIneMoney) {
        this.appLymaxonlIneMoney = appLymaxonlIneMoney;
    }

    public String getLoTrateonlIne() {
        return loTrateonlIne;
    }

    public void setLoTrateonlIne(String loTrateonlIne) {
        this.loTrateonlIne = loTrateonlIne;
    }

    public String getSecuCode() {
        return secuCode;
    }

    public void setSecuCode(String secuCode) {
        this.secuCode = secuCode;
    }

    public String getIsSueNameBbrOnlIne() {
        return isSueNameBbrOnlIne;
    }

    public void setIsSueNameBbrOnlIne(String isSueNameBbrOnlIne) {
        this.isSueNameBbrOnlIne = isSueNameBbrOnlIne;
    }

    public String getlIstaAte() {
        return lIstaAte;
    }

    public void setlIstaAte(String lIstaAte) {
        this.lIstaAte = lIstaAte;
    }

    public String getAPPLYCODEONLINE() {
        return APPLYCODEONLINE;
    }

    public void setAPPLYCODEONLINE(String APPLYCODEONLINE) {
        this.APPLYCODEONLINE = APPLYCODEONLINE;
    }

    public String getIssueprICE() {
        return IssueprICE;
    }

    public void setIssueprICE(String issueprICE) {
        IssueprICE = issueprICE;
    }

    public List<NewStockEnitiy.DataBeanToday> getData() {
        return data;
    }

    public void setData(List<NewStockEnitiy.DataBeanToday> data) {

        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(totalcount);
        dest.writeString(msg);
        dest.writeString(secuCODE);
        dest.writeString(onlIneStarTdate);
        dest.writeString(IsSueNameBbrOnlInr);
        dest.writeString(appLyMaXonnlIne);
        dest.writeString(isToday);
        dest.writeString(diLuTedpeRatto);
        dest.writeString(applyCodeonlIne);
        dest.writeString(isSuepRice);
        dest.writeString(weIghtedPeraioO);
        dest.writeString(appLymaxonlIneMoney);
        dest.writeString(loTrateonlIne);
        dest.writeString(secuCode);
        dest.writeString(isSueNameBbrOnlIne);
        dest.writeString(lIstaAte);
        dest.writeString(APPLYCODEONLINE);
        dest.writeString(IssueprICE);
        if (data != null) {
            dest.writeParcelableArray((data.toArray(new DataBeanToday[data.size()])), flags);
        }
        dest.writeString(Name);
        dest.writeString(Number);
        dest.writeString(connectTime);
        dest.writeString(mTime);
        dest.writeString(newPrice);
        dest.writeString(amountOfIncrease);
        dest.writeString(adapterType);
        dest.writeInt(newStockSize);
        dest.writeString(zdz);
    }


    public static final Creator<NewStockEnitiy> CREATOR = new Creator<NewStockEnitiy>(){
        @Override
        public NewStockEnitiy createFromParcel(Parcel source) {
            NewStockEnitiy macdEntity = new NewStockEnitiy();
            macdEntity.code = source.readString();
            macdEntity.totalcount = source.readString();
            macdEntity.msg = source.readString();
            macdEntity.secuCODE = source.readString();
            macdEntity.onlIneStarTdate = source.readString();
            macdEntity.IsSueNameBbrOnlInr = source.readString();
            macdEntity.appLyMaXonnlIne = source.readString();
            macdEntity.isToday = source.readString();
            macdEntity.diLuTedpeRatto = source.readString();
            macdEntity.applyCodeonlIne = source.readString();
            macdEntity.isSuepRice = source.readString();
            macdEntity.weIghtedPeraioO = source.readString();
            macdEntity.appLymaxonlIneMoney = source.readString();
            macdEntity.loTrateonlIne = source.readString();
            macdEntity.secuCode = source.readString();
            macdEntity.isSueNameBbrOnlIne = source.readString();
            macdEntity.lIstaAte = source.readString();
            macdEntity.APPLYCODEONLINE = source.readString();
            macdEntity.IssueprICE = source.readString();
            Parcelable[] pars = source.readParcelableArray(DataBeanToday.class.getClassLoader());
            if (pars != null && pars.length > 0) {
                macdEntity.data = Arrays.asList(Arrays.asList(pars).toArray(new DataBeanToday[pars.length]));
            }
            macdEntity.Name = source.readString();
            macdEntity.Number = source.readString();
            macdEntity.connectTime = source.readString();
            macdEntity.mTime = source.readString();
            macdEntity.newPrice = source.readString();
            macdEntity.amountOfIncrease = source.readString();
            macdEntity.adapterType = source.readString();
            macdEntity.newStockSize = source.readInt();
            macdEntity.zdz = source.readString();
            return macdEntity;
        }

        @Override
        public NewStockEnitiy[] newArray(int size) {
            return new NewStockEnitiy[size];
        }
    };
}
