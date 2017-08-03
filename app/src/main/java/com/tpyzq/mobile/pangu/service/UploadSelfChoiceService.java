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
 * Created by zhangwenbo on 2016/8/23.
 * 上传自选股后台服务， 需要现有账号
 *
 * 当用户注册账号后，进行后台同步，需要批量上传
 */
public class UploadSelfChoiceService extends Service {

    private static final String TAG = "UploadSelfChoiceService";

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
