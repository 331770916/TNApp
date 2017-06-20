package com.tpyzq.mobile.pangu.http.doConnect.self;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/9/13.
 * 自选股网络连接
 */
public class ToSelfChoiceStockNews implements ICommand {

    private SelfChoiceStockNews mSelfChoiceStockNews;

    public ToSelfChoiceStockNews (SelfChoiceStockNews selfChoiceStockNews) {
        mSelfChoiceStockNews = selfChoiceStockNews;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mSelfChoiceStockNews.doConnect(callbackResult);
    }
}
