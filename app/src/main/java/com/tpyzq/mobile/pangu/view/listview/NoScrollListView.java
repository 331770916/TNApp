package com.tpyzq.mobile.pangu.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by wangqi on 2016/8/5.
 * 屏蔽ListView 滑动
 */
public class NoScrollListView extends ListView {
    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context) {
        super(context);
    }

    @Override

    public boolean dispatchTouchEvent(MotionEvent ev) {
        //下面这句话是关键
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);//也有所不同哦

    }
}
