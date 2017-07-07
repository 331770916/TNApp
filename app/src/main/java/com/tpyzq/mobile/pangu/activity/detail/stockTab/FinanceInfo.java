package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockFinanceEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/10/30.
 * 财务页签
 */
public class FinanceInfo extends BaseStockDetailPager implements View.OnClickListener {
    TextView tv_view1;
    TextView tv_view2;
    TextView tv_view3;
    TextView tv_view4;

    TextView tv_view1_01;
    TextView tv_view1_02;
    TextView tv_view1_03;
    TextView tv_view1_04;
    TextView tv_view1_05;

    TextView tv_view2_01;
    TextView tv_view2_02;
    TextView tv_view2_03;
    TextView tv_view2_04;
    TextView tv_view2_05;
    TextView tv_view2_06;

    TextView tv_view3_01;
    TextView tv_view3_02;
    TextView tv_view3_03;

    TextView tv_view4_01;
    TextView tv_view4_02;
    TextView tv_view4_03;
    TextView tv_view4_04;
    String stockcode;

    public FinanceInfo(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        tv_view1 = (TextView) rootView.findViewById(R.id.tv_view1);
        tv_view2 = (TextView) rootView.findViewById(R.id.tv_view2);
        tv_view3 = (TextView) rootView.findViewById(R.id.tv_view3);
        tv_view4 = (TextView) rootView.findViewById(R.id.tv_view4);
        tv_view1_01 = (TextView) rootView.findViewById(R.id.tv_view1_01);
        tv_view1_02 = (TextView) rootView.findViewById(R.id.tv_view1_02);
        tv_view1_03 = (TextView) rootView.findViewById(R.id.tv_view1_03);
        tv_view1_04 = (TextView) rootView.findViewById(R.id.tv_view1_04);
        tv_view1_05 = (TextView) rootView.findViewById(R.id.tv_view1_05);
        tv_view2_01 = (TextView) rootView.findViewById(R.id.tv_view2_01);
        tv_view2_02 = (TextView) rootView.findViewById(R.id.tv_view2_02);
        tv_view2_03 = (TextView) rootView.findViewById(R.id.tv_view2_03);
        tv_view2_04 = (TextView) rootView.findViewById(R.id.tv_view2_04);
        tv_view2_05 = (TextView) rootView.findViewById(R.id.tv_view2_05);
        tv_view2_06 = (TextView) rootView.findViewById(R.id.tv_view2_06);
        tv_view3_01 = (TextView) rootView.findViewById(R.id.tv_view3_01);
        tv_view3_02 = (TextView) rootView.findViewById(R.id.tv_view3_02);
        tv_view3_03 = (TextView) rootView.findViewById(R.id.tv_view3_03);
        tv_view4_01 = (TextView) rootView.findViewById(R.id.tv_view4_01);
        tv_view4_02 = (TextView) rootView.findViewById(R.id.tv_view4_02);
        tv_view4_03 = (TextView) rootView.findViewById(R.id.tv_view4_03);
        tv_view4_04 = (TextView) rootView.findViewById(R.id.tv_view4_04);
    }

    @Override
    public void initData(String stockCode) {
        this.stockcode = stockCode;
        getFinance(stockCode);
    }

    private void getFinance(String stockcode) {
        HashMap map100201 = new HashMap();
        map100201.put("funcid", "100201");
        map100201.put("token", "");
        HashMap map100201_1 = new HashMap();
        map100201_1.put("secucode", stockcode);
        map100201.put("parms", map100201_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_NEW, map100201, new StringCallback() {
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
                    JSONArray dataArray = new JSONArray(data);
                    if ("0".equals(code)) {
                        if(null != dataArray && dataArray.length() > 0){
                            StockFinanceEntity stockFinanceBean = new Gson().fromJson(dataArray.getString(0), StockFinanceEntity.class);
                            setText(stockFinanceBean);
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setText(StockFinanceEntity stockFinanceBean) {
        String title = "";
        if (!TextUtils.isEmpty(stockFinanceBean.ENDDATE) && !TextUtils.isEmpty(stockFinanceBean.INFOSOURCE) && stockFinanceBean.ENDDATE.length() > 4 && stockFinanceBean.INFOSOURCE.length() > 1) {
            title = stockFinanceBean.ENDDATE.substring(0, 4) + stockFinanceBean.INFOSOURCE.substring(1);
        }
        tv_view1.setText(title);
        tv_view2.setText(title);
        tv_view3.setText(title);
        tv_view4.setText(title);
        tv_view1_01.setText(TransitionUtils.long2million(stockFinanceBean.BASICEPS));
        tv_view1_02.setText(TransitionUtils.long2million(stockFinanceBean.NETASSETPS));
        tv_view1_03.setText(TransitionUtils.long2million(stockFinanceBean.CAPITALSURPLUSFUNDPS));
        tv_view1_04.setText(TransitionUtils.long2million(stockFinanceBean.UNDIVIDEDPROFIT));
        tv_view1_05.setText(TransitionUtils.long2million(stockFinanceBean.OPERCASHFLOWPS));
        tv_view2_01.setText(TransitionUtils.long2million(stockFinanceBean.OPERATINGREENUE));
        tv_view2_02.setText(TransitionUtils.long2million(stockFinanceBean.OPERATINGREVENUEGROWRATE) + "%");
        tv_view2_03.setText(TransitionUtils.long2million(stockFinanceBean.OPERATINGPROFIT));
        tv_view2_04.setText(TransitionUtils.long2million(stockFinanceBean.OPERPROFITGROWRATE) + "%");
        tv_view2_05.setText(TransitionUtils.long2million(stockFinanceBean.NETPROFIT));
        tv_view2_06.setText(TransitionUtils.long2million(stockFinanceBean.NETPROFITGROWRATE) + "%");
        tv_view3_01.setText(TransitionUtils.long2million(stockFinanceBean.TOTALASSETS));
        tv_view3_02.setText(TransitionUtils.long2million(stockFinanceBean.TOTALLIABILITY));
        tv_view3_03.setText(TransitionUtils.long2million(stockFinanceBean.TOTALSHAREHOLDEREQUITY));
        tv_view4_01.setText(TransitionUtils.long2million(stockFinanceBean.NETOPERATECASHFLOW));
        tv_view4_02.setText(TransitionUtils.long2million(stockFinanceBean.NETOPERATECASHFLOWYOY) + "%");
        tv_view4_03.setText(TransitionUtils.long2million(stockFinanceBean.NETINVESTCASHFLOW));
        tv_view4_04.setText(TransitionUtils.long2million(stockFinanceBean.NETFINANCECASHFLOW));
        tv_view1.setOnClickListener(this);
        tv_view2.setOnClickListener(this);
        tv_view3.setOnClickListener(this);
        tv_view4.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.vp_stock_finance;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_view1:
                intent.setClass(mContext, KeyIndexActivity.class);
                intent.putExtra("stockcode", stockcode);
                mContext.startActivity(intent);
                break;
            case R.id.tv_view2:
                intent.setClass(mContext, StockProfitActivity.class);
                intent.putExtra("stockcode", stockcode);
                mContext.startActivity(intent);
                break;
            case R.id.tv_view3:
                intent.setClass(mContext, BalanceSheetActivity.class);
                intent.putExtra("stockcode", stockcode);
                mContext.startActivity(intent);
                break;
            case R.id.tv_view4:
                intent.setClass(mContext, CashFlowActivity.class);
                intent.putExtra("stockcode", stockcode);
                mContext.startActivity(intent);
                break;
        }
    }
}
