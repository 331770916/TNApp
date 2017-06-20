package com.tpyzq.mobile.pangu.view.chart.element;

import com.github.mikephil.charting.components.YAxis;

/**
 * Created by zhangwenbo on 2016/6/6.
 * 自定义y轴
 */
public class MyYAxis extends YAxis {

    private float baseValue=Float.NaN;
    private String minValue;
    public MyYAxis() {
        super();
    }

    public MyYAxis(AxisDependency axis) {
        super(axis);
    }

    public void setShowMaxAndUnit(String minValue) {
        setShowOnlyMinMax(true);
        this.minValue = minValue;
    }

    public float getBaseValue() {
        return baseValue;
    }

    public String getMinValue(){
        return minValue;
    }

    public void setBaseValue(float baseValue) {
        this.baseValue = baseValue;
    }
}
