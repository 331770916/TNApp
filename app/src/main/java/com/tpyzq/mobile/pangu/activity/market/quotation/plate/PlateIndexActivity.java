package com.tpyzq.mobile.pangu.activity.market.quotation.plate;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.QuotaionAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.tabLineIndicator.TabLineIndicator;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/7/1.
 * 板塊列表頁面
 */
public class PlateIndexActivity extends BaseActivity implements View.OnClickListener, TabLineIndicator.PageOnChangeListener, BaseTabPager.LoadInterface {
    private String [] mTitles = { "行业板块", "概念板块", "地域板块"};
    private TextView mTitleTv;
    private IndustryExpoentTab2 mIndustryPlate;
    private IndustryExpoentTab2 mConceptPlate;
    private IndustryExpoentTab2 mTerritoryPlate;
    private ProgressBar mLoadingProgress;
    private ArrayList<BaseTabPager> mTabs;
   // 1.行业2.板块3.地域
//    private final String[] busType = {"1","2","3"};
    @Override
    public void initView() {

        Intent intent = getIntent();
        int pageIndex = intent.getIntExtra("pageIndex", 0);

        findViewById(R.id.plateindex_back).setOnClickListener(this);

        mLoadingProgress = (ProgressBar) findViewById(R.id.plateindex_progress);
        mTitleTv = (TextView) findViewById(R.id.plateindex_title);

        TabLineIndicator tabPageIndicator = (TabLineIndicator) findViewById(R.id.plateIndicator);
        ViewPager viewPager = (ViewPager) findViewById(R.id.plate_viewPager);

        QuotaionAdapter adapter = new QuotaionAdapter();
        mTabs = new ArrayList<>();
        ArrayList<BaseTabPager> tabs1 = new ArrayList<>();

        mIndustryPlate = new IndustryExpoentTab2(this, tabs1,"1");
        mConceptPlate  = new IndustryExpoentTab2(this, tabs1,"2");
        mTerritoryPlate = new IndustryExpoentTab2(this, tabs1,"3");

        mIndustryPlate.setLoadListener(this);
        mConceptPlate.setLoadListener(this);
        mTerritoryPlate.setLoadListener(this);

        mTabs.add(mIndustryPlate);
        mTabs.add(mConceptPlate);
        mTabs.add(mTerritoryPlate);

        adapter.setDatas(mTabs);

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(pageIndex);
        mTitleTv.setText(mTitles[pageIndex]);

        mIndustryPlate.setViewPager(viewPager);
        mConceptPlate.setViewPager(viewPager);
        mTerritoryPlate.setViewPager(viewPager);

        tabPageIndicator.setViewPager(viewPager, pageIndex);
        tabPageIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTitleTv.setText(mTitles[position]);
//        mTabs.get(position).onResume();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void loading() {
        mLoadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void complited() {
        mLoadingProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIndustryPlate.onResume();
        mConceptPlate.onResume();
        mTerritoryPlate.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mIndustryPlate.onStop();
        mConceptPlate.onStop();
        mTerritoryPlate.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mIndustryPlate.onDestory();
        mConceptPlate.onDestory();
        mTerritoryPlate.onDestory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plateindex_back:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_plateindex;
    }
}
