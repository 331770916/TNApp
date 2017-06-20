package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangwenbo on 2016/9/17.
 * 资讯实体类
 */
public class InformationEntity implements Parcelable {

    private String publishTime;             //资讯发布时间
    private String publishTitle;            //资讯发布标题
    private String publishBreviaryContent;  //资讯列表缩略内容
    private String publishAboutStock;       //资讯相关股票
    private String newsId;                  //新闻id


    private long time;                       //long 类型的时间值
    private int type;                        //资讯重大事件列表类型 判断的字段
    private String tname;                    //资讯 热点公告解析详情界面     上面的状态   如  获补贴
    private String prob;                     //资讯 热点公告解析详情界面    的  百分比
    private String days;                     //资讯 热点公告解析   建议持有天数
    private String state;                     //资讯 热点公告解析   webView 中展示的数据
    private String stockcode;                //资讯 热点公告解析   股票代码
    private String price;                    //资讯热点公告解析    里面的最新价格


    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(publishTime);
        dest.writeString(publishTitle);
        dest.writeString(publishBreviaryContent);
        dest.writeString(publishAboutStock);
        dest.writeString(newsId);
    }

    public static final Creator<InformationEntity> CREATOR = new Creator<InformationEntity>(){
        @Override
        public InformationEntity createFromParcel(Parcel source) {
            InformationEntity informationEntity = new InformationEntity();
            informationEntity.publishTime = source.readString();
            informationEntity.publishTitle = source.readString();
            informationEntity.publishBreviaryContent = source.readString();
            informationEntity.publishAboutStock = source.readString();
            informationEntity.newsId = source.readString();

            return informationEntity;
        }

        @Override
        public InformationEntity[] newArray(int size) {
            return new InformationEntity[size];
        }
    };

}
