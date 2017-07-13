package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseTransactionPager;
import com.tpyzq.mobile.pangu.adapter.trade.SuccessAdapter;
import com.tpyzq.mobile.pangu.data.SuccessTransactionEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/8/12.
 * 已成页签
 */
public class SuccessTransactionPager extends BaseTransactionPager {
    public SuccessTransactionPager(Context context) {
        super(context);
    }

    LinearLayout ll_fourtext;
    TextView tv_text1, tv_text2, tv_text3, tv_text4;
    ImageView tv_empty;
    ListView lv_transaction;
    SuccessAdapter successAdapter;
    List<SuccessTransactionEntity> successTransactionBeans;
    @Override
    public void initData() {
        successTransactionBeans = new ArrayList<SuccessTransactionEntity>();
        tv_text1.setText("名称/价格");
        tv_text2.setText("委托时间");
        tv_text3.setText("买卖/数量");
        tv_text4.setText("金额");
        successAdapter = new SuccessAdapter(mContext);
        lv_transaction.setAdapter(successAdapter);
        lv_transaction.setEmptyView(tv_empty);
        getSecuritiesPositions();
    }
    public void setRefresh(){
        getSecuritiesPositions();
    }

    @Override
    public void setView() {
        ll_fourtext = (LinearLayout) rootView.findViewById(R.id.ll_fourtext);
        tv_text1 = (TextView) ll_fourtext.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) ll_fourtext.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) ll_fourtext.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) ll_fourtext.findViewById(R.id.tv_text4);
        lv_transaction = (ListView) rootView.findViewById(R.id.lv_transaction);
        tv_empty = (ImageView) rootView.findViewById(R.id.tv_empty);

    }

    private void getSecuritiesPositions() {
        HashMap map300180 = new HashMap();
        map300180.put("funcid", "300180");
        map300180.put("token", SpUtils.getString(mContext, "mSession", null));
        HashMap map300180_1 = new HashMap();
        map300180_1.put("FLAG", true);
        map300180_1.put("SEC_ID", "tpyzq");
//      map300180_1.put("FUND_ACCOUNT","610002680");
//      map300180_1.put("ACTION_IN","1");
        map300180_1.put("KEY_STR","");
        map300180_1.put("REC_COUNT","100");
        map300180.put("parms", map300180_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300180, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//                LogUtil.logE("查询已成", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if (code.equals("0")) {
                        successTransactionBeans.clear();
                        JSONArray jsonArray = new JSONArray(data);
                        SuccessTransactionEntity successTransactionBean;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            successTransactionBean = new Gson().fromJson(jsonArray.getString(i),SuccessTransactionEntity.class);
                            successTransactionBeans.add(successTransactionBean);
                        }
                        successAdapter.setSuccessTransactionBeans(successTransactionBeans);
                        successAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.pager_transaction;
    }

}
