package com.tpyzq.mobile.pangu.activity.trade.currency_fund;

import android.content.Intent;
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
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.ivCurrencyFund_back:      //销毁当前界面
                this.finish();
                break;
            case R.id.rlFundSubscribe:          //跳转货币基金申购界面
                intent.setClass(this, CurrencyFundSubscribeActivity.class);
                startActivity(intent);
                break;
            case R.id.rlFundRedeem:             //跳转货币基金赎回界面
                intent.setClass(this, CurrencyFundRedeemActivity.class);
                startActivity(intent);
                break;
            case R.id.rlFundQuery:              //跳转货币基金委托查询界面
                intent.setClass(this, CurrencyFundQueryActivity.class);
                startActivity(intent);
                break;
            case R.id.rlFundRevoke:             //跳转货币系基金撤单界面
                intent.setClass(this, CurrencyFundRevokeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
