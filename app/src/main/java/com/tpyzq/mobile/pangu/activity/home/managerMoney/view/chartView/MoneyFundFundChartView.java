package com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund.MoneyFundBaseView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart.MyLineChart;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart.MyMarkerView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart.OnMounthLabels;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetFundHistoryValueConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetFundHistoryValueConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.chart.element.MyXAxis;
import com.tpyzq.mobile.pangu.view.chart.element.MyYAxis;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangwenbo on 2016/9/26.
 * 非货币基金净值走势
 */
public class MoneyFundFundChartView extends MoneyFundBaseView implements RadioGroup.OnCheckedChangeListener, ICallbackResult {

    private RadioGroup mRadioGroup;
    private MyLineChart mMyLineChart;
    private MyMarkerView mMyMarkerView;

    private MyXAxis xAxisLine;
    private MyYAxis axisLeftLine;

    private ArrayList<String> mMounthXdates;
    private ArrayList<Float> mMounthValues;
    private boolean mMounthFlag = true;

    private ArrayList<String> mQuarterXdates;
    private ArrayList<Float> mQuarterValues;
    private boolean mQuarterFlag = false;

    private ArrayList<String> mHelfYearXdates;
    private ArrayList<Float> mHelfYearValues;
    private boolean mHelfYearFlag = false;

    private ArrayList<String> mOneYearXdates;
    private ArrayList<Float> mOneYearValues;
    private boolean mOneYearFlag = false;

    private SimpleRemoteControl mSimpleRemoteControl;
    private String mFundCode;

    private ProgressBar mProgress;

    private static final String TAG ="MoneyFundFundChartView";

    public MoneyFundFundChartView(Activity activity, Object object) {
        super(activity, object);
    }

    @Override
    public void initView(View view, Activity activity, Object object) {

        mMounthXdates = new ArrayList<>();
        mMounthValues = new ArrayList<>();
        mQuarterXdates = new ArrayList<>();
        mQuarterValues = new ArrayList<>();
        mHelfYearXdates = new ArrayList<>();
        mHelfYearValues = new ArrayList<>();
        mOneYearXdates = new ArrayList<>();
        mOneYearValues = new ArrayList<>();


        mSimpleRemoteControl = new SimpleRemoteControl(this);

        mProgress = (ProgressBar) view.findViewById(R.id.fundMistackProgress);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.mistakeFundChartBottomLayout);
        mRadioGroup.setOnCheckedChangeListener(this);

        mMyLineChart = (MyLineChart) view.findViewById(R.id.mistakeFundLineChart);
        initChart(mMyLineChart);

        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;

