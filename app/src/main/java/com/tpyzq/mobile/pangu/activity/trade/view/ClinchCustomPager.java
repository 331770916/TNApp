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
import com.tpyzq.mobile.pangu.adapter.trade.ClinchADealAdapter;
import com.tpyzq.mobile.pangu.data.ClinchDealEntity;
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
 * 成交  自定义  标签
 */
public class ClinchCustomPager extends BaseSearchPager {
    private String TAG = "ClinchCustomPager";
    private TextView mStartpvTime, mFinishpvTime;
    private TextView mInquire;
    private ClinchADealAdapter mAdapter;
    private List<ClinchDealEntity> beans;
    private TimePickerView mCjFinishpvTime, mCjStartpvTime;
    private PullToRefreshListView mClinchCustomListView;
    private ImageView iv_isEmpty;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private RelativeLayout kong_null;
    private Dialog mDialog;

    public ClinchCustomPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mClinchCustomListView = (PullToRefreshListView) rootView.findViewById(R.id.ClinchCustomListView);
        mStartpvTime = (TextView) rootView.findViewById(R.id.ClinchStartDate);
        mFinishpvTime = (TextView) rootView.findViewById(R.id.ClinchFinishDate);
        mInquire = (TextView) rootView.findViewById(R.id.ClinchInquire);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.CCP_Kong_Null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_clinchcustom;
    }

    @Override
    public void initData() {
        mStartpvTime.setText(Helper.getCurDate().toString());
        mFinishpvTime.setText(Helper.getCurDate().toString());

        beans = new ArrayList<>();
        mStartpvTime.setOnClickListener(new MyOnClickListenr());
        mFinishpvTime.setOnClickListener(new MyOnClickListenr());
        mInquire.setOnClickListener(new MyOnClickListenr());
        toStartDate();
        toFinishDate();
        mAdapter = new ClinchADealAdapter(mContext);
        mClinchCustomListView.setAdapter(mAdapter);
        mClinchCustomListView.setEmptyView(iv_isEmpty);

        mStartpvTime.setTextColor(Color.parseColor("#368de7"));
        mFinishpvTime.setTextColor(Color.parseColor("#368de7"));

    }

    private void toConnect(String i, String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", Helper.getMyDate(mStartpvTime.getText().toString()));
        map2.put("END_DATE", Helper.getMyDate(mFinishpvTime.getText().toString()));
        map2.put("MARKET", "");
        map2.put("SECU_CODE", "");
        map2.put("KEY_STR", i);
        map2.put("REC_COUNT", j);
        map.put("funcid", "300191");
        map.put("token", mSession);
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                if (mDialog!=null){
                    mDialog.dismiss();
                }
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
                                    position = data.getJSONObject(data.length() - 1).getString("KEY_STR");
                                }
                                refresh += 30;
                            } else {
                                position = data.getJSONObject(data.length() - 1).getString("KEY_STR");
                            }
                            for (int i = 0; i < data.length(); i++) {
                                ClinchDealEntity _bean = new ClinchDealEntity();
                                _bean.setName(data.getJSONObject(i).getString("SECU_NAME"));
                                _bean.setNum(data.getJSONObject(i).getString("MATCHED_PRICE"));
                                _bean.setDate(data.getJSONObject(i).getString("MATCHED_DATE"));
                                _bean.setTime(data.getJSONObject(i).getString("MATCHED_TIME"));
                                _bean.setAmount(data.getJSONObject(i).getString("MATCHED_QTY"));
                                _bean.setMoney(data.getJSONObject(i).getString("MATCHED_AMT"));
                                _bean.setTransaction(data.getJSONObject(i).getString("ENTRUST_BS"));
                                beans.add(_bean);
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

    private void toFinishDate() {
        mCjFinishpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mCjFinishpvTime.setTime(new Date());
        mCjFinishpvTime.setCyclic(false);
        mCjFinishpvTime.setCancelable(true);

        mCjFinishpvTime.setTitle("选择日期");

        //时间选择后回调
        mCjFinishpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mFinishpvTime.setText(simpleDateFormat.format(date));
            }
        });
    }


    private void toStartDate() {
        mCjStartpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mCjStartpvTime.setTime(new Date());
        mCjStartpvTime.setCyclic(false);
        mCjStartpvTime.setCancelable(true);

        mCjStartpvTime.setTitle("选择日期");

        //时间选择后回调
        mCjStartpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mStartpvTime.setText(simpleDateFormat.format(date));
            }
        });
    }


    class MyOnClickListenr implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ClinchStartDate:
                    mCjStartpvTime.show();
                    break;
                case R.id.ClinchFinishDate:
                    mCjFinishpvTime.show();
                    break;
                case R.id.ClinchInquire:
                    if (!TextUtils.isEmpty(mStartpvTime.getText().toString()) && !TextUtils.isEmpty(mFinishpvTime.getText().toString())) {

                        String startDay = Helper.getMyDate(mStartpvTime.getText().toString());
                        String endDay = Helper.getMyDate(mFinishpvTime.getText().toString());

                        String str = Helper.compareTo(startDay, endDay);

                        int days = Helper.daysBetween(startDay, endDay);

                        if (str.equalsIgnoreCase(startDay)) {
                            MistakeDialog.showDialog("请选择正确日期,起始日期不能超过截止日期", (Activity) mContext);
                        } else if (days > 90) {
                            MistakeDialog.showDialog("选择的日期间隔不能超过3个月", (Activity) mContext);
                        } else {

                            mDialog = LoadingDialog.initDialog((Activity) mContext, "正在查询...");
                            mDialog.show();
                            beans.clear();
                            mAdapter.notifyDataSetChanged();
                            toConnect(position, "30", false);
                            Update();
                        }
                    }
                    break;
            }
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    toConnect("", refresh + "", false);
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
        mClinchCustomListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mClinchCustomListView.isShownHeader()) {
                    //设置头布局样式文字
                    mClinchCustomListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mClinchCustomListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mClinchCustomListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mClinchCustomListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mClinchCustomListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mClinchCustomListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mClinchCustomListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mClinchCustomListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mClinchCustomListView.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });
    }

}
