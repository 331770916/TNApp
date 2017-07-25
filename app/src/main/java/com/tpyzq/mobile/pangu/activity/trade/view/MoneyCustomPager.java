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
import com.tpyzq.mobile.pangu.adapter.trade.MoneyAdapter;
import com.tpyzq.mobile.pangu.data.CapitalEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
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
 * Created by wangqi on 2016/8/16.
 * 资金 自定义
 */
public class MoneyCustomPager extends BaseSearchPager {

    private String TAG = "MoneyCustomPager";
    private PullToRefreshListView mZjTodayListView;
    private List<CapitalEntity> beans;
    private MoneyAdapter mAdapter;
    private TextView mZjStartDate, mZjFinishDate;
    private TextView mZjInquire;
    private TimePickerView mZjStartpvTime, mZjFinishpvTime;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private ImageView iv_isEmpty;
    private RelativeLayout kong_null;
    private Dialog mDialog;

    public MoneyCustomPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mZjTodayListView = (PullToRefreshListView) rootView.findViewById(R.id.CapitalTodayListView);
        mZjStartDate = (TextView) rootView.findViewById(R.id.CapitalStartDate);
        mZjFinishDate = (TextView) rootView.findViewById(R.id.CapitalFinishDate);
        mZjInquire = (TextView) rootView.findViewById(R.id.CapitalInquire);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.Kong_Null);
        mZjStartDate.setText(Helper.getCurDate().toString());
        mZjFinishDate.setText(Helper.getCurDate().toString());

        beans = new ArrayList<>();
        mZjStartDate.setOnClickListener(new MyOnClickListenr());
        mZjFinishDate.setOnClickListener(new MyOnClickListenr());
        mZjInquire.setOnClickListener(new MyOnClickListenr());
        mAdapter = new MoneyAdapter(mContext);
        toStartDate();
        toFinishDate();
        Update();
        mAdapter.setData(-1);
        mZjTodayListView.setAdapter(mAdapter);
        mZjTodayListView.setEmptyView(iv_isEmpty);
        mZjStartDate.setTextColor(Color.parseColor("#368de7"));
        mZjFinishDate.setTextColor(Color.parseColor("#368de7"));
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_capitalcustom;
    }

    @Override
    public void initData() {

    }

    private void toConnect(final String i, String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300186");
        map.put("token", mSession);
        map.put("parms", map2);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", Helper.getMyDate(mZjStartDate.getText().toString()));
        map2.put("END_DATE", Helper.getMyDate(mZjFinishDate.getText().toString()));
        map2.put("POSITION_STR", i);
        map2.put("REQUEST_NUM", j);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mDialog!=null){
                    mDialog.dismiss();
                }
                LogHelper.e(TAG, e.toString());
                ResultDialog.getInstance().showText("网络异常");
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
                            beans.clear();
                        }
                        if (data != null && data.length() > 0) {
                            if (flag) {
                                if (data.length() > 1) {
                                    position = data.getJSONObject(data.length() - 1).getString("POSITION_STR");
                                }
                                refresh += 30;
                            } else {
                                position = data.getJSONObject(data.length() - 1).getString("POSITION_STR");
                            }
                            for (int i = 0; i < data.length(); i++) {
                                CapitalEntity _bena = new CapitalEntity();
                                _bena.setBusiness_type(data.getJSONObject(i).getString("BUSINESS_TYPE"));
                                _bena.setDate(data.getJSONObject(i).getString("INIT_DATE"));
                                _bena.setBusiness(data.getJSONObject(i).getString("BUSINESS_NAME"));
                                _bena.setName(data.getJSONObject(i).getString("STOCK_NAME"));
                                _bena.setBalance(data.getJSONObject(i).getString("CLEAR_BALANCE"));
                                _bena.setStockName(data.getJSONObject(i).getString("STOCK_NAME"));
                                _bena.setStockcde(data.getJSONObject(i).getString("STOCK_CODE"));
                                _bena.setSecurityPrice(data.getJSONObject(i).getString("BUSINESS_PRICE"));
                                _bena.setHbcjsl(data.getJSONObject(i).getString("BUSINESS_AMOUNT"));
                                _bena.setCurrency(data.getJSONObject(i).getString("MONEY_TYPE"));
                                beans.add(_bena);
                            }
                            if (mDialog!=null){
                                mDialog.dismiss();
                            }
                            mAdapter.setData(beans);
                            mAdapter.notifyDataSetChanged();
                            sure = data.length();
                        }else {
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                            Helper.getInstance().showToast(mContext," 暂无数据");
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        if (mDialog!=null){
                            mDialog.dismiss();
                        }
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        if (mDialog!=null){
                            mDialog.dismiss();
                        }
                        ResultDialog.getInstance().showText(jsonObject.getString("msg"));
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
                case R.id.CapitalStartDate:
                    mZjStartpvTime.show();
                    break;
                case R.id.CapitalFinishDate:
                    mZjFinishpvTime.show();
                    break;
                case R.id.CapitalInquire:
                    if (!TextUtils.isEmpty(mZjStartDate.getText().toString()) && !TextUtils.isEmpty(mZjFinishDate.getText().toString())) {

                        String startDay = Helper.getMyDate(mZjStartDate.getText().toString());
                        String endDay = Helper.getMyDate(mZjFinishDate.getText().toString());

                        String str = Helper.compareTo(startDay, endDay);

                        int days = Helper.daysBetween(startDay, endDay);

                        if (str.equalsIgnoreCase(startDay)) {
                            MistakeDialog.showDialog("请选择正确日期,起始日期不能超过截止日期", (Activity) mContext);
                        } else if (days > 90) {
                            MistakeDialog.showDialog("选择的日期间隔不能超过3个月", (Activity) mContext);
                        }else {
                            mDialog = LoadingDialog.initDialog((Activity)mContext, "正在查询...");
                            mDialog.show();
                            beans.clear();
                            mAdapter.notifyDataSetChanged();
                            toConnect("", "30", false);
                        }
                    }

                    break;
            }
        }
    }


    /**
     * 开始日期
     */
    private void toStartDate() {
        mZjStartpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mZjStartpvTime.setTime(new Date());
        mZjStartpvTime.setCyclic(false);
        mZjStartpvTime.setCancelable(true);
        mZjStartpvTime.setTitle("选择日期");
        //时间选择后回调
        mZjStartpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mZjStartDate.setText(simpleDateFormat.format(date));
            }
        });
    }


    /***
     * 截至日期
     */
    private void toFinishDate() {
        mZjFinishpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mZjFinishpvTime.setTime(new Date());
        mZjFinishpvTime.setCyclic(false);
        mZjFinishpvTime.setCancelable(true);
        mZjFinishpvTime.setTitle("选择日期");
        //时间选择后回调
        mZjFinishpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mZjFinishDate.setText(simpleDateFormat.format(date));
            }
        });
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    toConnect("", "30", false);
                    break;
                case 1:
                    toConnect(position,"30",true);
                    break;
            }
        }
    };

    /**
     * 刷新 加载   数据
     */
    private void Update() {
        mZjTodayListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mZjTodayListView.isShownHeader()) {
                    //设置头布局样式文字
                    mZjTodayListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mZjTodayListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mZjTodayListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mZjTodayListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mZjTodayListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mZjTodayListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mZjTodayListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mZjTodayListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mZjTodayListView.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });
    }

}
