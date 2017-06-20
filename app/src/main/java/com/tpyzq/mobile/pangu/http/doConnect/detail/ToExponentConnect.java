package com.tpyzq.mobile.pangu.http.doConnect.detail;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/26.
 */
public class ToExponentConnect implements ICommand {

    private ExponentConnect mExponentConnect;
    public ToExponentConnect(ExponentConnect exponentConnect) {
        mExponentConnect = exponentConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mExponentConnect.doConnect(callbackResult);
    }
}
