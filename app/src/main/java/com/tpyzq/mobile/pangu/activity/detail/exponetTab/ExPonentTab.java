package com.tpyzq.mobile.pangu.activity.detail.exponetTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.quotation.StockListActivity;
import com.tpyzq.mobile.pangu.activity.market.quotation.StockListIntent;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.adapter.detail.ExponentAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.doConnect.detail.ExponentConnect;
import com.tpyzq.mobile.pangu.http.doConnect.detail.ToExponentConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.tpyzq.mobile.pangu.view.magicindicator.FragmentContainerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/10/26.
 * 指数详情指数tab
 */
public class ExPonentTab extends BaseExponentTab implements ICallbackResult, AdapterView.OnItemClickListener, ExponentConnect.ExpontentConnectInterface {

    private Activity mActivity;
    private static final String TAG = "ExPonentTab";
    private ExponentAdapter mAdapter;
    public static final String ZF_TYPE = "1000";
    public static final String DF_TYPE = "2000";
    public static final String HS_TYPE = "3000";
    private ArrayList<StockInfoEntity> mBeans;
    private String _exponentCode;
    private View footerView;
    private int mIndex;

    /**
     * 指数代码传6位
     * @param activity
     * @param exponentCode
     */
    public ExPonentTab(Activity activity, String exponentCode) {
        super(activity, exponentCode);
        mActivity = activity;
    }

    @Override
    public void initView(View view, final Activity activity, final String exponentCode) {
        mActivity = activity;
        try
        {


        if (TextUtils.isEmpty(exponentCode)) {
            return;
        }

        int length = exponentCode.length();
         _exponentCode = exponentCode;
        if (length > 6) {
            _exponentCode = Helper.getStockNumber(_exponentCode);
        }

        MagicIndicator tabLineDisClickIndicator = (MagicIndicator) view.findViewById(R.id.detailExponentDisClickIndicator);
        String [] strings = {"成份股涨幅", "成份股跌幅", "成份股换手率"};

        List<String> titleList = Arrays.asList(strings);

        footerView = LinearLayout.inflate(activity, R.layout.footer_exponent, null);
        MyListView listView = (MyListView) view.findViewById(R.id.detailExponentListView);
        mAdapter = new ExponentAdapter();
        listView.addFooterView(footerView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    StockListIntent data = new StockListIntent();
                    data.setHead2("现价");
                    data.setHead3("涨跌幅");
                    data.setHead1("股票名称");

                    intent.putExtra("tag","exponent");
                    intent.putExtra("stockIntent", data);
                    intent.putExtra("isExponent", true);

                    if (mIndex == 0) {
                        intent.putExtra("flag", "1");
                        intent.putExtra("asc", "1");
                        intent.putExtra("type", "1000");
                    } else if (mIndex == 1) {
                        intent.putExtra("flag", "1");
                        intent.putExtra("asc", "2");
                        intent.putExtra("type", "2000");
                        intent.putExtra("order", "2");
                    } else {
                        data.setHead3("换手率");
                        intent.putExtra("flag", "2");
                        intent.putExtra("asc", "1");
                        intent.putExtra("type", "3000");
                    }
                    intent.putExtra("exponentCode", _exponentCode);
                    intent.setClass(activity, StockListActivity.class);
                    activity.startActivity(intent);
                }
            });


        final SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
        simpleRemoteControl.setCommand(new ToExponentConnect(new ExponentConnect("0", "10", TAG, "1", "1", _exponentCode, ZF_TYPE, this)));
        simpleRemoteControl.startConnect();


        FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper();
        fragmentContainerHelper.handlePageSelected(0, false);
        setIndicatorListen(titleList ,tabLineDisClickIndicator, simpleRemoteControl, _exponentCode, fragmentContainerHelper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String stockName ="";
        String stockNumber = "";

        if (mBeans != null && mBeans.size() > 0) {
            stockName = mBeans.get(position).getStockName();
            stockNumber = mBeans.get(position).getStockNumber();
        }

        if (!TextUtils.isEmpty(stockName) && !TextUtils.isEmpty(stockNumber)) {
            Intent intent = new Intent();
            StockDetailEntity data = new StockDetailEntity();
            data.setStockName(stockName);
            data.setStockCode(stockNumber);
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, StockDetailActivity.class);
            mActivity.startActivity(intent);
        }
    }

    @Override
    public void getResult(Object result, String tag) {
        mBeans = (ArrayList<StockInfoEntity>) result;

        if (mBeans != null && mBeans.size() > 0) {
            mAdapter.setData(mBeans);

            if (mBeans.size() < 10) {
                TextView textView = (TextView) footerView.findViewById(R.id.footerMore);
                textView.setText("没有更多啦");
                textView.setTextColor(ContextCompat.getColor(mActivity, R.color.newStockTitle));
                footerView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bgColor));
                footerView.setClickable(false);
                footerView.setFocusable(false);
                footerView.setFocusableInTouchMode(false);
            }
        }



    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showEmpty() {

    }

    private void setIndicatorListen(final List<String> titlelist, final MagicIndicator magicIndicator, final SimpleRemoteControl simpleRemoteControl, final String exponentCode, final FragmentContainerHelper fragmentContainerHelper) {
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);

        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titlelist == null ? 0 : titlelist.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titlelist.get(index));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
                simplePagerTitleView.setText(titlelist.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIndex = index;
                        fragmentContainerHelper.handlePageSelected(index);
                        switch (index) {
                            case 0:
                                simpleRemoteControl.setCommand(new ToExponentConnect(new ExponentConnect("0", "10", TAG, "1", "1", exponentCode, ZF_TYPE, ExPonentTab.this)));
                                simpleRemoteControl.startConnect();
                                break;
                            case 1:
                                simpleRemoteControl.setCommand(new ToExponentConnect(new ExponentConnect("0", "10", TAG, "1", "2", exponentCode, DF_TYPE, ExPonentTab.this)));
                                simpleRemoteControl.startConnect();
                                break;
                            case 2:
                                simpleRemoteControl.setCommand(new ToExponentConnect(new ExponentConnect("0", "10", TAG, "2", "1", exponentCode, HS_TYPE, ExPonentTab.this)));
                                simpleRemoteControl.startConnect();
                                break;
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
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

        magicIndicator.setNavigator(commonNavigator);

        fragmentContainerHelper.attachMagicIndicator(magicIndicator);

    }

    @Override
    public int getLayotId() {
        return R.layout.tab_detail_exponent;
    }
}
