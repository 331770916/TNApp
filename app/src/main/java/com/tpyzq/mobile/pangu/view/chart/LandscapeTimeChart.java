package com.tpyzq.mobile.pangu.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.tpyzq.mobile.pangu.data.ChartTimeEntitiy;
import com.tpyzq.mobile.pangu.view.chart.element.MyLeftMarkerView;
import com.tpyzq.mobile.pangu.view.chart.element.MyRightMarkerView;

import java.util.List;


/**
 * Created by zhangwenbo on 2016/6/16.
 * 横向的分时图上
 */
public class LandscapeTimeChart extends MyTimeChart{

    private MyLeftMarkerView myMarkerViewLeft;
    private MyRightMarkerView myMarkerViewRight;
    private List<ChartTimeEntitiy> mChartTimeEntitiyDatas;
    private Entry e;


    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void setData(List<ChartTimeEntitiy> chartTimeEntitiyDatas) {
        super.setData(chartTimeEntitiyDatas);

        mChartTimeEntitiyDatas = chartTimeEntitiyDatas;
    }

    public LandscapeTimeChart (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

        public void setHighlightValue(Entry e, Highlight h) {
        this.e = e;
        if (mData == null)
            mIndicesToHighlight = null;
        else {
            mIndicesToHighlight = new Highlight[]{
                    h};
        }
        invalidate();
    }

    public void setMarker(MyLeftMarkerView markerLeft, MyRightMarkerView markerRight, List<ChartTimeEntitiy> chartTimeEntitiyDatas) {
        this.myMarkerViewLeft = markerLeft;
        this.myMarkerViewRight = markerRight;
        mChartTimeEntitiyDatas = chartTimeEntitiyDatas;
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (mIndicesToHighlight != null && myMarkerViewLeft != null && myMarkerViewRight != null) {
            for (int i = 0; i < mIndicesToHighlight.length; i++) {
                Entry e = this.e;
                if (e == null || e.getXIndex() != mIndicesToHighlight[i].getXIndex())
                    continue;
                e=mData.getEntryForHighlight(mIndicesToHighlight[i]);
                float[] pos = getMarkerPosition(e, mIndicesToHighlight[i]);
                myMarkerViewLeft.draw(canvas, mViewPortHandler.contentLeft() - myMarkerViewLeft.getWidth(), pos[1] - myMarkerViewLeft.getHeight() / 2);
                myMarkerViewRight.draw(canvas, mViewPortHandler.contentRight(), pos[1] - myMarkerViewRight.getHeight() / 2);
                float yValForXIndex1 = Float.parseFloat(mChartTimeEntitiyDatas.get(mIndicesToHighlight[i].getXIndex()).getPrice());
                float yValForXIndex2 = mChartTimeEntitiyDatas.get(mIndicesToHighlight[i].getXIndex()).getPercentage();

                myMarkerViewLeft.setData(yValForXIndex1);
                myMarkerViewRight.setData(yValForXIndex2);
                myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                myMarkerViewRight.refreshContent(e, mIndicesToHighlight[i]);
                /*重新计算大小*/
                myMarkerViewLeft.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                myMarkerViewLeft.layout(0, 0, myMarkerViewLeft.getMeasuredWidth(),
                        myMarkerViewLeft.getMeasuredHeight());
                myMarkerViewRight.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                myMarkerViewRight.layout(0, 0, myMarkerViewRight.getMeasuredWidth(),
                        myMarkerViewRight.getMeasuredHeight());
            }
        }
    }

}
