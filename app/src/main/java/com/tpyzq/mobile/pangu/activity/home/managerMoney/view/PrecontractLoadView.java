package com.tpyzq.mobile.pangu.activity.home.managerMoney.view;

/**
 * Created by zhangwenbo on 2017/4/10.
 */

public interface PrecontractLoadView {


    String getToken();

    String getFUNCTIONCODE();

    String getProd_code();

    String getFund_account();

    void queryPrecontractInfoError(String error);

    void queryPrecontractInfoSuccess(String code, String type, String message, String oreder, String force);

    void getTotalPrice(String totalPrice);

    void getProductStauts(String stauts);
}
