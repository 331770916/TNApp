package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by 刘泽鹏 on 2016/8/13.
 * 申购历史  隐藏部分  的实体类
 */
public class SubscribeHistoryFlowBean {


    /**
     * code : 0
     * msg : (单个配号信息查询成功300384)
     * data : [{"BUSINESS_AMOUNT":"28.00","NEXT_TRADE_DATE":"20161116","REMARK":"起始配号:27932804"}]
     */

    private String code;
    private String msg;
    /**
     * BUSINESS_AMOUNT : 28.00
     * NEXT_TRADE_DATE : 20161116
     * REMARK : 起始配号:27932804
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
        private String BUSINESS_AMOUNT;
        private String NEXT_TRADE_DATE;
        private String REMARK;

        public String getBUSINESS_AMOUNT() {
            return BUSINESS_AMOUNT;
        }

        public void setBUSINESS_AMOUNT(String BUSINESS_AMOUNT) {
            this.BUSINESS_AMOUNT = BUSINESS_AMOUNT;
        }

        public String getNEXT_TRADE_DATE() {
            return NEXT_TRADE_DATE;
        }

        public void setNEXT_TRADE_DATE(String NEXT_TRADE_DATE) {
            this.NEXT_TRADE_DATE = NEXT_TRADE_DATE;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }
    }
}
