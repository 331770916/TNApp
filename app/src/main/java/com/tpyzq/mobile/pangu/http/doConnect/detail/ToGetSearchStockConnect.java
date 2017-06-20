package com.tpyzq.mobile.pangu.http.doConnect.detail;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/11/30.
 * 搜索股票网络连接
 */

public class ToGetSearchStockConnect implements ICommand {

    private GetSearchStockConnect mGetSearchStockConnect;

    public ToGetSearchStockConnect (GetSearchStockConnect getSearchStockConnect) {
        mGetSearchStockConnect = getSearchStockConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetSearchStockConnect.doConnect(callbackResult);
    }
}
