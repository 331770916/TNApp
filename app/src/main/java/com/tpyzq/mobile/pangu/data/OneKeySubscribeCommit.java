package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/8/25 11:58
 */
public class OneKeySubscribeCommit {

    /**
     * code : 1
     * msg : (300543申购失败，原因：【120158】【校验委托买入单位,卖出单位失败】【stock_bs=1,p_entrust_amount=999997999.00,store_unit=1,buy_unit=500,sell_unit=1,entrust_prop=3】
     )
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
