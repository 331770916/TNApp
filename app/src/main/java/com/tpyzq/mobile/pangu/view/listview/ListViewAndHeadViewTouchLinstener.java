package com.tpyzq.mobile.pangu.view.listview;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


/**
 * Created by zhangwenbo on 2016/6/14.
 */
public class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

    private LinearLayout mHeadLayout;
    private int mId;

    public ListViewAndHeadViewTouchLinstener(LinearLayout headLayout, int id) {
        mHeadLayout = headLayout;
        mId = id;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 当在列头 和 listView控件上touch时，将这个touch的事件分发给ScrollView
        //（不管touch哪里，都要通知列头跟着动，下文的每个item向列头订阅了滚动事件，从而与列头联动）
        HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHeadLayout.findViewById(mId);
        headSrcrollView.onTouchEvent(event);
        //继续向子view下发touch事件
        return false;
    }
}
