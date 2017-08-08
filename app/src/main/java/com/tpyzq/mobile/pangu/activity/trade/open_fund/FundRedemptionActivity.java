package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.FundRedemptionDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


/**
 * 基金赎回
 */
public class FundRedemptionActivity extends BaseActivity implements View.OnClickListener {
    Button bt_true;
    ImageView iv_back;
    TextView tv_choose_fund;
    EditText et_fund_code   /*基金代码*/, et_fund_sum/*赎回份额*/;
    TextView tv_fund_name, tv_fund_value, tv_redeem_sum, tv_redeem_min_sum, tv_fund_redeem_way;

    FundDataEntity fundDataBean;
    public int point = -1;
    public int way = -1;
    public static final int POSITION_REQUSET = 1;
    public static final int HUGE_REQUSET = 2;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_true = (Button) findViewById(R.id.bt_true);
        tv_choose_fund = (TextView) findViewById(R.id.tv_choose_fund);
        et_fund_code = (EditText) findViewById(R.id.et_fund_code);
        et_fund_sum = (EditText) findViewById(R.id.et_fund_sum);
        tv_fund_name = (TextView) findViewById(R.id.tv_fund_name);
        tv_fund_value = (TextView) findViewById(R.id.tv_fund_value);
        tv_redeem_sum = (TextView) findViewById(R.id.tv_redeem_sum);
        tv_redeem_min_sum = (TextView) findViewById(R.id.tv_redeem_min_sum);
        tv_fund_redeem_way = (TextView) findViewById(R.id.tv_fund_redeem_way);


        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.fundRootLayout);
        initMoveKeyBoard(rootLayout, null, et_fund_code);

        setClearView();
        initData();
    }

    private void initData() {

        iv_back.setOnClickListener(this);
        tv_choose_fund.setOnClickListener(this);
        tv_fund_redeem_way.setOnClickListener(this);
        bt_true.setOnClickListener(this);
        bt_true.setClickable(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        Intent intent = getIntent();
        String fundcode = intent.getStringExtra("fundcode");
        String fundcompany = intent.getStringExtra("fundcompany");
        if (!TextUtils.isEmpty(fundcode)) {
            et_fund_code.setText(fundcode);
            getFundData(fundcode, fundcompany);
        }
        et_fund_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String fund_code = s.toString();
                et_fund_sum.setText("");
                if (fund_code.length() == 6) {
                    et_fund_sum.setEnabled(true);
                    getFundData(fund_code, "");
                } else {
                    setClearView();
                }
            }
        });
        et_fund_sum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sum = s.toString();
                if (!TextUtils.isEmpty(sum)) {
                    bt_true.setClickable(true);
                    bt_true.setBackgroundResource(R.drawable.button_login_pitchon);
                } else {
                    bt_true.setClickable(false);
                    bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == POSITION_REQUSET && resultCode == RESULT_OK) {
            dissmissKeyboardUtil();
            point = intent.getIntExtra("point", -1);
            et_fund_code.setText(intent.getStringExtra("fund_code"));
            getFundData(intent.getStringExtra("fund_code"), intent.getStringExtra("fund_company"));
        } else if (requestCode == HUGE_REQUSET && resultCode == RESULT_OK) {
            way = intent.getIntExtra("way", -1);
            if (way == 0) {
                tv_fund_redeem_way.setText("赎回顺延");
            } else {
                tv_fund_redeem_way.setText("赎回取消");
            }
        }
    }

    /**
     * 获取基金数据
     */
    private void getFundData(String fundcode, String fundcompany) {
        HashMap map300431 = new HashMap();
        map300431.put("funcid", "300431");
        map300431.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300431_1 = new HashMap();
        map300431_1.put("SEC_ID", "tpyzq");
        map300431_1.put("FLAG", "true");
        map300431_1.put("FUND_CODE", fundcode);
        map300431_1.put("FUND_COMPANY", fundcompany);
        map300431_1.put("OPER_TYPE", "1");
        map300431.put("parms", map300431_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300431, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(FundRedemptionActivity.this, ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    if (("0").equals(code)) {
                        fundDataBean = new Gson().fromJson(response, FundDataEntity.class);
                        setTextView();
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundRedemptionActivity.this, TransactionLoginActivity.class));
                    } else {
                        CentreToast.showText(FundRedemptionActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTextView() {
        et_fund_sum.setEnabled(true);
        tv_fund_name.setText(fundDataBean.data.get(0).FUND_NAME);
        tv_fund_value.setText(fundDataBean.data.get(0).NAV);
        tv_redeem_sum.setText(fundDataBean.data.get(0).ENABLE_CNT + "\t份");
        tv_redeem_min_sum.setText(fundDataBean.data.get(0).LEASE_CNT + "\t份");
    }

    private void setClearView() {
        et_fund_sum.setEnabled(false);
        fundDataBean = new FundDataEntity();
        tv_fund_name.setText("");
        tv_fund_value.setText("");
        tv_redeem_sum.setText("");
        tv_redeem_min_sum.setText("");
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_redemption;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.tv_choose_fund:
                intent = new Intent();
                intent.setClass(this, PositionFundActivity.class);
                bundle = new Bundle();
                bundle.putInt("point", point);
                intent.putExtras(bundle);
                startActivityForResult(intent, POSITION_REQUSET);
                break;
            case R.id.tv_fund_redeem_way:
                intent = new Intent();
                intent.setClass(this, HugeRdpActivity.class);
                bundle = new Bundle();
                bundle.putInt("way", way);
                intent.putExtras(bundle);
                startActivityForResult(intent, HUGE_REQUSET);
                break;
            case R.id.bt_true:
                String fundname = "";
                String fundcode = et_fund_code.getText().toString();
                if (null != fundDataBean && null != fundDataBean.data && fundDataBean.data.size() > 0) {
                    fundname = fundDataBean.data.get(0).FUND_NAME;
                }
                String sum = et_fund_sum.getText().toString();
                FundRedemptionDialog fundRedemptionDialog = new FundRedemptionDialog(FundRedemptionActivity.this, fundname, fundcode, sum, fundRecall);
                fundRedemptionDialog.show();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    FundRedemptionDialog.FundRecall fundRecall = new FundRedemptionDialog.FundRecall() {
        @Override
        public void setRecall() {
            String fundcode = et_fund_code.getText().toString();
            String fundcompany = et_fund_code.getText().toString();
            String sum = et_fund_sum.getText().toString();
            getFundBack(fundcode, fundcompany, sum);
        }
    };

    /**
     * 基金赎回
     */
    private void getFundBack(String fundcode, String fundcompany, String sum) {
        HashMap map720203 = new HashMap();
        map720203.put("funcid", "720203");
        map720203.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map720203_1 = new HashMap();
        map720203_1.put("SEC_ID", "tpyzq");
        map720203_1.put("FLAG", "true");
        map720203_1.put("FUND_CODE", fundcode);
        map720203_1.put("FUND_COMPANY_CODE", fundcompany);
        map720203_1.put("FUND_COUNT", sum);
        if (way == 0) {
            map720203_1.put("EXCEED_FLAG", 1);
        } else {
            map720203_1.put("EXCEED_FLAG", 0);
        }
        map720203.put("parms", map720203_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map720203, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(FundRedemptionActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    if (("0").equals(code)) {
                        CentreToast.showText(FundRedemptionActivity.this, "委托已提交", true);
                        et_fund_sum.setText("");
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundRedemptionActivity.this, TransactionLoginActivity.class));
                    } else {
                        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(getFragmentManager(),FundRedemptionActivity.class.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
