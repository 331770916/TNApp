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
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/10/17.
 * 公告连接
 */
public class AnnounceMentConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "AnnounceMentConnect";

    private String mHttpTAG;
    private String mProCode;

    public AnnounceMentConnect(String httpTag, String proCode) {
        mHttpTAG = httpTag;
        mProCode = proCode;
    }


    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {
        Map params = new HashMap();
        params.put("funcid", "900103");
        params.put("token", "");

        Map map = new HashMap();
        map.put("code", mProCode);
        map.put("channelid", "000100010003");

        params.put("parms", map);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.ULR_MANAGEMONEY, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());

                Map<String, Object> callbackData = new HashMap<String, Object>();
                callbackData.put("code", "-1");
                callbackData.put("msg", "网络异常");
                mCallbackResult.getResult(callbackData, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Map<String, Object> callbackData = new HashMap<String, Object>();
                //{"PageCount":"1","bytes":28,"CurCount":"6","totalCount":"6","data":[["143373","平安银行2015年半年度报告","","","平安银行股份有限公司","tpyinfotran","infotrans","深圳证券交易所","0","0","0","0","(S:21000001)","","0","0","0","492803418491","0","2015-08-14 00:00:00","2016-09-24 17:56:17","2016-09-24 17:56:17","2016-10-17 19:40:36","","","","-1","-1","-1","","","jy_492803418491_6","10","0","0"],["171063","平安银行2015年第三季度报告全文","","","平安银行股份有限公司","tpyinfotran","infotrans","深圳证券交易所","0","0","0","0","(S:21000001)","","0","0","0","498863493839","0","2015-10-23 00:00:00","2016-09-24 21:22:43","2016-09-24 21:22:43","2016-10-17 15:39:05","","","","-1","-1","-1","","","jy_498863493839_23","2","0","0"],["177296","平安银行2015年年度报告","","","平安银行股份有限公司","tpyinfotran","infotrans","深圳证券交易所","0","0","0","0","(S:21000001)","","0","0","0","510865815199","1","2016-03-10 00:00:00","2016-10-13 16:50:17","2016-09-24 22:19:14","2016-10-13 16:50:17","","","","-1","-1","-1","","","","0","0","0"],["177298","平安银行2015年年度报告摘要","","","平安银行股份有限公司","tpyinfotran","infotrans","深圳证券交易所","0","0","0","0","(S:21000001)","","0","0","0","510865815201","1","2016-03-10 00:00:00","2016-10-13 16:50:19","2016-09-24 22:19:14","2016-10-13 16:50:19","","","","-1","-1","-1","","","","0","0","0"],["178200","平安银行非公开发行优先股募集说明书","","","平安银行股份有限公司","tpyinfotran","infotrans","深圳证券交易所","0","0","0","0","(S:21000001)","","0","0","0","511346423428","0","2016-03-15 00:00:00","2016-09-24 22:27:23","2016-09-24 22:27:23","2016-09-24 22:27:23","","","","-1","-1","-1","","","jy_511346423428_130","0","0","0"],["197028","平安银行2016年第一季度报告全文","","","平安银行股份有限公司","tpyinfotran","infotrans","深圳证券交易所","0","0","0","0","(S:21000001)","","0","0","0","514488022814","1","2016-04-21 00:00:00","2016-09-28 15:35:21","2016-09-25 01:30:28","2016-09-28 15:35:21","","","","-1","-1","-1","","","","0","0","0"]],"code":"0"}
                try{
                    JSONObject res = new JSONObject(response);

                    if("0".equals(res.optString("code"))){
                        callbackData.put("totalCount", res.optString("totalCount"));
                        callbackData.put("code", res.optString("code"));
                        JSONArray data = res.getJSONArray("data");
                        if(null != data && data.length() > 0){
                            ArrayList<ArrayList<String>> itemsData = new ArrayList<ArrayList<String>>();
                            for(int i = 0 ;i < data.length();i++){
                                ArrayList<String> subItems = new ArrayList<String>();
                                JSONArray json = data.getJSONArray(i);
                                for(int j = 0;j < json.length();j++){
                                    subItems.add(json.getString(j).toString());
                                }
                                itemsData.add(subItems);
                            }
                            callbackData.put("data", itemsData);
                        }
                        mCallbackResult.getResult(callbackData, TAG);
                    }else{
                        callbackData.put("code", "-1");
                        callbackData.put("msg", "网络不好，请求数据失败");
                        mCallbackResult.getResult(callbackData, TAG);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    callbackData.put("code", "-1");
                    callbackData.put("msg", "报文解析失败" );
                    mCallbackResult.getResult(callbackData, TAG);
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String totalCount = "";
//                    if (null != responseValues.get("totalCount")) {
//                        totalCount = String.valueOf(responseValues.get("totalCount"));
//                    }
//
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
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
//
//
//                    if (data != null && data.size() > 0) {
//
//                        ArrayList<ArrayList<String>> itemsData = new ArrayList<ArrayList<String>>();
//                        for (Object object : data) {
//                            List<Object> items = (List)object;
//                            ArrayList<String> subItems = new ArrayList<String>();
//                            for (Object subObj : items) {
//                                String str = String.valueOf(subObj);
//                                subItems.add(str);
//                            }
//                            itemsData.add(subItems);
//                        }
//                        callbackData.put("data", itemsData);
//                    }
//
//                    mCallbackResult.getResult(callbackData, TAG);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


            }
        });
    }

}
