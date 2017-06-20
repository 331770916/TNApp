package com.tpyzq.mobile.pangu.view.chart;

import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

/**
 * Created by zhangwenbo on 2016/6/3.
 * 选择图数据时的高亮显示
 */
public class MyChartHighlighter extends ChartHighlighter<BarLineScatterCandleBubbleDataProvider> {

    public MyChartHighlighter(BarLineScatterCandleBubbleDataProvider chart) {
        super(chart);
    }

    /**
     * 通过像素获取index
     *
     * @param x 像素
     * @return index
     */
    @Override
    public int getXIndex(float x) {
        return super.getXIndex(x);
    }
}
