package com.tpyzq.mobile.pangu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 同步自选股服务， 需要先有账号
 *
 * 当用户，在没有网络情况下删除数据自选股， 有网时，再进入自选股界面应该和网络同步
 */
public class DownloadSelfChoiceService extends Service {

    private static final String TAG = "DownloadSelfChoiceService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_HHN(), "", new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
