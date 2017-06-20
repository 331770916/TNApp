package com.tpyzq.mobile.pangu.view.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by zhangwenbo on 2016/10/31.
 */
public class MyScrollView extends ScrollView {
    private OnScrollListener mListener;

    /**
     * 设置滑动距离监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 滑动距离监听器


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mListener != null) {
            mListener.onScroll(getScrollY());
        }
    }

    /**
     * 在滑动的时候调用，scrollY为已滑动的距离
     */
    public interface OnScrollListener{
        void onScroll(int scrollY);
    }

}
