package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/9/18.
 * 获取首页资讯信息网络连接
 */
public class ToGetHomeInfoConnect implements ICommand {

    private GetHomeInfoConnect mGetHomeInfoConnect;

    public ToGetHomeInfoConnect(GetHomeInfoConnect getHomeInfoConnect) {
        mGetHomeInfoConnect = getHomeInfoConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mGetHomeInfoConnect.doConnect(callbackResult);
    }
}
