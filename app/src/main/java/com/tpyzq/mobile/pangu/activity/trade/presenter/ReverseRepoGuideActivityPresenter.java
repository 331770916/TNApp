package com.tpyzq.mobile.pangu.activity.trade.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ReverseRepoGuideActivity;
import com.tpyzq.mobile.pangu.data.UserMoneyEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * anthor:Created by tianchen on 2017/4/1.
 * email:963181974@qq.com
 */

public class ReverseRepoGuideActivityPresenter {
    private ReverseRepoGuideActivity activity;

    public ReverseRepoGuideActivityPresenter(ReverseRepoGuideActivity activity) {
        this.activity = activity;
    }


    public ReverseRepoGuideActivity getActivity() {
        return activity;
    }

    public void getUseMoney() {
        HashMap map300608 = new HashMap();
        map300608.put("funcid", "300608");
        map300608.put("token", SpUtils.getString(activity, "mSession", null));
        HashMap map300608_1 = new HashMap();
        map300608_1.put("SEC_ID", "tpyzq");
        map300608_1.put("FLAG", "true");
        map300608.put("parms", map300608_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300608, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        UserMoneyEntity userMoneyBean = new Gson().fromJson(jsonArray.getString(0), UserMoneyEntity.class);
                        activity.setMoney(userMoneyBean.ENABLE_BALANCE);
                    } else if ("-6".equals(code)) {
                        activity.startActivity(new Intent(activity, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(activity, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
