package com.tpyzq.mobile.pangu.activity.home.information;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.ZxTabAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.InformationBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;


/**
 * 新闻详情  点击 相关标签  跳转的  新闻标签页面
 */
public class IndustryRelevanceActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "IndustryRelevance";
    private PullToRefreshListView mListView = null;
    private TextView tvActivityName;
    private ZxTabAdapter adapter;
    private String title;
    private ArrayList<InformationBean> list;
    private int mIndex = 0;


    @Override
    public void initView() {
        mListView = (PullToRefreshListView) this.findViewById(R.id.lvIndustryRelevance);
        tvActivityName = (TextView) this.findViewById(R.id.tvActivityName);
        this.findViewById(R.id.ivIndustryRelevance_back).setOnClickListener(this);
        initData();
    }

    private void initData() {
        title = getIntent().getStringExtra("keyword");
        tvActivityName.setText(title);
        list = new ArrayList<InformationBean>();
        adapter = new ZxTabAdapter(this);
        getData(mIndex);
        mListView.setAdapter(adapter);

        //item  点击跳转详情页
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(IndustryRelevanceActivity.this, NewsDetailActivity.class);
                InformationBean informationBean = list.get(position-1);
                String requestId = informationBean.getRequestId();
                intent.putExtra("requestId",requestId);
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
                                list.clear();
                                mIndex = 0;
                                getData(mIndex);
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
                                mIndex += 30;
                                getData(mIndex);
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
    public void onClick(View v) {
        if(v.getId() == R.id.ivIndustryRelevance_back){
            this.finish();      //点击返回销毁当前界面
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_industry_relevance;
    }

    /**
     * 获取数据
     */
    private void getData(int index) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("tag",title);
        map2.put("offset",index+"");
        map2.put("limit","30");
        map2.put("type","1");
        map1.put("FUNCTIONCODE","HQONG001");
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
                if("null\n".equals(response)){
                    return;
                }
                try {
                    JSONObject res = new JSONObject(response);
                    if("200".equals(res.optString("code"))){
                        JSONArray jsonArray = res.getJSONArray("message");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0;i < jsonArray.length();i++){
                                InformationBean informationBean = new InformationBean();
                                informationBean.setPublishTitle(jsonArray.getJSONObject(i).optString("title"));
                                String time = new SimpleDateFormat("MM-dd  HH:mm").format(new Date(jsonArray.getJSONObject(i).optLong("dt")));
                                informationBean.setTimes(time);
                                informationBean.setRequestId(jsonArray.getJSONObject(i).optString("id"));
                                list.add(informationBean);
                            }
                        }
                        adapter.setList(list);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //以前解析方式
//                Gson gson = new Gson();
//                Type type = new TypeToken<TabEntity>() {}.getType();
//                TabEntity tabBean = gson.fromJson(response, type);
//                String code = tabBean.getCode();
//                List<TabEntity.MessageBean> message = tabBean.getMessage();
//                if(code.equals("200") && message !=null && message.size()>0){
//                    for (int i = 0; i < message.size(); i++) {
//                        InformationBean informationBean = new InformationBean();
//                        TabEntity.MessageBean messageBean = message.get(i);
//                        String ids = messageBean.getId();
//                        String title = messageBean.getTitle();
//                        long dt = messageBean.getDt();
//                        Date date = new Date(dt);
//                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
//                        String time = sdf.format(date);               //时间
//                        informationBean.setPublishTitle(title);
//                        informationBean.setTimes(time);
//                        informationBean.setRequestId(ids);
//                        list.add(informationBean);
//                    }
//                }
//                adapter.setList(list);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

}
