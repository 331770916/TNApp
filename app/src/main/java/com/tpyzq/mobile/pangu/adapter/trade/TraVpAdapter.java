package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.activity.trade.BaseTraPager;

import java.util.List;

/**
 * Created by 陈新宇 on 2016/8/12.
 */
public class TraVpAdapter extends PagerAdapter {
    private List<BaseTraPager> pagers;

    public TraVpAdapter(List<BaseTraPager> pagers) {
        this.pagers = pagers;
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {

        return view == o;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseTraPager pager = pagers.get(position);
        container.addView(pager.rootView);
//        pager.initData(); //加载数据方法放在这，会导致提前调用，浪费流量
        return pager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}