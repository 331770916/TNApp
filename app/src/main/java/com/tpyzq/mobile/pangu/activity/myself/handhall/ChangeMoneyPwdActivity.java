package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.keyboard.NoSoftInputEditText;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoginDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.yzd.unikeysdk.OnInputDoneCallBack;
import com.yzd.unikeysdk.PasswordKeyboard;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;

import bonree.k.H;

/**
 * Created by ltyhome on 05/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:修改资金密码
 */

public class ChangeMoneyPwdActivity extends BaseActivity implements View.OnClickListener, InterfaceCollection.InterfaceCallback {
    private final String TAG = "ChangeMoneyPwdActivity";
    private EditText mAtpresent_et, mNew_et, mAgainNew_et;
    private NoSoftInputEditText mAtpresent_nset, mNew_nset, mAgainNew_nset;
    private Button mSubmit_but;

    private String mKeyboardInput_mAtpresent, mKeyboardInput_mNew, mKeyboardInput_mAgainNew;
    int MAXLINES = 8;
    private Dialog mCommit;

    @Override
    public void initView() {

        ((TextView) findViewById(R.id.publish_title)).setText(getString(R.string.changemoneypwdactivity));
        ((TextView) findViewById(R.id.HintShow)).setVisibility(View.GONE);

        findViewById(R.id.Ppublish_back).setOnClickListener(this);

        //原密码_明
        mAtpresent_et = (EditText) findViewById(R.id.AtpresentPasswordET);
        //原密码_密
        mAtpresent_nset = (NoSoftInputEditText) findViewById(R.id.AtpresentPassword);

        //新密码_明
        mNew_et = (EditText) findViewById(R.id.NewPasswordET);
        //新密码_密
        mNew_nset = (NoSoftInputEditText) findViewById(R.id.NewPassword);

        //再次输入密码_明
        mAgainNew_et = (EditText) findViewById(R.id.AgainNewPasswordET);
        //再次输入密码_密
        mAgainNew_nset = (NoSoftInputEditText) findViewById(R.id.AgainNewPassword);

        mSubmit_but = (Button) findViewById(R.id.Submit);

        mCommit = LoadingDialog.initDialog(this, "正在提交...");

        if ("0".equals(UserUtil.Keyboard)) {
            setPassEdit(false);
        } else {
            setPassEdit(true);
        }

        monitor();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    //初始化点击事件
    private void monitor() {

        mAtpresent_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        mAtpresent_nset.setMaxLines(MAXLINES);

        mSubmit_but.setOnClickListener(this);
        mSubmit_but.setClickable(false);
        mSubmit_but.setBackgroundResource(R.drawable.button_login_unchecked);

        mAtpresent_et.addTextChangedListener(new MyTextWatcher());
        mAtpresent_nset.addTextChangedListener(new MyTextWatcher());

        mNew_et.addTextChangedListener(new MyTextWatcher());
        mNew_nset.addTextChangedListener(new MyTextWatcher());

        mAgainNew_et.addTextChangedListener(new MyTextWatcher());
        mAgainNew_nset.addTextChangedListener(new MyTextWatcher());

        mSubmit_but.setOnClickListener(this);

        showKeyboardAtpresent(mAtpresent_nset);
        showKeyboardNew(mNew_nset);
        showAgainNew(mAgainNew_nset);

    }


    private void setPassEdit(Boolean flag) {     // true 密文  false 明文
        if (flag) {
            mAtpresent_et.setVisibility(View.GONE);
            mNew_et.setVisibility(View.GONE);
            mAgainNew_et.setVisibility(View.GONE);

            mAtpresent_nset.setVisibility(View.VISIBLE);
            mNew_nset.setVisibility(View.VISIBLE);
            mAgainNew_nset.setVisibility(View.VISIBLE);


        } else {
            mAtpresent_et.setVisibility(View.VISIBLE);
            mNew_et.setVisibility(View.VISIBLE);
            mAgainNew_et.setVisibility(View.VISIBLE);

            mAtpresent_nset.setVisibility(View.GONE);
            mNew_nset.setVisibility(View.GONE);
            mAgainNew_nset.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Ppublish_back:
                finish();
                break;
            case R.id.Submit:
                if (isEmptyData()) {
                    networkConnect();
                }
                break;

        }
    }

    private void networkConnect() {
        if (null != mCommit) {
            mCommit.show();
        }

        String unikey = "";
        try {
            unikey = UniKey.getInstance().getUnikeyId();
        } catch (UnikeyException e) {
            e.printStackTrace();
        }

        if ("0".equals(UserUtil.Keyboard)) {
            mKeyboardInput_mAtpresent = mAtpresent_et.getText().toString();
            mKeyboardInput_mNew = mNew_et.getText().toString();
            mKeyboardInput_mAgainNew = mAgainNew_et.getText().toString();
        }
//        else {
//            try {
////                mKeyboardInput_mAtpresent = UniKey.getInstance().encryptData(mKeyboardInput_mAtpresent);
////                mKeyboardInput_mNew = UniKey.getInstance().encryptData(mKeyboardInput_mNew);
//
//            } catch (UnikeyException e) {
//                e.printStackTrace();
//            }
//        }

        InterfaceCollection.getInstance().ModifyFundsPassword(TAG, mKeyboardInput_mAtpresent, mKeyboardInput_mNew, unikey, this);
    }


    private boolean isEmptyData() {
        if ("0".equals(UserUtil.Keyboard)) {
            if (!mNew_et.getText().toString().equals(mAgainNew_et.getText().toString())) {
                Helper.getInstance().showToast(this, "两次输入新密码不一致");
                return false;
            } else {
                return true;
            }
        } else {
            if (!mKeyboardInput_mNew.equals(mKeyboardInput_mAgainNew)) {
                Helper.getInstance().showToast(this, "两次输入新密码不一致");
                return false;
            } else {
                return true;
            }
        }
    }

    private void setTextView() {
        mAtpresent_et.setText("");
        mNew_et.setText("");
        mAgainNew_et.setText("");

        mAtpresent_nset.setText("");
        mNew_nset.setText("");
        mAgainNew_nset.setText("");
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
//                    Log.d(TAG, "num:" + i);
                    String s = "";
                    for (int a = 0; a < i; a++) {
                        s += "*";
                    }
                    v.setText(s);
                    v.setSelection(v.getText().length());
                }
            }, MAXLINES, false, "custom_keyboard_view");
            v.setPasswordKeyboard(passwordKeyboard);

