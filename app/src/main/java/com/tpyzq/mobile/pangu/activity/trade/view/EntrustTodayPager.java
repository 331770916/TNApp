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
 * Created by wangqi on 2016/8/13.
 * 委托  今日
 */
public class EntrustTodayPager extends BaseSearchPager {
    private String TAG = "EntrustTodayPager";

    private PullToRefreshListView mTodayListView;
    private List<TodayEntrustEntity> mBeans;
    private EntrustListViewAdapter mAapter;
    private ImageView iv_isEmpty;
    private RelativeLayout kong_null;

    public EntrustTodayPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mTodayListView = (PullToRefreshListView) rootView.findViewById(R.id.TodayListView);
        mTodayListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.EAMP_Kong_Null);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    public void initData() {
        mBeans = new ArrayList<>();
        toConnect();
        Update();
        mAapter = new EntrustListViewAdapter(mContext);
        mTodayListView.setAdapter(mAapter);
        mTodayListView.setEmptyView(iv_isEmpty);
    }

    private void toConnect() {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300160");
        map.put("token", mSession);
        map.put("parms", map2);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("FUND_ACCOUNT", "");
        map2.put("ACTION_IN", "0");// 0：全部委托 1：可撤委托

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
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
                        mBeans.clear();
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                TodayEntrustEntity _bean = new TodayEntrustEntity();
                                _bean.setName(data.getJSONObject(i).getString("SECU_NAME"));
                                _bean.setNum(data.getJSONObject(i).getString("PRICE"));
                                _bean.setDate( data.getJSONObject(i).getString("ORDER_DATE"));//日期
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
                            mAapter.setData(mBeans);
                            mAapter.notifyDataSetChanged();
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        ResultDialog.getInstance().showText(jsonObject.getString("msg"));
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
                    toConnect();
                    break;
            }
        }
    };

    /**
     * 刷新 加载 数据
     */
    private void Update() {
        mTodayListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                mTodayListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mTodayListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                mTodayListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                        mTodayListView.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }
}
