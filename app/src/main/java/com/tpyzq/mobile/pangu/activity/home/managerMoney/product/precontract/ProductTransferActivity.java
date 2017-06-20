package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.BanksTransferAccountsActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;


/**
 * Created by wangqi on 2017/4/10.
 * 产品预约转入资金
 */

public class ProductTransferActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTotalAssets;

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.affirm_btn).setOnClickListener(this);

        mTotalAssets = (TextView) findViewById(R.id.totalAssets);//您的总资产
        initData();
    }

    private void initData() {
        String total = getIntent().getStringExtra("total");
        mTotalAssets.setText(total);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_producttransfer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.affirm_btn:
                Intent intent = new Intent();
                intent.putExtra("tag", "100");
                intent.setClass(this, BanksTransferAccountsActivity.class);
                BRutil.menuSelect("N015");
                startActivity(intent);
                finish();
                break;
        }
    }
}
