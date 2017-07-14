package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2017/6/27.
 * 网络投票
 */
public class NetworkVotingEntity {
    private String meeting_name; //股东大会名称
    private String meeting_seq;  //股东大会编码
    private String company_name; //公司名称
    private String company_code; //公司代码(股票代码)
    private String begin_date;  //起始日期
    private String end_date;    //到期日期
    private String init_date;   //交易日期<到期交易日 委托日期 TODO>
    private String position_str; //定位串<股东大会编码 TODO>
    private String vote_motion; //投票议案（序号,议案编号）
    private String vote_info;   //议案名称
    private String vote_type;   //投票类别：0	非累积投票制 1累积投票制
    private String stock_code;   //证券代码
    private String stock_account; //股东代码
    private String business_amount;   //成交数量<委托数量 TODO>
    private String stock_name;   //证券名称
    private String status;      //委托状态
    private String entrust_status_name;//委托状态名称
    private String exchange_type; //市场
    private boolean isShowRule;             //控制子view是否显示
    private String entrust_amount;//累积投票和非累积投票 <委托状态名称 TODO>
    private String entrust_no;//委托编号
    private List<NetworkVotingEntity> list;

    public String getEntrust_status_name() {
        return entrust_status_name;
    }

    public void setEntrust_status_name(String entrust_status_name) {
        this.entrust_status_name = entrust_status_name;
    }

    public String getEntrust_no() {
        return entrust_no;
    }

    public void setEntrust_no(String entrust_no) {
        this.entrust_no = entrust_no;
    }

    public String getEntrust_amount() {
        return entrust_amount;
    }

    public void setEntrust_amount(String entrust_amount) {
        this.entrust_amount = entrust_amount;
    }

    public String getExchange_type() {
        return exchange_type;
    }

    public void setExchange_type(String exchange_type) {
        this.exchange_type = exchange_type;
    }

    public String getStock_account() {
        return stock_account;
    }

    public void setStock_account(String stock_account) {
        this.stock_account = stock_account;
    }

    public List<NetworkVotingEntity> getList() {
        return list;
    }

    public void setList(List<NetworkVotingEntity> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVote_info() {
        return vote_info;
    }

    public void setVote_info(String vote_info) {
        this.vote_info = vote_info;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public String getMeeting_seq() {
        return meeting_seq;
    }

    public void setMeeting_seq(String meeting_seq) {
        this.meeting_seq = meeting_seq;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getPosition_str() {
        return position_str;
    }

    public void setPosition_str(String position_str) {
        this.position_str = position_str;
    }

    public String getVote_motion() {
        return vote_motion;
    }

    public void setVote_motion(String vote_motion) {
        this.vote_motion = vote_motion;
    }

    public String getVote_type() {
        return vote_type;
    }

    public void setVote_type(String vote_type) {
        this.vote_type = vote_type;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getBusiness_amount() {
        return business_amount;
    }

    public void setBusiness_amount(String business_amount) {
        this.business_amount = business_amount;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public boolean isShowRule() {
        return isShowRule;
    }

    public void setShowRule(boolean showRule) {
        isShowRule = showRule;
    }
}
