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
 * Created by zhangwenbo on 2016/9/13.
 */
public class ArraySelfChoiceStockConnect {
    private ICallbackResult mCallbackResult;
    private static final String TAG = "ArraySelfChoiceStockConnect";
    private String mHttpTAG;
    private String mToken;
    private String mUserId;
    private String [] mStrPosition;
    private String [] mStrStockNumbers;

    public ArraySelfChoiceStockConnect(String httpTag, String token, String userId, String[] strPosition, String[] strStockNumbers) {
        mHttpTAG = httpTag;
        mToken = token;
        mUserId = userId;
        mStrPosition = strPosition;
        mStrStockNumbers = strStockNumbers;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {
        Map map1 = new HashMap();
        Map map2 = new HashMap();

        String strCode = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mStrStockNumbers.length; i++) {

            if (i == mStrStockNumbers.length - 1) {
                sb.append(mStrStockNumbers[i]);
            } else {
                sb.append(mStrStockNumbers[i]).append(",");
            }

        }

        strCode = sb.toString();

        String strPosition = "";

        StringBuilder sb1 = new StringBuilder();

        for (int i = 0; i < mStrPosition.length; i++) {
            if (i == mStrStockNumbers.length - 1) {
                sb1.append(mStrPosition[i]);
            } else {
                sb1.append(mStrPosition[i]).append(",");
            }
        }

        strPosition = sb1.toString();


        map1.put("funcid", "800115");
        map1.put("token", mToken);

        map2.put("USERID", mUserId);
        map2.put("SORTRULE", strPosition);
        map2.put("CODE", strCode);
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_HQ_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", "-1");
                mCallbackResult.getResult(map, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String code = jsonObject.getString("code");

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("code", code);
                    map.put("msg", msg);
                    mCallbackResult.getResult(map, TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("code", "-1");
                    mCallbackResult.getResult(map, TAG);
                }
            }
        });
    }

}
