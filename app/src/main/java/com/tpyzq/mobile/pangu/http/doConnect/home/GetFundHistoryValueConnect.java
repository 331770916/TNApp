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
 * 获取历史净值连接
 */
public class GetFundHistoryValueConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetFundHistoryValueConnect";

    private String mHttpTAG;
    private String mProCode;
    private String mStartDate;
    private String mEndDate;
    private String mPageindex;
    private String mPagesize;
    private String mIspaged;

    /**
     *
     * @param httpTag
     * @param proCode       基金代码
     * @param startDate     开始日期(yyyy-mm-dd)
     * @param endDate       结束日期(yyyy-mm-dd)
     * @param pageindex     第几页（整型数字）
     * @param pagesize      每页条数（整型数字）
     * @param ispaged       是否分页 0分页，1不分页，默认分页
     */
    public GetFundHistoryValueConnect(String httpTag, String proCode, String startDate, String endDate, String pageindex, String pagesize, String ispaged) {
        mHttpTAG = httpTag;
        mProCode = proCode;
        mStartDate = startDate;
        mEndDate = endDate;
        mPageindex = pageindex;
        mPagesize = pagesize;
        mIspaged = ispaged;
    }


    public void doConnect(ICallbackResult callbackResult){

        mCallbackResult = callbackResult;
        request();
    }

    public void request() {

        Map params = new HashMap();
        params.put("funcid", "100218");
        params.put("token", "");

        Map[] array = new Map[1];
        Map map = new HashMap();
        map.put("securitycode", mProCode);
        map.put("startdate", mStartDate);   //开始日期(yyyy-mm-dd)
        map.put("enddate", mEndDate);       //结束日期(yyyy-mm-dd)
        map.put("pageindex", mPageindex);   //第几页（整型数字）
        map.put("pagesize", mPagesize);    //每页条数（整型数字）
        map.put("ispaged", mIspaged);     //是否分页 0分页，1不分页，默认分页
        map.put("type", "1");
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
                Map<String, Object> callbackData = new HashMap<String, Object>();
                try{
                    JSONObject res = new JSONObject(response);
                    callbackData.put("totalCount", res.optString("totalcount"));
                    callbackData.put("code", res.optString("code"));
                    callbackData.put("msg", res.optString("msg"));
                    callbackData.put("fundtypecode", res.optString("fundtypecode"));
//                    callbackData.put("data", res.getJSONArray("data").toString());
                    JSONArray jsonArray = res.getJSONArray("data");
                    if(null != jsonArray && jsonArray.length() >0){
                        List<Map<String, String>> chartDatas = new ArrayList<Map<String, String>>();
                        for(int i = 0;i < jsonArray.length();i++){
                            Map<String, String> chartData = new HashMap<String, String>();
                            chartData.put("unitnv", jsonArray.getJSONObject(i).optString("UNITNV"));
                            chartData.put("accumulatedunitnv", jsonArray.getJSONObject(i).optString("ACCUMULATEDUNITNV"));
                            chartData.put("dailyProfit", jsonArray.getJSONObject(i).optString("DAILYPROFIT"));
                            chartData.put("latestweeklyyield", jsonArray.getJSONObject(i).optString("LATESTWEEKLYYIELD"));
                            chartData.put("endDate", jsonArray.getJSONObject(i).optString("ENDDATE"));
                            chartDatas.add(chartData);
                        }
                        callbackData.put("chartDatasList", chartDatas);
                    }
                    mCallbackResult.getResult(callbackData, TAG);
                }catch (JSONException e){
                    e.printStackTrace();
                    callbackData.put("code", "-1");
                    callbackData.put("msg", "" + e.toString());
                    mCallbackResult.getResult(callbackData, TAG);
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
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
//                    String fundtypecode = "";
//
//                    if (null != responseValues.get("fundtypecode")) {
//                        fundtypecode = String.valueOf(responseValues.get("fundtypecode"));
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
//                    callbackData.put("fundtypecode", fundtypecode);
//                    callbackData.put("data", data);
//
//                    if (data != null && data.size() > 0) {
//
//                        List<Map<String, String>> chartDatas = new ArrayList<Map<String, String>>();
//
//                        for (Object object : data) {
//
//                            Map<String, String> chartData = new HashMap<String, String>();
//
//                            Map<String, Object> items = (Map<String, Object>) object;
//
//                            String unitnv = ""; //单位净值
//                            if (null != items.get("UNITNV")) {
//                                unitnv = String.valueOf(items.get("UNITNV"));
//                            }
//
//                            String accumulatedunitnv = "";  //单位累计净值
//                            if (null != items.get("ACCUMULATEDUNITNV")) {
//                                accumulatedunitnv = String.valueOf(items.get("ACCUMULATEDUNITNV"));
//                            }
//
//                            String dailyProfit ="";     //每万份基金单位当日收益(元)
//                            if (null != items.get("DAILYPROFIT")) {
//                                dailyProfit = String.valueOf(items.get("DAILYPROFIT"));
//                            }
//
//                            String latestweeklyyield = "";  //最近7日折算年收益率
//                            if(null != items.get("LATESTWEEKLYYIELD")) {
//                                latestweeklyyield = String.valueOf(items.get("LATESTWEEKLYYIELD"));
//                            }
//
//                            String endDate = "";
//                            if (null != items.get("ENDDATE")) {
//                                endDate = String.valueOf(items.get("ENDDATE"));
//                                endDate = Helper.formateDate1(endDate);
//                            }
//
//                            chartData.put("unitnv", unitnv);
//                            chartData.put("accumulatedunitnv", accumulatedunitnv);
//                            chartData.put("dailyProfit", dailyProfit);
//                            chartData.put("latestweeklyyield", latestweeklyyield);
//                            chartData.put("endDate", endDate);
//                            chartDatas.add(chartData);
//                        }
//
//                        callbackData.put("chartDatasList", chartDatas);
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
