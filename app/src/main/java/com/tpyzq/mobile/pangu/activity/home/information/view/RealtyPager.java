package com.tpyzq.mobile.pangu.activity.home.information.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.ZxTabAdapter;
import com.tpyzq.mobile.pangu.data.InformationBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/9/18 10:55
 * 房地产模块
 */
public class RealtyPager extends BaseInfoPager {


    public static final String TAG = "RealtyPager";
    private PullToRefreshListView mListView;
    private ArrayList<InformationBean> list;
    private ZxTabAdapter adapter;
    private int mIndex;     //分页加载数据  的  标记
    private ProgressBar pb_Pager;
    private RelativeLayout rl_Pager;
    private LinearLayout llChongXinJiaZai;               //内容为空的   图片
    public RealtyPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        mIndex = 0;
        list = new ArrayList<InformationBean>();
        adapter = new ZxTabAdapter(mContext);
        getData(mIndex, ConstantUtil.CLEARED);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mContext, NewsDetailActivity.class);
                InformationBean informationBean = list.get(position - 1);
                String requestId = informationBean.getRequestId();
                intent.putExtra("requestId", requestId);
                mContext.startActivity(intent);
            }
        });

        //下拉刷新
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isShownHeader()) {
                    //判断头布局是否可见，如果可见执行下拉刷新
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

//                    mIndex = 0;
//                    getData(mIndex, ConstantUtil.CLEARED);
//                    adapter.notifyDataSetChanged();
//                    //将下拉视图收起
//                    mListView.onRefreshComplete();

                    //模拟加载数据线程休息3秒
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1000);
                                mIndex = 0;
                                getData(mIndex, ConstantUtil.CLEARED);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);

                            //完成对下拉刷新ListView的更新
                            adapter.notifyDataSetChanged();
                            //将下拉视图收起
                            mListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (refreshView.isShownFooter()) {
                    //判断尾布局是否可见，如果可见执行上拉加载更多
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");

                    mIndex += 30;
                    getData(mIndex, ConstantUtil.NOCLEARED);
                    adapter.notifyDataSetChanged();
                    //将视图收起
                    mListView.onRefreshComplete();

//                    //模拟加载数据线程休息3秒
//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... params) {
//                            try {
//                                Thread.sleep(100);
//                                mIndex += 30;
//                                getData(mIndex, ConstantUtil.NOCLEARED);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            return null;
//                        }
//
//                        @Override
//                        protected void onPostExecute(Void result) {
//                            super.onPostExecute(result);
//                            //完成对下拉刷新ListView的更新操作
//                            adapter.notifyDataSetChanged();
//                            //将视图收起
//                            mListView.onRefreshComplete();
//                        }
//                    }.execute();
                }
            }
        });

    }

    @Override
    public void setView() {
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.lvFinancial);
        mListView.setVisibility(View.GONE);
        pb_Pager = (ProgressBar) rootView.findViewById(R.id.pb_Pager);
        rl_Pager = (RelativeLayout) rootView.findViewById(R.id.rl_Pager);
        llChongXinJiaZai = (LinearLayout) rootView.findViewById(R.id.llChongXinJiaZai);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.financial_pager;

    }

    private void getData(int Index, final int type) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("tag", "房地产业");
        map2.put("offset", Index + "");
        map2.put("limit", "30");
        map2.put("type","2");
        map1.put("FUNCTIONCODE", "HQONG001");
        map1.put("TOKEN", "");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());

                pb_Pager.setVisibility(View.GONE);  //隐藏菊花
//                rl_Pager.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                rl_Pager.setBackgroundColor(mContext.getResources().getColor(R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);     //隐藏listView
                llChongXinJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
                llChongXinJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {       //点击图片重新请求数据
                        llChongXinJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        pb_Pager.setVisibility(View.VISIBLE);      //显示菊花
                        rl_Pager.setVisibility(View.VISIBLE);//显示背景
                        rl_Pager.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色

                        mIndex = 0;
                        getData(mIndex, ConstantUtil.CLEARED);

//                        //模拟加载数据线程休息1秒
//                        new AsyncTask<Void, Void, Void>() {
//                            @Override
//                            protected Void doInBackground(Void... params) {
//                                try {
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                return null;
//                            }
//
//                            @Override
//                            protected void onPostExecute(Void result) {
//                                super.onPostExecute(result);
//                                mIndex = 0;
//                                getData(mIndex, ConstantUtil.CLEARED);
//                            }
//                        }.execute();
                    }
                });

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if ("null\n".equals(response)) {
                    return;
                }

                if (type == ConstantUtil.CLEARED && list.size() > 0) {
                    list.clear();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        JSONArray message = jsonObject.getJSONArray("message");
                        for (int i = 0; i < message.length(); i++) {
                            InformationBean informationBean = new InformationBean();
                            JSONObject item = message.getJSONObject(i);
                            String ids = item.getString("id");
                            String title = item.getString("title");
                            long dt = item.getLong("dt");
                            Date date = new Date(dt);
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
                            String time = sdf.format(date);               //时间
                            informationBean.setPublishTitle(title);
                            informationBean.setTimes(time);
                            informationBean.setRequestId(ids);
                            list.add(informationBean);
                        }
                    } else {
                        pb_Pager.setVisibility(View.GONE);  //隐藏菊花
//                rl_Pager.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                        rl_Pager.setBackgroundColor(mContext.getResources().getColor(R.color.white));    //背景 为 白色
                        mListView.setVisibility(View.GONE);     //隐藏listView
                        llChongXinJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
                        llChongXinJiaZai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {       //点击图片重新请求数据
                                llChongXinJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                                pb_Pager.setVisibility(View.VISIBLE);      //显示菊花
                                rl_Pager.setVisibility(View.VISIBLE);//显示背景
                                rl_Pager.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色
                                mIndex = 0;
                                getData(mIndex, ConstantUtil.CLEARED);

//                                //模拟加载数据线程休息1秒
//                                new AsyncTask<Void, Void, Void>() {
//                                    @Override
//                                    protected Void doInBackground(Void... params) {
//                                        try {
//                                            Thread.sleep(100);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                        return null;
//                                    }
//
//                                    @Override
//                                    protected void onPostExecute(Void result) {
//                                        super.onPostExecute(result);
//                                        mIndex = 0;
//                                        getData(mIndex, ConstantUtil.CLEARED);
//                                    }
//                                }.execute();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pb_Pager.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                rl_Pager.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                adapter.setList(list);
            }
        });
    }
}
