package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * 作者：刘泽鹏 on 2016/9/5 18:20
 * OTC 合同签署页面传值用的 实体类
 */
public class OTC_ElectronicContractEntity implements Serializable {

    String prod_name;
    String prod_code;
    String is_sign;
    String prodta_no;
    String init_date;
    String econtract_id;
    String prodta_no_name;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProdta_no_name() {
        return prodta_no_name;
    }

    public void setProdta_no_name(String prodta_no_name) {
        this.prodta_no_name = prodta_no_name;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }

    public String getProdta_no() {
        return prodta_no;
    }

    public void setProdta_no(String prodta_no) {
        this.prodta_no = prodta_no;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getEcontract_id() {
        return econtract_id;
    }

    public void setEcontract_id(String econtract_id) {
        this.econtract_id = econtract_id;
    }
}
