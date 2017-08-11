package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetProductInfoOtcConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetProductInfoOtcConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;

import java.util.ArrayList;


/**
 * Created by wangqi on 2017/4/10.
 * 已经预约
 */

public class AlreadyReservationActivity extends BaseActivity implements View.OnClickListener, ICallbackResult {
    private static String TAG = "AlreadyReservation";

    private TextView mName, mIDNumber, mIdAddress, mValidDate, riskrank, mPrompt;

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.affirm_btn).setOnClickListener(this);


        String productCode = getIntent().getStringExtra("productCode");
        mName = (TextView) findViewById(R.id.Name);  //产品名称
        mIDNumber = (TextView) findViewById(R.id.IDNumber);    //发行日期
        mIdAddress = (TextView) findViewById(R.id.IdAddress); //起购金额
        mValidDate = (TextView) findViewById(R.id.validDate); //产品期限
        riskrank = (TextView) findViewById(R.id.riskrank);//风险等级
        mPrompt = (TextView) findViewById(R.id.Prompt);

        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
        simpleRemoteControl.setCommand(new ToGetProductInfoOtcConnect(new GetProductInfoOtcConnect(TAG, "", productCode, "")));
        simpleRemoteControl.startConnect();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_alreadyreservation;
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

    @Override
    public void getResult(Object result, String tag) {
        ArrayList<CleverManamgerMoneyEntity> mEntities = (ArrayList<CleverManamgerMoneyEntity>) result;
        if (mEntities != null && mEntities.size() > 0) {
            if (!TextUtils.isEmpty(mEntities.get(0).getMistackMsg())) {
                showDialog(mEntities.get(0).getMistackMsg());
            } else {
                mName.setText(mEntities.get(0).getPRODNAME());
                mIDNumber.setText(mEntities.get(0).getIPO_BEGIN_DATE());
                mIdAddress.setText(mEntities.get(0).getBUY_LOW_AMOUNT());

                mValidDate.setText(mEntities.get(0).getINVESTDAYS());
                riskrank.setText(mEntities.get(0).getRISKLEVEL());


                mPrompt.setText("请您于" + mEntities.get(0).getIPO_BEGIN_DATE() + " 9:00到15:00登录太牛APP购买本产品，如您不能在此时间内购买，产品预约自动失效。");
            }
        } else {
            showDialog("暂无数据");
        }
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),AlreadyReservationActivity.class.toString());
    }
}
