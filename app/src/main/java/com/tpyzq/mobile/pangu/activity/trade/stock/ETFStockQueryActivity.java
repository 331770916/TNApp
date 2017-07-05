package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;

/**
 * Created by ltyhome on 05/07/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: 成分股查询
 */

public class ETFStockQueryActivity extends BaseActivity implements View.OnClickListener ,InterfaceCollection.InterfaceCallback{
    private PullToRefreshListView etfList;

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_constituent_query;
    }


    @Override
    public void callResult(ResultInfo info) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
