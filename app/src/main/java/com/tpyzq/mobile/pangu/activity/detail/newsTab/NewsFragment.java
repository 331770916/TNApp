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
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.adapter.home.NewHomeInformationAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import java.util.ArrayList;
/**
 * Created by zhangwenbo on 2016/6/30.
 * 新闻
 */
public class NewsFragment extends BasePager implements View.OnClickListener ,InterfaceCollection.InterfaceCallback{

    private final String TAG = "NewsFragment";
    private ListView mListView;
    private ArrayList<InformationEntity> list;
    private ArrayList<InformationEntity> newlist;
    private NewHomeInformationAdapter adapter;
    private RelativeLayout rlNews;          //背景
    private ProgressBar pb_New_Pager;      //菊花
    private TextView tvNewJiaZai, tvNewGengDuo;          //重新加载  , 点击查看更多
    private String stockCode;

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
        initData();
    }

    @Override
    public void initData() {
        mListView.setVisibility(View.GONE);     //初始化 listView 隐藏
        tvNewGengDuo.setVisibility(View.GONE);  //初始化 隐藏 点击查看更多
        ifc.queryStockNews(stockCode,"30","1",TAG,this);
        list = new ArrayList<>();
        newlist = new ArrayList<>();
        adapter = new NewHomeInformationAdapter(mContext);
        mListView.setAdapter(adapter);
        tvNewGengDuo.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InformationEntity bean = list.get(position);
                String newId = bean.getNewsno();
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("requestId", newId);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public void callResult(ResultInfo info) {
        if(info.getCode().equals("200")){
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
            adapter.setDatas(newlist);
        }else {
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
                    ifc.queryStockNews(stockCode,"30","1",TAG,NewsFragment.this);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvNewGengDuo) {
            Intent intent = new Intent();
            intent.setClass(mContext, DetailNewsListActivity.class);
            intent.putExtra("code", stockCode);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public void destroy() {
        net.cancelSingleRequest(TAG);
    }
}
