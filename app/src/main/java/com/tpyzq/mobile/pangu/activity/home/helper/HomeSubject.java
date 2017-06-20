package com.tpyzq.mobile.pangu.activity.home.helper;

/**
 * Created by zhangwenbo on 2016/10/31.
 */
public interface HomeSubject {

    public void registerObserver(HomeObsever observer);

    public void removeObserver(HomeObsever observer);

    public void notifyObservers(int position, String tag);

}
