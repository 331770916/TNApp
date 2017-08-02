package com.tpyzq.mobile.pangu.http.doConnect.trade;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/7/18.
 * 基金信息网络连接
 */

public class FundInoConnect {

    /**
     * 获取基金产品
     * @param company_name 产品名称
     * @param pageIndex    第几页
     * @param session
     * @param tag
     */
    public void fundQueryConnect(String fundType, String company_name, int pageIndex, String session, String tag, final FundInfoConnectListener listener) {

        HashMap map300441 = new HashMap();
        map300441.put("funcid", "300441");
        map300441.put("token", session);
        HashMap map300441_1 = new HashMap();
        map300441_1.put("SEC_ID", "tpyzq");
        map300441_1.put("FUND_TYPE", fundType);
//        map300441_1.put("FUND_CODE", "");
        map300441_1.put("FUND_COMPANY_NAME", company_name);
        map300441_1.put("PAGE_INDEX", pageIndex);
        map300441_1.put("PAGE_SIZE", "20");
        map300441_1.put("FLAG", "true");
        map300441.put("parms", map300441_1);
        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.URL_JY, map300441, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (listener != null) {
                    listener.onRefreshComplete();
                    listener.showError(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {

                if (listener != null) {
                    listener.onRefreshComplete();
                }

                if (TextUtils.isEmpty(response)) {
                    if (listener != null) {
                        listener.showError(ConstantUtil.SERVICE_NO_DATA);
                    }
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String code = object.getString("code");

                    if ("-6".equals(code)) {
                        if (listener != null) {
                            listener.sessionFailed();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (listener != null) {
                            listener.showError(msg);
                        }
                        return;
                    }

                    String data = object.getString("data");
                    JSONArray jsonArray = new JSONArray(data);

                    if (jsonArray == null || jsonArray.length() <= 0) {
                        if (listener != null) {
                            listener.showError(ConstantUtil.SERVICE_NO_DATA);
                        }
                        return;
                    }

                    ArrayList<FundSubsEntity> entities = new ArrayList<FundSubsEntity>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        FundSubsEntity entity = new FundSubsEntity();

                        String fundname = jsonObject.optString("FUND_NAME");//基金名称
                        String fundCode = jsonObject.optString("FUND_CODE");//基金代码

                        String fund_val = jsonObject.optString("FUND_VAL");//净值
                        String openShare = jsonObject.optString("OPEN_SHARE");//最低投资


                        String company_name = jsonObject.optString("FUND_COMPANY_NAME");//基金归
                        String level = jsonObject.optString("OFUND_RISKLEVEL_NAME");//风险等级
                        String status = jsonObject.optString("FUND_STATUS_NAME");//状态

                        String type = jsonObject.optString("FUND_TYPE");// 类型0：不可定投 1：可定投
                        String typeCode = jsonObject.optString("OFUND_TYPE_CODE");//备注：2是货币型
                        String ofundType = jsonObject.optString("OFUND_TYPE");//基金类型中文含义 如：股票型、货币型等



                        entity.FUND_NAME = fundname;
                        entity.FUND_CODE = fundCode;
                        entity.FUND_VAL = fund_val;
                        entity.OPEN_SHARE = openShare;
                        entity.FUND_COMPANY_NAME = company_name;
                        entity.OFUND_RISKLEVEL_NAME = level;
                        entity.FUND_STATUS_NAME = status;
                        entity.FUND_TYPE = type;
                        entity.OFUND_TYPE = ofundType;
                        entity.FUND_TYPE_CODE = typeCode;
                        entities.add(entity);
                    }

                    if (listener != null) {
                        listener.getFundResult(entities);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.showError(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }

    public interface FundInfoConnectListener {

        void onRefreshComplete();

        void sessionFailed();

        void getFundResult(ArrayList<FundSubsEntity> entities);

        void showError(String error);
    }


}
