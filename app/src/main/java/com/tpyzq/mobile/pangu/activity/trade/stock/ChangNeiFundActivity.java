package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundSubscriptionActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 *刘泽鹏
 * 场内基金  导航页面
 */
public class ChangNeiFundActivity extends BaseActivity implements View.OnClickListener{



    @Override
    public void initView() {
        this.findViewById(R.id.ivCNFund_back).setOnClickListener(this);         //返回按钮
        this.findViewById(R.id.rlFundRenGou).setOnClickListener(this);         //基金认购
        this.findViewById(R.id.rlFundShenGou).setOnClickListener(this);         //基金申购
        this.findViewById(R.id.rlFundShuHui).setOnClickListener(this);         //基金赎回

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chang_nei_fund;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.ivCNFund_back:            //销毁当前界面
                this.finish();
                break;
            case R.id.rlFundRenGou:             //跳转基金认购界面
                intent.setClass(this,FundSubscriptionActivity.class);
                startActivity(intent);
                break;
            case R.id.rlFundShenGou:            //跳转基金申购界面
                intent.setClass(this, CNFundSubscribeActivity.class);
                startActivity(intent);
                break;
            case R.id.rlFundShuHui:             //跳转基金赎回界面
                intent.setClass(this, CNFundRedeemActivity.class);
                startActivity(intent);
                break;
        }
    }
}
