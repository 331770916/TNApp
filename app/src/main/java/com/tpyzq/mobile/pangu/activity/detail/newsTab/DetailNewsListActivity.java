package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.adapter.detail.NewsAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.DetailNewsEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialogTwo;
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
 * 详情页  新闻 tab  点击加载更多  跳转的页面
 */
public class DetailNewsListActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG ="DetailNewListActivity";
    private PullToRefreshListView mListView;
    private RelativeLayout rlDetailNew;
    private LinearLayout llNewJiaZai;
    private Dialog dialog;
    private NewsAdapter adapter;
    private int index;
    private ArrayList<DetailNewsEntity> list;
    private String code;

    @Override
    public void initView() {
        code = getIntent().getStringExtra("code");
        this.findViewById(R.id.ivDetailNew_back).setOnClickListener(this);              //返回按钮
        mListView = (PullToRefreshListView) this.findViewById(R.id.lvDetaiNewlList);   //listView
        llNewJiaZai = (LinearLayout) this.findViewById(R.id.llNewChongXinJiaZai);     //重新加载
        rlDetailNew = (RelativeLayout) this.findViewById(R.id.rlDetailNew);            //背景
        initData();
    }

    private void initData() {
        dialog = LoadingDialogTwo.initDialog(DetailNewsListActivity.this);  //菊花
        dialog.show();
        mListView.setVisibility(View.GONE);

        list = new ArrayList<>();
        adapter = new NewsAdapter(this);
        index = 0;
        getData(index, ConstantUtil.CLEARED);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailNewsEntity bean = list.get(position-1);
                String newId = bean.getId();
                Intent intent = new Intent(DetailNewsListActivity.this, NewsDetailActivity.class);
                intent.putExtra("requestId",newId);
                startActivity(intent);
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
                    //模拟加载数据线程休息3秒
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                index = 0;
                                getData(index,ConstantUtil.CLEARED);
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
                }else if (refreshView.isShownFooter()) {
                    //判断尾布局是否可见，如果可见执行上拉加载更多
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                    //模拟加载数据线程休息3秒
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                index += 30;
                                getData(index, ConstantUtil.NOCLEARED);
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
                            //将视图收起
                            mListView.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_list;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivDetailNew_back){
            finish();
        }
    }

    /**
     * 获取新闻列表数据
     */
    private void getData(int num, final int type) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("code",code);
        map2.put("offset",num + "");
        map2.put("limit","30");
        map1.put("FUNCTIONCODE","HQONG002");
        map1.put("TOKEN","");
        map1.put("PARAMS",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());
                dialog.dismiss();     //隐藏菊花
                rlDetailNew.setBackgroundColor(ContextCompat.getColor(DetailNewsListActivity.this,R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);         //隐藏listView
                llNewJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                llNewJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llNewJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        dialog.show();      //显示菊花
                        rlDetailNew.setVisibility(View.VISIBLE);//显示背景

                        rlDetailNew.setBackgroundColor(ContextCompat.getColor(DetailNewsListActivity.this,R.color.dividerColor)); //设置为灰色

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
                                index = 0;
                                getData(index,ConstantUtil.CLEARED);
                            }
                        }.execute();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {

                if(type == ConstantUtil.CLEARED && list.size()>0){
                    list.clear();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if("200".equals(code)){
                        JSONArray message = jsonObject.getJSONArray("message");
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();      //隐藏菊花
                }
                llNewJiaZai.setVisibility(View.GONE);       //隐藏重新加载
                rlDetailNew.setBackgroundColor(ContextCompat.getColor(DetailNewsListActivity.this,R.color.white));      //背景设置为白色
                mListView.setVisibility(View.VISIBLE);      //请求到数据 展示 listView

                adapter.setList(list);
            }
        });

    }
}
