package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/17.
 *
 * 获取历史净值连接
 */
public class ToGetFundHistoryValueConnect implements ICommand {

    private GetFundHistoryValueConnect mGetFundHistoryValueConnect;

    public ToGetFundHistoryValueConnect(GetFundHistoryValueConnect getFundHistoryValueConnect) {
        mGetFundHistoryValueConnect = getFundHistoryValueConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetFundHistoryValueConnect.doConnect(callbackResult);
    }
}
