package com.tpyzq.mobile.pangu.activity.trade.currency_fund;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseSearchPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyOneMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyOneWeekPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyThreeMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyTodayPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyZiDingYiPager;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.adapter.trade.InquireVpAdapter;
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
 * 刘泽鹏
 * 货币基金委托查询界面
 */
public class CurrencyFundQueryActivity extends BaseActivity implements View.OnClickListener {
    private static final String[] buy_vp = new String[]{"今日", "一周内", "一月内", "三月内", "自定义"};
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<BaseSearchPager> pagers = new ArrayList<>();      //存储 View 的集合
    private MagicIndicator entrust_buy;
    private ViewPager mViewPager;

    @Override
    public void initView() {
        entrust_buy = (MagicIndicator) findViewById(R.id.entrust_buy);
        mViewPager = (ViewPager) findViewById(R.id.vpHBJJEntrustQuery);
        findViewById(R.id.ivHBJJWTCX_back).setOnClickListener(this);
        initData();
        setIndicatorListen();
    }

    private void initData() {
        pagers.add(new CurrencyTodayPager(this));
        pagers.add(new CurrencyOneWeekPager(this));
        pagers.add(new CurrencyOneMonthPager(this));
        pagers.add(new CurrencyThreeMonthPager(this));
        pagers.add(new CurrencyZiDingYiPager(this));
        mViewPager.setAdapter(new InquireVpAdapter(pagers));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pagers.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                        mViewPager.setCurrentItem(index);
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
        entrust_buy.setNavigator(commonNavigator);
        ViewPagerHelper.bind(entrust_buy, mViewPager);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_fund_query;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivHBJJWTCX_back) {
            finish();
        }
    }
}
