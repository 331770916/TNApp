package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.activity.trade.view.BasePager;

import java.util.List;


/**
 * 作者：刘泽鹏 on 2016/10/30 16:09
 * OTC 查询界面的 viewPager  适配器
 */
public class OtcQueryAdapter extends PagerAdapter {

    private List<BasePager> pagers;

    public OtcQueryAdapter(List<BasePager> pagers) {
        this.pagers = pagers;
    }

    @Override
    public int getCount() {
        if(pagers != null && pagers.size()>0){
            return pagers.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
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
