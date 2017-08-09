package com.tpyzq.mobile.pangu.activity.trade.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.ETFHistoryAdapter;
import com.tpyzq.mobile.pangu.adapter.trade.VoteQueryAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;

import java.util.Date;
import java.util.List;

import static com.tpyzq.mobile.pangu.activity.trade.stock.ETFHistoryInquireActivity.fragmentManager;

/**
 * Created by wangqi on 2017/7/3.
 * ETF申赎历史查询
 */

public class ETFQueryPager extends BasePager implements InterfaceCollection.InterfaceCallback, ETFHistoryAdapter.ScallCallback {
    private TextView mtvStartTime, mtvFinishTime, fjInquire;
    private String TAG, startDate, finishDate, position = "";
    private TimePickerView startTime, finishTime;
    private boolean isScallBottom, mIsClean;
    private boolean isCustomRefresh = false;
    private Dialog mDialog, mistakeDialog;
    private PullToRefreshListView mListView;
    private ETFHistoryAdapter mAdapter;
    private ImageView iv_isEmpty;
    private int refresh = 30;
    private String token;

    private List<EtfDataEntity> myList;

    public ETFQueryPager(Context context, String params) {
        super(context, params);
        this.TAG = params;
        token = SpUtils.getString(context, "mSession", "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.pager_history_query;
    }

    @Override
    public void setView(String params) {
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.listView_stock);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.isEmpty);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if ("ETFQueryCustomPager".equals(params)) {
            LinearLayout fjTimepicker = (LinearLayout) rootView.findViewById(R.id.fjTimepicker);
            fjTimepicker.setVisibility(View.VISIBLE);
            mtvStartTime = (TextView) rootView.findViewById(R.id.fjstartDate);
            mtvFinishTime = (TextView) rootView.findViewById(R.id.fjfinishDate);
            fjInquire = (TextView) rootView.findViewById(R.id.fjInquire);
            fjInquire.setOnClickListener(new MyOnClickListenr());
            mtvStartTime.setText(helper.getBeforeString());
            mtvStartTime.setTextColor(Color.parseColor("#368de7"));
            mtvStartTime.setOnClickListener(new MyOnClickListenr());
            mtvFinishTime.setText(helper.getBeforeString());
            mtvFinishTime.setOnClickListener(new MyOnClickListenr());
            mtvFinishTime.setTextColor(Color.parseColor("#368de7"));
            startTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
            startTime.setTime(Helper.getBeforeDate());
            startTime.setCyclic(false);
            startTime.setCancelable(true);
            startTime.setTitle("选择日期");
            startTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                @Override
                public void onTimeSelect(Date date) {
                    mtvStartTime.setText(simpleDateFormat.format(date));
                }
            });
            finishTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
            finishTime.setTime(Helper.getBeforeDate());
            finishTime.setCyclic(false);
            finishTime.setCancelable(true);
            finishTime.setTitle("选择日期");
            //时间选择后回调
            finishTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                @Override
                public void onTimeSelect(Date date) {
                    mtvFinishTime.setText(simpleDateFormat.format(date));
                }
            });
        }
        mDialog = LoadingDialog.initDialog((Activity) mContext, "正在查询...");
    }


    class MyOnClickListenr implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fjstartDate:
                    startTime.show();
                    break;
                case R.id.fjfinishDate:
                    finishTime.show();
                    break;
                case R.id.fjInquire:
                    startDate = helper.getMyDate(mtvStartTime.getText().toString());
                    finishDate = helper.getMyDate(mtvFinishTime.getText().toString());
                    if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(finishDate)) {
                        String str = helper.compareTo(startDate, finishDate);
                        int days = helper.daysBetween(startDate, finishDate);
                        if (str.equalsIgnoreCase(startDate)) {
                            showDialog("请选择正确日期,起始日期不能超过截止日期");
                        } else if (days > 90) {
                            showDialog("选择的日期间隔不能超过3个月");
                        } else {
                            isCustomRefresh = true;
                            mDialog.show();
                            mAdapter.notifyDataSetChanged();
                            refresh(position, "30", false, startDate, finishDate);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void initData() {
        if (mAdapter == null) {
            mAdapter = new ETFHistoryAdapter(mContext);
            reuse();
        }

    }

    private void reuse() {
        mAdapter.setCallback(this);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(iv_isEmpty);
        if (!"ETFQueryCustomPager".equals(TAG))
            mDialog.show();
        refresh("", "30", false, "", "");
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mListView.isShownHeader()) {
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    if ("ETFQueryCustomPager".equals(TAG) && !isCustomRefresh) {

                        mListView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mListView.onRefreshComplete();
                            }
                        }, 500);

                    } else {
                        refresh("", String.valueOf(refresh), false, "", "");
                    }

                } else if (mListView.isShownFooter()) {
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    mListView.post(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1500);
                        }
                    });
                    refresh(position, "30", true, "", "");
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            View bottom = view.getChildAt(view.getLastVisiblePosition() - view.getFirstVisiblePosition());
                            if (view.getHeight() >= bottom.getBottom())
                                isScallBottom = true;
                            else
                                isScallBottom = false;
                        } else
                            isScallBottom = false;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void refresh(String page, String num, boolean isClean, String begin_date, String end_date) {

        if (TAG.equals("ETFQueryTodayPager")) {
            ifc.queryEntrust(token, "0", page, num, TAG, this);
        } else if (TAG.equals("ETFQueryOneWeekPager")) {
            ifc.queryHistory(token, "", "", "1", page, num, TAG, this);
        } else if (TAG.equals("ETFQueryInAMonthPager")) {
            ifc.queryHistory(token, "", "", "2", page, num, TAG, this);
        } else if (TAG.equals("ETFQueryThreeWeekPager")) {
            ifc.queryHistory(token, "", "", "3", page, num, TAG, this);
        } else if (TAG.equals("ETFQueryCustomPager")) {
            ifc.queryHistory(token, begin_date, end_date, "0", page, num, TAG, this);
        }

        mIsClean = isClean;
    }


    @Override
    public void callScall() {
        if (isScallBottom) {
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.getRefreshableView().setSelection(mListView.getBottom());
                }
            });
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        if (mDialog != null)
            mDialog.dismiss();
        String code = info.getCode();
        if ("0".equals(code)) {
            if (!mIsClean && myList != null) {
                myList.clear();
                mAdapter.notifyDataSetChanged();
            }
            Object object = info.getData();
            if (object instanceof List) {
                myList = (List<EtfDataEntity>) object;
                if (myList.size() > 0) {
                    mAdapter.setData(myList);
                }
            }
        } else if ("-6".equals(code)) {
            skip.startLogin(mContext);
        }else if ("400".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {   //  网络错误 解析错误 其他
            CentreToast.showText(mContext, info.getMsg());
        } else{
            showDialog(info.getMsg());
        }
        mListView.onRefreshComplete();

    }

    public void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(fragmentManager,ETFQueryPager.class.toString());
    }

    @Override
    public void destroy() {
        net.cancelSingleRequestByTag(TAG);
        if (mistakeDialog != null) {
            mistakeDialog.dismiss();
            mistakeDialog = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mAdapter = null;
        mListView = null;
        iv_isEmpty = null;
        mtvStartTime = null;
        mtvFinishTime = null;
        fjInquire = null;
        startDate = null;
        finishDate = null;
    }
}