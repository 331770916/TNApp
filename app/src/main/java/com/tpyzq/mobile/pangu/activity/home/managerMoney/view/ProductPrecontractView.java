package com.tpyzq.mobile.pangu.activity.home.managerMoney.view;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface ProductPrecontractView {

    String getToken();

    String getFUNCTIONCODE();

    String getFund_account();

    String getOrder_money();

    String getOrder_prod_code();

    void addPrecontractError(String error);

    void addPrecontractSuccess(String code, String type, String message);
}
