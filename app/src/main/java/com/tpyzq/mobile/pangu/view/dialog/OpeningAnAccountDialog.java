package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.util.ConstantUtil;


/**
 * Created by wangqi on 2016/11/18.
 * 开户
 */
public class OpeningAnAccountDialog extends BaseDialog implements View.OnClickListener {
    private Button mConfirmbtn, mCancelbtn;

    public OpeningAnAccountDialog(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        TextView mPromptTv = (TextView) findViewById(R.id.DLPromptIV);
        TextView mPromptDetailsTV = (TextView) findViewById(R.id.DLPromptDetailsTV);
        mConfirmbtn = (Button) findViewById(R.id.Confirmbtn);
        mCancelbtn = (Button) findViewById(R.id.Cancelbtn);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.openanaccount);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPromptTv.setCompoundDrawables(drawable, null, null, null);
        mPromptTv.setText("开户提醒");
        mPromptDetailsTV.setText("立即开户?");
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_item;
    }

    @Override
    public void initData() {
        mConfirmbtn.setOnClickListener(this);
        mCancelbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Cancelbtn:
                dismiss();
                break;
            case R.id.Confirmbtn:
                dismiss();
                Intent intent = new Intent();
                intent.putExtra("type", 0);//开户 ，开户传此，
                intent.putExtra("channel", ConstantUtil.OPEN_ACCOUNT_CHANNEL);// 开户id
                intent.setClass(context, com.cairh.app.sjkh.MainActivity.class);
                context.startActivity(intent);
                break;
        }
    }
}
