package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;

import java.util.ArrayList;


/**
 * Created by wangqi on 2017/4/10.
 * 预约成功
 */

public class ReservationSuccessActivity extends BaseActivity implements View.OnClickListener {

    private TextView mName, mIDNumber, mIdAddress, mValidDate, riskrank, mPrompt;

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.affirm_btn).setOnClickListener(this);

        mName = (TextView) findViewById(R.id.Name);  //产品名称
        mIDNumber = (TextView) findViewById(R.id.IDNumber);    //发行日期
        mIdAddress = (TextView) findViewById(R.id.IdAddress); //起购金额
        mValidDate = (TextView) findViewById(R.id.validDate); //产品期限
        riskrank = (TextView) findViewById(R.id.riskrank);//风险等级
        mPrompt = (TextView) findViewById(R.id.Prompt);

        Intent intent = getIntent();
        ArrayList<CleverManamgerMoneyEntity> cleverManamgerMoneyEntitys = intent.getParcelableArrayListExtra("cleverManamgerMoneyEntitys");

        if (cleverManamgerMoneyEntitys != null && cleverManamgerMoneyEntitys.size() > 0) {
            mName.setText(cleverManamgerMoneyEntitys.get(0).getPRODNAME());
            mIDNumber.setText(cleverManamgerMoneyEntitys.get(0).getIPO_BEGIN_DATE());
            mIdAddress.setText(cleverManamgerMoneyEntitys.get(0).getBUY_LOW_AMOUNT());

            mValidDate.setText(cleverManamgerMoneyEntitys.get(0).getINVESTDAYS());
            riskrank.setText(cleverManamgerMoneyEntitys.get(0).getRISKLEVEL());

            mPrompt.setText("请您于" + cleverManamgerMoneyEntitys.get(0).getIPO_BEGIN_DATE() + " 9:00到15:00登录太牛APP购买本产品，如您不能在此时间内购买，产品预约自动失效。");
        } else {
            MistakeDialog.showDialog("暂无数据", this);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_reservationsuccess;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.affirm_btn:
                finish();
                break;
        }
    }
}
