package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/10/14.
 */
public class GetFundManagerConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetFundManagerConnect";

    private String mHttpTAG;
    private String mProCode;

    public GetFundManagerConnect(String httpTag, String proCode) {
        mHttpTAG = httpTag;
        mProCode = proCode;
    }


    public void doConnect(ICallbackResult callbackResult){

        mCallbackResult = callbackResult;
        request();
    }

    public void request() {

        Map params = new HashMap();
        params.put("funcid", "100216");
        params.put("token", "");

        Map[] array = new Map[1];
        Map map = new HashMap();
//        mProCode = "002397";
        map.put("securitycode", mProCode);



        array[0] = map;

        params.put("parms", map);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.ULR_MANAGEMONEY, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());

                Map<String, Object> callbackData = new HashMap<String, Object>();
                callbackData.put("code", "-1");
                callbackData.put("msg", "" + e.toString());
                mCallbackResult.getResult(callbackData, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Map<String, Object> callbackData = new HashMap<String, Object>();
                try{
                    JSONObject res = new JSONObject(response);
                    callbackData.put("totalCount", res.optString("totalcount"));
                    callbackData.put("code", res.optString("code"));
                    callbackData.put("msg", res.optString("msg"));
//                    callbackData.put("data", data);
                    JSONArray jsonArray = res.getJSONArray("data");
                    if(null != jsonArray && jsonArray.length() >0){
                        List<Map<String, String>> managerList = new ArrayList<Map<String, String>>();
                        for(int i = 0;i < jsonArray.length();i++){
                            Map<String, String> manager = new HashMap<String, String>();
                            manager.put("name", jsonArray.getJSONObject(i).optString("NAME"));
                            manager.put("background", jsonArray.getJSONObject(i).optString("BACKGROUND"));
                            manager.put("image", jsonArray.getJSONObject(i).optString("IMAGE"));
                            manager.put("managerCode", jsonArray.getJSONObject(i).optString("PERSONALCODE"));
                            managerList.add(manager);
                        }
                        callbackData.put("managerList", managerList);
                    }
                    mCallbackResult.getResult(callbackData, TAG);
                }catch (JSONException e){
                    e.printStackTrace();
                    callbackData.put("code", "-1");
                    callbackData.put("msg", "" + e.toString());
                    mCallbackResult.getResult(callbackData, TAG);
                }

//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String totalCount = "";
//                    if (null != responseValues.get("totalcount")) {
//                        totalCount = String.valueOf(responseValues.get("totalcount"));
//                    }
//
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    String msg = "";
//                    if (null != responseValues.get("msg")) {
//                        msg = String.valueOf(responseValues.get("msg"));
//                    }
//
//                    List<Object> data = new ArrayList<Object>();
//
//                    if (null != responseValues.get("data")) {
//                        data = (List<Object>) responseValues.get("data");
//                    }
//
//                    Map<String, Object> callbackData = new HashMap<String, Object>();
//
//                    callbackData.put("totalCount", totalCount);
//                    callbackData.put("code", code);
//                    callbackData.put("msg", msg);
//                    callbackData.put("data", data);
//                    if (data != null && data.size() > 0) {
//
//                        List<Map<String, String>> managerList = new ArrayList<Map<String, String>>();
//
//                        for (Object object : data) {
//
//                            Map<String, String> managers = new HashMap<String, String>();
//
//                            Map<String, Object> items = (Map<String, Object>) object;
//
//                            String name = "";
//                            if (null != items.get("NAME")) {
//                                name = String.valueOf(items.get("NAME"));
//                            }
//
//                            String background = "";
//                            if (null != items.get("BACKGROUND")) {
//                                background = String.valueOf(items.get("BACKGROUND"));
//                            }
//
//                            String image ="";
//
//                            if (null != items.get("IMAGE")) {
//                                image = String.valueOf(items.get("IMAGE"));
//                            }
//
//                            String managerCode = "";
//
//                            if(null != items.get("PERSONALCODE")) {
//                                managerCode = String.valueOf(items.get("PERSONALCODE"));
//                            }
//
//                            managers.put("name", name);
//                            managers.put("background", background);
//                            managers.put("image", image);
//                            managers.put("managerCode", managerCode);
//                            managerList.add(managers);
//                        }
//
//                        callbackData.put("managerList", managerList);
//                    }
//
//                    mCallbackResult.getResult(callbackData, TAG);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Map<String, Object> callbackData = new HashMap<String, Object>();
//                    callbackData.put("code", "-1");
//                    callbackData.put("msg", "" + e.toString());
//                    mCallbackResult.getResult(callbackData, TAG);
//                }
            }
        });
    }
}
