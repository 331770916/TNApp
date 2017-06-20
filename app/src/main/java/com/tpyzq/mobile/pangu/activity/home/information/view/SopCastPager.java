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
import com.tpyzq.mobile.pangu.activity.home.information.adapter.SopCastAdapter;
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
 * 作者：刘泽鹏 on 2016/9/17 15:55
 * 要闻直播模块  首页
 */
public class SopCastPager extends BaseInfoPager {

    public static final String TAG = "SopCastPager";
    private PullToRefreshListView mListView;
    private SopCastAdapter adapter;
    private ArrayList<InformationBean> list;
    private int count;
    private ProgressBar pb_SopCastPager;            //菊花
    private RelativeLayout rlSopCast;               //包裹整个布局的  RelativeLayout
    private LinearLayout llSopCastJiaZai;          //内容为空的   图片

    public SopCastPager(Context context) {
        super(context);
    }


    @Override
    public void initData() {
        long stime = System.currentTimeMillis();
        count = 0;
        list = new ArrayList<InformationBean>();
        adapter = new SopCastAdapter(mContext);         //实例化适配器
        mListView.setAdapter(adapter);                  //适配
        getData("0", "30", ConstantUtil.CLEARED);                                        //获取数据源
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
//                    getData("0", "30", ConstantUtil.CLEARED);
//                    adapter.notifyDataSetChanged();
//                    //将下拉视图收起
//                    mListView.onRefreshComplete();

                    //模拟加载数据线程休息3秒
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1000);
                                getData("0", "30", ConstantUtil.CLEARED);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);

                            //完成对下拉刷新ListView的更新操作
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

                    count += 30;
                    getData(String.valueOf(count), "30", ConstantUtil.NOCLEARED);
                    adapter.notifyDataSetChanged();
                    //将视图收起
                    mListView.onRefreshComplete();

//                    //模拟加载数据线程休息3秒
//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... params) {
//                            try {
//                                Thread.sleep(100);
////                                list.clear();       //把 list 清空
//                                count += 30;
//                                getData(String.valueOf(count), "30", ConstantUtil.NOCLEARED);
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
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.lvSopCast);
        mListView.setVisibility(View.GONE);         //初始化隐藏 listView
        pb_SopCastPager = (ProgressBar) rootView.findViewById(R.id.pb_SopCastPager);    //显示菊花
        rlSopCast = (RelativeLayout) rootView.findViewById(R.id.rlSopCast);              //初始化  背景为灰色
        llSopCastJiaZai = (LinearLayout) rootView.findViewById(R.id.llSopCastJiaZai);
    }


    @Override
    public int getFragmentLayoutId() {
        return R.layout.sopcast_pager;
    }

    /**
     * 获取数据
     */
    private void getData(String offset, String limit, final int type) {

        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("offset", offset);
        map2.put("limit", limit);
        map1.put("FUNCTIONCODE", "HQONG007");
        map1.put("TOKEN", "");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());

                pb_SopCastPager.setVisibility(View.GONE);  //隐藏菊花
//                rlSopCast.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                rlSopCast.setBackgroundColor(mContext.getResources().getColor(R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);     //隐藏listView
                llSopCastJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
                llSopCastJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {       //点击图片重新请求数据
                        llSopCastJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        pb_SopCastPager.setVisibility(View.VISIBLE);      //显示菊花
                        rlSopCast.setVisibility(View.VISIBLE);//显示背景
                        rlSopCast.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色

                        getData("0", "30", ConstantUtil.CLEARED);

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
//                                getData("0", "30", ConstantUtil.CLEARED);
//                            }
//                        }.execute();
                    }
                });

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response.trim())) {
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
                            long dt = item.getLong("dt");
                            Date date = new Date(dt);
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd+HH:mm");
                            String time = sdf.format(date);               //时间
                            if (time == null) {
                                return;
                            }
                            String dates = time.substring(0, time.indexOf("+"));
                            String times = time.substring(time.indexOf("+") + 1, time.length());
                            String title = item.getString("title");
                            int pos = item.getInt("pos");
                            JSONArray stocks = item.getJSONArray("stocks");
                            if (stocks != null && stocks.length() > 0) {
                                ArrayList<String> nameList = new ArrayList<String>();
                                for (int j = 0; j < stocks.length(); j++) {
                                    JSONObject jsonObject1 = stocks.getJSONObject(j);
                                    String name = jsonObject1.getString("name");
                                    nameList.add(name);
                                }
                                informationBean.setSopCastList(nameList);
                            }
                            informationBean.setPublishTitle(title);     //标题
                            informationBean.setDate(dates);             //日期
                            informationBean.setTimes(times);            //时间
                            informationBean.setPos(pos);                //判断图片的 标识
                            informationBean.setRequestId(ids);          //新闻ID
                            list.add(informationBean);
                        }
                    } else {
//                        pb_SopCastPager.setVisibility(View.GONE);  //隐藏菊花
//                rlSopCast.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                        rlSopCast.setBackgroundColor(mContext.getResources().getColor(R.color.white));    //背景 为 白色
                        mListView.setVisibility(View.GONE);     //隐藏listView
                        llSopCastJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
                        llSopCastJiaZai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {       //点击图片重新请求数据
                                llSopCastJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                                pb_SopCastPager.setVisibility(View.VISIBLE);      //显示菊花
                                rlSopCast.setVisibility(View.VISIBLE);//显示背景
                                rlSopCast.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色

                                getData("0", "30", ConstantUtil.CLEARED);

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
//                                        getData("0", "30", ConstantUtil.CLEARED);
//                                    }
//                                }.execute();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pb_SopCastPager.setVisibility(View.GONE);       //隐藏 菊花
                rlSopCast.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));   //背景设置为白色
                mListView.setVisibility(View.VISIBLE);          //显示 listView
                adapter.setList(list);
            }
        });
    }
}
