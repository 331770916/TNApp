package com.tpyzq.mobile.pangu.http.doConnect.login;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2017/6/6.
 * 键盘
 */

public class ToKeyBoard implements ICommand {
    private KeyBoardConnect mKeyBoardConnect;

    public ToKeyBoard(KeyBoardConnect keyBoardConnect) {
        mKeyBoardConnect = keyBoardConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mKeyBoardConnect.doConnect(callbackResult);

    }
}
