package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.HBJJTodayPagerAdapter;
import com.tpyzq.mobile.pangu.data.CurrencyFundEntrustQueryTodayBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.RefreshListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/8/23 15:02
 * 货币基金委托查询       今日
 */
public class CurrencyTodayPager extends BasePager implements RefreshListView.OnRefreshListener{


    private String TAG = "TodayPager";
    private RefreshListView mListView;
    private ArrayList<HashMap<String,String>> list;
    private HBJJTodayPagerAdapter adapter;

    public CurrencyTodayPager(Context context) {
        super(context);
    }



    @Override
    public void setView() {
        mListView= (RefreshListView) rootView.findViewById(R.id.rlRefresh);
    }

    @Override
    public int getLayoutId() {
        return R.layout.currencyfund_todaypager;
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<HashMap<String,String>>();
        adapter = new HBJJTodayPagerAdapter(mContext);
        getData();
        mListView.setOnRefreshListener(this);
        mListView.setAdapter(adapter);
    }


    private void getData() {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map1.put("funcid","300444");
        map1.put("token",mSession);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());
            }

            @Override
            public void onResponse(String response, int id) {

                if(TextUtils.isEmpty(response)){
                    return;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CurrencyFundEntrustQueryTodayBean>() {}.getType();
                CurrencyFundEntrustQueryTodayBean todayBean = gson.fromJson(response, type);
                String code = todayBean.getCode();
                List<CurrencyFundEntrustQueryTodayBean.DataBean> data = todayBean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(mContext, TransactionLoginActivity.class);
                    mContext.startActivity(intent);
                }else
                if(code.equals("0") && data !=null){
                    for(int i=0;i<data.size();i++){
                        HashMap<String,String> map = new HashMap<String, String>();
                        CurrencyFundEntrustQueryTodayBean.DataBean dataBean = data.get(i);
                        map.put("tv_stockName",dataBean.getSECU_NAME());
                        map.put("tv_stockCode",dataBean.getSECU_CODE());
                        map.put("tv_Data",dataBean.getORDER_DATE());
                        map.put("tv_Time",dataBean.getORDER_TIME());
                        map.put("tv_EntrustNumber",dataBean.getQTY());
                        map.put("tv_EntrustMoney",dataBean.getPRICE());
                        map.put("tv_type",dataBean.getBUSINESS_NAME());

                        String entrust_status;
                        if(dataBean.getENTRUST_STATUS().equals("0")){
                            entrust_status="未报";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("1")){
                            entrust_status="待报";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("2")){
                            entrust_status="已报";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("3")){
                            entrust_status="已报待撤";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("4")){
                            entrust_status="部成待撤";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("6")){
                            entrust_status="已撤";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("7")){
                            entrust_status="部成";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("8")){
                            entrust_status="已成";
                            map.put("tv_state",entrust_status);
                        }if(dataBean.getENTRUST_STATUS().equals("9")){
                            entrust_status="废单";
                            map.put("tv_state",entrust_status);
                        }

                        list.add(map);
                    }
                }

                adapter.setList(list);
                mListView.hideFooterView();
                mListView.hideHeaderView();
//                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onDownPullRefresh() {
        getData();
    }

    @Override
    public void onLoadingMore() {
        getData();
    }
}
