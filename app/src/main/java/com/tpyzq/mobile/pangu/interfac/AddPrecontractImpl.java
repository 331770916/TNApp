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

public class AddPrecontractImpl implements IAddPrecontract {

    private static final String TAG = AddPrecontractImpl.class.getSimpleName();

    @Override
    public void addPrecontract(String TOKEN, String FUNCTIONCODE, String fund_account, String order_money, String order_prod_code, final IAddPrecontractResult iAddPrecontractResult) {
        Map params = new HashMap();
        params.put("TOKEN", TOKEN);
        params.put("FUNCTIONCODE",FUNCTIONCODE);
        Map map = new HashMap();
        map.put("fund_account", fund_account);
        map.put("order_money", order_money);
        map.put("order_prod_code", order_prod_code);
        params.put("PARAMS", map);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_UPDATE, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                iAddPrecontractResult.getAddPreconractResultError(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    iAddPrecontractResult.getAddPreconractResultError("reponse is null");
                }

                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("200".equals(code)){
                        iAddPrecontractResult.getAddPreconractResultSuccess(code, "", res.optString("msg"));
                    }else{
                        iAddPrecontractResult.getAddPreconractResultError("网络异常");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    iAddPrecontractResult.getAddPreconractResultError(e.toString());
                }

//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
//                try {
//
//                    Map<String, Object> result = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
//                    String code = "";
//                    if (result.get("code") != null) {
//                        code = String.valueOf(result.get("code"));
//                    }
//
//                    String msg = "";
//                    if (result.get("msg") != null) {
//                        msg = String.valueOf(result.get("msg"));
//                    }
//
//                    if ("-1".equals(code)) {
//                        iAddPrecontractResult.getAddPreconractResultError(msg);
//                        return;
//                    } else if ("10201".equals(code)) {
//                        iAddPrecontractResult.getAddPreconractResultError("入参错误");
//                        return;
//                    } else if ("10203".equals(code)) {
//                        iAddPrecontractResult.getAddPreconractResultError("没有数据");
//                        return;
//                    } else if ("500".equals(code)) {
//                        iAddPrecontractResult.getAddPreconractResultError("系统异常");
//                        return;
//                    } else if ("200".equals(code)) {
//                        iAddPrecontractResult.getAddPreconractResultSuccess(code, "", msg);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    iAddPrecontractResult.getAddPreconractResultError(e.toString());
//                }

            }
        });
    }
}
