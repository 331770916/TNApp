package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/8/30 14:01
 * OTC申购 选择OTC产品列表的实体类
 */
public class OTC_SubscribeListBean {

    /**
     * code : 0
     * msg : 337400成功
     * data : [{"PROD_CODE":"S46680","MAX_PERRED_SHARE":"","NAV":"1.0000","PROD_NAME":"太平洋乘风1号","PRODTA_NO":"CZZ","POSITION_STR":"0000000CZZ0000S46680"},{"PROD_CODE":"SF8831","MAX_PERRED_SHARE":"","NAV":"1.0000","PROD_NAME":"太平洋彩云尊享18号","PRODTA_NO":"CZZ","POSITION_STR":"0000000CZZ0000SF8831"}]
     */

    private String code;
    private String msg;
    /**
     * PROD_CODE : S46680
     * MAX_PERRED_SHARE :
     * NAV : 1.0000
     * PROD_NAME : 太平洋乘风1号
     * PRODTA_NO : CZZ
     * POSITION_STR : 0000000CZZ0000S46680
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
        private String PROD_CODE;
        private String MAX_PERRED_SHARE;
        private String NAV;
        private String PROD_NAME;
        private String PRODTA_NO;
        private String POSITION_STR;

        public String getPROD_CODE() {
            return PROD_CODE;
        }

        public void setPROD_CODE(String PROD_CODE) {
            this.PROD_CODE = PROD_CODE;
        }

        public String getMAX_PERRED_SHARE() {
            return MAX_PERRED_SHARE;
        }

        public void setMAX_PERRED_SHARE(String MAX_PERRED_SHARE) {
            this.MAX_PERRED_SHARE = MAX_PERRED_SHARE;
        }

        public String getNAV() {
            return NAV;
        }

        public void setNAV(String NAV) {
            this.NAV = NAV;
        }

        public String getPROD_NAME() {
            return PROD_NAME;
        }

        public void setPROD_NAME(String PROD_NAME) {
            this.PROD_NAME = PROD_NAME;
        }

        public String getPRODTA_NO() {
            return PRODTA_NO;
        }

        public void setPRODTA_NO(String PRODTA_NO) {
            this.PRODTA_NO = PRODTA_NO;
        }

        public String getPOSITION_STR() {
            return POSITION_STR;
        }

        public void setPOSITION_STR(String POSITION_STR) {
            this.POSITION_STR = POSITION_STR;
        }
    }
}
