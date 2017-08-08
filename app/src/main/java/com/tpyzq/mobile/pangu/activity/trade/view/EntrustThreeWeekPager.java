package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/8/13.
 * 委托  三月内
 */
public class EntrustThreeWeekPager extends BaseSearchPager {
    private String TAG = "EntrustThreeWeekPager";
    private PullToRefreshListView mThreeWeekListView;
    private List<TodayEntrustEntity> mBeans;
    private EntrustListViewAdapter mAdapter;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private ImageView iv_isEmpty;
    private RelativeLayout kong_null;

    public EntrustThreeWeekPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mThreeWeekListView = (PullToRefreshListView) rootView.findViewById(R.id.TodayListView);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.EAMP_Kong_Null);
        mBeans = new ArrayList<>();
        mAdapter = new EntrustListViewAdapter(mContext);
        mThreeWeekListView.setAdapter(mAdapter);
        mThreeWeekListView.setEmptyView(iv_isEmpty);
        Update();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }


    @Override
    public void initData() {
        toConnect("", "30", false);
    }

    private void toConnect(String i, final String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300171");
        map.put("token", mSession);
        map.put("parms", map2);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "3");
        map2.put("BEGIN_DATE", "");
        map2.put("END_DATE", "");
        map2.put("MARKET", "");
        map2.put("KEY_STR", i);
        map2.put("REQUEST_NUM", j);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
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
                                if (data.length() >= 1) {
                                    position = data.getJSONObject(data.length() - 1).getString("KEY_STR");  //定位串
                                }
                                refresh += 30;
                            }else {
                                position = data.getJSONObject(data.length() - 1).getString("KEY_STR");
                            }
                            for (int i = 0; i < data.length(); i++) {

                                TodayEntrustEntity _bean = new TodayEntrustEntity();
                                _bean.setName(data.getJSONObject(i).getString("SECU_NAME"));
                                _bean.setNum(data.getJSONObject(i).getString("PRICE"));
                                _bean.setDate(data.getJSONObject(i).getString("ORDER_DATE"));//日期
                                _bean.setTime(data.getJSONObject(i).getString("ORDER_TIME"));//时间

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
                            mAdapter.setData(mBeans);
                            mAdapter.notifyDataSetChanged();
                            sure = data.length();
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        CentreToast.showText(mContext,jsonObject.getString("msg"));
                        kong_null.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    toConnect("","30",false);
                    break;
                case 1:
                    toConnect(position,"30",true);
                    break;
            }
        }
    };

    /**
     * 刷新 加载 数据
     */
    private void Update() {
        mThreeWeekListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mThreeWeekListView.isShownHeader()) {
                    //设置头布局样式文字
                    mThreeWeekListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mThreeWeekListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mThreeWeekListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
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
                            mThreeWeekListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mThreeWeekListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mThreeWeekListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mThreeWeekListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mThreeWeekListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
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
                            mThreeWeekListView.onRefreshComplete();
                        }
                    }.execute();
                }

            }
        });
    }

}
