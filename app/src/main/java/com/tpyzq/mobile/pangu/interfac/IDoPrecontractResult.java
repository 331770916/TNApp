package com.tpyzq.mobile.pangu.interfac;

/**
 * Created by zhangwenbo on 2017/4/10.
 */

public interface IDoPrecontractResult {

     void getDoPreconractResultSuccess(String code, String type, String isOrder, String oreder, String force);

     void getDoPreconractResultError(String error);

}
