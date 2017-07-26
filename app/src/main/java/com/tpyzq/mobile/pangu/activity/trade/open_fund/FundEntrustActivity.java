package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseTransactionPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundEntrustCustomPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundEntrustMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundEntrustThreeMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundEntrustTodayPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FundEntrustWeekPager;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.adapter.trade.StockVpAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金委托
 */
public class FundEntrustActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private MagicIndicator mi_date;
    private ViewPager vp_view;
    private static final String[] buy_vp = new String[]{"今日", "一周内", "一月内", "三月内", "自定义"};
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<BaseTransactionPager> listBuy = new ArrayList<>();
    private Map<Integer,Boolean> isLoading = new HashMap<>();

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
        FundEntrustTodayPager entrustTodayPager = new FundEntrustTodayPager(this);   // 初始化第一个界面数据
        entrustTodayPager.setRefresh();
        isLoading.put(0,true);
        listBuy.add(entrustTodayPager);
        listBuy.add(new FundEntrustWeekPager(this));
        listBuy.add(new FundEntrustMonthPager(this));
        listBuy.add(new FundEntrustThreeMonthPager(this));
        listBuy.add(new FundEntrustCustomPager(this));
        vp_view.setAdapter(new StockVpAdapter(listBuy));
        for (int i = 1; i < listBuy.size(); i++) {
            isLoading.put(i,false);
        }
    }

    /**
     * 设置底部页签切换监听器
     */
    private void setIndicatorListen() {
        vp_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {   // 添加viewPager 监听 实现数据第一次加载
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!isLoading.get(position)){
                    isLoading.put(position,true);
                    listBuy.get(position).setRefresh();
                    listBuy.get(position);
                }else {
                    listBuy.get(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                simplePagerTitleView.setNormalColor(ColorUtils.BLACK);
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
        return R.layout.activity_fund_entrust;
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
