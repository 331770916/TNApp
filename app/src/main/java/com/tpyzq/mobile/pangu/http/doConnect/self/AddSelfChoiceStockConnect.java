package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/23.
 *
 * 增加到云端自选股
 */
public class AddSelfChoiceStockConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "AddSelfChoiceStockConnect";
    private String mHttpTAG;
    private String mToken;
    private String mAccount;
    private String mStockCode;
    private String mUserId;
    private String mStockName;
    private String mPrice;

    public AddSelfChoiceStockConnect(String httpTag, String token, String account, String stockCode, String userId, String stockName, String price) {
        mHttpTAG = httpTag;
        mToken = token;
        mAccount = account;
        mStockCode = stockCode;
        mUserId = userId;
        mStockName = stockName;
        mPrice = price;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request(mToken, mAccount, mStockCode, mUserId, mStockName, mPrice);
    }

    /**
     *
     * @param token
     * @param account
     * @param stockCode    stockCode 格式如:  210035,210026,210027
     * @param userId
     * @param stockName    stockName 格式如:  210035,210026,210027
     * @param price         price 格式如:  210035,210026,210027
     */
    private void request(String token, String account, String stockCode, String userId, String stockName, String price) {

        Map params = new HashMap();
        Map[] array = new Map[1];
        if (stockCode.contains(",")) {
            String [] stockCodes = stockCode.split(",");
            String [] stockNames = stockName.split(",");
            String [] prices = price.split(",");
            array = new Map[stockCodes.length];

            for (int i = 0; i < stockCodes.length; i++) {
                Map map3 = new HashMap();
                map3.put("BINDING", "");
                map3.put("CAPITALACCOUNT", account);     //资金帐号,与用户帐号必须传一个)
                map3.put("CODE", stockCodes[i]);               // CODE:股票代码（Y）
                map3.put("USERID", userId);             //用户账号ID（Y）
                map3.put("NAME",stockNames[i]);
                map3.put("PRICE", prices[i]);
                map3.put("SOURCE", "A002");
                map3.put("STATUS", "1");
                map3.put("TYPE", KeyEncryptionUtils.Typescno());//KeyEncryptionUtils.Typescno();
                array[i] = map3;
            }

        } else {
            Map map3 = new HashMap();
            map3.put("BINDING", "");
            map3.put("CAPITALACCOUNT", account);     //资金帐号,与用户帐号必须传一个)
            map3.put("CODE", stockCode);               // CODE:股票代码（Y）
            map3.put("USERID", userId);             //用户账号ID（Y）
            map3.put("NAME",stockName);
            map3.put("PRICE", price);
            map3.put("SOURCE", "A002");
            map3.put("STATUS", "1");
            map3.put("TYPE", "1");
            array[0] = map3;
        }


        Map map2 = new HashMap();
        map2.put("dataType", "");           //数据处理标识
        map2.put("dataArray", array);

        params.put("funcid", "800116");
        params.put("token", token);
        params.put("parms", map2);

        Gson gson = new Gson();
        String json = gson.toJson(params);
        LogHelper.i(TAG, "" + json);

        String tempUrl = "http://192.168.0.205:8080/HTTPServer/servlet";
        //FileUtil.URL_SELFCHOICENET
        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_NEW(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());

                mCallbackResult.getResult("" + e.toString(), TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                mCallbackResult.getResult(response, TAG);

            }
        });
    }

}
