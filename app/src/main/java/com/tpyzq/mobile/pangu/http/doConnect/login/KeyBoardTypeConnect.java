package com.tpyzq.mobile.pangu.http.doConnect.login;

import android.content.Context;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2017/6/9.
 */

public class KeyBoardTypeConnect {
    public static String TAG = "KeyBoardTypeConnect";
    private Context mContext;
    private String mHttpTag;
    private ICallbackResult mCallbackResult;

    public KeyBoardTypeConnect(Context context, String httpTag) {
        mContext = context;
        mHttpTag = httpTag;

    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        HashMap map = new HashMap();
        map.put("funcid", "400102");
        NetWorkUtil.getInstence().okHttpForPostString(mContext, ConstantUtil.URL_JYBD, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(mHttpTag, e.toString());
                mCallbackResult.getResult(false, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");

                    if ("0".equals(code)) {
                        String status = jsonObject.getString("status");
                        if ("1".equals(status)) {
                            mCallbackResult.getResult(true, TAG);
                        } else {
                            mCallbackResult.getResult(false, TAG);
                        }
                    } else {
                        mCallbackResult.getResult(false, TAG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
