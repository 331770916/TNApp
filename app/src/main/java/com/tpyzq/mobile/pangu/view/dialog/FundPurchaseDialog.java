package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;


/**
 * Created by 陈新宇 on 2016/8/31.
 * 基金申购页面dialog
 */
public class FundPurchaseDialog extends BaseDialog implements View.OnClickListener {
    TextView tv_title,tv_fund_priceway, tv_fund_name, tv_fund_code, tv_fund_price;
    Button bt_true, bt_false;
    FundSubsEntity fundData;
    String price;
    private FundPurchaseListen fundPurchaseListen;


    public FundPurchaseDialog(Context context, FundSubsEntity fundData, String price, FundPurchaseListen fundPurchaseListen) {
        super(context);
        this.fundData = fundData;
        this.price = price;
        this.fundPurchaseListen =  fundPurchaseListen;
    }

    @Override
    public void setView() {
        tv_fund_name = (TextView) findViewById(R.id.tv_fund_name);
        tv_fund_code = (TextView) findViewById(R.id.tv_fund_code);
        tv_fund_priceway = (TextView) findViewById(R.id.tv_fund_priceway);
        tv_fund_price = (TextView) findViewById(R.id.tv_fund_price);
        tv_title = (TextView) findViewById(R.id.tv_title);
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_fund_subs;
    }

    @Override
    public void initData() {
        bt_true.setOnClickListener(this);
        bt_false.setOnClickListener(this);
        tv_title.setText("基金申购");
        tv_fund_priceway.setText("申购金额:");
        tv_fund_name.setText(fundData.FUND_NAME);
        tv_fund_code.setText(fundData.FUND_CODE);
        tv_fund_price.setText(price);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                fundPurchaseListen.setEntrust(price,fundData.FUND_COMPANY,fundData.FUND_CODE);
                dismiss();
                break;

            case R.id.bt_false:
                dismiss();
                break;
        }
    }
    public interface FundPurchaseListen{
        void setEntrust(String price, String fund_company, String fund_code);
    }
}
