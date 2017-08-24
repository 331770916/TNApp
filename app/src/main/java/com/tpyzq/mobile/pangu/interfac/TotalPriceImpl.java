package com.tpyzq.mobile.pangu.interfac;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/10.
 */

public class TotalPriceImpl implements ITotalPrice {

    private static final String TAG = TotalPriceImpl.class.getSimpleName();

    @Override
    public void getStrockPrice(String token, final ITotalPriceResult iTotalPriceResult) {

        HashMap map300608 = new HashMap();
        map300608.put("funcid", "300608");
        map300608.put("token", token);
        HashMap map300608_1 = new HashMap();
        map300608_1.put("SEC_ID", "tpyzq");
        map300608_1.put("FLAG", "true");
        map300608.put("parms", map300608_1);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map300608, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                iTotalPriceResult.getStockPriceResultError(ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    iTotalPriceResult.getStockPriceResultError(ConstantUtil.SERVICE_NO_DATA);
                    return;
                }

                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        JSONArray array = res.getJSONArray("data");
                        if(null != array && array.length() > 0){
                            JSONObject json = array.getJSONObject(0);
                            String enable_balance = json.optString("ENABLE_BALANCE");
                            String opfund_market_value = json.optString("OPFUND_MARKET_VALUE");
                            String market_value = json.optString("MARKET_VALUE");
                            String ASSET_BALANCE = json.optString("ASSET_BALANCE");
                            String OTC_MARKET_VALUE = json.optString("OTC_MARKET_VALUE");
                            double d_total = Double.valueOf(ASSET_BALANCE) - Double.valueOf(OTC_MARKET_VALUE);
                            iTotalPriceResult.getStockPriceResultSuccess(String.valueOf(d_total));
                        }
                    }else if("-6".equals(code)){
                        iTotalPriceResult.getStockPriceResultError(code);
                    }else{
                        iTotalPriceResult.getStockPriceResultError(res.optString("msg"));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    iTotalPriceResult.getStockPriceResultError(ConstantUtil.JSON_ERROR);
                }

            }
        });



    }
}
