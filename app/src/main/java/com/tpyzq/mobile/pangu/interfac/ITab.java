package com.tpyzq.mobile.pangu.interfac;

/**
 * Created by zhangwenbo on 2016/8/24.
 */
public interface ITab {

    void registerObserver(ITabDataObserver observer);

    void removeObserver(ITabDataObserver observer);

    void notifyObservers();
}
