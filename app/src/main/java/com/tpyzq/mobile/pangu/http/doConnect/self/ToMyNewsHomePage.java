package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2016/10/24.
 * 我的消息
 */
public class ToMyNewsHomePage implements ICommand {


    private GetMyNomePage mGetMyNomePage;

    public ToMyNewsHomePage(GetMyNomePage getGetMyNomePage) {
        mGetMyNomePage = getGetMyNomePage;
    }


    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetMyNomePage.doConnect(callbackResult);
    }
}
