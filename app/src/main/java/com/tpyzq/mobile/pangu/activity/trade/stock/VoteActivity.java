package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * 股票   投票界面  点击跳入NetworkVoteActivity
 */

public class VoteActivity extends BaseActivity {
    private VoteAdapter voteAdapter;

    @Override
    public void initView() {
        ListView listView = (ListView) findViewById(R.id.mListView);
        voteAdapter = new VoteAdapter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote;
    }
}
