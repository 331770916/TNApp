package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.Helper;

/**
 * Created by wangqi on 2017/7/3.
 * ETF 导航栏
 */

public class ETFNavigationBarActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void initView() {
        findViewById(R.id.argumentBack).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.Layout_1).setOnClickListener(this);
        findViewById(R.id.Layout_2).setOnClickListener(this);
        findViewById(R.id.Layout_3).setOnClickListener(this);
        findViewById(R.id.Layout_4).setOnClickListener(this);
        findViewById(R.id.Layout_5).setOnClickListener(this);
        findViewById(R.id.Layout_6).setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_etf_navigationbar;
    }

    @Override
    public void onClick(View v) {
        Intent intent =new Intent();
        switch (v.getId()) {
            case R.id.Layout_1://申购
                intent.setClass(this,ETFApplyforOrRedeemActivity.class);
                intent.putExtra("type","Applyfor");
                break;
            case R.id.Layout_2://赎回
                intent.setClass(this,ETFApplyforOrRedeemActivity.class);
                intent.putExtra("type","Redeem");
                break;
            case R.id.Layout_3://申赎撤单
                intent.setClass(this,ETFRevokeActivity.class);
                break;
            case R.id.Layout_4://申赎成交
                intent.setClass(this,ETFTrasactionQueryActivity.class);
                break;
            case R.id.Layout_5://申赎委托
                intent.setClass(this,ETFHistoryInquireActivity.class);
                break;
            case R.id.Layout_6://成分股
                intent.setClass(this,ETFStockQueryActivity.class);
                break;
        }
        startActivity(intent);
    }
}
