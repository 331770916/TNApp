package com.tpyzq.mobile.pangu.data;

/**
 * Created by 刘泽鹏 on 2016/8/10.
 */
public class OneKeySubscribeItem {
    private int id;
    private String name;    //股票名称
    private String code;    //股票代码
    private String price;   //申购价格
    private String limit;   //申购上限
    private String num;     //申购股数
    private String market;  //市场
    private boolean isCheck = true;    //是否选中


    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


}
