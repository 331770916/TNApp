package com.tpyzq.mobile.pangu.interfac;


import java.util.Map;

/**
 * Created by zhangwenbo on 2016/11/2.
 */
public interface TransferAcountsSubject {

    void registerObserver(TransferAcountObsever observer);

    void removeObserver(TransferAcountObsever observer);

    void notifyObservers(Map<String, String> map, String tag);

}
