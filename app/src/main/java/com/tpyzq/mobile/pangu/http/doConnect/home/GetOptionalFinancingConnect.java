package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.NewOptionalFinancingEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
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
 * Created by wangqi on 2017/2/10.
 * 稳赢
 */

public class GetOptionalFinancingConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetOptionalFinancingConnect";
    private String mHttpTAG;
    private List<NewOptionalFinancingEntity> list;

    public GetOptionalFinancingConnect(String httpTag) {
        mHttpTAG = httpTag;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        final Map params = new HashMap();
        params.put("FUNCTIONCODE", "HQTNG102");
        Map map = new HashMap();
        map.put("SCHEMAID", "13");
        params.put("PARAMS", map);
        params.put("TOKEN", "");
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_RS, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                list = new ArrayList<NewOptionalFinancingEntity>();
                LogHelper.e(TAG, e.toString());
                mCallbackResult.getResult(list, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    JSONObject message = jsonObject.getJSONObject("message");
                    JSONArray prod = message.getJSONArray("prod");
                    if ("200".equals(code)) {
                        list = new ArrayList<NewOptionalFinancingEntity>();
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
//                        for (int j = 0; j < prod.length(); j++) {
//                            NewOptionalFinancingEntity bean = new NewOptionalFinancingEntity();
//                            if (j < 3) {
//                                if ("3".equals(prod.getJSONObject(j).getString("TYPE"))) {
//                                    bean.setProd_wfsy(prod.getJSONObject(j).getString("prod_wfsy"));
//                                    bean.setProd_name(prod.getJSONObject(j).getString("prod_name"));
//                                    bean.setProd_nhsy(prod.getJSONObject(j).getString("prod_nhsy"));
//                                    bean.setProd_qgje(prod.getJSONObject(j).getString("prod_qgje"));
//                                    bean.setProd_code(prod.getJSONObject(j).getString("prod_code"));
//                                    bean.setProd_source(prod.getJSONObject(j).getString("prod_source"));
//                                    bean.setProd_term(prod.getJSONObject(j).getString("prod_term"));
//                                    bean.setType(prod.getJSONObject(j).getString("TYPE"));
//                                    list.add(bean);
//
//                                } else {
//                                    bean.setProd_wfsy(prod.getJSONObject(j).getString("prod_wfsy"));
//                                    bean.setProd_name(prod.getJSONObject(j).getString("prod_name"));
//                                    bean.setProd_nhsy(prod.getJSONObject(j).getString("prod_nhsy"));
//                                    bean.setProd_qgje(prod.getJSONObject(j).getString("prod_qgje"));
//                                    bean.setProd_code(prod.getJSONObject(j).getString("prod_code"));
//                                    bean.setProd_source(prod.getJSONObject(j).getString("prod_source"));
//                                    bean.setOfund_risklevel_name(prod.getJSONObject(j).getString("ofund_risklevel_name"));
//                                    bean.setType(prod.getJSONObject(j).getString("TYPE"));
//                                    bean.setProd_term(prod.getJSONObject(j).getString("prod_term"));
//                                    list.add(bean);
//                                }
//                            }
//
//                        }
                    }
                    mCallbackResult.getResult(list, TAG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
