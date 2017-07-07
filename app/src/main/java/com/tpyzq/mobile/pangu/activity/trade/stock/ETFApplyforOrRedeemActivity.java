package com.tpyzq.mobile.pangu.activity.trade.stock;

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
import com.tpyzq.mobile.pangu.activity.trade.open_fund.SignActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;

import java.util.List;

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
    public static String TAG_APPLYFOR = "Applyfor_YES";   //  申购确定
    public static String TAG_REAEEM = "Redeem_YES";   //  赎回确定
    private Dialog mDialog;
    private EtfDataEntity etfDataEntity;
    private TextView title;

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
        title = (TextView) findViewById(R.id.tv_title);
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
            tv_upperlimit.setText("申购上限：--");
            title.setText("ETF申购");
        } else {
            title.setText("ETF赎回");
            tv_share.setText("赎回份额");
            btSubmit.setText("赎  回");
            tv_upperlimit.setText("赎回上限：--");
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
        switch (getCurrentFocus().getId()) {
            case R.id.et_input_count:
                String str = mInputCount.getText().toString().trim();
                if(str.indexOf('0')==0){
                    mInputCount.setText("");
                    Helper.getInstance().showToast(this, "首位不能为0");
                }
                break;
        }


    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (getCurrentFocus().getId()) {
            case R.id.et_input_code:
                if (s.length() == 6) {
                    //  请求网络接口
                    mDialog = LoadingDialog.initDialog(this, "正在查询...");
                    if (!this.isFinishing()) {
                        mDialog.show();
                    }
                    InterfaceCollection.getInstance().queryApplyfor(token, s.toString().trim(), TAG, this);
                    if (!mInputCount.getText().toString().trim().isEmpty()) {
                        btSubmit.setBackgroundResource(R.drawable.lonin);
                        btSubmit.setEnabled(true);
                    }
                } else {
                    //  清空数据
                    if ("Applyfor".equals(type)) {
                        tv_upperlimit.setText("申购上限：--");
                    } else {
                        tv_upperlimit.setText("赎回上限：--");
                    }
                    available_funds.setText("--");
                    etf_code.setText("--");
                    tv_shareholder.setText("--");
                    btSubmit.setBackgroundResource(R.drawable.lonin4);
                    btSubmit.setEnabled(false);
                }
                break;
            case R.id.et_input_count:
                if (!s.toString().isEmpty() && mInputCode.getText().toString().length() == 6) {
                    btSubmit.setBackgroundResource(R.drawable.lonin);
                    btSubmit.setEnabled(true);
                } else {
                    btSubmit.setBackgroundResource(R.drawable.lonin4);
                    btSubmit.setEnabled(false);
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
                        String count = mInputCount.getText().toString().trim();
                        if (etfDataEntity==null){
                            etfDataEntity = new EtfDataEntity();
                        }
                        etfDataEntity.setEntrust_amount(count);
                        StructuredFundDialog dialog = new StructuredFundDialog(this, TAG, this, etfDataEntity, null, null);
                        dialog.show();
                    }
                } else {
                    // 请求赎回
                    if (isJudge()) {
                        String count = mInputCount.getText().toString().trim();
                        if (etfDataEntity==null){
                            etfDataEntity = new EtfDataEntity();
                        }
                        etfDataEntity.setEntrust_amount(count);
                        StructuredFundDialog dialog = new StructuredFundDialog(this, TAG_SH, this, etfDataEntity, null, null);
                        dialog.show();
                    }
                }
                break;
        }
    }

    private boolean isJudge() {
        String code = mInputCode.getText().toString().trim();
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
        if ("Applyfor".equals(info.getTag())) {
            String code = info.getCode();
            if ("0".equals(code)) {
                List<EtfDataEntity> list = (List<EtfDataEntity>) info.getData();
                etfDataEntity = list.get(0);
                if ("Applyfor".equals(type)) {
                    tv_upperlimit.setText("申购上限：" + etfDataEntity.getAllot_max());
                } else {
                    tv_upperlimit.setText("赎回上限：" + etfDataEntity.getRedeem_max());
                }
                available_funds.setText(etfDataEntity.getEnable_balance());
                etf_code.setText(etfDataEntity.getStock_name());
                tv_shareholder.setText(etfDataEntity.getStock_account());
                mInputCount.setEnabled(true);
            } else if ("-6".equals(code)) {
                skip.startLogin(this);
            } else {//-1,-2,-3情况下显示定义好信息
                tv_upperlimit.setText("申购上限：--");
                available_funds.setText("--");
                etf_code.setText("--");
                tv_shareholder.setText("--");
                MistakeDialog.showDialog(info.getMsg(), ETFApplyforOrRedeemActivity.this);
                etfDataEntity = null;
//                mInputCount.setEnabled(false);
            }
        } else if ("Redeem".equals(info.getTag())) {
            String code = info.getCode();
            if ("0".equals(code)) {
                List<EtfDataEntity> list = (List<EtfDataEntity>) info.getData();
                etfDataEntity = list.get(0);
                tv_upperlimit.setText(etfDataEntity.getAllot_max());
                available_funds.setText(etfDataEntity.getEnable_balance());
                etf_code.setText(etfDataEntity.getStock_name());
                tv_shareholder.setText(etfDataEntity.getStock_account());
                mInputCount.setEnabled(true);
            } else if ("-6".equals(code)) {
                skip.startLogin(this);
            } else {//-1,-2,-3情况下显示定义好信息
                tv_upperlimit.setText("赎回上限：--");
                available_funds.setText("--");
                etf_code.setText("--");
                tv_shareholder.setText("--");
                MistakeDialog.showDialog(info.getMsg(), ETFApplyforOrRedeemActivity.this);
                etfDataEntity = null;
//                mInputCount.setEnabled(false);
            }
        } else if (TAG_APPLYFOR.equals(info.getTag())) {
            String code = info.getCode();
            if ("0".equals(code)) {
                List<EtfDataEntity> list = (List<EtfDataEntity>) info.getData();
                etfDataEntity = list.get(0);
                Helper.getInstance().showToast(this, info.getMsg());
                mInputCode.setText("");
                mInputCount.setText("");
                tv_upperlimit.setText("申购上限：--");
                available_funds.setText("--");
                etf_code.setText("--");
                tv_shareholder.setText("--");

            } else if ("-6".equals(code)) {
                skip.startLogin(this);
            } else {
                MistakeDialog.showDialog(info.getMsg(), ETFApplyforOrRedeemActivity.this);
            }

        } else if (TAG_REAEEM.equals(info.getTag())) {
            String code = info.getCode();
            if ("0".equals(code)) {
                List<EtfDataEntity> list = (List<EtfDataEntity>) info.getData();
                etfDataEntity = list.get(0);
                Helper.getInstance().showToast(this, info.getMsg());
                mInputCode.setText("");
                mInputCount.setText("");
                tv_upperlimit.setText("申购上限：--");
                available_funds.setText("--");
                etf_code.setText("--");
                tv_shareholder.setText("--");

            } else if ("-6".equals(code)) {
                skip.startLogin(this);
            } else {
                MistakeDialog.showDialog(info.getMsg(), ETFApplyforOrRedeemActivity.this);
            }

        }

    }

    @Override
    public void State() {
        if ("Applyfor".equals(type)) {   //  申购
            InterfaceCollection.getInstance().
                    applyforDetermine("300734", token, etfDataEntity.
                                    getExchange_type(), etfDataEntity.getStock_code(),
                            mInputCount.getText().toString().trim(), TAG_APPLYFOR, this);
        } else {   //  赎回
            InterfaceCollection.getInstance().
                    applyforDetermine("300736", token, etfDataEntity.
                                    getExchange_type(), etfDataEntity.getStock_code(),
                            mInputCount.getText().toString().trim(), TAG_REAEEM, this);

        }

    }
}
