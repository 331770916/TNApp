package com.tpyzq.mobile.pangu.http.doConnect.trade;

import com.tpyzq.mobile.pangu.data.OtcShareEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/7/24.
 * otc份额网络连接
 */

public class OTC_ShareConnect {

    public void toOtcShareConnect (String tag, String session, final OTC_ShareConnectInterface otcShareInterface) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300501");
        map1.put("token", session);
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (otcShareInterface != null) {
                    otcShareInterface.cloasLoading();
                    otcShareInterface.connectError(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (otcShareInterface != null) {
                    otcShareInterface.cloasLoading();
                    otcShareInterface.closeRefresh();
                }
                //{"code":"0","msg":"(OTC份额查询成功)","data":[{"OTC_MARKET_VALUE":"0.00","OTC_LIST":[],"OTC_INCOME":"0.00"}]}
                //{"code":"0","msg":"(OTC份额查询成功)","data":[{"OTC_MARKET_VALUE":"50000.00","OTC_LIST":[{"FROZEN_AMOUNT":"0","PROD_TYPE":"5","PROD_CODE":"SU1036","ENABLE_AMOUNT":"50000.00","INCOME_BALANCE":"0","NAV":"1.0000","ASSET_PRICE":"0","PROD_NAME":"太平洋证券太牛犇1号","CURRENT_AMOUNT":"50000.00","BUY_DATE":"20170329","MARKET_VALUE":"50000.00","COST_PRICE":"1.000"}],"OTC_INCOME":"0.00"}]}
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");

                    if ("-6".equals(code)) {
                        if (otcShareInterface != null) {
                            otcShareInterface.sessionFaild();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (otcShareInterface != null) {
                            otcShareInterface.connectError(msg);
                        }
                        return;
                    }
                    ArrayList<OtcShareEntity> datas = new ArrayList<OtcShareEntity>();
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject1 = data.getJSONObject(i);
                        String otc_market_value = jsonObject1.optString("OTC_MARKET_VALUE"); //市值
                        JSONArray otc_list = jsonObject1.optJSONArray("OTC_LIST");

                        if (otc_list.length() == 0) {
                            if (otcShareInterface != null) {
                                otcShareInterface.connectNoData();
                            }
                            return;
                        }

                        OtcShareEntity otcShareIntentBean0 = new OtcShareEntity();
                        otcShareIntentBean0.setMarket_value(otc_market_value);//市值
                        datas.add(otcShareIntentBean0);

                        for (int j = 0; j < otc_list.length(); j++) {
                            OtcShareEntity otcShareIntentBean = new OtcShareEntity();
                            JSONObject item = otc_list.getJSONObject(j);
                            String current_amount = item.optString("CURRENT_AMOUNT");
                            String prod_name = item.optString("PROD_NAME");
                            String prod_code = item.optString("PROD_CODE");
                            String buy_date = item.optString("BUY_DATE");
                            otcShareIntentBean.setCurrent_amount(current_amount);//份额
                            otcShareIntentBean.setProd_name(prod_name);         //股票名称
                            otcShareIntentBean.setProd_code(prod_code);         //股票代码
                            otcShareIntentBean.setBuy_date(buy_date);           //购入日期
                            otcShareIntentBean.setUnFold(false);
                            datas.add(otcShareIntentBean);
                        }

                    }
                    if (otcShareInterface != null) {
                        otcShareInterface.connectSuccess(datas);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (otcShareInterface != null) {
                        otcShareInterface.connectError(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }



    public interface OTC_ShareConnectInterface {

        void connectError(String error);//连接错误

        void closeRefresh();            //关闭刷新

        void sessionFaild();

        void connectNoData();

        void connectSuccess(ArrayList<OtcShareEntity> datas);

        void cloasLoading();
    }

}
