package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhangwenbo on 2017/7/28.
 * 热卖adapter
 */

public class HotSellAdapter extends FragmentPagerAdapter {

    private List<Fragment> pagers;
    String[] titles = {"推荐", "稳赢"};

    public HotSellAdapter(FragmentManager fm, List<Fragment> pagers) {
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
