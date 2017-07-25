package com.tpyzq.mobile.pangu.http.doConnect.login;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2017/6/22.
 */

public class ToAddUser implements ICommand {
    private AddUserConnect mAddUserConnect;

    public ToAddUser(AddUserConnect addUserConnect) {
        mAddUserConnect = addUserConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mAddUserConnect.doConnect(callbackResult);

    }
}
