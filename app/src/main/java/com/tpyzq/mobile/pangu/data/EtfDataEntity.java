package com.tpyzq.mobile.pangu.data;

/**
 * Created by zhang on 2017/7/4.
 * etf 参数实体类
 */

public class EtfDataEntity {
    private String exchange_type;    // 交易类别
    private String stock_code;       // 证券代码     基金代码
    private String stock_name;       // 证券名称     基金名称  <成份股名称 COMPONENT_NAME TODO>
    private String component_code;      //成份股代码
    private String stock_account;    // 股东账号
    private String enable_balance;   // 可用资金
    private String stock_max;        //  网下股票认购上限 <溢价比率 REPLACE_RATIO TODO>
    private String cash_max;         // 网下现金认购上限 <替代标志 REPLACE_FLAG TODO>
    private String allot_max;      // 申购上限 <替代标志名称 REPLACE_FLAG_NAME TODO>
    private String redeem_max;       //赎回上限

    private String init_date;        //  交易日期
    private String entrust_no;       // 委托编号
    private String entrust_balance;    // 委托金额 （<替代金额 REPLACE_BALANCE TODO>）
    private String entrust_amount;     // 委托数量（<成分股数量 COMPONENT_AMOUNT TODO>）
    private String entrust_prop;       // 委托属性  0 买卖 1 配股 2	转托  3 申购  4	回购
    private String prev_balance;       // 冻结解冻金额
    private String entrust_status_name;   // 委托状态名称
    private String exchange_type_name;    //状态名称
    /**
     * 委托状态
     * 0	未报
     * 1	待报
     * 2	已报
     * 3	已报待撤
     * 4	部成待撤
     * 5	部撤
     * 6	已撤
     * 7	部成
     * 8	已成
     * 9	废单
     */
    private String entrust_status;
    private String report_time;    // 申报时间
    private String business_amount;  //  成交数量
    private String entrust_price;     // 委托价格  （委托金额）
    private String position_str;    //  定位串
    private String trade_plat;   // 交易平台
    private String entrust_bs;//申赎成交查询 买卖方向 1 买入  2  卖出
    private String real_status_name;//申赎成交查询 状态名称
    private String real_status ;      //申赎成交查询 状态
    private String curr_time ;      //申赎成交查询 委托时间
    private String business_balance ;      //申赎成交查询 成交金额
    private String cbp_business_id ;      //申赎成交查询 成交编号

    public String getCbp_business_id() {
        return cbp_business_id;
    }

    public void setCbp_business_id(String cbp_business_id) {
        this.cbp_business_id = cbp_business_id;
    }

    public String getComponent_code() {
        return component_code;
    }

    public void setComponent_code(String component_code) {
        this.component_code = component_code;
    }

    public String getReal_status_name() {
        return real_status_name;
    }

    public void setReal_status_name(String real_status_name) {
        this.real_status_name = real_status_name;
    }


    public String getBusiness_balance() {
        return business_balance;
    }

    public void setBusiness_balance(String business_balance) {
        this.business_balance = business_balance;
    }

    public String getEntrust_status_name() {
        return entrust_status_name;
    }

    public void setEntrust_status_name(String entrust_status_name) {
        this.entrust_status_name = entrust_status_name;
    }

    public String getExchange_type_name() {
        return exchange_type_name;
    }

    public void setExchange_type_name(String exchange_type_name) {
        this.exchange_type_name = exchange_type_name;
    }

    public String getCurr_time() {
        return curr_time;
    }

    public void setCurr_time(String curr_time) {
        this.curr_time = curr_time;
    }

    public String getPrev_balance() {
        return prev_balance;
    }

    public void setPrev_balance(String prev_balance) {
        this.prev_balance = prev_balance;
    }

    public String getEntrust_bs() {
        return entrust_bs;
    }

    public void setEntrust_bs(String entrust_bs) {
        this.entrust_bs = entrust_bs;
    }

    public boolean isShowRule() {
        return isShowRule;
    }

    public void setShowRule(boolean showRule) {
        isShowRule = showRule;
    }

    private boolean isShowRule;             //控制子view是否显示

    public String getTrade_plat() {
        return trade_plat;
    }

    public void setTrade_plat(String trade_plat) {
        this.trade_plat = trade_plat;
    }


    public String getPosition_str() {
        return position_str;
    }

    public void setPosition_str(String position_str) {
        this.position_str = position_str;
    }

    public String getExchange_type() {
        return exchange_type;
    }

    public void setExchange_type(String exchange_type) {
        this.exchange_type = exchange_type;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getStock_account() {
        return stock_account;
    }

    public void setStock_account(String stock_account) {
        this.stock_account = stock_account;
    }

    public String getEnable_balance() {
        return enable_balance;
    }

    public void setEnable_balance(String enable_balance) {
        this.enable_balance = enable_balance;
    }

    public String getStock_max() {
        return stock_max;
    }

    public void setStock_max(String stock_max) {
        this.stock_max = stock_max;
    }

    public String getCash_max() {
        return cash_max;
    }

    public void setCash_max(String cash_max) {
        this.cash_max = cash_max;
    }

    public String getAllot_max() {
        return allot_max;
    }

    public void setAllot_max(String allot_max) {
        this.allot_max = allot_max;
    }

    public String getRedeem_max() {
        return redeem_max;
    }

    public void setRedeem_max(String redeem_max) {
        this.redeem_max = redeem_max;
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

    public String getEntrust_balance() {
        return entrust_balance;
    }

    public void setEntrust_balance(String entrust_balance) {
        this.entrust_balance = entrust_balance;
    }

    public String getEntrust_amount() {
        return entrust_amount;
    }

    public void setEntrust_amount(String entrust_amount) {
        this.entrust_amount = entrust_amount;
    }

    public String getEntrust_prop() {
        return entrust_prop;
    }

    public void setEntrust_prop(String entrust_prop) {
        this.entrust_prop = entrust_prop;
    }

    public String getEntrust_status() {
        return entrust_status;
    }

    public void setEntrust_status(String entrust_status) {
        this.entrust_status = entrust_status;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public String getBusiness_amount() {
        return business_amount;
    }

    public void setBusiness_amount(String business_amount) {
        this.business_amount = business_amount;
    }

    public String getEntrust_price() {
        return entrust_price;
    }

    public void setEntrust_price(String entrust_price) {
        this.entrust_price = entrust_price;
    }

    public String getReal_status() {
        return real_status;
    }

    public void setReal_status(String real_status) {
        this.real_status = real_status;
    }
}
