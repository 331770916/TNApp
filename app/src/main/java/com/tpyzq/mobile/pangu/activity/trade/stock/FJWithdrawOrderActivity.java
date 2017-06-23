package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * 分级基金撤单
 */

public class FJWithdrawOrderActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;


    @Override
    public void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fj_withdraw_order;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
