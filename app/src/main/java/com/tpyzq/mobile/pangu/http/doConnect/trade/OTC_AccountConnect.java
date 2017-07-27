package com.tpyzq.mobile.pangu.http.doConnect.trade;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/7/27.
 * otc账户
 */

public class OTC_AccountConnect {

    public void toOtcAccountConnect(String tag, String session, final OTC_AccountConnectListener listener) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG","true");
        map2.put("SEC_ID","tpyzq");
        map1.put("funcid","300506");
        map1.put("token",session);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.URL_JY, map1, new StringCallback() {
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

                    ArrayList<Map<String, String>> list = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        Map<String,String> map = new HashMap<String, String>();
                        JSONObject json = data.optJSONObject(i);
                        map.put("open_date",json.optString("OPEN_DATE"));
                        map.put("secum_account",json.optString("SECUM_ACCOUNT"));
                        map.put("prodta_no",json.optString("PRODTA_NO"));
                        map.put("prodholder_status",json.optString("STATUS_NAME"));
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

    public interface OTC_AccountConnectListener {
        void connectError(String error);//连接错误

        void closeRefresh();            //关闭刷新

        void sessionFaild();

        void connectNoData();

        void connectSuccess(ArrayList<Map<String, String>> datas);

        void cloasLoading();
    }

}
