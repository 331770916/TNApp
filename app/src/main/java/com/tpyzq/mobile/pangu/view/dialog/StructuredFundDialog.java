package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.Tag;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJFundGradingMergerActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJFundSplitActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJWithdrawOrderActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.Helper;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金
 * markbit  0 分级基金合并  1 分级基金分拆 2 基金撤单
 */

public class StructuredFundDialog extends BaseDialog implements View.OnClickListener {
    private String mTAG;
    private TextView tv_title;
    private TextView tv_2;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_8;
    private TextView tv_10;

    private Expression mExpression;
    private StructuredFundEntity mStructuredFundEntity;
    private String mShare;
    private String mInput;

    public StructuredFundDialog(Context context, String tag, Expression expression, StructuredFundEntity structuredFundEntity, String share, String input) {
        super(context);
        this.mTAG = tag;
        this.mExpression = expression;
        this.mStructuredFundEntity = structuredFundEntity;
        this.mShare = share;
        this.mInput = input;
    }

    @Override
    public void setView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_10 = (TextView) findViewById(R.id.tv_10);
        findViewById(R.id.bt_true).setOnClickListener(this);
        findViewById(R.id.bt_false).setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_structuredfund;
    }

    @Override
    public void initData() {
        tv_5.setText(context.getString(R.string.securities_name) + "：");
        if (FJFundGradingMergerActivity.TAG.equals(mTAG)) {
            tv_title.setText(R.string.Gradingfundmerger);
            tv_2.setText(R.string.Gradingfundmerger);
            tv_4.setText(mInput);
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_8.setText(mShare);
            tv_10.setText("");
        } else if (FJFundSplitActivity.TAG.equals(mTAG)) {
            tv_title.setText(R.string.FJFundSplitActivity);
            tv_2.setText(R.string.FJFundSplitActivity);
            tv_4.setText(mInput);
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_8.setText(mShare);
            tv_10.setText("");
        } else if (FJWithdrawOrderActivity.TAG.equals(mTAG)) {
            Drawable leftDrawable = context.getResources().getDrawable(R.mipmap.bank_img);
            leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getMinimumHeight());
            tv_title.setCompoundDrawables(leftDrawable, null, null, null);
            tv_title.setCompoundDrawablePadding(9);//设置图片和text之间的间距
            tv_title.setPadding(9, 0, 0, 0);
            tv_title.setText(R.string.IsRecall);
            tv_2.setText(R.string.Gradingfundmerger);
            tv_4.setText(mInput);
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_8.setText(mShare);
            tv_10.setText("");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                if (FJFundGradingMergerActivity.TAG.equals(mTAG)) {
                    Helper.getInstance().showToast(context, "委托提交成功");
                    dismiss();
                } else if (FJFundSplitActivity.TAG.equals(mTAG)) {
                    Helper.getInstance().showToast(context, "委托提交成功");
                    dismiss();
                } else if (FJWithdrawOrderActivity.TAG.equals(mTAG)) {
                    Helper.getInstance().showToast(context, "撤销此委托成功");
                    mExpression.State();
                    dismiss();
                }
                break;
            case R.id.bt_false:
                dismiss();
                break;
        }
    }

    public interface Expression {
        void State();
    }
}
