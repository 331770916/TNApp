package com.tpyzq.mobile.pangu.activity.myself.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.CustomDialog;
import com.tpyzq.mobile.pangu.view.dialog.HandoverDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/8/22.
 * 手机验证
 */
public class ShouJiVerificationActivity extends BaseActivity implements View.OnClickListener, InterfaceCollection.InterfaceCallback {
    private static String TAG = "ShouJiVerification";

    private static String TAG1 = "ImageVerification";
    private static String TAG2 = "VerificationCode";
    private static String TAG3 = "VoiceVerificationCode";
    private static String TAG4 = "InscriptionRegistry";

    private EditText mNumber_et;
    private EditText mImage_et;
    private SimpleDraweeView mSecurityCode;
    private EditText mCaptcha_et;
    private Button mCaptcha_but;
    private Button mLogIn_but;
    private LinearLayout mMarked_ll;
    private TextView mSound;
    private TextView mSound_tv;

    private CustomDialog.Builder mBuilder;

    private String mIdentify = "";
    private int pageIndex;
    private boolean mCaptchabtnState = false;
    private MyTimeCount time;

    DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                if (mBuilder.dialog != null && mBuilder.dialog.isShowing())
                    mBuilder.dialog.dismiss();
                OkHttpUtil.cancelSingleRequestByTag(ShouJiVerificationActivity.this.getClass().getName());
                mImage_et.setText("");
                mCaptcha_et.setText("");
                ImageVerification();
                if (time != null) {
                    time.cancel();
                    time.setMarked(true);
                    time.onFinish();
                }
            }
            return false;
        }
    };


    /**
     * @return 布局
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_shoujiverification;
    }

    @Override
    public void initView() {
        findViewById(R.id.SJYZ_back).setOnClickListener(this);

        //手机号
        mNumber_et = (EditText) findViewById(R.id.etNumber);
        //填写图片验证码
        mImage_et = (EditText) findViewById(R.id.etImage);
        //图片验证码
        mSecurityCode = (SimpleDraweeView) findViewById(R.id.SecurityCode);
        //短信验证码
        mCaptcha_et = (EditText) findViewById(R.id.etCaptcha);
        //获取短信验证码
        mCaptcha_but = (Button) findViewById(R.id.butCaptcha);
        //注册
        mLogIn_but = (Button) findViewById(R.id.butLogIn);
        mMarked_ll = (LinearLayout) findViewById(R.id.LinearLayout_Marked);
        //语音提示
        mSound = (TextView) findViewById(R.id.Sound);
        //获取语音验证码
        mSound_tv = (TextView) findViewById(R.id.tvSound);

        getIntentData();
        initLogic();
    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mIdentify = intent.getStringExtra("Identify");
            pageIndex = intent.getIntExtra("pageindex", 0);
        }
    }

    private void initLogic() {
        mBuilder = new CustomDialog.Builder(ShouJiVerificationActivity.this);
        mBuilder.create();
        mBuilder.dialog.setOnKeyListener(onKeyListener);
        mSound_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        monitor();
        ImageVerification();
    }

    private void monitor() {
        time = new MyTimeCount(120000, 1000, mCaptcha_but, mSound_tv);

        mNumber_et.addTextChangedListener(new MyTextWatcher());
        mImage_et.addTextChangedListener(new MyTextWatcher());
        mCaptcha_et.addTextChangedListener(new MyTextWatcher());

        mSecurityCode.setOnClickListener(this);
        mLogIn_but.setOnClickListener(this);
        mCaptcha_but.setOnClickListener(this);
        mSound_tv.setOnClickListener(this);

        mLogIn_but.setClickable(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SJublish_back:
                finish();
                break;
            case R.id.butLogIn:
                if (isEmptyData(true)) {
                    mLogIn_but.setClickable(false);
                    InscriptionRegistry();
                }
                break;
            case R.id.butCaptcha://短信
                if (isEmptyData(false)) {
                    mSound_tv.setClickable(false);
                    mCaptchabtnState = true;
                    time.setMarked(false);
                    VerificationCode();
                }
                break;
            case R.id.tvSound://语音
                if (isEmptyData(false)) {
                    mCaptcha_but.setClickable(false);
                    mCaptchabtnState = true;
                    time.setMarked(true);
                    VoiceVerificationCode();
                }
                break;
            case R.id.SecurityCode:
                ImageVerification();
                break;
        }
    }

    private boolean isEmptyData(boolean is) {
        String mNumber_str = mNumber_et.getText().toString().trim();
        String mImage_str = mImage_et.getText().toString().trim();
        String mCaptcha_str = mCaptcha_et.getText().toString().trim();

        if (is) {
            if (TextUtils.isEmpty(mNumber_str)) {
                Helper.getInstance().showToast(this, "请输入手机号");
                return false;
            } else if (TextUtils.isEmpty(mImage_str)) {
                Helper.getInstance().showToast(this, "请输入图片验证码");
                return false;
            } else if (TextUtils.isEmpty(mCaptcha_str)) {
                Helper.getInstance().showToast(this, "请输手机短信入验证码");
                return false;
            } else if (!Helper.isMobileNO(mNumber_str)) {
                Helper.getInstance().showToast(this, "请输入正确的手机号");
                return false;
            } else {
                return true;
            }
        } else {
            if (TextUtils.isEmpty(mNumber_str)) {
                Helper.getInstance().showToast(this, "请输入手机号");
                return false;
            } else if (TextUtils.isEmpty(mImage_str)) {
                Helper.getInstance().showToast(this, "请输入图片验证码");
                return false;
            } else if (!Helper.isMobileNO(mNumber_str)) {
                Helper.getInstance().showToast(this, "请输入正确的手机号");
                return false;
            } else {
                return true;
            }
        }
    }




    /**
     * 注册登录显示状态
     */
    private void initButtonshow() {
        if (TextUtils.isEmpty(mNumber_et.getText().toString())) {
            mLogIn_but.setBackgroundResource(R.drawable.button_login_unchecked);
            mLogIn_but.setTextColor(Color.parseColor("#ffffffff"));
        } else if (TextUtils.isEmpty(mImage_et.getText().toString())) {
            mLogIn_but.setBackgroundResource(R.drawable.button_login_unchecked);
            mLogIn_but.setTextColor(Color.parseColor("#ffffffff"));
        } else if (TextUtils.isEmpty(mCaptcha_et.getText().toString())) {
            if (!mCaptchabtnState) {
                mCaptcha_but.setClickable(true);
                mCaptcha_but.setBackgroundResource(R.drawable.captcha_button_unchecked);
                mCaptcha_but.setTextColor(Color.parseColor("#FFFFFF"));
            }
            mLogIn_but.setBackgroundResource(R.drawable.button_login_unchecked);
            mLogIn_but.setTextColor(Color.parseColor("#ffffffff"));
        } else if (!mCaptchabtnState) {
            mLogIn_but.setBackgroundResource(R.drawable.button_login_unchecked);
            mLogIn_but.setTextColor(Color.parseColor("#ffffffff"));
        } else {
            mLogIn_but.setClickable(true);
            mLogIn_but.setBackgroundResource(R.drawable.button_login_pitchon);
            mLogIn_but.setTextColor(Color.parseColor("#ffffffff"));
        }

    }


    //图片验证码网络请求
    private void ImageVerification() {
        mBuilder.setTitle("加载中...");
        mBuilder.create().show();
        InterfaceCollection.getInstance().getImageVerification(TAG1, this);
    }

    //短信验证码网络请求
    private void VerificationCode() {
        time.start();
        InterfaceCollection.getInstance().getVerificationCode(TAG2, mNumber_et.getText().toString(), mImage_et.getText().toString(), this);
    }

    //语音验证码网络请求
    private void VoiceVerificationCode() {
        time.start();
        InterfaceCollection.getInstance().getVoiceVerificationCode(TAG3, mNumber_et.getText().toString(), mImage_et.getText().toString(), this);
    }

    //绑定手机号
    private void InscriptionRegistry() {
        mBuilder.setTitle("正在提交...");
        mBuilder.create().show();
        InterfaceCollection.getInstance().BindingMobil(TAG4, mNumber_et.getText().toString(), mCaptcha_et.getText().toString(), this);
    }

    @Override
    public void callResult(ResultInfo info) {
        if (TAG1.equals(info.getTag())) {       //图片
            if (mBuilder.dialog != null) {
                mBuilder.dialog.dismiss();
            }
            if ("0".equals(info.getCode())) {
                Bitmap bitmap = Helper.base64ToBitmap((String) info.getData());
                mSecurityCode.setImageBitmap(bitmap);
                mSecurityCode.setAspectRatio(3.2f);
            } else {
                mSecurityCode.setImageResource(R.mipmap.ic_again);
                Helper.getInstance().showToast(this, info.getMsg());
            }
        } else if (TAG2.equals(info.getTag())) {   //短信
            if (!"0".equals(info.getCode()) && !ConstantUtil.NETWORK_ERROR_CODE.equals(info.getCode())) {
                MistakeDialog.showDialog(info.getMsg(), ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        time.cancel();
                        time.setMarked(true);
                        time.onFinish();
                    }
                });
            } else if (ConstantUtil.NETWORK_ERROR_CODE.equals(info.getCode())) {
                time.cancel();
                time.setMarked(true);
                time.onFinish();
            }
        } else if (TAG3.equals(info.getTag())) {        //语音
            if (!"0".equals(info.getCode()) && !ConstantUtil.NETWORK_ERROR_CODE.equals(info.getCode())) {
                MistakeDialog.showDialog(info.getMsg(), ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        time.cancel();
                        time.onFinish();
                    }
                });
            } else if (ConstantUtil.NETWORK_ERROR_CODE.equals(info.getCode())) {
                time.cancel();
                time.onFinish();
            }
        } else if (TAG4.equals(info.getTag())) {     //绑定手机号
            if ("0".equals(info.getCode())) {
                setUsermodData();
            }else if (ConstantUtil.NETWORK_ERROR_CODE.equals(info.getCode())) {
                if (mBuilder.dialog != null) {
                    mBuilder.dialog.dismiss();
                }
                time.cancel();
                time.setMarked(true);
                time.onFinish();
            } else {
                if (mBuilder.dialog != null) {
                    mBuilder.dialog.dismiss();
                }
                MistakeDialog.showDialog(info.getMsg(), ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        time.cancel();
                        time.setMarked(true);
                        time.onFinish();
                    }
                });
            }

        }
    }

    //修改数据库
    private void setUsermodData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setScno(mNumber_et.getText().toString().trim());       //注册账号
        Db_PUB_USERS.UpdateScno(userEntity);                             //修改 注册账号
        KeyEncryptionUtils.getInstance().localEncryptMobile(mNumber_et.getText().toString().trim());//修改手机号
        ShowNotice();
    }

    private void ShowNotice() {
        if ("1".equals(mIdentify)) {
            HandoverDialog.showDialog("验证成功", ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
//                    HOLE_SEQ.deleteAll();
                    SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                    finish();
                }
            });
        } else if ("2".equals(mIdentify)) {
            HandoverDialog.showDialog("验证成功", ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    //                    HOLE_SEQ.deleteAll();
                    SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                    finish();
                }
            });
        } else {
            HandoverDialog.showDialog("验证成功", ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {

                    Intent intent = getIntent();
                    if (intent == null) {
                        intent = new Intent();
                    }
                    UserUtil.refrushUserInfo();
                    if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                        intent.setClass(ShouJiVerificationActivity.this, TransactionLoginActivity.class);
                    } else if ("3".equals(KeyEncryptionUtils.getInstance().Typescno())) {
                        intent.setClass(ShouJiVerificationActivity.this, ShouJiVerificationActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    /**
     * 短信 语音 倒计时
     */
    class MyTimeCount extends CountDownTimer {
        View mView1, mView2;
        boolean mMarked = false;

        public MyTimeCount(long millisInFuture, long countDownInterval, View view1, View view2) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
            this.mView1 = view1;
            this.mView2 = view2;
        }

        public void setMarked(boolean marked) {
            this.mMarked = marked;
        }

        @Override
        public void onTick(long millisUntilFinished) {  //计时过程显示

            if (mMarked) {
                ((TextView) mView2).setClickable(false);
                mSound.setText("如果没有收到短信验证码，请尝试");
                ((TextView) mView2).setText("重获语音" + millisUntilFinished / 1000);
            } else {
                ((Button) mView1).setClickable(false);
                ((Button) mView1).setText("重获短信" + millisUntilFinished / 1000);
                ((Button) mView1).setTextColor(Color.parseColor("#87bd43"));
                ((Button) mView1).setBackgroundResource(R.drawable.captcha_button_pitchon);
            }
        }

        @Override
        public void onFinish() {//计时完毕时触发


            ((Button) mView1).setText("重发短信");
            ((Button) mView1).setClickable(true);
            ((Button) mView1).setBackgroundResource(R.drawable.captcha_button_unchecked);
            ((Button) mView1).setTextColor(Color.parseColor("#FFFFFF"));

            ((TextView) mView2).setClickable(true);
            mSound.setText(getString(R.string.sjzcText6));
            ((TextView) mView2).setText("重发语音");

            if (!mMarked) {
                mMarked_ll.setVisibility(View.VISIBLE);
            }

            mCaptchabtnState = false;
            mImage_et.setText("");
            mCaptcha_et.setText("");
            ImageVerification();
        }
    }

    /**
     * EditText 监听事件
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

}
