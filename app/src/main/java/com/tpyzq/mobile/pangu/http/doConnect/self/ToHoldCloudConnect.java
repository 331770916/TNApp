package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/9/19.
 * 持仓网络连接
 */
public class ToHoldCloudConnect implements ICommand {

    private HoldCloudConnect mHoldCloudConnect;

    public ToHoldCloudConnect(HoldCloudConnect holdCloudConnect) {
        mHoldCloudConnect = holdCloudConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mHoldCloudConnect.doConnect(callbackResult);
    }
}
