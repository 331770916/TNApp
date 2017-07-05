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
        findViewById(R.id.argumentBack).setOnClickListener(this);
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
            case R.id.argumentBack:
                finish();
                break;
            case R.id.Layout_1:   //  申购
//                Helper.getInstance().showToast(this,getString(R.string.ETF_shengou));
                intent.setClass(this,ETFApplyforOrRedeemActivity.class);
                intent.putExtra("Applyfor","");
                break;
            case R.id.Layout_2:   //赎回
//                Helper.getInstance().showToast(this,getString(R.string.ETF_shuhui));
                intent.setClass(this,ETFApplyforOrRedeemActivity.class);
                intent.putExtra("Redeem","");
                break;
            case R.id.Layout_3:
                Helper.getInstance().showToast(this,getString(R.string.ETF_shenshu_Revoke));
                break;
            case R.id.Layout_4:
                Helper.getInstance().showToast(this,getString(R.string.ETFConstituentStock));
                break;
            case R.id.Layout_5:
                intent.setClass(this,ETFTrasactionQueryActivity.class);
                break;
            case R.id.Layout_6:
                intent.setClass(this,ETFHistoryInquireActivity.class);
//                Helper.getInstance().showToast(this,getString(R.string.ETF_shenshu_History));
                break;
        }
        startActivity(intent);

    }
}
