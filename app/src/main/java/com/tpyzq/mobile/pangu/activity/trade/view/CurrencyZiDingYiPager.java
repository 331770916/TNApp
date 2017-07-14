package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.BaseSearchPager;
import com.tpyzq.mobile.pangu.adapter.trade.HBJJTodayPagerAdapter;
import com.tpyzq.mobile.pangu.data.CurrencyFundEntrustThreeMonthBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/8/24 10:52
 */
public class CurrencyZiDingYiPager extends BaseSearchPager implements TimePickerView.OnTimeSelectListener {

    private String TAG = "ZiDingYiPager";
    private PullToRefreshListView mListView;
    private TextView tvZDYStartDate, tvZDYEndDate, tvZDYQueryBtn = null;
    private ArrayList<HashMap<String, String>> list;
    private HBJJTodayPagerAdapter adapter;
    private TimePickerView mPvTime;
    private boolean mJuedgeTv;
    private SimpleDateFormat mFormate;
    private ImageView isEmpty;
    private String position;
    private Dialog mDialog;

    public CurrencyZiDingYiPager(Context context) {
        super(context);
    }


    @Override
    public void setView() {
        rootView.findViewById(R.id.llZiDingYi).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.llZiDingYiView).setVisibility(View.VISIBLE);
        mFormate = new SimpleDateFormat("yyyy-MM-dd");                                   //实例化时间格式

        mPvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);     //实例化选择时间空间
        mPvTime.setTime(new Date());
        mPvTime.setCyclic(false);
        mPvTime.setCancelable(true);
        mPvTime.setTitle("选择日期");
        mPvTime.setOnTimeSelectListener(this);                                         //时间选择后回调

        tvZDYStartDate = (TextView) rootView.findViewById(R.id.tvZDYStartDate);     //开始时间
        tvZDYEndDate = (TextView) rootView.findViewById(R.id.tvZDYEndDate);         //结束时间
        tvZDYQueryBtn = (TextView) rootView.findViewById(R.id.tvZDYQueryBtn);       //查询按钮

        tvZDYStartDate.setText(mFormate.format(new Date()));
        tvZDYEndDate.setText(mFormate.format(new Date()));

        //添加监听
        tvZDYStartDate.setOnClickListener(new MyOnClickListenr());
        tvZDYEndDate.setOnClickListener(new MyOnClickListenr());
        tvZDYQueryBtn.setOnClickListener(new MyOnClickListenr());
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.rlRefresh);
        isEmpty = (ImageView) rootView.findViewById(R.id.isEmpty);
        mDialog = LoadingDialog.initDialog((Activity) mContext, "加载中...");
    }

    @Override
    public int getLayoutId() {
        return R.layout.currencyfund_todaypager;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<HashMap<String, String>>();
        adapter = new HBJJTodayPagerAdapter(mContext);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(isEmpty);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  上拉
                refresh("", "30", false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  下拉
                refresh(position, "30", true);
            }
        });
    }


    class MyOnClickListenr implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvZDYStartDate:       //点击开始
                    mJuedgeTv = true;
                    mPvTime.show();
                    break;
                case R.id.tvZDYEndDate:         //点击结束
                    mJuedgeTv = false;
                    mPvTime.show();
                    break;
                case R.id.tvZDYQueryBtn:
                    if (!TextUtils.isEmpty(tvZDYStartDate.getText().toString()) && !TextUtils.isEmpty(tvZDYEndDate.getText().toString())) {

                        String startDay = Helper.getMyDate(tvZDYStartDate.getText().toString());
                        String endDay = Helper.getMyDate(tvZDYEndDate.getText().toString());

                        String str = Helper.compareTo(startDay, endDay);

                        int days = Helper.daysBetween(startDay, endDay);

                        if (str.equalsIgnoreCase(startDay)) {
                            MistakeDialog.showDialog("请选择正确日期,起始日期不能超过截止日期", (Activity) mContext);
                        } else if (days > 90) {
                            MistakeDialog.showDialog("选择的日期间隔不能超过3个月", (Activity) mContext);
                        } else {
                            mDialog = LoadingDialog.initDialog((Activity) mContext, "正在查询...");
                            mDialog.show();
                            list.clear();
                            adapter.notifyDataSetChanged();
                            refresh("", "30", false);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onTimeSelect(Date date) {
        if (mJuedgeTv) {
            tvZDYStartDate.setText(mFormate.format(date));
        } else {
            tvZDYEndDate.setText(mFormate.format(date));
        }

    }

    public void refresh(String page, String num, final boolean isClean) {
        String startDate = tvZDYStartDate.getText().toString();
        String newStartDate = startDate.replaceAll("-", "");
        String endDate = tvZDYEndDate.getText().toString();
        String newEndDate = endDate.replaceAll("-", "");
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", newStartDate);
        map2.put("END_DATE", newEndDate);
        map2.put("KEY_STR", page);
        map2.put("REQUEST_NUM", num);
        map1.put("funcid", "300445");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                mListView.onRefreshComplete();
                Helper.getInstance().showToast(mContext, ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                mListView.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CurrencyFundEntrustThreeMonthBean>() {
                }.getType();
                CurrencyFundEntrustThreeMonthBean threeMonthBean = gson.fromJson(response, type);
                String code = threeMonthBean.getCode();
                List<CurrencyFundEntrustThreeMonthBean.DataBean> data = threeMonthBean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                    mContext.startActivity(intent);
                } else if (code.equals("0") && data != null) {
                    if (!isClean) {
                        list.clear();
                    } else {
                        if (data.size() > 0) {
                            position = data.get(data.size() - 1).getKEY_STR();
                        }
                    }
                    for (int i = 0; i < data.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        CurrencyFundEntrustThreeMonthBean.DataBean dataBean = data.get(i);
                        map.put("tv_stockName", dataBean.getSECU_NAME());
                        map.put("tv_stockCode", dataBean.getSECU_CODE());
                        map.put("tv_Data", Helper.formateDate1(dataBean.getORDER_DATE()));
                        map.put("tv_Time",Helper.formateDate(dataBean.getORDER_TIME()));
                        map.put("tv_EntrustNumber", dataBean.getQTY());
                        map.put("tv_EntrustMoney", dataBean.getPRICE());
                        map.put("tv_type", dataBean.getBUSINESS_NAME());
                        map.put("tv_state", dataBean.getSTATUS_NAME());
                        list.add(map);
                    }
                    adapter.setList(list);
                }
            }
        });

    }

}
