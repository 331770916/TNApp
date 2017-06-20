package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/8/23.
 *
 * 增加到云端自选股
 */
public class ToAddSelfChoiceStockConnect implements ICommand {

    private AddSelfChoiceStockConnect mAddSelfChoiceStockConnect;

    public ToAddSelfChoiceStockConnect(AddSelfChoiceStockConnect addSelfChoiceStockConnect) {
        mAddSelfChoiceStockConnect = addSelfChoiceStockConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mAddSelfChoiceStockConnect.doConnect(callbackResult);
    }
}
