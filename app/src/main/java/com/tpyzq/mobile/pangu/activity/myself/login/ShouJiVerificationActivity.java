package com.tpyzq.mobile.pangu.activity.myself.login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.HandoverDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.SuccessDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/8/22.
 * 手机验证
 */
public class ShouJiVerificationActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "ShouJiVerification";

    private EditText mSJYZNumber, mSJYZ_ET;
    private Button mSJYZCaptchabtn;
    private LinearLayout LL_Marked;
    private TextView mSJYZLogIn, mSound, mSoundtv;

    private Dialog mLoadingDialog;

    private TimeCount time;
    private TimeCount1 time1;

    private boolean mCaptchabtnState = false;
    private String mIdentify = "";
    private int pageIndex = 0;
    private String destnumber, verifycode;

    @Override
    public void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            mIdentify = intent.getStringExtra("Identify");
            pageIndex = intent.getIntExtra("pageindex", 0);
        }


        findViewById(R.id.SJYZ_back).setOnClickListener(this);
        mSJYZNumber = (EditText) findViewById(R.id.SJYZNumber);            //手机号
        mSJYZ_ET = (EditText) findViewById(R.id.SJYZ_ET);                  //填写验证码
        mSJYZCaptchabtn = (Button) findViewById(R.id.SJYZCaptchabtn);      //短信验证码
        mSound = (TextView) findViewById(R.id.Sound);                        //语音验证码
        mSJYZLogIn = (TextView) findViewById(R.id.SJYZLogIn);                //手机验证按钮

        mSoundtv = (TextView) findViewById(R.id.Soundtv);
        LL_Marked = (LinearLayout) findViewById(R.id.LinearLayout_Marked);
        EditTextMonitor();

    }

    /**
     * EditText的 监听事件
     */
    private void EditTextMonitor() {
        time = new TimeCount(120000, 1000);
        time1 = new TimeCount1(120000, 1000);
        mSJYZ_ET.setOnClickListener(this);
        mSJYZCaptchabtn.setOnClickListener(this);
        mSoundtv.setOnClickListener(this);
        mSJYZNumber.addTextChangedListener(new MyTextWatcher());
        mSJYZ_ET.addTextChangedListener(new MyTextWatcher());

        mSoundtv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    }

    /**
     * 注册按钮状态
     */
    private void initButtonshow() {
        if (mSJYZNumber.getText().toString().equals("")) {
            mSJYZLogIn.setBackgroundResource(R.drawable.button_login_unchecked);
            mSJYZLogIn.setTextColor(Color.parseColor("#ffffffff"));
        } else if (mSJYZ_ET.getText().toString().equals("")) {
            mSJYZLogIn.setBackgroundResource(R.drawable.button_login_unchecked);
            mSJYZLogIn.setTextColor(Color.parseColor("#ffffffff"));
        } else if (mCaptchabtnState == false) {
            mSJYZLogIn.setBackgroundResource(R.drawable.button_login_unchecked);
            mSJYZLogIn.setTextColor(Color.parseColor("#ffffffff"));
        } else {
            mSJYZLogIn.setOnClickListener(this);
            mSJYZLogIn.setBackgroundResource(R.drawable.button_login_pitchon);
            mSJYZLogIn.setTextColor(Color.parseColor("#ffffffff"));
        }
    }

    /**
     * @return 布局
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_shoujiverification;
    }

    /**
     * Button  点击监听
     *
     * @param v ID
     */
    @Override
    public void onClick(View v) {
        String SJYZNumber = mSJYZNumber.getText().toString().trim();
        String SJYZ_ET = mSJYZ_ET.getText().toString().trim();
        switch (v.getId()) {
            case R.id.SJYZ_back:
                finish();
                break;
            case R.id.SJYZCaptchabtn:       //短信
                if (SJYZNumber.equals("")) {
                    Helper.getInstance().showToast(this, "请输入手机号");
                } else if (!Helper.isMobileNO(SJYZNumber)) {
                    Helper.getInstance().showToast(this, "请输入正确的手机号");
                } else {
                    mCaptchabtnState = true;
                    HTTPVerificationCode();
                }
                break;
            case R.id.Soundtv://语音
                if (SJYZNumber.equals("")) {
                    Helper.getInstance().showToast(this, "请输入手机号");
                } else if (!Helper.isMobileNO(SJYZNumber)) {
                    Helper.getInstance().showToast(this, "请输入正确的手机号");
                } else {
                    mCaptchabtnState = true;
                    HTTPVSound();
                }
                break;
            case R.id.SJYZLogIn:
                mLoadingDialog = LoadingDialog.initDialog(this, "正在提交...");
                //显示Dialog
                mLoadingDialog.show();
                if (SJYZ_ET.equals("")) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(this, "请输入验证码");
                } else if (SJYZNumber.equals("")) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(this, "请输入手机号");
                } else if (!Helper.isMobileNO(SJYZNumber)) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(this, "请输入正确的手机号");
                } else {
                    mSJYZLogIn.setEnabled(false);
                    Validatio();

                    break;
                }
        }
    }

    /**
     * 网络请求 语音验证码
     */
    private void HTTPVSound() {
        time1.start();
        HashMap map = new HashMap();
        map.put("phone", mSJYZNumber.getText().toString().trim());

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL_YY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                time1.cancel();
                mSoundtv.setClickable(true);
                mSound.setText(getString(R.string.sjzcText6));
                mSoundtv.setText("重发语音");
                Helper.getInstance().showToast(ShouJiVerificationActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
//                        JSONObject message = jsonObject.getJSONObject("message");
//                        String destnumber = message.getString("destnumber");
//                        String mVerifycode = message.getString("verifycode");
//                        String verifycode = HtmlUtil.delHTMLTag(mVerifycode);
                    } else {
                        MistakeDialog.showDialog(jsonObject.getString("message"), ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                time1.cancel();
                                mSoundtv.setClickable(true);
                                mSound.setText(getString(R.string.sjzcText6));
                                mSoundtv.setText("重发语音");
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 网络请求 短信验证码
     */
    private void HTTPVerificationCode() {
        time.start();
        HashMap map = new HashMap();
        map.put("phone", mSJYZNumber.getText().toString().trim());
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL_SJYZM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("手机短信", e.toString());
                Helper.getInstance().showToast(ShouJiVerificationActivity.this, ConstantUtil.NETWORK_ERROR);
                time.cancel();
                mSJYZCaptchabtn.setText("重发短信");
                mSJYZCaptchabtn.setClickable(true);
                mSJYZCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
                mSJYZCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
//                        Helper.getInstance().showToast(ShouJiZhuCe.this,"发送短信成功");
                    } else {
                        MistakeDialog.showDialog("验证码获取失败", ShouJiVerificationActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                time.cancel();
                                mSJYZCaptchabtn.setText("重发短信");
                                mSJYZCaptchabtn.setClickable(true);
                                mSJYZCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
                                mSJYZCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 手机获取验证码倒计时
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mSJYZCaptchabtn.setText("重发短信");
            mSJYZCaptchabtn.setClickable(true);
            mSJYZCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
            mSJYZCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));
            LL_Marked.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mSJYZCaptchabtn.setClickable(false);
            mSJYZCaptchabtn.setText("重获短信" + millisUntilFinished / 1000);
            mSJYZCaptchabtn.setTextColor(Color.parseColor("#87bd43"));
            mSJYZCaptchabtn.setBackgroundResource(R.drawable.captcha_button_pitchon);
        }
    }


    /**
     * 语音验证码倒计时
     */
    class TimeCount1 extends CountDownTimer {

        public TimeCount1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mSoundtv.setClickable(true);
            mSound.setText(getString(R.string.sjzcText6));
            mSoundtv.setText("重发语音");
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mSoundtv.setClickable(false);
            mSound.setText("如果没有收到短信验证码，请尝试");
            mSoundtv.setText("重获语音" + millisUntilFinished / 1000);
        }
    }


    /**
     * 验证码效验
     */
    private void Validatio() {
        HashMap map = new HashMap();
        map.put("phone", mSJYZNumber.getText().toString().trim());
        map.put("auth", mSJYZ_ET.getText().toString().trim());
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL_VALIDATION, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("验证码效验", e.toString());
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                mSJYZLogIn.setEnabled(true);
                Helper.getInstance().showToast(ShouJiVerificationActivity.this, ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String type = jsonObject.getString("type");
                    String message = jsonObject.getString("message");
                    if ("200".equals(code)) {
                        toConnect();
                    } else {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        mSJYZLogIn.setEnabled(true);
                        Helper.getInstance().showToast(ShouJiVerificationActivity.this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 绑定手机号
     */
    private void toConnect() {
        final HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "800101");
        map.put("token", "");
        map.put("parms", map1);
        map1.put("user_type", KeyEncryptionUtils.getInstance().Typescno());
        map1.put("user_account", UserUtil.userId);
        map1.put("phone", mSJYZNumber.getText().toString().trim());


        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_SJZCBD, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("", e.toString());
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                Helper.getInstance().showToast(ShouJiVerificationActivity.this, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        UserEntity userEntity = new UserEntity();
                        userEntity.setScno(mSJYZNumber.getText().toString().trim());       //注册账号
                        Db_PUB_USERS.UpdateScno(userEntity);                             //修改 注册账号
                        KeyEncryptionUtils.getInstance().localEncryptMobile(mSJYZNumber.getText().toString().trim());//修改手机号

                        if ("1".equals(mIdentify)) {
                            HandoverDialog.showDialog("验证成功", ShouJiVerificationActivity.this);
                        } else if ("2".equals(mIdentify)) {
                            HandoverDialog.showDialog("验证成功", ShouJiVerificationActivity.this);
                        } else {
                            SuccessDialog.showDialog("验证成功", ShouJiVerificationActivity.this);
                        }
                        LogUtil.e(TAG, jsonObject.getString("msg"));
                    } else {
                        mSJYZLogIn.setEnabled(true);
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        MistakeDialog.showDialog(jsonObject.getString("msg"), ShouJiVerificationActivity.this);
//                        Helper.getInstance().showToast(ShouJiVerification.this, jsonObject.getString("msg").toString());
//                        LogUtil.e(TAG, FileUtil.URL_SJZCBD + "{" + map.toString() + "}");
//                        LogUtil.e(TAG, jsonObject.getInt("code") + jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
            if (before == 0) {
                initButtonshow();
            } else {
                initButtonshow();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


}
