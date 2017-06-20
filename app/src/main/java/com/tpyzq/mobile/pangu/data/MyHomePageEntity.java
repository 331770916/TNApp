package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangqi on 2016/10/24.
 */
public class MyHomePageEntity implements Parcelable {
    private String code;
    private String msg;
    private String count_count;
    private String count_one;
    private String count_two;
    private String count_three;
    private int count_int;
    private int amount_int;
    private String token_Inform;
    private String push_time_inform;
    private String token_newshare;
    private String push_time_newshare;
    private String token_warning;
    private String push_time_warning;


    public MyHomePageEntity() {
    }

    public MyHomePageEntity(String code, String msg, String count_count, String count_one, String count_two, String count_three, int count_int, int amount_int, String token_Inform, String push_time_inform,
                            String token_newshare, String push_time_newshare, String token_warning, String push_time_warning) {
        this.code = code;
        this.msg = msg;
        this.count_count = count_count;
        this.count_one = count_one;
        this.count_two = count_two;
        this.count_three = count_three;
        this.count_int = count_int;
        this.amount_int = amount_int;
        this.token_Inform = token_Inform;
        this.push_time_inform = push_time_inform;
        this.token_newshare = token_newshare;
        this.push_time_newshare = push_time_newshare;
        this.token_warning = token_warning;
        this.push_time_warning = push_time_warning;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(count_count);
        dest.writeString(count_one);
        dest.writeString(count_two);
        dest.writeString(count_three);
        dest.writeInt(count_int);
        dest.writeInt(amount_int);
        dest.writeString(token_Inform);
        dest.writeString(push_time_inform);
        dest.writeString(token_newshare);
        dest.writeString(push_time_newshare);
        dest.writeString(token_warning);
        dest.writeString(push_time_warning);
    }

    public static final Creator<MyHomePageEntity> CREATOR = new Creator<MyHomePageEntity>() {

        @Override
        public MyHomePageEntity createFromParcel(Parcel source) {
            MyHomePageEntity bean = new MyHomePageEntity();
            bean.count_count = source.readString();
            bean.count_one = source.readString();
            bean.count_two = source.readString();
            bean.count_three = source.readString();
            bean.count_int = source.readInt();
            bean.amount_int = source.readInt();
            bean.token_Inform = source.readString();
            bean.push_time_inform = source.readString();
            bean.token_newshare = source.readString();
            bean.push_time_newshare = source.readString();
            bean.token_warning=source.readString();
            bean.push_time_warning=source.readString();

            return bean;
        }

        @Override
        public MyHomePageEntity[] newArray(int size) {
            return new MyHomePageEntity[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCount_count() {
        return count_count;
    }

    public void setCount_count(String count_count) {
        this.count_count = count_count;
    }

    public String getCount_one() {
        return count_one;
    }

    public void setCount_one(String count_one) {
        this.count_one = count_one;
    }

    public String getCount_two() {
        return count_two;
    }

    public void setCount_two(String count_two) {
        this.count_two = count_two;
    }

    public String getCount_three() {
        return count_three;
    }

    public void setCount_three(String count_three) {
        this.count_three = count_three;
    }

    public int getCount_int() {
        return count_int;
    }

    public void setCount_int(int count_int) {
        this.count_int = count_int;
    }

    public int getAmount_int() {
        return amount_int;
    }

    public void setAmount_int(int amount_int) {
        this.amount_int = amount_int;
    }

    public String getToken_Inform() {
        return token_Inform;
    }

    public void setToken_Inform(String token_Inform) {
        this.token_Inform = token_Inform;
    }

    public String getPush_time_inform() {
        return push_time_inform;
    }

    public void setPush_time_inform(String push_time_inform) {
        this.push_time_inform = push_time_inform;
    }

    public String getToken_newshare() {
        return token_newshare;
    }

    public void setToken_newshare(String token_newshare) {
        this.token_newshare = token_newshare;
    }

    public String getPush_time_newshare() {
        return push_time_newshare;
    }

    public void setPush_time_newshare(String push_time_newshare) {
        this.push_time_newshare = push_time_newshare;
    }

    public String getToken_warning() {
        return token_warning;
    }

    public void setToken_warning(String token_warning) {
        this.token_warning = token_warning;
    }

    public String getPush_time_warning() {
        return push_time_warning;
    }

    public void setPush_time_warning(String push_time_warning) {
        this.push_time_warning = push_time_warning;
    }
}
