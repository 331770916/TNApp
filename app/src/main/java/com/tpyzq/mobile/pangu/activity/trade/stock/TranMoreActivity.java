package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.StockPageListAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;


/**
 * 股票更多
 */
public class TranMoreActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView lv_stock_list;
    private ImageView iv_back;
    @Override
    public void initView() {
        lv_stock_list = (ListView) findViewById(R.id.lv_stock_list);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        initData();
    }

    private void initData() {
        lv_stock_list.setAdapter(new StockPageListAdapter(this, DataUtils.stock_morelist_name,DataUtils.stock_morelist_icon));
        lv_stock_list.setOnItemClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tran_more;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.putExtra("pageindex", TransactionLoginActivity.PAGE_INDEX_ChangNeiFundActivity);
                intent.setClass(this,ChangNeiFundActivity.class);
                break;
            case 1:
                intent.putExtra("pageindex",TransactionLoginActivity.PAGE_INDEX_NIHUIGOU);
                intent.setClass(this,ReverseRepoGuideActivity.class);
                break;
            case 2:
                intent.putExtra("pageindex",TransactionLoginActivity.PAGE_INDEX_StructuredFundActivity);
                intent.setClass(this,StructuredFundActivity.class);
                break;
            case 3:
                intent.putExtra("pageindex",TransactionLoginActivity.PAGE_INDEX_ETFNavigationBarActivity);
                intent.setClass(this,ETFNavigationBarActivity.class);
                break;
            case 4:
                intent.putExtra("pageindex",TransactionLoginActivity.PAGE_INDEX_NetworkVotingActivity);
                intent.setClass(this,NetworkVotingActivity.class);
                break;
//            case 5:
//
//                break;

        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
