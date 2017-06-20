package com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.http.doConnect.self.SelfChoiceStockNews;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToSelfChoiceStockNews;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;

/**
 * Created by zhangwenbo on 2016/11/11.
 */
public class SelfNewsService extends Service implements ICallbackResult {

    private static final String TAG = "SelfNewsService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String stockNumber = "";
        if (intent != null) {
            stockNumber = intent.getStringExtra("stockNumber");
        }

        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
        simpleRemoteControl.setCommand(new ToSelfChoiceStockNews(new SelfChoiceStockNews(TAG,"0", stockNumber)));
        simpleRemoteControl.startConnect();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void getResult(Object result, String tag) {
        LogHelper.e(TAG, "------------------------------------我走了" );
    }
}
