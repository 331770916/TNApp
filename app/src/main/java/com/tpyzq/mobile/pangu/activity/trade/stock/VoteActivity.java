package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.VoteAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * 网络投票界面
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
