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
 * Created by zhangwenbo on 2016/10/16.
 * 获取历史历任基金经理
 */
public class GetHistoryFundManagerConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetHistoryFundManagerConnect";

    private String mHttpTAG;
    private String mProCode;

    public GetHistoryFundManagerConnect(String httpTag, String proCode) {
        mHttpTAG = httpTag;
        mProCode = proCode;
    }


    public void doConnect(ICallbackResult callbackResult){

        mCallbackResult = callbackResult;
        request();
    }

    public void request() {

        Map params = new HashMap();
        params.put("funcid", "100217");
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
                //{"totalcount":"4","data":[{"ACCESSIONDATE":"2012-02-15","NAME":"朱红裕","RZQJ":"153","PERFORMANCE":"0.023121","DIMISSIONDATE":"2012-07-17"},{"ACCESSIONDATE":"2009-11-03","NAME":"黄顺祥","RZQJ":"987","PERFORMANCE":"-0.064480","DIMISSIONDATE":"2012-07-17"},{"ACCESSIONDATE":"2007-07-17","NAME":"康晓云","RZQJ":"1317","PERFORMANCE":"-0.005564","DIMISSIONDATE":"2011-02-23"},{"ACCESSIONDATE":"2007-07-17","NAME":"芮颖","RZQJ":"319","PERFORMANCE":"-0.005566","DIMISSIONDATE":"2008-05-31"}],"code":"0","msg":"查询成功"}
                Map<String, Object> callbackData = new HashMap<String, Object>();
                try{
                    JSONObject res = new JSONObject(response);
                    callbackData.put("totalCount", res.optString("totalcount"));
                    callbackData.put("code", res.optString("code"));
                    callbackData.put("msg", res.optString("msg"));
                    JSONArray jsonArray = res.getJSONArray("data");
                    if(null != jsonArray && jsonArray.length() >0){
                        List<Map<String, String>> managerList = new ArrayList<Map<String, String>>();
                        for(int i = 0;i < jsonArray.length();i++){
                            Map<String, String> manager = new HashMap<String, String>();
                            manager.put("startDate", jsonArray.getJSONObject(i).optString("ACCESSIONDATE"));
                            manager.put("name", jsonArray.getJSONObject(i).optString("NAME"));
                            manager.put("atDate", jsonArray.getJSONObject(i).optString("RZQJ"));
                            manager.put("performance", jsonArray.getJSONObject(i).optString("PERFORMANCE"));
                            manager.put("dimissionDate", jsonArray.getJSONObject(i).optString("DIMISSIONDATE"));
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
//
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
//                try {
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
//                        List<Map<String, String>> managerList = new ArrayList<Map<String, String>>();
//
//                        for (Object object : data) {
//
//                            Map<String, String> managers = new HashMap<String, String>();
//
//                            Map<String, Object> items = (Map<String, Object>) object;
//
//                            //起始日期
//                            String startDate = "";
//
//                            if (null != items.get("ACCESSIONDATE")) {
//                                startDate = String.valueOf(items.get("ACCESSIONDATE"));
//                            }
//
//                            //基金经理姓名
//                            String name = "";
//                            if (null != items.get("NAME")) {
//                                name = String.valueOf(items.get("NAME"));
//                            }
//
//                            //任职期间（天数）
//                            String atDate = "";
//                            if (null != items.get("RZQJ")) {
//                                atDate = String.valueOf(items.get("RZQJ"));
//                            }
//
//                            //任职期间基金净增长率
//                            String performance ="";
//
//                            if (null != items.get("PERFORMANCE")) {
//                                performance = String.valueOf(items.get("PERFORMANCE"));
//                            }
//
//                            String dimissionDate = "";
//
//                            if(null != items.get("DIMISSIONDATE")) {
//                                dimissionDate = String.valueOf(items.get("DIMISSIONDATE"));
//                            }
//
//                            managers.put("startDate", startDate);
//                            managers.put("name", name);
//                            managers.put("atDate", atDate);
//                            managers.put("performance", performance);
//                            managers.put("dimissionDate", dimissionDate);
//                            managerList.add(managers);
//                        }
//
//                        callbackData.put("managerList", managerList);
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
