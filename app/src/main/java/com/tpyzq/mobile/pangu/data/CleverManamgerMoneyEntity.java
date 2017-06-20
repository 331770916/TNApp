package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/9/22.
 * 智选理财实体类
 */
public class CleverManamgerMoneyEntity implements Parcelable {

    private String mistackMsg;

    //产品
    private String fundName;                //基金名称
    private String fundCode;                //基金代码
    private String fundLevel;               //基金风险
    private String earnType;                //收益类型
    private String fundExpectEarnings;      //基金预期收益
    private String fundTimeLimit;           //期限
    private String fundStartBuy;            //起购

    //非货币基金详情
    private String historyNetValueDate;     //历史净值走势日期
    private String historyNetValueUnit;     //历史净值单位
    private String historyNetValueTotal;    //累计净值
    //基金概况
    private String productCode;             //产品代码
    private String productType;             //产品类型
    private String fundCompany;             //基金公司
    private String fundOpenState;           //开放状态
    private String fundType;                //基金类型
    private String fundCreatTime;           //成立时间

    private String generalSituationTitle;   //标题
    private String generalSituationContent; //内容
    private String generalSituationContentUrl;//内容链接

    //历任基金经理

    private String startTime;
    private String endTime;
    private String manager;
    private String period;
    private String earn;

    //重仓
    private String stockName;
    private String stockNumber;
    private String    progressPersent;

    //公告

    private String announceTitle;
    private String announceFrom;
    private String announceDate;
    private String announceId;


    //14天

    private String PRODCODE;        //产品代码
    private String PRODNAME;        //产品名称
    private String COMPREF;         //业绩比较基准
    private String BUY_LOW_AMOUNT;  //起购金额（元）（整型数字）
    private String INVESTDAYS;      //投资期限（天）（整型数字）
    private String RISKLEVEL;       //风险等级（整型数字）0:默认等级 1:保本等级 2:低风险等级 3:中风险等级 4:高风险等级
    private String FIRSTAMMOUNT;    //首次参与最低金额（整型数字，元）
    private String APPENDAMMOUNT;   //追加参与最低金额（整型数字，元）
    private String PRODTYPE;        //产品类型
    private String PUBCOMPANY;      //发行公司
    private String DESCRIPITION;    //相关说明
    private String TYPE;            //产品大类，固定为3 (1-开放式基金2-OTC 3-14天现金增益
    private String ORDERNUM;        //排序字段，小的靠前
    private String ISHOT;           //是否热销0否1是
    private String ISSHOW;          //是否展示(0展示-1不展示)
    private String CREATETIME;      //创建时间yyyy-MM-dd
    private String MODIFYTIME;      //修改时间yyyy-MM-dd
    private String SUBNAME;         //产品简称
    private List<Map<String, String>> noticeDates;//
    private List<Map<String, String>> proaoclDates;//


    //otc
    private String DESCRIPTION;     //相关说明
    private String TIP;             //挂钩标的
    private String INCOMETYPE;      //收益类型（1固定2浮动3固定+浮动）
    private String PRODRATIO;       //收益率
    private String REVENUERULE;     //收益规则
    private String IPO_END_DATE;    //募集结束
    private String INTERESTDAY;     //起息日期（yyyy-MM-dd）
    private String ENDDAY;          //到期日（yyyy-MM-dd）
    private String PAYDAY;          //资金到账日;
    private String INVEST_TYPE;     //投资类别(String)
    private String PROD_STATUS;     //产品状态（产品交易状态）(String
    private String IPO_BEGIN_DATE;  //产品募集开始日期(String)
    private String CREAT_TIME;      //现在时间
    private String Oreder;          //判断是不是预约
    private boolean force;


    private List<Map<String, String>> otcNoticeDate;//
    private List<Map<String, String>> otcProaoclDate;//
    //基金

