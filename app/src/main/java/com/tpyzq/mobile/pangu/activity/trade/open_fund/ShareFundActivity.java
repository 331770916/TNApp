package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundEntity;
import com.tpyzq.mobile.pangu.data.FundRedemptionEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.FundShareDialog;
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
 * 基金分红
 */

public class ShareFundActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_choose_fund, tv_choose_shareway;
    private int point = -1;
    private int way = -1;
    private static final int POSITION_REQUSET = 1;
    private static final int HUGE_REQUSET = 2;
    private List<FundEntity> fundBeans;
    private Button bt_true;
    private String fundcode;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_choose_fund = (TextView) findViewById(R.id.tv_choose_fund);
        tv_choose_shareway = (TextView) findViewById(R.id.tv_choose_shareway);
        bt_true = (Button) findViewById(R.id.bt_true);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        tv_choose_fund.setOnClickListener(this);
        tv_choose_shareway.setOnClickListener(this);
        bt_true.setOnClickListener(this);
        fundBeans = new ArrayList<FundEntity>();
        bt_true.setClickable(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        Intent intent = getIntent();
        fundcode =  intent.getStringExtra("fundcode");
        if (!TextUtils.isEmpty(fundcode)){
            tv_choose_fund.setText(fundcode);
            bt_true.setClickable(true);
            bt_true.setBackgroundResource(R.drawable.button_login_pitchon);
            fundQuery(true);
        }else {
            fundQuery(false);
        }
    }

    /**
     * 获取基金份额
     */
    private void fundQuery(final boolean flag) {
        HashMap map720260 = new HashMap();
        map720260.put("funcid", "720260");
        map720260.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map720260_1 = new HashMap();
        map720260_1.put("SEC_ID", "tpyzq");
        map720260_1.put("FLAG", "true");
        map720260.put("parms", map720260_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map720260, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(ShareFundActivity.this,ConstantUtil.NETWORK_ERROR);
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
                        FundRedemptionEntity fundRedemptionBean = new Gson().fromJson(jsonArray.getString(0), FundRedemptionEntity.class);
                        for (int i = 0; i < fundRedemptionBean.RESULT_LIST.size(); i++) {
                            FundEntity fundBean = new FundEntity();
                            fundBean.fund_code = fundRedemptionBean.RESULT_LIST.get(i).FUND_CODE;
                            fundBean.fund_company = fundRedemptionBean.RESULT_LIST.get(i).FUND_COMPANY_NAME;
                            fundBean.fund_name = fundRedemptionBean.RESULT_LIST.get(i).FUND_NAME;
                            fundBean.auto_buy = fundRedemptionBean.RESULT_LIST.get(i).AUTO_BUY;
                            if (flag){
                                if ("0".equals(fundBean.auto_buy)){
                                    tv_choose_shareway.setText("份额分红");
                                    way = 1;
                                }else {
                                    tv_choose_shareway.setText("现金分红");
                                    way = 0;
                                }
                            }
                            fundBeans.add(fundBean);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(ShareFundActivity.this, TransactionLoginActivity.class));
                    } else {
                        CentreToast.showText(ShareFundActivity.this,msg);
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
            tv_choose_fund.setText(fundBeans.get(point).fund_code);
            if ("0".equals(fundBeans.get(point).auto_buy)){
                tv_choose_shareway.setText("份额分红");
                way = 1;
            }else {
                tv_choose_shareway.setText("现金分红");
                way = 0;
            }

            bt_true.setClickable(true);
            bt_true.setBackgroundResource(R.drawable.button_login_pitchon);

        } else if (requestCode == HUGE_REQUSET && resultCode == RESULT_OK) {
            way = intent.getIntExtra("way", -1);
            if (way == 0) {
                tv_choose_shareway.setText("现金分红");
            } else {
                tv_choose_shareway.setText("份额分红");
            }
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_share_fund;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose_fund:
                intent = new Intent();
                intent.setClass(ShareFundActivity.this, PositionFundActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("fundbean", (Serializable) fundBeans);
                bundle.putInt("point", point);
                intent.putExtras(bundle);
                startActivityForResult(intent, POSITION_REQUSET);
                break;
            case R.id.tv_choose_shareway:
                intent = new Intent();
                intent.setClass(ShareFundActivity.this, ShareWayActivity.class);
                bundle = new Bundle();
                bundle.putInt("way", way);
                intent.putExtras(bundle);
                startActivityForResult(intent, HUGE_REQUSET);
                break;
            case R.id.bt_true:
                FundShareDialog fundShareDialog;
                if (TextUtils.isEmpty(fundcode)){
                    fundShareDialog = new FundShareDialog(this,fundBeans.get(point).fund_code,way);
                }else {
                    fundShareDialog = new FundShareDialog(this,fundcode,way);
                }
                fundShareDialog.show();
                break;
        }
    }
}
