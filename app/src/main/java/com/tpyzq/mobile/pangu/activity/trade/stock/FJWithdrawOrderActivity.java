package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FJWithdrawOrderAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 分级基金撤单
 */

public class FJWithdrawOrderActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private List<StructuredFundEntity> mList;
    private FJWithdrawOrderAdapter mFjwithdrawOrderAdapter;


    @Override
    public void initView() {
        mList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setOnItemClickListener(this);
        mFjwithdrawOrderAdapter = new FJWithdrawOrderAdapter(this, mList);
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fj_withdraw_order;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
