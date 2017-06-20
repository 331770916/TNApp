package com.tpyzq.mobile.pangu.http.doConnect.home;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/10/17.
 * 公告连接
 */
public class ToAnnounceMentConnect implements ICommand {

    private AnnounceMentConnect mAnnounceMentConnect;
    public ToAnnounceMentConnect(AnnounceMentConnect announceMentConnect) {
        mAnnounceMentConnect = announceMentConnect;
    }

    @Override
    public void connect(ICallbackResult callbackResult) {
        mAnnounceMentConnect.doConnect(callbackResult);
    }
}
