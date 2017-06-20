package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.activity.trade.stock.QueryLimitActivity;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wangqi on 2017/2/17.
 * 新股 我的申购额度  A B股数量 逻辑
 */

public class MyPurchaseQuotaView implements View.OnClickListener {
    private static final String TAG = "MyPurchaseQuotaView";
    private Context mContext;
    private TextView mAtextView, mBtextView;
    private View mButton;

    public MyPurchaseQuotaView(Context context, TextView AtextView, TextView BtextView, View button) {
        mContext = context;
        mAtextView = AtextView;
        mBtextView = BtextView;
        mButton = button;
        mButton.setOnClickListener(this);
        initLogIc();
    }

    private void initLogIc() {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("funcid", "300380");
        map1.put("token", SpUtils.getString(mContext, "mSession", ""));
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(mContext, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<OneKeySubscribeBean>() {
                }.getType();
                OneKeySubscribeBean bean = gson.fromJson(response, type);
                List<OneKeySubscribeBean.DataBean> data = bean.getData();
                String code = bean.getCode();
                if (data != null && code.equals("0") && data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        OneKeySubscribeBean.DataBean dataBean = data.get(i);
                        String market = dataBean.getMARKET();
                        if (market.equals("1")) {
                            mAtextView.setText(dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf(".")) + "股");
                        } else if (market.equals("2")) {
                            mBtextView.setText(dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf(".")) + "股");
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(mContext, QueryLimitActivity.class);
        intent.putExtra("session", SpUtils.getString(mContext, "mSession", ""));
        mContext.startActivity(intent);
    }
}
