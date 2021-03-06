package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.AddOrModFixFundActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FixFundListActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ETFApplyforOrRedeemActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ETFRevokeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJFundGradingMergerActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJFundSplitActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.FJWithdrawOrderActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.VoteDetailActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.FixFundEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金
 * markbit  0 分级基金合并  1 分级基金分拆 2 基金撤单
 */

public class StructuredFundDialog extends BaseDialog implements View.OnClickListener, InterfaceCollection.InterfaceCallback {
    private String mTAG;
    private TextView tv_title;
    private TextView tv_0;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private TextView tv_9;
    private TextView tv_10;
    private TextView tv_11;
    private TextView tv_12;
    private ImageView img_1;

    private Expression mExpression;
    private Object object;
    private String mShare;
    private String mInput;
    private InterfaceCollection ifc;
    private String mSession;

    private TableRow tR_1;
    private TableRow tR_2;
    private TableRow tR_3;
    private TableRow tR_4;
    private TableRow tR_5;
    private TableRow tR_6;
    private TableLayout tableLayout;


    public StructuredFundDialog(Context context) {
        super(context);
    }

    public void setData(String tag, Expression expression, Object object, String share, String input) {
        this.mTAG = tag;
        this.mExpression = expression;
        this.object = object;
        this.mShare = share;
        this.mInput = input;

    }

    @Override
    public void setView() {
        ConstantUtil.list_item_flag = true;
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        img_1 = (ImageView) findViewById(R.id.bank_img);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_9 = (TextView) findViewById(R.id.tv_9);
        tv_11 = (TextView) findViewById(R.id.tv_11);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_10 = (TextView) findViewById(R.id.tv_10);
        tv_12 = (TextView) findViewById(R.id.tv_12);


        tR_1 = (TableRow) findViewById(R.id. tableRow1);
        tR_2 = (TableRow) findViewById(R.id. tableRow2);
        tR_3 = (TableRow) findViewById(R.id. tableRow3);
        tR_4 = (TableRow) findViewById(R.id. tableRow4);
        tR_5 = (TableRow) findViewById(R.id. tableRow5);
        tR_6 = (TableRow) findViewById(R.id. tableRow6);


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
            StructuredFundEntity mStructuredFundEntity = (StructuredFundEntity) object;
            img_1.setImageResource(R.mipmap.structuredfund_merge);
            tv_title.setText(R.string.Gradingfundmerger);
            tv_2.setText(R.string.Gradingfundmerger);
            tv_4.setText(mInput);
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_7.setText(R.string.hbed);
            tv_8.setText(mShare);
            tv_10.setText(mStructuredFundEntity.getStock_account());
            tR_6.setVisibility(View.GONE);
        } else if (FJFundSplitActivity.TAG.equals(mTAG)) {
            StructuredFundEntity mStructuredFundEntity = (StructuredFundEntity) object;
            img_1.setImageResource(R.mipmap.structuredfund_partition);
            tv_title.setText(R.string.FJFundSplitActivity);
            tv_2.setText(R.string.FJFundSplitActivity);
            tv_4.setText(mInput);
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_7.setText(R.string.fced);
            tv_8.setText(mShare);
            tv_10.setText(mStructuredFundEntity.getStock_account());
            tR_6.setVisibility(View.GONE);
        } else if (FJWithdrawOrderActivity.TAG.equals(mTAG)) {
            StructuredFundEntity mStructuredFundEntity = (StructuredFundEntity) object;
            img_1.setImageResource(R.mipmap.chehui);
            tv_title.setText(R.string.IsRecall);
            tv_2.setText(mStructuredFundEntity.getBusiness_name());
            tv_4.setText(mStructuredFundEntity.getStocken_code());
            tv_6.setText(mStructuredFundEntity.getStoken_name());
            tv_8.setText(mStructuredFundEntity.getEntrust_amount());
            tv_10.setText(mStructuredFundEntity.getStock_account());
            tR_6.setVisibility(View.GONE);
        } else if (VoteDetailActivity.TAG.equals(mTAG)) {
            img_1.setImageResource(R.mipmap.vote_dialog_icon);
            tv_title.setText(R.string.voteDialogTitle);

            tv_0.setText("共有" + mShare + "笔委托需要提交，请确认");
            tv_0.setVisibility(View.VISIBLE);
            tv_0.setTextColor(getContext().getResources().getColor(R.color.hushenTab_titleColor));

            tv_1.setText("股东代码：");
            tv_2.setText(mInput);
            tR_2.setVisibility(View.GONE);


            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,Helper.dip2px(context,10),0,Helper.dip2px(context,20));
            tableLayout.setLayoutParams(layoutParams);

