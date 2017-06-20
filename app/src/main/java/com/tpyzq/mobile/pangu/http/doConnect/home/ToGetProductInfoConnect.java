package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/10.
 * 获取产品信息网络连接
 */
public class ToGetProductInfoConnect implements ICommand {

    private GetProductInfoConnect mGetProductInfoConnect;

    public ToGetProductInfoConnect(GetProductInfoConnect getProductInfoConnect) {
        mGetProductInfoConnect = getProductInfoConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetProductInfoConnect.doConnect(callbackResult);
    }
}
