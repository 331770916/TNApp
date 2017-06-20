package com.tpyzq.mobile.pangu.view.dealviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wangqi on 2016/5/27.
 *   1
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;// 去掉ViewPager默认的滑动效果
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;// 去掉拦截事件的功能
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //viewpager自适应高度
//
//        int height = 0;
//        //下面遍历所有child的高度
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (h > height) //采用最大的view的高度。
//                height = h;
//        }
//
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
//                MeasureSpec.EXACTLY);
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

}
