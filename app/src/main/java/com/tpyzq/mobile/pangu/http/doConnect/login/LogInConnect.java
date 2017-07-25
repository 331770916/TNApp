package com.tpyzq.mobile.pangu.http.doConnect.login;

import android.content.Context;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wangqi on 2017/6/22.
 */

public class LogInConnect {
    public static String TAG = "LogInConnect";
    public static String MSG = "密码键盘解密失败";
    private Context mContext;
    private String mHttoTag;
    private ICallbackResult mICallbackResult;
    private String mUnikey;
    private String mPasswordformat;
    private String mPassword;
    private String mAccount;
    private String mIp = "";

    public LogInConnect(Context context, String httptag, String unikey, String passwordformat, String account, String password) {
        mContext = context;
        mHttoTag = httptag;
        mUnikey = unikey;
        mPasswordformat = passwordformat;
        mAccount = account;
        mPassword = password;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mICallbackResult = callbackResult;

        OkHttpUtils.get().url("http://ip.taobao.com/service/getIpInfo.php?ip=myip")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject data = json.getJSONObject("data");
                    mIp = data.getString("ip");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        request();
    }

    private void request() {
        Map map1 = new HashMap<>();
        map1.put("funcid", "300010");
        map1.put("token", SpUtils.getString(mContext, "mSession", ""));
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("LOGIN_CODE", mAccount);//610002680     101000913 //用户账号
        map2.put("USER_PWD", mPassword);                       //密码
        map2.put("PWD_TYPE", mPasswordformat); //密码类型 0：明文 1：密文
        map2.put("MOBILE", DeviceUtil.getDeviceId(CustomApplication.getContext()));                       //绑定UNIKEYID的手机号
        map2.put("UNIKEYID", mUnikey);                       //UNIKEY插件ID
        map2.put("APP_TYPE", "1");                       //手机类型 0：ios        1：android
        map2.put("TCC", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map2.put("SRRC", android.os.Build.MODEL);
        map2.put("OP_STATION", "Android" + "," + UserUtil.Mobile + "," + DeviceUtil.getDeviceId(CustomApplication.getContext()) + "," + APPInfoUtils.getVersionName(mContext) + "," + mIp);
        map2.put("APP_ID", ConstantUtil.APP_ID);
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(mContext, ConstantUtil.URL_JY, map1, new StringCallback() {
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
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    if ("0".equals(code)){
                        mICallbackResult.getResult(data,TAG);
                    }else if (MSG.equals(msg)){
                        mICallbackResult.getResult(msg,TAG);
                    }else {
                        mICallbackResult.getResult(msg,TAG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
