package com.tpyzq.mobile.pangu.http.doConnect.trade;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/7/25.
 * otc撤单网络连接
 */

public class OTC_RevokeConnect {

    public void toOTC_RevokeConnect(String session, String tag, final OTC_RevokeConnectListener listener) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("WITHDRAW_FLAG", "1");
        map1.put("funcid", "730204");
        map1.put("token", session);
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (listener != null) {
                    listener.cloasLoading();
                    listener.closeRefresh();
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

                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    String msg = res.optString("msg");

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

                    JSONArray data = res.optJSONArray("data");

                    if(null == data || data.length() <= 0){
                        if (listener != null) {
                            listener.connectNoData();
                        }
                        return;
                    }
                    ArrayList<Map<String, String>> datas = new ArrayList<Map<String, String>>();
                    for(int i = 0;i < data.length();i++){
                        JSONObject json = data.optJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();

                        String prod_name = json.optString("PROD_NAME");                         //产品名称
                        String prod_code = json.optString("PROD_CODE");                         //产品代码
                        String entrust_date = json.optString("ENTRUST_DATE");                   //日期
                        String entrust_time = json.optString("ENTRUST_TIME");                   //时间
                        String entrust_amount = json.optString("ENTRUST_AMOUNT");               //份额
                        String entrust_status_name = json.optString("ENTRUST_STATUS_NAME");     //状态
                        String prodta_no = json.optString("PRODTA_NO");                         //产品TA码
                        String allot_no = json.optString("ALLOT_NO");                           //申请编码
                        String business_flag = json.optString("BUSINESS_FLAG");                 //类型
                        map.put("prod_name", prod_name);
                        map.put("prod_code", prod_code);
                        map.put("business_flag", business_flag);
                        map.put("entrust_date", Helper.formateDate1(entrust_date));
                        map.put("entrust_time", Helper.formateDate(entrust_time));
                        map.put("entrust_amount", entrust_amount);
                        map.put("entrust_status_name", entrust_status_name);
                        map.put("prodta_no", prodta_no);
                        map.put("allot_no", allot_no);

                        datas.add(map);
                    }

                    if (listener != null) {
                        listener.connectSuccess(datas);
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

    public interface OTC_RevokeConnectListener{

        void connectError(String error);

        void closeRefresh();            //关闭刷新

        void cloasLoading();

        void sessionFaild();

        void connectNoData();

        void connectSuccess(ArrayList<Map<String, String>> datas);

    }
}
