package com.tpyzq.mobile.pangu.activity.trade.stock;


import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;

import java.util.List;

/**
 * wangq
 * 分级基金合并
 */
public class FJFundGradingMergerActivity extends BaseActivity implements View.OnClickListener, InterfaceCollection.InterfaceCallback, StructuredFundDialog.Expression {
    public static String TAG = "FJFundGradingMergerActivity";
    public static final int REQUSET = 1;

    private EditText mInput_et;                 //输入股票代码
    private EditText mAmount_et;                //输入基金份额
    private TextView mCnFundNameValue_tv;       //基金名称
    private TextView mCnFundNetValueValue_tv;   //股东代码
    private TextView mStatements_tv;            //状态说明
    private TextView mCnExpendableFundValue_tv; //可合数量
    private Button mConfirm_but;                //合并
    private String mSession;

    private int MAXNUM = 6;
    private int mPoint = -1;
    private InterfaceCollection ifc;
    private StructuredFundEntity bean;
    private Dialog mDialog;

    @Override
    public void initView() {
        findViewById(R.id.ivCurrencyFundRedeem_back).setOnClickListener(this);
        findViewById(R.id.textView8).setOnClickListener(this);
        mInput_et = (EditText) findViewById(R.id.editText);
        mAmount_et = (EditText) findViewById(R.id.amount_et);
        mCnFundNameValue_tv = (TextView) findViewById(R.id.tvCnFundNameValue);
        mCnFundNetValueValue_tv = (TextView) findViewById(R.id.tvCnFundNetValueValue);
        mStatements_tv = (TextView) findViewById(R.id.tvStatements);
        mCnExpendableFundValue_tv = (TextView) findViewById(R.id.tvCnExpendableFundValue);
        mConfirm_but = (Button) findViewById(R.id.butConfirm);

        initMonitor();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_grading_fund_merger;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ivCurrencyFundRedeem_back:
                finish();
                break;
            case R.id.textView8:        //选择基金
                intent.setClass(this, FJFundChooseActivity.class);
                intent.putExtra("point", mPoint);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.butConfirm:
                StructuredFundDialog dialog = new StructuredFundDialog(this, TAG, this, bean, mAmount_et.getText().toString(), mInput_et.getText().toString());
                dialog.show();
                break;
        }
    }

    private void initMonitor() {
        ifc = InterfaceCollection.getInstance();
        mSession = SpUtils.getString(this, "mSession", "");
        mConfirm_but.setText("合并");
        mConfirm_but.setOnClickListener(this);
        mConfirm_but.setClickable(false);
        mInput_et.addTextChangedListener(new MyInputTextWatcher());
        mAmount_et.addTextChangedListener(new MyAmountTextWatcher());

        mAmount_et.setEnabled(false);
    }

    private void getAffirmMsg(String stocken_code) {
        mDialog = LoadingDialog.initDialog(this, "加载中...");
        if (!this.isFinishing()) {
            mDialog.show();
        }
        ifc.queryStructuredFund(mSession, stocken_code, TAG, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUSET && resultCode == RESULT_OK) {
            mPoint = intent.getIntExtra("point", -1);
            mInput_et.setText(intent.getStringExtra("Code"));
            intent.getStringExtra("Market");
            intent.getStringExtra("Name");
        }
        if (requestCode == REQUSET && resultCode == 500) {
            mAmount_et.setText("");
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        mDialog.dismiss();
        if ("0".equals(info.getCode())) {
            mAmount_et.setEnabled(true);
            List<StructuredFundEntity> list = (List<StructuredFundEntity>) info.getData();
            bean = list.get(0);
            mCnFundNameValue_tv.setText(bean.getStoken_name());
            mCnFundNetValueValue_tv.setText(bean.getStock_account());
            mStatements_tv.setText(bean.getFund_status());
            mCnExpendableFundValue_tv.setText(bean.getMerge_amount());


            bean = new StructuredFundEntity();
            bean.setStoken_name("测试");
            bean.setMerge_amount("1200");
            bean.setFund_status("测试");
            mCnFundNameValue_tv.setText("测试");
            mCnFundNetValueValue_tv.setText(bean.getStoken_name());
            mStatements_tv.setText(bean.getFund_status());
            mCnExpendableFundValue_tv.setText(bean.getMerge_amount());

        } else if ("-1".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
            Helper.getInstance().showToast(this, info.getMsg());
            factoryReset();
        } else if ("-6".equals(info.getCode())) {
            Intent intent = new Intent();
            intent.setClass(this, TransactionLoginActivity.class);
            startActivity(intent);
        } else {
            MistakeDialog.showDialog(info.getMsg(), this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    mInput_et.setText("");
                    factoryReset();
                }
            });
        }
    }

    @Override
    public void State() {
        mInput_et.setText("");
        factoryReset();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            finish();
        }
        return false;
    }

    /**
     * EditText 内容监听
     */
    class MyInputTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == MAXNUM) {
                getAffirmMsg(s.toString());
            } else {
                factoryReset();
            }
        }
    }

    class MyAmountTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {                                                                     //如果申购金额输入框有数据
                mConfirm_but.setBackgroundResource(R.drawable.lonin);                 //背景亮
                mConfirm_but.setClickable(true);                                        //可点击
            } else {
                mConfirm_but.setBackgroundResource(R.drawable.lonin4);                //背景灰色
                mConfirm_but.setClickable(false);                                       //不可点击
            }
        }
    }


    //清空数据
    private void factoryReset() {
        mAmount_et.setEnabled(false);
        mCnFundNameValue_tv.setText("--");
        mCnFundNetValueValue_tv.setText("--");
        mStatements_tv.setText("--");
        mCnExpendableFundValue_tv.setText("--");
        mAmount_et.setText("");
    }
}
