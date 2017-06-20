package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * OTC 查询 导航界面
 * 刘泽鹏
 */
public class OTC_QueryActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public void initView() {
        this.findViewById(R.id.ivOTC_Query_back).setOnClickListener(this);          //返回按钮
        this.findViewById(R.id.rlOTC_EntrustQuery).setOnClickListener(this);        //委托查询的相对布局
        this.findViewById(R.id.rlOTC_BargainQuery).setOnClickListener(this);        //成交查询的相对布局
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__query;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.rlOTC_EntrustQuery:
                intent.setClass(this,OTC_EntrustQueryActivity.class);               //跳转委托查询界面
                startActivity(intent);
                break;
            case R.id.rlOTC_BargainQuery:                                          //跳转成交查询界面
                intent.setClass(this,OTC_BargainQueryActivity.class);
                startActivity(intent);
                break;
            case R.id.ivOTC_Query_back:                                             //销毁当前界面
                finish();
                break;
        }
    }
}
