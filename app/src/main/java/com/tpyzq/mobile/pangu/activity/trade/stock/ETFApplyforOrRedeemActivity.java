package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;

/**
 * Created by zhang on 2017/7/4.
 * ETF  apply for    申购    redeem 赎回
 */

public class ETFApplyforOrRedeemActivity extends BaseActivity implements TextWatcher, View.OnClickListener, InterfaceCollection.InterfaceCallback, StructuredFundDialog.Expression {
    private EditText mInputCode, mInputCount; //
    private TextView tv_upperlimit, tv_share, available_funds, etf_code, tv_shareholder;
    private String type;
    private ImageView img_back;
    private Button btSubmit;
    private String token;
    public static String TAG = "Applyfor";   //  申购
    public static String TAG_SH = "Redeem";   //  赎回
    private Dialog mDialog;
    private EtfDataEntity etfDataEntity;

    @Override
    public void initView() {
        mInputCode = (EditText) findViewById(R.id.et_input_code);
        mInputCount = (EditText) findViewById(R.id.et_input_count);
        tv_upperlimit = (TextView) findViewById(R.id.tv_upperlimit);
        tv_share = (TextView) findViewById(R.id.tv_share);
        img_back = (ImageView) findViewById(R.id.iv_back);
        etf_code = (TextView) findViewById(R.id.tv_etf_code);
        btSubmit = (Button) findViewById(R.id.bt_commint);
        available_funds = (TextView) findViewById(R.id.available_funds);
        tv_shareholder = (TextView) findViewById(R.id.tv_shareholder_code);
        getIntentPost();
        initEvent();
    }

    private void initEvent() {
        mInputCode.addTextChangedListener(this);
        mInputCount.addTextChangedListener(this);
        img_back.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    private void getIntentPost() {
        token = SpUtils.getString(this, "mSession", "");
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
        }
        if ("Applyfor".equals(type)) {
            tv_share.setText("申购份额");
            btSubmit.setText("申  购");
        } else {
            tv_share.setText("赎回份额");
            btSubmit.setText("赎  回");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_etf_redeem;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (getCurrentFocus().getId()) {
            case R.id.et_input_code:
                if (s.length() == 6) {
                    //  请求网络接口
                    mDialog = LoadingDialog.initDialog(this, "正在查询...");
                    InterfaceCollection.getInstance().queryApplyfor(token, s.toString().trim(), TAG, this);
                }else {
                    //  清空数据
                    tv_upperlimit.setText("");
                    available_funds.setText("");
                    etf_code.setText("");
                    tv_shareholder.setText("");
                    btSubmit.setEnabled(false);
                }
                break;
            case R.id.et_input_count:
                if (s.toString().isEmpty() && mInputCode.toString().length()==6){
                    btSubmit.setBackgroundResource(R.drawable.lonin);
                    btSubmit.setEnabled(true);
                }

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:    // 返回按钮
                finish();
                break;
            case R.id.bt_commint:
                //  点击赎回或者申购
                if ("Applyfor".equals(type)) {
                    //  请求申购
                    if (isJudge()) {
                        StructuredFundDialog dialog = new StructuredFundDialog(this, TAG, this, etfDataEntity, null, null);
                        dialog.show();
                    }
                } else {
                    // 请求赎回
                    if (isJudge()) {
                        InterfaceCollection.getInstance().
                                applyforDetermine(token, etfDataEntity.
                                                getExchange_type(), etfDataEntity.getStock_code(),
                                        mInputCount.getText().toString().trim(), TAG_SH, this);
                    }
                }
                break;
        }
    }

    private boolean isJudge() {
        String code = mInputCount.getText().toString().trim();
        String count = mInputCount.getText().toString().trim();
        if (code.isEmpty()) {
            Helper.getInstance().showToast(this, "请输入代码");
            return false;
        }
        if (count.isEmpty()) {
            Helper.getInstance().showToast(this, "请输入份额");
            return false;
        }
        return true;
    }


    @Override
    public void callResult(ResultInfo info) {
        if (mDialog.isShowing() && mDialog != null) {
            mDialog.dismiss();
        }
        if (info.getTag().equals("Applyfor")) {
            String code = info.getCode();
            if ("0".equals(code)) {
                etfDataEntity = (EtfDataEntity) info.getData();
                tv_upperlimit.setText(etfDataEntity.getAllot_max());
                available_funds.setText(etfDataEntity.getEnable_balance());
                etf_code.setText(etfDataEntity.getStock_name());
                tv_shareholder.setText(etfDataEntity.getStock_code());
            } else if ("-6".equals(code)) {
                skip.startLogin(this);
            } else {//-1,-2,-3情况下显示定义好信息
                Helper.getInstance().showToast(this, info.getMsg());
            }
        } else {
            String code = info.getCode();
            if ("0".equals(code)) {
                tv_upperlimit.setText("");
                available_funds.setText("");
                etf_code.setText("");
                tv_shareholder.setText("");
            } else if ("-6".equals(code)) {
                skip.startLogin(this);
            } else {//-1,-2,-3情况下显示定义好信息
                Helper.getInstance().showToast(this, info.getMsg());
            }
        }
    }

    @Override
    public void State() {
        InterfaceCollection.getInstance().
                applyforDetermine(token, etfDataEntity.
                                getExchange_type(), etfDataEntity.getStock_code(),
                        mInputCount.getText().toString().trim(), TAG_SH, this);
    }
}
