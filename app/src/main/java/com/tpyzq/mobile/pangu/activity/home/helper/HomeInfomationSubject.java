package com.tpyzq.mobile.pangu.activity.home.helper;

/**
 * Created by zhangwenbo on 2016/11/16.
 * 首页消息提示主题
 */
public interface HomeInfomationSubject {

    void registerObserver(HomeInfomationObsever observer);

    void removeObserver(HomeInfomationObsever observer);

    void notifyObservers(int count);
}
