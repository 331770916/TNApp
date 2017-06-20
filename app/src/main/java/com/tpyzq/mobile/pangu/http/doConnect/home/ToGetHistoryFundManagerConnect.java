package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/16.
 * 获取历史历任基金经理
 */
public class ToGetHistoryFundManagerConnect implements ICommand {

    private GetHistoryFundManagerConnect mToGetHistoryFundManagerConnect;

    public ToGetHistoryFundManagerConnect (GetHistoryFundManagerConnect getHistoryFundManagerConnect) {
        mToGetHistoryFundManagerConnect = getHistoryFundManagerConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mToGetHistoryFundManagerConnect.doConnect(callbackResult);
    }
}
