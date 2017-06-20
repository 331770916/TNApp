package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.interfac.IgetRectListener;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.CalendarNewStockAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;
import com.tpyzq.mobile.pangu.interfac.CalendarDecorator;
import com.tpyzq.mobile.pangu.interfac.PrimeDayDisableDecorator;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangwnebo on 2016/8/13.
 * 新股日历
 */
public class CalendarNewStockActivity extends BaseActivity implements View.OnClickListener, OnDateSelectedListener, OnMonthChangedListener, AdapterView.OnItemClickListener {

    private final String TAG = "CalendarNewStockActivity";

    private TextView mPublishTv;
    private MaterialCalendarView mCalendarView;
    private CalendarNewStockAdapter mAdapter;
    private NewStockEnitiy mEntity;
    private ArrayList<NewStockEnitiy.DataBeanToday> mAdapterDatas;
    private Button mNewBtn;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private int radius;

    @Override
    public void initView() {

        Intent intent = getIntent();

        mEntity = intent.getParcelableExtra("entiity");


        findViewById(R.id.publish_back).setOnClickListener(this);
        findViewById(R.id.publish_request).setOnClickListener(this);
        mNewBtn = (Button) findViewById(R.id.oneBtnNew);
        mNewBtn.setOnClickListener(this);

        MyListView listView = (MyListView) findViewById(R.id.newStockCalendarListView);
        mAdapter = new CalendarNewStockAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        mCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        mPublishTv = (TextView) findViewById(R.id.publishTv);

        mCalendarView.setTileWidthDp(45);
        mCalendarView.setTileHeightDp(32);


        mCalendarView.setAllowClickDaysOutsideCurrentMonth(false);
        mCalendarView.setOnDateChangedListener(this);
        mCalendarView.setOnMonthChangedListener(this);
        mPublishTv.setText(getSelectedDatesString());

        final List<CalendarDay> calendarDays = new ArrayList<>();

        boolean falg = false;

        if (mEntity == null) {
            return;
        }

        if (null != mEntity.getData() && mEntity.getData().size() > 0) {
            for (NewStockEnitiy.DataBeanToday _bean : mEntity.getData()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(_bean.getONLINESTARTDATE());
                    CalendarDay calendarDay = CalendarDay.from(date);
                    calendarDays.add(calendarDay);
                    mCalendarView.addDecorator(new PrimeDayDisableDecorator(calendarDays));

                    if ("N".equalsIgnoreCase(_bean.getISTODAY())) {
                        falg = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LogHelper.e(TAG, e.toString());
                }
            }


            mCalendarView.setRectListener(new IgetRectListener() {
                @Override
                public void getRectBound(int bottom, int left, int right, int top) {
                    LogHelper.i(TAG, "bottom: --->" + bottom + "left: --->" +  left + "right ---->" +  right + "top---->" + top);

//                    int _radius = ((bottom + top) / 2) - ((bottom + top) / 8);
                    int _radius = ((bottom + top) / 2);
                    if (radius != _radius) {
                        mCalendarView.addDecorator(new CalendarDecorator(ContextCompat.getColor(CustomApplication.getContext(), R.color.orange), _radius - 2, calendarDays));
                    }
                    radius = _radius;

                }
            });


            mCalendarView.setSelectedDate(calendarDays.get(0));
            mPublishTv.setText(getSelectedDatesString() + "发行");

            if (mEntity != null && mEntity.getData() != null && mEntity.getData().size() > 0) {
                mAdapterDatas = disposeDatas(calendarDays.get(0), mEntity.getData());
                mAdapter.setDatas(mAdapterDatas);
            }


            if (falg) {
                mNewBtn.setClickable(true);
                mNewBtn.setFocusable(true);
                mNewBtn.setFilterTouchesWhenObscured(true);
                mNewBtn.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.drawable.one_new_btnbg));
            } else {
                mNewBtn.setClickable(false);
                mNewBtn.setFocusable(false);
                mNewBtn.setFilterTouchesWhenObscured(false);
                mNewBtn.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.drawable.one_new_disbtnbg));
            }
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse("1970-1-1");
                CalendarDay calendarDay = CalendarDay.from(date);
                calendarDays.add(calendarDay);
                mCalendarView.addDecorator(new PrimeDayDisableDecorator(calendarDays));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("name", mAdapterDatas.get(position).getISSUENAMEABBR_ONLINE());
        intent.putExtra("number", mAdapterDatas.get(position).getSECUCODE());
        intent.setClass(CalendarNewStockActivity.this, PublishNewStockDetail.class);
        startActivity(intent);
    }

    /**
     * 数据处理
     *
     * @param _calendarDay 天数
     * @param datas        数据源
     */
    private ArrayList<NewStockEnitiy.DataBeanToday> disposeDatas(CalendarDay _calendarDay, List<NewStockEnitiy.DataBeanToday> datas) {

        ArrayList<NewStockEnitiy.DataBeanToday> beans = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (NewStockEnitiy.DataBeanToday bean : datas) {

            try {
                String onLineDate = bean.getONLINESTARTDATE();
                Date date = sdf.parse(onLineDate);
                CalendarDay calendarDay = CalendarDay.from(date);
                if (calendarDay.equals(_calendarDay)) {
                    beans.add(bean);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return beans;
    }

    private boolean juedgeDate(CalendarDay selectedDate) {
        boolean falg = false;
        try {

            if (null != mEntity.getData() && mEntity.getData().size() > 0) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                for (NewStockEnitiy.DataBeanToday bean : mEntity.getData()) {

                    String onLineDate = bean.getONLINESTARTDATE();
                    Date date = sdf.parse(onLineDate);
                    CalendarDay calendarDay = CalendarDay.from(date);

                    if (selectedDate.equals(calendarDay) && bean.getISTODAY().equalsIgnoreCase("N")) {
                        falg = true;
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return falg;
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mPublishTv.setText(getSelectedDatesString() + "发行");

        if (mEntity != null && mEntity.getData() != null && mEntity.getData().size() > 0) {
            mAdapterDatas = disposeDatas(date, mEntity.getData());

            mAdapter.setDatas(mAdapterDatas);
        }



        if (juedgeDate(date)) {
            mNewBtn.setClickable(true);
            mNewBtn.setFocusable(true);
            mNewBtn.setFilterTouchesWhenObscured(true);
            mNewBtn.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.drawable.one_new_btnbg));
        } else {
            mNewBtn.setClickable(false);
            mNewBtn.setFocusable(false);
            mNewBtn.setFilterTouchesWhenObscured(false);
            mNewBtn.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.drawable.one_new_disbtnbg));
        }

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_back:
                finish();
                break;
            case R.id.publish_request:
                Intent intent = new Intent();
                HomeFragmentHelper.getInstance().gotoPage(this,TransactionLoginActivity.PAGE_INDEX_ManagerMySubscribeActivity,intent);
//                intent.setClass(CalendarNewStockActivity.this, MySubscribeActivity.class);
//                startActivity(intent);
                break;
            case R.id.oneBtnNew:
                Intent intent1 = new Intent();
                HomeFragmentHelper.getInstance().gotoPage(this,TransactionLoginActivity.PAGE_INDEX_OneKeySubscribe,intent1);

                break;
        }
    }

    private String getSelectedDatesString() {
        CalendarDay date = mCalendarView.getSelectedDate();
        if (date == null) {
            mNewBtn.setClickable(false);
            mNewBtn.setFocusable(false);
            mNewBtn.setFilterTouchesWhenObscured(false);
            mNewBtn.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.drawable.one_new_disbtnbg));

            return "未选中任何新股";
        }
        return FORMATTER.format(date.getDate());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_calendar_newstock;
    }
}
