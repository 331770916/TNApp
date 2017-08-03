package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


/**
 * Created by wangqi on 2016/9/13.
 * 上市所退整理板块协议
 */
public class ProtocolActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "Protocol";
    private TextView Headline, Data1, Data2, Name, Date;
    String EXCHANGETYPE;
    private LinearLayout pLinearLayout;
    private Dialog loadingDialog;
    private String secu_code;
    private int names;
    private int names1;
    private TextView title;
    private TextView mtext1;
    private TextView mtext2;
    private Button mSigned;
    private LinearLayout mAGLinearLayout;

    @Override
    public void initView() {
        title = (TextView) findViewById(R.id.publish_title);

        findViewById(R.id.PLpublish_back).setOnClickListener(this);
        Headline = (TextView) findViewById(R.id.Headline);
        Data1 = (TextView) findViewById(R.id.Data1);
        Data2 = (TextView) findViewById(R.id.Data2);
        Name = (TextView) findViewById(R.id.Name);
        Date = (TextView) findViewById(R.id.Date);
        mtext1 = (TextView) findViewById(R.id.mtext1);
        mtext2 = (TextView) findViewById(R.id.mtext2);
        mtext1.setVisibility(View.VISIBLE);
        mtext2.setVisibility(View.VISIBLE);
        mAGLinearLayout = (LinearLayout) findViewById(R.id.AGLinearLayout);
        mSigned = (Button) findViewById(R.id.Sifned);
        findViewById(R.id.Yse).setOnClickListener(this);
        findViewById(R.id.NO).setOnClickListener(this);

        if (getIntent().getStringExtra("Record").contains("d")) {
            mAGLinearLayout.setVisibility(View.GONE);
            mSigned.setVisibility(View.VISIBLE);
        } else {
            mAGLinearLayout.setVisibility(View.VISIBLE);
            mSigned.setVisibility(View.GONE);
        }
        initData();
    }

    private void initData() {
        String KeyName = getIntent().getStringExtra("Name");
        names = getIntent().getIntExtra("Marker", -1);
        String KeySHTSDate = getIntent().getStringExtra("SHTSDate");
        String KeySHFXJSDate = getIntent().getStringExtra("SZTSDate");


        if (names == 1) {
            title.setText(getString(R.string.Protocol));
        } else if (names == 2) {
            title.setText(getString(R.string.Protocol_1));
        }


        if (!TextUtils.isEmpty(KeyName)) {
            Name.setText("客户:" + KeyName);
        } else {
            Name.setText("客户:" + "- -");
        }

        if (!TextUtils.isEmpty(KeySHTSDate) && !"0".equals(KeySHTSDate)) {
            if (names == 1) {
                Date.setText("日期:" + KeySHTSDate);
            } else {
                Date.setText("日期:" + KeySHFXJSDate);
            }
        } else if (!TextUtils.isEmpty(KeySHFXJSDate) && !"0".equals(KeySHFXJSDate)) {
            if (names == 1) {
                Date.setText("日期:" + KeySHTSDate);
            } else {
                Date.setText("日期:" + KeySHFXJSDate);
            }
        } else {
            Date.setText("日期:" + "- -");
        }

        Headline.setText(getString(R.string.Protoco3));
        Data1.setText(getString(R.string.RiskWarning2));
        Data2.setText(getString(R.string.Protoco2));
        mtext1.setText(getString(R.string.RiskWarning3));
        mtext2.setText(getString(R.string.Protoco4));


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_protocol;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.PLpublish_back:
                finish();
                break;
            case R.id.Yse:
                loadingDialog = LoadingDialog.initDialog(this, "正在加载");
                loadingDialog.show();
                toConnect();
                break;
            case R.id.NO:
                ResultDialog.getInstance().show("" + "权限开通失败", R.mipmap.lc_failed);
//                intent.putExtra("names1",names1);
//                intent.setClass(this, AgreementSigned.class);
//                startActivity(intent);
                finish();
                break;
        }
    }


    /**
     * 网络请求
     */
    private void toConnect() {
        String mSession = SpUtils.getString(this, "mSession", "");
        names1 = getIntent().getIntExtra("Marker", -1);

        if (names1 == 1) {
            EXCHANGETYPE = "1";
            secu_code = getIntent().getStringExtra("Code");

        } else {
            EXCHANGETYPE = "2";
            secu_code = getIntent().getStringExtra("Code");
        }
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "715231");
        map.put("token", mSession);
        map.put("parms", map1);

        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map1.put("EXCHANGE_TYPE", EXCHANGETYPE);
        map1.put("STOCK_ACCOUNT", secu_code);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(ProtocolActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        ResultDialog.getInstance().show("" + jsonObject.getString("msg"), R.mipmap.duigou);
                        mAGLinearLayout.setVisibility(View.GONE);
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        startActivity(new Intent(ProtocolActivity.this, TransactionLoginActivity.class));
                    } else if ("400".equals(code)) {
                            loadingDialog.dismiss();
                        startActivityForResult(new Intent(ProtocolActivity.this, AgreementActivity.class),100);
                    }else {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        MistakeDialog.showDialog(jsonObject.getString("msg"), ProtocolActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(ProtocolActivity.this, "网络异常");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            finish();
        }
    }
}
