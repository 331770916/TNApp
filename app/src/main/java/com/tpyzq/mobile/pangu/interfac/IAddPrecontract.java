package com.tpyzq.mobile.pangu.interfac;

/**
 * Created by zhangwenbo on 2017/4/10.
 * 新增预约
 */

public interface IAddPrecontract {

    void addPrecontract(String TOKEN, String FUNCTIONCODE, String fund_account, String order_money,
                        String order_prod_code, IAddPrecontractResult iAddPrecontractResult);


}
