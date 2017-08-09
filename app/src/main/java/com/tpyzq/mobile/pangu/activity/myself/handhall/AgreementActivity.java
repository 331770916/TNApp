package com.tpyzq.mobile.pangu.activity.myself.handhall;

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
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/8/30.
 * 电子签名约定书
 */
public class AgreementActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "Agreement";
    private LinearLayout mAGLinearLayout;
    private TextView Headline, Data, Name, Date;
    private Button mSigned;

    @Override
    public void initView() {

        mAGLinearLayout = (LinearLayout) findViewById(R.id.AGLinearLayout);
        mSigned = (Button) findViewById(R.id.Sifned);

        Headline = (TextView) findViewById(R.id.Headline);
        Data = (TextView) findViewById(R.id.Data);
        Name = (TextView) findViewById(R.id.Name);
        Date = (TextView) findViewById(R.id.Date);
        findViewById(R.id.AGpublish_back).setOnClickListener(this);
        findViewById(R.id.Yse).setOnClickListener(this);
        findViewById(R.id.NO).setOnClickListener(this);
        initData();
        QuerytoConnect();
    }

    /**
     * 查询用户是否签署电子签名约定书 网络
     */
    private void QuerytoConnect() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "700160");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {

//            private AgreementBean.BeanData1 _bean;

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(AgreementActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        JSONObject json = res.getJSONArray("data").getJSONObject(0);
                        if(false == json.optBoolean("IS_SIGNED")){
                            mAGLinearLayout.setVisibility(View.VISIBLE);
                            mSigned.setVisibility(View.GONE);
                        }else {
                            mAGLinearLayout.setVisibility(View.GONE);
                            mSigned.setVisibility(View.VISIBLE);
                        }
                        String name = json.optString("CLIENT_NAME");
                        if(!TextUtils.isEmpty(name)){
                            Name.setText("客户:" + name);
                        }else{
                            Name.setText("客户:" + "- -");
                        }
                        String date = json.optString("SIGN_DATE");
                        if(!"0".equals(date) && !TextUtils.isEmpty(date)){
                            Date.setText("日期:" + Helper.getMyDateY_M_D(date));
                        }else{
                            Date.setText("日期:" + "- -");
                        }
                    }else if("-6".equals(code)){
                        Intent intent = new Intent(AgreementActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    }else{
                        MistakeDialog.showDialog(res.optString("msg"), AgreementActivity.this);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    MistakeDialog.showDialog(e.toString(), AgreementActivity.this);
                }
                /**
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<AgreementBean>() {
                }.getType();
                AgreementBean bean = gson.fromJson(response, type);
                if (bean.getCode().equals("0")) {
                    if (bean.getData() != null && bean.getData().size() > 0) {
                        for (int i = 0; i < bean.getData().size(); i++) {
                            _bean = new AgreementBean.BeanData1();
                            _bean.setIS_SIGNED(bean.getData().get(i).getIS_SIGNED());
                            _bean.setCLIENT_NAME(bean.getData().get(i).getCLIENT_NAME());
                            _bean.setSIGN_DATE(bean.getData().get(i).getSIGN_DATE());
                        }

                        if (!TextUtils.isEmpty(_bean.getSIGN_DATE().toString()) && !"0".equals(_bean.getSIGN_DATE().toString())) {
                            Date.setText("日期:" + Helper.getMyDateY_M_D(_bean.getSIGN_DATE().toString()));
                        } else {
                            Date.setText("日期:" + "- -");
                        }

                        if (!TextUtils.isEmpty(_bean.getCLIENT_NAME().toString())) {
                            Name.setText("客户:" + _bean.getCLIENT_NAME().toString());
                        } else {
                            Name.setText("客户:" + "- -");
                        }


                    }
                    if (false == _bean.getIS_SIGNED()) {
                        mAGLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        mAGLinearLayout.setVisibility(View.GONE);
                    }
                } else if (bean.getCode().equals("-6")) {
                    Intent intent = new Intent(Agreement.this, TransactionLoginActivity.class);
                    startActivity(intent);
                } else {
                    MistakeDialog.showDialog(bean.getMsg(), Agreement.this);
                }
                 */
            }
        });
    }


    /**
     * 电子签名约定书签署内容
     */
    private void SigntoConnect() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "700161");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(AgreementActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        ResultDialog.getInstance().show("" + "签署成功", R.mipmap.lc_success);
                        mAGLinearLayout.setVisibility(View.GONE);
                    }else if("-6".equals(code)){
                        Intent intent = new Intent(AgreementActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    }else{
                        MistakeDialog.showDialog(res.optString("msg"), AgreementActivity.this);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    MistakeDialog.showDialog(e.toString(), AgreementActivity.this);
                }
                /**
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<AgreementBean>() {
                }.getType();
                AgreementBean bean = gson.fromJson(response, type);
                if (bean.getCode().equals("0")) {
                    ResultDialog.getInstance().show("" + "签署成功", R.mipmap.lc_success);
                    mAGLinearLayout.setVisibility(View.GONE);
                } else if (bean.getCode().equals("-6")) {
                    Intent intent = new Intent(Agreement.this, TransactionLoginActivity.class);
                    startActivity(intent);
                } else {
                    MistakeDialog.showDialog(bean.getMsg(), Agreement.this);
                }
                */
            }
        });
    }

    private void initData() {
        Headline.setText("电子签名约定书");
        Data.setText(getString(R.string.AgreementTextView));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Yse:
                SigntoConnect();
                break;
            case R.id.AGpublish_back:
                setResult(RESULT_OK, getIntent());
                finish();
                break;
            case R.id.NO:
                ResultDialog.getInstance().show("" + "权限开通失败", R.mipmap.lc_failed);
                finish();
                break;
        }
    }
}
