package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/10.
 * 热销产品
 */
public class ToGetHotProductConnect implements ICommand {

    private GetHotProductConnect mGetHotProductConnect;

    public ToGetHotProductConnect(GetHotProductConnect getHotProductConnect) {
        mGetHotProductConnect = getHotProductConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetHotProductConnect.doConnect(callbackResult);
    }
}
