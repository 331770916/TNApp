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
 * Created by zhangwenbo on 2016/9/12.
 * 添加一条自选股到价提醒
 */
public class AddRemainStockPriceConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "AddRemainStockPriceConnect";
    private String mHttpTAG;
    private String mToken;
    private String mUserId;
    private String mCode;
    private String mMaxImum;
    private String mMinImum;
    private String mStockName;
    private String mRisesizqconfine;
    private String mFallsizeconfine;

    /**
     *
     * @param httpTag
     * @param token             用户标识
     * @param userId
     * @param code              股票代码
     * @param maxImum           最高限
     * @param minImum           最低限
     * @param risesizqconfine   涨跌幅高限
     * @param fallsizeconfine   涨跌幅低限
     */
    public AddRemainStockPriceConnect(String httpTag, String token, String userId, String stockName, String code, String maxImum, String minImum, String risesizqconfine, String fallsizeconfine) {
        mHttpTAG = httpTag;
        mToken = token;
        mUserId = userId;
        mStockName = stockName;
        mCode = code;
        mMaxImum = maxImum;
        mMinImum = minImum;
        mRisesizqconfine = risesizqconfine;
        mFallsizeconfine = fallsizeconfine;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }


    private void request() {

        Map[] array = new Map[1];

        Map map1 = new HashMap();
        map1.put("funcid", "800112");
        map1.put("token", mToken);

        Map map2 = new HashMap();
        map2.put("USERID", mUserId);
        map2.put("CODE", mCode);
        map2.put("NAME", mStockName);
        map2.put("MAXIMUM", mMaxImum);
        map2.put("MINIMUM", mMinImum);
        map2.put("RISESIZQCONFINE", mRisesizqconfine);
        map2.put("FALLSIZECONFINE", mFallsizeconfine);

        array[0] = map2;
        Map map3 = new HashMap();
        map3.put("dataType", "");
        map3.put("dataArray", array);

        map1.put("parms", map3);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_HQ_HS(), map1, new StringCallback() {
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

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    mCallbackResult.getResult(msg, TAG);

                } catch (Exception e) {
                 e.printStackTrace();
                    mCallbackResult.getResult("" + e.toString(), TAG);
                }

            }
        });
    }


}
