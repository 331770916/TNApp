package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.BaseSearchPager;
import com.tpyzq.mobile.pangu.adapter.trade.EntrustListViewAdapter;
import com.tpyzq.mobile.pangu.data.TodayEntrustEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by wangqi on 2016/8/15.
 * 委托 自定义  标签
 */
public class EntrustCustomPager extends BaseSearchPager {
    private String TAG = "EntrustCustomPager";
    private PullToRefreshListView mWtListView;
    private TextView mWtStartDate, mWtfinishDate;
    private TextView mWtInquire;
    private TimePickerView mWtStartpvTime;
    private TimePickerView mWtFinishpvTime;
    private List<TodayEntrustEntity> mBeans;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private EntrustListViewAdapter mAdapter;
    private ImageView iv_isEmpty;
    private RelativeLayout kong_null;
    private Dialog mDialog;

    public EntrustCustomPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mWtStartDate = (TextView) rootView.findViewById(R.id.EntrustStartDate);
        mWtfinishDate = (TextView) rootView.findViewById(R.id.EntrustFinishDate);
        mWtInquire = (TextView) rootView.findViewById(R.id.EntrustInquire);
        mWtListView = (PullToRefreshListView) rootView.findViewById(R.id.EntrustListView);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.EAMP_Kong_Null);
        mWtStartDate.setText(Helper.getBeforeString());
        mWtfinishDate.setText(Helper.getBeforeString());
        mBeans = new ArrayList<>();
        mWtStartDate.setOnClickListener(new MyOnClickListenr());
        mWtfinishDate.setOnClickListener(new MyOnClickListenr());
        mWtInquire.setOnClickListener(new MyOnClickListenr());
        mAdapter = new EntrustListViewAdapter(mContext);
        mWtListView.setAdapter(mAdapter);
        mWtListView.setEmptyView(iv_isEmpty);
        mWtStartDate.setTextColor(Color.parseColor("#368de7"));
        mWtfinishDate.setTextColor(Color.parseColor("#368de7"));
        toStartDate();
        toFinishDate();
        Update();

    }


    @Override
    public void initData() {
    }

    /**
     * 开始日期
     */
    private void toStartDate() {
        mWtStartpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mWtStartpvTime.setTime(Helper.getBeforeDate());
        mWtStartpvTime.setCyclic(false);
        mWtStartpvTime.setCancelable(true);
        mWtStartpvTime.setTitle("选择日期");
        //时间选择后回调
        mWtStartpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mWtStartDate.setText(simpleDateFormat.format(date));
            }
        });
    }

    /***
     * 截至日期
     */
    private void toFinishDate() {
        mWtFinishpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mWtFinishpvTime.setTime(Helper.getBeforeDate());
        mWtFinishpvTime.setCyclic(false);
        mWtFinishpvTime.setCancelable(true);
        mWtFinishpvTime.setTitle("选择日期");
        //时间选择后回调
        mWtFinishpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mWtfinishDate.setText(simpleDateFormat.format(date));
            }
        });
    }


    private void toConnect(String i, String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300171");
        map.put("token", mSession);
        map.put("parms", map2);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", Helper.getMyDate(mWtStartDate.getText().toString()));
        map2.put("END_DATE", Helper.getMyDate(mWtfinishDate.getText().toString()));
        map2.put("MARKET", "");
        map2.put("KEY_STR", i);
        map2.put("REC_COUNT", j);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                CentreToast.showText(mContext,ConstantUtil.NETWORK_ERROR);
                kong_null.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (!flag){
                            mBeans.clear();
                        }
                        if (data != null && data.length() > 0) {
                            if (flag) {
                                if (data.length() > 1) {
                                    position = data.getJSONObject(data.length() - 1).getString("KEY_STR");
                                }
                                refresh += 30;
                            } else {
                                position = data.getJSONObject(data.length() - 1).getString("KEY_STR");
                            }
                            for (int i = 0; i < data.length(); i++) {

                                TodayEntrustEntity _bean = new TodayEntrustEntity();
                                _bean.setName(data.getJSONObject(i).getString("SECU_NAME"));
                                _bean.setNum(data.getJSONObject(i).getString("PRICE"));
                                _bean.setDate(data.getJSONObject(i).getString("ORDER_DATE")); //日期
                                _bean.setTime(data.getJSONObject(i).getString("ORDER_TIME")); //时间
                                String QTY = data.getJSONObject(i).getString("QTY");                  //委托
                                if (!"0".equals(QTY)) {
                                    int qty_idx = QTY.lastIndexOf(".");
                                    String QTY_New = QTY.substring(0, qty_idx);
                                    _bean.setEntrust(QTY_New);
                                } else {
                                    _bean.setEntrust(QTY);
                                }

                                String MATCHED_QTY = data.getJSONObject(i).getString("MATCHED_QTY");  //成交
                                if (!"0".equals(MATCHED_QTY)) {
                                    int matched_qty_idx = MATCHED_QTY.lastIndexOf(".");
                                    String MATCHED_QTY_New = MATCHED_QTY.substring(0, matched_qty_idx);
                                    _bean.setSucceed(MATCHED_QTY_New);
                                } else {
                                    _bean.setSucceed(MATCHED_QTY);
                                }


                                if (data.getJSONObject(i).getString("IS_WITHDRAW").equals("0")) {
                                    _bean.setTransaction(data.getJSONObject(i).getString("ENTRUST_BS"));
                                } else if (data.getJSONObject(i).getString("IS_WITHDRAW").equals("2")) {
                                    _bean.setTransaction("3");
                                }

                                _bean.setStatus(data.getJSONObject(i).getString("STATUS_NAME"));
                                mBeans.add(_bean);
                            }
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                            mAdapter.setData(mBeans);
                            mAdapter.notifyDataSetChanged();
                            sure = data.length();
                        } else {
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                            CentreToast.showText(mContext,"暂无数据");
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
//                        ResultDialog.getInstance().showText(jsonObject.getString("msg"));
                        kong_null.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    class MyOnClickListenr implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.EntrustStartDate:
                    mWtStartpvTime.show();
                    break;
                case R.id.EntrustFinishDate:
                    mWtFinishpvTime.show();
                    break;
                case R.id.EntrustInquire:
                    if (!TextUtils.isEmpty(mWtStartDate.getText().toString()) && !TextUtils.isEmpty(mWtfinishDate.getText().toString())) {

                        String startDay = Helper.getMyDate(mWtStartDate.getText().toString());
                        String endDay = Helper.getMyDate(mWtfinishDate.getText().toString());

                        String str = Helper.compareTo(startDay, endDay);

                        int days = Helper.daysBetween(startDay, endDay);

                        if (str.equalsIgnoreCase(startDay)) {
                            MistakeDialog.showDialog("请选择正确日期,起始日期不能超过截止日期", (Activity) mContext);
                        } else if (days > 90) {
                            MistakeDialog.showDialog("选择的日期间隔不能超过3个月", (Activity) mContext);
                        } else {
                            mDialog = LoadingDialog.initDialog((Activity) mContext, "正在查询...");
                            mDialog.show();
                            mBeans.clear();
                            mAdapter.notifyDataSetChanged();
                            toConnect("", "30", false);
                        }
                    }
                    break;
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_custom;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    toConnect("", "30", false);
                    break;
                case 1:
                    toConnect(position, "30", true);
                    break;
            }
        }
    };


    /**
     * 刷新 加载   数据
     */
    private void Update() {
        mWtListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mWtListView.isShownHeader()) {
                    //设置头布局样式文字
                    mWtListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mWtListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mWtListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                handler.sendEmptyMessage(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            //将下拉视图收起
                            mWtListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mWtListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mWtListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mWtListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mWtListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            //将下拉视图收起
                            mWtListView.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });
    }

}
