package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ToLoadPdfConnect implements ICommand {

    private LoadPdfConnect mLoadPdfConnect;

    public ToLoadPdfConnect(LoadPdfConnect loadPdfConnect) {
        mLoadPdfConnect = loadPdfConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mLoadPdfConnect.doConnect(callbackResult);
    }
}
