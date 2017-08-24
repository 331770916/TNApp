package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.List;

/**
 * Created by ltyhome on 26/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:  网络投票界面
 */

public class VoteActivity extends BaseActivity  implements InterfaceCollection.InterfaceCallback,View.OnClickListener{
    private final String TAG = "VoteActivity";
    private List<NetworkVotingEntity> myList;
    private PullToRefreshListView listView;
    private String mSession,position = "";
    private VoteAdapter mAdapter;
    private ImageView iv_isEmpty;
    private boolean mIsClean;
    private Dialog mDialog;
    private int refresh = 30;
    private boolean isRequest = true; //请求标志位

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        listView = (PullToRefreshListView) findViewById(R.id.voteListview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);
        findViewById(R.id.detail_back).setOnClickListener(this);
        mDialog = LoadingDialog.initDialog(this, "正在查询...");
        mAdapter = new VoteAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(iv_isEmpty);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>(){
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isRequest) {
                    return;
                }
                isRequest = true;
                if (listView.isShownHeader()) {
                    listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    listView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refresh("",String.valueOf(refresh),false);
                }else if (listView.isShownFooter()){
                    listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    listView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refresh(position,"30",true);
                }
            }
        });
        mDialog.show();
        refresh("","30",false);
    }

    public void refresh(String page,String num,boolean isClean){
        mInterface.queryNetworkVoting(mSession,"",page,num,TAG,this);
        mIsClean = isClean;
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void callResult(ResultInfo info) {
        if (mDialog != null)
            mDialog.dismiss();
        String code = info.getCode();
        if("0".equals(code)){
            if (!mIsClean&&myList!=null)
                myList.clear();
            Object object = info.getData();
            if(object instanceof List){
                myList = (List<NetworkVotingEntity>)object;
                if(myList.size()>0){
                    if(myList.size()<30) {
                        listView.onRefreshComplete();
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        listView.onRefreshComplete();
                        listView.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    position = myList.get(myList.size()-1).getPosition_str();
                    mAdapter.setData(myList);
                }else{
                    if (mIsClean) {//下拉刷新或者初始化时提示
//                        showToast(" 暂无数据");
                        CentreToast.showText(VoteActivity.this,"暂无数据");
                    }
                    listView.onRefreshComplete();
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        }else if("-6".equals(code)){
            skip.startLogin(this);
            listView.onRefreshComplete();
        }else{//-1,-2,-3情况下显示定义好信息
            CentreToast.showText(VoteActivity.this,info.getMsg());
            listView.onRefreshComplete();
        }
        isRequest = false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote;
    }

    @Override
    public void destroy() {
        net.cancelSingleRequest(TAG);
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog = null;
        }
        mAdapter = null;
        listView = null;
        iv_isEmpty = null;
    }
}
