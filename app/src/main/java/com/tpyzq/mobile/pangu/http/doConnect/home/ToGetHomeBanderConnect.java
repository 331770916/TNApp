package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/19.
 * 获取广告
 */
public class ToGetHomeBanderConnect implements ICommand {

    private GetHomeBanderConnect mGetHomeBanderConnect;
    public ToGetHomeBanderConnect(GetHomeBanderConnect getHomeBanderConnect) {
        mGetHomeBanderConnect = getHomeBanderConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetHomeBanderConnect.doConnect(callbackResult);
    }
}
