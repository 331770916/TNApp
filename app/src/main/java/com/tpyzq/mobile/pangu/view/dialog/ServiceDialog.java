package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;


/**
 * Created by wangqi on 2016/9/22.
 * 客服热线 弹窗
 */
public class ServiceDialog extends BaseDialog implements View.OnClickListener {

    private Button mCancelbtn, mConfirmbtn;

    public ServiceDialog(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        TextView mPromptTv = (TextView) findViewById(R.id.DLPromptIV);
        TextView mPromptDetailsTV = (TextView) findViewById(R.id.DLPromptDetailsTV);
        mConfirmbtn = (Button) findViewById(R.id.Confirmbtn);
        mCancelbtn = (Button) findViewById(R.id.Cancelbtn);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.kefurexian);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPromptTv.setCompoundDrawables(drawable, null, null, null);
        mPromptTv.setText("客服热线");
        mPromptDetailsTV.setText("确认拨打客服电话?");
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+context.getResources().getString(R.string.dh)));
                context.startActivity(intent);
                dismiss();
//                    mContext.startActivity(new Intent(Intent.ACTION_CALL).setData();
                break;
        }
    }
}