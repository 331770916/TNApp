package com.tpyzq.mobile.pangu.activity.trade.stock;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteQueryShareholdersAccountAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * 投票查询选择股东账户
 */

public class VoteQueryShareholdersAccountActivity extends BaseActivity {
    private VoteQueryShareholdersAccountAdapter voteQueryShareholdersAccountAdapter;

    @Override
    public void initView() {
        voteQueryShareholdersAccountAdapter = new VoteQueryShareholdersAccountAdapter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote_query_shareholders_accout;
    }
}
