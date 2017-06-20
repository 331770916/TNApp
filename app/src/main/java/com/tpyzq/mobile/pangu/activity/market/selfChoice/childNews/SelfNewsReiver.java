package com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by zhangwenbo on 2016/11/11.
 *
 */
public class SelfNewsReiver extends BroadcastReceiver {

    private static final String TAG = "SelfNewsReiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String doAction = intent.getStringExtra("doAction");
        String stockNumber = intent.getStringExtra("stockNumber");

        if ("updateSelfChoiceData".equals(doAction)) {
            Intent i = new Intent(context, SelfNewsService.class);
            if (!TextUtils.isEmpty(stockNumber)) {
                i.putExtra("stockNumber", stockNumber);
            }
            context.startService(i);
        }

    }
}
