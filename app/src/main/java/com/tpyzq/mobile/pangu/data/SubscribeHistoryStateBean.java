package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/8/18 10:35
 */
public class SubscribeHistoryStateBean {

    /**
     * totalcount : 1
     * data : [{"SECUCODE":"21300523","STATUS":"已上市"}]
     * code : 0
     * msg : 查询成功
     */

    private String totalcount;
    private String code;
    private String msg;
    /**
     * SECUCODE : 21300523
     * STATUS : 已上市
     */

    private List<DataBean> data;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String SECUCODE;
        private String STATUS;

        public String getSECUCODE() {
            return SECUCODE;
        }

        public void setSECUCODE(String SECUCODE) {
            this.SECUCODE = SECUCODE;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }
    }
}
