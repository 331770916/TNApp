package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.NoSoftInputEditText;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.yzd.unikeysdk.OnInputDoneCallBack;
import com.yzd.unikeysdk.PasswordKeyboard;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/8/31.
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "Password";
    private EditText mAtpresentET, mNewET, mAgainNewET;
    private NoSoftInputEditText mAtpresent, mNew, mAgainNew;
    private String mKeyboardInput_mAtpresent, mKeyboardInput_mNew, mKeyboardInput_mAgainNew;
    String passwordFormat;
    private Dialog mLoadingDialog;

    @Override
    public void initView() {

        findViewById(R.id.Ppublish_back).setOnClickListener(this);
        findViewById(R.id.Submit).setOnClickListener(this);

        mAtpresentET = (EditText) findViewById(R.id.AtpresentPasswordET);                  //原密码_明
        mAtpresent = (NoSoftInputEditText) findViewById(R.id.AtpresentPassword);           //原密码_密

        mNewET = (EditText) findViewById(R.id.NewPasswordET);                              //新密码_明
        mNew = (NoSoftInputEditText) findViewById(R.id.NewPassword);                       //新密码_密

        mAgainNewET = (EditText) findViewById(R.id.AgainNewPasswordET);                    //再次输入密码_明
        mAgainNew = (NoSoftInputEditText) findViewById(R.id.AgainNewPassword);             //再次输入密码_密

        mAtpresentET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
//        查询加密键盘是否显示
        if ("0".equals(UserUtil.Keyboard)) {
            setPassEdit(false);
        } else {
            setPassEdit(true);
        }
        showKeyboardAtpresent(mAtpresent);
        showKeyboardNew(mNew);
        showAgainNew(mAgainNew);
    }


    private void setPassEdit(Boolean flag) {     // true 密文  false 明文
        if (flag) {
            mAtpresentET.setVisibility(View.GONE);
            mNewET.setVisibility(View.GONE);
            mAgainNewET.setVisibility(View.GONE);

            mAtpresent.setVisibility(View.VISIBLE);
            mNew.setVisibility(View.VISIBLE);
            mAgainNew.setVisibility(View.VISIBLE);
            passwordFormat = "1";
        } else {
            mAtpresentET.setVisibility(View.VISIBLE);
            mNewET.setVisibility(View.VISIBLE);
            mAgainNewET.setVisibility(View.VISIBLE);

            mAtpresent.setVisibility(View.GONE);
            mNew.setVisibility(View.GONE);
            mAgainNew.setVisibility(View.GONE);
            passwordFormat = "0";
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    public void onClick(View v) {
        String mAtpresentET_str = mAtpresentET.getText().toString().trim();
        String mNewET_str = mNewET.getText().toString().trim();
        String mAgainNewET_str = mAgainNewET.getText().toString().trim();

        String mAtpresent_str = mAtpresent.getText().toString();
        String mNew_str = mNew.getText().toString().trim();
        String mAgainNew_str = mAgainNew.getText().toString();
        switch (v.getId()) {
            case R.id.Ppublish_back:
                finish();
                break;
            case R.id.Submit:
                if (mAtpresentET_str.equals("") || mNewET_str.equals("") || mAgainNewET_str.equals("")) {
                    if (mAtpresent_str.equals("") || mNew_str.equals("") || mAgainNew_str.equals("")) {
                        CentreToast.showText(this, "密码不能为空");
                    } else {
                        if (mKeyboardInput_mAtpresent.equals(mKeyboardInput_mNew)) {
                            CentreToast.showText(this, "新密码不能与原密码相同");
                            setTextView();
                        } else if (!mKeyboardInput_mNew.equals(mKeyboardInput_mAgainNew)) {
                            CentreToast.showText(this, "两次输入密码不一样");
                            setTextView();
                        } else {
                            int a = Helper.weakPwdCheck(mKeyboardInput_mNew);
                            if (a == 1) {
                                CentreToast.showText(this, "新密码不能为连续相同3个数字");
                                setTextView();
                            } else if (a == 2) {
                                CentreToast.showText(this, "新密码不能为连续3个数字");
                                setTextView();
                            } else if (Helper.isContinuous(mKeyboardInput_mNew)) {
                                CentreToast.showText(this, "新密码不能为2个连续相同3个数字");
                                setTextView();
                            } else {
                                //通过
                                mLoadingDialog = LoadingDialog.initDialog(this, "正在提交...");
                                //显示Dialog
                                mLoadingDialog.show();
                                toConnect();
                            }
                        }
                    }

                } else {
                    if (mAtpresentET_str.equals(mNewET_str)) {
                        CentreToast.showText(this, "原密码跟新密码不一样");
                        setTextView();
                    } else if (!mNewET_str.equals(mAgainNewET_str)) {
                        CentreToast.showText(this, "两次输入密码不样");
                        setTextView();
                    } else {
                        int a = Helper.weakPwdCheck(mNewET_str);
                        if (a == 1) {
                            CentreToast.showText(this, "新密码不能为连续相同3个数字");
                            setTextView();
                        } else if (a == 2) {
                            CentreToast.showText(this, "新密码不能为连续3个数字");
                            setTextView();
                        } else if (Helper.isContinuous(mNewET_str)) {
                            CentreToast.showText(this, "新密码不能为2个连续相同3个数字");
                            setTextView();
                        } else {
                            //通过
                            mLoadingDialog = LoadingDialog.initDialog(this, "正在提交...");
                            //显示Dialog
                            mLoadingDialog.show();
                            toConnect();
                        }
                    }
                }
                break;
        }
    }

    private void setTextView() {
        mAtpresentET.setText("");
        mNewET.setText("");
        mAgainNewET.setText("");

        mAtpresent.setText("");
        mNew.setText("");
        mAgainNew.setText("");
    }

    /**
     * 网络请求
     */
    private void toConnect() {
        String unikey = "";
        try {
            unikey = UniKey.getInstance().getUnikeyId();
        } catch (UnikeyException e) {
            e.printStackTrace();
        }

        if (passwordFormat.equals("0")) {
            mKeyboardInput_mNew = mNewET.getText().toString().trim();
            mKeyboardInput_mAtpresent = mAtpresentET.getText().toString().trim();
        } else {
            try {
                mKeyboardInput_mNew = UniKey.getInstance().encryptData(mKeyboardInput_mNew);
                mKeyboardInput_mAtpresent = UniKey.getInstance().encryptData(mKeyboardInput_mAtpresent);

            } catch (UnikeyException e) {
                e.printStackTrace();
            }
        }

        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        String mSession = SpUtils.getString(this, "mSession", "");
        map.put("funcid", "700060");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map1.put("NEW_PWD", mKeyboardInput_mNew);
        map1.put("OLD_PWD", mKeyboardInput_mAtpresent);
        map1.put("PWD_TYPE", passwordFormat); //密码类型 0：明文 1：密文
        map1.put("MOBILE", DeviceUtil.getDeviceId(CustomApplication.getContext()));                       //绑定UNIKEYID的手机号
        map1.put("UNIKEYID", unikey);                       //UNIKEY插件ID
        map1.put("APP_TYPE", "1");                       //手机类型 0：ios        1：android

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                LogHelper.e(TAG, e.toString());
                CentreToast.showText(ChangePasswordActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String mCode_Str = object.getString("code");
                    String mMsg_Str = object.getString("msg");

                    if (mCode_Str.equals("0")) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setIslogin("false");
                        Db_PUB_USERS.UpdateIslogin(userEntity);
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        showDialog("修改密码成功",true);
                    } else if (mCode_Str.equals("-6")) {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        Intent intent = new Intent(ChangePasswordActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        setTextView();
                        showDialog(mMsg_Str,false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CentreToast.showText(ChangePasswordActivity.this, "网络异常");
                }
            }
        });

    }

    private void showDialog(String msg,boolean isClick){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),ChangePasswordActivity.class.toString());
        if (isClick){
            customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
                @Override
                public void confirmOnclick() {
                    finish();
                    customCenterDialog.dismiss();
                }
            });
        }
    }


    /**
     * 键盘插件数据获取 原密码
     */
    private void showKeyboardAtpresent(final NoSoftInputEditText v) {
        try {
            PasswordKeyboard passwordKeyboard = UniKey.getInstance().getPasswordKeyboard(this, new OnInputDoneCallBack() {
                @Override
                public void getInputEncrypted(String s) {
                    mKeyboardInput_mAtpresent = s;
//                    getPasswrod( Base64.encodeToString(s.getBytes(),0));
//                    getPasswrod(s);
                }

                @Override
                public void getCharNum(int i) {
                    //显示当前用户已经输入的字符串个数
                    Log.d(TAG, "num:" + i);
                    String s = "";
                    for (int a = 0; a < i; a++) {
                        s += "*";
                    }
                    v.setText(s);
                    v.setSelection(v.getText().length());
                }
            }, 6, false, "custom_keyboard_view");
            v.setPasswordKeyboard(passwordKeyboard);

        } catch (UnikeyException e) {
            CentreToast.showText(ChangePasswordActivity.this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
        }
    }

    /**
     * 键盘插件数据获取 新密码
     */
    private void showKeyboardNew(final NoSoftInputEditText v) {

        try {
            PasswordKeyboard passwordKeyboard = UniKey.getInstance().getPasswordKeyboard(this, new OnInputDoneCallBack() {
                @Override
                public void getInputEncrypted(String s) {
                    mKeyboardInput_mNew = s;
//                    getPasswrod( Base64.encodeToString(s.getBytes(),0));
//                    getPasswrod(s);
                }

                @Override
                public void getCharNum(int i) {
                    //显示当前用户已经输入的字符串个数
                    Log.d(TAG, "num:" + i);
                    String s = "";
                    for (int a = 0; a < i; a++) {
                        s += "*";
                    }
                    v.setText(s);
                    v.setSelection(v.getText().length());
                }
            }, 6, false, "custom_keyboard_view");
            v.setPasswordKeyboard(passwordKeyboard);

        } catch (UnikeyException e) {
            CentreToast.showText(ChangePasswordActivity.this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
        }
    }

    /**
     * 键盘插件数据获取 再次输入新密码
     */
    private void showAgainNew(final NoSoftInputEditText v) {
        try {
            PasswordKeyboard passwordKeyboard = UniKey.getInstance().getPasswordKeyboard(this, new OnInputDoneCallBack() {
                @Override
                public void getInputEncrypted(String s) {
                    mKeyboardInput_mAgainNew = s;
//                    getPasswrod( Base64.encodeToString(s.getBytes(),0));
//                    getPasswrod(s);
                }

                @Override
                public void getCharNum(int i) {
                    //显示当前用户已经输入的字符串个数
                    Log.d(TAG, "num:" + i);
                    String s = "";
                    for (int a = 0; a < i; a++) {
                        s += "*";
                    }
                    v.setText(s);
                    v.setSelection(v.getText().length());
                }
            }, 6, false, "custom_keyboard_view");
            v.setPasswordKeyboard(passwordKeyboard);

        } catch (UnikeyException e) {
            CentreToast.showText(ChangePasswordActivity.this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
        }
    }
}