        if (entity != null && !TextUtils.isEmpty(entity.getSECURITYCODE())) {
            mMounthFlag = true;
            mProgress.setVisibility(View.VISIBLE);
            mFundCode = entity.getSECURITYCODE();
            mSimpleRemoteControl.setCommand(new ToGetFundHistoryValueConnect(new GetFundHistoryValueConnect(TAG, mFundCode, getMounthDate()[1], getMounthDate()[0], "", "", "")));
            mSimpleRemoteControl.startConnect();
        }
    }

    /**
     * 获取一个月的起始时间
     * @return
     */
    private String[] getMounthDate () {
        String [] dates = new String[2];
        String startDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        String endDate = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());

        dates[0] = startDate;
        dates[1] = endDate;
        return dates;
    }

    /**
     * 获取一个季度的起始时间
     * @return
     */
    private String[] getQuarterDate() {
        String [] dates = new String[2];
        String startDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);

        String endDate = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());

        dates[0] = startDate;
        dates[1] = endDate;
        return dates;
    }

    /**
     * 获取半年的起始时间
     * @return
     */
    private String[] getHalfYearDate() {
        String [] dates = new String[2];
        String startDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -6);

        String endDate = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());

        dates[0] = startDate;
        dates[1] = endDate;
        return dates;
    }

    /**
     * 获取一年的起始时间
     * @return
     */
    private String [] getOneYearDate() {
        String [] dates = new String[2];
        String startDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        String endDate = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());

        dates[0] = startDate;
        dates[1] = endDate;
        return dates;
    }

    @Override
    public void getResult(Object result, String tag) {

        Map<String, Object> datas = (Map<String, Object>) result;
        mProgress.setVisibility(View.GONE);

        try {
            if (datas == null) {
                return;
            }

            String code = (String)datas.get("code");
            String msg = (String) datas.get("msg");

            if (!TextUtils.isEmpty(code) && "-1".equals(code)) {
                Helper.getInstance().showToast(CustomApplication.getContext(), msg);
                return;
            }

            List<Map<String, String>> chartDatasList = (List<Map<String, String>>) datas.get("chartDatasList");

            if (chartDatasList == null || chartDatasList.size() <= 0) {
                return;
            }

            for (Map<String, String> item : chartDatasList) {
                String endTime = item.get("endDate");
                String unitnv = item.get("unitnv");

                if (mMounthFlag) {
                    mMounthXdates.add(endTime);
                    mMounthValues.add(Float.parseFloat(unitnv));
                } else if (mQuarterFlag) {
                    mQuarterXdates.add(endTime);
                    mQuarterValues.add(Float.parseFloat(unitnv));
                } else if (mHelfYearFlag) {
                    mHelfYearXdates.add(endTime);
                    mHelfYearValues.add(Float.parseFloat(unitnv));
                } else if (mOneYearFlag) {
                    mOneYearXdates.add(endTime);
                    mOneYearValues.add(Float.parseFloat(unitnv));
                }

            }


            if (mMounthFlag) {
                Collections.reverse(mMounthXdates);
                Collections.reverse(mMounthValues);
                mMyMarkerView.setDateList(mMounthXdates);
                setChartData(new OnMounthLabels(false), mMounthXdates, mMounthValues);

            } else if (mQuarterFlag) {
                Collections.reverse(mQuarterXdates);
                Collections.reverse(mQuarterValues);
                mMyMarkerView.setDateList(mQuarterXdates);
                setChartData(new OnMounthLabels(false), mQuarterXdates, mQuarterValues);

            } else if (mHelfYearFlag) {
                Collections.reverse(mHelfYearXdates);
                Collections.reverse(mHelfYearValues);
                mMyMarkerView.setDateList(mHelfYearXdates);
                setChartData(new OnMounthLabels(false), mHelfYearXdates, mHelfYearValues);
            } else if (mOneYearFlag) {
                Collections.reverse(mOneYearXdates);
                Collections.reverse(mOneYearValues);
                mMyMarkerView.setDateList(mOneYearXdates);
                setChartData(new OnMounthLabels(false), mOneYearXdates, mOneYearValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setChartData(TypeXLabels typeXLabels, ArrayList<String> strDates, ArrayList<Float> values) {

        if (values == null || values.size() == 0) {
            return;
        }

        xAxisLine.setXLabels(typeXLabels.setXOnLabels(strDates));
        axisLeftLine.setStartAtZero(false);
//        axisLeftLine.setAxisMinValue(0);



        ArrayList<Entry> lineChartEntries = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {

            lineChartEntries.add(new Entry(values.get(i), i));
        }

        LineDataSet lineDataSet = new LineDataSet(lineChartEntries, "净值走势");

        lineDataSet.setDrawValues(false);
        lineDataSet.setCircleRadius(0);
//        lineDataSet.setColor(Color.parseColor("#daecff"));
        lineDataSet.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.mistakeFundChartColor));
        lineDataSet.setHighLightColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.yellow));

        String sdk = android.os.Build.VERSION.SDK;//SDK版本

        if (Integer.parseInt(sdk) < 19) {
            lineDataSet.setDrawFilled(false);
        } else {
            Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.color.mistakeFundChartFullColor);
            lineDataSet.setFillDrawable(drawable);
            lineDataSet.setDrawFilled(true);
        }

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(lineDataSet);
        LineData cd = new LineData(new String[values.size()], sets);
        mMyLineChart.setData(cd);

        mMyLineChart.invalidate();//刷新图
    }

    /**
     * 初始化折线图
     */
    private void initChart(MyLineChart myLineChart) {
        myLineChart.setScaleEnabled(false);
        myLineChart.setDrawBorders(true);
        myLineChart.setBorderWidth(1);
        myLineChart.setBorderColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.lineColor));
        myLineChart.setDescription("");
        Legend lineChartLegend = myLineChart.getLegend();
        lineChartLegend.setEnabled(false);

        //x轴
        xAxisLine = myLineChart.getXAxis();
        xAxisLine.setDrawLabels(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisLine.setDrawGridLines(true);

        //左边y
        axisLeftLine = myLineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(5, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(true);
        axisLeftLine.setStartAtZero(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);
        myLineChart.getAxisRight().setEnabled(false);

        mMyMarkerView = new MyMarkerView(CustomApplication.getContext(), R.layout.mark_mistakefundchart);
        // set the marker to the chart
        myLineChart.setMarkerView(mMyMarkerView);

        //背景线
        xAxisLine.setGridColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.lineColor));
        xAxisLine.setAxisLineColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
        xAxisLine.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
        axisLeftLine.setGridColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
        axisLeftLine.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));

        axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.000");
                return mFormat.format(value);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mistakeFundChartBtn1:
                mMounthFlag = true;
                mQuarterFlag = false;
                mHelfYearFlag = false;
                mOneYearFlag = false;

                if (mMounthXdates != null && mMounthXdates.size() > 0 && mMounthValues != null && mMounthValues.size() > 0) {
                    mMyMarkerView.setDateList(mMounthXdates);
                    setChartData(new OnMounthLabels(false), mMounthXdates, mMounthValues);
                } else {
                    if (!TextUtils.isEmpty(mFundCode)) {
                        mProgress.setVisibility(View.VISIBLE);
                        mSimpleRemoteControl.setCommand(new ToGetFundHistoryValueConnect(new GetFundHistoryValueConnect(TAG, mFundCode, getMounthDate()[1], getMounthDate()[0], "", "", "")));
                        mSimpleRemoteControl.startConnect();
                    }
                }

                break;
            case R.id.mistakeFundChartBtn2:
                mMounthFlag = false;
                mQuarterFlag = true;
                mHelfYearFlag = false;
                mOneYearFlag = false;

                if (mQuarterValues != null && mQuarterValues.size() > 0 && mQuarterXdates != null && mQuarterXdates.size() > 0) {
                    mMyMarkerView.setDateList(mQuarterXdates);
                    setChartData(new OnMounthLabels(false), mQuarterXdates, mQuarterValues);
                } else {
                    if (!TextUtils.isEmpty(mFundCode)) {
                        mProgress.setVisibility(View.VISIBLE);
                        mSimpleRemoteControl.setCommand(new ToGetFundHistoryValueConnect(new GetFundHistoryValueConnect(TAG, mFundCode, getQuarterDate()[1], getQuarterDate()[0], "", "", "")));
                        mSimpleRemoteControl.startConnect();
                    }
                }

                break;
            case R.id.mistakeFundChartBtn3:
                mMounthFlag = false;
                mQuarterFlag = false;
                mHelfYearFlag = true;
                mOneYearFlag = false;

                if (mHelfYearValues != null && mHelfYearValues.size() > 0 && mHelfYearXdates != null && mHelfYearXdates.size() > 0) {
                    mMyMarkerView.setDateList(mHelfYearXdates);
                    setChartData(new OnMounthLabels(false), mHelfYearXdates, mHelfYearValues);
                } else {
                    if (!TextUtils.isEmpty(mFundCode)) {
                        mProgress.setVisibility(View.VISIBLE);
                        mSimpleRemoteControl.setCommand(new ToGetFundHistoryValueConnect(new GetFundHistoryValueConnect(TAG, mFundCode, getHalfYearDate()[1], getHalfYearDate()[0], "", "", "")));
                        mSimpleRemoteControl.startConnect();
                    }
                }

                break;
            case R.id.mistakeFundChartBtn4:
                mMounthFlag = false;
                mQuarterFlag = false;
                mHelfYearFlag = false;
                mOneYearFlag = true;

                if (mOneYearValues != null && mOneYearValues.size() > 0 && mOneYearXdates != null && mOneYearXdates.size() > 0) {
                    mMyMarkerView.setDateList(mOneYearXdates);
                    setChartData(new OnMounthLabels(false), mOneYearXdates, mOneYearValues);
                } else {
                    if (!TextUtils.isEmpty(mFundCode)) {
                        mProgress.setVisibility(View.VISIBLE);
                        mSimpleRemoteControl.setCommand(new ToGetFundHistoryValueConnect(new GetFundHistoryValueConnect(TAG, mFundCode, getOneYearDate()[1], getOneYearDate()[0], "", "", "")));
                        mSimpleRemoteControl.startConnect();
                    }
                }

                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_mistakefund_chart;
    }

    public interface TypeXLabels {
        SparseArray<String> setXOnLabels(ArrayList<String> dates);
    }
}
