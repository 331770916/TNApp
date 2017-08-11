package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;



/**
 * Created by wangqi on 2016/9/14.
 * 开通上交风险警示
 */
public class RiskWarningActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "RiskWarning";
    private static String TAGDate = "RiskWarningActivity" ;
    private TextView Headline, Data1, Data2, mName, mDate, mtext1, mtext2;
    private Dialog loadingDialog;
    private Button mSigned;
    private LinearLayout mAGLinearLayout;

    @Override
    public void initView() {
        TextView title = (TextView) findViewById(R.id.publish_title);
        title.setText(getString(R.string.RiskWarning));

        findViewById(R.id.PLpublish_back).setOnClickListener(this);
        Headline = (TextView) findViewById(R.id.Headline);
        Data1 = (TextView) findViewById(R.id.Data1);
        Data2 = (TextView) findViewById(R.id.Data2);
        mName = (TextView) findViewById(R.id.Name);
        mDate = (TextView) findViewById(R.id.Date);
        mtext1 = (TextView) findViewById(R.id.mtext1);
        mtext2 = (TextView) findViewById(R.id.mtext2);
        findViewById(R.id.Yse).setOnClickListener(this);
        findViewById(R.id.NO).setOnClickListener(this);
        mAGLinearLayout = (LinearLayout) findViewById(R.id.AGLinearLayout);
        mSigned = (Button) findViewById(R.id.Sifned);

        Data1.setVisibility(View.VISIBLE);
        mtext1.setVisibility(View.VISIBLE);
        mtext2.setVisibility(View.VISIBLE);

        String KeyRecord = getIntent().getStringExtra("Record");
        if (KeyRecord.contains("w")) {
            mAGLinearLayout.setVisibility(View.GONE);
            mSigned.setVisibility(View.VISIBLE);
        } else {
            mAGLinearLayout.setVisibility(View.VISIBLE);
            mSigned.setVisibility(View.GONE);
        }

        initData();
        toConnectDate();
    }


    private void initData() {
        String KeyName = getIntent().getStringExtra("Name");
        if (!TextUtils.isEmpty(KeyName)) {
            mName.setText("客户:" + KeyName);
        } else {
            mName.setText("客户:" + "- -");
        }

        Headline.setText(getString(R.string.RiskWarning1));
        mtext1.setText(getString(R.string.RiskWarning3));
        mtext2.setText(getString(R.string.RiskWarning4));
        Data1.setText(getString(R.string.RiskWarning2));
        Data2.setText(getString(R.string.Tui));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_protocol;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PLpublish_back:
                finish();
                break;
            case R.id.Yse:
                toConnect();
                break;
            case R.id.NO:
                ResultDialog.getInstance().show("" + "权限开通失败", R.mipmap.lc_failed);
//                Intent intent=new Intent();
//                intent.putExtra("names1",0);
//                intent.setClass(this, AgreementSigned.class);
//                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 客户时间 网络请求
     */
    private void toConnectDate() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "700071");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAGDate, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                Helper.getInstance().showToast(RiskWarningActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                String SHFXJS_SIGN_DATE = data.getJSONObject(i).getString("SHFXJS_SIGN_DATE");
                                if (!TextUtils.isEmpty(SHFXJS_SIGN_DATE) && !"0".equals(SHFXJS_SIGN_DATE)) {
                                    mDate.setText("日期:" + (Helper.getMyDateY_M_D(SHFXJS_SIGN_DATE)));//是上海风险警示协议
                                } else {
                                    mDate.setText("日期:" + "- -");
                                }
                            }
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        startActivity(new Intent(RiskWarningActivity.this, TransactionLoginActivity.class));
                    } else {
                        showDialog(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(RiskWarningActivity.this, "网络异常");
                }
            }
        });
    }


    /**
     * 网络请求
     */
    private void toConnect() {
        loadingDialog = LoadingDialog.initDialog(this, "正在加载");
        loadingDialog.show();
        String mSession = SpUtils.getString(this, "mSession", "");
        String Code = getIntent().getStringExtra("Code");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "715221");
        map.put("token", mSession);
        map.put("parms", map1);

        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map1.put("STOCK_ACCOUNT", Code);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(RiskWarningActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(jsonObject.getString("code"))) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        ResultDialog.getInstance().show("" + "权限已开通", R.mipmap.lc_success);
                        mAGLinearLayout.setVisibility(View.GONE);
                    } else if ("-6".equals(code)) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        startActivity(new Intent(RiskWarningActivity.this, TransactionLoginActivity.class));
                    } else if ("400".equals(code)) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        startActivityForResult(new Intent(RiskWarningActivity.this, AgreementActivity.class), 100);
                    } else {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        showDialog(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(RiskWarningActivity.this, "网络异常");
                }
            }
        });
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),RiskWarningActivity.class.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (loadingDialog != null && loadingDialog.isShowing())
                NetWorkUtil.getInstence().cancelSingleRequestByTag(TAG);
                NetWorkUtil.getInstence().cancelSingleRequestByTag(TAGDate);
            if (loadingDialog!=null)
                loadingDialog.dismiss();
            finish();
        }
        return false;
    }
}
