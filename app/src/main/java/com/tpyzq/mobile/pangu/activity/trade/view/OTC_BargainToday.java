package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_EntrustTodayAdapter;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SkipUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/9/7 13:25
 */
public class OTC_BargainToday extends BasePager {

    private String TAG = "OTC_BargainToday";
    private PullToRefreshListView mListView;
    private ArrayList<HashMap<String, String>> list;
    private OTC_EntrustTodayAdapter adapter;
    private ImageView OTCBargainKong;

    public OTC_BargainToday(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        OTCBargainKong = (ImageView) rootView.findViewById(R.id.OTCBargainKong);        //空 图片
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.rl_OTCBargainRefresh);
    }

    @Override
    public void initData() {
        super.initData();
        list = new ArrayList<HashMap<String, String>>();
        adapter = new OTC_EntrustTodayAdapter(mContext);
        getData();
        mListView.setEmptyView(OTCBargainKong);
        mListView.setAdapter(adapter);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                            list.clear();
                            getData();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //将下拉视图收起
                        mListView.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }

    private void getData() {
        String mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("funcid", "300504");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    if ("0".equals(code)) {
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                String prod_name = data.getJSONObject(i).getString("PROD_NAME");
                                String prod_code = data.getJSONObject(i).getString("PROD_CODE");
                                String prod_prop_name = data.getJSONObject(i).getString("PROD_PROP_NAME");
                                String business_balance = data.getJSONObject(i).getString("BUSINESS_BALANCE");
                                String business_amount = data.getJSONObject(i).getString("BUSINESS_AMOUNT");
                                String date = data.getJSONObject(i).getString("INIT_DATE");
                                String times = data.getJSONObject(i).getString("BUSINESS_TIME");
                                String mDate = "";
                                String mTimes = "";
                                if (TextUtils.isEmpty(date)) {
                                    mDate = Helper.getInstance().getMyDateYMD(date);
                                }
                                if (!TextUtils.isEmpty(times)) {
//                                if (times.length() == 5) {
//                                    times = "0" + times;
//                                }
                                    mTimes = Helper.getInstance().getMyDateHMS(times);
                                }
                                map.put("tv_stockName", prod_name);
                                map.put("tv_stockCode", prod_code);
                                map.put("tv_businessName", prod_prop_name);
                                map.put("tv_businessMoney", business_balance);
                                map.put("tv_businessShare", business_amount);
                                map.put("tv_Data", mDate);
                                map.put("tv_Time", mTimes);
                                list.add(map);
                            }
                            adapter.setList(list);

                        }
                    } else if (code.equals("-6")) {
                        SkipUtils.getInstance().startLogin(mContext);
                    } else {
                        Helper.getInstance().showToast(mContext, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.setList(list);

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.otc_bargain_query;
    }

}
