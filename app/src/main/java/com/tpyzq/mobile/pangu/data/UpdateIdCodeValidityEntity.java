package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangwenbo on 2017/6/21.
 * 身份证有效期修改实体类
 */

public class UpdateIdCodeValidityEntity implements Parcelable {

    private String error_no;        //接口编号 0表示正常流程，1表示有重要资料修改，-1表示接口异常
    private String error_info;      //异常信息
    private String apply_status;    //1.申请中 2.处理中 8.成功，9失败
    private String user_biz_id;

    private String userId;
    private String name;
    private String apply_id;        //申请业务id
    private String idCard;          //身份证号码
    private String issued_depart;   //身份证签发机关
    private String idCardAddress;   //身份证地址
    private String idCardBeginDate; //身份证有效期开始时间
    private String idCardEndDate;   //身份证有效期截止时间

    public String getError_no() {
        return error_no;
    }

    public void setError_no(String error_no) {
        this.error_no = error_no;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public String getApply_status() {
        return apply_status;
    }

    public void setApply_status(String apply_status) {
        this.apply_status = apply_status;
    }

    public String getUser_biz_id() {
        return user_biz_id;
    }

    public void setUser_biz_id(String user_biz_id) {
        this.user_biz_id = user_biz_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIssued_depart() {
        return issued_depart;
    }

    public void setIssued_depart(String issued_depart) {
        this.issued_depart = issued_depart;
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress;
    }

    public String getIdCardBeginDate() {
        return idCardBeginDate;
    }

    public void setIdCardBeginDate(String idCardBeginDate) {
        this.idCardBeginDate = idCardBeginDate;
    }

    public String getIdCardEndDate() {
        return idCardEndDate;
    }

    public void setIdCardEndDate(String idCardEndDate) {
        this.idCardEndDate = idCardEndDate;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(error_no);
        dest.writeString(error_info);
        dest.writeString(apply_status);
        dest.writeString(user_biz_id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(apply_id);
        dest.writeString(idCard);
        dest.writeString(issued_depart);
        dest.writeString(idCardAddress);
        dest.writeString(idCardBeginDate);
        dest.writeString(idCardEndDate);
    }

    public static final Creator<UpdateIdCodeValidityEntity> CREATOR = new Creator<UpdateIdCodeValidityEntity>() {
        @Override
        public UpdateIdCodeValidityEntity createFromParcel(Parcel source) {
            UpdateIdCodeValidityEntity updateIdCodeValidityEntity = new UpdateIdCodeValidityEntity();

            updateIdCodeValidityEntity.error_no = source.readString();
            updateIdCodeValidityEntity.error_info = source.readString();
            updateIdCodeValidityEntity.apply_status = source.readString();
            updateIdCodeValidityEntity.user_biz_id = source.readString();
            updateIdCodeValidityEntity.userId = source.readString();
            updateIdCodeValidityEntity.name = source.readString();
            updateIdCodeValidityEntity.apply_id = source.readString();
            updateIdCodeValidityEntity.idCard = source.readString();
            updateIdCodeValidityEntity.issued_depart = source.readString();
            updateIdCodeValidityEntity.idCardAddress = source.readString();
            updateIdCodeValidityEntity.idCardBeginDate = source.readString();
            updateIdCodeValidityEntity.idCardEndDate = source.readString();

            return updateIdCodeValidityEntity;
        }

        @Override
        public UpdateIdCodeValidityEntity[] newArray(int size) {
            return new UpdateIdCodeValidityEntity[size];
        }
    };
}
