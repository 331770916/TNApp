package com.tpyzq.mobile.pangu.activity.home.information.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.adapter.home.NewHomeInformationAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.loopswitch.AutoSwitchAdapter;
import com.tpyzq.mobile.pangu.view.loopswitch.AutoSwitchView;
import java.util.ArrayList;
import java.util.List;

import static com.tpyzq.mobile.pangu.util.ConstantUtil.ZIXUN_NUM;

/**
 * 要闻模块 首页
 */
public class HotPintsPager extends BasePager implements View.OnClickListener,InterfaceCollection.InterfaceCallback {
    private PullToRefreshListView refreshListView;
    private NewHomeInformationAdapter mListViewAdapter;             //listView 适配器
    private ArrayList<InformationEntity> leve4,leve1;                    //存储  重大事件数据的  集合
    private RelativeLayout rlHot;
    private LinearLayout llHotJiaZai;
    private AutoSwitchView loopView;
    private AutoSwitchAdapter adapter;
    private boolean isFirst = true;
    public int page =1;

    public HotPintsPager(Context context,String params) {
        super(context,params);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hot_pints_pager;
    }

    @Override
    public void setView(final String params) {
        leve4 = new ArrayList<>();
        leve1 = new ArrayList<>();
        refreshListView = (PullToRefreshListView)rootView.findViewById(R.id.listview);
        rlHot = (RelativeLayout) rootView.findViewById(R.id.rlHot);     //包裹所有布局的 RelativeLayout  默认为灰色
        llHotJiaZai = (LinearLayout) rootView.findViewById(R.id.llHotJiaZai);
        loopView = new AutoSwitchView(mContext);
        loopView.setLayoutParams(new AbsListView.LayoutParams(-1,mContext.getResources().getDimensionPixelSize(R.dimen.size250)));
        refreshListView.getRefreshableView().addHeaderView(loopView);
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isShownHeader()) {
                    refreshListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    refreshListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    refreshListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    page = 1;
                    leve1.clear();
                    ifc.queryImportant("3","1","4","GetImportant4",HotPintsPager.this);
                    ifc.queryImportant(ZIXUN_NUM,page+"","3","GetImportant1",HotPintsPager.this);
                }else if(refreshView.isShownFooter()){
                    refreshListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    refreshListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    refreshListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    page ++;
                    ifc.queryImportant("3","1","4","GetImportant4",HotPintsPager.this);
                    ifc.queryImportant(ZIXUN_NUM,page+"","3","GetImportant1",HotPintsPager.this);
                }
            }
        });
        mListViewAdapter = new NewHomeInformationAdapter((Activity) mContext);            //实例化 ListView 适配器
        refreshListView.setAdapter(mListViewAdapter);                                  //适配
        // 给热点公告解析列表添加   item 的点击事件
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                if (!TextUtils.isEmpty(leve1.get(position).getNewsno()))
                    intent.putExtra("requestId", leve1.get(position).getNewsno());
                mContext.startActivity(intent);
            }
        });
        adapter = new AutoSwitchAdapter(mContext);
        loopView.setAdapter(adapter);
    }


    public void initData() {
       if(isFirst){
           ifc.queryImportant("3","1","4","GetImportant4",this);
           ifc.queryImportant(ZIXUN_NUM,page+"","3","GetImportant1",this);
           isFirst = false;
       }
    }


    @Override
    public void callResult(ResultInfo info) {
        rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        if(info.getCode().equals("200")){
            Object obj = info.getData();
            if(obj!=null&&obj instanceof List){
                switch (info.getTag()){
                    case "GetImportant4":
                        leve4 = (ArrayList<InformationEntity>)obj;
                        adapter.setDatas(Helper.covertLoopModel(leve4));
                        break;
                    case "GetImportant1":
                        if(page==1)
                            leve1 = (ArrayList<InformationEntity>)obj;
                        else
                            leve1.addAll((ArrayList<InformationEntity>)obj);
                        mListViewAdapter.setDatas(leve1);
                        break;
                }
            }
        }else{
            rlHot.setBackgroundColor(mContext.getResources().getColor(R.color.white));    //背景 为 白色
            refreshListView.setVisibility(View.GONE);     //隐藏listView
            llHotJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
            llHotJiaZai.setOnClickListener(this);
        }
        refreshListView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llHotJiaZai:          //点击  重新加载的时候
                isFirst = true;
                llHotJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                rlHot.setVisibility(View.VISIBLE);//显示背景
                rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色
                ifc.queryImportant(ZIXUN_NUM,page+"","3","GetImportant1",HotPintsPager.this);
                break;
        }
    }



    @Override
    public void destroy() {
        net.cancelSingleRequest("GetImportant4");
        net.cancelSingleRequest("GetImportant1");
    }
}
