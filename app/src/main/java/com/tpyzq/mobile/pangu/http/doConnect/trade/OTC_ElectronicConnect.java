package com.tpyzq.mobile.pangu.http.doConnect.trade;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/7/27.
 */

public class OTC_ElectronicConnect {

    public void toElectronicConnect(String tag, final OTC_ElectronicConnectListener listener) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG","true");
        map2.put("SEC_ID","tpyzq");
        map1.put("funcid","300511");
        map1.put("token",SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
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

                    ArrayList<OTC_ElectronicContractEntity> datas = new ArrayList<OTC_ElectronicContractEntity>();
                    for (int i = 0; i < data.length(); i++) {
                        OTC_ElectronicContractEntity intentBean = new OTC_ElectronicContractEntity();
                        JSONObject json = data.optJSONObject(i);
                        intentBean.setProd_name(json.optString("PROD_NAME"));
                        intentBean.setProd_code(json.optString("PROD_CODE"));
                        intentBean.setIs_sign(json.optString("IS_SIGN"));
                        intentBean.setProdta_no(json.optString("PRODTA_NO"));
                        intentBean.setInit_date(json.optString("INIT_DATE"));
                        intentBean.setEcontract_id(json.optString("ECONTRACT_ID"));
                        intentBean.setProdta_no_name(json.optString("PRODTA_NO_NAME"));
                        intentBean.setStatus(json.optString("ECONTRACT_STATUS"));
                        datas.add(intentBean);
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

    public interface OTC_ElectronicConnectListener {
        void connectError(String error);//连接错误

        void closeRefresh();            //关闭刷新

        void sessionFaild();

        void connectNoData();

        void connectSuccess(ArrayList<OTC_ElectronicContractEntity> datas);

        void cloasLoading();
    }

}
