package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.StockPageListAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;

/**
 * Created by ltyhome on 27/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: 网络投票
 */

public class NetworkVotingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lv_stock_list;
    private ImageView iv_back;

    @Override
    public void initView() {
        lv_stock_list = (ListView) findViewById(R.id.lv_voting_list);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_stock_list.setAdapter(new StockPageListAdapter(this, DataUtils.stock_votelist_name,DataUtils.stock_votelist_icon));
        lv_stock_list.setOnItemClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vote_list;
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(this, VoteActivity.class);
                break;
            case 1:
                intent.setClass(this, VoteQueryActivity.class);
                break;
        }
        startActivity(intent);
    }
}
