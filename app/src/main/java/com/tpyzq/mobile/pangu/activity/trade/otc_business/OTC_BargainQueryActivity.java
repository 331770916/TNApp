package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.BasePager;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainCustom;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainOneMonth;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainOneWeek;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainThreeMonth;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainToday;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.adapter.trade.OtcQueryAdapter;
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
 * OTC 成交查询界面
 * 刘泽鹏
 */
public class OTC_BargainQueryActivity extends BaseActivity implements View.OnClickListener {

    private MagicIndicator mi_OtcBargainQuery;      //title
    private static final String[] bargainQuery_tab = new String[]{"今日", "一周内", "一月内", "三月内", "自定义"};
    private List<String> bargainQuery_tab_list;       //存储标题的 集合
    private ViewPager mViewPager;
    private List<BasePager> pagers=new ArrayList<BasePager>();
    private OtcQueryAdapter adapter;

    @Override
    public void initView() {

        mi_OtcBargainQuery = (MagicIndicator) this.findViewById(R.id.mi_OtcBargainQuery);
        mViewPager = (ViewPager) findViewById(R.id.vpOTCBargainQuery);
        findViewById(R.id.ivOTC_BargainQueryBack).setOnClickListener(this);

        initData();
        setIndicatorListen();
    }

    private void initData() {

        bargainQuery_tab_list = Arrays.asList(bargainQuery_tab);

        pagers.add(new OTC_BargainToday(this));
        pagers.add(new OTC_BargainOneWeek(this));
        pagers.add(new OTC_BargainOneMonth(this));
        pagers.add(new OTC_BargainThreeMonth(this));
        pagers.add(new OTC_BargainCustom(this));

        adapter = new OtcQueryAdapter(pagers);
        mViewPager.setAdapter(adapter);

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


    /**
     * 设置底部页签切换监听器
     */
    private void setIndicatorListen() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return bargainQuery_tab_list == null ? 0 : bargainQuery_tab_list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(bargainQuery_tab_list.get(index));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(OTC_BargainQueryActivity.this.getResources().getColor(R.color.blue));
                simplePagerTitleView.setText(bargainQuery_tab_list.get(index));
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
                linePagerIndicator.setColors(OTC_BargainQueryActivity.this.getResources().getColor(R.color.blue));
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
        mi_OtcBargainQuery.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mi_OtcBargainQuery, mViewPager);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__bargain_query;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivOTC_BargainQueryBack:
                finish();
                break;
        }
    }
}
