package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.tpyzq.mobile.pangu.base.BasePager;

import java.util.List;

/**
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:
 */

public class FJEntrustedDealAdapter extends PagerAdapter {

    private List<BasePager> pagers;

    public FJEntrustedDealAdapter(List<BasePager> pagers){
        this.pagers = pagers;
    }

    public FJEntrustedDealAdapter(List<BasePager> pagers,int type) {
        this.pagers = pagers;
        for (int i = 0;i < pagers.size();i++)
            pagers.get(i).setType(type);
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
        return pager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
