package com.tpyzq.mobile.pangu.view.chart;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.ChartKEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/6/3.
 * 柱形图
 */
public class MyBarChart extends BarChart {



    public static boolean SHOWMINMAXVALUE = true;

    public MyBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {

            initChart();
        }
    }

    private void initChart() {

        setScaleYEnabled(false);                // 取消Y轴缩放动画
        setAutoScaleMinMaxEnabled(true);        // 自动缩放调整
        setDescription("");                     // 描述
        setBackgroundColor(Color.WHITE);        // 背景色
        setDrawGridBackground(false);           // 是否设置格线图背景
        setDrawBarShadow(false);                // 是否展示数据
        setDrawBorders(false);                  // 是否画边框
        setDragDecelerationEnabled(false);      // 是否设置手点击放大的效果
        getLegend().setEnabled(false);          // 是否设置显示当前颜色说明

         /* Y轴 */
        YAxis left = getAxisLeft();             // 实例化Y轴
        left.setDrawLabels(true);               // 左侧Y轴坐标
        left.setDrawAxisLine(true);             // 左侧Y轴
        left.setDrawGridLines(false);           // 横向线
        getAxisRight().setEnabled(false);       // 不显示右侧Y轴
        left.setShowOnlyMinMax(false);          // 仅仅显示最大值最小值
        left.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART); // 纵轴数据显示在图形内部
        left.setShowOnlyMinMax(SHOWMINMAXVALUE);

        /* X轴 */
        XAxis xAxis = getXAxis();               // 实例化x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x轴位置


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

        int count = chartKEntities.size();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(chartKEntities.get(i).getTime() + "");
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<Integer> colors  = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = Float.parseFloat(chartKEntities.get(i).getAmount());
            float close = Float.parseFloat(chartKEntities.get(i).getClose());
            float open  = Float.parseFloat(chartKEntities.get(i).getOpen());

            yVals1.add(new BarEntry(val, i));
            if (close > open) {
                colors.add(Color.RED);
            } else {
                colors.add(Color.GREEN);
            }
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setDrawValues(false);
        set1.setColors(colors);
        set1.setBarSpacePercent(35f);
        set1.setHighLightColor(ContextCompat.getColor(getContext(), R.color.highLight));
        set1.setHighLightAlpha(255);
        set1.setHighlightEnabled(true);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        setData(data);

        // 最多显示60组数据
        setVisibleXRangeMaximum(120);
        // 最少显示30组数据
        setVisibleXRangeMinimum(30);
        // 移动到最右侧数据
        moveViewToX(chartKEntities.size() - 1);
    }

}
