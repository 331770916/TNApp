package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.tpyzq.mobile.pangu.activity.home.information.view.HotPintsPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.ListPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.SopCastPager;
import com.tpyzq.mobile.pangu.base.BasePager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hackware on 2016/9/10.
 */

public class InfoPagerAdapter extends PagerAdapter {
    private List<String> mDataList,mClassnoList;
    private Map<String,BasePager> cache;
    private Context context ;

    public InfoPagerAdapter(List<String> dataList,Context context) {
        mDataList = dataList;
        this.context = context;
        cache = new HashMap();
    }

    public void setClassNoList(List<String> classNoList){
        this.mClassnoList = classNoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager pager;
        String title = mDataList.get(position);
        String classno = mClassnoList.get(position);
        switch (title){
            case "要闻":
                pager = new HotPintsPager(context,"1");
                break;
            case "直播":
                pager = new SopCastPager(context,"2");
                break;
            default:
                pager = new ListPager(context,classno);
                break;
        }
        cache.put(title,pager);
        container.addView(pager.rootView);
        return pager.rootView;
    }

    public BasePager getPager(String title){
        return cache.get(title);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }
}
