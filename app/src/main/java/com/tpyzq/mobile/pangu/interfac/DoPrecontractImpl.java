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
                iDoPrecontractResult.getDoPreconractResultError(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    iDoPrecontractResult.getDoPreconractResultError("response is null");
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("200".equals(code)){
                        String type = res.optString("type");
                        JSONObject array = res.getJSONObject("message");
                        String order = array.optString("order");
                        String force = array.optString("forceprod");
                        String isorder = array.optString("isorder");
                        iDoPrecontractResult.getDoPreconractResultSuccess(code, type, isorder, order, force);
                    }else{
                        iDoPrecontractResult.getDoPreconractResultError("网络请求异常");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    iDoPrecontractResult.getDoPreconractResultError("报文解析异常");
                }
                //{"code":"-3","msg":"funcid参数错误！"}
                //{"message":[],"code":"10203","type":"NO_DATA_ERROR"}

//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
//                try {
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
//                    if ("-3".equals(code)) {
//                        iDoPrecontractResult.getDoPreconractResultError(msg);
//                        return;
//                    } else if ("10201".equals(code)) {
//                        iDoPrecontractResult.getDoPreconractResultError("入参错误");
//                        return;
//                    } else if ("10203".equals(code)) {
//                        iDoPrecontractResult.getDoPreconractResultError("无数据");
//                        return;
//                    } else if ("500".equals(code)) {
//                        iDoPrecontractResult.getDoPreconractResultError("系统异常");
//                        return;
//                    } else if ("200".equals(code)) {
//                        String type = "";
//                        if (null != result.get("type")) {
//                            type = String.valueOf(result.get("type"));
//                        }
//
//                        Object object = result.get("message");
//                        if (object != null && object instanceof Map) {
//                            Map<String, Object> objList = (Map<String,Object>) object;
//                            String order = "";
//                            String force = "";
//                            String isorder = "";
//
//                            if (null != objList.get("order")) {
//                                order = String.valueOf(objList.get("order"));
//                            }
//
//                            if (null != objList.get("forceprod")) {
//                                force = String.valueOf(objList.get("forceprod"));
//                            }
//
//                            if (null != objList.get("isorder")) {
//                                isorder = String.valueOf(objList.get("isorder"));
//                            }
//
//                            iDoPrecontractResult.getDoPreconractResultSuccess(code, type, isorder, order, force);
//
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    iDoPrecontractResult.getDoPreconractResultError(e.toString());
//                }

            }
        });

    }
}
