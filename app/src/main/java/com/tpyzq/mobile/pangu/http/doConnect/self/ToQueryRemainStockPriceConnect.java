package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/9/12.
 * 查询到价提醒数据
 */
public class ToQueryRemainStockPriceConnect implements ICommand {

    private QueryRemainStockPriceConnect mQueryRemainStockPriceConnect;
    public ToQueryRemainStockPriceConnect(QueryRemainStockPriceConnect queryRemainStockPriceConnect) {
        mQueryRemainStockPriceConnect = queryRemainStockPriceConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mQueryRemainStockPriceConnect.doConnect(callbackResult);
    }
}
