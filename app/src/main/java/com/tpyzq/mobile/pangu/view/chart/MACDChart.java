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
import com.tpyzq.mobile.pangu.data.MACDEntity;
import com.tpyzq.mobile.pangu.view.chart.calculate.CalculateChartValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/6/4.
 * MACD图
 */
public class MACDChart extends BarChart {

    public MACDChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initChart();
        }
    }

    /**
     * 初始化Chart
     */
    private void initChart() {

        setDescription("");                 // 描述
        setBackgroundColor(Color.WHITE);    // 背景色
        setDrawGridBackground(false);       // 是否设置格线图背景
        setDrawValueAboveBar(true);         // 是否绘制数据在图之上
        setMaxVisibleValueCount(60);        // 当前所显示的最大数据的数量
        setScaleYEnabled(false);            // 是否缩放Y轴
        setAutoScaleMinMaxEnabled(true);    // 自动缩放调整
        setDragDecelerationEnabled(false);  // 是否设置手点击放大的效果
        getLegend().setEnabled(false);      // 是否设置显示当前颜色说明

        /*Y轴*/
        YAxis left = getAxisLeft();         // 实例化Y轴
        left.setLabelCount(6, false);       // y轴显示数据的个数
        left.setStartAtZero(false);         // 是否从零开始
        left.setAxisMinValue(-1f);          // 最小值
        left.setAxisMaxValue(1f);           // 最大值
        getAxisRight().setEnabled(false);   // 不显示右侧Y轴
        left.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);    // 纵轴数据显示在图形内部

         /* X轴 */
        XAxis xAxis = getXAxis();                       // 实例化X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置X轴位置
        setDrawBarShadow(false);                        // 是否显示值

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

        List<MACDEntity> _entitys  = CalculateChartValue.convertMACDData(chartKEntities, getContext());

        int count = _entitys.size();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(_entitys.get(i).getDate()+ "");
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) _entitys.get(i).getMacd();
            yVals1.add(new BarEntry(val, i));
            if (val > 0) {
                colors.add(Color.RED);
            } else {
                colors.add(Color.GREEN);
            }
        }


        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setDrawValues(false);
        set1.setBarSpacePercent(35f);
        set1.setColors(colors);
        set1.setHighLightColor(ContextCompat.getColor(getContext(), R.color.highLight));
        set1.setHighLightAlpha(255);
        set1.setHighlightEnabled(true);

        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
//        data.setValueTypeface(mTf);
        setData(data);

        // 最多显示60组数据
        setVisibleXRangeMaximum(120);
        // 最少显示30组数据
        setVisibleXRangeMinimum(30);
        // 移动到最右侧数据
        moveViewToX(chartKEntities.size() - 1);
    }

}
