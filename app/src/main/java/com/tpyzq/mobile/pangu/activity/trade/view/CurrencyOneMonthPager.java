package com.tpyzq.mobile.pangu.activity.trade.view;

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
import com.tpyzq.mobile.pangu.adapter.trade.HBJJTodayPagerAdapter;
import com.tpyzq.mobile.pangu.data.CurrencyFundEntrustThreeMonthBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.RefreshListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/8/24 10:51
 * 一月
 */
public class CurrencyOneMonthPager extends BasePager {

    private String TAG = "OneMonthPager";
    private PullToRefreshListView mListView;
    private ArrayList<HashMap<String, String>> list;
    private HBJJTodayPagerAdapter adapter;
    private ImageView isEmpty;
    private String position;

    public CurrencyOneMonthPager(Context context) {
        super(context);
    }


    @Override
    public void setView() {
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.rlRefresh);
        isEmpty = (ImageView) rootView.findViewById(R.id.isEmpty);
    }

    @Override
    public int getLayoutId() {
        return R.layout.currencyfund_todaypager;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<HashMap<String, String>>();
        adapter = new HBJJTodayPagerAdapter(mContext);
        refresh("", "30", false);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(isEmpty);


        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  上拉
                refresh("", "30", false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  下拉
                refresh(position, "30", true);
            }
        });
    }


    public void refresh(String page, String num, final boolean isClean) {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("HIS_TYPE", "2");
        map2.put("BEGIN_DATE", "");
        map2.put("END_DATE", "");
        map2.put("KEY_STR", page);
        map2.put("REQUEST_NUM", num);
        map1.put("funcid", "300445");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mListView.onRefreshComplete();
                Helper.getInstance().showToast(mContext,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                mListView.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CurrencyFundEntrustThreeMonthBean>() {
                }.getType();
                CurrencyFundEntrustThreeMonthBean threeMonthBean = gson.fromJson(response, type);
                String code = threeMonthBean.getCode();
                List<CurrencyFundEntrustThreeMonthBean.DataBean> data = threeMonthBean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                    mContext.startActivity(intent);
                } else if (code.equals("0") && data != null) {
                    if (!isClean) {
                        list.clear();
                    } else {
                        if (data.size() > 0) {
                            position = data.get(data.size() - 1).getKEY_STR();
                        }
                    }
                    for (int i = 0; i < data.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        CurrencyFundEntrustThreeMonthBean.DataBean dataBean = data.get(i);
                        map.put("tv_stockName", dataBean.getSECU_NAME());
                        map.put("tv_stockCode", dataBean.getSECU_CODE());
                        map.put("tv_Data", dataBean.getORDER_DATE());
                        map.put("tv_Time", dataBean.getORDER_TIME());
                        map.put("tv_EntrustNumber", dataBean.getQTY());
                        map.put("tv_EntrustMoney", dataBean.getPRICE());
                        map.put("tv_type", dataBean.getBUSINESS_NAME());

                        String entrust_status;
                        if (dataBean.getENTRUST_STATUS().equals("0")) {
                            entrust_status = "未报";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("1")) {
                            entrust_status = "待报";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("2")) {
                            entrust_status = "已报";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("3")) {
                            entrust_status = "已报待撤";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("4")) {
                            entrust_status = "部成待撤";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("6")) {
                            entrust_status = "已撤";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("7")) {
                            entrust_status = "部成";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("8")) {
                            entrust_status = "已成";
                            map.put("tv_state", entrust_status);
                        }
                        if (dataBean.getENTRUST_STATUS().equals("9")) {
                            entrust_status = "废单";
                            map.put("tv_state", entrust_status);
                        }

                        list.add(map);
                    }
                }
            }
        });

        adapter.setList(list);
    }
}
