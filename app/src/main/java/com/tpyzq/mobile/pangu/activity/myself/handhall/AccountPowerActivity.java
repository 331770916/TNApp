package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.MyAccoutPower;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.progress.ColorArcProgressBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


/**
 * 账户全景图
 */
public class AccountPowerActivity extends BaseActivity implements View.OnClickListener {
    private ColorArcProgressBar pb_power;
    private MyAccoutPower myAccoutPower;
    private RatingBar rb_star;
    private int num = 0;
    private ImageView iv_back;
    private TextView tv_huA,    /*沪A股东账户*/
            tv_shenA,/*深A股东账户*/
            tv_chuangye,/*创业板*/
//            tv_otc,/*OTC*/
            tv_fengxian,/*风险评测*/
            tv_hetong,/*电子签名约定书*/
//            tv_contacts,/*第二联系人*/
            tv_identity;/*身份证状态*/

    @Override
    public void initView() {
        pb_power = (ColorArcProgressBar) findViewById(R.id.pb_power);
        rb_star = (RatingBar) findViewById(R.id.rb_star);
        tv_huA = (TextView) findViewById(R.id.tv_huA);
        tv_shenA = (TextView) findViewById(R.id.tv_shenA);
        tv_chuangye = (TextView) findViewById(R.id.tv_chuangye);
//        tv_otc = (TextView) findViewById(R.id.tv_otc);
        tv_fengxian = (TextView) findViewById(R.id.tv_fengxian);
        tv_hetong = (TextView) findViewById(R.id.tv_hetong);
//        tv_contacts = (TextView) findViewById(tv_contacts);
        tv_identity = (TextView) findViewById(R.id.tv_identity);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        initData();
        getServer1();
    }

    private void initData() {
        myAccoutPower = new MyAccoutPower();
        iv_back.setOnClickListener(this);
        tv_hetong.setOnClickListener(this);
//        tv_contacts = (TextView) findViewById(tv_contacts);
        tv_fengxian.setOnClickListener(this);
        tv_identity.setOnClickListener(this);
    }

