package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
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
import com.tpyzq.mobile.pangu.activity.trade.BaseSearchPager;
import com.tpyzq.mobile.pangu.adapter.trade.MoneyAdapter;
import com.tpyzq.mobile.pangu.data.CapitalEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
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
 * Created by wangqi on 2016/8/16.
 * 资金一月内
 */
public class MoneyInAMonthPager extends BaseSearchPager {

    private String TAG = "MoneyInAMonthPager";
    private PullToRefreshListView mZjTodayListView;
    private List<CapitalEntity> beans;
    private MoneyAdapter mAdapter;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private ImageView iv_isEmpty;
    private RelativeLayout kong_null;

    public MoneyInAMonthPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mZjTodayListView = (PullToRefreshListView) rootView.findViewById(R.id.ZjTodayListView);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.MWP_Kong_Null);
        beans = new ArrayList<>();
        Update();
        mAdapter = new MoneyAdapter(mContext);
        mAdapter.setData(-1);
        mZjTodayListView.setAdapter(mAdapter);
        mZjTodayListView.setEmptyView(iv_isEmpty);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_capitaltoday;
    }

    @Override
    public void initData() {
        toConnect("", "30", false);
    }


    private void toConnect(final String i, final String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300186");
        map.put("token", mSession);
        map.put("parms", map2);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "2");
        map2.put("BEGIN_DATE", "");
        map2.put("END_DATE", "");
        map2.put("POSITION_STR", i);
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
                        if (!flag) {
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
                            mAdapter.setData(beans);
                            mAdapter.notifyDataSetChanged();
                            sure = data.length();
                        }
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
                    //设置头布局样式文字
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
