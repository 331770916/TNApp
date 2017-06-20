package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;


/**
 * Created by 陈新宇 on 2016/10/20.
 */
public class APPUpdateDialog extends BaseDialog implements View.OnClickListener {
    Button bt_true;
    Button bt_false;

    public APPUpdateDialog(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_appupdate;
    }

    @Override
    public void initData() {
        bt_true.setOnClickListener(this);
        bt_false.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                dismiss();
                break;
            case R.id.bt_false:
                dismiss();
                break;
        }
    }
}
