package com.tpyzq.mobile.pangu.activity.trade.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ReverseRepoActivity;
import com.tpyzq.mobile.pangu.data.AuthorizeEntity;
import com.tpyzq.mobile.pangu.data.TimeShareEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.panguutil.JudgeStockUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.TransitionUtils.fundPirce;

/**
 * anthor:Created by tianchen on 2017/4/6.
 * email:963181974@qq.com
 */

public class ReverseRepoActivityPresenter {
    ReverseRepoActivity activity;

    public ReverseRepoActivityPresenter(ReverseRepoActivity activity) {
        this.activity = activity;
    }

    public void getHeaderView(final String code) {
        Map<String, String> map003 = new HashMap();
        Object[] object = new Object[1];
        map003.put("FUNCTIONCODE", "HQING003");
        Map map2 = new HashMap();
        map2.put("market", "0");
        map2.put("stockcode", code);
        map2.put("time", "93000");
        map2.put("type", "0");
        object[0] = map2;
        map003.put("PARAMS", Arrays.toString(object));
        NetWorkUtil.getInstence().okHttpForGet("", ConstantUtil.URL, map003, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONArray object = new JSONArray(response);
                    JSONObject jsonObject = object.getJSONObject(0);
                    JSONArray jsArray = jsonObject.getJSONArray("data");
                    JSONArray jsArray_stock = jsArray.getJSONArray(0);
                    String closePrice = jsArray.getString(2);
                    TimeShareEntity timeShareData = new TimeShareEntity();
                    timeShareData.mClosePrice = Float.parseFloat(closePrice);

                    activity.setAdapter(jsArray);
                    double price;

                    if (!"0.0".equals(jsArray.getString(20))) {
                        price = Double.parseDouble(jsArray.getString(20));
                    } else {
                        price = Double.parseDouble(jsArray.getString(25));
                    }
                    activity.setTextView(fundPirce(code, price + ""), fundPirce(code, jsArray.getString(46)));
                    // et_price.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getAmount(String code) {
        HashMap<Object, Object> map300130 = new HashMap<>();
        map300130.put("funcid", "333001");
        map300130.put("token", SpUtils.getString(activity, "mSession", null));
        HashMap<Object, Object> map300130_1 = new HashMap<>();
        map300130_1.put("FLAG", true);
        map300130_1.put("SEC_ID", "tpyzq");
        String market = JudgeStockUtils.getStockMarketCode(code);
        map300130_1.put("MARKET", market);
        map300130_1.put("TRD_ID", 0);
        map300130_1.put("SECU_CODE", code.substring(2));
        map300130_1.put("ORDER_PRICE", 1);
        map300130.put("parms", map300130_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300130, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(activity, "网络访问失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        AuthorizeEntity authorizeBean = new Gson().fromJson(jsonArray.getString(0), AuthorizeEntity.class);
                        int num = (int) Double.parseDouble(authorizeBean.TRD_QTY)*100;
                        activity.setPrice(num);
                    }else if ("-6".equals(code)){
                        activity.startActivity(new Intent(activity, TransactionLoginActivity.class));
                    } else {
                        MistakeDialog.showDialog(msg, activity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getBuy(final String prodcode, String price, String num) {
        HashMap map300140 = new HashMap();
        map300140.put("funcid", "300140");
        map300140.put("token", SpUtils.getString(activity, "mSession", null));
        HashMap map300140_1 = new HashMap();
        map300140_1.put("SEC_ID", "tpyzq");
        String market_code = "";
        if (prodcode.startsWith("2")) {
            market_code = "2";
        } else if (prodcode.startsWith("1")) {
            market_code = "1";
        }
        map300140_1.put("MARKET", market_code);
        map300140_1.put("TRD_ID", "2");
        map300140_1.put("SECU_CODE", prodcode.substring(2));
        map300140_1.put("ORDER_PRICE", price);
        map300140_1.put("ORDER_QTY", num);
        map300140_1.put("ENTRUST_PROP", 0);
        map300140_1.put("FLAG", true);
        map300140.put("parms", map300140_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300140, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(activity, "网络访问失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        activity.setClearView();
                        getAmount(prodcode);
                        ToastUtils.showShort(activity,"委托已提交");
                    }else if ("-6".equals(code)){
                        activity.startActivity(new Intent(activity, TransactionLoginActivity.class));
                    } else{
                        MistakeDialog.showDialog(msg,activity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
