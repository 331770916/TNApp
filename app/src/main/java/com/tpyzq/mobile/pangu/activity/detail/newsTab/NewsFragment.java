package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.adapter.detail.NewAnnStudyAdapter;
import com.tpyzq.mobile.pangu.data.DetailNewsEntity;
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
 * Created by zhangwenbo on 2016/6/30.
 * 新闻
 */
public class NewsFragment extends BaseDetailNewsPager implements View.OnClickListener {

    private final String TAG = "NewsFragment";
    private ListView mListView;
    private ArrayList<DetailNewsEntity> list;
    private ArrayList<DetailNewsEntity> newlist;
    private NewAnnStudyAdapter adapter;
    private RelativeLayout rlNews;          //背景
    private ProgressBar pb_New_Pager;      //菊花
    private TextView tvNewJiaZai, tvNewGengDuo;          //重新加载  , 点击查看更多
    private String stockCode, code;

    public NewsFragment(Context context, String stockCode) {
        super(context, stockCode);
    }


    @Override
    public void setView(String stockCode) {
        this.stockCode = stockCode;
        mListView = (ListView) rootView.findViewById(R.id.lvNews);
        rlNews = (RelativeLayout) rootView.findViewById(R.id.rlNews);               //初始化默认背景为 灰色
        pb_New_Pager = (ProgressBar) rootView.findViewById(R.id.pb_New_Pager);    //初始化显示 菊花
        tvNewJiaZai = (TextView) rootView.findViewById(R.id.tvNewJiaZai);         //重新加载
        tvNewGengDuo = (TextView) rootView.findViewById(R.id.tvNewGengDuo);       //点击查看更多
    }

    @Override
    public void initData() {
        super.initData();
        mListView.setVisibility(View.GONE);     //初始化 listView 隐藏
        tvNewGengDuo.setVisibility(View.GONE);  //初始化 隐藏 点击查看更多
        getData();
        list = new ArrayList<>();
        newlist = new ArrayList<>();
        adapter = new NewAnnStudyAdapter(mContext);
        mListView.setAdapter(adapter);
        tvNewGengDuo.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailNewsEntity bean = list.get(position);
                String newId = bean.getId();
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("requestId", newId);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 获取新闻列表数据
     */
    private void getData() {
//        code = stockCode.substring(2, stockCode.length());
        code = stockCode;
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("code", code);
        map1.put("FUNCTIONCODE", "HQONG002");
        map2.put("offset","0");
        map2.put("limit","30");
        map1.put("TOKEN", "");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_WA(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                pb_New_Pager.setVisibility(View.GONE);      //隐藏菊花
                rlNews.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);         //隐藏listView
                tvNewGengDuo.setVisibility(View.GONE);   //隐藏点击查看更多
                tvNewJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                tvNewJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvNewJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        pb_New_Pager.setVisibility(View.VISIBLE);      //显示菊花
                        rlNews.setVisibility(View.VISIBLE);//显示背景
                        rlNews.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色

                        //模拟加载数据线程休息1秒
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                super.onPostExecute(result);
                                getData();
                            }
                        }.execute();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)){
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        JSONArray message = jsonObject.optJSONArray("message");
                        if (message != null&&message.length()>0) {
                            for (int i = 0; i < message.length(); i++) {
                                DetailNewsEntity bean = new DetailNewsEntity();
                                JSONObject item = message.getJSONObject(i);
                                String newId = item.getString("id");        //新闻ID 用于跳转详情页
                                String title = item.getString("title");     //新闻标题
                                long dt = item.getLong("dt");               //时间的  long 值
                                Date date = new Date(dt);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time = simpleDateFormat.format(date);    //格式化后的 时间
                                bean.setTitle(title);
                                bean.setId(newId);
                                bean.setTime(time);
                                bean.setType(1);
                                list.add(bean);
                            }
                            pb_New_Pager.setVisibility(View.GONE);      //隐藏菊花
                            tvNewJiaZai.setVisibility(View.GONE);       //隐藏重新加载
                            rlNews.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));      //背景设置为白色
                            mListView.setVisibility(View.VISIBLE);      //请求到数据 展示 listView
                            tvNewGengDuo.setVisibility(View.VISIBLE);   //显示点击查看更多

                            if (list != null && list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (i < 2) {
                                        newlist.add(list.get(i));
                                    }
                                }

                                if (newlist != null && newlist.size() > 0) {
                                    if (newlist.size() >= 2) {
                                        tvNewGengDuo.setVisibility(View.VISIBLE);
                                    } else {
                                        tvNewGengDuo.setVisibility(View.GONE);
                                    }
                                }
                            }
                            adapter.setList(newlist);
                        } else {
                            showNoData();
                        }
                    } else {
                        showNoData();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //没有数据时的展示
            private void showNoData() {
                pb_New_Pager.setVisibility(View.GONE);      //隐藏菊花
                rlNews.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);         //隐藏listView
                tvNewGengDuo.setVisibility(View.GONE);   //隐藏点击查看更多
                tvNewJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                tvNewJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvNewJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        pb_New_Pager.setVisibility(View.VISIBLE);      //显示菊花
                        rlNews.setVisibility(View.VISIBLE);//显示背景
                        rlNews.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色

                        //模拟加载数据线程休息1秒
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                super.onPostExecute(result);
                                getData();
                            }
                        }.execute();
                    }
                });
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvNewGengDuo) {
            Intent intent = new Intent();
            intent.setClass(mContext, DetailNewsListActivity.class);
            intent.putExtra("code", code);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

}
