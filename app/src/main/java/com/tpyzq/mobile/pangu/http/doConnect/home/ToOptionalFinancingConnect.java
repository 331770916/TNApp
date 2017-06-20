package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2017/2/10.
 * 自选理财
 */

public class ToOptionalFinancingConnect implements ICommand {

    private GetOptionalFinancingConnect mGetOptionalFinancingConnect;

    public ToOptionalFinancingConnect(GetOptionalFinancingConnect getOptionalFinancingConnect) {
        mGetOptionalFinancingConnect = getOptionalFinancingConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetOptionalFinancingConnect.doConnect(callbackResult);
    }
}
