package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/19.
 */

public class LoadPdfConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "LoadPdfConnect";
    private String mHttpTAG;
    private String mToken;
    private String mProCode;
    private String mProType;
    private ArrayList<CleverManamgerMoneyEntity> mCleverManamgerMoneyEntitys;

    public LoadPdfConnect(String httpTag, String token, String proCode, String proType, ArrayList<CleverManamgerMoneyEntity> cleverManamgerMoneyEntities) {
        mHttpTAG = httpTag;
        mToken = token;
        mProCode = proCode;
        mProType = proType;
        mCleverManamgerMoneyEntitys = cleverManamgerMoneyEntities;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {
        Map params = new HashMap();
        params.put("FUNCTIONCODE", "HQTNG107");
        params.put("TOKEN", mToken);

        final Map map = new HashMap();
        map.put("prod_code", mProCode);
        map.put("prod_kind_type", mProType);
        params.put("PARAMS", map);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.URL_NEW_ZX, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                mCallbackResult.getResult("网络请求异常", TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    mCallbackResult.getResult("网络请求异常", TAG);
                    return;
                }
                LogUtil.e(TAG,"--------res:"+response);
                //{"message":{"id":"2160","prod_kind_type":"3","creat_time":"2017-04-19 08:38:47.0","enclosure_list":[{"fileurl":"http://106.120.112.246:8082/prodfile/1492167112213附件5：123.pdf","filename":"附件5：123.pdf"},{"fileurl":"http://106.120.112.246:8082/prodfile/1492562185310.pdf","filename":"雪球内容API接口文档1.1.pdf"}],"prod_code":"cc002"},"code":"200","type":"success"}
                try{
                    JSONObject res = new JSONObject(response);
                    if("200".equals(res.optString("code"))){
                        JSONObject jsonObject = res.optJSONObject("message");
                        JSONArray array = jsonObject.getJSONArray("enclosure_list");
                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                        if(null != array && array.length() > 0){
                            for(int i = 0; i < array.length();i++){
                                JSONObject json = array.getJSONObject(i);
                                Map map = new HashMap();
                                map.put("name", json.optString("filename"));
                                map.put("url", json.optString("fileurl"));
                                maps.add(map);
                            }
                        }
                        mCleverManamgerMoneyEntitys.get(0).setOtcProaoclDate(maps);
                        mCallbackResult.getResult(mCleverManamgerMoneyEntitys, TAG);
                    }else{
                        mCallbackResult.getResult("请求PDF数据异常", TAG);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    mCallbackResult.getResult("报文数据解析异常", TAG);
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
//                    String  code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    if (!"200".equals(code)) {
//                        mCallbackResult.getResult(mCleverManamgerMoneyEntitys, TAG);
//                        return;
//                    }
//
//                    Object object = responseValues.get("message");
//
//                    if (object != null && object instanceof Map) {
//                        Map<String, Object> objectMap = (Map<String, Object>) object;
//                        if (objectMap == null || !objectMap.containsKey("enclosure_list")) {
//                            mCallbackResult.getResult(mCleverManamgerMoneyEntitys, TAG);
//                            return;
//                        }
//
//                        Object object1 = objectMap.get("enclosure_list");
//
//                        List<Map<String, Object>> pdfs = (List<Map<String, Object>>)object1;
//
//                        if (pdfs == null || pdfs.size() <= 0) {
//                            mCallbackResult.getResult(mCleverManamgerMoneyEntitys, TAG);
//                            return;
//                        }
//
//                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
//                        for (int i = 0; i < pdfs.size(); i++) {
//                            Map<String, Object> pdf = pdfs.get(i);
//                            Map map = new HashMap();
//                            String name = "";
//                            if (null != pdf.get("filename")) {
//                                name = String.valueOf(pdf.get("filename"));
//                            }
//
//                            String url = "";
//                            if (null != pdf.get("fileurl")) {
//                                url = String.valueOf(pdf.get("fileurl"));
//                            }
//
//                            map.put("name", name);
//                            map.put("url",url);
//                            maps.add(map);
//                        }
//
//                        mCleverManamgerMoneyEntitys.get(0).setOtcProaoclDate(maps);
//                        mCallbackResult.getResult(mCleverManamgerMoneyEntitys, TAG);
//                    } else {
//                        mCallbackResult.getResult(mCleverManamgerMoneyEntitys, TAG);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    mCallbackResult.getResult(mCleverManamgerMoneyEntitys, TAG);
//                }
            }
        });
    }
}
