package com.tpyzq.mobile.pangu.activity.trade.view;

/**
 * 作者：刘泽鹏 on 2016/9/7 13:41
 */
public class OTC_BargainThreeMonth extends OTC_BargainToday  {

    private final String TAG = OTC_BargainThreeMonth.class.getSimpleName();
    @Override
    public void toConnect() {
        if (type == BARGAIN_TYPE) {
            mConnect.toCustomDayConnect(TAG, "3", "", "", this);
        } else if (type == ENTRUST_TYPE) {
            mConnect.toEnturstCustomDayConnect(TAG, "3", "", "", this);
        }
    }

}
