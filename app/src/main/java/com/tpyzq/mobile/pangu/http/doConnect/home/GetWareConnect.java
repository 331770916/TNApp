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
 * Created by zhangwenbo on 2016/10/17.
 */
public class GetWareConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetWareConnect";

    private String mHttpTAG;
    private String mProCode;

    public GetWareConnect(String httpTag, String proCode) {
        mHttpTAG = httpTag;
        mProCode = proCode;
    }


    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {

        Map params = new HashMap();
        params.put("funcid", "100219");
        params.put("token", "");

        Map[] array = new Map[1];
        Map map = new HashMap();
        map.put("securitycode", mProCode);

        array[0] = map;

        params.put("parms", map);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_NEW(), params, new StringCallback() {
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
                //{"totalcount":"10","data":[{"SECUCODE":"601211","RATIOINNV":"0.006600","SECUABBR":"国泰君安"},{"SECUCODE":"300443","RATIOINNV":"0.000700","SECUABBR":"金雷风电"},{"SECUCODE":"000982","RATIOINNV":"0.000700","SECUABBR":"中银绒业"},{"SECUCODE":"300436","RATIOINNV":"0.000500","SECUABBR":"广 生 堂"},{"SECUCODE":"600998","RATIOINNV":"0.000500","SECUABBR":"九 州 通"},{"SECUCODE":"300431","RATIOINNV":"0.000500","SECUABBR":"暴风集团"},{"SECUCODE":"600529","RATIOINNV":"0.000400","SECUABBR":"山东药玻"},{"SECUCODE":"002159","RATIOINNV":"0.000400","SECUABBR":"三特索道"},{"SECUCODE":"300145","RATIOINNV":"0.000400","SECUABBR":"中金环境"},{"SECUCODE":"601898","RATIOINNV":"0.000400","SECUABBR":"中煤能源"}],"code":"0","msg":"查询成功"}
                Map<String, Object> callbackData = new HashMap<String, Object>();
                try{
                    JSONObject res = new JSONObject(response);
                    callbackData.put("totalCount", res.optString("totalcount"));
                    callbackData.put("code", res.optString("code"));
                    callbackData.put("msg", res.optString("msg"));
                    JSONArray jsonArray = res.getJSONArray("data");
                    if(null != jsonArray && jsonArray.length() >0){
                        List<Map<String, String>> wareHouseList = new ArrayList<Map<String, String>>();
                        for(int i = 0;i < jsonArray.length();i++){
                            Map<String, String> wareHouse = new HashMap<String, String>();
                            wareHouse.put("secuCode", jsonArray.getJSONObject(i).optString("SECUCODE"));
                            wareHouse.put("rationnv", jsonArray.getJSONObject(i).optString("RATIOINNV"));
                            wareHouse.put("secuabbr", jsonArray.getJSONObject(i).optString("SECUABBR"));
                            wareHouseList.add(wareHouse);
                        }
                        callbackData.put("wareHouseList", wareHouseList);
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
//
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
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
//
//                    if (data != null && data.size() > 0) {
//
//                        List<Map<String, String>> wareHouseList = new ArrayList<Map<String, String>>();
//
//                        for (Object object : data) {
//
//                            Map<String, String> wareHouses = new HashMap<String, String>();
//
//                            Map<String, Object> items = (Map<String, Object>) object;
//
//                            String secuCode = "";   //证券代码
//                            if (null != items.get("SECUCODE")) {
//                                secuCode = String.valueOf(items.get("SECUCODE"));
//                            }
//
//                            String rationnv = "";
//                            if (null != items.get("RATIOINNV")) {       // 占资产净值比例
//                                rationnv = String.valueOf(items.get("RATIOINNV"));
//                            }
//
//                            String secuabbr ="";    //证券简称
//
//                            if (null != items.get("SECUABBR")) {
//                                secuabbr = String.valueOf(items.get("SECUABBR"));
//                            }
//
//
//                            wareHouses.put("secuCode", secuCode);
//                            wareHouses.put("rationnv", rationnv);
//                            wareHouses.put("secuabbr", secuabbr);
//                            wareHouseList.add(wareHouses);
//                        }
//
//                        callbackData.put("wareHouseList", wareHouseList);
//                    }
//
//                    mCallbackResult.getResult(callbackData, TAG);
//
//
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
