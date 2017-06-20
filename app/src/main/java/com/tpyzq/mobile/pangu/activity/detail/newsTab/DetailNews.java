package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.adapter.detail.DetailNewsAdapter;
import com.tpyzq.mobile.pangu.view.dealviewpager.NoScrollViewPager;
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
 * 作者：刘泽鹏 on 2016/10/26 09:55
 * 详情页  里面的   新闻公告研报  部分
 */
public class DetailNews extends BaseDetailNewsPager {

    private MagicIndicator magicIndicator;      //上边的  Tab
    private NoScrollViewPager mViewPager;               //下边的  ViewPager
    private static final String[] news_tab = new String[]{"新闻", "公告", "研报"};
    private List<String> news_tab_list;       //存储标题的 集合
    private List<BaseDetailNewsPager> viewList;      //存储view 的 集合
    private String stockCode;
    private DetailNewsAdapter adapter;

    public DetailNews(Context context, String stockCode) {
        super(context,stockCode);
    }



    @Override
    public void setView(String stockCode) {
        this.stockCode = stockCode;
        magicIndicator = (MagicIndicator) rootView.findViewById(R.id.mi_DetailNews);
        mViewPager = (NoScrollViewPager) rootView.findViewById(R.id.vp_DetailNews);
        initDatas();
        setIndicatorListen();
    }

    public void initDatas() {
        super.initData();
        news_tab_list = Arrays.asList(news_tab);
        viewList = new ArrayList<>();

        viewList.add(new NewsFragment(mContext,stockCode));              //"000972"
        viewList.add(new AnnouncementFragment(mContext,stockCode));      //"11601211"
        viewList.add(new StudyFragment(mContext,stockCode));             //"11601211"

        adapter= new DetailNewsAdapter(viewList);
        mViewPager.setAdapter(adapter);
    }


    /**
     * 设置底部页签切换监听器
     */
    private void setIndicatorListen() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return news_tab_list == null ? 0 : news_tab_list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(news_tab_list.get(index));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(mContext.getResources().getColor(R.color.blue));
                simplePagerTitleView.setText(news_tab_list.get(index));
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
                linePagerIndicator.setColors(mContext.getResources().getColor(R.color.blue));
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
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_news;
    }
}
