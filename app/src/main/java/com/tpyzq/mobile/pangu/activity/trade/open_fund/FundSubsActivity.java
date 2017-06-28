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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskConfirmActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ChangeAccoutActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.FundEntity;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.data.SubsStatusEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.FundSubsDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils.encryptBySessionKey;

/**
 * 基金认购
 */
public class FundSubsActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_fund_code/* 输入基金代码 */, et_rengou_price/* 输入认购金额 */;
    private TextView tv_fund_name/* 基金名称 */, tv_netvalue/* 基金净值 */, tv_lowest_investment/* 个人最低投资 */, tv_usable_money/* 可用资金 */, tv_choose_fund/* 选择基金产品 */;
    private Button bt_true/* 确定按钮 */;
    private List<FundSubsEntity> fundSubsBeans;
    private FundSubsEntity fundSubsBean;
    private FundDataEntity fundDataBean;
    private ImageView iv_back;//退出
    public static final int REQUSET = 1;//进入产品列表和签署协议界面
    public int point = -1;
    private FundEntity fundBean;
    private List<FundEntity> fundBeans;
    private SubsStatusEntity subsStatusBean;
    private AssessConfirmEntity assessConfirmBean;
    private String fundcode;
    private String session;
    private FundSubsListen fundSubsListen;
    private static int REQUESTCODE = 1001; //进入风险确认页面的请求码
    private static int REQAGREEMENTCODE = 1002; //进入签署协议页面的请求码

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_fund_code = (EditText) findViewById(R.id.et_fund_code);
        et_rengou_price = (EditText) findViewById(R.id.et_rengou_price);
        tv_fund_name = (TextView) findViewById(R.id.tv_fund_name);
        tv_netvalue = (TextView) findViewById(R.id.tv_netvalue);
        tv_lowest_investment = (TextView) findViewById(R.id.tv_lowest_investment);
        tv_usable_money = (TextView) findViewById(R.id.tv_usable_money);
        bt_true = (Button) findViewById(R.id.bt_true);
        tv_choose_fund = (TextView) findViewById(R.id.tv_choose_fund);
        initData();
        fundQuery();
    }

    /**
     * 获取基金产品
     */
    private void fundQuery() {
        HashMap map300441 = new HashMap();
        map300441.put("funcid", "300441");
        map300441.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300441_1 = new HashMap();
        map300441_1.put("SEC_ID", "tpyzq");
        map300441_1.put("FUND_TYPE", "1");
        map300441_1.put("FLAG", "true");
        map300441.put("parms", map300441_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300441, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            fundSubsBean = new Gson().fromJson(jsonArray.getString(i), FundSubsEntity.class);
                            fundSubsBeans.add(fundSubsBean);
                        }
                        for (int i = 0; i < fundSubsBeans.size(); i++) {
                            fundBean = new FundEntity();
                            fundBean.fund_code = fundSubsBeans.get(i).FUND_CODE;
                            fundBean.fund_name = fundSubsBeans.get(i).FUND_NAME;
                            fundBean.fund_company = fundSubsBeans.get(i).FUND_COMPANY;
                            fundBeans.add(fundBean);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundSubsActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(FundSubsActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        map300431_1.put("OPER_TYPE", "0");
        map300431.put("parms", map300431_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300431, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(FundSubsActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
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
                        setTextView(fundDataBean);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundSubsActivity.this, TransactionLoginActivity.class));
                    } else {
                        clearView();
                        ToastUtils.showShort(FundSubsActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取基金认购购买
     */
    private void buy_rengou(final String price, final String fund_company) {
        HashMap map300439 = new HashMap();
        map300439.put("funcid", "300439");
        map300439.put("token", session);
        map300439.put("secret", UserUtil.Keyboard);
        HashMap map300439_1 = new HashMap();
        map300439_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        map300439_1.put("FUND_COMPANY", encryptBySessionKey(fund_company));
        map300439_1.put("FUND_CODE", encryptBySessionKey(fundcode));
        map300439_1.put("BUY_AMOUNT", encryptBySessionKey(price));
        map300439_1.put("FLAG", encryptBySessionKey("true"));
        map300439_1.put("DO_OPEN", encryptBySessionKey(""));
        map300439_1.put("DO_CONTRACT", encryptBySessionKey(""));
        map300439_1.put("DO_PRE_CONDITION", encryptBySessionKey("1"));
        map300439.put("parms", map300439_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300439, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    if ("0".equals(code)) {
                        subsStatusBean = new Gson().fromJson(response, SubsStatusEntity.class);
                        //判断是否跳转风险评测界面
                        if (!TextUtils.isEmpty(subsStatusBean.data.get(0).IS_ABLE) && "0".equals(subsStatusBean.data.get(0).IS_ABLE)) {
                            startFinish();
                        } else {
                            assessConfirmBean = new AssessConfirmEntity();
                            assessConfirmBean.productcode = fundcode;
                            assessConfirmBean.productcompany = fund_company;
                            assessConfirmBean.productprice = price;
                            assessConfirmBean.type = "1";
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
                            intent.setClass(FundSubsActivity.this, AssessConfirmActivity.class);
                            intent.putExtra("transaction", "true");
                            intent.putExtra("assessConfirm", assessConfirmBean);
                            startActivityForResult(intent, REQAGREEMENTCODE);
                        }
                    } else {
                        subsStatusBean = new Gson().fromJson(response, SubsStatusEntity.class);
                        ToastUtils.showShort(FundSubsActivity.this, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTextView(FundDataEntity fundDataBean) {
        et_rengou_price.setEnabled(true);
        fundcode = fundDataBean.data.get(0).FUND_CODE;
        tv_fund_name.setText("基金名称\t\t" + fundDataBean.data.get(0).FUND_NAME);
        tv_netvalue.setText("基金净值\t\t" + fundDataBean.data.get(0).NAV);
        tv_lowest_investment.setText("个人最低投资\t\t" + fundDataBean.data.get(0).OPEN_SHARE + "\t元");
        tv_usable_money.setText("可用投资\t\t" + fundDataBean.data.get(0).ENABLE_BALANCE + "\t元");
    }

    /**
     * 初始化布局逻辑
     */
    private void initData() {
        tv_choose_fund.setOnClickListener(this);
        bt_true.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        bt_true.setClickable(false);
        et_rengou_price.setEnabled(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        et_rengou_price.addTextChangedListener(new PriceWatch());
        fundSubsBeans = new ArrayList<FundSubsEntity>();
        fundBeans = new ArrayList<FundEntity>();
        session = SpUtils.getString(this, "mSession", null);
        fundSubsListen = new FundSubsListen() {
            @Override
            public void setBuy(String price, String fund_company) {
                Intent intent = new Intent(FundSubsActivity.this, RiskConfirmActivity.class);
                intent.putExtra("fundSubsBean",fundDataBean);
                intent.putExtra("from","fundSubs");
                FundSubsActivity.this.startActivityForResult(intent, REQUESTCODE);
            }
        };
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
                    et_rengou_price.setText("");
                } else {
                    bt_true.setClickable(false);
                    bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
                }
            }
        });
    }

    private void clearView() {
        et_fund_code.setText("");
        et_rengou_price.setText("");
        tv_fund_name.setText("基金名称");
        tv_netvalue.setText("基金净值");
        tv_lowest_investment.setText("个人最低投资");
        tv_usable_money.setText("可用投资");
        bt_true.setClickable(false);
        et_rengou_price.setEnabled(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
    }

    public void startFinish() {
        ToastUtils.showShort(FundSubsActivity.this, "委托成功");
        clearView();
    }


    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_subs;
    }

    /**
     * 此界面的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_choose_fund:
                intent.setClass(this, FundProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fundbean", (Serializable) fundBeans);
                bundle.putInt("point", point);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bt_true:
                if (Helper.getInstance().isNeedShowRiskDialog()) {
                    Helper.getInstance().showCorpDialog(this, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            Intent intent = new Intent(FundSubsActivity.this, RiskEvaluationActivity.class);
                            intent.putExtra("isLogin", true);
                            FundSubsActivity.this.startActivity(intent);
                        }
                    }, new CancelDialog.NagtiveClickListener() {
                        @Override
                        public void onNagtiveClick() {
                            FundSubsDialog dialog = new FundSubsDialog(FundSubsActivity.this, fundDataBean, et_rengou_price.getText().toString(), fundSubsListen);
                            dialog.show();
                        }
                    });
                } else {
                    FundSubsDialog dialog = new FundSubsDialog(this, fundDataBean, et_rengou_price.getText().toString(), fundSubsListen);
                    dialog.show();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUSET && resultCode == RESULT_OK) {//产品列表返回
            point = intent.getIntExtra("point", -1);
            et_fund_code.setText(fundSubsBeans.get(point).FUND_CODE);
            getFundData(fundSubsBeans.get(point).FUND_CODE, fundSubsBeans.get(point).FUND_COMPANY);
        }
        if (requestCode == REQAGREEMENTCODE && resultCode == RESULT_OK) {//签署协议页面返回
            et_rengou_price.setText("");
        }
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {//风险同意书签署返回
            buy_rengou(et_rengou_price.getText().toString().trim(),fundDataBean.data.get(0).FUND_COMPANY);
        }
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

    public interface FundSubsListen {
        void setBuy(String price, String fund_company);
    }
}
