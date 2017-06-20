package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.activity.trade.BaseSearchPager;

import java.util.List;


/**
 * Created by wangqi on 2016/8/12.
 * 查询  标签Adapter
 */
public class InquireVpAdapter extends PagerAdapter {
    private List<BaseSearchPager> pagers;
    public InquireVpAdapter(List<BaseSearchPager> pagers) {
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
        BaseSearchPager pager = pagers.get(position);
        container.addView(pager.rootView);
//			pager.initData(); //加载数据方法放在这，会导致提前调用，浪费流量
        return pager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}