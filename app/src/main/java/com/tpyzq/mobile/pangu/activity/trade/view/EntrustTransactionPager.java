package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseTransactionPager;
import com.tpyzq.mobile.pangu.adapter.trade.EquitiesWithdrawAdapter;
import com.tpyzq.mobile.pangu.data.EquitiesWithDrawEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.RevokeDialog;
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
 * 委托可撤页签
 */
public class EntrustTransactionPager extends BaseTransactionPager {
    public EntrustTransactionPager(Context context) {
        super(context);
    }

    LinearLayout ll_fourtext;
    TextView tv_text1, tv_text2, tv_text3, tv_text4;
    ImageView tv_empty;
    PullToRefreshListView lv_transaction;
    EquitiesWithdrawAdapter equitiesWithdrawAdapter;
    List<EquitiesWithDrawEntity> equitiesWithDrawBeans;

    @Override
    public void initData() {
//        getSecuritiesPositions();
    }

    private void initEvent() {
        lv_transaction.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_transaction.setEmptyView(tv_empty);
        lv_transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = equitiesWithDrawBeans.get(position - 1).SECU_NAME;
                String titm = equitiesWithDrawBeans.get(position - 1).ORDER_TIME;
                String price = equitiesWithDrawBeans.get(position - 1).PRICE;
                String withdrawnQty = equitiesWithDrawBeans.get(position - 1).QTY;
                String entrustBs = equitiesWithDrawBeans.get(position - 1).ENTRUST_BS;
                String entrusNo = equitiesWithDrawBeans.get(position - 1).ENTRUST_NO;
                RevokeDialog revokeDialog = new RevokeDialog(mContext, name, titm, price, withdrawnQty, entrustBs, entrusNo, expression);
                revokeDialog.show();
            }
        });
        lv_transaction.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getSecuritiesPositions();
            }
        });
    }

    public void setRefresh() {
        getSecuritiesPositions();
    }

    RevokeDialog.Expression expression;

    @Override
    public void setView() {
        ll_fourtext = (LinearLayout) rootView.findViewById(R.id.ll_fourtext);
        tv_text1 = (TextView) ll_fourtext.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) ll_fourtext.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) ll_fourtext.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) ll_fourtext.findViewById(R.id.tv_text4);
        lv_transaction = (PullToRefreshListView) rootView.findViewById(R.id.lv_transaction);
        tv_empty = (ImageView) rootView.findViewById(R.id.tv_empty);
        expression = new RevokeDialog.Expression() {
            @Override
            public void State() {
                getSecuritiesPositions();
            }
        };
        equitiesWithDrawBeans = new ArrayList<EquitiesWithDrawEntity>();
        tv_text1.setText("股票名称");
        tv_text2.setText("委托/成交价");
        tv_text3.setText("委托/成交数");
        tv_text4.setText("状态/买卖");
        equitiesWithdrawAdapter = new EquitiesWithdrawAdapter(mContext);
        lv_transaction.setAdapter(equitiesWithdrawAdapter);
        initEvent();
    }

    private void getSecuritiesPositions() {
        HashMap map300130 = new HashMap();
        map300130.put("funcid", "300160");
        map300130.put("token", SpUtils.getString(mContext, "mSession", null));
        HashMap map300130_1 = new HashMap();
        map300130_1.put("FLAG", true);
        map300130_1.put("SEC_ID", "tpyzq");
        map300130_1.put("FUND_ACCOUNT", "101000913");
        map300130_1.put("ACTION_IN", "1");
        map300130.put("parms", map300130_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300130, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络访问失败", Toast.LENGTH_SHORT).show();
                lv_transaction.onRefreshComplete();
            }

            @Override
            public void onResponse(String response, int id) {
                lv_transaction.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if (code.equals("0")) {
                        JSONArray jsonArray = new JSONArray(data);
                        EquitiesWithDrawEntity equitiesWithDrawBean;
                        equitiesWithDrawBeans.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            equitiesWithDrawBean = new Gson().fromJson(jsonArray.getString(i), EquitiesWithDrawEntity.class);
                            equitiesWithDrawBeans.add(equitiesWithDrawBean);
                        }
                        equitiesWithdrawAdapter.setEquitiesWithdrawBeans(equitiesWithDrawBeans);
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
