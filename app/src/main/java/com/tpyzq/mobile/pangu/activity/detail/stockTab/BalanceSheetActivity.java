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
import com.tpyzq.mobile.pangu.data.BalanceSheetEntity;
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

/**
 * 资产负债表
 */
public class BalanceSheetActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private MagicIndicator mi_title;
    private String[] title;
    private List<String> lTitle;
    private FragmentContainerHelper mFragmentContainerHelper;
    private ScrollView ll_balance_sheet;
    private TextView tv_view1_01, tv_view1_02, tv_view1_03, tv_view1_04, tv_view1_05, tv_view1_06, tv_view1_07, tv_view1_08, tv_view1_09, tv_view1_10, tv_view1_11, tv_view1_12, tv_view1_13, tv_view1_14, tv_view1_15, tv_view1_16, tv_view1_17, tv_view1_18, tv_view1_19, tv_view1_20, tv_view1_21, tv_view1_22, tv_view2_01, tv_view2_02, tv_view2_03, tv_view2_04, tv_view2_05, tv_view2_06, tv_view2_07, tv_view2_08, tv_view2_09, tv_view2_10, tv_view2_11, tv_view2_12, tv_view2_13, tv_view2_14, tv_view2_15, tv_view3_01, tv_view3_02, tv_view3_03, tv_view3_04, tv_view3_05, tv_view3_06, tv_view3_07, tv_view3_08, tv_view3_09, tv_view3_10;
    private String stockcode;
    private TextView tv_choose_title;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mi_title = (MagicIndicator) findViewById(R.id.mi_title);
        tv_choose_title = (TextView) findViewById(R.id.tv_choose_title);
        ll_balance_sheet = (ScrollView) findViewById(R.id.ll_balance_sheet);
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
//        tv_view1_15 = (TextView) findViewById(R.id.tv_view1_15);
//        tv_view1_16 = (TextView) findViewById(R.id.tv_view1_16);
//        tv_view1_17 = (TextView) findViewById(R.id.tv_view1_17);
//        tv_view1_18 = (TextView) findViewById(R.id.tv_view1_18);
        tv_view1_19 = (TextView) findViewById(R.id.tv_view1_19);
        tv_view1_20 = (TextView) findViewById(R.id.tv_view1_20);
        tv_view1_21 = (TextView) findViewById(R.id.tv_view1_21);
        tv_view1_22 = (TextView) findViewById(R.id.tv_view1_22);
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
        tv_view2_13 = (TextView) findViewById(R.id.tv_view2_13);
        tv_view2_14 = (TextView) findViewById(R.id.tv_view2_14);
        tv_view2_15 = (TextView) findViewById(R.id.tv_view2_15);
        tv_view3_01 = (TextView) findViewById(R.id.tv_view3_01);
        tv_view3_02 = (TextView) findViewById(R.id.tv_view3_02);
        tv_view3_03 = (TextView) findViewById(R.id.tv_view3_03);
        tv_view3_04 = (TextView) findViewById(R.id.tv_view3_04);
