package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.FundShareAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundEntity;
import com.tpyzq.mobile.pangu.data.FundRedemptionEntity;
import com.tpyzq.mobile.pangu.data.FundShareEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/10/9.
 * 基金份额
 */
public class FundShareActivity extends BaseActivity implements View.OnClickListener {

    private TextView mMarketPriceTv;
    private TextView mEarnTv;
    private TextView mAvailableTv;
    private FundShareAdapter mAdapter;
    private RelativeLayout rl_view1;
    private RelativeLayout rl_view2;
    private List<FundEntity> fundBeans;
    private ImageView iv_kong;
    private LinearLayout ll_content;

    @Override
    public void initView() {
        findViewById(R.id.fundshareBackbtn).setOnClickListener(this);
        mEarnTv = (TextView) findViewById(R.id.fundShareEarn);
        mAvailableTv = (TextView) findViewById(R.id.fundShareAveriable);
        mMarketPriceTv = (TextView) findViewById(R.id.fundshareMarketPrice);
        rl_view1 = (RelativeLayout) findViewById(R.id.rl_view1);
        rl_view2 = (RelativeLayout) findViewById(R.id.rl_view2);
        ListView listView = (ListView) findViewById(R.id.fundshareListView);
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        mAdapter = new FundShareAdapter(this);
        listView.setAdapter(mAdapter);
        fundBeans = new ArrayList<FundEntity>();
        iv_kong.setVisibility(View.VISIBLE);
        ll_content.setVisibility(View.GONE);
        fundQuery();
    }

    /**
     * 获取基金份额
     */
    private void fundQuery() {
        HashMap map720260 = new HashMap();
        map720260.put("funcid", "720260");
        map720260.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map720260_1 = new HashMap();
        map720260_1.put("SEC_ID", "tpyzq");
        map720260_1.put("FLAG", "true");
        map720260.put("parms", map720260_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map720260, new StringCallback() {
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
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        FundRedemptionEntity fundRedemptionBean = new Gson().fromJson(jsonArray.getString(0), FundRedemptionEntity.class);

                        mMarketPriceTv.setText(fundRedemptionBean.OPFUND_MARKET_VALUE);
                        mEarnTv.setText("盈亏\n" + fundRedemptionBean.TOTAL_INCOME);
                        if (fundRedemptionBean.TOTAL_INCOME.startsWith("0")) {
                            rl_view1.setBackgroundColor(ColorUtils.STOCKGRAY);
                            rl_view2.setBackgroundColor(ColorUtils.STOCKGRAY);
                        } else if (fundRedemptionBean.TOTAL_INCOME.contains("-")) {
                            rl_view1.setBackgroundColor(ColorUtils.GREEN);
                            rl_view2.setBackgroundColor(ColorUtils.GREEN);
                        } else {
                            rl_view1.setBackgroundColor(ColorUtils.RED);
                            rl_view2.setBackgroundColor(ColorUtils.RED);
                        }
                        mAvailableTv.setText("可用资金\n" + fundRedemptionBean.AVAILABLE);

                        mAdapter.setDatas(setData(fundRedemptionBean));
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundShareActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(FundShareActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<FundShareEntity> setData(FundRedemptionEntity fundRedemptionBean) {
        ArrayList<FundShareEntity> datas = new ArrayList<>();

        for (int i = 0; i < fundRedemptionBean.RESULT_LIST.size(); i++) {
            FundShareEntity entity = new FundShareEntity();
            entity.setStockName(fundRedemptionBean.RESULT_LIST.get(i).FUND_NAME);
            entity.setStockCode(fundRedemptionBean.RESULT_LIST.get(i).FUND_CODE);
            entity.setHoldShare(fundRedemptionBean.RESULT_LIST.get(i).CURRENT_SHARE);
            entity.setNetWorth(fundRedemptionBean.RESULT_LIST.get(i).NAV);
            entity.setMarketValue(fundRedemptionBean.RESULT_LIST.get(i).MARKET_VALUE);
            entity.setProfitLoss(fundRedemptionBean.RESULT_LIST.get(i).INCOME);
            entity.setCommpanyName(fundRedemptionBean.RESULT_LIST.get(i).FUND_COMPANY_NAME);
            entity.setCommpanyCode(fundRedemptionBean.RESULT_LIST.get(i).FUND_COMPANY_CODE);
            entity.setFreezeNumber(fundRedemptionBean.RESULT_LIST.get(i).FROZEN_AMOUNT);
            entity.setAvailableNumber(fundRedemptionBean.RESULT_LIST.get(i).ENABLE_AMOUNT);
            String AUTO_BUY = fundRedemptionBean.RESULT_LIST.get(i).AUTO_BUY;
            if ("0".equals(AUTO_BUY)) {
                entity.setProfitType("份额分红");
            } else {
                entity.setProfitType("现金分红");
            }
            entity.setCostPrice(fundRedemptionBean.RESULT_LIST.get(i).OF_COST_PRICE);
            entity.setUnfold(false);
            datas.add(entity);
        }

        if (datas != null && datas.size() > 0) {
            iv_kong.setVisibility(View.GONE);
            ll_content.setVisibility(View.VISIBLE);
        }else {
            iv_kong.setVisibility(View.VISIBLE);
            ll_content.setVisibility(View.GONE);
        }
        return datas;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fundshareBackbtn:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fundshare;
    }
}
