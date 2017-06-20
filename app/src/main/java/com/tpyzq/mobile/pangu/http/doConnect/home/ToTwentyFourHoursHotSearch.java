package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by wangqi on 2016/12/30.
 * 热搜网络链接
 */

public class ToTwentyFourHoursHotSearch implements ICommand {

    private TwentyFourHoursHotSearchConnect mToTwentyFourHoursHotSearch;

    public ToTwentyFourHoursHotSearch(TwentyFourHoursHotSearchConnect twentyFourHoursHotSearchConnect) {
        mToTwentyFourHoursHotSearch = twentyFourHoursHotSearchConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mToTwentyFourHoursHotSearch.doConnect(callbackResult);

    }
}
