package com.tpyzq.mobile.pangu.view.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tpyzq.mobile.pangu.data.ChartKEntity;
import com.tpyzq.mobile.pangu.view.chart.calculate.CalculateChartValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/6/5.
 * KDJ图
 */
public class MyKdjChart extends LineChart {

    private int[] mColors = new int[] {
            Color.RED,
            Color.GREEN,
            Color.BLUE
    };

    public MyKdjChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initChart();
        }
    }

    private void initChart() {
        setDrawGridBackground(false);       // 是否设置格线图背景
        setDescription("");                 // 描述
        setDrawBorders(false);              // 不描框
        setDrawGridBackground(false);       // 是否设置格线图背景
        setDragDecelerationEnabled(false);  // 是否设置手点击放大的效果
        setBackgroundColor(Color.WHITE);    // 背景色
        getLegend().setEnabled(false);      // 是否设置显示当前颜色说明
        setScaleYEnabled(false);            // 取消Y轴缩放动画
        setAutoScaleMinMaxEnabled(true);    // 自动缩放调整

         /* Y轴 */
        YAxis left = getAxisLeft();          // 实例化Y轴
        left.setDrawLabels(true);            // 左侧Y轴坐标
        left.setDrawAxisLine(true);          // 左侧Y轴
        left.setDrawGridLines(true);         // 横向线
        getAxisRight().setEnabled(false);    // 不显示右侧Y轴
        left.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);    // 纵轴数据显示在图形内部

        /* X轴 */
        XAxis xAxis = getXAxis();                       // 实例化X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置X轴位置
    }

    /**
     * 填充数据
     *
     * @param chartKEntities 数据实体
     */
    public void setData(List<ChartKEntity> chartKEntities) {

        if (chartKEntities == null && chartKEntities.size() <= 0) {
            return;
        }

        resetTracking();
        int count = chartKEntities.size();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(chartKEntities.get(i).getTime() + "");
        }

        ArrayList<ArrayList<Entry>> lineDatas = CalculateChartValue.convertKDJData(chartKEntities, getContext());

        if (lineDatas == null && lineDatas.size() <= 0) {
            return ;
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        for (int i = 0; i < lineDatas.size(); i++) {
            LineDataSet d = new LineDataSet(lineDatas.get(i), "");
            d.setLineWidth(1f);   //设置线宽

            d.setDrawCircles(false);   //线上不用小圆点
            d.setDrawCubic(true);     //开启设置线的圆滑度
            d.setCubicIntensity(0.2f); //设置线的圆滑度
            d.setDrawValues(false);

            int color = mColors[i % mColors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        LineData data = new LineData(xVals, dataSets);
        setData(data);

//        // 最多显示60组数据
        setVisibleXRangeMaximum(120);
//        // 最少显示30组数据
        setVisibleXRangeMinimum(30);
        // 移动到最右侧数据
        moveViewToX(chartKEntities.size() - 1);
    }

}
