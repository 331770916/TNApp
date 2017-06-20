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
import com.tpyzq.mobile.pangu.adapter.trade.ClinchADealAdapter;
import com.tpyzq.mobile.pangu.data.ClinchDealEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
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
 * 成交 三月内   标签
 */
public class ClinchThreeWeekPager extends BaseSearchPager {
    private String TAG = "ClinchThreeWeekPager";
    private PullToRefreshListView mClinchListView;
    private List<ClinchDealEntity> beans;
    private ClinchADealAdapter mAdapter;
    private ImageView iv_isEmpty;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private RelativeLayout kong_null;

    public ClinchThreeWeekPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mClinchListView = (PullToRefreshListView) rootView.findViewById(R.id.ClinchListView);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.CCP_Kong_Null_1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_clinchtoday;
    }

    @Override
    public void initData() {
        beans = new ArrayList<>();
        toConnect("", "30", false);
        Update();
        mAdapter = new ClinchADealAdapter(mContext);
        mClinchListView.setAdapter(mAdapter);
        mClinchListView.setEmptyView(iv_isEmpty);
    }

    private void toConnect(String i, String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "3");
        map2.put("BEGIN_DATE", "");
        map2.put("END_DATE", "");
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
                kong_null.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    LogHelper.e(TAG, response);
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
                                _bean.setDate( data.getJSONObject(i).getString("MATCHED_DATE"));
                                _bean.setTime(data.getJSONObject(i).getString("MATCHED_TIME"));
                                _bean.setAmount(data.getJSONObject(i).getString("MATCHED_QTY"));
                                _bean.setMoney(data.getJSONObject(i).getString("MATCHED_AMT"));
                                _bean.setTransaction(data.getJSONObject(i).getString("ENTRUST_BS"));
                                beans.add(_bean);
                            }
                            mAdapter.setData(beans);
                            mAdapter.notifyDataSetChanged();
                            sure = data.length();
                        }
                    } else {
                        ToastUtils.centreshow(mContext, "网络异常");
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
        mClinchListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mClinchListView.isShownHeader()) {
                    //设置头布局样式文字
                    mClinchListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mClinchListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mClinchListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mClinchListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mClinchListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mClinchListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mClinchListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mClinchListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mClinchListView.onRefreshComplete();
                        }
                    }.execute();
                }

            }
        });
    }

}
