package com.tpyzq.mobile.pangu.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangwenbo on 2016/6/6.
 * 分时实体类
 */
public class ChartTimeEntitiy implements Parcelable{

    private String amount;      //总量
    private String price;       //现价
    private String time;        //时间

    private float  totalPrice;  //总价
    private int    acountBuy;   //买入量
    private float  valPrice;    //均价
    private float  percentage;  //百分比

    public static float percentMax;  // y轴右侧的最大百分比数 = 偏移量 + 0.05f
    public static float percentMin;  // y轴左侧的最小百分比数 = - （偏移量 + 0.05f）
    public static float maxyValue ; // y轴左侧的最大值  =  close * (1 + percentMax)
    public static float minyValue ; // y轴左侧的最小值  = close * (1 - percentMax)
    public static float baseValue ; //基准值  就是close

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setAcountBuy(int acountBuy) {
        this.acountBuy = acountBuy;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setValPrice(float valPrice) {
        this.valPrice = valPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public float getValPrice() {
        return valPrice;
    }

    public int getAcountBuy() {
        return acountBuy;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeString(price);
        dest.writeString(time);
        dest.writeFloat(totalPrice);
        dest.writeInt(acountBuy);
        dest.writeFloat(valPrice);
        dest.writeFloat(percentage);
    }

    public static  final Creator<ChartTimeEntitiy> CREATOR = new Creator<ChartTimeEntitiy>(){
        @Override
        public ChartTimeEntitiy createFromParcel(Parcel source) {
            ChartTimeEntitiy chartTimeEntitiy = new ChartTimeEntitiy();
            chartTimeEntitiy.amount = source.readString();      //总量
            chartTimeEntitiy.price = source.readString();       //现价
            chartTimeEntitiy.time = source.readString();        //时间
            chartTimeEntitiy.totalPrice = source.readFloat();  //总价
            chartTimeEntitiy.acountBuy = source.readInt();   //买入量
            chartTimeEntitiy.valPrice = source.readFloat();    //均价
            chartTimeEntitiy.percentage = source.readFloat();  //百分比

            return chartTimeEntitiy;
        }

        @Override
        public ChartTimeEntitiy[] newArray(int size) {
            return new ChartTimeEntitiy[size];
        }
    };
}
