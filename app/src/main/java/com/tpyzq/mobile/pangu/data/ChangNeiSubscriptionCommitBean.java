package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/30 14:17
 */
public class ChangNeiSubscriptionCommitBean {

    /**
     * code : 0
     * msg : (场内基金认购成功)
     * data : [[{"INIT_DATE":"20160930","ENTRUST_NO":"415"}]]
     */

    private String code;
    private String msg;
    /**
     * INIT_DATE : 20160930
     * ENTRUST_NO : 415
     */

    private List<List<DataBean>> data;

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

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean {
        private String INIT_DATE;
        private String ENTRUST_NO;

        public String getINIT_DATE() {
            return INIT_DATE;
        }

        public void setINIT_DATE(String INIT_DATE) {
            this.INIT_DATE = INIT_DATE;
        }

        public String getENTRUST_NO() {
            return ENTRUST_NO;
        }

        public void setENTRUST_NO(String ENTRUST_NO) {
            this.ENTRUST_NO = ENTRUST_NO;
        }
    }
}
