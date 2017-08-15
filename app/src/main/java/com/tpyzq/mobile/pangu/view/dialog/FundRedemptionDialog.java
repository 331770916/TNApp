package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;


/**
 * Created by 陈新宇 on 2016/8/31.
 * 基金赎回页面dialog
 */
public class FundRedemptionDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_fund_name, tv_fund_code, tv_fund_price;
    private Button bt_true, bt_false;
    private String sum;
    private String fundcode;
    private String fundname;
    private FundRecall fundRecall;
    private TextView tv_fund_priceway;
    private TextView tv_title;

    public FundRedemptionDialog(Context context, String fundname, String fundcode, String sum, FundRecall fundRecall) {
        super(context);
        this.fundname = fundname;
        this.fundcode = fundcode;
        this.sum = sum;
        this.fundRecall = fundRecall;
    }

    @Override
    public void setView() {
        tv_fund_name = (TextView) findViewById(R.id.tv_fund_name);
        tv_fund_code = (TextView) findViewById(R.id.tv_fund_code);
        tv_fund_price = (TextView) findViewById(R.id.tv_fund_price);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_fund_priceway = (TextView) findViewById(R.id.tv_fund_priceway);
        findViewById(R.id.ll_fhfs).setVisibility(View.GONE);
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
        tv_title.setText("基金赎回");
        tv_fund_priceway.setText("赎回份额:");
        tv_fund_name.setText(fundname);
        tv_fund_code.setText(fundcode);
        tv_fund_price.setText(sum);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                fundRecall.setRecall();
                dismiss();
                break;

            case R.id.bt_false:
                dismiss();
                break;
        }
    }

    public interface FundRecall {
        void setRecall();
    }
}
