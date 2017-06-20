package com.tpyzq.mobile.pangu.activity.home.information;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.ImportantEventAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.ImportantEventValueEntity;
import com.tpyzq.mobile.pangu.data.InformationBean;
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
 * 重大事件  列表界面 Activity
 * 刘泽鹏
 */
public class ImportantEventActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "ImportantEventActivity";
    private PullToRefreshListView mListView;     //下拉刷新的   listView
    private ImportantEventAdapter adapter;
    private ArrayList<String> keyList;              //存储  日期  集合
    private ArrayList<InformationBean> eventList;   //
    private HashMap<String,ArrayList<HashMap>> valueMap;     // 日期  对应的  当日的数据
    private ArrayList<ImportantEventValueEntity> valueList;
    private int mIndex;
    private RelativeLayout rlImportantEvent;    //包裹布局的 RelativeLayout
    private LinearLayout llImportantEventJiaZai;        //重新加载的图片
    private Dialog dialog;


    @Override
    public void initView() {
        mListView = (PullToRefreshListView) this.findViewById(R.id.lvImportantEvent);
        mListView.setVisibility(View.GONE);
        rlImportantEvent = (RelativeLayout) this.findViewById(R.id.rlImportantEvent);  //初始化默认背景为 灰色
        llImportantEventJiaZai = (LinearLayout) this.findViewById(R.id.llImportantEventJiaZai); //
        dialog = LoadingDialogTwo.initDialog(ImportantEventActivity.this);    //菊花
        dialog.show();  //显示  菊花
        initData();
    }

    private void initData() {
        keyList = new ArrayList<String>();
        eventList = new ArrayList<InformationBean>();
        valueMap = new HashMap<String,ArrayList<HashMap>>();
        valueList = new ArrayList<ImportantEventValueEntity>();
        adapter = new ImportantEventAdapter(this);
        mListView.setAdapter(adapter);
        mIndex = 0;
        getEventData(mIndex, ConstantUtil.CLEARED);

        //给  ListView 添加 item 点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImportantEventValueEntity importantEventValueBean = valueList.get(position-1);
                String id1 = importantEventValueBean.getId();
                Intent intent = new Intent();
                intent.setClass(ImportantEventActivity.this,ImportantEventDetailActivity.class);
                intent.putExtra("id",id1);
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

                    mIndex=0;
                    getEventData(mIndex,ConstantUtil.CLEARED);
                    adapter.notifyDataSetChanged();
                    //将下拉视图收起
                    mListView.onRefreshComplete();

//                    //模拟加载数据线程休息3秒
//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... params) {
//                            try {
//                                Thread.sleep(1000);
//                                mIndex=0;
//                                getEventData(mIndex,ConstantUtil.CLEARED);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            return null;
//                        }
//
//                        @Override
//                        protected void onPostExecute(Void result) {
//                            super.onPostExecute(result);
//
//                            //完成对下拉刷新ListView的更新操作
//                            adapter.notifyDataSetChanged();
//                            //将下拉视图收起
//                            mListView.onRefreshComplete();
//                        }
//                    }.execute();
                } else if (refreshView.isShownFooter()) {
                    //判断尾布局是否可见，如果可见执行上拉加载更多
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");

                    mIndex++;
                    if(keyList != null && keyList.size()>0){
                        keyList.clear();
                    }
                    if(valueMap != null && valueMap.size()>0){
                        valueMap.clear();
                    }
                    getEventData(mIndex,ConstantUtil.NOCLEARED);
                    adapter.notifyDataSetChanged();
                    //将视图收起
                    mListView.onRefreshComplete();

//                    //模拟加载数据线程休息3秒
//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... params) {
//                            try {
//                                Thread.sleep(1000);
//                                mIndex++;
//                                if(keyList != null && keyList.size()>0){
//                                    keyList.clear();
//                                }
//                                if(valueMap != null && valueMap.size()>0){
//                                    valueMap.clear();
//                                }
//                                getEventData(mIndex,ConstantUtil.NOCLEARED);
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

        this.findViewById(R.id.ivImportantEvent_back).setOnClickListener(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_important_event;
    }

    @Override
    public void onClick(View v) {
        if(R.id.ivImportantEvent_back == v.getId()){
            finish();
        }
    }

    /**
     * 获取重大事件数据
     */
    private void getEventData(int pageIndex, final int type) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("offset",pageIndex);
        map1.put("FUNCTIONCODE","HQONG008");
        map1.put("TOKEN","");
        map1.put("PARAMS",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());
                mListView.setVisibility(View.GONE);     //隐藏listView
                rlImportantEvent.setBackgroundColor(ContextCompat.getColor(ImportantEventActivity.this,R.color.white));  //初始化默认背景为 灰色
                dialog.dismiss();  // 隐藏 菊花
                llImportantEventJiaZai.setVisibility(View.VISIBLE); // 显示 重新加载
                llImportantEventJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llImportantEventJiaZai.setVisibility(View.GONE); // 隐藏 重新加载
                        rlImportantEvent.setVisibility(View.VISIBLE);   //显示背景
                        rlImportantEvent.setBackgroundColor(ContextCompat.getColor(ImportantEventActivity.this,
                                R.color.dividerColor));     //背景变灰色
                        dialog.show();// 显示 菊花

                        mIndex = 0;
                        getEventData(mIndex,ConstantUtil.CLEARED);
//                        //模拟加载数据线程休息1秒
//                        new AsyncTask<Void, Void, Void>() {
//                            @Override
//                            protected Void doInBackground(Void... params) {
//                                try {
//                                    Thread.sleep(1000);
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
//                                getEventData(mIndex,ConstantUtil.CLEARED);
//                            }
//                        }.execute();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                if("null\n".equals(response)){
                    return;
                }


                if(type == ConstantUtil.CLEARED && valueList.size()>0){
                    valueList.clear();
                }
                if(type == ConstantUtil.CLEARED && keyList.size()>0){
                    keyList.clear();
                }
                if(type == ConstantUtil.CLEARED && valueMap.size()>0){
                    valueMap.clear();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if("200".equals(code)){
                        JSONObject message = jsonObject.getJSONObject("message");
                        long date = message.getLong("date");
                        if(date == 0){
                            return;
                        }
                        Date time = new Date(date);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String timed = sdf.format(time);      //时间

                        JSONArray stocks = message.getJSONArray("stocks");
                        ArrayList<HashMap> stockList = new ArrayList<HashMap>();
                        for (int i = 0; i < stocks.length(); i++) {
                            HashMap map = new HashMap();
                            JSONObject item = stocks.getJSONObject(i);
                            String titl = item.getString("titl");
                            String id1 = item.getString("id");
                            int type = item.getInt("type");
                            long pdt = item.getLong("pdt");
                            map.put("titl",titl);
                            map.put("id1",id1);
                            map.put("type",type);
                            map.put("pdt",pdt);
                            stockList.add(map);
                        }

                        keyList.add(timed);
                        valueMap.put(timed,stockList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                ++mIndex;
//                if (mIndex < 5) {
//                    getEventData(mIndex);
//                } else {

                    for (int k = 0; k < keyList.size(); k++) {
                        ImportantEventValueEntity keyBean = new ImportantEventValueEntity();
                        keyBean.setItemType(0);
                        keyBean.setDate(keyList.get(k));
                        valueList.add(keyBean);

                        ArrayList<HashMap> value = valueMap.get("" + keyList.get(k));
                        if(value == null && value.size()<=0){
                            return;
                        }
                        for (int h = 0; h < value.size(); h++) {
                            ImportantEventValueEntity beans = new ImportantEventValueEntity();
                            HashMap map = value.get(h);
                            String titl = (String) map.get("titl");
                            String ids = (String) map.get("id1");
                            int types = (int) map.get("type");
                            long pdt = (long) map.get("pdt");
                            Date time = new Date(pdt);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String timed = sdf.format(time);      //时间
                            beans.setTiele(titl);
                            beans.setTime(timed);
                            beans.setImgType(types);
                            beans.setItemType(1);
                            beans.setId(ids);
                            valueList.add(beans);
                        }

                    }

                rlImportantEvent.setBackgroundColor(ContextCompat.getColor(ImportantEventActivity.this,R.color.white));  //初始化默认背景为 灰色
                dialog.dismiss();  // 隐藏 菊花
                mListView.setVisibility(View.VISIBLE);
                adapter.setList(valueList);
                }
//            }

        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }
}
