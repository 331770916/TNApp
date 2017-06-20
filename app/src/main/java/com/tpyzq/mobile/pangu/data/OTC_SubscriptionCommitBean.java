package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/8/29 21:16
 */
public class OTC_SubscriptionCommitBean {

    /**
     * code : 1
     * msg : 【221012】【交易时间非法】【prodta_no=CZZ,prod_code=S46676,curr_time=211424】
     * data : []
     */

    private String code;
    private String msg;
    private List<?> data;

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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
