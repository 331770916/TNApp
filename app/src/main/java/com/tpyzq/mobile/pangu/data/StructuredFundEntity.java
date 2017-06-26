package com.tpyzq.mobile.pangu.data;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金实体类
 */

public class StructuredFundEntity {
    private String stoken_name;              //证券名称
    private String stocken_code;            //证券代码
    private String business_name;            //业务
    private String report_time;               //时间
    private String entrust_amount;            //分拆份额（委托数量）
    private String position_str;               //定位串 翻页用
    private String curr_date;               //日期
    private String entrust_status;          //状态（0未报、1待报、2已报、3已报待撤。。。）
    private String entrust_balance;         //委托金额
    private String business_amount;         //成交数量
    private String merge_amount;             //可合并数量
    private String split_amount;             //可拆分数量
    private String exchange_type;            //交易类别
    private String fund_status;              //基金状态
    private String stock_account;            //当前市场的主证券账户(股东代码、不包含"沪A")
    private String market;                  //交易市场
    private String init_date;               //交易日期(发生日期)
    private String entrust_no;              //委托编号（撤单操作时使用）
    private String serial_no;                 //流水号

    private boolean isShowRule;         //控制子view是否显示

    public boolean isShowRule() {
        return isShowRule;
    }

    public void setShowRule(boolean showRule) {
        isShowRule = showRule;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getEntrust_balance() {
        return entrust_balance;
    }

    public void setEntrust_balance(String entrust_balance) {
        this.entrust_balance = entrust_balance;
    }

    public String getBusiness_amount() {
        return business_amount;
    }


    public void setBusiness_amount(String business_amount) {
        this.business_amount = business_amount;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getEntrust_amount() {
        return entrust_amount;
    }

    public void setEntrust_amount(String entrust_amount) {
        this.entrust_amount = entrust_amount;
    }

    public String getPosition_str() {
        return position_str;
    }

    public void setPosition_str(String position_str) {
        this.position_str = position_str;
    }

    public String getCurr_date() {
        return curr_date;
    }

    public void setCurr_date(String curr_date) {
        this.curr_date = curr_date;
    }

    public String getEntrust_status() {
        return entrust_status;
    }

    public void setEntrust_status(String entrust_status) {
        this.entrust_status = entrust_status;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getEntrust_no() {
        return entrust_no;
    }

    public void setEntrust_no(String entrust_no) {
        this.entrust_no = entrust_no;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getStocken_code() {
        return stocken_code;
    }

    public void setStocken_code(String stocken_code) {
        this.stocken_code = stocken_code;
    }

    public String getStoken_name() {
        return stoken_name;
    }

    public void setStoken_name(String stoken_name) {
        this.stoken_name = stoken_name;
    }

    public String getMerge_amount() {
        return merge_amount;
    }

    public void setMerge_amount(String merge_amount) {
        this.merge_amount = merge_amount;
    }

    public String getSplit_amount() {
        return split_amount;
    }

    public void setSplit_amount(String split_amount) {
        this.split_amount = split_amount;
    }

    public String getExchange_type() {
        return exchange_type;
    }

    public void setExchange_type(String exchange_type) {
        this.exchange_type = exchange_type;

    }

    public String getFund_status() {
        return fund_status;
    }

    public void setFund_status(String fund_status) {
        this.fund_status = fund_status;
    }

    public String getStock_account() {
        return stock_account;
    }

    public void setStock_account(String stock_account) {
        this.stock_account = stock_account;
    }
}
