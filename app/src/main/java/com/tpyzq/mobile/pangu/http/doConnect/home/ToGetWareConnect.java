package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/17.
 * 重仓网络连接
 */
public class ToGetWareConnect implements ICommand {

    private GetWareConnect mGetWareConnect;
    public ToGetWareConnect (GetWareConnect getWareConnect) {
        mGetWareConnect = getWareConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetWareConnect.doConnect(callbackResult);
    }
}
