package com.tpyzq.mobile.pangu.activity.myself.login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_OPTIONALHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_SEARCHHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.QuerySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToQuerySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.HandoverDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.SuccessDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/8/19.
 * 手机注册获取
 */
public class ShouJiZhuCeActivity extends BaseActivity implements View.OnClickListener, ICallbackResult, WXLogin.Marked {
    private static String TAG = "ShouJiZhuCeActivity";

    private Button mCaptchabtn, mSjLogIn;
    private EditText mSjNumber, mCaptchabtn_ET;
    private TextView mSoundtv, mSound;
    private TimeCount time;
    private TimeCount1 time1;

    private Dialog mLoadingDialog;

    private String mIdentify = "";
    private boolean mCaptchabtnState = false;
    private int pageIndex = 0;
    private int Marker = 0;

    private LinearLayout LL_Marked;
    private EditText mImage_et;
    private SimpleDraweeView mSecurityCode;

    @Override
    public void initView() {
//        FileUtil.auto_flag = true;
        Intent intent = getIntent();
        if (intent != null) {
            mIdentify = intent.getStringExtra("Identify");
            pageIndex = intent.getIntExtra("pageindex", 0);
        }
        findViewById(R.id.SJublish_back).setOnClickListener(this);
        findViewById(R.id.mWeiXin).setOnClickListener(this);
        mCaptchabtn = (Button) findViewById(R.id.Captchabtn);            //短信验证码
        mSound = (TextView) findViewById(R.id.Sound);                      //语音验证码
        mSjLogIn = (Button) findViewById(R.id.SJLogIn);                  //注册
        mSjNumber = (EditText) findViewById(R.id.SjNumber);              //手机号
        mCaptchabtn_ET = (EditText) findViewById(R.id.Captchabtn_ET);   //填写验证码

        mSoundtv = (TextView) findViewById(R.id.Soundtv);
        LL_Marked = (LinearLayout) findViewById(R.id.LinearLayout_Marked);
        mSoundtv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        //填写图片验证码
        mImage_et = (EditText) findViewById(R.id.etImage);
        //图片验证码
        mSecurityCode = (SimpleDraweeView) findViewById(R.id.SecurityCode);


        EditTextMonitor();
    }

    /**
     * EditText的 监听事件
     */
    private void EditTextMonitor() {
        mSecurityCode.setDrawingCacheEnabled(true);
        time = new TimeCount(120000, 1000);
        time1 = new TimeCount1(120000, 1000);
        mCaptchabtn.setOnClickListener(this);
        mSoundtv.setOnClickListener(this);
        mSecurityCode.setOnClickListener(this);
        mSjNumber.addTextChangedListener(new MyTextWatcher());
        mImage_et.addTextChangedListener(new MyTextWatcher());
        mCaptchabtn_ET.addTextChangedListener(new MyTextWatcher());
        mCaptchabtn.setClickable(false);
        mSjLogIn.setOnClickListener(this);
        mSjLogIn.setClickable(false);
        requestData();
    }

    //获取图片验证码
    private void requestData() {
        ImageVerification();
    }

    private void ImageVerification() {

        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.SecurityIps + "/note/getImage", map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Helper.getInstance().showToast(ShouJiZhuCeActivity.this, ConstantUtil.NETWORK_ERROR);
                mSecurityCode.setImageResource(R.mipmap.ic_again);
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");

                    String type = jsonObject.getString("type");
                    if ("0".equals(code)) {
                        String message = jsonObject.getString("message");
                        Bitmap bitmap = Helper.base64ToBitmap(message);
                        mSecurityCode.setImageBitmap(bitmap);
                        mSecurityCode.setAspectRatio(3.2f);
                    } else {
                        Helper.getInstance().showToast(ShouJiZhuCeActivity.this, type.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 注册登录显示状态
     */
    private void initButtonshow() {
        if (TextUtils.isEmpty(mSjNumber.getText().toString())) {
            mSjLogIn.setBackgroundResource(R.drawable.button_login_unchecked);
            mSjLogIn.setTextColor(Color.parseColor("#ffffffff"));

            mCaptchabtn.setClickable(false);
            mCaptchabtn.setTextColor(Color.parseColor("#87bd43"));
            mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_pitchon);

        } else if (TextUtils.isEmpty(mImage_et.getText().toString())) {

            mCaptchabtn.setClickable(false);
            mCaptchabtn.setTextColor(Color.parseColor("#87bd43"));
            mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_pitchon);

            mSjLogIn.setBackgroundResource(R.drawable.button_login_unchecked);
            mSjLogIn.setTextColor(Color.parseColor("#ffffffff"));
        } else if (TextUtils.isEmpty(mCaptchabtn_ET.getText().toString())) {
            if (!mCaptchabtnState) {
                mCaptchabtn.setClickable(true);
                mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
                mCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));
            }

            mSjLogIn.setBackgroundResource(R.drawable.button_login_unchecked);
            mSjLogIn.setTextColor(Color.parseColor("#ffffffff"));
        } else if (!mCaptchabtnState) {
            mSjLogIn.setBackgroundResource(R.drawable.button_login_unchecked);
            mSjLogIn.setTextColor(Color.parseColor("#ffffffff"));
        } else {
            mSjLogIn.setClickable(true);
            mSjLogIn.setBackgroundResource(R.drawable.button_login_pitchon);
            mSjLogIn.setTextColor(Color.parseColor("#ffffffff"));
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_shoujizhuce;
    }

