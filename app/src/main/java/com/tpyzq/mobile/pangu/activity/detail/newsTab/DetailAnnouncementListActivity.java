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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 详情页面  公告tab  点击  加载更多 跳转的页面
 */
public class DetailAnnouncementListActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG ="DetailAnnouncementList";
    private PullToRefreshListView mListView;
    private RelativeLayout rlDetailAnnouncement;
    private LinearLayout llAnnouncementJiaZai;
    private Dialog dialog;
    private NewsAdapter adapter;
    private int index;
    private ArrayList<DetailNewsEntity> list;
    private String stockCode;

    @Override
    public void initView() {
        stockCode = getIntent().getStringExtra("stockCode");
        this.findViewById(R.id.ivDetailAnnouncement_back).setOnClickListener(this);              //返回按钮
        mListView = (PullToRefreshListView) this.findViewById(R.id.lvDetaiAnnouncementList);   //listView
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        llAnnouncementJiaZai = (LinearLayout) this.findViewById(R.id.llAnnouncementJiaZai);     //重新加载
        rlDetailAnnouncement = (RelativeLayout) this.findViewById(R.id.rlDetailAnnouncement);  //背景
        initData();
    }

    private void initData() {
        dialog = LoadingDialogTwo.initDialog(DetailAnnouncementListActivity.this);  //菊花
        dialog.show();
        mListView.setVisibility(View.GONE);

        list = new ArrayList<>();
        adapter = new NewsAdapter(this);
        getData();
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailNewsEntity bean = list.get(position-1);
                String msgId = bean.getId();
                Intent intent = new Intent(DetailAnnouncementListActivity.this,AnnouncementStydyDetailActivity.class);
                intent.putExtra("msgId",msgId);
                intent.putExtra("type",1);
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
                                getData();
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
                }/*else if (refreshView.isShownFooter()) {
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
                                getData(index);
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
                }*/
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_announcement_list;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivDetailAnnouncement_back){
            finish();
        }
    }

    private void getData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("channelid","000100010001");
        map2.put("code",stockCode);
        map1.put("funcid","900103");
        map1.put("token","");
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_ZX_GS, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());
                if (!isFinishing())
                    dialog.dismiss();     //隐藏菊花
                rlDetailAnnouncement.setBackgroundColor(ContextCompat.getColor(DetailAnnouncementListActivity.this,R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);         //隐藏listView
                llAnnouncementJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                llAnnouncementJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llAnnouncementJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        if (!isFinishing())
                            dialog.show();      //显示菊花
                        rlDetailAnnouncement.setVisibility(View.VISIBLE);//显示背景

                        rlDetailAnnouncement.setBackgroundColor(ContextCompat.getColor(DetailAnnouncementListActivity.this,R.color.dividerColor)); //设置为灰色

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

                if(list != null && list.size()>0){
                    list.clear();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if("0".equals(code)){
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            DetailNewsEntity bean = new DetailNewsEntity();
                            JSONArray item = data.getJSONArray(i);
                            String title = item.getString(1);
                            String source = item.getString(7);
                            String time = item.getString(19);
                            String msgId = item.getString(0);
                            try {
                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                                Date parse = sdf1.parse(time);
                                String format = sdf2.format(parse);
                                bean.setTime(format);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            bean.setId(msgId);
                            bean.setTitle(title);
                            bean.setSource(source);
                            bean.setType(2);
                            list.add(bean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!isFinishing())
                    dialog.dismiss();      //隐藏菊花
                llAnnouncementJiaZai.setVisibility(View.GONE);       //隐藏重新加载
                rlDetailAnnouncement.setBackgroundColor(ContextCompat.getColor(DetailAnnouncementListActivity.this,R.color.white));      //背景设置为白色
                mListView.setVisibility(View.VISIBLE);      //请求到数据 展示 listView

                adapter.setList(list);
            }
        });
    }
}
