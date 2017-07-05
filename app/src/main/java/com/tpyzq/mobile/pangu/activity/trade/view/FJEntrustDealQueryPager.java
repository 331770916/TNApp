package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FJEntrustedDealListViewAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;

import java.util.Date;
import java.util.List;

/**
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: entrust deal query 委托0 成交查询1
 */
public class FJEntrustDealQueryPager extends BasePager implements InterfaceCollection.InterfaceCallback, FJEntrustedDealListViewAdapter.ScallCallback {
    private TextView mtvStartTime, mtvFinishTime, fjInquire;
    private String TAG, startDate, finishDate, position = "";
    private FJEntrustedDealListViewAdapter mAdapter;
    private TimePickerView startTime, finishTime;
    private List<StructuredFundEntity> myList;
    private PullToRefreshListView listView;
    private boolean isScallBottom, mIsClean;
    private Dialog mistakeDialog, mDialog;
    private LinearLayout fjTimepicker;
    private ImageView iv_isEmpty;
    private int refresh = 30;
    private boolean isCustomRefresh = false;

    public FJEntrustDealQueryPager(Context context, String params) {
        super(context, params);
        TAG = params;
    }

    @Override
    public void setView(String params) {
        listView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        if ("EntrustCustomPager".equals(params) || "DealCustomPager".equals(params)) {
            fjTimepicker = (LinearLayout) rootView.findViewById(R.id.fjTimepicker);
            fjTimepicker.setVisibility(View.VISIBLE);
            mtvStartTime = (TextView) rootView.findViewById(R.id.fjstartDate);
            mtvFinishTime = (TextView) rootView.findViewById(R.id.fjfinishDate);
            fjInquire = (TextView) rootView.findViewById(R.id.fjInquire);
            fjInquire.setOnClickListener(new MyOnClickListenr());
            mtvStartTime.setText(helper.getCurDate());
            mtvStartTime.setTextColor(Color.parseColor("#368de7"));
            mtvStartTime.setOnClickListener(new MyOnClickListenr());
            mtvFinishTime.setText(helper.getCurDate());
            mtvFinishTime.setOnClickListener(new MyOnClickListenr());
            mtvFinishTime.setTextColor(Color.parseColor("#368de7"));
            startTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
            startTime.setTime(new Date());
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
            finishTime.setTime(new Date());
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
                            mistakeDialog = MistakeDialog.showDialog("请选择正确日期,起始日期不能超过截止日期", (Activity) mContext);
                        } else if (days > 90) {
                            mistakeDialog = MistakeDialog.showDialog("选择的日期间隔不能超过3个月", (Activity) mContext);
                        } else {
                            isCustomRefresh = true;
                            mDialog.show();
                            mAdapter.notifyDataSetChanged();
                            refresh(position, "30", false);
                        }
                    }
                    break;
            }
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
                myList = (List<StructuredFundEntity>) object;
                if (myList.size() > 0) {
                    position = myList.get(myList.size() - 1).getPosition_str();
                    if (mIsClean)
                        refresh += 30;
                    mAdapter.setData(myList);
                } else {
                    helper.showToast(mContext, " 暂无数据");
                }
            }
        } else if ("-6".equals(code)) {
            skip.startLogin(mContext);
        } else {//-1,-2,-3情况下显示定义好信息
            helper.showToast(mContext, info.getMsg());
        }
        listView.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.page_fj_entrustquery_today;
    }

    @Override
    public void callScall() {//回调底部最后一个item点击时滚动
        if (isScallBottom) {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.getRefreshableView().setSelection(listView.getBottom());
                }
            });
        }
    }

    @Override
    public void initData() {
        if (mAdapter == null) {
            mAdapter = new FJEntrustedDealListViewAdapter(mContext, getType());
            mAdapter.setCallback(this);
            listView.setAdapter(mAdapter);
            listView.setEmptyView(iv_isEmpty);
            if (!"EntrustCustomPager".equals(TAG) && !"DealCustomPager".equals(TAG))
                mDialog.show();
            refresh("", "30", false);
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    if (listView.isShownHeader()) {
                        listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                        listView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                        listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                        if ("EntrustCustomPager".equals(TAG) && !isCustomRefresh) {
                                listView.onRefreshComplete();
                        } else if ("DealCustomPager".equals(TAG) && !isCustomRefresh) {
                            listView.onRefreshComplete();
                        } else {
                            refresh("", String.valueOf(refresh), false);
                        }
                    } else if (listView.isShownFooter()) {
                        listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                        listView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                        listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                        if ("EntrustCustomPager".equals(TAG) && !isCustomRefresh) {
                            listView.onRefreshComplete();
                        } else if ("DealCustomPager".equals(TAG) && !isCustomRefresh) {
                            listView.onRefreshComplete();
                        } else {
                            refresh(position, "30", true);
                        }


                    }
                }
            });
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
    }

    public void refresh(String page, String num, boolean isClean) {
        switch (getType()) {
            case 0://委托
                if (TAG.equals("EntrustTodayPager")) {
                    ifc.queryTodayEntrust(mSession, page, num, "1", TAG, this);
                } else if (TAG.equals("EntrustOneWeekPager")) {
                    ifc.queryHistoryEntrust(mSession, page, num, "1", "", "", TAG, this);
                } else if (TAG.equals("EntrustInAMonthPager")) {
                    ifc.queryHistoryEntrust(mSession, page, num, "2", "", "", TAG, this);
                } else if (TAG.equals("EntrustThreeWeekPager")) {
                    ifc.queryHistoryEntrust(mSession, page, num, "3", "", "", TAG, this);
                } else if (TAG.equals("EntrustCustomPager") && !TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(finishDate)) {
                    ifc.queryHistoryEntrust(mSession, page, num, "0", startDate, finishDate, TAG, this);
                }
                break;
            case 1://成交
                if (TAG.equals("DealTodayPager")) {
                    ifc.queryTodayDeal(mSession, page, num, TAG, this);
                } else if (TAG.equals("DealOneWeekPager")) {
                    ifc.queryHistoryDeal(mSession, page, num, "1", "", "", TAG, this);
                } else if (TAG.equals("DealInAMonthPager")) {
                    ifc.queryHistoryDeal(mSession, page, num, "2", "", "", TAG, this);
                } else if (TAG.equals("DealThreeWeekPager")) {
                    ifc.queryHistoryDeal(mSession, page, num, "3", "", "", TAG, this);
                } else if (TAG.equals("DealCustomPager") && !TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(finishDate)) {
                    ifc.queryHistoryDeal(mSession, page, num, "0", startDate, finishDate, TAG, this);
                }
                break;
        }
        mIsClean = isClean;
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
        listView = null;
        iv_isEmpty = null;
        mtvStartTime = null;
        mtvFinishTime = null;
        fjInquire = null;
        startDate = null;
        finishDate = null;
    }
}
