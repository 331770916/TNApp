package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * 作者：刘泽鹏 on 2016/10/30 16:09
 * OTC 查询界面的 viewPager  适配器
 */
public class OtcQueryAdapter extends FragmentPagerAdapter {

    private List<Fragment> pagers;
    String[] titles = {"今日", "一周内", "一月内", "三月内", "自定义"};

    public OtcQueryAdapter(FragmentManager fm, List<Fragment> pagers) {
        super(fm);
        this.pagers = pagers;
    }

    @Override
    public Fragment getItem(int position) {

        if(pagers != null && pagers.size()>0){
            return pagers.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if(pagers != null && pagers.size()>0){
            return pagers.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
