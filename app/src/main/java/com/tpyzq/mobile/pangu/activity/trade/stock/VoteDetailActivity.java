package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteAdapter;
import com.tpyzq.mobile.pangu.adapter.trade.VoteDetailAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltyhome on 26/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:  网络投票详情界面
 */

public class VoteDetailActivity extends BaseActivity  implements InterfaceCollection.InterfaceCallback,View.OnClickListener{
    private VoteDetailAdapter accumulateAdapter,unAccumulateAdapter;
    //accumulate 累积投票议案  unAccumulate 非累积投票议案
    private List<NetworkVotingEntity> accumulate,unAccumulate;
    private AutoListview accumulateList,unAccumulateList;
    private ImageView back;
    private final String TAG = "VoteDetailActivity";
    private String mSession;
    private Dialog mDialog;

    @Override
    public void initView() {
        accumulate = new ArrayList<>();
        unAccumulate = new ArrayList<>();
        mSession = SpUtils.getString(this, "mSession", "");
        back = (ImageView)findViewById(R.id.detail_back);
        back.setOnClickListener(this);

        accumulateList  = (AutoListview) findViewById(R.id.accumulateList);
        accumulateAdapter = new VoteDetailAdapter(this);
        accumulateList.setAdapter(accumulateAdapter);
        unAccumulateList = (AutoListview) findViewById(R.id.unAccumulateList);
        unAccumulateAdapter = new VoteDetailAdapter(this);
        unAccumulateList.setAdapter(unAccumulateAdapter);
        mDialog = LoadingDialog.initDialog(this, "正在查询...");
        mDialog.show();
        mInterface.queryProposal(mSession,"",TAG,this);
    }

    @Override
    public void callResult(ResultInfo info) {
        if (mDialog != null)
            mDialog.dismiss();
        String code = info.getCode();
        if("0".equals(code)){
            Object object = info.getData();
            if(object instanceof List){
                List<NetworkVotingEntity> myList = (List<NetworkVotingEntity>)object;
                if(myList.size()>0){
                    for (NetworkVotingEntity voteEntity: myList) {
                        String type = voteEntity.getVote_type();
                        if(TextUtils.isEmpty(type)){
                            if("1".equals(type))
                                accumulate.add(voteEntity);
                            else if("0".equals(type))
                                unAccumulate.add(voteEntity);
                        }
                    }
                    accumulateAdapter.setData(accumulate);
                    unAccumulateAdapter.setData(unAccumulate);
                }else
                    showToast(" 暂无数据");
            }
        }else if("-6".equals(code)){
            skip.startLogin(this);
        }else{//-1,-2,-3情况下显示定义好信息
            showToast(info.getMsg());
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote_network;
    }

    @Override
    public void destroy() {
        net.cancelSingleRequest(TAG);
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog = null;
        }
        accumulateAdapter = null;
        unAccumulateAdapter = null;
        accumulateList.removeAllViews();
        accumulateList = null;
        unAccumulateList.removeAllViews();
        unAccumulateList = null;
    }
}
