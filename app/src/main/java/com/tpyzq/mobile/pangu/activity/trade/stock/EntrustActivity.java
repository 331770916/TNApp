package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseSearchPager;
import com.tpyzq.mobile.pangu.activity.trade.view.EntrustCustomPager;
import com.tpyzq.mobile.pangu.activity.trade.view.EntrustInAMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.EntrustOneWeekPager;
import com.tpyzq.mobile.pangu.activity.trade.view.EntrustThreeWeekPager;
import com.tpyzq.mobile.pangu.activity.trade.view.EntrustTodayPager;
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
 * Created by wangqi on 2016/7/28.
 * 委托查询  页面
 */
public class EntrustActivity extends BaseActivity implements View.OnClickListener {
    private static final String[] buy_vp = new String[]{"今日", "一周内", "一月内", "三月内", "自定义"};
    private ViewPager mWtViewPager;
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<BaseSearchPager> listBuy = new ArrayList<>();
    private MagicIndicator entrust_buy;

    @Override
    public void initView() {
        mWtViewPager = (ViewPager) findViewById(R.id.wt_view);
        entrust_buy = (MagicIndicator) findViewById(R.id.entrust_buy);
        findViewById(R.id.publish_detail_back).setOnClickListener(this);
        setIndicatorListen();        //设置indicator指针
        initData();
    }


    private void initData() {
        listBuy.add(new EntrustTodayPager(this));
        listBuy.add(new EntrustOneWeekPager(this));
        listBuy.add(new EntrustInAMonthPager(this));
        listBuy.add(new EntrustThreeWeekPager(this));
        listBuy.add(new EntrustCustomPager(this));
        mWtViewPager.setAdapter(new InquireVpAdapter(listBuy));

        mWtViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                listBuy.get(position);
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
                        mWtViewPager.setCurrentItem(index);
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
        ViewPagerHelper.bind(entrust_buy, mWtViewPager);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.publish_detail_back) {
            finish();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_entrust;
    }


}