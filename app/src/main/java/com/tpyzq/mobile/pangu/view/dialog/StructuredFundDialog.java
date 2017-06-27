package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.Tag;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJFundGradingMergerActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJFundSplitActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJWithdrawOrderActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;

import java.util.List;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金
 * markbit  0 分级基金合并  1 分级基金分拆 2 基金撤单
 */

public class StructuredFundDialog extends BaseDialog implements View.OnClickListener, InterfaceCollection.InterfaceCallback {
    private String mTAG;
    private TextView tv_title;
    private TextView tv_2;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_8;
    private TextView tv_10;
    private ImageView img_1;

    private Expression mExpression;
    private StructuredFundEntity mStructuredFundEntity;
    private String mShare;
    private String mInput;
    private InterfaceCollection ifc;
    private String mSession;

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

        img_1 = (ImageView) findViewById(R.id.bank_img);
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
        mSession = SpUtils.getString(context, "mSession", "");
        tv_5.setText(context.getString(R.string.securities_name) + "：");
        if (FJFundGradingMergerActivity.TAG.equals(mTAG)) {
            img_1.setImageResource(R.mipmap.structuredfund_merge);
            tv_title.setText(R.string.Gradingfundmerger);
            tv_2.setText(R.string.Gradingfundmerger);
            tv_4.setText(mInput);
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_8.setText(mShare);
            tv_10.setText("");
        } else if (FJFundSplitActivity.TAG.equals(mTAG)) {
            img_1.setImageResource(R.mipmap.structuredfund_partition);
            tv_title.setText(R.string.FJFundSplitActivity);
            tv_2.setText(R.string.FJFundSplitActivity);
            tv_4.setText(mInput);
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_8.setText(mShare);
            tv_10.setText("");
        } else if (FJWithdrawOrderActivity.TAG.equals(mTAG)) {
            img_1.setImageResource(R.mipmap.bank_img);
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
        ifc = InterfaceCollection.getInstance();
        switch (v.getId()) {
            case R.id.bt_true:
                if (FJFundGradingMergerActivity.TAG.equals(mTAG)) {
                    ifc.mergerStructuredFund("tpyzq", mStructuredFundEntity.getExchange_type(), mStructuredFundEntity.getStock_account(), mInput, mShare, mSession, mTAG, this);
                    Helper.getInstance().showToast(context, "委托提交成功");
                    dismiss();
                } else if (FJFundSplitActivity.TAG.equals(mTAG)) {
                    ifc.splitStructuredFund("tpyzq", mStructuredFundEntity.getExchange_type(), mStructuredFundEntity.getStock_account(), mInput, mShare, mSession, mTAG, this);
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

    @Override
    public void callResult(ResultInfo info) {
        List<StructuredFundEntity> list = (List<StructuredFundEntity>) info.getData();
        StructuredFundEntity bean = list.get(0);
        if ("0".equals(info.getCode())) {
            bean.getInit_date();
            bean.getMerge_amount();
            Helper.getInstance().showToast(context, "委托提交成功");
            mExpression.State();
            dismiss();
        } else if ("-1".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
            Helper.getInstance().showToast(context, info.getMsg());
        } else {
            Helper.getInstance().showToast(context, "委托提交成功");
            dismiss();
        }
    }

    public interface Expression {
        void State();
    }
}
