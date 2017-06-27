package com.tpyzq.mobile.pangu.activity.trade.stock;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * 投票查询
 */

public class VoteQueryActivity extends BaseActivity {
    private VoteQueryAdapter voteQueryAdapter;

    @Override
    public void initView() {
        voteQueryAdapter = new VoteQueryAdapter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote_query;
    }
}
