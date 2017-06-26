package com.tpyzq.mobile.pangu.activity.trade.view;
import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.adapter.trade.FJEntrustedDealListViewAdapter;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;

import java.util.List;

/**
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: entrust deal query 委托0 成交查询1
 */
public class FJEntrustDealQueryPager extends BasePager implements InterfaceCollection.InterfaceCallback,FJEntrustedDealListViewAdapter.ScallCallback{
    private FJEntrustedDealListViewAdapter mAdapter;
    private List<StructuredFundEntity> myList;
    private PullToRefreshListView listView;
    private RelativeLayout kong_null;
    private ImageView iv_isEmpty;
    private boolean isScallBottom;
    private String TAG;

    public FJEntrustDealQueryPager(Context context,String params) {
        super(context,params);
        TAG = params;
    }

    @Override
    public void setView(String params) {
        listView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        iv_isEmpty = (ImageView) rootView.findViewById(R.id.iv_isEmpty);
        kong_null = (RelativeLayout) rootView.findViewById(R.id.EAMP_Kong_Null);
    }


    @Override
    public void callResult(ResultInfo info) {
        String code = info.getCode();
        if(code.equals("0")){
            myList = (List<StructuredFundEntity>)info.getData();
            mAdapter.setData(myList);
            listView.onRefreshComplete();
        }else{
            helper.showToast(mContext,info.getMsg());
            kong_null.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.page_fj_entrustquery_today;
    }

    @Override
    public void callScall() {
        if(isScallBottom){
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.getRefreshableView().setSelection(listView.getBottom());
                }
            });
        }
    }

    @Override
    public void initData() {
        if(mAdapter==null){
            mAdapter = new FJEntrustedDealListViewAdapter(mContext,getType());
            mAdapter.setCallback(this);
            listView.setAdapter(mAdapter);
            listView.setEmptyView(iv_isEmpty);
            ifc.setInterfaceCallback(this);
            ifc.getData(20);
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>(){
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    if (listView.isShownHeader()) {
                        listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                        listView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                        listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                        SystemClock.sleep(1500);
                        ifc.getData(20);
                    }else if (listView.isShownFooter()){
                        listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                        listView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                        listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                        SystemClock.sleep(1500);
                        ifc.getData(20);
                    }
                }
            });
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState){
                        case SCROLL_STATE_IDLE:
                            if(view.getLastVisiblePosition()==view.getCount()-1){
                                View bottom = view.getChildAt(view.getLastVisiblePosition()-view.getFirstVisiblePosition());
                                if(view.getHeight()>=bottom.getBottom())
                                    isScallBottom = true;
                                else
                                    isScallBottom = false;
                            }else
                                isScallBottom = false;
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                }
            });
        }
    }

    @Override
    public void destroy() {

    }
}
