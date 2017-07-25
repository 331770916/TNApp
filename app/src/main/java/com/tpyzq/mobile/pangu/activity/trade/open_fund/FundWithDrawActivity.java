package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.WithdrawAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.WithDrawEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.dialog.FundWithDrawDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bonree.k.H;
import okhttp3.Call;

/**
 * 基金撤单
 */
public class FundWithDrawActivity extends BaseActivity implements View.OnClickListener, FundWithDrawDialog.FundWithDrawListen {
    ImageView iv_back;
    ImageView iv_kong;
    LinearLayout ll_title;
    TextView tv_text1, tv_text2, tv_text3, tv_text4;
    List<WithDrawEntity> withDrawBeens;
    WithdrawAdapter withdrawAdapter;
    PullToRefreshListView lv_fund;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        lv_fund = (PullToRefreshListView) findViewById(R.id.lv_fund);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        tv_text1 = (TextView) ll_title.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) ll_title.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) ll_title.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) ll_title.findViewById(R.id.tv_text4);
        initData();
        getFund();
    }

    private void initData() {
        lv_fund.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        withDrawBeens = new ArrayList<>();
        iv_back.setOnClickListener(this);
        tv_text1.setText("名称");
        tv_text2.setText("委托时间");
        tv_text3.setText("金额/份额");
        tv_text4.setText("业务类型/状态");
        withdrawAdapter = new WithdrawAdapter(this);
        withdrawAdapter.setWithDrawBeens(withDrawBeens);
        lv_fund.setAdapter(withdrawAdapter);
        lv_fund.setEmptyView(iv_kong);
        lv_fund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FundWithDrawDialog fundWithDrawDialog = new FundWithDrawDialog(FundWithDrawActivity.this, FundWithDrawActivity.this, withDrawBeens.get(position).FUND_NAME, withDrawBeens.get(position).FUND_CODE, withDrawBeens.get(position).BUSINESS_NAME, withDrawBeens.get(position).TRADE_AMOUNT, withDrawBeens.get(position).TRADE_AMOUNT, withDrawBeens.get(position).ORDER_DATE, withDrawBeens.get(position).ENTRUST_NO);
                fundWithDrawDialog.show();
            }
        });

        lv_fund.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getFund();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {


            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_with_draw;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 获取当日委托基金撤单数据
     */
    public void getFund() {
        HashMap map720320 = new HashMap();
        map720320.put("funcid", "720320");
        map720320.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map720320_1 = new HashMap();
        map720320_1.put("SEC_ID", "tpyzq");
        map720320_1.put("FLAG", "true");
        map720320_1.put("ACTION_IN", "1");
        map720320.put("parms", map720320_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map720320, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                lv_fund.onRefreshComplete();
                Helper.getInstance().showToast(FundWithDrawActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                lv_fund.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    JSONArray dataArray = new JSONArray(data);
                    if (code.equals("0")) {
                        WithDrawEntity withDrawBean;
                        withDrawBeens.clear();
                        for (int i = 0; i < dataArray.length(); i++) {
                            withDrawBean = new Gson().fromJson(dataArray.getString(i), WithDrawEntity.class);
                            withDrawBeens.add(withDrawBean);
                        }
                        withdrawAdapter.setWithDrawBeens(withDrawBeens);
                        withdrawAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showShort(FundWithDrawActivity.this, msg);
                        startActivity(new Intent(FundWithDrawActivity.this, TransactionLoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setClear() {
        getFund();
    }
}