//        tv_view3_05 = (TextView) findViewById(R.id.tv_view3_05);
        tv_view3_06 = (TextView) findViewById(R.id.tv_view3_06);
        tv_view3_07 = (TextView) findViewById(R.id.tv_view3_07);
        tv_view3_08 = (TextView) findViewById(R.id.tv_view3_08);
        tv_view3_09 = (TextView) findViewById(R.id.tv_view3_09);
        tv_view3_10 = (TextView) findViewById(R.id.tv_view3_10);
        initData();
    }

    public void initData() {
        mFragmentContainerHelper = new FragmentContainerHelper();
        mFragmentContainerHelper.handlePageSelected(0, true);
        title = new String[]{"全部", "年报", "中报", "一季报", "三季报"};
        lTitle = Arrays.asList(title);
        setIndicatorListen();
        Intent intent = getIntent();
        stockcode = intent.getStringExtra("stockcode");
        if (!TextUtils.isEmpty(stockcode)){
            getData(stockcode, "0");
        }
        iv_back.setOnClickListener(this);
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
//                simplePagerTitleView.setText(lTitle.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setClear();
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

    private void setClear() {
        tv_choose_title.setText("----");
        tv_view1_01.setText("--.--");
        tv_view1_02.setText("--.--");
        tv_view1_03.setText("--.--");
        tv_view1_04.setText("--.--");
        tv_view1_05.setText("--.--");
        tv_view1_06.setText("--.--");
        tv_view1_07.setText("--.--");
        tv_view1_08.setText("--.--");
        tv_view1_09.setText("--.--");
        tv_view1_10.setText("--.--");
        tv_view1_11.setText("--.--");
        tv_view1_12.setText("--.--");
        tv_view1_13.setText("--.--");
        tv_view1_14.setText("--.--");
//        tv_view1_15.setText("--.--");
//        tv_view1_16.setText("--.--");
//        tv_view1_17.setText("--.--");
//        tv_view1_18.setText("--.--");
        tv_view1_19.setText("--.--");
        tv_view1_20.setText("--.--");
        tv_view1_21.setText("--.--");
        tv_view1_22.setText("--.--");
        tv_view2_01.setText("--.--");
        tv_view2_02.setText("--.--");
        tv_view2_03.setText("--.--");
        tv_view2_04.setText("--.--");
        tv_view2_05.setText("--.--");
        tv_view2_06.setText("--.--");
        tv_view2_07.setText("--.--");
        tv_view2_08.setText("--.--");
        tv_view2_09.setText("--.--");
        tv_view2_10.setText("--.--");
        tv_view2_11.setText("--.--");
        tv_view2_12.setText("--.--");
        tv_view2_13.setText("--.--");
        tv_view2_14.setText("--.--");
        tv_view2_15.setText("--.--");
        tv_view3_01.setText("--.--");
        tv_view3_02.setText("--.--");
        tv_view3_03.setText("--.--");
        tv_view3_04.setText("--.--");
//        tv_view3_05.setText("--.--");
        tv_view3_06.setText("--.--");
        tv_view3_07.setText("--.--");
        tv_view3_08.setText("--.--");
        tv_view3_09.setText("--.--");
        tv_view3_10.setText("--.--");
    }

    private void getData(String stockcode, final String pager) {
        HashMap map100200 = new HashMap();
        map100200.put("funcid", "100206");
        map100200.put("token", "");
        HashMap map100200_1 = new HashMap();
        map100200_1.put("secucode", stockcode);
        map100200_1.put("querytype", pager);
        map100200.put("parms", map100200_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_NEW, map100200, new StringCallback() {
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
                    if (code.equals("0")) {
                        BalanceSheetEntity balanceSheetBean = new Gson().fromJson(dataArray.getString(0),BalanceSheetEntity.class);
                        setText(balanceSheetBean,pager);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setText(BalanceSheetEntity balanceSheetBean,String pager) {
        switch (pager) {
            case "0":
                tv_choose_title.setText(balanceSheetBean.ENDDATE.substring(0,4)+"全部");
                break;
            case "4":
                tv_choose_title.setText(balanceSheetBean.ENDDATE.substring(0,4)+"年报");
                break;
            case  "2":
                tv_choose_title.setText(balanceSheetBean.ENDDATE.substring(0,4)+"中报");
                break;
            case "1":
                tv_choose_title.setText(balanceSheetBean.ENDDATE.substring(0,4)+"一季报");
                break;
            case "3":
                tv_choose_title.setText(balanceSheetBean.ENDDATE.substring(0,4)+"三季报");
                break;
        }
        tv_view1_01.setText(TransitionUtils.long2million(balanceSheetBean.CASHEQUIVALENTS)+"元");
        tv_view1_02.setText(TransitionUtils.long2million(balanceSheetBean.CLIENTDEPOSIT)+"元");
        tv_view1_03.setText(TransitionUtils.long2million(balanceSheetBean.SETTLEMENTPROVI)+"元");
        tv_view1_04.setText(TransitionUtils.long2million(balanceSheetBean.CLIENTPROVI)+"元");
        tv_view1_05.setText(TransitionUtils.long2million(balanceSheetBean.TRADINGASSETS)+"元");
        tv_view1_06.setText(TransitionUtils.long2million(balanceSheetBean.DERIVATIVEASSETS)+"元");
        tv_view1_07.setText(TransitionUtils.long2million(balanceSheetBean.BOUGHTSELLBACKASSETS)+"元");
        tv_view1_08.setText(TransitionUtils.long2million(balanceSheetBean.INTERESTRECEIVABLE)+"元");
        tv_view1_09.setText(TransitionUtils.long2million(balanceSheetBean.ACCOUNTRECEIVABLE)+"元");
        tv_view1_10.setText(TransitionUtils.long2million(balanceSheetBean.REFUNDABLEDEPOSIT)+"元");
        tv_view1_11.setText(TransitionUtils.long2million(balanceSheetBean.HOLDFORSALEASSETS)+"元");
        tv_view1_12.setText(TransitionUtils.long2million(balanceSheetBean.HOLDTOMATURITYINVESTMENTS)+"元");
        tv_view1_13.setText(TransitionUtils.long2million(balanceSheetBean.LONGTERMEQUITYINVEST)+"元");
        tv_view1_14.setText(TransitionUtils.long2million(balanceSheetBean.FIXEDASSETS)+"元");
//        tv_view1_15.setText(TransitionUtils.long2million(balanceSheetBean.INTANGIBLEASSETS);
//        tv_view1_16.setText(TransitionUtils.long2million(balanceSheetBean.SEATCOSTS);
//        tv_view1_17.setText(TransitionUtils.long2million(balanceSheetBean.OTHERASSETS);
//        tv_view1_18.setText(TransitionUtils.long2million(balanceSheetBean.FIXEDASSETS);
        tv_view1_19.setText(TransitionUtils.long2million(balanceSheetBean.INTANGIBLEASSETS)+"元");
        tv_view1_20.setText(TransitionUtils.long2million(balanceSheetBean.SEATCOSTS)+"元");
        tv_view1_21.setText(TransitionUtils.long2million(balanceSheetBean.OTHERASSETS)+"元");
        tv_view1_22.setText(TransitionUtils.long2million(balanceSheetBean.TOTALASSETS)+"元");
        tv_view2_01.setText(TransitionUtils.long2million(balanceSheetBean.SHORTTERMLOAN)+"元");
        tv_view2_02.setText(TransitionUtils.long2million(balanceSheetBean.TRADINGLIABILITY)+"元");
        tv_view2_03.setText(TransitionUtils.long2million(balanceSheetBean.DERIVATIVELIABILITY)+"元");
        tv_view2_04.setText(TransitionUtils.long2million(balanceSheetBean.SOLDBUYBACKSECUPROCEEDS)+"元");
        tv_view2_05.setText(TransitionUtils.long2million(balanceSheetBean.PROXYSECUPROCEEDS)+"元");
        tv_view2_06.setText(TransitionUtils.long2million(balanceSheetBean.SUBISSUESECUPROCEEDS)+"元");
        tv_view2_07.setText(TransitionUtils.long2million(balanceSheetBean.ACCOUNTSPAYABLE)+"元");
        tv_view2_08.setText(TransitionUtils.long2million(balanceSheetBean.SALARIESPAYABLE)+"元");
        tv_view2_09.setText(TransitionUtils.long2million(balanceSheetBean.TAXSPAYABLE)+"元");
        tv_view2_10.setText(TransitionUtils.long2million(balanceSheetBean.INTERESTPAYABLE)+"元");
        tv_view2_11.setText(TransitionUtils.long2million(balanceSheetBean.ESTIMATELIABILITY)+"元");
        tv_view2_12.setText(TransitionUtils.long2million(balanceSheetBean.BONDSPAYABLE)+"元");
        tv_view2_13.setText(TransitionUtils.long2million(balanceSheetBean.DEFERREDTAXLIABILITY)+"元");
        tv_view2_14.setText(TransitionUtils.long2million(balanceSheetBean.OTHERLIABILITY)+"元");
        tv_view2_15.setText(TransitionUtils.long2million(balanceSheetBean.TOTALLIABILITY)+"元");
        tv_view3_01.setText(TransitionUtils.long2million(balanceSheetBean.PAIDINCAPITAL)+"元");
        tv_view3_02.setText(TransitionUtils.long2million(balanceSheetBean.CAPITALRESERVEFUND)+"元");
        tv_view3_03.setText(TransitionUtils.long2million(balanceSheetBean.SURPLUSRESERVEFUND)+"元");
        tv_view3_04.setText(TransitionUtils.long2million(balanceSheetBean.ORDINARYRISKRESERVEFUND)+"元");
//        tv_view3_05.setText(TransitionUtils.long2million(balanceSheetBean.RETAINEDPROFIT);
        tv_view3_06.setText(TransitionUtils.long2million(balanceSheetBean.RETAINEDPROFIT)+"元");
        tv_view3_07.setText(TransitionUtils.long2million(balanceSheetBean.SEWITHOUTMI)+"元");
        tv_view3_08.setText(TransitionUtils.long2million(balanceSheetBean.MINORITYINTERESTS)+"元");
        tv_view3_09.setText(TransitionUtils.long2million(balanceSheetBean.TOTALSHAREHOLDEREQUITY)+"元");
        tv_view3_10.setText(TransitionUtils.long2million(balanceSheetBean.TOTALLIABILITYANDEQUITY)+"元");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_balance_sheet;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
