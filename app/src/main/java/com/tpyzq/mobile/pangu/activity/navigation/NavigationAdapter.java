package com.tpyzq.mobile.pangu.activity.navigation;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.tpyzq.mobile.pangu.base.BaseFragment;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/12/20.
 */

public class NavigationAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mFragments;

    public NavigationAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(ArrayList<BaseFragment> fragments) {
        mFragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public BaseFragment getItem(int arg0) {
        if (null != mFragments && mFragments.size() > 0) {
            return mFragments.get(arg0);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (null != mFragments && mFragments.size() > 0) {
            return mFragments.size();
        }
        return 0;
    }
}
