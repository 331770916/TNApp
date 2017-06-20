package com.prolificinteractive.materialcalendarview.interfac;

/**
 * Created by Administrator on 2016/8/25.
 */
public interface ISubject {

    void registerObserver(IObserver observer);

    void removeObserver(IObserver observer);

    void notifyObservers();
}
