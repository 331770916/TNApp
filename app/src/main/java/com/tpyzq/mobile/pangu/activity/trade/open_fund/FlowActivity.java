package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.ContractEntity;


/**
 * 电子合同流水
 */
public class FlowActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_fund_code;
    private TextView tv_fund_company;
    private TextView tv_time;
    private TextView tv_fund_status;
    private TextView tv_sign;
    private ImageView iv_back;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_fund_code = (TextView) findViewById(R.id.tv_fund_code);
        tv_fund_company = (TextView) findViewById(R.id.tv_fund_company);
        tv_fund_status = (TextView) findViewById(R.id.tv_fund_status);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        ContractEntity contractBean = (ContractEntity) intent.getSerializableExtra("sign");
        tv_fund_code.setText(contractBean.FUND_CODE);
        tv_fund_company.setText(contractBean.FUND_COMPANY);
//        0	未报
//        1	已报
//        2	成功
//        3	补正
//        4	结束
//        5	已补正
        switch (contractBean.ECONTRACT_STATUS) {
            case "0":
                tv_fund_status.setText("未报");
                break;
            case "1":
                tv_fund_status.setText("已报");
                break;
            case "2":
                tv_fund_status.setText("成功");
                break;
            case "3":
                tv_fund_status.setText("补正");
                break;
            case "4":
                tv_fund_status.setText("结束");
                break;
            case "5":
                tv_fund_status.setText("已补正");
                break;
            default:
                tv_fund_status.setText("--");
                break;
        }

        tv_time.setText(contractBean.INIT_DATE);
        tv_sign.setText("合同编号:" + contractBean.ECONTRACT_ID);
        iv_back.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_flow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
