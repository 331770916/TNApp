package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ToGetProductInfoOtcConnect  implements ICommand {

    private GetProductInfoOtcConnect mGetProductInfoConnect;

    public ToGetProductInfoOtcConnect(GetProductInfoOtcConnect getProductInfoConnect) {
        mGetProductInfoConnect = getProductInfoConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetProductInfoConnect.doConnect(callbackResult);
    }
}
