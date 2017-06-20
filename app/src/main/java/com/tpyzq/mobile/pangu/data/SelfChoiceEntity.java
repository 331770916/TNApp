package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/9/13.
 */
public class SelfChoiceEntity {

    private String msg;
    private String totalcount;
    private String code;
    private List<SubSelfChoiceEntity> data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SubSelfChoiceEntity> getData() {
        return data;
    }

    public void setData(List<SubSelfChoiceEntity> data) {
        this.data = data;
    }
}