//        Object   abc =passwordKeyboard.getView("bt_abc");
//
//            Button bt_abc= (Button) passwordKeyboard.getView("bt_abc");
//
//            bt_abc.setVisibility(View.GONE);


        } catch (UnikeyException e) {
            Helper.getInstance().showToast(this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
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
//                    Log.d(TAG, "num:" + i);
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
            Helper.getInstance().showToast(this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
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
//                    Log.d(TAG, "num:" + i);
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
            Helper.getInstance().showToast(this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        if (TAG.equals(info.getTag())) {
            if (null != mCommit)
                mCommit.dismiss();
            if ("0".equals(info.getCode())) {
                setTextView();
                MistakeDialog.showDialog("修改密码成功", this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        finish();
                    }
                });
            }else if ("-6".equals(info.getCode())){
                startActivity(new Intent().setClass(this,TransactionLoginActivity.class));
                finish();
            }else if (ConstantUtil.NETWORK_ERROR_CODE.equals(info.getCode())){
                setTextView();
                Helper.getInstance().showToast(this,info.getMsg());
            }else {
                setTextView();
                MistakeDialog.showDialog(info.getMsg(),this);
            }
        }
    }


    /**
     * EditText 内容监听
     */
    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            initButtonshow();
        }
    }

    /**
     * Button 显示状态
     */
    private void initButtonshow() {
        if (TextUtils.isEmpty(mAtpresent_et.getText().toString()) && TextUtils.isEmpty(mAtpresent_nset.getText().toString())) {
            mSubmit_but.setClickable(false);
            mSubmit_but.setBackgroundResource(R.drawable.button_login_unchecked);
        } else if (TextUtils.isEmpty(mNew_et.getText().toString()) && TextUtils.isEmpty(mNew_nset.getText().toString())) {
            mSubmit_but.setClickable(false);
            mSubmit_but.setBackgroundResource(R.drawable.button_login_unchecked);
        } else if (TextUtils.isEmpty(mAgainNew_et.getText().toString()) && TextUtils.isEmpty(mAgainNew_nset.getText().toString())) {
            mSubmit_but.setClickable(false);
            mSubmit_but.setBackgroundResource(R.drawable.button_login_unchecked);
        } else {
            mSubmit_but.setClickable(true);
            mSubmit_but.setBackgroundResource(R.drawable.button_login_pitchon);
        }
    }
}
