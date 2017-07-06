package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.activity.trade.view.BasePager;

import java.util.List;


/**
 * 作者：刘泽鹏 on 2016/8/24 14:47
 */
public class HBJJQueryAdapter extends PagerAdapter {
    private List<BasePager> pagers;
    public HBJJQueryAdapter(List<BasePager> pagers) {
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
        BasePager basePager = pagers.get(position);
        container.addView(basePager.rootView);
        return basePager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
