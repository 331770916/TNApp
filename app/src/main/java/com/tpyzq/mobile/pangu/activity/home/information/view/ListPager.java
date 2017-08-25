package com.tpyzq.mobile.pangu.activity.home.information.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.tpyzq.mobile.pangu.activity.home.information.adapter.ZxTabAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import static com.tpyzq.mobile.pangu.util.ConstantUtil.ZIXUN_NUM;

/**
 * 作者：刘泽鹏 on 2016/9/18 10:55
 * 房地产模块
 */
public class ListPager extends BasePager implements InterfaceCollection.InterfaceCallback{
    private String classno;
    private PullToRefreshListView mListView;
    private ZxTabAdapter adapter;
    private ArrayList<InformationEntity> list;
    private int mIndex = 1;     //分页加载数据  的  标记
    private RelativeLayout rl_Pager;
    private LinearLayout llChongXinJiaZai;               //内容为空的   图片
    private boolean isFirst = true;
    private Dialog dialog;

    public ListPager(Context context,String param) {
        super(context,param);
    }

    @Override
    public void initData() {
        if(isFirst) {
            dialog.show();
            ifc.queryHkstocks(classno, ZIXUN_NUM, String.valueOf(mIndex), classno, this);
            isFirst = false;
        }
    }

    @Override
    public void setView(String params) {
        this.classno = params;
        dialog = LoadingDialog.initDialog((Activity) mContext, "加载中...");
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.lvFinancial);
        rl_Pager = (RelativeLayout) rootView.findViewById(R.id.rl_Pager);
        llChongXinJiaZai = (LinearLayout) rootView.findViewById(R.id.llChongXinJiaZai);
        list = new ArrayList<>();
        adapter = new ZxTabAdapter(mContext);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mContext, NewsDetailActivity.class);
                InformationEntity informationBean = list.get(position - 1);
                intent.putExtra("requestId", informationBean.getNewsno());
                intent.putExtra("classno", classno);
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
                    mIndex = 1;
                    list.clear();
                    ifc.queryHkstocks(classno,ZIXUN_NUM,String.valueOf(mIndex),classno,ListPager.this);
                } else if (refreshView.isShownFooter()) {
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                    mIndex ++;
                    ifc.queryHkstocks(classno,ZIXUN_NUM,String.valueOf(mIndex),classno,ListPager.this);
                }
            }
        });
    }

    @Override
    public void callResult(ResultInfo info) {
        dialog.dismiss();
        rl_Pager.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        if(info.getCode().equals("200")){
            Object obj = info.getData();
            if(obj!=null&&obj instanceof List){
                if(mIndex==1)
                    list = (ArrayList<InformationEntity>)obj;
                else
                    list.addAll((ArrayList<InformationEntity>)obj);
                adapter.setList(list);
            }
        }else{
            llChongXinJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
            llChongXinJiaZai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {       //点击图片重新请求数据
                    llChongXinJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                    rl_Pager.setVisibility(View.VISIBLE);//显示背景
                    rl_Pager.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色
                    isFirst = true;
                    mIndex = 1;
                    list.clear();
                    ifc.queryHkstocks(classno,ZIXUN_NUM,String.valueOf(mIndex),classno,ListPager.this);
                }
            });
        }
        mListView.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.financial_pager;
    }


    @Override
    public void destroy() {
        dialog.dismiss();
        net.cancelSingleRequest(classno);
    }
}
