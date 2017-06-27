package com.tpyzq.mobile.pangu.activity.trade.stock;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteAccumulateProposalAdapter;
import com.tpyzq.mobile.pangu.adapter.trade.VoteNonAccumulateProposalAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * 网络投票界面
 */

public class VoteNetworkActivity extends BaseActivity {
    private VoteAccumulateProposalAdapter voteAccumulateProposalAdapter;   //  累积投票议案
    private VoteNonAccumulateProposalAdapter voteNonAccumulateProposalAdapter;  //  非累积投票议案

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote_network;
    }
}
