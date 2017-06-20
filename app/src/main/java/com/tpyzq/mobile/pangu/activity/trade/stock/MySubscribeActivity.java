package com.tpyzq.mobile.pangu.activity.trade.stock;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.StayPaymentFragment;
import com.tpyzq.mobile.pangu.activity.trade.SubscribeHistoryFragment;
import com.tpyzq.mobile.pangu.adapter.trade.TabAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的申购界面
 */
public class MySubscribeActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imageView_back=null;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<Fragment>();//页卡视图集合
    @Override
    public void initView() {
        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_subscribe;
    }

    private void init() {
        tabLayout= (TabLayout) this.findViewById(R.id.tabLayout);
        mViewPager= (ViewPager) this.findViewById(R.id.mySubscribe_viewPager);
        StayPaymentFragment stayPaymentFragment = new StayPaymentFragment();    //实例化待缴费 Fragment
        SubscribeHistoryFragment subscribeHistoryFragment = new SubscribeHistoryFragment();     //实例化历史Fragment
        mViewList.add(stayPaymentFragment);
        mViewList.add(subscribeHistoryFragment);
        mTitleList.add("待缴款");
        mTitleList.add("申购历史");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));    //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));    //为TabLayout添加tab名称
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(),mViewList,mTitleList);   //初始化适配器
        mViewPager.setAdapter(tabAdapter);   //适配
        tabLayout.setupWithViewPager(mViewPager);       //将TabLayout和ViewPager关联起来
        tabLayout.setTabsFromPagerAdapter(tabAdapter);      //给Tabs设置适配器

        this.imageView_back= (ImageView) this.findViewById(R.id.activityNewStock_back);
        this.imageView_back.setOnClickListener(this);       //点击返回图片
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activityNewStock_back:
                this.finish();
                break;
        }
    }
}
