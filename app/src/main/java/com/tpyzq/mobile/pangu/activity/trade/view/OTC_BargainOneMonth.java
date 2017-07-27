package com.tpyzq.mobile.pangu.activity.trade.view;

/**
 * 作者：刘泽鹏 on 2016/9/7 13:38
 * OTC 成交查询  一月
 */
public class OTC_BargainOneMonth extends OTC_BargainToday {

    private final String TAG = OTC_BargainOneMonth.class.getSimpleName();
    @Override
    public void toConnect() {

        if (type == BARGAIN_TYPE) {
            mConnect.toCustomDayConnect(TAG, "2", "", "", this);
        } else if (type == ENTRUST_TYPE) {
            mConnect.toEnturstCustomDayConnect(TAG, "2", "", "", this);
        }
    }

}
