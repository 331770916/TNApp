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
 * Created by zhangwenbo on 2016/10/19.
 * 获取广告
 */
public class GetHomeBanderConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetHomeBanderConnect";
    private String mHttpTAG;

    public GetHomeBanderConnect(String httpTag) {
        mHttpTAG = httpTag;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {
        Map params = new HashMap();
        params.put("funcid", "400108");
//       String mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        params.put("token", "");
        Map map = new HashMap();
        map.put("state", "");
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
                //{"ErrorInfo":"查询成功","iTotalRecords":"3","data":[{"img_url":"http://123.127.198.52:8082/AD/ios/1.png","create_date":"2016-10-19 09:19:16","ad_id":"d74c6c3fd6af43c8b8268cd52374a095","ad_name":"太平洋证券","phone_type":"","product_code":"Z60002","ad_describe":"","img_base64":"","update_date":"2016-10-19 15:21:03","ad_type":"1","img_order":"1","ad_state":"3"},{"img_url":"http://123.127.198.52:8082/AD/ios/2.png","create_date":"2016-10-19 09:19:22","ad_id":"f757788d5ec14538b1239fa0021dcbe7","ad_name":"太平洋证券","phone_type":"","product_code":"Z60002","ad_describe":"","img_base64":"","update_date":"2016-10-19 15:20:55","ad_type":"1","img_order":"1","ad_state":"3"},{"img_url":"http://123.127.198.52:8082/AD/ios/3.png","create_date":"2016-10-19 09:19:28","ad_id":"73a92e98437b457385fe84c9fa4ebe8d","ad_name":"太平洋证券","phone_type":"","product_code":"Z60002","ad_describe":"","img_base64":"","update_date":"2016-10-19 15:26:53","ad_type":"1","img_order":"1","ad_state":"1"}],"code":"0","msg":"success"}
                try{
                    JSONObject res = new JSONObject(response);
                    Map<String, Object> callbackData = new HashMap<String, Object>();
                    if("0".equals(res.optString("code"))) {
                        callbackData.put("totalCount", res.optString("iTotalRecords"));
                        callbackData.put("code", res.optString("code"));
                        callbackData.put("msg", res.optString("msg"));
                        JSONArray data = res.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            List<Map<String, String>> banderDatas = new ArrayList<Map<String, String>>();
                            for (int i = 0;i < data.length();i++){
                                JSONObject json = data.optJSONObject(i);
                                Map<String, String> banderData = new HashMap<String, String>();
                                banderData.put("imgUrl", json.optString("img_url"));     //图片地址
                                banderData.put("creatDate", json.optString("create_date"));     //创建时间
                                banderData.put("AdId", json.optString("ad_id"));     //广告id
                                banderData.put("AdName", json.optString("ad_name"));     //广告名称
                                banderData.put("poneType", json.optString("phone_type"));     //广告类型
                                banderData.put("product_code", json.optString("product_code"));     //产品代码
                                banderData.put("ad_describe", json.optString("ad_describe"));     //广告描述
                                banderData.put("img_base64", json.optString("img_base64"));     //图片base64
                                banderData.put("update_date", json.optString("update_date"));     //修改时间
                                banderData.put("ad_type", json.optString("ad_type"));      //广告类型
                                banderData.put("img_order", json.optString("img_order"));      //图片排序字段
                                banderData.put("ad_state", json.optString("ad_state"));      //广告状态/1：发布，2：打回，3：待审
                                banderDatas.add(banderData);
                            }
                            callbackData.put("banderDatas", banderDatas);
                        }
                        mCallbackResult.getResult(callbackData, TAG);
                    }else{
                        callbackData.put("code", "-1");
                        callbackData.put("msg", "" + res.optString("ErrorInfo"));
                        mCallbackResult.getResult(callbackData, TAG);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Map<String, Object> callbackData = new HashMap<String, Object>();
                    callbackData.put("code", "-1");
                    callbackData.put("msg", "" + e.toString());
                    mCallbackResult.getResult(callbackData, TAG);
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String totalCount = "";
//                    if (null != responseValues.get("iTotalRecords")) {
//                        totalCount = String.valueOf(responseValues.get("iTotalRecords"));
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
//                        List<Map<String, String>> banderDatas = new ArrayList<Map<String, String>>();
//
//                        for (Object object : data) {
//                            Map<String, String> banderData = new HashMap<String, String>();
//                            Map<String, Object> items = (Map<String, Object>) object;
//
//                            String imgUrl = "";         //图片地址
//
//                            if (null != items.get("img_url")) {
//                                imgUrl = String.valueOf(items.get("img_url"));
//                            }
//
//                            String creatDate = "";//创建时间
//
//                            if (null != items.get("create_date")) {
//                                creatDate = String.valueOf(items.get("create_date"));
//                            }
//
//                            String AdId = "";   //广告id
//                            if (null != items.get("ad_id")) {
//                                AdId = String.valueOf(items.get("ad_id"));
//                            }
//
//                            String AdName = "";//广告名称
//
//                            if (null != items.get("ad_name")) {
//                                AdName = String.valueOf(items.get("ad_name"));
//                            }
//
//                            String poneType = "";//广告类型
//                            if (null != items.get("phone_type")) {
//                                poneType = String.valueOf(items.get("phone_type"));
//                            }
//
//                            String product_code = ""; //产品代码
//
//                            if (null != items.get("product_code")) {
//                                product_code = String.valueOf(items.get("product_code"));
//                            }
//
//                            String ad_describe = "";//广告描述
//
//                            if (null != items.get("ad_describe")) {
//                                ad_describe = String.valueOf(items.get("ad_describe"));
//                            }
//
//                            String img_base64 = "";//图片base64
//
//                            if (null != items.get("img_base64")) {
//                                img_base64 = String.valueOf(items.get(img_base64));
//                            }
//
//                            String update_date = "";    //修改时间
//
//                            if (null != items.get("update_date")) {
//                                update_date = String.valueOf(items.get("update_date"));
//                            }
//
//                            String ad_type = "";        //广告类型
//                            if (null != items.get("ad_type")) {
//                                ad_type = String.valueOf(items.get("ad_type"));
//                            }
//
//
//                            String img_order ="";       //图片排序字段
//                            if (null != items.get("img_order")) {
//                                img_order = String.valueOf(items.get("img_order"));
//                            }
//
//                            String ad_state = ""; //广告状态/1：发布，2：打回，3：待审
//
//                            if (null != items.get("ad_state")) {
//                                ad_state = String.valueOf(items.get("ad_state"));
//                            }
//
//                            banderData.put("imgUrl", imgUrl);     //图片地址
//                            banderData.put("creatDate", creatDate);     //创建时间
//                            banderData.put("AdId", AdId);     //广告id
//                            banderData.put("AdName", AdName);     //广告名称
//                            banderData.put("poneType", poneType);     //广告类型
//                            banderData.put("product_code", product_code);     //产品代码
//                            banderData.put("ad_describe", ad_describe);     //广告描述
//                            banderData.put("img_base64", img_base64);     //图片base64
//                            banderData.put("update_date", update_date);     //修改时间
//                            banderData.put("ad_type", ad_type);      //广告类型
//                            banderData.put("img_order", img_order);      //图片排序字段
//                            banderData.put("ad_state", ad_state);      //广告状态/1：发布，2：打回，3：待审
//
//                            banderDatas.add(banderData);
//                        }
//
//                        callbackData.put("banderDatas", banderDatas);
//                    }
//
//                    mCallbackResult.getResult(callbackData, TAG);
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
