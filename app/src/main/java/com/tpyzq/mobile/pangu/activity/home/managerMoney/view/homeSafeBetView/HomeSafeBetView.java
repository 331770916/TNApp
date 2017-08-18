package com.tpyzq.mobile.pangu.activity.home.managerMoney.view.homeSafeBetView;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.HomeFragment;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.ManagerMoenyDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund.MoneyFundBaseView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.MoneyFundFundChartView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart.MyLineChart;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.chart.ProgressMarkView;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.NewOptionalFinancingEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.view.RiskControlView;
import com.tpyzq.mobile.pangu.view.chart.element.MyXAxis;
import com.tpyzq.mobile.pangu.view.chart.element.MyYAxis;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangwenbo on 2017/8/17.
 * 首页稳赢理财项
 */

public class HomeSafeBetView extends MoneyFundBaseView implements GetHomeSafeBetPresenter.GetHomeSafeBetListener, View.OnClickListener{

    private final String TAG = HomeSafeBetView.class.getSimpleName();
    private Activity    mActivity;
    private MyLineChart mMyLineChart;
    private MyXAxis     xAxisLine;
    private MyYAxis     axisLeftLine;
    private ProgressMarkView mProgressMarkView;
    private GetHomeSafeBetPresenter mPresenter;

    //稳赢理财
    private LinearLayout mViewGroup;
    private TextView     mPushTime ;//发行时间
    private TextView     mStauts ;  //状态， 预约中  认购中  预约已满。。。。。
    private TextView     mTypeName;
    private TextView     mRadio ;
    private TextView     mRadioTitle;//七日年化收益率 等标题、。。。
    private TextView     mProName;
    private TextView     mDay ;//投资期限
    private TextView     mDayTitle;
    private TextView     mPrice ;//起投金额
    private RiskControlView mRiskControlView;
    private List<NewOptionalFinancingEntity> mDatas = new ArrayList<>();
    private HomeFragment.JumpPageListener mJumpPageListener;

    public HomeSafeBetView (Activity activity, Object object) {
        super(activity, object);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity, Object object) {
        mActivity = activity;
        mJumpPageListener = (HomeFragment.JumpPageListener) object;
        mPresenter = new GetHomeSafeBetPresenter();
        initSafeBet(view);
        //progress
        mMyLineChart = (MyLineChart) view.findViewById(R.id.item_newoptional_chart);
        mMyLineChart.setVisibility(View.GONE);
        initChart(mMyLineChart);

        mPresenter.getSafeBetData(TAG, this);
    }

    private void initSafeBet(View view) {
        view.findViewById(R.id.moreLayout).setOnClickListener(this);
        mViewGroup = (LinearLayout) view.findViewById(R.id.finncingLayout);
        mViewGroup.setVisibility(View.GONE);
        mViewGroup.setOnClickListener(this);

        mPushTime = (TextView) view.findViewById(R.id.tv_text8);//发行时间
        mStauts = (TextView) view.findViewById(R.id.tv_text7);//状态， 预约中  认购中  预约已满。。。。。
        mTypeName = (TextView) view.findViewById(R.id.tv_text1);
        mRadio =  (TextView) view.findViewById(R.id.tv_text2);
        mRadioTitle = (TextView) view.findViewById(R.id.tv_text3);//七日年化收益率 等标题、。。。
        mProName = (TextView) view.findViewById(R.id.tv_text4);
        mDay = (TextView) view.findViewById(R.id.tv_text5);//投资期限
        mDayTitle = (TextView) view.findViewById(R.id.tv_text9);
        mPrice = (TextView) view.findViewById(R.id.tv_text6);//起投金额
        mRiskControlView = (RiskControlView) view.findViewById(R.id.rcv_progress);

    }

    private void initChart(MyLineChart myLineChart) {
        myLineChart.setScaleEnabled(false);
        myLineChart.setDrawBorders(false);
//        myLineChart.setBorderWidth(1);
//        myLineChart.setBorderColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.lineColor));
        myLineChart.setDescription("");
        Legend lineChartLegend = myLineChart.getLegend();
        lineChartLegend.setEnabled(false);

        //x轴
        xAxisLine = myLineChart.getXAxis();
        xAxisLine.setDrawLabels(false);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisLine.setDrawGridLines(false);
        xAxisLine.setDrawAxisLine(false);
        xAxisLine.setEnabled(false);

        //左边y
        axisLeftLine = myLineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(1, true);
        axisLeftLine.setDrawLabels(false);
        axisLeftLine.setDrawGridLines(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);
        myLineChart.getAxisRight().setEnabled(false);
        myLineChart.setTouchEnabled(false);
        myLineChart.setClickable(false);
        myLineChart.setDragEnabled(false);
        myLineChart.setDragDecelerationEnabled(false);

        mProgressMarkView = new ProgressMarkView(CustomApplication.getContext(), R.layout.mark_message);
        myLineChart.setMarkerView(mProgressMarkView);
    }