    private void getServer1() {
        UserUtil.refrushUserInfo();
        //账户资料完整性
        HashMap mapHQTNG010 = new HashMap();
        mapHQTNG010.put("FUNCTIONCODE", "HQTNG010");
        mapHQTNG010.put("TOKEN", SpUtils.getString(AccountPowerActivity.this, "mSession", null));
        HashMap mapHQTNG010_1 = new HashMap();
        mapHQTNG010_1.put("cust_id", UserUtil.capitalAccount);
        mapHQTNG010.put("PARAMS", mapHQTNG010_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_HQ_BB(), mapHQTNG010, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(AccountPowerActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(response);
                    String msg = json.getString("MSG");
                    String code = json.getString("CODE");
                    String data = json.getString("DATA");
                    if ("200".equals(code)) {
                        JSONObject jsonObject = new JSONObject(data);
                        myAccoutPower.idcard1 = jsonObject.getString("is_id_complete");        //身份证是否需要升位
                        myAccoutPower.idcard2 = jsonObject.getString("is_id_overdue");         //身份证是否过期
                        myAccoutPower.stipulator = jsonObject.getString("is_sign_agreement");  //电子签名约定书是否已签署
                        myAccoutPower.risk1 = jsonObject.getString("is_risk_appraisal");       //风险测评是否完成
                        myAccoutPower.risk2 = jsonObject.getString("is_risk_overdue");         //风险测评是否过期
                        getServer2();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getServer2() {
        //账户权限开通
        HashMap mapHQTNG011 = new HashMap();
        mapHQTNG011.put("FUNCTIONCODE", "HQTNG011");
        mapHQTNG011.put("TOKEN", SpUtils.getString(AccountPowerActivity.this, "mSession", null));
        HashMap mapHQTNG011_1 = new HashMap();
        mapHQTNG011_1.put("cust_id", UserUtil.capitalAccount);
        mapHQTNG011.put("PARAMS", mapHQTNG011_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_HQ_BB(), mapHQTNG011, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(AccountPowerActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(response);
                    String msg = json.getString("MSG");
                    String code = json.getString("CODE");
                    String data = json.getString("DATA");
                    if ("200".equals(code)) {
                        JSONObject jsonObject = new JSONObject(data);
                        myAccoutPower.huA = jsonObject.getString("sh_a_security_account");
                        myAccoutPower.shenA = jsonObject.getString("sz_a_security_account");
                        myAccoutPower.startupboard = jsonObject.getString("is_open_gem");
                        myAccoutPower.otc = jsonObject.getString("is_open_otc");
//                        getServer3();
                        setView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    private void getServer3() {
//        String mSession = SpUtils.getString(this, "mSession", "");
//        HashMap map700010 = new HashMap();
//        HashMap map700010_1 = new HashMap();
//        map700010.put("funcid", "700010");  //700010  300040
//        map700010.put("token", mSession);
//        map700010.put("parms", map700010_1);
//        map700010_1.put("SEC_ID", "tpyzq");
//        map700010_1.put("FLAG", "true");
//
//        NetWorkUtil.getInstence().okHttpForPostString("", FileUtil.URL_JY, map700010, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                LogHelper.e("", e.toString());
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                if (TextUtils.isEmpty(response)) {
//                    return;
//                }
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String data = jsonObject.getString("data");
//                    String msg = jsonObject.getString("msg");
//                    String code = jsonObject.getString("code");
//                    if ("0".equals(code)){
//                        JSONArray jsonArray = new JSONArray(data);
//                        myAccoutPower.contacts = jsonArray.getJSONObject(0).getString("SECOND_MOBILE");
//                        setView();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void setView() {
        Drawable lan= getResources().getDrawable(R.mipmap.gouxuan_lan);
        lan.setBounds(0, 0, lan.getMinimumWidth(), lan.getMinimumHeight());
        Drawable hui= getResources().getDrawable(R.mipmap.gouxuan_hui);
        hui.setBounds(0, 0, hui.getMinimumWidth(), hui.getMinimumHeight());

        if (!TextUtils.isEmpty(myAccoutPower.huA)) {
            tv_huA.setText("已开户");
            tv_huA.setTextColor(ColorUtils.GRAY);
            tv_huA.setCompoundDrawables(lan,null,null,null);
            num++;
        } else {
            tv_huA.setText(Html.fromHtml("<u>" + "未开户" + "</u>"));
            tv_huA.setCompoundDrawables(hui,null,null,null);
        }
        if (!TextUtils.isEmpty(myAccoutPower.shenA)) {
            tv_shenA.setText("已开户");
            tv_shenA.setTextColor(ColorUtils.GRAY);
            tv_shenA.setCompoundDrawables(lan,null,null,null);
            num++;
        } else {
            tv_shenA.setText(Html.fromHtml("<u>" + "未开户" + "</u>"));
            tv_shenA.setCompoundDrawables(hui,null,null,null);
        }
        if (!TextUtils.isEmpty(myAccoutPower.startupboard) && "002".equals(myAccoutPower.startupboard)) {
            tv_chuangye.setText("已开户");
            tv_chuangye.setTextColor(ColorUtils.GRAY);
            tv_chuangye.setCompoundDrawables(lan,null,null,null);
            num++;
        } else {
            tv_chuangye.setText(Html.fromHtml("<u>" + "未开户" + "</u>"));
            tv_chuangye.setCompoundDrawables(hui,null,null,null);
        }
//        if (!TextUtils.isEmpty(myAccoutPower.otc) && "002".equals(myAccoutPower.otc)) {
//            tv_otc.setText("已开户");
//            tv_otc.setTextColor(ColorUtils.GRAY);
//            tv_otc.setCompoundDrawables(lan,null,null,null);
//            num++;
//        } else {
//            tv_otc.setText(Html.fromHtml("<u>" + "未开户，去开户" + "</u>"));
//            tv_otc.setCompoundDrawables(hui,null,null,null);
//        }

        if ("002".equals(myAccoutPower.risk1) && "001".equals(myAccoutPower.risk2)) {
            tv_fengxian.setText("已评测");
            tv_fengxian.setTextColor(ColorUtils.GRAY);
            tv_fengxian.setCompoundDrawables(lan,null,null,null);
            num++;
        } else if ("001".equals(myAccoutPower.risk1)) {
            tv_fengxian.setText(Html.fromHtml("<u>" + "未完成，开始评测" + "</u>"));
        } else {
            tv_fengxian.setText(Html.fromHtml("<u>" + "已过期，再次评测" + "</u>"));
        }

        if ("002".equals(myAccoutPower.stipulator)) {
            tv_hetong.setText("已签署");
            tv_hetong.setTextColor(ColorUtils.GRAY);
            num++;
        } else {
            tv_hetong.setText(Html.fromHtml("<u>" + "未签署，去签署" + "</u>"));
        }

//        if (!TextUtils.isEmpty(myAccoutPower.contacts)) {
//            tv_contacts.setText("已填写");
//            tv_contacts.setTextColor(ColorUtils.GRAY);
//            tv_contacts.setCompoundDrawables(lan,null,null,null);
//            num++;
//        } else {
//            tv_contacts.setText(Html.fromHtml("<u>" + "未填写，去填写" + "</u>"));
//            tv_contacts.setCompoundDrawables(hui,null,null,null);
//        }
        if ("002".equals(myAccoutPower.idcard1) && "001".equals(myAccoutPower.idcard2)) {
            tv_identity.setText("正常");
            tv_identity.setTextColor(ColorUtils.GRAY);
            num++;
        } else if ("001".equals(myAccoutPower.idcard1)) {
            tv_identity.setText(Html.fromHtml("<u>" + "异常，请到营业部办理" + "</u>"));
        } else {
            tv_identity.setText(Html.fromHtml("<u>" + "异常，请到营业部办理" + "</u>"));
        }
        float percent = num / 6f * 100f;
        pb_power.setCurrentValues(percent);
        rb_star.setRating(num);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_power;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_fengxian:
                startActivity(new Intent(AccountPowerActivity.this, RiskEvaluationActivity.class));
                break;
            case R.id.tv_hetong:
                startActivity(new Intent(AccountPowerActivity.this, AgreementActivity.class));
                break;
            case R.id.tv_identity:
                startActivity(new Intent(AccountPowerActivity.this, UpdateIdCodeValidityActivity.class));
                break;
        }
    }
}
