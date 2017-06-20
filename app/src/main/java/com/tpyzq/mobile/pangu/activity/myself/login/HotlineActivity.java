package com.tpyzq.mobile.pangu.activity.myself.login;

import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.dialog.ServiceDialog;


/**
 * Created by wangqi on 2016/8/19.
 * 客服热线
 */
public class HotlineActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void initView() {
        findViewById(R.id.DianHua).setOnClickListener(this);
        findViewById(R.id.publish_back).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DianHua:
                ServiceDialog dialog = new ServiceDialog(this);
                dialog.show();
                break;
            case R.id.publish_back:
                finish();
                break;
        }
    }
}
