package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by Administrator on 2016/9/13.
 * 自选股编辑排序网络云同步
 */
public class ToArraySelfChoiceStockConnect implements ICommand {

    private ArraySelfChoiceStockConnect mArraySelfChoiceStockConnect;

    public ToArraySelfChoiceStockConnect(ArraySelfChoiceStockConnect arraySelfChoiceStockConnect){
        mArraySelfChoiceStockConnect = arraySelfChoiceStockConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mArraySelfChoiceStockConnect.doConnect(callbackResult);
    }
}
