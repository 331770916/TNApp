package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.detail.AnnouncementAdapter;
import com.tpyzq.mobile.pangu.data.DetailNewsEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
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
 * Created by zhangwenbo on 2016/6/30.
 * 研报页面
 */
public class StudyFragment extends BaseDetailNewsPager implements View.OnClickListener{

    private final String TAG = "StudyFragment";
    private ListView mListView;
    private ArrayList<DetailNewsEntity> list;
    private ArrayList<DetailNewsEntity> newlist;
    private AnnouncementAdapter adapter;
    private RelativeLayout rlStudy;          //背景
    private ProgressBar pb_Study_Pager;      //菊花
    private TextView tvStudyJiaZai,tvStudyGengDuo;          //重新加载 , 加载更多
    private String stockCode;

    public StudyFragment(Context context,String stockCode) {
        super(context,stockCode);
    }

    @Override
    public void setView(String stockCode) {
        this.stockCode = stockCode;
        mListView = (ListView) rootView.findViewById(R.id.lvDetailNewsStudy);
        rlStudy = (RelativeLayout) rootView.findViewById(R.id.rlStudy);               //初始化默认背景为 灰色
        pb_Study_Pager = (ProgressBar) rootView.findViewById(R.id.pb_Study_Pager);    //初始化显示 菊花
        tvStudyJiaZai = (TextView) rootView.findViewById(R.id.tvStudyJiaZai);         //重新加载
        tvStudyGengDuo = (TextView) rootView.findViewById(R.id.tvStudyGengDuo);         //加载更多
    }

    @Override
    public void initData() {
        super.initData();
        mListView.setVisibility(View.GONE);         //初始化 隱藏
        tvStudyGengDuo.setVisibility(View.GONE);  //初始化 隐藏 点击查看更多
        getData(stockCode);
        list = new ArrayList<>();
        newlist = new ArrayList<>();
        adapter = new AnnouncementAdapter(mContext);
        mListView.setAdapter(adapter);
        tvStudyGengDuo.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailNewsEntity bean = list.get(position);
                String msgId = bean.getId();
                Intent intent = new Intent(mContext,AnnouncementStydyDetailActivity.class);
                intent.putExtra("msgId",msgId);
                intent.putExtra("type",2);
                mContext.startActivity(intent);
            }
        });
    }

    private void getData(final String stockCode) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("channelid","000100010002");
        map2.put("code",stockCode);
        map1.put("funcid","900103");
        map1.put("token","");
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_ZX_GS, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());

                pb_Study_Pager.setVisibility(View.GONE);      //隐藏菊花
                rlStudy.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);         //隐藏listView
                tvStudyGengDuo.setVisibility(View.GONE);   //隐藏点击查看更多
                tvStudyJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                tvStudyJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvStudyJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        pb_Study_Pager.setVisibility(View.VISIBLE);      //显示菊花
                        rlStudy.setVisibility(View.VISIBLE);//显示背景
                        rlStudy.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dividerColor)); //设置为灰色

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
                                getData(stockCode);
                            }
                        }.execute();
                    }
                });

            }

            @Override
            public void onResponse(String response, int id) {
//                Log.i(TAG,response);
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

                        pb_Study_Pager.setVisibility(View.GONE);      //隐藏菊花
                        tvStudyJiaZai.setVisibility(View.GONE);       //隐藏重新加载
                        rlStudy.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));      //背景设置为白色
                        mListView.setVisibility(View.VISIBLE);      //请求到数据 展示 listView
                        tvStudyGengDuo.setVisibility(View.VISIBLE);   //显示点击查看更多

                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                if (i < 2) {
                                    newlist.add(list.get(i));
                                }
                            }
                            if (newlist != null && newlist.size() > 0) {
                                if (newlist.size() >= 2) {
                                    tvStudyGengDuo.setVisibility(View.VISIBLE);
                                } else {
                                    tvStudyGengDuo.setVisibility(View.GONE);
                                }
                            }
                        }
                        adapter.setList(newlist);
                    } else {
                        pb_Study_Pager.setVisibility(View.GONE);      //隐藏菊花
                        mListView.setVisibility(View.GONE);      //请求到数据 展示 listView
                        tvStudyGengDuo.setVisibility(View.GONE);   //显示点击查看更多
                        rlStudy.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                        tvStudyJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                        tvStudyJiaZai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvStudyJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                                pb_Study_Pager.setVisibility(View.VISIBLE);      //显示菊花
                                rlStudy.setVisibility(View.VISIBLE);//显示背景
                                rlStudy.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dividerColor)); //设置为灰色

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
                                        getData(stockCode);
                                    }
                                }.execute();
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvStudyGengDuo){
            Intent intent = new Intent();
            intent.setClass(mContext, DetailStudyListActivity.class);
            intent.putExtra("stockCode",stockCode);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_study;
    }
}
