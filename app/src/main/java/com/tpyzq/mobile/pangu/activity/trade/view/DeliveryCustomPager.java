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
import com.tpyzq.mobile.pangu.adapter.trade.DeliveryListViewAdapter;
import com.tpyzq.mobile.pangu.data.CapitalEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
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

import static com.tpyzq.mobile.pangu.activity.trade.stock.DeliveryOrderActivity.deliveryOrderFragmentManager;

/**
 * Created by wangqi on 2016/8/13.
 * 交割单  自定义 标签
 */
public class DeliveryCustomPager extends BaseSearchPager {
    private String TAG = "DeliveryCustomPager";
    private TimePickerView mJgStartpvTime, mJgFinishpvTime;
    private PullToRefreshListView mCustomListView;
    private List<CapitalEntity> beans;
    private TextView mJgStartDate, mJgfinishDate;
    private TextView mJginquire;
    private ImageView iv_isEmpty;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private DeliveryListViewAdapter mAdapter;
    private RelativeLayout kong_null;
    private Dialog mDialog;


    public DeliveryCustomPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mJgStartDate = (TextView) rootView.findViewById(R.id.DeliveryStartDate);
        mJgfinishDate = (TextView) rootView.findViewById(R.id.DeliveryFinishDate);
        mJginquire = (TextView) rootView.findViewById(R.id.DeliveryInquire);
        mCustomListView = (PullToRefreshListView) rootView.findViewById(R.id.DeliveryListView);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.DP_Kong_Null_1);
        mJgStartDate.setText(Helper.getBeforeString());
        mJgfinishDate.setText(Helper.getBeforeString());
        mJgStartDate.setOnClickListener(new MyOnClickListenr());
        mJgfinishDate.setOnClickListener(new MyOnClickListenr());
        mJginquire.setOnClickListener(new MyOnClickListenr());
        beans = new ArrayList<>();
        toStartDate();
        toFinishDate();
        Update();
        mAdapter = new DeliveryListViewAdapter(mContext);
        mAdapter.setData(-1);
        mCustomListView.setAdapter(mAdapter);
        mCustomListView.setEmptyView(iv_isEmpty);

        mJgStartDate.setTextColor(Color.parseColor("#368de7"));
        mJgfinishDate.setTextColor(Color.parseColor("#368de7"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_deliverycustom;
    }


    @Override
    public void initData() {

    }


    private void toConnect(String i, final String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();

        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", Helper.getMyDate(mJgStartDate.getText().toString()));
        map2.put("END_DATE", Helper.getMyDate(mJgfinishDate.getText().toString()));
        map2.put("POSITION_STR", i);
        map2.put("REQUEST_NUM", j);
        map.put("funcid", "300184");
        map.put("token", mSession);
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                LogHelper.e(TAG, e.toString());
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
                                CapitalEntity _bean = new CapitalEntity();
                                _bean.setDate(data.getJSONObject(i).getString("INIT_DATE"));
                                _bean.setName(data.getJSONObject(i).getString("STOCK_NAME"));
                                _bean.setStockCode(data.getJSONObject(i).getString("STOCK_CODE"));
                                _bean.setBalance(data.getJSONObject(i).getString("OCCUR_BALANCE"));
                                _bean.setTransactionPrice(data.getJSONObject(i).getString("BUSINESS_PRICE"));
                                _bean.setHbcjsl(data.getJSONObject(i).getString("BUSINESS_AMOUNT"));
                                _bean.setShareholder(data.getJSONObject(i).getString("STOCK_ACCOUNT"));
                                _bean.setRemarks(data.getJSONObject(i).getString("REMARK"));
                                _bean.setCommission(data.getJSONObject(i).getString("FARE0"));
                                _bean.setStamps(data.getJSONObject(i).getString("FARE1"));
                                _bean.setTransferFee(data.getJSONObject(i).getString("FARE2"));  //FARE2
                                _bean.setOtherexpenses(data.getJSONObject(i).getString("EXCHANGE_FARE6"));//其他费用
                                _bean.setTransaction(data.getJSONObject(i).getString("ENTRUST_BS"));
                                beans.add(_bean);
                            }
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                            mAdapter.setData(beans);
                            mAdapter.notifyDataSetChanged();
                            sure = data.length();
                        }else {
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
                        CentreToast.showText(mContext,jsonObject.getString("msg"));
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
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.DeliveryStartDate:
                    mJgStartpvTime.show();
                    break;
                case R.id.DeliveryFinishDate:
                    mJgFinishpvTime.show();
                    break;
                case R.id.DeliveryInquire:
                    if (!TextUtils.isEmpty(mJgStartDate.getText().toString()) && !TextUtils.isEmpty(mJgfinishDate.getText().toString())) {

                        String startDay = Helper.getMyDate(mJgStartDate.getText().toString());
                        String endDay = Helper.getMyDate(mJgfinishDate.getText().toString());

                        String str = Helper.compareTo(startDay, endDay);

                        int days = Helper.daysBetween(startDay, endDay);

                        if (str.equalsIgnoreCase(startDay)&& !str.equals(endDay)) {
                            showDialog("请选择正确日期,起始日期不能超过截止日期");
                        } else if (days > 90) {
                            showDialog("选择的日期间隔不能超过3个月");
                        } else {
                            mDialog = LoadingDialog.initDialog((Activity) mContext, "正在查询...");
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

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(deliveryOrderFragmentManager,DeliveryCustomPager.class.toString());
    }

    private void toFinishDate() {
        mJgFinishpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mJgFinishpvTime.setTime(Helper.getBeforeDate());
        mJgFinishpvTime.setCyclic(false);
        mJgFinishpvTime.setCancelable(true);

        mJgFinishpvTime.setTitle("选择日期");

        //时间选择后回调
        mJgFinishpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mJgfinishDate.setText(simpleDateFormat.format(date));
            }
        });
    }


    private void toStartDate() {
        mJgStartpvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mJgStartpvTime.setTime(Helper.getBeforeDate());
        mJgStartpvTime.setCyclic(false);
        mJgStartpvTime.setCancelable(true);

        mJgStartpvTime.setTitle("选择日期");

        //时间选择后回调
        mJgStartpvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mJgStartDate.setText(simpleDateFormat.format(date));
            }
        });
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
        mCustomListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mCustomListView.isShownHeader()) {
                    //设置头布局样式文字
                    mCustomListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mCustomListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mCustomListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mCustomListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mCustomListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mCustomListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mCustomListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mCustomListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mCustomListView.onRefreshComplete();
                        }
                    }.execute();
                }

            }
        });
    }

}
