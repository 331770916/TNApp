package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwnebo on 2016/8/23.
 *
 * 删除云端自选股
 */
public class DeleteSelfChoiceStockConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "DeleteSelfChoiceStockConnect";
    private String mHttpTAG;

    private String mToken;
    private String mAccount;
    private String mStockCode;
    private String mUserId;

    /**
     * @param httpTag
     * @param token
     * @param account       资金帐号,与用户帐号必须传一个)
     * @param stockCode     CODE:股票代码（Y）
     * @param userId        用户账号ID（Y）
     */
    public DeleteSelfChoiceStockConnect(String httpTag, String token, String account, String stockCode, String userId) {
        mHttpTAG = httpTag;

        mToken = token;
        mAccount = account;
        mStockCode = stockCode;
        mUserId = userId;
    }

    /**
     * @param callbackResult
     */
    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request(mToken, mAccount, mStockCode, mUserId);
    }

    /**
     * @param token
     * @param account
     * @param stockCode stockCode    stockCode 格式如:  210035,210026,210027
     * @param userId
     */
    private void request(final String token, String account, String stockCode, String userId) {

        //
        Map params = new HashMap();
        Map[] array = new Map[1];

        if (stockCode.contains(",")) {
            String [] stockCodes = stockCode.split(",");
            array = new Map[stockCodes.length];

            for (int i = 0; i < stockCodes.length; i++) {
                Map map3 = new HashMap();
                map3.put("CAPITALACCOUNT", account);     //资金帐号,与用户帐号必须传一个)
                map3.put("CODE", stockCodes[i]);               // CODE:股票代码（Y）
                map3.put("USERID", userId);             //用户账号ID（Y）
                array[i] = map3;
            }

        } else {
            Map map3 = new HashMap();
            map3.put("CAPITALACCOUNT", account);     //资金帐号,与用户帐号必须传一个)
            map3.put("CODE", stockCode);               // CODE:股票代码（Y）
            map3.put("USERID", userId);             //用户账号ID（Y）

            array[0] = map3;
        }

        Map map2 = new HashMap();
        map2.put("dataType", "");           //数据处理标识
        map2.put("dataArray", array);


        params.put("funcid", "800113");
        params.put("token", token);
        params.put("parms", map2);


        Gson gson = new Gson();
        String json = gson.toJson(params);
        LogHelper.e(TAG, json);

        String tempUrl = "http://192.168.0.205:8080/HTTPServer/servlet";
        //FileUtil.URL_SELFCHOICENET
        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_HQ_HS(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mCallbackResult.getResult("失败" + e.toString(), TAG);
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String code = jsonObject.getString("code");
                    String totalcount = jsonObject.getString("totalcount");
                    if (!TextUtils.isEmpty(code) && !code.equals("0")) {
                        Helper.getInstance().showToast(CustomApplication.getContext(), "" + msg);
                    } else if (!TextUtils.isEmpty(code) && "0".equals(code) && !TextUtils.isEmpty(totalcount) && "0".equals(totalcount)) {
                        mCallbackResult.getResult("删除失败", TAG);
                    }
                    mCallbackResult.getResult(response, TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallbackResult.getResult("网络请求失败" + e.toString(), TAG);
                }

            }
        });
    }
}
