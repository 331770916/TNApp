package com.tpyzq.mobile.pangu.view.listview;

/**
 * Created by zhangwenbo on 2016/6/14.
 * 当发生了滚动事件时的实现类
 */
public class OnScrollChangedListenerImpl implements MyHScrollView.OnScrollChangedListener {

    private MyHScrollView mScrollViewArg;
    public OnScrollChangedListenerImpl(MyHScrollView scrollViewar) {
        mScrollViewArg = scrollViewar;
    }
    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        mScrollViewArg.smoothScrollTo(l, t);
    }
}
