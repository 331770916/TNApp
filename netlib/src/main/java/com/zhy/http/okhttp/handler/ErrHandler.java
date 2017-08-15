package com.zhy.http.okhttp.handler;

import android.content.Context;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by huweidong on 2017/08/11
 * email : huwwds@gmail.com
 */

public class ErrHandler extends BaseHandler {
    HashMap<String,Long>map=new HashMap<>();
    long noNet;
    long netErr;
    long serviceErr;

    public ErrHandler(Context context) {
        super(context);
    }

    public void handleErr(Call call, Exception e, Callback callback, int id) {
        try {
            String url = call.request().url().toString();
            if (map.get(url)!=null){
                if(System.currentTimeMillis()-map.get(url)<3000){
                    map.put(url,System.currentTimeMillis());
                    return;
                }else if(System.currentTimeMillis()-map.get(url)<10000){
                    return;
                }
            }
            map.put(url,System.currentTimeMillis());
            if (OkHttpUtils.NOT_NEED_ERR_HANDLER.equals(call.request().tag())) return;

            if (!isNetWorked()) {
                if (noNet==0){
                    noNet=System.currentTimeMillis();
                }else if(System.currentTimeMillis()-noNet<3000){
                    return;
                }
                Toast.makeText(mContext, "当前无网络", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestCall requestCall = OkHttpUtils.get().url("https://www.baidu.com/").tag(OkHttpUtils.NOT_NEED_ERR_HANDLER).build();
            requestCall.buildCall(stringCallback);
            //请求百度
            OkHttpUtils.getInstance().execute(requestCall, stringCallback);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    StringCallback stringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            if (netErr==0){
                netErr=System.currentTimeMillis();
            }else if(System.currentTimeMillis()-netErr<3000){
                return;
            }
            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response, int id) {
            if (serviceErr==0){
                serviceErr=System.currentTimeMillis();
            }else if(System.currentTimeMillis()-serviceErr<3000){
                return;
            }
            Toast.makeText(mContext, "服务器请求异常", Toast.LENGTH_SHORT).show();
        }
    };
}
