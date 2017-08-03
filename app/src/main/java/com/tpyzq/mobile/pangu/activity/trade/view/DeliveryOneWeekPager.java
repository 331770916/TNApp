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
import com.tpyzq.mobile.pangu.adapter.trade.DeliveryListViewAdapter;
import com.tpyzq.mobile.pangu.data.CapitalEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
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
 * 交割单  一周内 标签
 */
public class DeliveryOneWeekPager extends BaseSearchPager {
    private final String TAG = "DeliveryOneWeekPager";
    private DeliveryListViewAdapter mListViewAdapter;
    private PullToRefreshListView mOneWeeListView;
    private List<CapitalEntity> beans;
    private ImageView iv_isEmpty;
    String position = "";
    int refresh = 30;
    int sure = 30;
    private RelativeLayout kong_null;

    public DeliveryOneWeekPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        mOneWeeListView = (PullToRefreshListView) rootView.findViewById(R.id.DeliveryTodayListView);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.DP_Kong_Null);
        beans = new ArrayList<>();
        Update();
        mListViewAdapter = new DeliveryListViewAdapter(mContext);
        mListViewAdapter.setData(-1);
        mOneWeeListView.setAdapter(mListViewAdapter);
        mOneWeeListView.setEmptyView(iv_isEmpty);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_deliverytoday;
    }


    @Override
    public void initData() {
        toConnect("", "30", false);
    }


    private void toConnect(String i, String j, final boolean flag) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();

        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("HIS_TYPE", "1");
        map2.put("BEGIN_DATE", "");
        map2.put("END_DATE", "");
        map2.put("POSITION_STR", i);
        map2.put("REQUEST_NUM", j);
        map.put("funcid", "300184");
        map.put("token", mSession);
        map.put("parms", map2);

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
                                _bean.setTransferFee(data.getJSONObject(i).getString("FARE2"));
                                _bean.setOtherexpenses(data.getJSONObject(i).getString("EXCHANGE_FARE6"));
                                _bean.setTransaction(data.getJSONObject(i).getString("ENTRUST_BS"));
                                beans.add(_bean);
                            }
                            mListViewAdapter.setData(beans);
                            mListViewAdapter.notifyDataSetChanged();
                            sure = data.length();
                        }
                    } else {
                        kong_null.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(mContext, "网络异常");
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
        mOneWeeListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mOneWeeListView.isShownHeader()) {
                    //设置头布局样式文字
                    mOneWeeListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mOneWeeListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mOneWeeListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mOneWeeListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mOneWeeListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mOneWeeListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mOneWeeListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mOneWeeListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                            mOneWeeListView.onRefreshComplete();
                        }
                    }.execute();
                }

            }
        });
    }
}
