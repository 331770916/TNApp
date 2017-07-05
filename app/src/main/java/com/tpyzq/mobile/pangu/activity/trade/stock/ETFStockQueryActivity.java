package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

/**
 * Created by ltyhome on 05/07/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: 成分股查询
 */

public class ETFStockQueryActivity extends BaseActivity implements View.OnClickListener ,InterfaceCollection.InterfaceCallback, AdapterView.OnItemClickListener {
    private PullToRefreshListView etfList;
    private Dialog mDialog;


    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        etfList = (PullToRefreshListView) findViewById(R.id.etfList);
        etfList.setOnItemClickListener(this);
        requestData();
    }

    private void requestData() {
        mDialog = LoadingDialog.initDialog(this, "正在查询...");
        if (!mDialog.isShowing())
            mDialog.show();


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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
