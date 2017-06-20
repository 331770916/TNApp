package com.tpyzq.mobile.pangu.adapter.home;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.activity.home.information.view.BaseInfoPager;
import com.tpyzq.mobile.pangu.log.LogUtil;

import java.util.List;


/**
 * Created by chenxy on 2016/8/11.
 * 处理资讯主页的adapter
 */

public class PagerInfoAdapter extends PagerAdapter {
    private List<BaseInfoPager> pagers;

    public PagerInfoAdapter(List<BaseInfoPager> pagers) {
        this.pagers = pagers;
    }

    public void setBaseInfoPager(List<BaseInfoPager> pagers) {
        this.pagers = pagers;
        notifyDataSetChanged();
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
        LogUtil.e("adapter","-----------"+position);
        BaseInfoPager pager = pagers.get(position);
        container.addView(pager.rootView);
        if (position ==  0){
            pager.initData(); //加载数据方法放在这，会导致提前调用，浪费流量
        }
        return pager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
