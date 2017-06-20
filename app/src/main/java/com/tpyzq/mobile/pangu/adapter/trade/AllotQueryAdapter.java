package com.tpyzq.mobile.pangu.adapter.trade;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.activity.trade.view.BaseTab;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/9/6.
 */
public class AllotQueryAdapter extends PagerAdapter {

    private ArrayList<BaseTab> mDatas;

    public void setDatas(ArrayList<BaseTab> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mDatas.get(position).getContentView());
        return mDatas.get(position).getContentView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
