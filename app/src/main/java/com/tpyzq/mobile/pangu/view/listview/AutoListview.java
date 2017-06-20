package com.tpyzq.mobile.pangu.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 作者：刘泽鹏 on 2016/9/13 21:56
 */
public class AutoListview extends ListView {


    public AutoListview(Context context) {

        super(context);

    }


    public AutoListview(Context context, AttributeSet attrs) {

        super(context, attrs);

    }


    public AutoListview(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }


    @Override

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}