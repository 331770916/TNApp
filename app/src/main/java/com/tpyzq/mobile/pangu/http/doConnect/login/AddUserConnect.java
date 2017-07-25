package com.tpyzq.mobile.pangu.http.doConnect.login;

import android.content.Context;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2017/6/22.
 * 绑定资金账号
 */

public class AddUserConnect {
    public final static String TAG = "AddUserConnect";
    private Context mContext;
    private String mHttpTag;
    private String mAccount;
    private ICallbackResult mCallbackResult;

    public AddUserConnect(Context context, String httpTag, String account) {
        mContext = context;
        mHttpTag = httpTag;
        mAccount = account;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;

        request();
    }


    private void request() {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "800104");
        map.put("token", SpUtils.getString(mContext,"mSession",""));
        map.put("parms", map1);
        map1.put("type", "1");
        map1.put("account", mAccount);
        map1.put("user_type", KeyEncryptionUtils.getInstance().Typescno());
        map1.put("user_account", UserUtil.userId);

        NetWorkUtil.getInstence().okHttpForPostString(mContext, ConstantUtil.URL_JYBD, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mCallbackResult.getResult(ConstantUtil.NETWORK_ERROR, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    String code = jsonobject.getString("code");
                    String msg = jsonobject.getString("msg");
                    if ("0".equals(code)){
                        mCallbackResult.getResult("0",TAG);
                    }else {
                        mCallbackResult.getResult(msg,TAG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


}
