package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.interfac.IsClickedListener;

/**
 * Created by wangqi on 2016/12/7.
 * OTC 认购确认信息弹框
 */

public class OTC_SubscriptionDialog extends BaseDialog implements View.OnClickListener{
    private static final String TAG = "OTC_SubscriptionWindow";
    private final String pro_name;
    private TextView tvOTC_ChanPinMingCheng,tvOTC_ChanPinDaiMa,tvOTC_RenGouJinE=null;
    private Context context;
    private String stockCode;
    private String SubscriptionMoney;
    private IsClickedListener isOk;

    public OTC_SubscriptionDialog(Context context, String pro_name,
                                  String SubscriptionMoney, String stockCode, IsClickedListener isOk) {
        super(context);
        this.context=context;
        this.pro_name = pro_name;
        this.stockCode=stockCode;
        this.SubscriptionMoney=SubscriptionMoney;
        this.isOk = isOk;
    }

    @Override
    public void setView() {
        tvOTC_ChanPinMingCheng= (TextView) findViewById(R.id.tvOTC_ChanPinMingCheng);       //产品名称
        tvOTC_ChanPinMingCheng.setText(pro_name);
        tvOTC_ChanPinDaiMa= (TextView) findViewById(R.id.tvOTC_ChanPinDaiMa);               //产品代码
        tvOTC_ChanPinDaiMa.setText(stockCode);
        tvOTC_RenGouJinE= (TextView) findViewById(R.id.tvOTC_RenGouJinE);                   //认购金额
        tvOTC_RenGouJinE.setText(SubscriptionMoney);


    }

    @Override
    public int getLayoutId() {
        return R.layout.otc_subsription_popupwindow;
    }

    @Override
    public void initData() {
        findViewById(R.id.tvOTC_QX).setOnClickListener(this);                                 //取消
        findViewById(R.id.tvOTC_QD).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvOTC_QX:
                dismiss();
                break;
            case R.id.tvOTC_QD:
                isOk.callBack(true);
                break;
        }
    }
}
