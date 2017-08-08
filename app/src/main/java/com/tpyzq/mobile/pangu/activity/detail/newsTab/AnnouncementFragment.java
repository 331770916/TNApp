package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.detail.AnnouncementAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.data.InformationEntity;
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
 * 公告页面
 */
public class AnnouncementFragment extends BasePager implements View.OnClickListener {

    private final String TAG = "AnnouncementFragment";
    private ListView mListView;
    private ArrayList<InformationEntity> list;
    private ArrayList<InformationEntity> newlist;
    private AnnouncementAdapter adapter;
    private RelativeLayout rlAnnouncement;          //背景
    private ProgressBar pb_Announcement_Pager;      //菊花
    private TextView tvAnnouncementJiaZai,tvAnnouncementGengDuo;          //重新加载 , 加载更多
    private String stockCode;

    public AnnouncementFragment(Context context,String stockCode) {
        super(context,stockCode);
    }

    @Override
    public void setView(String stockCode) {
        this.stockCode = stockCode;
        mListView = (ListView) rootView.findViewById(R.id.lvAnnouncement);
        rlAnnouncement = (RelativeLayout) rootView.findViewById(R.id.rlAnnouncement);               //初始化默认背景为 灰色
        pb_Announcement_Pager = (ProgressBar) rootView.findViewById(R.id.pb_Announcement_Pager);    //初始化显示 菊花
        tvAnnouncementJiaZai = (TextView) rootView.findViewById(R.id.tvAnnouncementJiaZai);         //重新加载
        tvAnnouncementGengDuo = (TextView) rootView.findViewById(R.id.tvAnnouncementGengDuo);       //加载更多
        initData();
    }

    @Override
    public void initData() {
        mListView.setVisibility(View.GONE);     //初始化 listView 隐藏
        tvAnnouncementGengDuo.setVisibility(View.GONE);  //初始化 隐藏 点击查看更多
        getData();
        list = new ArrayList<>();
        newlist = new ArrayList<>();
        adapter = new AnnouncementAdapter(mContext);
        mListView.setAdapter(adapter);
        tvAnnouncementGengDuo.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InformationEntity bean = list.get(position);
                String msgId = bean.getNewsno();
                Intent intent = new Intent(mContext,AnnouncementStydyDetailActivity.class);
                intent.putExtra("msgId",msgId);
                intent.putExtra("type",1);
                mContext.startActivity(intent);
            }
        });
    }

    private void getData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("channelid","000100010001");
        map2.put("code",stockCode);
        map1.put("funcid","900103");
        map1.put("token","");
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_HQ_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                pb_Announcement_Pager.setVisibility(View.GONE);      //隐藏菊花
                rlAnnouncement.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                mListView.setVisibility(View.GONE);         //隐藏listView
                tvAnnouncementGengDuo.setVisibility(View.GONE);   //隐藏点击查看更多
                tvAnnouncementJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                tvAnnouncementJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvAnnouncementJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        pb_Announcement_Pager.setVisibility(View.VISIBLE);      //显示菊花
                        rlAnnouncement.setVisibility(View.VISIBLE);//显示背景
                        rlAnnouncement.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dividerColor)); //设置为灰色
                        getData();
                    }
                });

            }

            @Override
            public void onResponse(String response, int id) {
//                Log.i(TAG,response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    if("0".equals(code)){
                        JSONArray data = jsonObject.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            InformationEntity bean = new InformationEntity();
                            JSONArray item = data.optJSONArray(i);
                            if (item==null){
                                continue;
                            }
                            String title = item.optString(1);
                            String source = item.optString(7);
                            String time = item.optString(19);
                            String msgId = item.optString(0);
                            try {
                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                                Date parse = sdf1.parse(time);
                                String format = sdf2.format(parse);
                                bean.setTime(format);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            bean.setNewsno(msgId);
                            bean.setTitle(title);
                            bean.setSource(source);
                            bean.setType(2);
                            list.add(bean);
                        }

                        pb_Announcement_Pager.setVisibility(View.GONE);      //隐藏菊花
                        tvAnnouncementJiaZai.setVisibility(View.GONE);       //隐藏重新加载
                        rlAnnouncement.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));      //背景设置为白色
                        mListView.setVisibility(View.VISIBLE);      //请求到数据 展示 listView
                        tvAnnouncementGengDuo.setVisibility(View.VISIBLE);   //显示点击查看更多

                        if (list != null && list.size() > 0) {

                            for (int i = 0; i < list.size(); i++) {
                                if (i < 2) {
                                    newlist.add(list.get(i));
                                }
                            }
                            if (newlist != null && newlist.size() > 0) {
                                if (newlist.size() >= 2) {
                                    tvAnnouncementGengDuo.setVisibility(View.VISIBLE);
                                } else {
                                    tvAnnouncementGengDuo.setVisibility(View.GONE);
                                }
                            }
                        }
                        adapter.setList(newlist);
                    }else {
                        pb_Announcement_Pager.setVisibility(View.GONE);      //隐藏菊花
                        rlAnnouncement.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));    //背景 为 白色
                        mListView.setVisibility(View.GONE);         //隐藏listView
                        tvAnnouncementGengDuo.setVisibility(View.GONE);   //隐藏点击查看更多
                        tvAnnouncementJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                        tvAnnouncementJiaZai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvAnnouncementJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                                pb_Announcement_Pager.setVisibility(View.VISIBLE);      //显示菊花
                                rlAnnouncement.setVisibility(View.VISIBLE);//显示背景
                                rlAnnouncement.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dividerColor)); //设置为灰色
                                //模拟加载数据线程休息1秒
                                getData();
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
        if(v.getId() == R.id.tvAnnouncementGengDuo){
            Intent intent = new Intent();
            intent.setClass(mContext, DetailAnnouncementListActivity.class);
            intent.putExtra("stockCode",stockCode);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_announcement;
    }

    @Override
    public void destroy() {
        net.cancelSingleRequest(TAG);
    }
}
