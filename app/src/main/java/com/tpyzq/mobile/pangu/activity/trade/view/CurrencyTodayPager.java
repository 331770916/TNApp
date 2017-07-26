package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.BaseSearchPager;
import com.tpyzq.mobile.pangu.adapter.trade.HBJJTodayPagerAdapter;
import com.tpyzq.mobile.pangu.data.CurrencyFundEntrustQueryTodayBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/8/23 15:02
 * 货币基金委托查询       今日
 */
public class CurrencyTodayPager extends BaseSearchPager  {


    private String TAG = "TodayPager";
    private PullToRefreshListView mListView;
    private ArrayList<HashMap<String, String>> list;
    private HBJJTodayPagerAdapter adapter;
    private ImageView isEmpty;

    public CurrencyTodayPager(Context context) {
        super(context);
    }


    @Override
    public void setView() {
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.rlRefresh);
        isEmpty = (ImageView) rootView.findViewById(R.id.isEmpty);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        adapter = new HBJJTodayPagerAdapter(mContext);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(isEmpty);


        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  上拉
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  下拉
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.currencyfund_todaypager;
    }

    @Override
    public void initData() {
        getData();
    }


    private void getData() {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("ACTION_IN", "0");
        map1.put("funcid", "300444");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mListView.onRefreshComplete();
                Helper.getInstance().showToast(mContext, ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                mListView.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                list = new ArrayList<HashMap<String, String>>();
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CurrencyFundEntrustQueryTodayBean>() {
                }.getType();
                CurrencyFundEntrustQueryTodayBean todayBean = gson.fromJson(response, type);
                String code = todayBean.getCode();
                List<CurrencyFundEntrustQueryTodayBean.DataBean> data = todayBean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                    mContext.startActivity(intent);
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        CurrencyFundEntrustQueryTodayBean.DataBean dataBean = data.get(i);
                        map.put("tv_stockName", dataBean.getSECU_NAME());
                        map.put("tv_stockCode", dataBean.getSECU_CODE());
                        map.put("tv_Data", Helper.formateDate1(dataBean.getORDER_DATE()));
                        map.put("tv_Time",Helper.formateDate(dataBean.getORDER_TIME()));
                        map.put("tv_EntrustNumber", dataBean.getQTY());
                        map.put("tv_EntrustMoney", dataBean.getORDER_AMT());
                        map.put("tv_type", dataBean.getBUSINESS_NAME());
                        map.put("tv_state", dataBean.getSTATUS_NAME());
                        list.add(map);
                    }
                    adapter.setList(list);
                } else {
                    MistakeDialog.showDialog(todayBean.getMsg(), (Activity) mContext);
                }
            }
        });
    }


}
