package com.tpyzq.mobile.pangu.data;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金实体类
 */

public class StructuredFundEntity {
    private String stoken_name;              //证券名称
    private String merge_amount;             //可合并数量
    private String split_amount;             //可拆分数量
    private String exchange_type;            //交易类别
    private String fund_status;              //基金状态
    private String stock_account;            //当前市场的主证券账户

    private String stocken_code;            //证券代码
    private String market;                  //交易市场

    private String init_date;               //交易日期
    private String entrust_no;              //委托编号

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
