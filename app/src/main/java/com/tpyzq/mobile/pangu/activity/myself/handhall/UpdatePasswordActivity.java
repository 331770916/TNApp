package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/20.
 * 忘记密码
 */

public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener{

    private final String TAG = UpdatePasswordActivity.class.getSimpleName();
    private EditText mNewPasswrod;
    private EditText mConfirmPassword;
    private TextView mFinishBtn;
    private String mUserId;
    private String mFundAccount;
    private String mUser_biz_id;

    private boolean newPasswordIsNull;
    private boolean confirmPasswrodIsNull;

    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private Dialog mProgressDialog;

    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);

        mNewPasswrod = (EditText) findViewById(R.id.ed_updatePwd_new);
        mConfirmPassword = (EditText) findViewById(R.id.ed_updatePwd_confirm);

        mNewPasswrod.addTextChangedListener(new CustomTextWatcher("newPwd"));
        mConfirmPassword.addTextChangedListener(new CustomTextWatcher("confirmPwd"));

        mFinishBtn = (TextView) findViewById(R.id.updatePwd_finishBtn);
        mFinishBtn.setClickable(false);
        mFinishBtn.setFocusable(false);
        mFinishBtn.setOnClickListener(this);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");
        mFundAccount = intent.getStringExtra("fundAccount");
        mUser_biz_id = intent.getStringExtra("user_biz_id");

        initLoadDialog();
        stup(mUser_biz_id, mUserId, "5", "设置密码", "0", "initView");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updatePwd_finishBtn:

                if (checkPwd()) {
                    initLoadDialog();
                    updatePassword();
                }

                break;
            case R.id.userIdBackBtn:

                CancelDialog.cancleDialog(UpdatePasswordActivity.this);

                break;
        }
    }

    /**
     * 验证密码有效性
     * @return
     */
    private boolean checkPwd() {
        if (TextUtils.isEmpty(mNewPasswrod.getText().toString())) {
            CentreToast.showText(this, "新密码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
            CentreToast.showText(this, "确认密码不能为空");
            return false;
        }

        if (!mNewPasswrod.getText().toString().equals(mConfirmPassword.getText().toString())) {
            CentreToast.showText(this, "两次输入的密码不同，请重新输入");
            return false;
        }
        return true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                clickBackKey = true;
                mProgressDialog.cancel();
                return false;
            } else {
                CancelDialog.cancleDialog(UpdatePasswordActivity.this);
                return false;
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_updatepwd;
    }

    /**网络连接**/

    private void stup(String user_biz_id, String userid, String node_code, String node_name, String opr_type, final String flag) {
        Map map = new HashMap();
        map.put("code", "WT6041");

        Map<String,String> params = new HashMap<>();
        params.put("user_biz_id", user_biz_id);
        params.put("node_code", node_code);
        params.put("node_name", node_name);//交易
        params.put("opr_memo", node_name);//资金
        params.put("opr_type", opr_type);//通信
        params.put("opr_id", userid);
        params.put("result_comment", node_name);
        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String error_no = jsonObject.getString("error_no");
                    String errorInfo = jsonObject.getString("error_info");

                    if (!"0".equals(error_no)) {
                        showMistackDialog(errorInfo, new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {
                                finish();
                            }
                        });
                        return;
                    }

                    if ("success".equals(flag)) {
                        CentreToast.showText(UpdatePasswordActivity.this, "密码修改成功");
                        finish();
                    } else if ("failed".equals(flag)) {
                        showMistackDialog("密码修改失败", null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });

    }

    /**修改密码**/

    private void updatePassword () {
        Map map = new HashMap();
        map.put("code", "WT6320");

        Map<String,String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("pwdType", "1,0,0");
        params.put("pwdJy", mNewPasswrod.getText().toString());//交易
        params.put("pwdZj", "");//资金
        params.put("pwdTx", "");//通信
        params.put("pwdResetJyma", "1");
        params.put("creditReset", "0");
        params.put("fund_account", mFundAccount);

        map.put("params", params);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.getURL_USERINFO(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showMistackDialog("网络异常", null);
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }

                    showMistackDialog("网络数据返回为空", null);
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error_no = jsonObject.getString("error_no");
                    String error_info = jsonObject.getString("error_info");

                    if ("0".equals(error_no)) {

                        stup(mUser_biz_id, mUserId, "8", "办理成功", "0", "success");

                    } else {
                        stup(mUser_biz_id, mUserId, "9", "办理失败", "0", "failed");
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }

                    showMistackDialog("网络数据解析异常", null);
                }
            }
        });
    }

    private void finishBtnStauts() {
        if (newPasswordIsNull && confirmPasswrodIsNull) {
            mFinishBtn.setClickable(true);
            mFinishBtn.setFocusable(true);
            mFinishBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.userid_nextstup_p));
        } else {
            mFinishBtn.setClickable(false);
            mFinishBtn.setFocusable(false);
            mFinishBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.userid_nextstup_n));
        }

    }

    private void settingNextBtn (String tag, boolean isNotNull) {
        switch (tag) {
            case "newPwd":
                newPasswordIsNull = isNotNull;
                finishBtnStauts();
                break;
            case "confirmPwd":
                confirmPasswrodIsNull = isNotNull;
                finishBtnStauts();
                break;
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        private String flag;
        public CustomTextWatcher (String flag) {
            this.flag = flag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                settingNextBtn(flag, true);
            } else {
                settingNextBtn(flag, false);
            }
        }
    }


    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(UpdatePasswordActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg,  CancelDialog.PositiveClickListener onClickListener) {
        CancelDialog.cancleDialog(UpdatePasswordActivity.this, errorMsg, onClickListener);
    }
}
