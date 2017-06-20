package com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/9/28.
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private ArrayList<String> mDates;
    private static final String TAG = "MyMarkerView";

    private int mScreenWidth;
    private int mChartViewHeight;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.marker_tv);

        mScreenWidth = Helper.getScreenWidth(context);
        mChartViewHeight = Helper.dip2px(context, 200);
    }


    public void setDateList(ArrayList<String> dates) {
        mDates = dates;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        DecimalFormat mFormat = new DecimalFormat("#0.000");
        String date = "";
        if (mDates != null && mDates.size() > 0) {
            date = mDates.get(e.getXIndex());
        }

        tvContent.setText("单位净值(元):" +  mFormat.format(e.getVal()) + "\n时间：" + date);
    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        LogHelper.i(TAG, "xpos:" + xpos);

        if ((mScreenWidth - xpos) > getWidth()/2 && xpos > getWidth()/2) {
            return -(getWidth() / 2);
        } else if ((mScreenWidth - xpos) > getWidth()/2 && xpos < getWidth()/2) {
            return -(getWidth() / 2) + (int)(xpos/2);
        } else {
            return -(getWidth());
        }

    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        LogHelper.i(TAG, "ypos:" + ypos);
        LogHelper.i(TAG, "mChartViewHeight:" + mChartViewHeight);
        if ((mChartViewHeight - ypos) > ypos) {
            return -getHeight() + (int)(ypos/2);
        } else {
            return - getHeight() - 50;
        }

    }
}
