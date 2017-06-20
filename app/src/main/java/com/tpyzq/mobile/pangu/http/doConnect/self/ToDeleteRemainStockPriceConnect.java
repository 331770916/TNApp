package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/9/12.
 * 删除到价提醒
 */
public class ToDeleteRemainStockPriceConnect implements ICommand {

    private DeleteRemainStockPriceConnect mDeleteRemainStockPriceConnect;

    public ToDeleteRemainStockPriceConnect(DeleteRemainStockPriceConnect deleteRemainStockPriceConnect) {
        mDeleteRemainStockPriceConnect = deleteRemainStockPriceConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mDeleteRemainStockPriceConnect.doConnect(callbackResult);
    }
}
