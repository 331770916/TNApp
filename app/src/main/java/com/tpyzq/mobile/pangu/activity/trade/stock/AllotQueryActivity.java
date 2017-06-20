package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.AllotCustomTab;
import com.tpyzq.mobile.pangu.activity.trade.view.AllotMounthTab;
import com.tpyzq.mobile.pangu.activity.trade.view.AllotThreeMounthTab;
import com.tpyzq.mobile.pangu.activity.trade.view.AllotTodayTab;
import com.tpyzq.mobile.pangu.activity.trade.view.AllotWeekTab;
import com.tpyzq.mobile.pangu.activity.trade.view.BaseTab;
import com.tpyzq.mobile.pangu.adapter.trade.AllotQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.tabLineIndicator.TabLineIndicator;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/26.
 * 调拨记录
 */
public class AllotQueryActivity extends BaseActivity implements View.OnClickListener{

    private AllotTodayTab mTab1;
    private AllotWeekTab mTab2;
    private AllotMounthTab mTab3;
    private AllotThreeMounthTab mTab4;
    private ProgressBar mProgressBar;
    public static final String TAG = "AllotQuery";

    @Override
    public void initView() {
        findViewById(R.id.allotQuery_back).setOnClickListener(this);
        TabLineIndicator indicator = (TabLineIndicator) findViewById(R.id.allotQueryIndicator);
        ViewPager viewPager = (ViewPager) findViewById(R.id.allotQueryViewPager);
        AllotQueryAdapter adapter = new AllotQueryAdapter();
        viewPager.setAdapter(adapter);

        indicator.setViewPager(viewPager, 0);

        mProgressBar = (ProgressBar) findViewById(R.id.allotquery_progress);

        ArrayList<BaseTab> queryTabs = new ArrayList<>();
        mTab1 = new AllotTodayTab(this, queryTabs);
        mTab2= new AllotWeekTab(this, queryTabs);
        mTab3 = new AllotMounthTab(this, queryTabs);
        mTab4 = new AllotThreeMounthTab(this, queryTabs);
        AllotCustomTab tab5 = new AllotCustomTab(this, queryTabs);

        mTab1.setViewPager(viewPager);
        mTab2.setViewPager(viewPager);
        mTab3.setViewPager(viewPager);
        mTab4.setViewPager(viewPager);
        tab5.setViewPager(viewPager);

        ArrayList<BaseTab> tabs = new ArrayList<>();
        tabs.add(mTab1);
        tabs.add(mTab2);
        tabs.add(mTab3);
        tabs.add(mTab4);
        tabs.add(tab5);

        adapter.setDatas(tabs);
    }

    public void loadingProgress() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    public void complitedProgress() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTab1.onResuem();
        mTab2.onResuem();
        mTab3.onResuem();
        mTab4.onResuem();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mTab1.onStop();
        mTab2.onStop();
        mTab3.onStop();
        mTab4.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTab1.onDestory();
        mTab2.onDestory();
        mTab3.onDestory();
        mTab4.onDestory();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_allotquery;
    }
}
