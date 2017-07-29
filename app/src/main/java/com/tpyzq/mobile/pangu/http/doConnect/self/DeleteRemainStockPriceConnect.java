package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/12.
 */
public class DeleteRemainStockPriceConnect  {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "DeleteRemainStockPriceConnect";
    private String mHttpTAG;
    private String mToken;
    private String mUserId;
    private String mStockNumber;
    private String mContentFlag;


    public DeleteRemainStockPriceConnect(String httpTag, String token, String userId, String stockNumber, String content) {
        mHttpTAG = httpTag;
        mToken = token;
        mUserId = userId;
        mStockNumber = stockNumber;
        mContentFlag = content;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        Map map1 = new HashMap();
        map1.put("funcid", "800120");
        map1.put("token", mToken);

        Map map2 = new HashMap();
        map2.put("USERID", mUserId);
        map2.put("CODE", mStockNumber);

        if (mContentFlag.contains("股价涨")) {
            map2.put("ASTRICT", "1");
        } else if (mContentFlag.contains("股价跌")) {
            map2.put("ASTRICT", "2");
        } else if (mContentFlag.contains("日涨幅")) {
            map2.put("ASTRICT", "3");
        } else if (mContentFlag.contains("日跌幅")) {
            map2.put("ASTRICT", "4");
        }


        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_NEW(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
            //{"msg":"设置成功","totalcount":2,"code":0}
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    mCallbackResult.getResult(msg, TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
