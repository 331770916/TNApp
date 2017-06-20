package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * Created by zhangwenbo on 2017/4/11.
 * 产品流程界面
 */

public class PrecontractFlowActivity extends BaseActivity {

    @Override
    public void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView text = (TextView) findViewById(R.id.toolbar_title);
        text.setText("产品预约流程");

        findViewById(R.id.userIdBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initData() {
        findViewById(R.id.flowTextWarn).setVisibility(View.GONE);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_precontract_flow;
    }
}
