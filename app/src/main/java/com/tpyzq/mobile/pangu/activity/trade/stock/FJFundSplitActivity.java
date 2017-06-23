package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金 拆分
 */

public class FJFundSplitActivity extends BaseActivity implements View.OnClickListener {
    public static String TAG = "FJFundSplitActivity";
    public static final int REQUSET = 1;

    private EditText mInput_et;                 //输入股票代码
    private EditText mAmount_et;                //输入基金份额
    private TextView mCnFundNameValue_tv;       //基金名称
    private TextView mCnFundNetValueValue_tv;   //股东代码
    private TextView mStatements_tv;            //状态说明
    private TextView mCnExpendableFundValue_tv; //可合数量
    private Button mConfirm_but;                //拆分

    private String mSession;

    private int MAXNUM = 6;
    private int mPoint = -1;
    private TextView mTitle_tv;


    @Override
    public void initView() {
        findViewById(R.id.ivCurrencyFundRedeem_back).setOnClickListener(this);
        findViewById(R.id.textView8).setOnClickListener(this);
        mTitle_tv = (TextView) findViewById(R.id.tvTitle);
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
//            case R.id.butConfirm:
//
//                break;
        }
    }


    private void initMonitor() {
        mSession = SpUtils.getString(this, "mSession", "");
        mTitle_tv.setText("分级基金分拆");
        mConfirm_but.setText("分拆");
        mConfirm_but.setOnClickListener(this);
        mConfirm_but.setClickable(false);
        mInput_et.addTextChangedListener(new MyInputTextWatcher());
        mAmount_et.addTextChangedListener(new MyAmountTextWatcher());

        mAmount_et.setEnabled(false);

    }

    private void getAffirmMsg(String stocken_code) {
        StructuredFundEntity bean = new StructuredFundEntity();
        bean.setStoken_name("测试");
        bean.setMerge_amount("1200");
        bean.setFund_status("测试");


        mCnFundNameValue_tv.setText("测试");
        mCnFundNetValueValue_tv.setText(bean.getStoken_name());
        mStatements_tv.setText(bean.getFund_status());
        mCnExpendableFundValue_tv.setText(bean.getMerge_amount());
        mAmount_et.setEnabled(true);


        HashMap map = new HashMap();
        HashMap hashMap = new HashMap();
//        map.put("funcid", "300701");
//        map.put("STOCK_CODE", mEditText_et.getText().toString().trim());
//        map.put("token", mSession);
//        hashMap.put("FLAG", "true");
//        map.put("parms", hashMap);
//
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUSET && resultCode == RESULT_OK) {
            mPoint = intent.getIntExtra("point", -1);
            mInput_et.setText(intent.getStringExtra("Name"));
            intent.getStringExtra("Market");
            getAffirmMsg(intent.getStringExtra("Code"));
        }
        if (requestCode == REQUSET && resultCode == 500) {
            mAmount_et.setText("");
        }
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
        mCnFundNameValue_tv.setText("");
        mCnFundNetValueValue_tv.setText("");
        mStatements_tv.setText("");
        mCnExpendableFundValue_tv.setText("");
        mAmount_et.setText("");
    }
}
