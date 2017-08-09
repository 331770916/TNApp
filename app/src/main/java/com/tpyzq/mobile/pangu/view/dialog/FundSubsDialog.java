package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundSubsActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.SubsStatusEntity;


/**
 * Created by 陈新宇 on 2016/8/31.
 * 基金认购页面dialog
 */
public class FundSubsDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_fund_name, tv_fund_code, tv_fund_price,tv_fhfs;
    private Button bt_true, bt_false;
    private String price;
    private FundDataEntity fundDataBean;
    private String code;
    private SubsStatusEntity subsStatusBean;
    private FundSubsActivity.FundSubsListen fundSubsListen;
    private String content;

    public FundSubsDialog(Context context, FundDataEntity fundDataBean, String price, String content,FundSubsActivity.FundSubsListen fundSubsListen) {
        super(context);
        this.fundDataBean = fundDataBean;
        this.price = price;
        this.fundSubsListen = fundSubsListen;
        this.content = content;

    }

    @Override
    public void setView() {
        tv_fund_name = (TextView) findViewById(R.id.tv_fund_name);
        tv_fund_code = (TextView) findViewById(R.id.tv_fund_code);
        tv_fund_price = (TextView) findViewById(R.id.tv_fund_price);
        tv_fhfs  = (TextView) findViewById(R.id.tv_fund_fhfs);
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
        if (null != fundDataBean && null != fundDataBean.data && fundDataBean.data.size() > 0) {
            tv_fund_name.setText(fundDataBean.data.get(0).FUND_NAME);
            tv_fund_code.setText(fundDataBean.data.get(0).FUND_CODE);
            tv_fhfs.setText(content);
        }
        tv_fund_price.setText(price);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                if (null != fundDataBean && null != fundDataBean.data && fundDataBean.data.size() > 0) {
                    fundSubsListen.setBuy(price, fundDataBean.data.get(0).FUND_COMPANY,content);
                } else {
                    fundSubsListen.setBuy(price, "",content);
                }
                dismiss();
                break;
            case R.id.bt_false:
                dismiss();
                break;
        }
    }
}
