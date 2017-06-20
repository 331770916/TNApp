package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/8/23.
 * 查询网络云端的自选股
 */
public class ToQuerySelfChoiceStockConnect implements ICommand {

    private QuerySelfChoiceStockConnect mQuerySelfChoiceStockConnect;
    public ToQuerySelfChoiceStockConnect(QuerySelfChoiceStockConnect querySelfChoiceStockConnect) {
        mQuerySelfChoiceStockConnect = querySelfChoiceStockConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mQuerySelfChoiceStockConnect.doConnect(callbackResult);
    }
}
