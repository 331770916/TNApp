package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.CashFlowEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.magicindicator.FragmentContainerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class CashFlowActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private MagicIndicator mi_title;
    private String[] title;
    private List<String> lTitle;
    private FragmentContainerHelper mFragmentContainerHelper;
    private ScrollView ll_cash_flow;
    private TextView tv_view1,
            tv_view1_01,
            tv_view1_02,
            tv_view1_03,
            tv_view1_04,
            tv_view1_05,
            tv_view1_06,
            tv_view1_07,
            tv_view1_08,
            tv_view1_09,
            tv_view1_10,
            tv_view1_11,
            tv_view1_12,
            tv_view1_13,
            tv_view1_14,
            tv_view1_15,
            tv_view2_01,
            tv_view2_02,
            tv_view2_03,
            tv_view2_04,
            tv_view2_05,
            tv_view2_06,
            tv_view2_07,
            tv_view2_08,
            tv_view2_09,
            tv_view2_10,
            tv_view2_11,
            tv_view2_12,
            tv_view3_01,
            tv_view3_02,
            tv_view3_03,
            tv_view3_04,
            tv_view3_05,
            tv_view3_06,
            tv_view3_07,
            tv_view3_08,
            tv_view3_09,
            tv_view3_10,
            tv_view3_11,
            tv_view3_12,
            tv_view4_01,
            tv_view5_01,
            tv_view5_02,
            tv_view5_03,
            tv_view6_01,
            tv_view6_02,
            tv_view6_03,
            tv_view6_04,
            tv_view6_05,
            tv_view6_06,
            tv_view6_07,
            tv_view6_08,
            tv_view6_09,
            tv_view6_10,
            tv_view6_11,
            tv_view6_12,
            tv_view6_13,
            tv_view6_14,
            tv_view6_15,
            tv_view6_16,
            tv_view6_17;
    private String stockcode;
    private TextView tv_choose_title;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mi_title = (MagicIndicator) findViewById(R.id.mi_title);
        tv_choose_title = (TextView) findViewById(R.id.tv_choose_title);
        ll_cash_flow = (ScrollView) findViewById(R.id.ll_cash_flow);
        tv_view1_01 = (TextView) findViewById(R.id.tv_view1_01);
        tv_view1_02 = (TextView) findViewById(R.id.tv_view1_02);
        tv_view1_03 = (TextView) findViewById(R.id.tv_view1_03);
        tv_view1_04 = (TextView) findViewById(R.id.tv_view1_04);
        tv_view1_05 = (TextView) findViewById(R.id.tv_view1_05);
        tv_view1_06 = (TextView) findViewById(R.id.tv_view1_06);
        tv_view1_07 = (TextView) findViewById(R.id.tv_view1_07);
        tv_view1_08 = (TextView) findViewById(R.id.tv_view1_08);
        tv_view1_09 = (TextView) findViewById(R.id.tv_view1_09);
        tv_view1_10 = (TextView) findViewById(R.id.tv_view1_10);
        tv_view1_11 = (TextView) findViewById(R.id.tv_view1_11);
        tv_view1_12 = (TextView) findViewById(R.id.tv_view1_12);
        tv_view1_13 = (TextView) findViewById(R.id.tv_view1_13);
        tv_view1_14 = (TextView) findViewById(R.id.tv_view1_14);
        tv_view1_15 = (TextView) findViewById(R.id.tv_view1_15);
        tv_view2_01 = (TextView) findViewById(R.id.tv_view2_01);
        tv_view2_02 = (TextView) findViewById(R.id.tv_view2_02);
        tv_view2_03 = (TextView) findViewById(R.id.tv_view2_03);
        tv_view2_04 = (TextView) findViewById(R.id.tv_view2_04);
        tv_view2_05 = (TextView) findViewById(R.id.tv_view2_05);
        tv_view2_06 = (TextView) findViewById(R.id.tv_view2_06);
        tv_view2_07 = (TextView) findViewById(R.id.tv_view2_07);
        tv_view2_08 = (TextView) findViewById(R.id.tv_view2_08);
        tv_view2_09 = (TextView) findViewById(R.id.tv_view2_09);
        tv_view2_10 = (TextView) findViewById(R.id.tv_view2_10);
        tv_view2_11 = (TextView) findViewById(R.id.tv_view2_11);
        tv_view2_12 = (TextView) findViewById(R.id.tv_view2_12);
        tv_view3_01 = (TextView) findViewById(R.id.tv_view3_01);
        tv_view3_02 = (TextView) findViewById(R.id.tv_view3_02);
        tv_view3_03 = (TextView) findViewById(R.id.tv_view3_03);
        tv_view3_04 = (TextView) findViewById(R.id.tv_view3_04);
        tv_view3_05 = (TextView) findViewById(R.id.tv_view3_05);
        tv_view3_06 = (TextView) findViewById(R.id.tv_view3_06);
        tv_view3_07 = (TextView) findViewById(R.id.tv_view3_07);
        tv_view3_08 = (TextView) findViewById(R.id.tv_view3_08);
        tv_view3_09 = (TextView) findViewById(R.id.tv_view3_09);
        tv_view3_10 = (TextView) findViewById(R.id.tv_view3_10);
        tv_view3_11 = (TextView) findViewById(R.id.tv_view3_11);
        tv_view3_12 = (TextView) findViewById(R.id.tv_view3_12);
        tv_view4_01 = (TextView) findViewById(R.id.tv_view4_01);
        tv_view5_01 = (TextView) findViewById(R.id.tv_view5_01);
        tv_view5_02 = (TextView) findViewById(R.id.tv_view5_02);
        tv_view5_03 = (TextView) findViewById(R.id.tv_view5_03);
        tv_view6_01 = (TextView) findViewById(R.id.tv_view6_01);
        tv_view6_02 = (TextView) findViewById(R.id.tv_view6_02);
        tv_view6_03 = (TextView) findViewById(R.id.tv_view6_03);
        tv_view6_04 = (TextView) findViewById(R.id.tv_view6_04);
        tv_view6_05 = (TextView) findViewById(R.id.tv_view6_05);
        tv_view6_06 = (TextView) findViewById(R.id.tv_view6_06);
        tv_view6_07 = (TextView) findViewById(R.id.tv_view6_07);
        tv_view6_08 = (TextView) findViewById(R.id.tv_view6_08);
        tv_view6_09 = (TextView) findViewById(R.id.tv_view6_09);
        tv_view6_10 = (TextView) findViewById(R.id.tv_view6_10);
        tv_view6_11 = (TextView) findViewById(R.id.tv_view6_11);
        tv_view6_12 = (TextView) findViewById(R.id.tv_view6_12);
        tv_view6_13 = (TextView) findViewById(R.id.tv_view6_13);
        tv_view6_14 = (TextView) findViewById(R.id.tv_view6_14);
        tv_view6_15 = (TextView) findViewById(R.id.tv_view6_15);
        tv_view6_16 = (TextView) findViewById(R.id.tv_view6_16);
        tv_view6_17 = (TextView) findViewById(R.id.tv_view6_17);
        initData();
    }

    private void initData() {
        mFragmentContainerHelper = new FragmentContainerHelper();
        mFragmentContainerHelper.handlePageSelected(0, true);
        title = new String[]{"全部", "年报", "中报", "一季报", "三季报"};
        lTitle = Arrays.asList(title);
        setIndicatorListen();
        iv_back.setOnClickListener(this);
        Intent intent = getIntent();
        stockcode = intent.getStringExtra("stockcode");
        if (!TextUtils.isEmpty(stockcode)){
            getData(stockcode, "0");
        }
    }

    private void getData(String stockcode, final String pager) {
        HashMap map100200 = new HashMap();
        map100200.put("funcid", "100205");
        map100200.put("token", "");
        HashMap map100200_1 = new HashMap();
        map100200_1.put("secucode", stockcode);
        map100200_1.put("querytype", pager);
        map100200.put("parms", map100200_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_HQ_HS(), map100200, new StringCallback() {
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
                        CashFlowEntity cashFlowBean = new Gson().fromJson(dataArray.getString(0), CashFlowEntity.class);
                        setView(cashFlowBean,pager);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setView(CashFlowEntity cashFlowBean, String pager) {
        switch (pager) {
            case "0":
                tv_choose_title.setText(cashFlowBean.ENDDATE.substring(0,4)+"全部");
                break;
            case "4":
                tv_choose_title.setText(cashFlowBean.ENDDATE.substring(0,4)+"年报");
                break;
            case  "2":
                tv_choose_title.setText(cashFlowBean.ENDDATE.substring(0,4)+"中报");
                break;
            case "1":
                tv_choose_title.setText(cashFlowBean.ENDDATE.substring(0,4)+"一季报");
                break;
            case "3":
                tv_choose_title.setText(cashFlowBean.ENDDATE.substring(0,4)+"三季报");
                break;
        }
        tv_view1_01.setText(TransitionUtils.long2million(cashFlowBean.NETDEALTRADINGASSETS)+"元");
        tv_view1_02.setText(TransitionUtils.long2million(cashFlowBean.INTERESTANDCOMMISSIONCASHIN)+"元");
//        tv_view1_03.setText(cashFlowBean.OTHERCASHINRELATEDOPERATE);
//        tv_view1_04.setText(cashFlowBean.SUBTOTALOPERATECASHINFLOW);
        tv_view1_05.setText(TransitionUtils.long2million(cashFlowBean.OTHERCASHINRELATEDOPERATE)+"元");
        tv_view1_06.setText(TransitionUtils.long2million(cashFlowBean.SUBTOTALOPERATECASHINFLOW)+"元");
//        tv_view1_07.setText(cashFlowBean.NETDEALTRADINGASSETS);
        tv_view1_08.setText(TransitionUtils.long2million(cashFlowBean.COMMISSIONCASHPAID)+"元");
//        tv_view1_09.setText(cashFlowBean.NETDEALTRADINGASSETS);
//        tv_view1_10.setText(cashFlowBean.NETDEALTRADINGASSETS);
        tv_view1_11.setText(TransitionUtils.long2million(cashFlowBean.STAFFBEHALFPAID)+"元");
        tv_view1_12.setText(TransitionUtils.long2million(cashFlowBean.ALLTAXESPAID)+"元");
        tv_view1_13.setText(TransitionUtils.long2million(cashFlowBean.OTHEROPERATECASHPAID)+"元");
        tv_view1_14.setText(TransitionUtils.long2million(cashFlowBean.SUBTOTALOPERATECASHOUTFLOW)+"元");
        tv_view1_15.setText(TransitionUtils.long2million(cashFlowBean.NETOPERATECASHFLOW)+"元");
        tv_view2_01.setText(TransitionUtils.long2million(cashFlowBean.INVESTWITHDRAWALCASH)+"元");
        tv_view2_02.setText(TransitionUtils.long2million(cashFlowBean.INVESTPROCEEDS)+"元");
//        tv_view2_03.setText(cashFlowBean.NETCASHDEALSUBCOMPANY);
        tv_view2_04.setText(TransitionUtils.long2million(cashFlowBean.NETCASHDEALSUBCOMPANY)+"元");
        tv_view2_05.setText(TransitionUtils.long2million(cashFlowBean.OTHERCASHFROMINVESTACT)+"元");
        tv_view2_06.setText(TransitionUtils.long2million(cashFlowBean.SUBTOTALINVESTCASHINFLOW)+"元");
        tv_view2_07.setText(TransitionUtils.long2million(cashFlowBean.INVESTCASHPAID)+"元");
        tv_view2_08.setText(TransitionUtils.long2million(cashFlowBean.FIXINTANOTHERASSETACQUICASH)+"元");
//        tv_view2_09.setText(cashFlowBean.OTHERCASHTOINVESTACT);
        tv_view2_10.setText(TransitionUtils.long2million(cashFlowBean.OTHERCASHTOINVESTACT)+"元");
        tv_view2_11.setText(TransitionUtils.long2million(cashFlowBean.SUBTOTALINVESTCASHOUTFLOW)+"元");
        tv_view2_12.setText(TransitionUtils.long2million(cashFlowBean.NETINVESTCASHFLOW)+"元");
        tv_view3_01.setText(TransitionUtils.long2million(cashFlowBean.CASHFROMINVEST)+"元");
//        tv_view3_02.setText(cashFlowBean.NETDEALTRADINGASSETS);
        tv_view3_03.setText(TransitionUtils.long2million(cashFlowBean.CASHFROMMINOSINVESTSUB)+"元");
        tv_view3_04.setText(TransitionUtils.long2million(cashFlowBean.CASHFROMBORROWING)+"元");
        tv_view3_05.setText(TransitionUtils.long2million(cashFlowBean.CASHFROMBONDSISSUE)+"元");
        tv_view3_06.setText(TransitionUtils.long2million(cashFlowBean.SUBTOTALFINANCECASHINFLOW)+"元");
        tv_view3_07.setText(TransitionUtils.long2million(cashFlowBean.BORROWINGREPAYMENT)+"元");
        tv_view3_08.setText(TransitionUtils.long2million(cashFlowBean.DIVIDENDINTERESTPAYMENT)+"元");
        tv_view3_09.setText(TransitionUtils.long2million(cashFlowBean.PROCEEDSFROMSUBTOMINOS)+"元");
        tv_view3_10.setText(TransitionUtils.long2million(cashFlowBean.OTHERFINANCEACTPAYMENT)+"元");
        tv_view3_11.setText(TransitionUtils.long2million(cashFlowBean.SUBTOTALFINANCECASHOUTFLOW)+"元");
        tv_view3_12.setText(TransitionUtils.long2million(cashFlowBean.NETFINANCECASHFLOW)+"元");
        tv_view4_01.setText(TransitionUtils.long2million(cashFlowBean.EXCHANRATECHANGEEFFECT)+"元");
        tv_view5_01.setText(TransitionUtils.long2million(cashFlowBean.CASHEQUIVALENTINCREASE)+"元");
        tv_view5_02.setText(TransitionUtils.long2million(cashFlowBean.BEGINPERIODCASH)+"元");
        tv_view5_03.setText(TransitionUtils.long2million(cashFlowBean.ENDPERIODCASHEQUIVALENT)+"元");
        tv_view6_01.setText(TransitionUtils.long2million(cashFlowBean.NETPROFIT)+"元");
        tv_view6_02.setText(TransitionUtils.long2million(cashFlowBean.ASSETSDEPRECIATIONRESERVES)+"元");
        tv_view6_03.setText(TransitionUtils.long2million(cashFlowBean.FIXEDASSETDEPRECIATION)+"元");
        tv_view6_04.setText(TransitionUtils.long2million(cashFlowBean.INTANGIBLEASSETAMORTIZATION)+"元");
        tv_view6_05.setText(TransitionUtils.long2million(cashFlowBean.DEFERREDEXPENSEAMORT)+"元"); //FIXINTANOTHERASSETDISPOLOSS
        tv_view6_06.setText(TransitionUtils.long2million(cashFlowBean.FIXEDASSETSCRAPLOSS)+"元");
        tv_view6_07.setText(TransitionUtils.long2million(cashFlowBean.LOSSFROMFAIRVALUECHANGES)+"元");
        tv_view6_08.setText(TransitionUtils.long2million(cashFlowBean.FINANCIALEXPENSE)+"元");
        tv_view6_09.setText(TransitionUtils.long2million(cashFlowBean.INVESTLOSS)+"元");
        tv_view6_10.setText(TransitionUtils.long2million(cashFlowBean.DEFEREDTAXLIABILITYINCREASE)+"元");
        tv_view6_11.setText(TransitionUtils.long2million(cashFlowBean.OPERATERECEIVABLEDECREASE)+"元");
        tv_view6_12.setText(TransitionUtils.long2million(cashFlowBean.OPERATEPAYABLEINCREASE)+"元");
        tv_view6_13.setText(TransitionUtils.long2million(cashFlowBean.OTHERS)+"元");
//        tv_view6_14.setText(cashFlowBean.OTHERS);
        tv_view6_15.setText(TransitionUtils.long2million(cashFlowBean.CASHATENDOFYEAR)+"元");
//        tv_view6_16.setText(cashFlowBean.CASHATENDOFYEAR);
        tv_view6_17.setText(TransitionUtils.long2million(cashFlowBean.NETDEALTRADINGASSETS)+"元");
    }

    private void clearView() {
        tv_choose_title.setText("----");
        tv_view1_01.setText("--.--");
        tv_view1_02.setText("--.--");
//        tv_view1_03.setText("--.--");
//        tv_view1_04.setText("--.--");
        tv_view1_05.setText("--.--");
        tv_view1_06.setText("--.--");
//        tv_view1_07.setText("--.--");
        tv_view1_08.setText("--.--");
//        tv_view1_09.setText("--.--");
//        tv_view1_10.setText("--.--");
        tv_view1_11.setText("--.--");
        tv_view1_12.setText("--.--");
        tv_view1_13.setText("--.--");
        tv_view1_14.setText("--.--");
        tv_view1_15.setText("--.--");
        tv_view2_01.setText("--.--");
        tv_view2_02.setText("--.--");
//        tv_view2_03.setText("--.--");
        tv_view2_04.setText("--.--");
        tv_view2_05.setText("--.--");
        tv_view2_06.setText("--.--");
        tv_view2_07.setText("--.--");
        tv_view2_08.setText("--.--");
//        tv_view2_09.setText("--.--");
        tv_view2_10.setText("--.--");
        tv_view2_11.setText("--.--");
        tv_view2_12.setText("--.--");
        tv_view3_01.setText("--.--");
//        tv_view3_02.setText("--.--");
        tv_view3_03.setText("--.--");
        tv_view3_04.setText("--.--");
        tv_view3_05.setText("--.--");
        tv_view3_06.setText("--.--");
        tv_view3_07.setText("--.--");
        tv_view3_08.setText("--.--");
        tv_view3_09.setText("--.--");
        tv_view3_10.setText("--.--");
        tv_view3_11.setText("--.--");
        tv_view3_12.setText("--.--");
        tv_view4_01.setText("--.--");
        tv_view5_01.setText("--.--");
        tv_view5_02.setText("--.--");
        tv_view5_03.setText("--.--");
        tv_view6_01.setText("--.--");
        tv_view6_02.setText("--.--");
        tv_view6_03.setText("--.--");
        tv_view6_04.setText("--.--");
        tv_view6_05.setText("--.--");
        tv_view6_06.setText("--.--");
        tv_view6_07.setText("--.--");
        tv_view6_08.setText("--.--");
        tv_view6_09.setText("--.--");
        tv_view6_10.setText("--.--");
        tv_view6_11.setText("--.--");
        tv_view6_12.setText("--.--");
        tv_view6_13.setText("--.--");
//        tv_view6_14.setText("--.--");
        tv_view6_15.setText("--.--");
//        tv_view6_16.setText("--.--");
        tv_view6_17.setText("--.--");
    }

    /**
     * 设置底部页签切换监听器
     */
    private void setIndicatorListen() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return lTitle == null ? 0 : lTitle.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setText(lTitle.get(index));
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.blue));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearView();
                        mFragmentContainerHelper.handlePageSelected(index);
                        String pager = "";
                        switch (index) {
                            case 0:
                                pager = "0";
                                break;
                            case 1:
                                pager = "4";
                                break;
                            case 2:
                                pager = "2";
                                break;
                            case 3:
                                pager = "1";
                                break;
                            case 4:
                                pager = "3";
                                break;
                        }
                        if (!TextUtils.isEmpty(stockcode)){
                            getData(stockcode, pager);
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(getResources().getColor(R.color.blue));
                return linePagerIndicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 1.0f;
                } else if (index == 1) {
                    return 1.0f;
                } else if (index == 2) {
                    return 1.0f;
                } else {
                    return 1.0f;
                }
            }
        });
        mi_title.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(mi_title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_flow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
