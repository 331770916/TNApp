package com.tpyzq.mobile.pangu.view.chart.element;

import android.util.SparseArray;

import com.github.mikephil.charting.components.XAxis;

/**
 * Created by zhangwenbo on 2016/6/6.
 * 自定义X轴坐标
 */
public class MyXAxis extends XAxis {

    private SparseArray<String> labels;
    public SparseArray<String> getXLabels() {
        return labels;
    }
    public void setXLabels(SparseArray<String> labels) {
        this.labels = labels;
    }
}
