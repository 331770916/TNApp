package com.tpyzq.mobile.pangu.interfac;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/4/10.
 */

public class DoPrecontractImpl implements IDoPrecontract {

    private static final String TAG = DoPrecontractImpl.class.getSimpleName();

    @Override
    public void doPrecontract(String TOKEN, String FUNCTIONCODE, String prod_code, String fund_account, final IDoPrecontractResult iDoPrecontractResult) {
        Map params = new HashMap();
        params.put("TOKEN", TOKEN);
        params.put("FUNCTIO" +
                "NCODE",FUNCTIONCODE);
        Map map = new HashMap();
        map.put("prod_code", prod_code);
        map.put("fund_account", fund_account);
        params.put("PARAMS", map);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_HQ_WA(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                iDoPrecontractResult.getDoPreconractResultError(ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    iDoPrecontractResult.getDoPreconractResultError(ConstantUtil.SERVICE_NO_DATA);
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    String type = res.optString("type");
                    if("200".equals(code)){
                        JSONObject array = res.getJSONObject("message");
                        String order = array.optString("order");
                        String force = array.optString("forceprod");
                        String isorder = array.optString("isorder");
                        iDoPrecontractResult.getDoPreconractResultSuccess(code, type, isorder, order, force);
                    }else{
                        iDoPrecontractResult.getDoPreconractResultError(type);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    iDoPrecontractResult.getDoPreconractResultError(ConstantUtil.JSON_ERROR);
                }
            }
        });

    }
}
