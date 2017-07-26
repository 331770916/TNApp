package com.tpyzq.mobile.pangu.activity.home.information;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.ZxTabAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import java.util.ArrayList;


/**
 * 新闻详情  点击 相关标签  跳转的  新闻标签页面
 */
public class IndustryRelevanceActivity extends BaseActivity implements View.OnClickListener ,InterfaceCollection.InterfaceCallback{

    private static final String TAG = "IndustryRelevance";
    private PullToRefreshListView mListView = null;
    private TextView tvActivityName;
    private ZxTabAdapter adapter;
    private String title;
    private ArrayList<InformationEntity> list;
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
        list = new ArrayList<InformationEntity>();
        adapter = new ZxTabAdapter(this);
//        getData(mIndex);
        mListView.setAdapter(adapter);

        //item  点击跳转详情页
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(IndustryRelevanceActivity.this, NewsDetailActivity.class);
                InformationEntity informationBean = list.get(position-1);
                String requestId = informationBean.getNewsno();
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
                    list.clear();
                    mIndex = 0;
//                    getData(mIndex);
                    //模拟加载数据线程休息3秒
                }else if (refreshView.isShownFooter()) {
                    //判断尾布局是否可见，如果可见执行上拉加载更多
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                    mIndex += 30;
//                    getData(mIndex);
                }
            }
        });
    }

    @Override
    public void callResult(ResultInfo info) {
        if(info.getCode().equals("0")){
            list = (ArrayList<InformationEntity>)info.getData();
            adapter.setList(list);
            //完成对下拉刷新ListView的更新操作
            adapter.notifyDataSetChanged();
            //将下拉视图收起
            mListView.onRefreshComplete();
        }
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

}
