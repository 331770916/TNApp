package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseTransactionPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundHistoryCustomPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundHistoryMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundHistoryThreeMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundHistoryTodayPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundHistoryWeekPager;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.adapter.trade.StockVpAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.ViewPagerHelper;
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
 * 历史成交
 */
public class FundHistoryActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    MagicIndicator mi_date;
    ViewPager vp_view;
    private static final String[] buy_vp = new String[]{"今日", "一周内", "一月内", "三月内", "自定义"};
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<BaseTransactionPager> listBuy = new ArrayList<>();
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mi_date = (MagicIndicator) findViewById(R.id.mi_date);
        vp_view = (ViewPager) findViewById(R.id.vp_view);
        initData();
        setIndicatorListen();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        listBuy.add(new FundHistoryTodayPager(this));
        listBuy.add(new FundHistoryWeekPager(this));
        listBuy.add(new FundHistoryMonthPager(this));
        listBuy.add(new FundHistoryThreeMonthPager(this));
        listBuy.add(new FundHistoryCustomPager(this));
        vp_view.setAdapter(new StockVpAdapter(listBuy));
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
                return buy_vp_list == null ? 0 : buy_vp_list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(buy_vp_list.get(index));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.blue));
                simplePagerTitleView.setText(buy_vp_list.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vp_view.setCurrentItem(index);
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
        mi_date.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mi_date, vp_view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_history;
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
