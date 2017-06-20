package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * OTC 账户导航页面
 * 刘泽鹏
 */
public class OTC_AccountActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void initView() {
        this.findViewById(R.id.ivOTC_Account_back).setOnClickListener(this);        //返回按钮
        this.findViewById(R.id.rlOTC_OpenAccount).setOnClickListener(this);         //开户
        this.findViewById(R.id.rlOTC_AccountQuery).setOnClickListener(this);        //账户查询
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__account;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.ivOTC_Account_back:       //销毁当前界面
                finish();
                break;
            case R.id.rlOTC_OpenAccount:        //跳转开户界面
                intent.setClass(this, OTC_OpenAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.rlOTC_AccountQuery:       //跳转账户查询界面
                intent.setClass(this, OTC_AccountQueryActivity.class);
                startActivity(intent);
                break;
        }
    }
}
