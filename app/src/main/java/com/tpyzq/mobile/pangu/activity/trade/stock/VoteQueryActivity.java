package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.activity.trade.view.VoteQueryPager;
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
 * 投票查询
 */

public class VoteQueryActivity extends BaseActivity implements View.OnClickListener {
    private VoteQueryPager  oneWeekPager, inAMonthPager, threeWeekPager, customPager;
    private String[] buy_vp = new String[]{ "一周内", "一月内", "三月内", "自定义"};
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<BasePager> listBuy = new ArrayList<>();

    public static final int REQUSET = 2;
    private int mPoint = -1;

    private TextView mShareholderCode_tv;
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;

    @Override
    public void initView() {
        findViewById(R.id.detail_back).setOnClickListener(this);
        mShareholderCode_tv = (TextView) findViewById(R.id.tvShareholderCode);
        magicIndicator = (MagicIndicator) findViewById(R.id.capital_buy);
        viewPager = (ViewPager) findViewById(R.id.fj_view);
        magicIndicator.setOnClickListener(this);
        mShareholderCode_tv.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote_query;
    }


    @Override
    protected void onStart() {
        super.onStart();
        setIndicatorListen();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_back:
                finish();
                break;
            case R.id.tvShareholderCode:
                Intent intent =new Intent();
                intent.setClass(this, FJFundChooseActivity.class);
                intent.putExtra("point", mPoint);
                intent.putExtra("tag",1);
                startActivityForResult(intent, REQUSET);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUSET && resultCode == RESULT_OK) {
            mPoint = intent.getIntExtra("point", -1);
            mShareholderCode_tv.setText(intent.getStringExtra("Code"));
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

        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);//TAG VoteQueryTodayPager...
        oneWeekPager = new VoteQueryPager(this, "VoteQueryOneWeekPager");
        listBuy.add(oneWeekPager);
        inAMonthPager = new VoteQueryPager(this, "VoteQueryInAMonthPager");
        listBuy.add(inAMonthPager);
        threeWeekPager = new VoteQueryPager(this, "VoteQueryThreeWeekPager");
        listBuy.add(threeWeekPager);
        customPager = new VoteQueryPager(this, "VoteQueryCustomPager");
        listBuy.add(customPager);

        viewPager.setAdapter(new FJEntrustedDealAdapter(listBuy, 0));
        oneWeekPager.initData();
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
