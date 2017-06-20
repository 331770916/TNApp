package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/14.
 * 获得基金经理接口
 */
public class ToGetFundManagerConnect implements ICommand {

    private GetFundManagerConnect mGetFundManagerConnect;

    public ToGetFundManagerConnect(GetFundManagerConnect getFundManagerConnect) {
        mGetFundManagerConnect = getFundManagerConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetFundManagerConnect.doConnect(callbackResult);
    }
}
