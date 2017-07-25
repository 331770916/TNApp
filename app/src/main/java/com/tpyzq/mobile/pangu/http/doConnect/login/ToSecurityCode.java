package com.tpyzq.mobile.pangu.http.doConnect.login;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2017/6/14.
 */

public class ToSecurityCode implements ICommand {
    private SecurityCodeConnect mSecurityCodeConnect;

    public ToSecurityCode(SecurityCodeConnect securityCodeConnect) {
        mSecurityCodeConnect = securityCodeConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mSecurityCodeConnect.doConnect(callbackResult);

    }
}
