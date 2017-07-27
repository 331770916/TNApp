package com.tpyzq.mobile.pangu.activity.trade.view;


/**
 * 作者：刘泽鹏 on 2016/9/7 13:34
 * OTC 成交查询 一周
 */
public class OTC_BargainOneWeek extends OTC_BargainToday {

    private final String TAG = OTC_BargainOneWeek.class.getSimpleName();
    @Override
    public void toConnect() {


        if (type == BARGAIN_TYPE) {
            mConnect.toCustomDayConnect(TAG, "1", "", "", this);
        } else if (type == ENTRUST_TYPE) {
            mConnect.toEnturstCustomDayConnect(TAG, "1", "", "", this);
        }

    }

}
