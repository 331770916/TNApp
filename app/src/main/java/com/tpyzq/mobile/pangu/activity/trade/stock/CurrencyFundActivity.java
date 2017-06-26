package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * 刘泽鹏
 * 货币基金导航页
 */

public class CurrencyFundActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void initView() {
        this.findViewById(R.id.ivCurrencyFund_back).setOnClickListener(this);   //返回按钮
        this.findViewById(R.id.rlFundSubscribe).setOnClickListener(this);       //基金申购
        this.findViewById(R.id.rlFundRedeem).setOnClickListener(this);          //基金赎回
        this.findViewById(R.id.rlFundQuery).setOnClickListener(this);           //委托查询
        this.findViewById(R.id.rlFundRevoke).setOnClickListener(this);          //基金撤单
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_fund;
    }

    @Override
    public void onClick(View v) {

    }
}
