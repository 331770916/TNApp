package com.tpyzq.mobile.pangu.http.doConnect.login;

import android.content.Context;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2017/6/14.
 * 交易登录图片验证码
 */

public class SecurityCodeConnect {
    public static String TAG = "SecurityCodeConnect";
    public static String MSG = "验证码网络请求失败";
    private Context mContext;
    private String mHttoTag;
    private ICallbackResult mICallbackResult;

    public SecurityCodeConnect(Context context, String httptag) {
        mContext = context;
        mHttoTag = httptag;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mICallbackResult = callbackResult;
        request();
    }

    private void request() {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("FUNCTIONCODE ", "HQING002");
        map.put("parms", map1);
        map1.put("limit", "");
        map1.put("offset ", "");
        map1.put("tag ", "");
        NetWorkUtil.getInstence().okHttpForPostString(mContext, ConstantUtil.getURL_JY_UI(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mICallbackResult.getResult(ConstantUtil.NETWORK_ERROR, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("CODE");
                    String verificationcode = jsonObject.getString("VERIFICATIONCODE");
                    String verificationimage = jsonObject.getString("VERIFICATIONIMAGE");
                    if ("0".equals(code)) {
                        if (verificationcode != null && verificationcode != "") {
                            mICallbackResult.getResult(jsonObject, TAG);
                        }
                    } else {
                        mICallbackResult.getResult(MSG, TAG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
