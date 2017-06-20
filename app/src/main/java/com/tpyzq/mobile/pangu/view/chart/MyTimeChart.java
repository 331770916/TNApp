package com.tpyzq.mobile.pangu.view.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tpyzq.mobile.pangu.data.ChartTimeEntitiy;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.chart.element.MyXAxis;
import com.tpyzq.mobile.pangu.view.chart.element.MyXAxisRenderer;
import com.tpyzq.mobile.pangu.view.chart.element.MyYAxis;
import com.tpyzq.mobile.pangu.view.chart.element.MyYAxisRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/6/6.
 * 分时图上
 */
public class MyTimeChart extends LineChart {

    private String [] mTimesX; // x轴坐标值

    public MyTimeChart (Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initChart();
        }
    }

    private void initChart () {

        mTimesX = defaultMinutesCount();

        setDescription("");                    // 描述
        setDrawBorders(true);                  // 是否画边框
        setBackgroundColor(Color.WHITE);       // 背景色
        setBorderWidth(1);                     // 边框宽度
        getLegend().setEnabled(false);         // 是否设置显示当前颜色说明
        setScaleEnabled(false);                // 是否放大缩小

        /* x轴 */
        MyXAxis xAxisLine = getXAxis();         // 实例化X轴
        xAxisLine.setDrawLabels(true);          // 是否设置坐标
        xAxisLine.setXLabels(getXLabels());     // 设置X轴坐标
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴位置

        /* 左边y */
        /*折线图y轴左没有basevalue，调用系统的*/
        MyYAxis axisLeftLine = getAxisLeft();       // 实例化左侧Y轴
        axisLeftLine.setLabelCount(7, true);        // 左侧Y轴的坐标点有几个
        axisLeftLine.setDrawLabels(true);           // 是否画坐标
        axisLeftLine.setDrawGridLines(true);        // 是否画grid线
        axisLeftLine.setDrawAxisLine(true);         // 轴不显示 避免和border冲突
        axisLeftLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);    // 纵轴数据显示在图形内部

        //左侧y轴样式
        axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

        /* 右边y */
        MyYAxis axisRightLine = getAxisRight();     // 实例化右侧Y轴
        axisRightLine.setLabelCount(3, true);       // 右侧Y轴的坐标点有几个
        axisRightLine.setDrawLabels(true);          // 是否画坐标
        axisRightLine.setStartAtZero(false);        // 是否从零开始
        axisRightLine.setDrawGridLines(true);       // 是否画grid线
        axisRightLine.setDrawAxisLine(true);        // 是否画线
        LimitLine ll = new LimitLine(0);            // 基准线
        ll.setLineWidth(1f);                        // 基准线宽
        ll.setLineColor(Color.RED);                 // 基准线颜色
        ll.enableDashedLine(10f, 10f, 0f);          // 基准线样式
        axisRightLine.addLimitLine(ll);             // 设置基准线
        axisRightLine.setBaseValue(0);              // 基准线基准值
        axisRightLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);   // 纵轴数据显示在图形内部

        //右侧y轴样式
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });


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

        //设置y左右两轴最大最小值
        getAxisLeft().setAxisMinValue(ChartTimeEntitiy.minyValue);
        getAxisLeft().setAxisMaxValue(ChartTimeEntitiy.maxyValue);
        getAxisRight().setAxisMinValue(ChartTimeEntitiy.percentMin);
        getAxisRight().setAxisMaxValue(ChartTimeEntitiy.percentMax);

        ArrayList<Entry> whiteLineEntries = new ArrayList<Entry>();
        ArrayList<Entry> yellowLineEntries = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            String price = chartTimeEntitiyDatas.get(i).getPrice();
            float _price = Float.parseFloat(price);
            float valPrice = chartTimeEntitiyDatas.get(i).getValPrice();

            whiteLineEntries.add(new Entry(_price, i));

            //240 - 162 = 78
            yellowLineEntries.add(new Entry(valPrice, i));
        }

        LineDataSet d1 = new LineDataSet(whiteLineEntries, "成交价");
        LineDataSet d2 = new LineDataSet(yellowLineEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        d1.setCircleRadius(0);
        d2.setCircleRadius(0);

        d1.setColor(Color.BLUE);
        d2.setColor(Color.YELLOW);

        d1.setHighLightColor(Color.BLACK);
        d2.setHighlightEnabled(false);

        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        d2.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData data = new LineData(mTimesX, sets);
        setData(data);
        invalidate();
    }

    /**  获取x轴坐标  **/
    private SparseArray<String> getXLabels() {
        //一天中有4个小时在传输数据， 一天则有240条数据 平分
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "09:30");
        xLabels.put(60, "10:30");
        xLabels.put(120, "11:30/13:00");
        xLabels.put(180, "14:00");
        xLabels.put(240, "15:00");
        return xLabels;
    }

    /** 默认分时数据的总大小 **/
    private String[] defaultMinutesCount() {

        String fileName = "TimeLineData.txt";

        String strTime = Helper.getInstance().getStringFromAssets(getContext(), fileName);

        String [] timesX = strTime.split(",");


        return timesX;
    }

    public String[] setMinutesCount(String [] timesX){
        mTimesX = timesX;
        return mTimesX;
    }

    @Override
    protected void init() {
        super.init();
        mXAxis = new MyXAxis();
        mXAxisRenderer = new MyXAxisRenderer(mViewPortHandler, (MyXAxis) mXAxis, mLeftAxisTransformer,this);

        mAxisLeft = new MyYAxis(YAxis.AxisDependency.LEFT);
        mAxisRendererLeft = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisLeft, mLeftAxisTransformer);

        mAxisRight = new MyYAxis(YAxis.AxisDependency.RIGHT);
        mAxisRendererRight = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisRight, mRightAxisTransformer);
    }

    /*返回转型后的左右轴*/
    @Override
    public MyYAxis getAxisLeft() {
        return (MyYAxis) super.getAxisLeft();
    }

    @Override
    public MyXAxis getXAxis() {
        return (MyXAxis) super.getXAxis();
    }

    @Override
    public MyYAxis getAxisRight() {
        return (MyYAxis) super.getAxisRight();
    }
}