            tR_2.setVisibility(View.GONE);
            tR_3.setVisibility(View.GONE);
            tR_4.setVisibility(View.GONE);
            tR_5.setVisibility(View.GONE);
            tR_6.setVisibility(View.GONE);

        } else if (ETFApplyforOrRedeemActivity.TAG.equals(mTAG)) {   //  申购
            EtfDataEntity etfDataEntity = (EtfDataEntity) object;
            img_1.setImageResource(R.mipmap.jy_jijinshengou);
            tv_title.setText("ETF申购");
            tv_1.setText("操作类别:");
            tv_3.setText("ETF代码:");
            tv_5.setText("ETF名称:");
            tv_7.setText("申购数量:");
            tv_9.setText("股东代码:");
            tv_2.setText("ETF申购");
            tv_4.setText(etfDataEntity.getStock_code());
            tv_6.setText(etfDataEntity.getStock_name());
            tv_8.setText(etfDataEntity.getEntrust_amount());
            tv_10.setText(etfDataEntity.getStock_account());
            tR_6.setVisibility(View.GONE);
        } else if (ETFApplyforOrRedeemActivity.TAG_SH.equals(mTAG)) {   //  赎回
            EtfDataEntity etfDataEntity = (EtfDataEntity) object;
            img_1.setImageResource(R.mipmap.jy_jijingouhui);
            tv_title.setText("ETF赎回");
            tv_1.setText("操作类别:");
            tv_3.setText("ETF代码:");
            tv_5.setText("ETF名称:");
            tv_7.setText("赎回数量:");
            tv_9.setText("股东代码:");
            tv_2.setText("ETF赎回");
            tv_4.setText(etfDataEntity.getStock_code());
            tv_6.setText(etfDataEntity.getStock_name());
            tv_8.setText(etfDataEntity.getEntrust_amount());
            tv_10.setText(etfDataEntity.getStock_account());
            tR_6.setVisibility(View.GONE);
        } else if (ETFRevokeActivity.TAG.equalsIgnoreCase(mTAG)) {
            EtfDataEntity etfDataEntity = (EtfDataEntity) object;
            tv_title.setText("ETF申赎撤单");
            tv_1.setText("操作类别:");
            tv_3.setText("ETF代码:");
            tv_5.setText("ETF名称:");
            tv_7.setText("赎回份额:");
            tv_9.setText("股东代码:");
            tv_2.setText("ETF申赎撤单");
            tv_4.setText(etfDataEntity.getStock_code());
            tv_6.setText(etfDataEntity.getStock_name());
            tv_8.setText(etfDataEntity.getPrev_balance());
            tv_10.setText(etfDataEntity.getStock_account());
            tR_6.setVisibility(View.GONE);
        } else if (FixFundListActivity.TAG.equalsIgnoreCase(mTAG)) {//基金定投撤单弹框
            FixFundEntity fixFundEntity = (FixFundEntity) object;
            tv_title.setText("定投撤销");
            tv_1.setText("基金名称：");
            tv_3.setText("基金代码：");
            tv_5.setText("月扣款日：");
            tv_7.setText("定投金额：");
            tv_9.setText("开始日期：");
            tv_11.setText("结束日期：");
            tv_2.setText(fixFundEntity.getFUND_NAME());
            tv_4.setText(fixFundEntity.getFUND_CODE());
            tv_6.setText("每月"+fixFundEntity.getEN_FUND_DATE()+"日");
            tv_8.setText(fixFundEntity.getBALANCE());
            tv_10.setText(fixFundEntity.getSTART_DATE());
            tv_12.setText(fixFundEntity.getEND_DATE());
//        } else if (AddOrModFixFundActivity.TAG_SUBMIT.equalsIgnoreCase(mTAG)||AddOrModFixFundActivity.TAG_MODIFY.equalsIgnoreCase(mTAG)) {//定投基金增加或修改
        } else if (AddOrModFixFundActivity.TAG_REQUEST_FUND.equalsIgnoreCase(mTAG)) {//定投基金增加或修改
            FixFundEntity fixFundEntity = (FixFundEntity) object;
            tv_title.setText(mShare);
            img_1.setImageResource(R.mipmap.icon_fix_fund);
            tv_1.setText("基金名称：");
            tv_3.setText("基金代码：");
            tv_5.setText("月扣款日：");
            tv_7.setText("定投金额：");
            tv_9.setText("开始日期：");
            tv_11.setText("结束日期：");
            tv_2.setText(fixFundEntity.getFUND_NAME());
            tv_4.setText(fixFundEntity.getFUND_CODE());
            tv_6.setText("每月"+fixFundEntity.getEN_FUND_DATE()+"日");
            tv_8.setText(fixFundEntity.getBALANCE());
            tv_10.setText(fixFundEntity.getSTART_DATE());
            tv_12.setText(fixFundEntity.getEND_DATE());
        }
    }


    @Override
    public void onClick(View v) {
        ifc = InterfaceCollection.getInstance();
        ConstantUtil.list_item_flag = true;
        switch (v.getId()) {
            case R.id.bt_true:
                if (FJFundGradingMergerActivity.TAG.equals(mTAG)) {
                    dismiss();
                    StructuredFundEntity mStructuredFundEntity = (StructuredFundEntity) object;
                    ifc.mergerStructuredFund(mStructuredFundEntity.getExchange_type(), mStructuredFundEntity.getStock_account(), mInput, mShare, mSession, mTAG, this);
                } else if (FJFundSplitActivity.TAG.equals(mTAG)) {
                    dismiss();
                    StructuredFundEntity mStructuredFundEntity = (StructuredFundEntity) object;
                    ifc.splitStructuredFund(mStructuredFundEntity.getExchange_type(), mStructuredFundEntity.getStock_account(), mInput, mShare, mSession, mTAG, this);
                } else if (FJWithdrawOrderActivity.TAG.equals(mTAG)) {
                    mExpression.State();
                    dismiss();
                } else if (VoteDetailActivity.TAG.equals(mTAG)) {
                    mExpression.State();
                    dismiss();
                } else if (ETFApplyforOrRedeemActivity.TAG.equals(mTAG)) {   //  申购
                    mExpression.State();
                    dismiss();
                } else if (ETFApplyforOrRedeemActivity.TAG_SH.equals(mTAG)) {   //  赎回
                    mExpression.State();
                    dismiss();
                } else if (FixFundListActivity.TAG.equalsIgnoreCase(mTAG)) {//基金定投撤单弹框
                    mExpression.State();
                } else if (ETFRevokeActivity.TAG.equalsIgnoreCase(mTAG)) {
                    mExpression.State();
                    dismiss();
//                } else if (AddOrModFixFundActivity.TAG_SUBMIT.equalsIgnoreCase(mTAG)||AddOrModFixFundActivity.TAG_MODIFY.equalsIgnoreCase(mTAG)) {
                } else if (AddOrModFixFundActivity.TAG_REQUEST_FUND.equalsIgnoreCase(mTAG)) {
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
        if ("0".equals(info.getCode())) {
            StructuredFundEntity bean = (StructuredFundEntity) info.getData();
            bean.getInit_date();
            bean.getMerge_amount();
//            CentreToast.showText(context, "委托提交成功");
            CentreToast.showText(context,"委托已提交",true);
            mExpression.State();
        } else if ("400".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
            CentreToast.showText(context, info.getMsg());
        } else {
//            CentreToast.showText(context, info.getMsg());
            CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(info.getMsg(),CustomCenterDialog.SHOWCENTER);
            customCenterDialog.show(((Activity) context).getFragmentManager(),StructuredFundDialog.class.toString());
        }
    }

    public interface Expression {
        void State();
    }
}
