package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/8/30 17:14
 */
public class OTC_SubscribeCommitBean {

    /**
     * code : 0
     * msg : 337412成功
     * data : [{"SERIAL_NO":"7","INIT_DATE":"20160830","ALLOT_NO":"147600053201608300000007"}]
     */

    private String code;
    private String msg;
    /**
     * SERIAL_NO : 7
     * INIT_DATE : 20160830
     * ALLOT_NO : 147600053201608300000007
     */

    private List<DataBean> data;

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
        private String SERIAL_NO;
        private String INIT_DATE;
        private String ALLOT_NO;

        public String getSERIAL_NO() {
            return SERIAL_NO;
        }

        public void setSERIAL_NO(String SERIAL_NO) {
            this.SERIAL_NO = SERIAL_NO;
        }

        public String getINIT_DATE() {
            return INIT_DATE;
        }

        public void setINIT_DATE(String INIT_DATE) {
            this.INIT_DATE = INIT_DATE;
        }

        public String getALLOT_NO() {
            return ALLOT_NO;
        }

        public void setALLOT_NO(String ALLOT_NO) {
            this.ALLOT_NO = ALLOT_NO;
        }
    }
}
