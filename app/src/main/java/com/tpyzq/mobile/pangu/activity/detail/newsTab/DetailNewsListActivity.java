package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.app.Dialog;
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
import com.tpyzq.mobile.pangu.adapter.detail.NewsAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialogTwo;
import java.util.ArrayList;
import java.util.List;
/**
 * 详情页  新闻 tab  点击加载更多  跳转的页面
 */
public class DetailNewsListActivity extends BaseActivity implements View.OnClickListener ,InterfaceCollection.InterfaceCallback{

    private static String TAG ="DetailNewListActivity";
    private PullToRefreshListView mListView;
    private RelativeLayout rlDetailNew;
    private LinearLayout llNewJiaZai;
    private Dialog dialog;
    private NewsAdapter adapter;
    private int page;
    private ArrayList<InformationEntity> list;
    private String code;

    @Override
    public void initView() {
        code = getIntent().getStringExtra("code");
        this.findViewById(R.id.ivDetailNew_back).setOnClickListener(this);              //返回按钮
        mListView = (PullToRefreshListView) this.findViewById(R.id.lvDetaiNewlList);   //listView
        llNewJiaZai = (LinearLayout) this.findViewById(R.id.llNewChongXinJiaZai);     //重新加载
        rlDetailNew = (RelativeLayout) this.findViewById(R.id.rlDetailNew);            //背景
        initData();
    }

    private void initData() {
        dialog = LoadingDialogTwo.initDialog(DetailNewsListActivity.this);  //菊花
        dialog.show();
        mListView.setVisibility(View.GONE);
        list = new ArrayList<>();
        adapter = new NewsAdapter(this);
        mInterface.queryStockNews(code,"30","1",TAG,this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InformationEntity bean = list.get(position-1);
                String newId = bean.getNewsno();
                Intent intent = new Intent(DetailNewsListActivity.this, NewsDetailActivity.class);
                intent.putExtra("requestId",newId);
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
                    page = 1;
                    list.clear();
                    mInterface.queryStockNews(code,"30","1",TAG,DetailNewsListActivity.this);
                }else if (refreshView.isShownFooter()) {
                    //判断尾布局是否可见，如果可见执行上拉加载更多
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                    page ++;
                    mInterface.queryStockNews(code,"30",page+"",TAG,DetailNewsListActivity.this);
                }
            }
        });
    }


    @Override
    public void callResult(ResultInfo info) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();      //隐藏菊花
        if(info.getCode().equals("200")){
            Object object = info.getData();
            if(object!=null){
                llNewJiaZai.setVisibility(View.GONE);       //隐藏重新加载
                rlDetailNew.setBackgroundColor(ContextCompat.getColor(DetailNewsListActivity.this,R.color.white));      //背景设置为白色
                mListView.setVisibility(View.VISIBLE);      //请求到数据 展示 listView
                adapter.setList((List<InformationEntity>)object);
            }
        }else {
            rlDetailNew.setBackgroundColor(ContextCompat.getColor(DetailNewsListActivity.this,R.color.white));    //背景 为 白色
            mListView.setVisibility(View.GONE);         //隐藏listView
            llNewJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
            llNewJiaZai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llNewJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                    dialog.show();      //显示菊花
                    rlDetailNew.setVisibility(View.VISIBLE);//显示背景
                    rlDetailNew.setBackgroundColor(ContextCompat.getColor(DetailNewsListActivity.this,R.color.dividerColor)); //设置为灰色
                    mInterface.queryStockNews(code,"30","1",TAG,DetailNewsListActivity.this);
                }
            });
        }
        mListView.onRefreshComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_list;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivDetailNew_back){
            finish();
        }
    }
}
