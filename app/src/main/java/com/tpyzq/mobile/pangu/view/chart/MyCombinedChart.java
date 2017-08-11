package com.tpyzq.mobile.pangu.view.chart;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tpyzq.mobile.pangu.data.ChartKEntity;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.chart.gesture.IOnValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/6/3.
 * 蜡烛图
 */
public class MyCombinedChart extends CombinedChart implements OnChartGestureListener, OnChartValueSelectedListener {

    private static final String TAG = "MyCombinedChart";
    private final Vibrator     mVibrator;
    private boolean            isTranslate;
    private MyChartHighlighter myChartHighlighter;                  // 高亮线
    private List<ChartKEntity> mChartKEntities;                        // 数据源
    private IOnValueSelectedListener mIOnValueSelectedListener;     // 选择值的监听

    private Handler invalidate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public MyCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChart();

        mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    /**
     * 初始化
     */
    private void initChart() {
        setDescription("");                 // 描述
        setBackgroundColor(Color.WHITE);    // 背景色
        setDrawGridBackground(false);       // 是否设置格线图背景
        setDrawBarShadow(false);            // 是否展示数据
        setDragDecelerationEnabled(false);  // 是否设置手点击放大的效果
        setScaleYEnabled(false);            // 取消Y轴缩放动画
        setAutoScaleMinMaxEnabled(true);    // 自动缩放调整

        /* Y轴 */
        YAxis left = getAxisLeft();            // 实例化Y轴
        left.setDrawLabels(true);              // 左侧Y轴坐标
        left.setDrawAxisLine(true);            // 左侧Y轴
        left.setDrawGridLines(true);           // 横向线
        getAxisRight().setEnabled(false);      // 不显示右侧Y轴
        left.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART); // 纵轴数据显示在图形内部

        /* X轴 */
        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        /* 图形触摸监听 */
        setOnChartGestureListener(this);

        /* 被选择监听（高亮监听） */
        setOnChartValueSelectedListener(this);

        // 用来将像素转成index
        myChartHighlighter = new MyChartHighlighter(this);

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

        mChartKEntities = chartKEntities;
        ArrayList<String> timeY = new ArrayList<>();
        for (int i = 0; i < chartKEntities.size(); i++) {
            timeY.add(chartKEntities.get(i).getTime() + "");
        }


        CombinedData data = new CombinedData(timeY);

        data.setData(generateCandleData(chartKEntities));
        data.setData(generateLineData(chartKEntities));

        setData(data);

        notifyDataSetChanged();

        // 最多显示60组数据
        setVisibleXRangeMaximum(120);
        // 最少显示30组数据
        setVisibleXRangeMinimum(30);
        // 移动到最右侧数据
        moveViewToX(chartKEntities.size() - 1);
        /*
         延迟100毫秒执行invalidate
        为了解决控件使用setAutoScaleMinMaxEnabled方法后的一个小bug
          */
        invalidate.sendEmptyMessageDelayed(0, 100);
    }

    protected CandleData generateCandleData(List<ChartKEntity> chartKEntities) {
        CandleData d = new CandleData();
        ArrayList<CandleEntry> entries = new ArrayList<>();
        for (int index = 0; index < chartKEntities.size(); index++) {
            float high = Float.parseFloat(chartKEntities.get(index).getHigh());
            float low = Float.parseFloat(chartKEntities.get(index).getLow());
            float open = Float.parseFloat(chartKEntities.get(index).getOpen());
            float close = Float.parseFloat(chartKEntities.get(index).getClose());
            entries.add(new CandleEntry(index, high, low, open, close));
        }

        CandleDataSet set = new CandleDataSet(entries, "K线");
        // 不显示横向高亮线
        set.setDrawHorizontalHighlightIndicator(false);
        set.setValueTextSize(10f);
        set.setDrawValues(false);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(0.7f);
        set.setDecreasingColor(Color.GREEN);
        set.setDecreasingPaintStyle(Paint.Style.STROKE);
        set.setIncreasingColor(Color.RED);
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setDecreasingPaintStyle(Paint.Style.FILL);

        d.addDataSet(set);
        return d;
    }

    private LineData generateLineData(List<ChartKEntity> chartKEntities) {
        LineData d = new LineData();
        d.addDataSet(getLineDataSet(5, chartKEntities));
        d.addDataSet(getLineDataSet(10, chartKEntities));
        d.addDataSet(getLineDataSet(30, chartKEntities));
        return d;
    }

    private LineDataSet getLineDataSet(int ma, List<ChartKEntity> chartKEntities) {

        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = ma - 1; index < chartKEntities.size(); index++) {
            float sum = 0;
            for (int m = 0; m < ma; m++) {
                float _close = Float.parseFloat(chartKEntities.get(index - m).getClose()) ;
                sum += _close;
            }
            sum /= ma;
            entries.add(new Entry(sum, index));
        }
        LineDataSet set = new LineDataSet(entries, "MA " + ma);
        // 不显示横向高亮线
        set.setDrawHorizontalHighlightIndicator(false);
        set.setColor(5 == ma ? Color.rgb(240, 0, 70) : 10 == ma ? Color.rgb(0, 0, 70) : Color.rgb(100, 100, 255));
        set.setLineWidth(1f);
        set.setDrawCircles(false);
        set.setDrawCubic(false);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return set;
    }

    /*
    * Gesture callbacks
    * Start
    * *******************************************************************************/
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        isTranslate = false;
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        setDragEnabled(true);
        getData().setHighlightEnabled(false);

        if (null != mIOnValueSelectedListener) {
            mIOnValueSelectedListener.end();
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        if (!isTranslate) {
            CentreToast.showText(getContext().getApplicationContext(), "长按\n震动50毫秒\n可以左右滑动  查看数据");
            // 震动50毫秒
            mVibrator.vibrate(50);
            setDragEnabled(false);
            getData().setHighlightEnabled(true);

            float x = me.getRawX();
            // TODO 通过像素换算index  高亮显示
            int index = myChartHighlighter.getXIndex(x);
            highlightValue(index, 0);

            if (null != mIOnValueSelectedListener) {
                mIOnValueSelectedListener.start();
                float open = Float.parseFloat(mChartKEntities.get(index).getOpen());
                float close = Float.parseFloat(mChartKEntities.get(index).getClose());
                float high = Float.parseFloat(mChartKEntities.get(index).getHigh());
                float low  = Float.parseFloat(mChartKEntities.get(index).getLow());

                mIOnValueSelectedListener.data(open, close, high, low);
            }
        }
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        isTranslate = true;
    }
    /* End *******************************************************************************/

    /*
    * Selection callbacks
    * Start
    * *******************************************************************************/
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        // TODO index转换成像素
//        float f = me.getRawX();
//        if (f < mWidth / 2) {
//            Log.i(TAG, "显示在右侧");
//        } else {
//            Log.i(TAG, "显示在左侧");
//        }

        try {
            assert null != mChartKEntities;
            float open = Float.parseFloat(mChartKEntities.get(e.getXIndex()).getOpen());
            float close = Float.parseFloat(mChartKEntities.get(e.getXIndex()).getClose());
            float high =  Float.parseFloat(mChartKEntities.get(e.getXIndex()).getHigh());
            float low = Float.parseFloat(mChartKEntities.get(e.getXIndex()).getLow());
            if (null != mIOnValueSelectedListener) {
                mIOnValueSelectedListener.data(open, close, high, low);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "onNothingSelected");
    }
    /* End *******************************************************************************/

    public void setOnValueSelectedListener(IOnValueSelectedListener listener) {
        mIOnValueSelectedListener = listener;
    }

}
