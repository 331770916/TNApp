package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * 定投基金实体
 * Created by lx on 2017/7/19.
 */

public class FixFundEntity implements Serializable {
    private String FUND_CODE;//基金代码
    private String FUND_NAME;//基金名称
    private String BALANCE;//发生金额
    private String SEND_BALANCE;//共申购金额
    private String EN_FUND_DATE;//扣款允许日
    private String END_DATE;//到期日期
    private String DEAL_DATE;//处理日期
    private String ALLOTNO;//申请编号
    private String DEAL_FLAG;//处理标志  0：未处理 1：已处理 2：处理失败 3处理作废 4已撤单
    private String DEAL_FLAG_NAME;//处理标志名字
    private String START_DATE;//开始日期
    private String CURR_RATION_TIMES;//累计期数
    private String POSITION_STR;//定位串

    public String getPOSITION_STR() {
        return POSITION_STR;
    }

    public void setPOSITION_STR(String POSITION_STR) {
        this.POSITION_STR = POSITION_STR;
    }

    public String getFUND_CODE() {
        return FUND_CODE;
    }

    public void setFUND_CODE(String FUND_CODE) {
        this.FUND_CODE = FUND_CODE;
    }

    public String getFUND_NAME() {
        return FUND_NAME;
    }

    public void setFUND_NAME(String FUND_NAME) {
        this.FUND_NAME = FUND_NAME;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String BALANCE) {
        this.BALANCE = BALANCE;
    }

    public String getSEND_BALANCE() {
        return SEND_BALANCE;
    }

    public void setSEND_BALANCE(String SEND_BALANCE) {
        this.SEND_BALANCE = SEND_BALANCE;
    }

    public String getEN_FUND_DATE() {
        return EN_FUND_DATE;
    }

    public void setEN_FUND_DATE(String EN_FUND_DATE) {
        this.EN_FUND_DATE = EN_FUND_DATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getDEAL_DATE() {
        return DEAL_DATE;
    }

    public void setDEAL_DATE(String DEAL_DATE) {
        this.DEAL_DATE = DEAL_DATE;
    }

    public String getALLOTNO() {
        return ALLOTNO;
    }

    public void setALLOTNO(String ALLOTNO) {
        this.ALLOTNO = ALLOTNO;
    }

    public String getDEAL_FLAG() {
        return DEAL_FLAG;
    }

    public void setDEAL_FLAG(String DEAL_FLAG) {
        this.DEAL_FLAG = DEAL_FLAG;
    }

    public String getDEAL_FLAG_NAME() {
        return DEAL_FLAG_NAME;
    }

    public void setDEAL_FLAG_NAME(String DEAL_FLAG_NAME) {
        this.DEAL_FLAG_NAME = DEAL_FLAG_NAME;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getCURR_RATION_TIMES() {
        return CURR_RATION_TIMES;
    }

    public void setCURR_RATION_TIMES(String CURR_RATION_TIMES) {
        this.CURR_RATION_TIMES = CURR_RATION_TIMES;
    }


    public FixFundEntity(String FUND_CODE, String FUND_NAME, String BALANCE, String SEND_BALANCE, String EN_FUND_DATE, String END_DATE, String DEAL_DATE, String ALLOTNO, String DEAL_FLAG, String DEAL_FLAG_NAME, String START_DATE, String CURR_RATION_TIMES) {
        this.FUND_CODE = FUND_CODE;
        this.FUND_NAME = FUND_NAME;
        this.BALANCE = BALANCE;
        this.SEND_BALANCE = SEND_BALANCE;
        this.EN_FUND_DATE = EN_FUND_DATE;
        this.END_DATE = END_DATE;
        this.DEAL_DATE = DEAL_DATE;
        this.ALLOTNO = ALLOTNO;
        this.DEAL_FLAG = DEAL_FLAG;
        this.DEAL_FLAG_NAME = DEAL_FLAG_NAME;
        this.START_DATE = START_DATE;
        this.CURR_RATION_TIMES = CURR_RATION_TIMES;
    }

    public FixFundEntity() {
    }
}
