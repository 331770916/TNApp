package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskConfirmActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.data.SubsStatusEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.FundPurchaseDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils.encryptBySessionKey;
import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * 基金申购
 */
public class FundPurchaseActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUESTCODE = 1001;//进入风险确认页面的请求码
    private static int REQAGREEMENTCODE = 1002; //进入签署协议页面的请求码
    private TextView tv_choose_fund/*选择基金产品*/, tv_fund_name/*基金名称*/, tv_fund_value/*基金净值*/, tv_low_money/*个人最低投资*/, tv_usable_money/*可用资金*/;
    private ImageView iv_back/*返回*/;
    private EditText et_fund_code/*基金代码*/, et_fund_price/*申购金额*/;
    private static int REQUEST = 5; //Activity请求码
    private FundDataEntity fundDataBean;      //基金信息
    private Button bt_true;
    private FundSubsEntity fundData;
    private AssessConfirmEntity assessConfirmBean;
    private SubsStatusEntity subsStatusBean;

    @Override
    public void initView() {
        tv_choose_fund = (TextView) findViewById(R.id.tv_choose_fund);
        tv_fund_name = (TextView) findViewById(R.id.tv_fund_name);
        tv_fund_value = (TextView) findViewById(R.id.tv_fund_value);
        tv_low_money = (TextView) findViewById(R.id.tv_low_money);
        tv_usable_money = (TextView) findViewById(R.id.tv_usable_money);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_fund_code = (EditText) findViewById(R.id.et_fund_code);
        et_fund_price = (EditText) findViewById(R.id.et_fund_price);
        bt_true = (Button) findViewById(R.id.bt_true);

        initData();
    }

    private void initData() {
        tv_choose_fund.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        et_fund_price.setEnabled(false);
        bt_true.setOnClickListener(FundPurchaseActivity.this);
        bt_true.setClickable(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        et_fund_price.addTextChangedListener(new PriceWatch());
        et_fund_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String fundcode = s.toString();
                if (!TextUtils.isEmpty(fundcode) && s.length() == 6) {
                    getFundData(fundcode, "");
                    et_fund_price.setText("");
                } else {
                    bt_true.setClickable(false);
                    bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_fund:
                startActivityForResult(new Intent(this, OptionalFundActivity.class), REQUEST);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_true:
                FundPurchaseDialog fundPurchaseDialog = new FundPurchaseDialog(this, fundData, et_fund_price.getText().toString(), fundPurchaseListen);
                fundPurchaseDialog.show();
                break;
        }
    }


    private FundPurchaseDialog.FundPurchaseListen fundPurchaseListen = new FundPurchaseDialog.FundPurchaseListen() {

        @Override
        public void setEntrust(String price, String fund_company, String fund_code) {
//            buy_shengou(price, fund_company, fund_code);
            if (Helper.isGoToActivity(FundPurchaseActivity.this,null)) {
                Intent intent = new Intent(FundPurchaseActivity.this, RiskConfirmActivity.class);
                intent.putExtra("fundData",fundData);
                intent.putExtra("from","fundPurchase");
                FundPurchaseActivity.this.startActivityForResult(intent, REQUESTCODE);
            }

        }
    };

    /**
     * 获取基金申购购买
     */
    private void buy_shengou(final String price, final String fund_company, String fund_code) {
        HashMap map300440 = new HashMap();
        map300440.put("funcid", "300440");
        map300440.put("token", SpUtils.getString(FundPurchaseActivity.this, "mSession", null));
        map300440.put("secret", UserUtil.Keyboard);
        HashMap map300440_1 = new HashMap();
        map300440_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        map300440_1.put("FUND_COMPANY", encryptBySessionKey(fund_company));
        map300440_1.put("FUND_CODE", encryptBySessionKey(fund_code));
        map300440_1.put("BUY_AMOUNT", encryptBySessionKey(price));
//        map300440_1.put("FUND_COMPANY", encryptBySessionKey("01"));
//        map300440_1.put("FUND_CODE", encryptBySessionKey("000326"));
//        map300440_1.put("BUY_AMOUNT", encryptBySessionKey("10000"));
        map300440_1.put("FLAG", encryptBySessionKey("true"));
        map300440_1.put("DO_OPEN", encryptBySessionKey(""));
        map300440_1.put("DO_CONTRACT", encryptBySessionKey(""));
        map300440_1.put("DO_PRE_CONDITION", encryptBySessionKey("1"));
        map300440.put("parms", map300440_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300440, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(context, "委托失败");
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
                    if ("0".equals(code)) {
                        subsStatusBean = new Gson().fromJson(response, SubsStatusEntity.class);
                        //判断是否跳转风险评测界面
                        if (!TextUtils.isEmpty(subsStatusBean.data.get(0).IS_ABLE) && "0".equals(subsStatusBean.data.get(0).IS_ABLE)) {
                            startFinish();
                        } else {
                            assessConfirmBean = new AssessConfirmEntity();
                            assessConfirmBean.productcode = fundData.FUND_CODE;
                            assessConfirmBean.productcompany = fund_company;
                            assessConfirmBean.productprice = price;
                            assessConfirmBean.type = "2";
                            assessConfirmBean.IS_ABLE = subsStatusBean.data.get(0).IS_ABLE;
                            String IS_AGREEMENT = subsStatusBean.data.get(0).IS_AGREEMENT;
                            assessConfirmBean.IS_AGREEMENT = IS_AGREEMENT;
                            assessConfirmBean.IS_OPEN = subsStatusBean.data.get(0).IS_OPEN;
                            assessConfirmBean.IS_VALIB_RISK_LEVEL = subsStatusBean.data.get(0).IS_VALIB_RISK_LEVEL;
                            assessConfirmBean.OFRISK_FLAG = subsStatusBean.data.get(0).OFRISK_FLAG;
                            assessConfirmBean.OFUND_RISKLEVEL_NAME = subsStatusBean.data.get(0).OFUND_RISKLEVEL_NAME;
                            assessConfirmBean.RISK_LEVEL = subsStatusBean.data.get(0).RISK_LEVEL;
                            assessConfirmBean.RISK_LEVEL_NAME = subsStatusBean.data.get(0).RISK_LEVEL_NAME;
                            assessConfirmBean.RISK_RATING = subsStatusBean.data.get(0).RISK_RATING;

                            Intent intent = new Intent();
                            intent.setClass(FundPurchaseActivity.this, AssessConfirmActivity.class);
                            intent.putExtra("assessConfirm", assessConfirmBean);
                            intent.putExtra("transaction", "true");
                            startActivityForResult(intent, REQAGREEMENTCODE);
                        }
                    } else if ("-6".equals(code)) {
                        FundPurchaseActivity.this.startActivity(new Intent(FundPurchaseActivity.this, TransactionLoginActivity.class));
                    } else {
                        MistakeDialog.showDialog(response, (Activity) FundPurchaseActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearView() {
        et_fund_code.setText("");
        et_fund_price.setText("");
        tv_fund_name.setText("基金名称");
        tv_fund_value.setText("基金净值");
        tv_low_money.setText("个人最低投资");
        tv_usable_money.setText("可用投资");
        bt_true.setClickable(false);
        et_fund_price.setEnabled(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
    }

    private void startFinish() {
        ToastUtils.showShort(FundPurchaseActivity.this, "委托成功");
        clearView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            fundData = (FundSubsEntity) data.getSerializableExtra("data");
            et_fund_code.setText(fundData.FUND_CODE);
            getFundData(fundData.FUND_CODE, fundData.FUND_COMPANY_NAME);
        }

        if (requestCode == REQAGREEMENTCODE && resultCode == RESULT_OK) {//签署协议页面返回
            et_fund_price.setText("");
        }
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {//风险同意书签署返回
            buy_shengou(et_fund_price.getText().toString().trim(),fundData.FUND_COMPANY,fundData.FUND_CODE);
        }
    }

    /**
     * 获取基金数据
     */
    private void getFundData(final String fundcode, String fundcompany) {
        HashMap map300431 = new HashMap();
        map300431.put("funcid", "300431");
        map300431.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300431_1 = new HashMap();
        map300431_1.put("SEC_ID", "tpyzq");
        map300431_1.put("FLAG", "true");
        map300431_1.put("FUND_CODE", fundcode);
        map300431_1.put("FUND_COMPANY", fundcompany);
        map300431_1.put("OPER_TYPE", "0");
        map300431.put("parms", map300431_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300431, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(FundPurchaseActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
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
                    if (code.equals("0")) {
                        fundDataBean = new Gson().fromJson(response, FundDataEntity.class);
                        setTextView(fundDataBean);
                        fundData = new FundSubsEntity();
                        fundData.FUND_CODE = fundDataBean.data.get(0).FUND_CODE;
                        fundData.FUND_COMPANY = fundDataBean.data.get(0).FUND_COMPANY;
                        fundData.FUND_NAME = fundDataBean.data.get(0).FUND_NAME;
                    } else {
                        clearView();
                        ToastUtils.showShort(FundPurchaseActivity.this, msg);
//                        startActivity(new Intent(FundSubsActivity.this, TransactionLoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTextView(FundDataEntity fundDataBean) {
        et_fund_price.setEnabled(true);
        tv_fund_name.setText("基金名称\t\t" + fundDataBean.data.get(0).FUND_NAME);
        tv_fund_value.setText("基金净值\t\t" + fundDataBean.data.get(0).NAV);
        tv_low_money.setText("个人最低投资\t\t" + fundDataBean.data.get(0).OPEN_SHARE + "\t元");
        tv_usable_money.setText("可用投资\t\t" + fundDataBean.data.get(0).ENABLE_BALANCE + "\t元");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_purchase;
    }

    class PriceWatch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                bt_true.setClickable(true);
                bt_true.setBackgroundResource(R.drawable.button_login_pitchon);
            } else {
                bt_true.setClickable(false);
                bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
            }
        }
    }
}
