package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by 刘泽鹏 on 2016/8/11.
 */
public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> list_fragment;                         //fragment列表
    private FragmentManager fm;
    private  List<String> list_Title;

    public TabAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.fm = fm;
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }


    @Override
    public Fragment getItem(int position) {
        if (list_fragment != null && list_fragment.size() > 0) {
            return list_fragment.get(position);
        }
        return null;

    }

    @Override
    public int getCount() {
        if (list_fragment != null && list_fragment.size() > 0) {
            return list_fragment.size();
        }
        return 0;
    }

    public void setFragments(List fragments, List<String> list_Title) {
        if (this.list_fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.list_fragment) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.list_fragment = fragments;
        notifyDataSetChanged();
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }
}
