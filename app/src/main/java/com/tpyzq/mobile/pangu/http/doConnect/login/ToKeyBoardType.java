package com.tpyzq.mobile.pangu.http.doConnect.login;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2017/6/9.
 */

public class ToKeyBoardType implements ICommand {
    private KeyBoardTypeConnect mKeyBoardTypeConnect;

    public ToKeyBoardType(KeyBoardTypeConnect keyBoardTypeConnect) {
        mKeyBoardTypeConnect = keyBoardTypeConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mKeyBoardTypeConnect.doConnect(callbackResult);

    }
}
