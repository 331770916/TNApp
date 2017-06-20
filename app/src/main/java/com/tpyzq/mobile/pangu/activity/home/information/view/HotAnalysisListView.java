package com.tpyzq.mobile.pangu.activity.home.information.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 作者：刘泽鹏 on 2016/9/16 14:17
 * 自定义  热点模块首页的  ListView
 */
public class HotAnalysisListView extends ListView {


    public HotAnalysisListView(Context context) {
        super(context);
    }

    public HotAnalysisListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HotAnalysisListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
