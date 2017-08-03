package com.tpyzq.mobile.pangu.http.doConnect.trade;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.OTC_Helper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/7/26.
 * 成交查询网络接口
 */

public class OTC_BargainConnect {

    /**
     * 今日网络连接
     * @param tag
     * @param listener
     */
    public void toToadyConnect(String tag, final OTC_BargainConnectListener listener) {
        Map map = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map.put("funcid", "300504");
        map.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.connectError(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.closeRefresh();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (listener != null) {
                            listener.sessionFaild();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (listener != null) {
                            listener.connectError(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.optJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        if (listener != null) {
                            listener.connectNoData();
                        }
                        return;
                    }

                    ArrayList<Map<String, String>> list = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        String prod_name = data.getJSONObject(i).optString("PROD_NAME");
                        String prod_code = data.getJSONObject(i).optString("PROD_CODE");
                        String prod_prop_name = data.getJSONObject(i).optString("PROD_PROP_NAME");
                        String business_balance = data.getJSONObject(i).optString("BUSINESS_BALANCE");
                        String business_amount = data.getJSONObject(i).optString("BUSINESS_AMOUNT");
                        String date = data.getJSONObject(i).optString("INIT_DATE");
                        String times = data.getJSONObject(i).optString("BUSINESS_TIME");
                        String mDate = "";
                        String mTimes = "";
                        if (TextUtils.isEmpty(date)) {
                            mDate = Helper.getInstance().getMyDateYMD(date);
                        }
                        if (!TextUtils.isEmpty(times)) {
                            mTimes = Helper.getInstance().getMyDateHMS(times);
                        }
                        map.put("tv_stockName", prod_name);
                        map.put("tv_stockCode", prod_code);
                        map.put("tv_businessName", prod_prop_name);
                        map.put("tv_businessMoney", business_balance);
                        map.put("tv_businessShare", business_amount);
                        map.put("tv_Data", mDate);
                        map.put("tv_Time", mTimes);
                        list.add(map);
                    }

                    if (listener != null) {
                        listener.connectSuccess(list);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.connectError(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }

    /**
     * 其他日期网络连接
     * @param tag
     * @param listener
     */
    public void toCustomDayConnect(String tag,  String hisType, String startDate,
                                   String endDate, final OTC_BargainConnectListener listener ) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map2.put("HIS_TYPE", hisType);
        map2.put("BEGIN_DATE", startDate);
        map2.put("END_DATE", endDate);
        map1.put("funcid","300505");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.connectError(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.closeRefresh();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (listener != null) {
                            listener.sessionFaild();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (listener != null) {
                            listener.connectError(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.optJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        if (listener != null) {
                            listener.connectNoData();
                        }
                        return;
                    }

                    ArrayList<Map<String, String>> list = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        String prod_name = data.getJSONObject(i).optString("PROD_NAME");
                        String prod_code = data.getJSONObject(i).optString("PROD_CODE");
                        String prod_prop_name = data.getJSONObject(i).optString("PROD_PROP_NAME");
                        String business_balance = data.getJSONObject(i).optString("BUSINESS_BALANCE");
                        String business_amount = data.getJSONObject(i).optString("BUSINESS_AMOUNT");
                        String date = data.getJSONObject(i).optString("BUSINESS_DATE");
                        String times = data.getJSONObject(i).optString("BUSINESS_TIME");
                        String mDate = "";
                        String mTimes = "";
                        if (TextUtils.isEmpty(date)) {
                            mDate = Helper.getInstance().getMyDateYMD(date);
                        }
                        if (!TextUtils.isEmpty(times)) {
                            mTimes = Helper.getInstance().getMyDateHMS(times);
                        }
                        map.put("tv_stockName", prod_name);
                        map.put("tv_stockCode", prod_code);
                        map.put("tv_businessName", prod_prop_name);
                        map.put("tv_businessMoney", business_balance);
                        map.put("tv_businessShare", business_amount);
                        map.put("tv_Data", mDate);
                        map.put("tv_Time", mTimes);

                        list.add(map);
                    }

                    if (listener != null) {
                        listener.connectSuccess(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.connectError(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }


    public void toEnturstToadyConnect(String tag, final OTC_BargainConnectListener listener) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map2.put("WITHDRAW_FLAG","0");
        map1.put("funcid","730204");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.connectError(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.closeRefresh();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (listener != null) {
                            listener.sessionFaild();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (listener != null) {
                            listener.connectError(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.optJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        if (listener != null) {
                            listener.connectNoData();
                        }
                        return;
                    }

                    ArrayList<Map<String, String>> list = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject json = data.getJSONObject(i);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("tv_Data", Helper.formateDate1(json.optString("ENTRUST_DATE")));
                        map.put("tv_Time", Helper.formateDate(json.optString("ENTRUST_TIME")));
                        map.put("tv_stockName", json.optString("PROD_NAME"));
                        map.put("tv_stockCode", json.optString("PROD_CODE"));
                        map.put("tv_EntrustMoney", json.optString("ENTRUST_BALANCE"));
                        map.put("tv_EntrustNumber", json.optString("ENTRUST_AMOUNT"));
                        map.put("tv_type", json.optString("BUSINESS_FLAG"));
                        map.put("tv_state",json.optString("ENTRUST_STATUS_NAME"));
                        list.add(map);
                    }

                    if (listener != null) {
                        listener.connectSuccess(list);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.connectError(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }

    public void toEnturstCustomDayConnect(String tag,  String hisType, String startDate,
                                   String endDate, final OTC_BargainConnectListener listener) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map2.put("HIS_TYPE", hisType);
        map2.put("BEGIN_DATE", startDate);
        map2.put("END_DATE", endDate);
        map1.put("funcid","300508");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.connectError(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.closeRefresh();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (listener != null) {
                            listener.sessionFaild();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (listener != null) {
                            listener.connectError(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.optJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        if (listener != null) {
                            listener.connectNoData();
                        }
                        return;
                    }

                    ArrayList<Map<String, String>> list = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject json = data.getJSONObject(i);
                        map.put("tv_Data", Helper.formateDate1(json.optString("ENTRUST_DATE")));
                        map.put("tv_Time", Helper.formateDate(json.optString("ENTRUST_TIME")));
                        map.put("tv_stockName", json.optString("PROD_NAME"));
                        map.put("tv_stockCode", json.optString("PROD_CODE"));
                        map.put("tv_EntrustMoney", json.optString("ENTRUST_BALANCE"));
                        map.put("tv_EntrustNumber", json.optString("ENTRUST_AMOUNT"));
                        map.put("tv_type", json.optString("BUSINESS_FLAG"));
                        map.put("tv_state", OTC_Helper.getEntrustStaus(json.optString("ENTRUST_STATUS")));
                        list.add(map);
                    }

                    if (listener != null) {
                        listener.connectSuccess(list);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.connectError(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }

    public interface OTC_BargainConnectListener {
        void connectError(String error);//连接错误

        void closeRefresh();            //关闭刷新

        void sessionFaild();

        void connectNoData();

        void connectSuccess(ArrayList<Map<String, String>> datas);

        void cloasLoading();
    }
}
