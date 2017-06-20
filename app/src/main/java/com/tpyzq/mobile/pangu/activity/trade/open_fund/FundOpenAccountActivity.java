package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.PdfActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.FundCompanyEntity;
import com.tpyzq.mobile.pangu.data.FundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
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
 * 基金开户
 */
public class FundOpenAccountActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_fund_company;
    private List<FundCompanyEntity> fundCompanyBeens;
    private List<FundEntity> fundBeans;
    private Button bt_true;
    public static final int REQUSET = 1;
    public int point = -1;
    private TextView tv_notice;
    private CheckBox cb_open_fund;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_fund_company = (TextView) findViewById(R.id.tv_fund_company);
        bt_true = (Button) findViewById(R.id.bt_true);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        cb_open_fund = (CheckBox) findViewById(R.id.cb_open_fund);
        initData();
        fundQuery();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        bt_true.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
        bt_true.setClickable(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        tv_fund_company.setOnClickListener(this);
        fundCompanyBeens = new ArrayList<FundCompanyEntity>();
        fundBeans = new ArrayList<FundEntity>();
        cb_open_fund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if ("选择基金公司".equals(tv_fund_company.getHint().toString()) && TextUtils.isEmpty(tv_fund_company.getText().toString())) {
                        bt_true.setClickable(false);
                        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
                    } else {
                        bt_true.setClickable(true);
                        bt_true.setBackgroundResource(R.drawable.button_login_pitchon);
                    }
                } else {
                    bt_true.setClickable(false);
                    bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
                }
            }
        });
    }

    /**
     * 获取基金产品
     */
    private void fundQuery() {
        HashMap map300436 = new HashMap();
        map300436.put("funcid", "300436");
        map300436.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300436_1 = new HashMap();
        map300436_1.put("SEC_ID", "tpyzq");
        map300436_1.put("FLAG", "true");
        map300436.put("parms", map300436_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300436, new StringCallback() {
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
                        FundCompanyEntity fundCompanyBean;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            fundCompanyBean = new Gson().fromJson(jsonArray.getString(i), FundCompanyEntity.class);
                            fundCompanyBeens.add(fundCompanyBean);
                        }
                        FundEntity fundBean;
                        for (int i = 0; i < fundCompanyBeens.size(); i++) {
                            fundBean = new FundEntity();
                            fundBean.company_code = fundCompanyBeens.get(i).FUND_COMPANY;
                            fundBean.fund_company = fundCompanyBeens.get(i).EXCHANGE_NAME;
                            fundBeans.add(fundBean);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundOpenAccountActivity.this, TransactionLoginActivity.class));
                    } else {
                        MistakeDialog.showDialog(msg, FundOpenAccountActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fund_open(String company_code) {
        HashMap map300436 = new HashMap();
        map300436.put("funcid", "720140");
        map300436.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300436_1 = new HashMap();
        map300436_1.put("SEC_ID", "tpyzq");
        map300436_1.put("FLAG", "true");
        map300436_1.put("FUND_COMPANY", company_code);
        map300436.put("parms", map300436_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300436, new StringCallback() {
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
                        MistakeDialog.showDialog("开户成功", FundOpenAccountActivity.this);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundOpenAccountActivity.this, TransactionLoginActivity.class));
                    } else {
                        MistakeDialog.showDialog(msg, FundOpenAccountActivity.this);
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
        if (requestCode == REQUSET && resultCode == RESULT_OK) {
            point = intent.getIntExtra("point", -1);
            tv_fund_company.setText(fundBeans.get(point).fund_company);
            if (cb_open_fund.isChecked()) {
                bt_true.setClickable(true);
                bt_true.setBackgroundResource(R.drawable.button_login_pitchon);
            } else {
                bt_true.setClickable(false);
                bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_fund_company:
                intent = new Intent();
                intent.setClass(this, FundCompanyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fundbean", (Serializable) fundBeans);
                bundle.putInt("point", point);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bt_true:
                fund_open(fundBeans.get(point).company_code);
                break;
            case R.id.tv_notice:
                String path = Helper.getAppFileDirPath(CustomApplication.getContext()) + "pdf/fundOpenUser.pdf";
                intent = new Intent();
                intent.setClass(FundOpenAccountActivity.this, PdfActivity.class);
                intent.putExtra("filePath", path);
                intent.putExtra("fileName", "基金投资人权益须知");
                startActivity(intent);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_open_account;
    }
}
