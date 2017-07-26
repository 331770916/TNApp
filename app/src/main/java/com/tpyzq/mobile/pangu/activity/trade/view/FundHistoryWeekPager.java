package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.BaseTransactionPager;
import com.tpyzq.mobile.pangu.adapter.trade.FundHistoryAdapter;
import com.tpyzq.mobile.pangu.data.FundHistoryThreeEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
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

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/10/24.
 */
public class FundHistoryWeekPager extends BaseTransactionPager {
    LinearLayout ll_fourtext;
    TextView tv_text1, tv_text2, tv_text3, tv_text4;
    PullToRefreshListView lv_transaction;
    ImageView tv_empty;
    int sure = 30;
    List<FundHistoryThreeEntity> fundHistoryThreeBeen;
    FundHistoryAdapter fundHistoryAdapter;
    String position = "";

    public FundHistoryWeekPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setView() {
        ll_fourtext = (LinearLayout) rootView.findViewById(R.id.ll_fourtext);
        tv_text1 = (TextView) ll_fourtext.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) ll_fourtext.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) ll_fourtext.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) ll_fourtext.findViewById(R.id.tv_text4);
        lv_transaction = (PullToRefreshListView) rootView.findViewById(R.id.lv_transaction);
        tv_empty = (ImageView) rootView.findViewById(R.id.tv_empty);
        fundHistoryThreeBeen = new ArrayList<>();
        fundHistoryAdapter = new FundHistoryAdapter(mContext);
        tv_text1.setText("名称");
        tv_text2.setText("成交日期");
        tv_text3.setText("业务名称");
        tv_text4.setText("成交金额/份额");
        lv_transaction.setAdapter(fundHistoryAdapter);
        lv_transaction.setEmptyView(tv_empty);
        Update();
    }

    @Override
    public void setRefresh() {
        fundQuery("", true);
    }

    /**
     * 获取基金产品
     */
    private void fundQuery(String point, final boolean flag) {
        HashMap map300433 = new HashMap();
        map300433.put("funcid", "300433");
        map300433.put("token", SpUtils.getString(mContext, "mSession", null));
        HashMap map300433_1 = new HashMap();
        map300433_1.put("SEC_ID", "tpyzq");
        map300433_1.put("HIS_TYPE", "1");
        map300433_1.put("BEGIN_DATE", "");
        map300433_1.put("END_DATE", "");
        map300433_1.put("POSITION_STR", point);
        map300433_1.put("REQUEST_NUM", "30");
        map300433_1.put("FLAG", "true");
        map300433.put("parms", map300433_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300433, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    JSONArray jsonArray = new JSONArray(data);
                    if ("0".equals(code)) {
                        if (flag) {
                            fundHistoryThreeBeen.clear();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FundHistoryThreeEntity fundHistoryThreeBean = new Gson().fromJson(jsonArray.getString(i), FundHistoryThreeEntity.class);
                            fundHistoryThreeBean.unfold = false;
                            fundHistoryThreeBeen.add(fundHistoryThreeBean);
                        }
                        if (fundHistoryThreeBeen.size() > 0) {
                            position = fundHistoryThreeBeen.get(fundHistoryThreeBeen.size() - 1).POSITION_STR;
                        }
                        fundHistoryAdapter.setHisThreeBean(fundHistoryThreeBeen);
                    } else if ("-6".equals(code)) {
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(mContext, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lv_transaction.onRefreshComplete();
            }
        });
    }


    /**
     * 刷新 加载   数据
     */
    private void Update() {
        lv_transaction.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isShownHeader()) {
                    //设置尾布局样式文字
                    lv_transaction.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_transaction.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    lv_transaction.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                fundQuery("", true);
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            //将下拉视图收起
                            lv_transaction.onRefreshComplete();
                        }
                    }.execute();
                } else if (refreshView.isShownFooter()) {
                    //设置尾布局样式文字
                    lv_transaction.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_transaction.getLoadingLayoutProxy().setPullLabel("上拉刷新数据");
                    lv_transaction.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                fundQuery(position, false);
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            //将下拉视图收起
                            lv_transaction.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });

        lv_transaction.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (sure >= 30) {

                } else {
                    ToastUtils.showShort(mContext, "已经滑到底了");
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.pager_fund;
    }
}
