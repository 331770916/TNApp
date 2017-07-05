package com.tpyzq.mobile.pangu.activity.trade.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.tpyzq.mobile.pangu.adapter.trade.FJEntrustedDealListViewAdapter;
import com.tpyzq.mobile.pangu.adapter.trade.VoteQueryAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;

import java.util.Date;
import java.util.List;

/**
 * 投票查询page
 */

public class VoteQueryPager extends BasePager implements InterfaceCollection.InterfaceCallback, VoteQueryAdapter.ScallCallback {
    private TextView mtvStartTime, mtvFinishTime, fjInquire;
    private String TAG, startDate, finishDate, position = "";
    private TimePickerView startTime, finishTime;
    private boolean isScallBottom, mIsClean;
    private boolean isCustomRefresh = false;
    private Dialog mDialog, mistakeDialog;
    private PullToRefreshListView mListView;
    private VoteQueryAdapter mAdapter;
    private ImageView iv_isEmpty;
    private String mMarket = "1";
    private int refresh = 30;
    private List<NetworkVotingEntity> myList;

    public VoteQueryPager(Context context, String params) {
        super(context, params);
        this.TAG = params;
    }

    public void setmMarket(String market,boolean isRefresh){
        mMarket = market;
        if(isRefresh)
            refresh("", "30", false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.page_vote_query;
    }

    @Override
    public void setView(String params) {
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.listView_stock);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.isEmpty);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        if ("VoteQueryCustomPager".equals(params)) {
            LinearLayout fjTimepicker = (LinearLayout) rootView.findViewById(R.id.fjTimepicker);
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
    public void initData() {
        if (mAdapter == null) {
            mAdapter = new VoteQueryAdapter(mContext, getType());
            mAdapter.setCallback(this);
            mListView.setAdapter(mAdapter);
            mListView.setEmptyView(iv_isEmpty);
            if (!"VoteQueryCustomPager".equals(TAG))
                mDialog.show();
            refresh("", "30", false);
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    if (mListView.isShownHeader()) {
                        mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                        mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                        mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
//                  释放开始刷新
                        mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(1500);
                            }
                        });
                        if ("VoteQueryCustomPager".equals(TAG) && !isCustomRefresh) {
                            mListView.onRefreshComplete();
                        } else {
                            refresh("", String.valueOf(refresh), false);
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
                        refresh(position, "30", true);
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
    }

    public void refresh(String page, String num, boolean isClean) {

        switch (getType()) {
            case 0:
                if(TAG.equals("VoteQueryTodayPager")){
                    ifc.queryTodayVoting(mSession, mMarket, page,num,  TAG, this);
                }else if (TAG.equals("VoteQueryOneWeekPager")) {
                    ifc.queryHistoryNetworkVoting(mSession, "1", mMarket,page,num, null, null, TAG, this);
                } else if (TAG.equals("VoteQueryInAMonthPager")) {
                    ifc.queryHistoryNetworkVoting(mSession, "2",mMarket, page, num,null, null, TAG, this);
                } else if (TAG.equals("VoteQueryThreeWeekPager")) {
                    ifc.queryHistoryNetworkVoting(mSession, "3", mMarket,page, num,null, null, TAG, this);
                } else if (TAG.equals("VoteQueryCustomPager") && !TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(finishDate)) {
                    ifc.queryHistoryNetworkVoting(mSession, "0",mMarket, page, num,startDate, finishDate, TAG, this);
                }
                break;
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
            if (!mIsClean && myList != null)
                myList.clear();
            Object object = info.getData();
            if (object instanceof List) {
                myList = (List<NetworkVotingEntity>) object;
                if (myList.size() > 0) {
                    position = myList.get(myList.size()-1).getPosition_str();
                    if(mIsClean)
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
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
        mListView.onRefreshComplete();
            }
        },500);


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
        mListView.removeAllViews();
        mListView = null;
        iv_isEmpty = null;
        mtvStartTime = null;
        mtvFinishTime = null;
        fjInquire = null;
        startDate = null;
        finishDate = null;
    }


}