    private String SECURITYCODE;    //基金代码
    private String FUNDNAME;        //基金名称
    private String KFZT;            //开放状态
    private String FXDJ;            //风险等级
    private String QGJE;            //起购金额
    private String FUNDTYPE;        //基金类型
    private String ESTABLISHMENTDATE;   //成立日期（yyyy-mm-dd）
    private String UNITNV;              //最新单位净值
    private String ACCUMULATEDUNITNV;   //单位累计净值
    private String DAILYPROFIT;         //每万份基金单位当日收益(元)
    private String LATESTWEEKLYYIELD;   //最近7日折算年收益率
    private String CHANGEPCTRM;         //月涨跌幅（%）
    private String INVESTADVISORNAME;   //基金管理人（基金公司）
    private String FUNDTYPECODE;        //1101-股票型；1103-混合型；1105-债券型；1107-保本型；1109-货币型；1199-其他型


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fundName);
        dest.writeString(fundCode);
        dest.writeString(fundLevel);
        dest.writeString(earnType);
        dest.writeString(fundExpectEarnings);
        dest.writeString(fundTimeLimit);
        dest.writeString(fundStartBuy);
        dest.writeString(historyNetValueDate);
        dest.writeString(historyNetValueUnit);
        dest.writeString(historyNetValueTotal);
        dest.writeString(productCode);
        dest.writeString(productType);
        dest.writeString(fundCompany);
        dest.writeString(fundOpenState);
        dest.writeString(fundType);
        dest.writeString(fundCreatTime);
        dest.writeString(generalSituationTitle);
        dest.writeString(generalSituationContent);
        dest.writeString(generalSituationContentUrl);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(manager);
        dest.writeString(period);
        dest.writeString(earn);
        dest.writeString(stockName);
        dest.writeString(stockNumber);
        dest.writeString(progressPersent);
        dest.writeString(announceTitle);
        dest.writeString(announceFrom);
        dest.writeString(announceDate);
        dest.writeString(announceId);
        dest.writeString(PRODCODE);
        dest.writeString(PRODNAME);
        dest.writeString(COMPREF);
        dest.writeString(BUY_LOW_AMOUNT);
        dest.writeString(INVESTDAYS);
        dest.writeString(RISKLEVEL);
        dest.writeString(FIRSTAMMOUNT);
        dest.writeString(APPENDAMMOUNT);
        dest.writeString(PRODTYPE);
        dest.writeString(PUBCOMPANY);
        dest.writeString(DESCRIPITION);
        dest.writeString(TYPE);
        dest.writeString(ORDERNUM);
        dest.writeString(ISHOT);
        dest.writeString(ISSHOW);
        dest.writeString(CREATETIME);
        dest.writeString(MODIFYTIME);
        dest.writeString(SUBNAME);
        dest.writeString(DESCRIPTION);
        dest.writeString(TIP);
        dest.writeString(INCOMETYPE);
        dest.writeString(PRODRATIO);
        dest.writeString(REVENUERULE);
        dest.writeString(IPO_END_DATE);
        dest.writeString(INTERESTDAY);
        dest.writeString(ENDDAY);
        dest.writeString(PAYDAY);
        dest.writeString(INVEST_TYPE);
        dest.writeString(PROD_STATUS);
        dest.writeString(IPO_BEGIN_DATE);
        dest.writeSerializable(CREAT_TIME);
        dest.writeString(SECURITYCODE);
        dest.writeString(FUNDNAME);
        dest.writeString(KFZT);
        dest.writeString(FXDJ);
        dest.writeString(QGJE);
        dest.writeString(FUNDTYPE);
        dest.writeString(ESTABLISHMENTDATE);
        dest.writeString(UNITNV);
        dest.writeString(ACCUMULATEDUNITNV);
        dest.writeString(DAILYPROFIT);
        dest.writeString(LATESTWEEKLYYIELD);
        dest.writeString(CHANGEPCTRM);
        dest.writeString(INVESTADVISORNAME);
        dest.writeString(FUNDTYPECODE);
    }



    public static final Creator<CleverManamgerMoneyEntity> CREATOR  = new Creator<CleverManamgerMoneyEntity>() {
        @Override
        public CleverManamgerMoneyEntity createFromParcel(Parcel source) {
            CleverManamgerMoneyEntity bean = new CleverManamgerMoneyEntity();


            bean.fundName = source.readString();
            bean.fundCode =  source.readString();
            bean.fundLevel =  source.readString();
            bean.earnType =  source.readString();
            bean.fundExpectEarnings =  source.readString();
            bean.fundTimeLimit =  source.readString();
            bean.fundStartBuy =  source.readString();
            bean.historyNetValueDate =  source.readString();
            bean.historyNetValueUnit =  source.readString();
            bean.historyNetValueTotal =  source.readString();
            bean.productCode =  source.readString();
            bean.productType =  source.readString();
            bean.fundCompany =  source.readString();
            bean.fundOpenState =  source.readString();
            bean.fundType =  source.readString();
            bean.fundCreatTime =  source.readString();
            bean.generalSituationTitle =  source.readString();
            bean.generalSituationContent =  source.readString();
            bean.generalSituationContentUrl = source.readString();
            bean.startTime =  source.readString();
            bean.endTime =  source.readString();
            bean.manager =  source.readString();
            bean.period =  source.readString();
            bean.earn =  source.readString();
            bean.stockName =  source.readString();
            bean.stockNumber =  source.readString();
            bean.progressPersent =  source.readString();
            bean.announceTitle =  source.readString();
            bean.announceFrom =  source.readString();
            bean.announceDate =  source.readString();
            bean.announceId = source.readString();
            bean.PRODCODE =  source.readString();
            bean.PRODNAME =  source.readString();

            bean.COMPREF =  source.readString();
            bean.BUY_LOW_AMOUNT =  source.readString();
            bean.INVESTDAYS =  source.readString();
            bean.RISKLEVEL =  source.readString();
            bean.FIRSTAMMOUNT =  source.readString();
            bean.APPENDAMMOUNT =  source.readString();
            bean.PRODTYPE =  source.readString();
            bean.PUBCOMPANY =  source.readString();
            bean.DESCRIPITION =  source.readString();
            bean.TYPE =  source.readString();
            bean.ORDERNUM =  source.readString();
            bean.ISHOT =  source.readString();
            bean.ISSHOW =  source.readString();
            bean.CREATETIME =  source.readString();
            bean. MODIFYTIME =  source.readString();
            bean.SUBNAME =  source.readString();
            bean.DESCRIPTION =  source.readString();
            bean.TIP =  source.readString();
            bean.INCOMETYPE =  source.readString();
            bean.PRODRATIO =  source.readString();
            bean.REVENUERULE =  source.readString();
            bean.IPO_END_DATE =  source.readString();
            bean.INTERESTDAY =  source.readString();
            bean.ENDDAY =  source.readString();
            bean.PAYDAY =  source.readString();
            bean.INVEST_TYPE = source.readString();
            bean.PROD_STATUS = source.readString();
            bean.IPO_BEGIN_DATE = source.readString();
            bean.CREAT_TIME = source.readString();
            bean.SECURITYCODE =  source.readString();
            bean.FUNDNAME =  source.readString();
            bean.KFZT =  source.readString();
            bean.FXDJ =  source.readString();
            bean.QGJE =  source.readString();
            bean.FUNDTYPE =  source.readString();
            bean.ESTABLISHMENTDATE =  source.readString();
            bean.UNITNV =  source.readString();
            bean.ACCUMULATEDUNITNV =  source.readString();
            bean.DAILYPROFIT =  source.readString();
            bean.LATESTWEEKLYYIELD =  source.readString();
            bean.CHANGEPCTRM =  source.readString();
            bean.INVESTADVISORNAME =  source.readString();
            bean.FUNDTYPECODE =  source.readString();

            return bean;
        }

        @Override
        public CleverManamgerMoneyEntity[] newArray(int size) {
            return new CleverManamgerMoneyEntity[size];
        }
    };

    public String getMistackMsg() {
        return mistackMsg;
    }

    public void setMistackMsg(String mistackMsg) {
        this.mistackMsg = mistackMsg;
    }

    public String getPRODCODE() {
        return PRODCODE;
    }

    public void setPRODCODE(String PRODCODE) {
        this.PRODCODE = PRODCODE;
    }

    public String getPRODNAME() {
        return PRODNAME;
    }

    public void setPRODNAME(String PRODNAME) {
        this.PRODNAME = PRODNAME;
    }

    public String getCOMPREF() {
        return COMPREF;
    }

    public void setCOMPREF(String COMPREF) {
        this.COMPREF = COMPREF;
    }

    public String getBUY_LOW_AMOUNT() {
        return BUY_LOW_AMOUNT;
    }

    public void setBUY_LOW_AMOUNT(String BUY_LOW_AMOUNT) {
        this.BUY_LOW_AMOUNT = BUY_LOW_AMOUNT;
    }

    public String getINVESTDAYS() {
        return INVESTDAYS;
    }

    public void setINVESTDAYS(String INVESTDAYS) {
        this.INVESTDAYS = INVESTDAYS;
    }

    public String getRISKLEVEL() {
        return RISKLEVEL;
    }

    public void setRISKLEVEL(String RISKLEVEL) {
        this.RISKLEVEL = RISKLEVEL;
    }

    public String getFIRSTAMMOUNT() {
        return FIRSTAMMOUNT;
    }

    public void setFIRSTAMMOUNT(String FIRSTAMMOUNT) {
        this.FIRSTAMMOUNT = FIRSTAMMOUNT;
    }

    public String getAPPENDAMMOUNT() {
        return APPENDAMMOUNT;
    }

    public void setAPPENDAMMOUNT(String APPENDAMMOUNT) {
        this.APPENDAMMOUNT = APPENDAMMOUNT;
    }

    public String getPRODTYPE() {
        return PRODTYPE;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public void setPRODTYPE(String PRODTYPE) {
        this.PRODTYPE = PRODTYPE;
    }

    public String getPUBCOMPANY() {
        return PUBCOMPANY;
    }

    public void setPUBCOMPANY(String PUBCOMPANY) {
        this.PUBCOMPANY = PUBCOMPANY;
    }

    public String getDESCRIPITION() {
        return DESCRIPITION;
    }

    public void setDESCRIPITION(String DESCRIPITION) {
        this.DESCRIPITION = DESCRIPITION;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getORDERNUM() {
        return ORDERNUM;
    }

    public void setORDERNUM(String ORDERNUM) {
        this.ORDERNUM = ORDERNUM;
    }

    public String getISHOT() {
        return ISHOT;
    }

    public void setISHOT(String ISHOT) {
        this.ISHOT = ISHOT;
    }

    public String getISSHOW() {
        return ISSHOW;
    }

    public void setISSHOW(String ISSHOW) {
        this.ISSHOW = ISSHOW;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public String getMODIFYTIME() {
        return MODIFYTIME;
    }

    public void setMODIFYTIME(String MODIFYTIME) {
        this.MODIFYTIME = MODIFYTIME;
    }

    public String getSUBNAME() {
        return SUBNAME;
    }

    public void setSUBNAME(String SUBNAME) {
        this.SUBNAME = SUBNAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getTIP() {
        return TIP;
    }

    public void setTIP(String TIP) {
        this.TIP = TIP;
    }

    public String getINCOMETYPE() {
        return INCOMETYPE;
    }

    public void setINCOMETYPE(String INCOMETYPE) {
        this.INCOMETYPE = INCOMETYPE;
    }

    public String getPRODRATIO() {
        return PRODRATIO;
    }

    public void setPRODRATIO(String PRODRATIO) {
        this.PRODRATIO = PRODRATIO;
    }

    public String getREVENUERULE() {
        return REVENUERULE;
    }

    public void setREVENUERULE(String REVENUERULE) {
        this.REVENUERULE = REVENUERULE;
    }

    public String getIPO_END_DATE() {
        return IPO_END_DATE;
    }

    public void setIPO_END_DATE(String IPO_END_DATE) {
        this.IPO_END_DATE = IPO_END_DATE;
    }

    public String getINTERESTDAY() {
        return INTERESTDAY;
    }

    public void setINTERESTDAY(String INTERESTDAY) {
        this.INTERESTDAY = INTERESTDAY;
    }

    public String getENDDAY() {
        return ENDDAY;
    }

    public void setENDDAY(String ENDDAY) {
        this.ENDDAY = ENDDAY;
    }

    public String getPAYDAY() {
        return PAYDAY;
    }

    public void setPAYDAY(String PAYDAY) {
        this.PAYDAY = PAYDAY;
    }

    public String getINVEST_TYPE() {
        return INVEST_TYPE;
    }

    public void setINVEST_TYPE(String INVEST_TYPE) {
        this.INVEST_TYPE = INVEST_TYPE;
    }

    public String getPROD_STATUS() {
        return PROD_STATUS;
    }

    public String getIPO_BEGIN_DATE() {
        return IPO_BEGIN_DATE;
    }

    public void setIPO_BEGIN_DATE(String IPO_BEGIN_DATE) {
        this.IPO_BEGIN_DATE = IPO_BEGIN_DATE;
    }


    public void setCREAT_TIME(String CREAT_TIME) {
        this.CREAT_TIME = CREAT_TIME;
    }

    public String getCREAT_TIME() {
        return CREAT_TIME;
    }

    public void setPROD_STATUS(String PROD_STATUS) {
        this.PROD_STATUS = PROD_STATUS;
    }

    public String getSECURITYCODE() {
        return SECURITYCODE;
    }

    public void setSECURITYCODE(String SECURITYCODE) {
        this.SECURITYCODE = SECURITYCODE;
    }

    public String getFUNDNAME() {
        return FUNDNAME;
    }

    public void setFUNDNAME(String FUNDNAME) {
        this.FUNDNAME = FUNDNAME;
    }

    public String getKFZT() {
        return KFZT;
    }

    public void setKFZT(String KFZT) {
        this.KFZT = KFZT;
    }

    public String getFXDJ() {
        return FXDJ;
    }

    public void setFXDJ(String FXDJ) {
        this.FXDJ = FXDJ;
    }

    public String getQGJE() {
        return QGJE;
    }

    public void setQGJE(String QGJE) {
        this.QGJE = QGJE;
    }

    public String getFUNDTYPE() {
        return FUNDTYPE;
    }

    public void setFUNDTYPE(String FUNDTYPE) {
        this.FUNDTYPE = FUNDTYPE;
    }

    public String getESTABLISHMENTDATE() {
        return ESTABLISHMENTDATE;
    }

    public void setESTABLISHMENTDATE(String ESTABLISHMENTDATE) {
        this.ESTABLISHMENTDATE = ESTABLISHMENTDATE;
    }

    public String getUNITNV() {
        return UNITNV;
    }

    public void setUNITNV(String UNITNV) {
        this.UNITNV = UNITNV;
    }

    public String getACCUMULATEDUNITNV() {
        return ACCUMULATEDUNITNV;
    }

    public void setACCUMULATEDUNITNV(String ACCUMULATEDUNITNV) {
        this.ACCUMULATEDUNITNV = ACCUMULATEDUNITNV;
    }

    public String getDAILYPROFIT() {
        return DAILYPROFIT;
    }

    public void setDAILYPROFIT(String DAILYPROFIT) {
        this.DAILYPROFIT = DAILYPROFIT;
    }

    public String getLATESTWEEKLYYIELD() {
        return LATESTWEEKLYYIELD;
    }

    public void setLATESTWEEKLYYIELD(String LATESTWEEKLYYIELD) {
        this.LATESTWEEKLYYIELD = LATESTWEEKLYYIELD;
    }

    public String getOreder() {
        return Oreder;
    }

    public void setOreder(String oreder) {
        Oreder = oreder;
    }

    public String getCHANGEPCTRM() {
        return CHANGEPCTRM;
    }

    public void setCHANGEPCTRM(String CHANGEPCTRM) {
        this.CHANGEPCTRM = CHANGEPCTRM;
    }

    public String getINVESTADVISORNAME() {
        return INVESTADVISORNAME;
    }

    public void setINVESTADVISORNAME(String INVESTADVISORNAME) {
        this.INVESTADVISORNAME = INVESTADVISORNAME;
    }

    public String getFUNDTYPECODE() {
        return FUNDTYPECODE;
    }

    public void setFUNDTYPECODE(String FUNDTYPECODE) {
        this.FUNDTYPECODE = FUNDTYPECODE;
    }

    public String getAnnounceTitle() {
        return announceTitle;
    }

    public void setAnnounceTitle(String announceTitle) {
        this.announceTitle = announceTitle;
    }

    public String getAnnounceFrom() {
        return announceFrom;
    }

    public void setAnnounceFrom(String announceFrom) {
        this.announceFrom = announceFrom;
    }

    public String getAnnounceDate() {
        return announceDate;
    }

    public String getAnnounceId() {
        return announceId;
    }

    public void setAnnounceId(String announceId) {
        this.announceId = announceId;
    }

    public String getEarnType() {
        return earnType;
    }

    public void setEarnType(String earnType) {
        this.earnType = earnType;
    }

    public void setAnnounceDate(String announceDate) {
        this.announceDate = announceDate;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getProgressPersent() {
        return progressPersent;
    }

    public void setProgressPersent(String progressPersent) {
        this.progressPersent = progressPersent;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getProductType() {
        return productType;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getEarn() {
        return earn;
    }

    public void setEarn(String earn) {
        this.earn = earn;
    }

    public String getGeneralSituationTitle() {
        return generalSituationTitle;
    }

    public void setGeneralSituationTitle(String generalSituationTitle) {
        this.generalSituationTitle = generalSituationTitle;
    }

    public String getGeneralSituationContent() {
        return generalSituationContent;
    }

    public void setGeneralSituationContent(String generalSituationContent) {
        this.generalSituationContent = generalSituationContent;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundLevel() {
        return fundLevel;
    }

    public void setFundLevel(String fundLevel) {
        this.fundLevel = fundLevel;
    }

    public String getFundExpectEarnings() {
        return fundExpectEarnings;
    }

    public void setFundExpectEarnings(String fundExpectEarnings) {
        this.fundExpectEarnings = fundExpectEarnings;
    }

    public String getFundTimeLimit() {
        return fundTimeLimit;
    }

    public void setFundTimeLimit(String fundTimeLimit) {
        this.fundTimeLimit = fundTimeLimit;
    }

    public String getFundStartBuy() {
        return fundStartBuy;
    }

    public void setFundStartBuy(String fundStartBuy) {
        this.fundStartBuy = fundStartBuy;
    }

    public String getHistoryNetValueDate() {
        return historyNetValueDate;
    }

    public void setHistoryNetValueDate(String historyNetValueDate) {
        this.historyNetValueDate = historyNetValueDate;
    }

    public String getHistoryNetValueUnit() {
        return historyNetValueUnit;
    }

    public void setHistoryNetValueUnit(String historyNetValueUnit) {
        this.historyNetValueUnit = historyNetValueUnit;
    }

    public String getHistoryNetValueTotal() {
        return historyNetValueTotal;
    }

    public void setHistoryNetValueTotal(String historyNetValueTotal) {
        this.historyNetValueTotal = historyNetValueTotal;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getFundCompany() {
        return fundCompany;
    }

    public void setFundCompany(String fundCompany) {
        this.fundCompany = fundCompany;
    }

    public String getFundOpenState() {
        return fundOpenState;
    }

    public void setFundOpenState(String fundOpenState) {
        this.fundOpenState = fundOpenState;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundCreatTime() {
        return fundCreatTime;
    }

    public void setFundCreatTime(String fundCreatTime) {
        this.fundCreatTime = fundCreatTime;
    }

    public String getGeneralSituationContentUrl() {
        return generalSituationContentUrl;
    }

    public void setGeneralSituationContentUrl(String generalSituationContentUrl) {
        this.generalSituationContentUrl = generalSituationContentUrl;
    }

    public List<Map<String, String>> getNoticeDates() {
        return noticeDates;
    }

    public void setNoticeDates(List<Map<String, String>> noticeDates) {
        this.noticeDates = noticeDates;
    }

    public List<Map<String, String>> getProaoclDates() {
        return proaoclDates;
    }

    public void setProaoclDates(List<Map<String, String>> proaoclDates) {
        this.proaoclDates = proaoclDates;
    }

    public List<Map<String, String>> getOtcNoticeDate() {
        return otcNoticeDate;
    }

    public void setOtcNoticeDate(List<Map<String, String>> otcNoticeDate) {
        this.otcNoticeDate = otcNoticeDate;
    }

    public List<Map<String, String>> getOtcProaoclDate() {
        return otcProaoclDate;
    }

    public void setOtcProaoclDate(List<Map<String, String>> otcProaoclDate) {
        this.otcProaoclDate = otcProaoclDate;
    }
}
