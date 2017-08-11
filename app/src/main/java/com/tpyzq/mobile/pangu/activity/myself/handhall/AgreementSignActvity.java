package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.AgreementSignedEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * Created by wangqi on 2016/9/13.
 * 退市和风险警示协议签署
 */
public class AgreementSignActvity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "AgreementSignActvity";
    private AgreementSignedEntity _bean;
    private List<AgreementSignedEntity> beans;

    private String market_a_1, secu_rights_1;
    private TextView tvHuA, tvNotSatisfied2;
    private LinearLayout ic_linearLayout2;
    private ImageView ic_notSatisfied2;
    private LinearLayout isShow;
    private RelativeLayout isNodata;
    private Dialog mDialog;

    @Override
    public void initView() {
        findViewById(R.id.ASpublish_back).setOnClickListener(this);
        tvHuA = (TextView) findViewById(R.id.tvHuA);

        tvNotSatisfied2 = (TextView) findViewById(R.id.tvNotSatisfied2);

        ic_linearLayout2 = (LinearLayout) findViewById(R.id.ic_LinearLayout2);

        ic_notSatisfied2 = (ImageView) findViewById(R.id.ic_NotSatisfied2);

        isShow = (LinearLayout) findViewById(R.id.Linear_data);
        isNodata = (RelativeLayout) findViewById(R.id.stockNewsLayout);

        findViewById(R.id.btnRelativeLayout2).setOnClickListener(this);
        isNodata.setOnClickListener(this);
        isShow.setVisibility(View.GONE);
        isNodata.setVisibility(View.VISIBLE);

        mDialog = LoadingDialog.initDialog(this, "加载中...");
    }



    /**
     * 网络请求
     */
    private void toConnect() {
        if (!AgreementSignActvity.this.isFinishing()) {
            mDialog.show();
        }
        String mSession = SpUtils.getString(this, "mSession", "");
        final HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "700070");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                isShow.setVisibility(View.GONE);
                isNodata.setVisibility(View.VISIBLE);
                CentreToast.showText(AgreementSignActvity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    beans = new ArrayList<AgreementSignedEntity>();
                    beans.clear();
                    if ("0".equals(jsonObject.getString("code"))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                _bean = new AgreementSignedEntity();
                                _bean.setMarket(data.getJSONObject(i).getString("MARKET"));
                                _bean.setSecu_code(data.getJSONObject(i).getString("SECU_CODE"));
                                _bean.setSecu_rights(data.getJSONObject(i).getString("SECU_RIGHTS"));
                                _bean.setSecu_name(data.getJSONObject(i).getString("SECU_NAME"));
                                beans.add(_bean);
                            }
                        }
                        if (beans != null && beans.size() > 0) {
                            isShow.setVisibility(View.VISIBLE);
                            isNodata.setVisibility(View.GONE);
                            for (int i = 0; i < beans.size(); i++) {
                                switch (beans.get(i).getMarket()) {
                                    case "1":
                                        market_a_1 = beans.get(i).getSecu_code();
                                        tvHuA.setText("沪A-" + market_a_1);
                                        break;
                                }
                                if ("1".equals(beans.get(i).getMarket())) {
                                    secu_rights_1 = beans.get(i).getSecu_rights();
                                    if (secu_rights_1.contains("w")) {
                                        tvNotSatisfied2.setText(R.string.Signed);
                                        ic_linearLayout2.setBackgroundResource(R.drawable.mybut_1_selector);
                                        ic_notSatisfied2.setImageDrawable(getResources().getDrawable(R.mipmap.sgd));
                                        tvNotSatisfied2.setTextColor(Color.parseColor("#ffffff"));
                                    } else {
                                        tvNotSatisfied2.setText(R.string.nosign);
                                        tvNotSatisfied2.setTextColor(Color.parseColor("#ffffff"));
                                        ic_linearLayout2.setBackgroundResource(R.drawable.mybut_selector);
                                        ic_notSatisfied2.setImageDrawable(getResources().getDrawable(R.mipmap.notsatisfied));
                                    }
                                }
                            }
                        } else {
                            isShow.setVisibility(View.GONE);
                            isNodata.setVisibility(View.VISIBLE);
                        }
                    } else if ("-6".equals(jsonObject.getString("msg"))) {
                        startActivity(new Intent(AgreementSignActvity.this, TransactionLoginActivity.class));
                    } else {
                        isShow.setVisibility(View.GONE);
                        isNodata.setVisibility(View.VISIBLE);
                        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(jsonObject.getString("msg"),CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(getFragmentManager(),AgreementSignActvity.class.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_agreementsigned;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ASpublish_back:
                finish();
                break;
            case R.id.btnRelativeLayout2:
                intent.setClass(this, RiskWarningActivity.class);
                intent.putExtra("Record", secu_rights_1);
                intent.putExtra("Code", market_a_1);
                intent.putExtra("Name", beans.get(0).getSecu_name());
                startActivity(intent);
                break;
            case R.id.stockNewsLayout:
                if (!AgreementSignActvity.this.isFinishing()) {
                    mDialog.show();
                }
                toConnect();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Db_PUB_USERS.islogin()) {
            toConnect();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDialog != null && mDialog.isShowing())
                NetWorkUtil.getInstence().cancelSingleRequestByTag(TAG);
                mDialog.dismiss();
            finish();
        }
        return false;
    }
}
