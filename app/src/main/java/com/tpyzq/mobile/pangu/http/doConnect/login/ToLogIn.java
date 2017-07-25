package com.tpyzq.mobile.pangu.http.doConnect.login;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2017/6/14.
 */

public class ToLogIn implements ICommand {
    private LogInConnect mLogInConnect;

    public ToLogIn(LogInConnect logInConnect) {
        mLogInConnect = logInConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mLogInConnect.doConnect(callbackResult);

    }
}
