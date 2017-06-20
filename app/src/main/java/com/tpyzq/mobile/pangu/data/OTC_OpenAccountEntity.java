package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * 作者：刘泽鹏 on 2016/9/2 11:23
 * OTC 开户界面  向 产品公司 列表界面 传递数据 用的实体类
 */
public class OTC_OpenAccountEntity implements Serializable {

    private String productName;
    private String productNo;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }
}
