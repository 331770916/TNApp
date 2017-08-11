package com.zhy.http.okhttp.handler;

import android.content.Context;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import okhttp3.Call;

/**
 * Created by huweidong on 2017/08/11
 * email : huwwds@gmail.com
 */

public class ErrHandler extends BaseHandler {

    public ErrHandler(Context context) {
        super(context);
    }

    public void handleErr(Call call, Exception e, Callback callback, int id) {
        if (OkHttpUtils.NOT_NEED_ERR_HANDLER.equals(call.request().tag())) return;

        if (!isNetWorked()) {
            Toast.makeText(mContext, "当前无网络", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestCall requestCall = OkHttpUtils.get().url("https://www.baidu.com/").tag(OkHttpUtils.NOT_NEED_ERR_HANDLER).build();
        requestCall.buildCall(stringCallback);
        //请求百度
        OkHttpUtils.getInstance().execute(requestCall, stringCallback);
    }

    StringCallback stringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response, int id) {
            Toast.makeText(mContext, "服务器请求异常", Toast.LENGTH_SHORT).show();
        }
    };
}
