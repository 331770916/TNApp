package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangwenbo on 2016/6/4.
 * MACD实体类
 */
public class MACDEntity implements Parcelable {

    private double dea;
    private double diff;
    private double macd;
    private String date;
    public static float value = 1f;//最大值最小值

    public MACDEntity() {
    }

    public MACDEntity(double dea, double diff, double macd, String date) {
        this.dea = dea;
        this.diff = diff;
        this.macd = macd;
        this.date = date;
    }

    public double getDea() {
        return this.dea;
    }

    public void setDea(double dea) {
        this.dea = dea;
    }

    public double getDiff() {
        return this.diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public double getMacd() {
        return this.macd;
    }

    public void setMacd(double macd) {
        this.macd = macd;
    }

    public String getDate() {
        return this.date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(dea);
        dest.writeDouble(diff);
        dest.writeDouble(macd);
        dest.writeString(date);
    }

    public static final Creator<MACDEntity> CREATOR = new Creator<MACDEntity>(){
        @Override
        public MACDEntity createFromParcel(Parcel source) {
            MACDEntity macdEntity = new MACDEntity();
            macdEntity.dea = source.readDouble();
            macdEntity.diff = source.readDouble();
            macdEntity.macd = source.readDouble();
            macdEntity.date = source.readString();

            return macdEntity;
        }

        @Override
        public MACDEntity[] newArray(int size) {
            return new MACDEntity[size];
        }
    };
}
