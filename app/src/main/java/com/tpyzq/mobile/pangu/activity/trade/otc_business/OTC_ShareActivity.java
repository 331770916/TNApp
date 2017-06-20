package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OTCShareQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OtcShareEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 刘泽鹏
 * OTC 份额查询界面
 */
public class OTC_ShareActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "OTC_ShareActivity";
    private PullToRefreshScrollView mPullRefreshScrollView;
    private MyListView mListView;
    private ArrayList<OtcShareEntity> list;
    private OTCShareQueryAdapter adapter;
    private ImageView iv_ShareKong;             //空图片
    private TextView tvOtc_ShareMarketValue;   //市值
    private ListView listView;


    @Override
    public void initView() {
        iv_ShareKong = (ImageView) this.findViewById(R.id.iv_ShareKong);
        tvOtc_ShareMarketValue = (TextView) this.findViewById(R.id.tvOtc_ShareMarketValue);
        this.findViewById(R.id.ivOTC_ShareQueryBack).setOnClickListener(this);   //返回按钮
        mPullRefreshScrollView = (PullToRefreshScrollView) this.findViewById(R.id.svOtcShate);  //scrollView
        this.mListView = (MyListView) this.findViewById(R.id.lvShareQuery);        //listView
        getDate();                                                                 //获取数据源
        list = new ArrayList<OtcShareEntity>();
        adapter = new OTCShareQueryAdapter(OTC_ShareActivity.this);
        mListView.setFocusable(false);
        mListView.setAdapter(adapter);

        //上拉、下拉设定  Mode.PULL_FROM_START
        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);


        //上拉监听函数
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (refreshView.isShownHeader()) {
                    //判断头布局是否可见，如果可见执行下拉刷新
                    //设置尾布局样式文字
                    mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    //模拟加载数据线程休息3秒
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                if(list != null && list.size()>0){
                                    list.clear();
                                }
                                getDate();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);

                            //完成对下拉刷新ListView的更新操作
//                            adapter.notifyDataSetChanged();
                            //将下拉视图收起
                            mPullRefreshScrollView.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });

    }

    /**
     * 获取数据源
     */
    private void getDate() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300501");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                mPullRefreshScrollView.setVisibility(View.GONE);        //请求失败  隐藏 scrollView
                iv_ShareKong.setVisibility(View.VISIBLE);               //显示  空图片
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if("0".equals(code)){
                        JSONArray data = jsonObject.getJSONArray("data");

                        if(data.length() == 0){
                            mPullRefreshScrollView.setVisibility(View.GONE);        //请求失败  隐藏 scrollView
                            iv_ShareKong.setVisibility(View.VISIBLE);               //显示  空图片
                        }

                        for(int i = 0;i<data.length();i++){
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String otc_market_value = jsonObject1.getString("OTC_MARKET_VALUE"); //市值
                            tvOtc_ShareMarketValue.setText(otc_market_value);                   //给 市值 赋值
                            JSONArray otc_list = jsonObject1.getJSONArray("OTC_LIST");

                            if(otc_list.length() == 0){
                                mPullRefreshScrollView.setVisibility(View.GONE);        //请求失败  隐藏 scrollView
                                iv_ShareKong.setVisibility(View.VISIBLE);               //显示  空图片
                            }

                            for(int j=0;j<otc_list.length();j++){
                                OtcShareEntity otcShareIntentBean = new OtcShareEntity();
                                JSONObject item = otc_list.getJSONObject(j);
                                String current_amount = item.getString("CURRENT_AMOUNT");
                                String prod_name = item.getString("PROD_NAME");
                                String prod_code = item.getString("PROD_CODE");
                                String buy_date = item.getString("BUY_DATE");
//                                String prod_end_date = item.getString("PROD_END_DATE");
                                otcShareIntentBean.setCurrent_amount(current_amount);//份额
                                otcShareIntentBean.setProd_name(prod_name);         //股票名称
                                otcShareIntentBean.setProd_code(prod_code);         //股票代码
                                otcShareIntentBean.setBuy_date(buy_date);           //购入日期
//                                otcShareIntentBean.setProd_end_date(prod_end_date);//到期日期
                                otcShareIntentBean.setUnFold(false);
                                list.add(otcShareIntentBean);
                            }
                        }

                    }
                    adapter.setList(list);      //添加数据
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__share;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivOTC_ShareQueryBack) {
            this.finish();                                                  //点击返回按钮销毁当前activity
        }
    }
}
