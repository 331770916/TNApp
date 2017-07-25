package com.tpyzq.mobile.pangu.data;

/**
 * 作者：刘泽鹏 on 2016/10/19 13:41
 * OTC 份额  存储和传递  数据的 实体类
 */
public class OtcShareEntity {

    String current_amount;   //份额
    String market_value;     //市值
    String prod_name;        //股票名称
    String prod_code;        //股票代码
    String buy_date;         //购入日期
    String prod_end_date;    //到期日期
    boolean unFold;          //判断 是否展开


    public String getMarket_value() {
        return market_value;
    }

    public void setMarket_value(String market_value) {
        this.market_value = market_value;
    }

    public boolean isUnFold() {
        return unFold;
    }

    public void setUnFold(boolean unFold) {
        this.unFold = unFold;
    }

    public String getCurrent_amount() {
        return current_amount;
    }

    public void setCurrent_amount(String current_amount) {
        this.current_amount = current_amount;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public String getProd_end_date() {
        return prod_end_date;
    }

    public void setProd_end_date(String prod_end_date) {
        this.prod_end_date = prod_end_date;
    }
}
