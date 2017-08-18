package com.tpyzq.mobile.pangu.activity.home.managerMoney.view.homeSafeBetView;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.NewOptionalFinancingEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
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
 * Created by zhangwnebo on 2017/8/18.
 */

public class GetHomeSafeBetPresenter {

    public void getSafeBetData(String tag, final GetHomeSafeBetListener listener) {
        Map params = new HashMap();
        params.put("FUNCTIONCODE", "HQTNG102");
        Map map = new HashMap();
        map.put("SCHEMAID", "13");
        params.put("PARAMS", map);
        params.put("TOKEN", "");
        NetWorkUtil.getInstence().okHttpForPostString(tag, ConstantUtil.getURL_HQ_WB(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (listener != null) {
                    listener.onError(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    if (listener != null) {
                        listener.onError(ConstantUtil.SERVICE_NO_DATA);
                    }
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String type = jsonObject.getString("type");

                    if (!"200".equals(code)) {
                        if (listener != null) {
                            listener.onError(type);
                        }
                        return;
                    }

                    JSONObject message = jsonObject.getJSONObject("message");
                    JSONArray prod = message.getJSONArray("prod");

                    ArrayList<NewOptionalFinancingEntity> list = new ArrayList<NewOptionalFinancingEntity>();
                    NewOptionalFinancingEntity bean = new NewOptionalFinancingEntity();
                    bean.setProd_code(prod.getJSONObject(0).optString("prod_code"));
                    bean.setType(prod.getJSONObject(0).optString("TYPE"));
                    bean.setProd_type(prod.getJSONObject(0).optString("prod_type"));
                    bean.setProd_nhsy(prod.getJSONObject(0).optString("prod_nhsy"));
                    bean.setProd_qgje(prod.getJSONObject(0).optString("prod_qgje"));
                    bean.setSchema_id(message.getString("schema_id"));
                    bean.setProd_status(prod.getJSONObject(0).optString("prod_status"));
                    bean.setIpo_begin_date(prod.getJSONObject(0).optString("ipo_begin_date"));
                    bean.setProd_type_name(prod.getJSONObject(0).optString("prod_type_name"));

                    bean.setProd_wfsy(prod.getJSONObject(0).optString("prod_wfsy"));
                    bean.setProd_name(prod.getJSONObject(0).optString("prod_name"));
                    bean.setProd_term(prod.getJSONObject(0).optString("prod_term"));
                    bean.setProd_source(prod.getJSONObject(0).optString("prod_source"));
                    list.add(bean);

                    if (listener != null) {
                        listener.getData(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }


    public interface GetHomeSafeBetListener{

        public void onError(String error);

        public void getData(List<NewOptionalFinancingEntity> list ) ;

    }

}
