package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/9/12.
 * 添加一条自选股到价提醒
 */
public class ToAddRemainStockPriceConnect implements ICommand {

    private AddRemainStockPriceConnect mAddRemainStockPriceConnect;

    public ToAddRemainStockPriceConnect(AddRemainStockPriceConnect addRemainStockPriceConnect) {
        mAddRemainStockPriceConnect = addRemainStockPriceConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mAddRemainStockPriceConnect.doConnect(callbackResult);
    }

}