    @Override
    public void onClick(View v) {
        String Captchabtn_ET = mCaptchabtn_ET.getText().toString().trim();
        String jNumber = mSjNumber.getText().toString().trim();
        String mImage_str = mImage_et.getText().toString().trim();
        switch (v.getId()) {
            case R.id.SJublish_back:
                finish();
                break;
            case R.id.SJLogIn:
                mLoadingDialog = LoadingDialog.initDialog(this, "正在提交...");
                //显示Dialog
                mLoadingDialog.show();
                if (jNumber.equals("")) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(this, "请输入手机号");
                } else if (mImage_str.equals("")) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(this, "请输入图片验证码");
                } else if (Captchabtn_ET.equals("")) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(this, "请输手机短信入验证码");
                } else if (!Helper.isMobileNO(jNumber)) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(this, "请输入正确的手机号");
                } else {
                    mSjLogIn.setClickable(false);
                    InscriptionRegistry();
                }
                break;
            case R.id.Captchabtn://短信
                mSoundtv.setClickable(false);
                if (jNumber.equals("")) {
                    mImage_et.setText("");
                    ImageVerification();
                    Helper.getInstance().showToast(this, "请输入手机号");
                } else if (mImage_str.equals("")) {
                    mImage_et.setText("");
                    ImageVerification();
                    Helper.getInstance().showToast(this, "请输入图片验证码");
                } else if (!Helper.isMobileNO(jNumber)) {
                    mImage_et.setText("");
                    ImageVerification();
                    Helper.getInstance().showToast(this, "请输入正确的手机号");
                } else {
                    mCaptchabtnState = true;
                    HTTPVerificationCode();
                }
                break;
            case R.id.Soundtv://语音
                mCaptchabtn.setClickable(false);
                if (jNumber.equals("")) {
                    mImage_et.setText("");
                    ImageVerification();
                    Helper.getInstance().showToast(this, "请输入手机号");
                } else if (mImage_str.equals("")) {
                    mImage_et.setText("");
                    ImageVerification();
                    Helper.getInstance().showToast(this, "请输入图片验证码");
                } else if (!Helper.isMobileNO(jNumber)) {
                    mImage_et.setText("");
                    ImageVerification();
                    Helper.getInstance().showToast(this, "请输入正确的手机号");
                } else {
                    mCaptchabtnState = true;
                    HTTPVSound();
                }
                break;
            case R.id.mWeiXin:
                WXLogin login = new WXLogin(ShouJiZhuCeActivity.this, ShouJiZhuCeActivity.this, Marker);
                Marker = 1;
                break;
            case R.id.SecurityCode:
                ImageVerification();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Marker = 0;
    }


    /**
     * 注册登录
     */
    private void InscriptionRegistry() {
        String mSjNumber_str = mSjNumber.getText().toString().trim();
        String mRegId = SpUtils.getString(this, "RegId", "");

        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map.put("phone", mSjNumber_str);
        map.put("auth", mCaptchabtn_ET.getText().toString());
        map.put("phone_type", "1");  //手机类型 1.安卓 2.苹果 3.其他
        map.put("token", mRegId);
        map.put("user_account", mSjNumber_str);
        map.put("user_type", "1");//账户类型，1：手机，2：qq，3：微信


        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.SecurityIps + "/note/authAndRegister", map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                time.cancel();
                time1.cancel();
                mCaptchabtn.setText("重发短信");
                mCaptchabtn.setClickable(true);
                mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
                mCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));

                mSoundtv.setClickable(true);
                mSound.setText(getString(R.string.sjzcText6));
                mSoundtv.setText("重发语音");
                mCaptchabtnState = false;
                mCaptchabtn_ET.setText("");
                mImage_et.setText("");
                ImageVerification();
                Helper.getInstance().showToast(ShouJiZhuCeActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                response = response.replace("Served at: /Bigdata", "");
                try {
                    JSONObject object = new JSONObject(response);
                    String code_Str = object.getString("code");
                    String msg_Str = object.getString("message");
                    if ("0".equals(code_Str)) { //新用户
                        WipeData();
                        UserEntity userEntity = new UserEntity();
                        userEntity.setScno(mSjNumber.getText().toString().trim());       //注册账号
                        userEntity.setRegisterID(mSjNumber.getText().toString().trim()); //注册账号标识ID
                        userEntity.setIsregister("0");                                   //0是注册用户，1是未注册用户
                        userEntity.setTypescno("1");                                     //账号类型（4:微博 1:手机 2:QQ 3:微信）

                        Db_PUB_USERS.UpdateScno(userEntity);                             //修改 注册账号
                        Db_PUB_USERS.UpdateTypescno(userEntity);                         //修改 账号类型（4:微博 1:手机 2:QQ 3:微信）
                        Db_PUB_USERS.UpdateRegister(userEntity);                         //修改 注册标识ID
                        KeyEncryptionUtils.getInstance().localEncryptMobile(mSjNumber.getText().toString().trim());//修改  加密  手机号

                        //删除数据股所有自选股
                        HOLD_SEQ.deleteAll();
                        SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                        updateNewUserSelfChoice();

                        Db_PUB_USERS.UpdateIsregister(userEntity);                       //修改  是否注册用户

                        SpUtils.putString(ShouJiZhuCeActivity.this, "First", "");

                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }

                        if ("1".equals(mIdentify)) {
                            setData();
                            HandoverDialog.showDialog("切换成功", ShouJiZhuCeActivity.this);
                        } else if ("2".equals(mIdentify)) {
                            HandoverDialog.showDialog("注册成功", ShouJiZhuCeActivity.this);
                        } else {
                            SuccessDialog.showDialog("注册成功", ShouJiZhuCeActivity.this);
                        }
                    } else if (code_Str.equals("1")) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setScno(mSjNumber.getText().toString().trim());        //注册账号
                        userEntity.setIsregister("0");                                    //0是注册用户，1是未注册用户
                        userEntity.setTypescno("1");                                      //账号类型（4:微博 1:手机 2:QQ 3:微信）
                        userEntity.setRegisterID(mSjNumber.getText().toString().trim());  //注册账号标识ID


                        //修改 数据储存
                        Db_PUB_USERS.UpdateScno(userEntity);                             //修改 注册账号
                        Db_PUB_USERS.UpdateIsregister(userEntity);                       //修改  是否注册用户
                        Db_PUB_USERS.UpdateTypescno(userEntity);                         //修改 账号类型（4:微博 1:手机 2:QQ 3:微信）
                        Db_PUB_USERS.UpdateRegister(userEntity);                         //修改 注册账号标识ID
                        KeyEncryptionUtils.getInstance().localEncryptMobile(mSjNumber.getText().toString().trim());//修改  加密  手机号
                        UserUtil.refrushUserInfo();

                        //删除数据股所有自选股
                        HOLD_SEQ.deleteAll();
                        SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                        Db_PUB_STOCKLIST.deleteAllStocListkDatas();                            //从云端下载数据库自选股
                        Db_HOME_INFO.deleteAllSelfNewsDatas();//删除自选股新闻

                        SimpleRemoteControl mSimpleRemoteControl = new SimpleRemoteControl(ShouJiZhuCeActivity.this);
                        mSimpleRemoteControl.setCommand(new ToQuerySelfChoiceStockConnect(new QuerySelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, UserUtil.userId)));
                        mSimpleRemoteControl.startConnect();


                        SpUtils.putString(ShouJiZhuCeActivity.this, "First", "");
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        if ("1".equals(mIdentify)) {
                            setData();
                            HandoverDialog.showDialog("切换成功", ShouJiZhuCeActivity.this);
                        } else if ("2".equals(mIdentify)) {
                            HandoverDialog.showDialog("注册成功", ShouJiZhuCeActivity.this);
                        } else {
                            SuccessDialog.showDialog("注册成功", ShouJiZhuCeActivity.this);
                        }

                    } else {
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                        MistakeDialog.showDialog(msg_Str, ShouJiZhuCeActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                time.cancel();
                                time1.cancel();
                                mCaptchabtn.setText("重发短信");
                                mCaptchabtn.setClickable(true);
                                mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
                                mCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));

                                mSoundtv.setClickable(true);
                                mSound.setText(getString(R.string.sjzcText6));
                                mSoundtv.setText("重发语音");
                                mCaptchabtnState = false;
                                mCaptchabtn_ET.setText("");
                                mImage_et.setText("");
                                ImageVerification();
                            }
                        });
                    }

                } catch (JSONException e) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 当新用户更新自选股
     */
    private void updateNewUserSelfChoice() {
        List<StockInfoEntity> mChartTimeDatas = Db_PUB_STOCKLIST.queryStockListDatas();
        if (mChartTimeDatas != null && mChartTimeDatas.size() > 0) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < mChartTimeDatas.size(); i++) {
                String stockNumber = mChartTimeDatas.get(i).getStockNumber();
                String stockName = mChartTimeDatas.get(i).getStockName();
                String NewPrice = mChartTimeDatas.get(i).getNewPrice();

                sb.append(stockNumber).append(",");
                sb1.append(stockName).append(",");
                sb2.append(NewPrice).append(",");

            }
            String stockNumbers = sb.toString();
            String stockNames = sb1.toString();
            String NewPrices = sb2.toString();

            NewPrices.substring(0, NewPrices.length() - 1);
            UserUtil.refrushUserInfo();

            if (Db_PUB_USERS.isRegister()) {
                Db_PUB_STOCKLIST.deleteAllStocListkDatas();
                Db_HOME_INFO.deleteAllSelfNewsDatas();
            } else {
                SimpleRemoteControl mSimpleRemoteControl = new SimpleRemoteControl(ShouJiZhuCeActivity.this);
                mSimpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockNumbers, UserUtil.userId, stockNames, NewPrices)));
                mSimpleRemoteControl.startConnect();
            }
        }
    }


    /**
     * 网络请求 短信验证码
     */
    private void HTTPVerificationCode() {
        time.start();
        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map.put("phone", mSjNumber.getText().toString().trim());
        map.put("auth", mImage_et.getText().toString().trim());

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.SecurityIps + "/note/imgAuthSms", map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("手机短信", e.toString());
                Helper.getInstance().showToast(ShouJiZhuCeActivity.this, ConstantUtil.NETWORK_ERROR);
                time.cancel();
                mCaptchabtn.setText("重发短信");
                mCaptchabtn.setClickable(true);
                mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
                mCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));
                mCaptchabtnState = false;
                mImage_et.setText("");
                ImageVerification();
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
//                        Helper.getInstance().showToast(ShouJiZhuCe.this,"发送短信成功");
                    } else {
                        MistakeDialog.showDialog("验证码获取失败", ShouJiZhuCeActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                time.cancel();
                                mCaptchabtn.setText("重发短信");
                                mCaptchabtn.setClickable(true);
                                mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
                                mCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));
                                mCaptchabtnState = false;
                                mImage_et.setText("");
                                ImageVerification();
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
     * 网络请求 语音验证码
     */
    private void HTTPVSound() {
        time1.start();
        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map.put("phone", mSjNumber.getText().toString().trim());
        map.put("auth", mImage_et.getText().toString().trim());

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.SecurityIps + "/note/imgAuthVoice?=&=&=", map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                time1.cancel();
                mSoundtv.setClickable(true);
                mSound.setText(getString(R.string.sjzcText6));
                mSoundtv.setText("重发语音");
                Helper.getInstance().showToast(ShouJiZhuCeActivity.this, "网络异常");
                mCaptchabtnState = false;
                mImage_et.setText("");
                ImageVerification();
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
                    } else {
                        MistakeDialog.showDialog(jsonObject.getString("message"), ShouJiZhuCeActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                time1.cancel();
                                mSoundtv.setClickable(true);
                                mSound.setText(getString(R.string.sjzcText6));
                                mSoundtv.setText("重发语音");
                                mCaptchabtnState = false;
                                mImage_et.setText("");
                                ImageVerification();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void getResult(Object result, String tag) {
        if ("AddSelfChoiceStockConnect".equals(tag)) {
            String msg = (String) result;
            SelfStockHelper.explanOneTimiceAddSelfChoiceResult(ShouJiZhuCeActivity.this, msg);
        } else if ("QuerySelfChoiceStockConnect".equals(tag)) {
            //老用户
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            if (result instanceof String) {
                MistakeDialog.showDialog("导入自选股异常" + result.toString(), ShouJiZhuCeActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {

                    }
                });

            } else {
                //把查询到的股票添加到数据库
                ArrayList<StockInfoEntity> stockInfoEntities = (ArrayList<StockInfoEntity>) result;

                if (stockInfoEntities != null && stockInfoEntities.size() > 0) {
                    for (StockInfoEntity stockInfoEntity : stockInfoEntities) {
                        Db_PUB_STOCKLIST.addOneStockListData(stockInfoEntity);
                        SelfStockHelper.sendUpdateSelfChoiceBrodcast(ShouJiZhuCeActivity.this, stockInfoEntity.getStockNumber());
                    }
                }
            }
        }
    }

    //切换手机把之前的数据清空
    private void WipeData() {
        UserEntity user = new UserEntity();
        user.setMobile("");
        user.setScno("");
        user.setRegisterID("");
        user.setTypescno("");
        user.setTradescno("");

        Db_PUB_USERS.UpdateMobile(user);
        Db_PUB_USERS.UpdateScno(user);
        Db_PUB_USERS.UpdateRegister(user);
        Db_PUB_USERS.UpdateTypescno(user);
        Db_PUB_USERS.UpdateTradescno(user);
    }

    @Override
    public void MarkedLogic(boolean isMarked) {
        if (isMarked) {
            if ("1".equals(mIdentify)) {
                seWXtData();
                HandoverDialog.showDialog("切换成功", this);
            } else if ("2".equals(mIdentify)) {
                HandoverDialog.showDialog("注册成功", this);
            } else {
                SuccessDialog.showDialog("注册成功", this);
            }
        } else {
            Marker = 0;
            Helper.getInstance().showToast(this, "微信登录失败");
        }
    }


    /**
     * 短信验证码倒计时
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mCaptchabtn.setText("重发短信");
            mCaptchabtn.setClickable(true);
            mSoundtv.setClickable(true);
            mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_unchecked);
            mCaptchabtn.setTextColor(Color.parseColor("#FFFFFF"));
            LL_Marked.setVisibility(View.VISIBLE);
            mCaptchabtnState = false;
            mImage_et.setText("");
            ImageVerification();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mCaptchabtn.setClickable(false);
            mCaptchabtn.setText("重获短信" + millisUntilFinished / 1000);
            mCaptchabtn.setTextColor(Color.parseColor("#87bd43"));
            mCaptchabtn.setBackgroundResource(R.drawable.captcha_button_pitchon);
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
            mCaptchabtn.setClickable(true);
            mSound.setText(getString(R.string.sjzcText6));
            mSoundtv.setText("重发语音");
            mCaptchabtnState = false;
            mImage_et.setText("");
            ImageVerification();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mSoundtv.setClickable(false);
            mSound.setText("如果没有收到短信验证码，请尝试");
            mSoundtv.setText("重获语音" + millisUntilFinished / 1000);
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


    //清空数据库数据
    private void seWXtData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setIslogin("false");
        userEntity.setTradescno("");
        userEntity.setMobile("");

        SpUtils.putString(CustomApplication.getContext(), "mSession", "");
        Db_PUB_USERS.UpdateMobile(userEntity);
        Db_PUB_USERS.UpdateIslogin(userEntity);
        Db_PUB_USERS.UpdateTradescno(userEntity);
        Db_PUB_SEARCHHISTORYSTOCK.deleteAllDatas();
        Db_PUB_OPTIONALHISTORYSTOCK.deleteAllDatas();
        Db_PUB_USERS.clearRefreshTime();

    }

    //清空数据库数据
    private void setData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setIslogin("false");
        userEntity.setTradescno("");

        SpUtils.putString(CustomApplication.getContext(), "mSession", "");
        Db_PUB_USERS.UpdateIslogin(userEntity);
        Db_PUB_USERS.UpdateTradescno(userEntity);
        Db_PUB_SEARCHHISTORYSTOCK.deleteAllDatas();
        Db_PUB_OPTIONALHISTORYSTOCK.deleteAllDatas();
        Db_PUB_USERS.clearRefreshTime();
    }

    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        this.finish();
    }

}
