package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * Created by wangqi on 2016/8/24.
 * 查询主页
 */
public class ReferActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void initView() {
        findViewById(R.id.wt).setOnClickListener(this);
        findViewById(R.id.cj).setOnClickListener(this);
        findViewById(R.id.zj).setOnClickListener(this);
        findViewById(R.id.jgd).setOnClickListener(this);
        findViewById(R.id.ivCNFund_back).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_demand;
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.wt:
                intent.setClass(this, EntrustActivity.class);
                startActivity(intent);
                break;
            case R.id.cj:
                intent.setClass(this,ClinchDealActivity.class);
                startActivity(intent);
                break;
            case R.id.zj:
                intent.setClass(this,CapitalQueryActivity.class);
                startActivity(intent);
                break;
            case R.id.jgd:
                intent.setClass(this,DeliveryOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ivCNFund_back:
                finish();
                break;
        }
}
}
