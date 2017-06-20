package com.tpyzq.mobile.pangu.view.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.tpyzq.mobile.pangu.data.ChartTimeEntitiy;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangwenbo on 2016/6/6.
 * 分时图下
 */
public class MyTimeBarChart extends BarChart {

    public static boolean SHOWMINMAXVALUE = true;

    public MyTimeBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initChart();
        }
    }

    private void initChart() {
        setDescription("");                 // 描述
        setBackgroundColor(Color.WHITE);    // 背景色
        setDrawGridBackground(false);       // 是否设置格线图背景
        setDrawBarShadow(false);            // 是否展示值
        setDragDecelerationEnabled(false);  // 当前所显示的最大数据的数量
        setScaleEnabled(false);             // 是否缩放
        setDrawBorders(true);               // 是否画边框
        setBorderWidth(1);                  // 边框的粗细
        getLegend().setEnabled(false);      // 是否设置显示当前颜色说明

         /* Y轴 */
        YAxis left = getAxisLeft();          // 实例化Y轴
        left.setDrawLabels(true);            // 左侧Y轴坐标
        left.setDrawAxisLine(true);          // 左侧Y轴
        left.setDrawGridLines(true);         // 横向线
        getAxisRight().setEnabled(false);    // 不显示右侧Y轴
        left.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART); // 纵轴数据显示在图形内部
        left.setShowOnlyMinMax(SHOWMINMAXVALUE);

        /* X轴 */
        XAxis xAxis = getXAxis();       // 实例化X轴
        xAxis.setDrawLabels(false);     // 是否
        xAxis.setDrawGridLines(false);  // 是否设置格线图线
        xAxis.setDrawAxisLine(false);   //是否画线
    }


    /**
     * 填充数据
     *
     * @param chartTimeEntitiyDatas 数据实体
     */
    public void setData(List<ChartTimeEntitiy> chartTimeEntitiyDatas) {
        if (chartTimeEntitiyDatas == null && chartTimeEntitiyDatas.size() <= 0) {
            return;
        }

        int count = chartTimeEntitiyDatas.size();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<Integer> colors  = new ArrayList<>();
        int lastAcountBuy = 0;
        for (int i = 0; i < count; i++) {
            int acountBuy = chartTimeEntitiyDatas.get(i).getAcountBuy();
            if (i > 0) {
                lastAcountBuy = chartTimeEntitiyDatas.get(i - 1).getAcountBuy();
            }

            yVals1.add(new BarEntry(acountBuy, i));

            if (acountBuy - lastAcountBuy > 0) {
                colors.add(Color.RED);
            } else if (acountBuy - lastAcountBuy < 0) {
                colors.add(Color.GREEN);
            } else {
                colors.add(Color.GRAY);
            }
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setDrawValues(false);
        set1.setColors(colors);
        set1.setBarSpacePercent(35f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(getMinutesCount(), dataSets);
        data.setValueTextSize(10f);

        setData(data);

        invalidate();
    }

    /** 分时数据的总大小 **/
    public String[] getMinutesCount() {

        String fileName = "TimeLineData.txt";

        String strTime = Helper.getInstance().getStringFromAssets(getContext(), fileName);

        String [] timesX = strTime.split(",");

        return timesX;
    }

}
