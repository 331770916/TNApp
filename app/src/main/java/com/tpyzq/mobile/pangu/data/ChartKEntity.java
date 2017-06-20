package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangwenbo on 2016/6/3.
 * K线图数据
 */
public class ChartKEntity implements Parcelable{

    private String time;        //时间
    private String open;        //今开
    private String close;       //昨收
    private String high;        //最高
    private String low;         //最低
    private String amount;      //总成交量
    private String amountMoney; //总成交金额

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmountMoney(String amountMoney) {
        this.amountMoney = amountMoney;
    }

    public String getAmountMoney() {
        return amountMoney;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getClose() {
        return close;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getHigh() {
        return high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getLow() {
        return low;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getOpen() {
        return open;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(open);
        dest.writeString(close);
        dest.writeString(high);
        dest.writeString(low);
        dest.writeString(amount);
        dest.writeString(amountMoney);
    }

    public static final Creator<ChartKEntity> CREATOR = new Creator<ChartKEntity>(){
        @Override
        public ChartKEntity createFromParcel(Parcel source) {
            ChartKEntity chartKEntity = new ChartKEntity();
            chartKEntity.time = source.readString();
            chartKEntity.open  = source.readString() ;        //今开
            chartKEntity.close = source.readString();       //昨收
            chartKEntity.high = source.readString();        //最高
            chartKEntity.low = source.readString();         //最低
            chartKEntity.amount = source.readString();      //总成交量
            chartKEntity.amountMoney = source.readString(); //总成交金额
            return chartKEntity;
        }

        @Override
        public ChartKEntity[] newArray(int size) {
            return new ChartKEntity[size];
        }
    };
}
