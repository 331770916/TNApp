package com.tpyzq.mobile.pangu.adapter.home;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * anthor:Created by tianchen on 2017/5/15.
 * email:963181974@qq.com
 * 首页轮播图ViewPager的Adapter
 */

public class CarouselAdapter extends PagerAdapter{
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
