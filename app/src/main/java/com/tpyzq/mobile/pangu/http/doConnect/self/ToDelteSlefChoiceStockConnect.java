package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/8/23.
 * 删除自选股
 */
public class ToDelteSlefChoiceStockConnect implements ICommand {

    private DeleteSelfChoiceStockConnect mDeleteSelfChoiceStockConnect;

    public ToDelteSlefChoiceStockConnect(DeleteSelfChoiceStockConnect deleteSelfChoiceStockConnect) {
        mDeleteSelfChoiceStockConnect = deleteSelfChoiceStockConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mDeleteSelfChoiceStockConnect.doConnect(callbackResult);
    }
}
