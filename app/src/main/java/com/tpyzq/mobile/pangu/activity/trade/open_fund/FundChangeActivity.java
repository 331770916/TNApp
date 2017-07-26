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
import android.widget.Toast;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundChangeEntity;
import com.tpyzq.mobile.pangu.data.FundEntity;
import com.tpyzq.mobile.pangu.data.FundRedemptionEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.dialog.FundChangeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 基金转换
 */
public class FundChangeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_choose_fund;        //选择持仓基金
    private TextView tv_choose_shareway;     //选择基金产品
    private TextView tv_fund_code1;          //转出基金代码
    private TextView et_fund_code2;          //转入基金代码
    private EditText et_fund_fene;           //转换份额
    private ImageView iv_back;                //返回按键
    private Button bt_true;                    //确定按钮
    private FundRedemptionEntity fundRedemptionBean;  //持仓对象
    private List<FundEntity> fundBeans;
    private int point = -1;
    private int point2 = -1;
    private static final int POSITION_REQUSET = 1;
    private static final int POSITION_REQUSET2 = 2;
    private LinearLayout ll_output;
    private LinearLayout ll_input;
    private TextView tv_output_nav;
    private TextView tv_output_share;
    private TextView tv_input_nav;
    private FundChangeEntity fundChangeBean;
    private ArrayList<FundChangeEntity> fundChangeBeans;
    private FundChangeClear fundChangeClear;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_choose_fund = (TextView) findViewById(R.id.tv_choose_fund);
        tv_choose_shareway = (TextView) findViewById(R.id.tv_choose_shareway);
        tv_fund_code1 = (TextView) findViewById(R.id.tv_fund_code1);
        et_fund_code2 = (TextView) findViewById(R.id.et_fund_code2);
        et_fund_fene = (EditText) findViewById(R.id.et_fund_fene);
        bt_true = (Button) findViewById(R.id.bt_true);
        ll_output = (LinearLayout) findViewById(R.id.ll_output);
        ll_input = (LinearLayout) findViewById(R.id.ll_input);
        tv_output_nav = (TextView) findViewById(R.id.tv_output_nav);
        tv_output_share = (TextView) findViewById(R.id.tv_output_share);
        tv_input_nav = (TextView) findViewById(R.id.tv_input_nav);
        initData();
    }

    private void initData() {
        fundChangeClear = new FundChangeClear() {
            @Override
            public void setClear() {
                et_fund_fene.setText("");
            }
        };
        fundChangeBeans = new ArrayList<>();
        fundChangeBean = new FundChangeEntity();
        fundBeans = new ArrayList<>();
        tv_choose_fund.setOnClickListener(this);
        tv_choose_shareway.setOnClickListener(this);
        bt_true.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        bt_true.setClickable(false);
        tv_choose_shareway.setClickable(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        et_fund_fene.addTextChangedListener(new FundFeneTextWatch());
        et_fund_fene.setEnabled(false);
        Intent intent = getIntent();
        String fundcode =  intent.getStringExtra("fundcode");
        String fundcompany =  intent.getStringExtra("fundcompany");
        if (!TextUtils.isEmpty(fundcode)){
            tv_fund_code1.setText(fundcode);
            ll_output.setVisibility(View.VISIBLE);
            fundChange(fundcode, fundcompany);
            fundQuery(fundcode,fundcompany);
            tv_choose_shareway.setClickable(true);
        }
    }

    /**
     * 获取基金份额
     */
    private void fundQuery(final String fundcode, String fundcompany) {
        HashMap map720260 = new HashMap();
        map720260.put("funcid", "720260");
        map720260.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map720260_1 = new HashMap();
        map720260_1.put("SEC_ID", "tpyzq");
        map720260_1.put("FUND_CODE", fundcode);
        map720260_1.put("FUND_COMPANY_CODE", fundcompany);
        map720260_1.put("FLAG", "true");
        map720260.put("parms", map720260_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map720260, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogUtil.e(response);
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        fundRedemptionBean = new Gson().fromJson(jsonArray.getString(0), FundRedemptionEntity.class);
                        for (int i = 0; i < fundRedemptionBean.RESULT_LIST.size(); i++) {
                            FundEntity fundBean = new FundEntity();
                            fundBean.fund_code = fundRedemptionBean.RESULT_LIST.get(i).FUND_CODE;
                            fundBean.fund_company = fundRedemptionBean.RESULT_LIST.get(i).FUND_COMPANY_NAME;
                            fundBean.fund_name = fundRedemptionBean.RESULT_LIST.get(i).FUND_NAME;
                            fundBean.fund_nav = fundRedemptionBean.RESULT_LIST.get(i).NAV;
                            fundBean.fund_share = fundRedemptionBean.RESULT_LIST.get(i).CURRENT_SHARE;
                            fundBeans.add(fundBean);
                        }
                        if (!TextUtils.isEmpty(fundcode)){
                            tv_output_nav.setText("转出净值:\t\t\t" + fundBeans.get(0).fund_nav);
                            tv_output_share.setText("可转份额:\t\t\t" + fundBeans.get(0).fund_share);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundChangeActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(FundChangeActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取基金份额
     */
    private void fundChange(String code, String company) {
        HashMap map300443 = new HashMap();
        map300443.put("funcid", "300443");
        map300443.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300443_1 = new HashMap();
        map300443_1.put("SEC_ID", "tpyzq");
        map300443_1.put("FUND_COMPANY", company);
        map300443_1.put("FUND_CODE", code);
        map300443_1.put("FLAG", "true");
        map300443.put("parms", map300443_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300443, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Helper.getInstance().showToast(FundChangeActivity.this,ConstantUtil.NETWORK_ERROR);
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
                            FundChangeEntity fundChangeBean = new Gson().fromJson(jsonArray.getString(i), FundChangeEntity.class);
                            fundChangeBeans.add(fundChangeBean);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundChangeActivity.this, TransactionLoginActivity.class));
                        finish();
                    } else {
                        ToastUtils.showShort(FundChangeActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == POSITION_REQUSET && resultCode == RESULT_OK) {
            point = intent.getIntExtra("point", -1);
            tv_fund_code1.setText(intent.getStringExtra("fund_code"));

            ll_output.setVisibility(View.VISIBLE);
            tv_output_nav.setText("转出净值:\t\t\t" + intent.getStringExtra("fund_nav"));
            tv_output_share.setText("可转份额:\t\t\t" + intent.getStringExtra("fund_share"));
            fundChange(intent.getStringExtra("fund_code"),intent.getStringExtra("fund_company"));
            tv_choose_shareway.setClickable(true);

        } else if (requestCode == POSITION_REQUSET2 && resultCode == RESULT_OK) {
            et_fund_fene.setEnabled(true);
            point2 = intent.getIntExtra("point2", -1);
            et_fund_code2.setText(fundChangeBeans.get(point2).FUND_CODE);
            ll_input.setVisibility(View.VISIBLE);
            tv_input_nav.setText("转入净值:\t\t\t" + fundChangeBeans.get(point).FUND_VAL);

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_change;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.tv_choose_fund:
                intent = new Intent();
                intent.setClass(FundChangeActivity.this, PositionFundActivity.class);
                bundle = new Bundle();
                bundle.putInt("point", point);
                intent.putExtras(bundle);
                startActivityForResult(intent, POSITION_REQUSET);
                break;
            case R.id.tv_choose_shareway:
                intent = new Intent();
                intent.setClass(FundChangeActivity.this, PositionFund2Activity.class);
                bundle = new Bundle();
                bundle.putSerializable("fundChangeBeans", (Serializable) fundChangeBeans);
                bundle.putInt("point2", point2);
                intent.putExtras(bundle);
                startActivityForResult(intent, POSITION_REQUSET2);
                break;
            case R.id.bt_true:
                FundChangeDialog fundChangeDialog = new FundChangeDialog(this, fundChangeClear, tv_fund_code1.getText().toString(), et_fund_code2.getText().toString(), et_fund_fene.getText().toString());
                fundChangeDialog.show();
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    private class FundFeneTextWatch implements TextWatcher {

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
            }
        }
    }

    public interface FundChangeClear {
        void setClear();
    }
}
