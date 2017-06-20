package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/20 15:01
 * 资讯 模块   常用的实体类
 */
public class InformationBean implements Serializable {
    private String publishTime;             //资讯发布时间
    private String publishTitle;            //资讯发布标题
    private String publishBreviaryContent;  //资讯列表缩略内容
    private String publishAboutStock;       //资讯相关股票



    private long time;                       //long 类型的时间值
    private int type;                        //资讯重大事件列表类型 判断的字段
    private String tname;                    //资讯 热点公告解析详情界面     上面的状态   如  获补贴
    private String prob;                     //资讯 热点公告解析详情界面    的  百分比
    private String days;                     //资讯 热点公告解析   建议持有天数
    private String state;                     //资讯 热点公告解析   webView 中展示的数据
    private String stockcode;                //资讯 热点公告解析   股票代码
    private String price;                    //资讯热点公告解析    里面的最新价格

    private String relevanceName;              //资讯   直播里面  相关股票名称
    private String date;                        //资讯 直播  里用到的  日期
    private String times;                       //资讯 直播  里用到的  时间
    private int pos;                            //资讯 直播  判断图片用的
    private List<String> sopCastList;         //资讯  直播  里用于存  相关股票的集合
    private String requestId;                   //资讯重大事件详情  请求入参  id
    private String close;               //前收盘价格

    private String stocks;                      //热点公告详情  股票代码
    private String jsonArray;                //item数据
    private String stockslength;                //数据长度
    private ArrayList<String> name;                        //热点股票名称
    private String thePrice;                    //热点公告 展示股票价格


    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public String getStockslength() {
        return stockslength;
    }

    public void setStockslength(String stockslength) {
        this.stockslength = stockslength;
    }

    public String getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(String jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public List<String> getSopCastList() {
        return sopCastList;
    }

    public void setSopCastList(List<String> sopCastList) {
        this.sopCastList = sopCastList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getRelevanceName() {
        return relevanceName;
    }

    public void setRelevanceName(String relevanceName) {
        this.relevanceName = relevanceName;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishTitle() {
        return publishTitle;
    }

    public void setPublishTitle(String publishTitle) {
        this.publishTitle = publishTitle;
    }

    public String getPublishBreviaryContent() {
        return publishBreviaryContent;
    }

    public void setPublishBreviaryContent(String publishBreviaryContent) {
        this.publishBreviaryContent = publishBreviaryContent;
    }

    public String getPublishAboutStock() {
        return publishAboutStock;
    }

    public void setPublishAboutStock(String publishAboutStock) {
        this.publishAboutStock = publishAboutStock;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getProb() {
        return prob;
    }

    public void setProb(String prob) {
        this.prob = prob;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setThePrice(String thePrice) {
        this.thePrice = thePrice;
    }

    public String getThePrice() {
        return thePrice;
    }
}
