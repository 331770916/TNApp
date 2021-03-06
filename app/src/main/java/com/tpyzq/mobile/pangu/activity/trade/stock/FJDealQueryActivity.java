package com.tpyzq.mobile.pangu.activity.trade.stock;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.FJEntrustDealQueryPager;
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
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: deal query 成交查询1
 */

public class FJDealQueryActivity  extends BaseActivity implements View.OnClickListener{
    private FJEntrustDealQueryPager todayPager,oneWeekPager,inAMonthPager,threeWeekPager,customPager;
    private String[] buy_vp = new String[]{"今日", "一周内", "一月内", "三月内","自定义"};
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<BasePager> listBuy = new ArrayList<>();
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;
    private TextView title;
    public static FragmentManager fragmentManager;


    @Override
    public void initView() {
        fragmentManager = getFragmentManager();
        findViewById(R.id.ivCNFund_back).setOnClickListener(this);
        title =(TextView)this.findViewById(R.id.fj_title);
        title.setText("成交查询");
        magicIndicator = (MagicIndicator)this.findViewById(R.id.capital_buy);
        viewPager = (ViewPager)this.findViewById(R.id.fj_view);
        setIndicatorListen();
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
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);//TAG  DealTodayPager....
        todayPager = new FJEntrustDealQueryPager(this,"DealTodayPager");
        listBuy.add(todayPager);
        oneWeekPager = new FJEntrustDealQueryPager(this,"DealOneWeekPager");
        listBuy.add(oneWeekPager);
        inAMonthPager = new FJEntrustDealQueryPager(this,"DealInAMonthPager");
        listBuy.add(inAMonthPager);
        threeWeekPager = new FJEntrustDealQueryPager(this,"DealThreeWeekPager");
        listBuy.add(threeWeekPager);
        customPager = new FJEntrustDealQueryPager(this,"DealCustomPager");
        listBuy.add(customPager);
        viewPager.setAdapter(new FJEntrustedDealAdapter(listBuy,1));
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
    public void onClick(View v) {
        finish();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(!listBuy.isEmpty()){
            for (BasePager pager:listBuy)
                ((FJEntrustDealQueryPager)pager).setIsFirst(true);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_fj_entrus_query;
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
        threeWeekPager=null;
        customPager.destroy();
        customPager = null;
    }
}
