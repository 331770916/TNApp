package com.tpyzq.mobile.pangu.interfac;

/**
 * Created by zhangwenbo on 2016/8/12.
 * listView滚动主题 用于listview到底部后通知返回了多少条数据
 */
public interface IListViewScrollSubject {

    void registerObserver(IListViewScrollObserver observer);

    void removeObserver(IListViewScrollObserver observer);

    void notifyObservers();
}
