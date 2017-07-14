package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteDetailAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 26/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:  网络投票详情界面
 */

public class VoteDetailActivity extends BaseActivity  implements InterfaceCollection.InterfaceCallback,View.OnClickListener{
    private VoteDetailAdapter accumulateAdapter,unAccumulateAdapter;
    //accumulate 1累积投票议案  unAccumulate 0非累积投票议案
    private List<NetworkVotingEntity> accumulate,unAccumulate,submitList;
    private String mSession,meeting_seq="",company_code="",stock_account="",exchange_type="";
    private AutoListview accumulateList,unAccumulateList;
    public static final String TAG = "VoteDetailActivity";
    public LinearLayout goneUnacc;
    private TextView goneAcc;
    private Dialog mDialog,mistake;
    private ImageView back;
    private Button submit;
    private StructuredFundDialog mStructuredFundDialog;


    @Override
    public void initView() {
        if(getIntent()!=null) {
            meeting_seq = getIntent().getStringExtra("meeting_seq");
            company_code = getIntent().getStringExtra("company_code");
            stock_account = getIntent().getStringExtra("stock_account");
            exchange_type = getIntent().getStringExtra("exchange_type");
        }
        accumulate = new ArrayList<>();
        unAccumulate = new ArrayList<>();
        submitList = new ArrayList<>();
        mSession = SpUtils.getString(this, "mSession", "");
        back = (ImageView)findViewById(R.id.detail_back);
        back.setOnClickListener(this);
        ((TextView)findViewById(R.id.voteTitleName)).setText(company_code);
        ((TextView)findViewById(R.id.voteTitleCode)).setText(stock_account);
        accumulateList  = (AutoListview) findViewById(R.id.accumulateList);
        accumulateList.setDivider(null);
        goneAcc = (TextView)findViewById(R.id.vote_gone_acc);
        goneUnacc = (LinearLayout)findViewById(R.id.vote_gone_unacc);
        accumulateAdapter = new VoteDetailAdapter(this);
        accumulateList.setAdapter(accumulateAdapter);
        unAccumulateList = (AutoListview) findViewById(R.id.unAccumulateList);
        unAccumulateList.setDivider(null);
        submit = (Button)findViewById(R.id.voteSubmit);
        submit.setOnClickListener(this);
        unAccumulateAdapter = new VoteDetailAdapter(this);
        unAccumulateList.setAdapter(unAccumulateAdapter);
        mDialog = LoadingDialog.initDialog(this, "正在查询...");
        mDialog.show();
        mInterface.queryProposal(mSession,meeting_seq,TAG+"query",this);
    }

    @Override
    public void callResult(ResultInfo info) {
        if (mDialog != null)
            mDialog.dismiss();
        String code = info.getCode();
        if("0".equals(code)){
            Object object = info.getData();
            if(info.getTag().equals(TAG+"query")){
                if(object instanceof Map){
                    Map<String,List<NetworkVotingEntity>> map = (Map<String,List<NetworkVotingEntity>>)object;
                    if(map.size()>0){
                        unAccumulate = map.get("0");
                        if(unAccumulate.size()==0)
                            goneUnacc.setVisibility(View.VISIBLE);
                        else {
                            goneUnacc.setVisibility(View.GONE);
                            unAccumulateAdapter.setData(unAccumulate);
                        }
                        accumulate = map.get("1");
                        if(accumulate.size()==0){
                            goneAcc.setText("未查询到累积投票议案");
                        }else{
                            goneAcc.setText("注意：累积投票制选举，对于每个选举议案组，股东可投票总数＝股东持股数×当选人数");
                            accumulateAdapter.setData(accumulate);
                        }
                    }
                }
            }else if(info.getTag().equals(TAG+"submit")){
                showToast(info.getMsg());
            }
        }else if("-6".equals(code)){
            skip.startLogin(this);
        }else{//-1,-2,-3情况下显示定义好信息
            showToast(info.getMsg());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_back:
                finish();
                break;
            case R.id.voteSubmit:
                submitList.clear();
                if(accumulate.size()>0){
                    NetworkVotingEntity entity = null;
                    int count = 0;boolean isFirst = true;
                    List<NetworkVotingEntity> temp = new ArrayList<>();
                    for (NetworkVotingEntity et:accumulate) {
                        List<NetworkVotingEntity> subList = et.getList();
                        if(subList.size()>0){
                            for (NetworkVotingEntity ve:subList) {
                                if(!TextUtils.isEmpty(ve.getEntrust_no())){
                                    company_code = et.getStatus();
                                    temp.add(ve);
                                    submitList.add(ve);
                                }
                            }
                            if(temp.size()>0) {
                                count++;
                                temp.clear();
                            }else if(isFirst){
                                entity = et;
                                isFirst = false;
                            }
                        }
                    }
                    if (count!=accumulate.size()) {
                        String msg = "议案组：\"" + entity.getVote_info() + "(当选人数：" + entity.getList().size() + ")\"未表决，请表决后再提交!";
                        mistake = MistakeDialog.showDialog("提示", msg, false, this, null);
                    } else {
                        if (ConstantUtil.list_item_flag) {
                            ConstantUtil.list_item_flag = false;
                            mStructuredFundDialog = new StructuredFundDialog(this);
                            mStructuredFundDialog.setData(TAG, new StructuredFundDialog.Expression() {
                                @Override
                                public void State() {
                                    if (unAccumulate.size() > 0)
                                        submitList.addAll(unAccumulate);
                                    mDialog.show();
                                    mInterface.submitVoting(mSession, company_code, exchange_type, meeting_seq, submitList, TAG + "submit", VoteDetailActivity.this);
                                }
                            }, null, String.valueOf(accumulate.size() + unAccumulate.size()), stock_account);
                            mStructuredFundDialog.show();

                        }
                    }
                } else {
                    if (ConstantUtil.list_item_flag) {
                        ConstantUtil.list_item_flag = false;
                        mStructuredFundDialog = new StructuredFundDialog(this);
                        mStructuredFundDialog.setData(TAG, new StructuredFundDialog.Expression() {
                            @Override
                            public void State() {
                                mDialog.show();
                                mInterface.submitVoting(mSession, company_code, exchange_type, meeting_seq, unAccumulate, TAG + "submit", VoteDetailActivity.this);
                            }
                        }, null, String.valueOf(unAccumulate.size()), stock_account);
                        mStructuredFundDialog.show();
                    }
                }
                break;
        }
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
        if(mistake!=null){
            mistake.dismiss();
            mistake = null;
        }
        accumulateAdapter = null;
        unAccumulateAdapter = null;
        accumulateList = null;
        unAccumulateList = null;
        submitList = null;
    }
}
