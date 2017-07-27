package com.tpyzq.mobile.pangu.activity.home.information.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.SopCastAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import java.util.ArrayList;
import java.util.List;

import static com.tpyzq.mobile.pangu.util.ConstantUtil.ZIXUN_NUM;

/**
 * 作者：刘泽鹏 on 2016/9/17 15:55
 * 直播模块  首页
 */
public class SopCastPager extends BasePager implements InterfaceCollection.InterfaceCallback,View.OnClickListener{
    private PullToRefreshListView mListView;
    private SopCastAdapter adapter;
    private ArrayList<InformationEntity> list;
    private int count = 1;
    private ProgressBar pb_SopCastPager;            //菊花
    private RelativeLayout rlSopCast;               //包裹整个布局的  RelativeLayout
    private LinearLayout llSopCastJiaZai;          //内容为空的   图片
    private boolean isFirst = true;
    private String classno;

    public SopCastPager(Context context,String params) {
        super(context,params);
    }

    @Override
    public int getLayoutId() {
        return R.layout.sopcast_pager;
    }

    @Override
    public void setView(String params) {
        this.classno = params;
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.lvSopCast);
        mListView.setVisibility(View.GONE);         //初始化隐藏 listView
        pb_SopCastPager = (ProgressBar) rootView.findViewById(R.id.pb_SopCastPager);    //显示菊花
        rlSopCast = (RelativeLayout) rootView.findViewById(R.id.rlSopCast);              //初始化  背景为灰色
        llSopCastJiaZai = (LinearLayout) rootView.findViewById(R.id.llSopCastJiaZai);
        rootView.findViewById(R.id.goTop).setOnClickListener(this);
        list = new ArrayList();
        adapter = new SopCastAdapter(mContext);         //实例化适配器
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mContext, NewsDetailActivity.class);
                InformationEntity informationBean = list.get(position - 1);
                String requestId = informationBean.getNewsno();
                intent.putExtra("requestId", requestId);
                mContext.startActivity(intent);
            }
        });
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isShownHeader()) {
                    //判断头布局是否可见，如果可见执行下拉刷新
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    count = 1;
                    list.clear();
                    adapter.setCurrentDay(null);
                    ifc.queryStreaming("30","2",ZIXUN_NUM,String.valueOf(count),classno,SopCastPager.this);
                } else if (refreshView.isShownFooter()) {
                    //判断尾布局是否可见，如果可见执行上拉加载更多
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                    count ++ ;
                    ifc.queryStreaming("3","2",ZIXUN_NUM,String.valueOf(count),classno,SopCastPager.this);
                }
            }
        });
    }

    @Override
    public void initData() {
        if(isFirst) {
            ifc.queryStreaming("3", "2", ZIXUN_NUM, String.valueOf(count), classno, this);
            isFirst = false;
        }
    }

    @Override
    public void onClick(View v) {
        mListView.getRefreshableView().smoothScrollToPosition(0);
        adapter.notifyDataSetInvalidated();
    }

    @Override
    public void callResult(ResultInfo info) {
        pb_SopCastPager.setVisibility(View.GONE);       //隐藏 菊花
        rlSopCast.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));   //背景设置为白色
        if(info.getCode().equals("200")){
            Object obj = info.getData();
            if(obj!=null&&obj instanceof List){
                mListView.setVisibility(View.VISIBLE);          //显示 listView
                if(count==1)
                    list = (ArrayList<InformationEntity>)obj;
                else
                    list.addAll((ArrayList<InformationEntity>)obj);
                adapter.setList(list);
                mListView.onRefreshComplete();
            }
        }else{
            mListView.setVisibility(View.GONE);     //隐藏listView
            llSopCastJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
            llSopCastJiaZai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {       //点击图片重新请求数据
                    llSopCastJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                    pb_SopCastPager.setVisibility(View.VISIBLE);      //显示菊花
                    rlSopCast.setVisibility(View.VISIBLE);//显示背景
                    rlSopCast.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色
                    count = 1;
                    list.clear();
                    ifc.queryStreaming("3","2",ZIXUN_NUM,String.valueOf(count),classno,SopCastPager.this);
                }
            });
        }
    }

    @Override
    public void destroy() {
        net.cancelSingleRequest(classno);
    }
}
