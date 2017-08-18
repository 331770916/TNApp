package com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by zhangwnebo on 2017/8/17.
 * 首页mark
 */

public class ProgressMarkView extends MarkerView {

    private TextView tvContent;
    private ArrayList<String> mDates;
    private static final String TAG = ProgressMarkView.class.getSimpleName();

    private int mScreenWidth;
    private int mChartViewHeight;

    public ProgressMarkView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.marker_tv);

        mScreenWidth = Helper.getScreenWidth(context);
        mChartViewHeight = Helper.dip2px(context, 40);
    }

    public void setDateList(ArrayList<String> dates) {
        mDates = dates;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String date = "";
        if (mDates != null && mDates.size() > 0) {
            date = mDates.get(e.getXIndex());
        }
        tvContent.setText("距结束:" +  date);
    }

    @Override
    public int getXOffset(float xpos) {
        if ((mScreenWidth - xpos) > getWidth()/2 && xpos > getWidth()/2) {
            //左侧边界
            return -(getWidth() / 2);
        } else if ((mScreenWidth - xpos) > getWidth()/2 && xpos < getWidth()/2) {
            //右侧边界
            return -(getWidth() / 2) + (int)(xpos/2);
        } else {
            return -(getWidth());
        }
    }

    @Override
    public int getYOffset(float ypos) {
        if ((mChartViewHeight - ypos) > ypos) {
            return -getHeight() + (int)(ypos/2);
        } else {
            return - getHeight() - 10;
        }
    }
}