    private void setChartData(ArrayList<String> strDates) {

        if (strDates == null || strDates.size() == 0) {
            return;
        }

        ArrayList<Float> values = new ArrayList<>();
        for (String str : strDates) {
            float f = 1f;
            values.add(f);
        }

        xAxisLine.setXLabels(new CustomLabels().setXOnLabels(strDates));
        axisLeftLine.setStartAtZero(false);

        ArrayList<Entry> lineChartEntries = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {

            lineChartEntries.add(new Entry(values.get(i), i));
        }

        LineDataSet lineDataSet = new LineDataSet(lineChartEntries, "");

        lineDataSet.setDrawValues(false);
        lineDataSet.setCircleRadius(0);
//        lineDataSet.setColor(Color.parseColor("#daecff"));
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.newStockTitle));
        lineDataSet.setHighlightEnabled(false);
//        lineDataSet.setHighLightColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.newStockTitle));



        lineDataSet.setDrawFilled(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(lineDataSet);
        LineData cd = new LineData(new String[values.size()], sets);
        mMyLineChart.setData(cd);

        mMyLineChart.highlightValue(strDates.size()-1, 0);
//        mProgressMarkView.invalidate();
        mMyLineChart.invalidate();//刷新图
    }

    @Override
    public void onError(String error) {
        mViewGroup.setVisibility(View.GONE);
    }

    @Override
    public void getData(List<NewOptionalFinancingEntity> list) {
        mViewGroup.setVisibility(View.VISIBLE);
        mDatas = list;
        setHomeSafeBetData(list);
    }

    private void setHomeSafeBetData(List<NewOptionalFinancingEntity> list) {
        String prod_wfsy = list.get(0).getProd_wfsy();
        String prod_term = list.get(0).getProd_term();
        String prod_nhsy = list.get(0).getProd_nhsy();
        float risk = Float.valueOf(prod_nhsy);
        mRiskControlView.setMaxProgress(20);
        mRiskControlView.setProgress(risk);

        String prod_name = list.get(0).getProd_name();
        if (prod_name.length() > 6) {
            prod_name = prod_name.substring(0, 6) + "\n" + prod_name.substring(6);
        }

        mTypeName.setText(list.get(0).getProd_type_name());
        mRadio.setText(prod_nhsy + "%");

        mProName.setText(prod_name);
        if (TextUtils.isEmpty(prod_term)) {
            prod_term = "--";
        }
        if ("3".equals(list.get(0).getType())) {
            mRadioTitle.setText("年化收益率");
            mDayTitle.setText("投资期限");
            mDay.setText(prod_term + "天");
        } else {
            mDayTitle.setText("万份收益");
            mRadioTitle.setText("七日年化收益率");
            mDay.setText(prod_wfsy + "元");
        }

        String prod_qgje = list.get(0).getProd_qgje();
        if (TextUtils.isEmpty(prod_qgje)) {
            prod_qgje = "--";
        }
        mPrice.setText(prod_qgje + "元");
        String prod_status = list.get(0).getProd_status();

        if (!TextUtils.isEmpty(prod_status)) {
            switch (prod_status) {
                case "0":
                    prod_status = "未发布";
                    mRiskControlView.setProgressColor(ColorUtils.BT_GRAY);
                    mRadio.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mDay.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mPrice.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mStauts.setBackgroundResource(R.mipmap.bg_bule_item);

                    break;
                case "1":
                    prod_status = "预约中";
                    mRiskControlView.setProgressColor(ColorUtils.ORANGE);
                    mRadio.setTextColor(mActivity.getResources().getColor(R.color.blue));
                    mDay.setTextColor(mActivity.getResources().getColor(R.color.blue));
                    mPrice.setTextColor(mActivity.getResources().getColor(R.color.blue));
                    mStauts.setBackgroundResource(R.mipmap.bg_bule_item);
                    break;
                case "2":
                    prod_status = "预约已满";
                    mRiskControlView.setProgressColor(ColorUtils.BT_GRAY);
                    mRadio.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mDay.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mPrice.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mStauts.setBackgroundResource(R.mipmap.bg_bule_item);

                    break;
                case "3":
                    prod_status = "认购中";
                    mRiskControlView.setProgressColor(ColorUtils.ORANGE);
                    mRadio.setTextColor(mActivity.getResources().getColor(R.color.orange2));
                    mDay.setTextColor(mActivity.getResources().getColor(R.color.orange2));
                    mPrice.setTextColor(mActivity.getResources().getColor(R.color.orange2));
                    mStauts.setBackgroundResource(R.mipmap.bg_red_item);
                    break;
                case "4":
                    prod_status = "已售罄";
                    mRiskControlView.setProgressColor(ColorUtils.BT_GRAY);
                    mRadio.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mDay.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mPrice.setTextColor(mActivity.getResources().getColor(R.color.texts));
                    mStauts.setBackgroundResource(R.mipmap.bg_red_item);

                    break;
                case "-1":
                    prod_status = "NO.1 热销中";
                    mRiskControlView.setProgressColor(ColorUtils.ORANGE);
                    mRadio.setTextColor(mActivity.getResources().getColor(R.color.orange2));
                    mDay.setTextColor(mActivity.getResources().getColor(R.color.orange2));
                    mPrice.setTextColor(mActivity.getResources().getColor(R.color.orange2));
                    mStauts.setBackgroundResource(R.mipmap.bg_red_item);
                    break;
                default:
                    break;
            }
            mStauts.setText(prod_status);
        }else {
            mStauts.setText("NO.1 热销中");
            mRiskControlView.setProgressColor(ColorUtils.ORANGE);
            mRadio.setTextColor(mActivity.getResources().getColor(R.color.orange2));
            mDay.setTextColor(mActivity.getResources().getColor(R.color.orange2));
            mPrice.setTextColor(mActivity.getResources().getColor(R.color.orange2));
            mStauts.setBackgroundResource(R.mipmap.bg_red_item);
        }

        if ("3".equals(list.get(0).getType())) {
            mPushTime.setVisibility(View.VISIBLE);
        } else {
            mPushTime.setVisibility(View.INVISIBLE);
        }
        String date = list.get(0).getIpo_begin_date();
        if (TextUtils.isEmpty(date)) {
            date = "--";
        }
        mPushTime.setText("发行时间:" + date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finncingLayout:
                Intent intent = new Intent();
                if (mDatas != null && mDatas.size() > 0) {
                    String prod_status = mDatas.get(0).getProd_status();
                    intent.putExtra("productCode", mDatas.get(0).getProd_code());
                    String type = mDatas.get(0).getType();
                    intent.putExtra("TYPE", type);
                    intent.putExtra("prod_type", mDatas.get(0).getProd_type());
                    intent.putExtra("prod_nhsy", mDatas.get(0).getProd_nhsy());
                    intent.putExtra("prod_qgje", mDatas.get(0).getProd_qgje());
                    intent.putExtra("schema_id", mDatas.get(0).getSchema_id());
                    intent.putExtra("prod_code", mDatas.get(0).getProd_code());
                    intent.putExtra("target", "finncing");
                    intent.putExtra("ofund_risklevel_name", mDatas.get(0).getOfund_risklevel_name());
                    boolean register = Db_PUB_USERS.isRegister();
                    BRutil.menuNewSelect("Z1-4-4", mDatas.get(0).getSchema_id(), mDatas.get(0).getProd_code(), "2", new Date(), "-1", "-1");
                    if ("3".equals(type)) {
                        if (!register) {
                            intent.putExtra("Identify", "2");
                            intent.setClass(mActivity, ShouJiZhuCeActivity.class);
                        } else {
                            intent.putExtra("prod_status", prod_status);
                            intent.setClass(mActivity, ManagerMoenyDetailActivity.class);
                        }
                    } else {
                        intent.setClass(mActivity, ManagerMoenyDetailActivity.class);

                    }

                    mActivity.startActivity(intent);
                }

                break;

            case R.id.moreLayout:
                if (mJumpPageListener != null) {
                    HomeFragmentHelper.getInstance().gotoPager("稳赢理财", mActivity, mJumpPageListener, null);
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_newoptional_financing;
    }

    private class CustomLabels implements MoneyFundFundChartView.TypeXLabels{
        @Override
        public SparseArray<String> setXOnLabels(ArrayList<String> dates) {
            if (dates == null || dates.size() <= 0) {
                return null;
            }

            SparseArray<String> xLabels = new SparseArray<>();

            for (int i = 0; i < dates.size(); i++) {
                xLabels.put(i, "");
            }

            return xLabels;
        }
    }
}
