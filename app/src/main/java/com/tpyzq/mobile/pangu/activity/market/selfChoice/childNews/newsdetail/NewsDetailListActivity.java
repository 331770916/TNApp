package com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews.newsdetail;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.market.NewsDetailListAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.ImportantEventDetailRelatedInfoBean;
import com.tpyzq.mobile.pangu.data.InformationBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 行情  详情界面   里面   新闻，公告，研报    点击  查看更多   跳转的  更多页面
 */
public class NewsDetailListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "NewsDetailList";
    private PullToRefreshListView mListView;
    private NewsDetailListAdapter adapter;
    private ArrayList<InformationBean> list;

    @Override
    public void initView() {
        this.findViewById(R.id.ivNewsDetailList_back).setOnClickListener(this);     //点击销毁当前界面
        mListView = (PullToRefreshListView) this.findViewById(R.id.lvNewsDetailList);
        initData();
    }

    private void initData() {
        list = new ArrayList<InformationBean>();
        getData();      //获取列表数据
        adapter = new NewsDetailListAdapter(this);
        mListView.setAdapter(adapter);

        // listView  点击条目  跳转详情界面
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                                list.clear();       //把 list 清空
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
                            //将视图收起
                            mListView.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });

    }

    /**
     * 网络请求获取列表数据
     */
    private void getData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("code","600069");
        map1.put("FUNCTIONCODE","HQONG002");
        map1.put("TOKEN","");
        map1.put("PARAMS",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
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
                Type type = new TypeToken<ImportantEventDetailRelatedInfoBean>() {}.getType();
                ImportantEventDetailRelatedInfoBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<ImportantEventDetailRelatedInfoBean.MessageBean> message = bean.getMessage();
                if(code.equals("200") && message !=null && message.size()>0){
                    for (int i = 0; i < message.size(); i++) {
                        ImportantEventDetailRelatedInfoBean.MessageBean messageBean = message.get(i);
                        String title = messageBean.getTitle();
                        long dt = messageBean.getDt();
                        Date date = new Date(dt);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
                        String formatTime = sdf.format(date);               //时间
                        InformationBean informationBean = new InformationBean();
                        informationBean.setPublishTitle(title);
                        informationBean.setTimes(formatTime);
                        list.add(informationBean);
                    }
                }
                adapter.setList(list);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivNewsDetailList_back:
                finish();
                break;
        }
    }
}
