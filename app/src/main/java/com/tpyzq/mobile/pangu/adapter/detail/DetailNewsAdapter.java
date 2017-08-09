package com.tpyzq.mobile.pangu.adapter.detail;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.tpyzq.mobile.pangu.base.BasePager;

import java.util.List;


/**
 * 作者：刘泽鹏 on 2016/10/26 10:43
 * 详情页的  新闻公告研报 的 viewPager 的适配器
 */
public class DetailNewsAdapter extends PagerAdapter {

    private List<BasePager> pagers;

    public DetailNewsAdapter(List<BasePager> pagers) {
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
        BasePager pager = pagers.get(position);
        container.addView(pager.rootView);
//        pager.initData(); //加载数据方法放在这，会导致提前调用，浪费流量
        return pager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
