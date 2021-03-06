package com.tpyzq.mobile.pangu.activity.trade.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.activity.trade.stock.TraChooseBreedActivity;
import com.tpyzq.mobile.pangu.data.StockInfoBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.TransitionUtils.string2double4;
import static com.tpyzq.mobile.pangu.util.TransitionUtils.string2doubleS3;

/**
 * anthor:Created by tianchen on 2017/4/5.
 * email:963181974@qq.com
 */

public class TraChooseBreedActivityPresenter {
    TraChooseBreedActivity activity;

    public TraChooseBreedActivityPresenter(TraChooseBreedActivity activity) {
        this.activity = activity;
    }

    public TraChooseBreedActivity getActivity(){
        return activity;
    }
/**
    public void getTraBreed() {

        Map<String, String> map001 = new HashMap<>();
        Object[] object = new Object[1];
        map001.put("FUNCTIONCODE", "HQING001");
        Map map2 = new HashMap();
        map2.put("asc", "1");
        map2.put("startNo", "0");
        map2.put("number", "30");
        map2.put("flag", "1");
        map2.put("type", "10");
        object[0] = map2;
        map001.put("PARAMS", Arrays.toString(object));
        NetWorkUtil.getInstence().okHttpForGet("", ConstantUtil.URL, map001, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(e.toString());
            }
            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                    String code = jsonObject.getString("code");
                    List<StockInfoBean> hu_data = new ArrayList<>();
                    List<StockInfoBean> sz_data = new ArrayList<>();
                    if ("0".equals(code)) {
                        JSONArray jsdata = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsdata.length(); i++) {
                            StockInfoBean stockInfo = new StockInfoBean();
                            stockInfo.stockCode = jsdata.getJSONArray(i).getString(0);
                            String beforePrice = jsdata.getJSONArray(i).getString(1);
                            String currentPrice = jsdata.getJSONArray(i).getString(2);
                            String stockName = jsdata.getJSONArray(i).getString(3);
                            if (!TextUtils.isEmpty(stockName)){
                                stockName = stockName.trim();
                                stockInfo.stockName1 = stockName;
                                if (Helper.isDecimal(stockName.substring(stockName.length()-3))){
                                    stockInfo.stockName2 = Integer.valueOf(stockName.substring(stockName.length()-3));
                                }else {
                                    stockInfo.stockName2 = 0;
                                }
                            }
                            String turnoverRate = jsdata.getJSONArray(i).getString(4);
                            String endPrice = jsdata.getJSONArray(i).getString(5);
                            String TTM = jsdata.getJSONArray(i).getString(6);
                            if (stockInfo.stockCode.startsWith("1")) {
                                if (Helper.isDecimal(currentPrice)) {
                                    double price = Double.valueOf(currentPrice);
                                    stockInfo.yearIncome = string2doubleS3(price+"") + "%";
                                    stockInfo.wYuanIncome = string2double4(price / 360d * 100d  + "");
                                    stockInfo.tenwYuanDayIncome = string2doubleS3(price / 360d * 1000d * stockInfo.stockName2+ "");
                                }
                                hu_data.add(stockInfo);
                            } else {
                                if (Helper.isDecimal(currentPrice)) {
                                    double price = Double.valueOf(currentPrice);
                                    stockInfo.yearIncome = string2doubleS3(price+"") + "%";
                                    stockInfo.wYuanIncome = string2double4(price / 365d * 100d + "");
                                    stockInfo.tenwYuanDayIncome = string2doubleS3(price / 365d * 10d * stockInfo.stockName2+ "");
                                }
                                sz_data.add(stockInfo);
                            }
                        }
                        Collections.sort(hu_data);
                        Collections.sort(sz_data);
                        activity.setHuPager(hu_data);
                        activity.setSzPager(sz_data);
                    } else {
                        CentreToast.showText(activity, "数据异常: " + code);
                    }
                } catch (JSONException e) {
                    CentreToast.showText(activity, "数据解析异常");
                    e.printStackTrace();
                }
            }
        });
    }
 */
public void getTraBreed() {
    Map<String, String> map001 = new HashMap<>();
    Object[] object = new Object[1];
    map001.put("FUNCTIONCODE", "HQING019");
    map001.put("TOKEN", "");
    Map map2 = new HashMap();
    map2.put("market", "");
    object[0] = map2;
    String json = new Gson().toJson(object);
    map001.put("PARAMS", json);
    NetWorkUtil.getInstence().okHttpForGet("", ConstantUtil.getURL_HQ_HHN(), map001, new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e(e.toString());
        }

        @Override
        public void onResponse(String response, int id) {
            if (TextUtils.isEmpty(response)) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
                String code = jsonObject.getString("code");
                String type = jsonObject.getString("type");
                List<StockInfoBean> hu_data = new ArrayList<>();
                List<StockInfoBean> sz_data = new ArrayList<>();
                if ("200".equals(code)) {
                    JSONArray jsdata = jsonObject.getJSONArray("message");
                    for (int i = 0; i < jsdata.length(); i++) {
                        StockInfoBean stockInfo = new StockInfoBean();
                        stockInfo.stockCode = jsdata.getJSONObject(i).optString("mcode");
                        String earnings = jsdata.getJSONObject(i).optString("earnings");
                        String w_eamings = jsdata.getJSONObject(i).optString("w_eamings");
                        String sw_eamings = jsdata.getJSONObject(i).optString("sw_eamings");
                        String stockName = jsdata.getJSONObject(i).optString("sname");
                        String market = jsdata.getJSONObject(i).optString("market");
                        String dj_time = jsdata.getJSONObject(i).optString("dj_time");//可取日期
                        String dq_time = jsdata.getJSONObject(i).optString("dq_time");//可用日期
                        String sq_time = jsdata.getJSONObject(i).optString("sq_time");//行情日期
                        String occupied_days = jsdata.getJSONObject(i).optString("occupied_days");

                        if (!TextUtils.isEmpty(stockName)){
                            stockName = stockName.trim();
                            stockInfo.stockName1 = stockName;
                            if (Helper.isDecimal(stockName.substring(stockName.length()-3))){
                                stockInfo.stockName2 = Integer.valueOf(stockName.substring(stockName.length()-3));
                            }else {
                                stockInfo.stockName2 = 0;
                            }
                        }
                        stockInfo.yearIncome = string2doubleS3(earnings+"") + "%";
                        stockInfo.wYuanIncome = string2double4(w_eamings);
                        stockInfo.tenwYuanDayIncome = string2doubleS3(sw_eamings);
                        stockInfo.dj_time = dj_time;
                        stockInfo.dq_time = dq_time;
                        stockInfo.sq_time = sq_time;
                        stockInfo.occupied_days = occupied_days;
                        if ("sh".equals(market)) {
                            hu_data.add(stockInfo);
                        } else if ("sz".equals(market)){
                            sz_data.add(stockInfo);
                        }
                    }
                    Collections.sort(hu_data);
                    Collections.sort(sz_data);
                    activity.setHuPager(hu_data);
                    activity.setSzPager(sz_data);
                } else {
//                    CentreToast.showText(activity, "数据异常: " + type);
                }
            } catch (JSONException e) {
                CentreToast.showText(activity, "数据解析异常");
                e.printStackTrace();
            }

        }
    });
}
}
