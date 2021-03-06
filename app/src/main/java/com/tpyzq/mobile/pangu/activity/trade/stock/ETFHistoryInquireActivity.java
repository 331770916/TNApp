package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.ETFQueryPager;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.adapter.trade.FJEntrustedDealAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.BasePager;
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
 * Created by wangqi on 2017/7/3.
 * ETF 申赎历史查询
 */

public class ETFHistoryInquireActivity extends BaseActivity implements View.OnClickListener {
    private ETFQueryPager todayPager, oneWeekPager, inAMonthPager, threeWeekPager, customPager;
    private String[] buy_vp = new String[]{"今日", "一周内", "一月内", "三月内", "自定义"};
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<BasePager> listBuy = new ArrayList<>();
    private MagicIndicator capital_buy;
    private ViewPager viewPager;
    private boolean isTrue;
    public static FragmentManager fragmentManager;

    @Override
    public void initView() {
        fragmentManager = getFragmentManager();
        findViewById(R.id.ivCNFund_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.fj_title)).setText(getString(R.string.ETF_shenshu_History));
        capital_buy = (MagicIndicator) findViewById(R.id.capital_buy);
        viewPager = (ViewPager) findViewById(R.id.fj_view);
        setIndicatorListen();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fj_entrus_query;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCNFund_back:
                finish();
                break;
        }
    }

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
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.bkColor));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.blue));
                simplePagerTitleView.setText(buy_vp_list.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
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
                return 1.0f;
            }
        });

        capital_buy.setNavigator(commonNavigator);
        ViewPagerHelper.bind(capital_buy, viewPager);//TAG ETFTodayPager...
        todayPager = new ETFQueryPager(this, "ETFQueryTodayPager");
        listBuy.add(todayPager);
        oneWeekPager = new ETFQueryPager(this, "ETFQueryOneWeekPager");
        listBuy.add(oneWeekPager);
        inAMonthPager = new ETFQueryPager(this, "ETFQueryInAMonthPager");
        listBuy.add(inAMonthPager);
        threeWeekPager = new ETFQueryPager(this, "ETFQueryThreeWeekPager");
        listBuy.add(threeWeekPager);
        customPager = new ETFQueryPager(this, "ETFQueryCustomPager");
        listBuy.add(customPager);


        viewPager.setAdapter(new FJEntrustedDealAdapter(listBuy));
        todayPager.initData();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                listBuy.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void destroy() {
        todayPager.destroy();
        todayPager = null;
        oneWeekPager.destroy();
        oneWeekPager = null;
        inAMonthPager.destroy();
        inAMonthPager = null;
        threeWeekPager.destroy();
        threeWeekPager = null;
        customPager.destroy();
        customPager = null;
    }
}
