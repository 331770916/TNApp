package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
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
 * Created by 陈新宇 on 2016/10/26.
 */
public class DetailView3 extends BaseStockDetailPager {
    private MagicIndicator mi_title;
    private FrameLayout fl_view;
    private String[] title;
    private List<String> lTitle;
    private List<BaseStockDetailPager> listBuy;
    private FragmentContainerHelper mFragmentContainerHelper;
    private String stockCode;

    public DetailView3(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mi_title = (MagicIndicator) rootView.findViewById(R.id.mi_title);
        fl_view = (FrameLayout) rootView.findViewById(R.id.fl_view);
    }

    @Override
    public void initData(String stockCode) {
        this.stockCode = stockCode.substring(2);
        mFragmentContainerHelper = new FragmentContainerHelper();
        mFragmentContainerHelper.handlePageSelected(0, true);
        title = new String[]{"资金", "简况", "财务", "股东"};
        lTitle = Arrays.asList(title);
        listBuy = new ArrayList<>();
        listBuy.add(new StockCapital(mContext));
        listBuy.add(new SimpleInfo(mContext));
        listBuy.add(new FinanceInfo(mContext));
        listBuy.add(new StockHolder(mContext));
        fl_view.addView(listBuy.get(0).rootView);
        listBuy.get(0).initData(this.stockCode);
        setIndicatorListen();
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
                return lTitle == null ? 0 : lTitle.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setText(lTitle.get(index));
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(mContext.getResources().getColor(R.color.blue));
//                simplePagerTitleView.setText(lTitle.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fl_view.removeAllViews();
                        mFragmentContainerHelper.handlePageSelected(index);
                        fl_view.addView(listBuy.get(index).rootView);
                        listBuy.get(index).initData(stockCode);
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
        mi_title.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(mi_title);
    }


    @Override
    public int getLayoutId() {
        return R.layout.pager_stockdetail_view;
    }
}
