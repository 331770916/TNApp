package com.tpyzq.mobile.pangu.activity.myself.login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.ManagerMoenyDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.ProductBuyActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.AssetsAnalysisActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.FinancialLifeActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.StockBillActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.StockRecallActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.TradingDynamicsActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.UsableCapitalActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AccountPowerActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementSignActvity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.ChangePasswordActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.PersonalDataActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.StockHolderInfoActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundInfoActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundOpenAccountActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundPurchaseActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundRedemptionActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundSubsActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundWithDrawActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.OpenFundActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_AccountActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ElectronicContractActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_QueryActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_RedeemActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_RevokeActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_SubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_SubscriptionActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BankBusinessIndexActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BanksTransferAccountsActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BuyAndSellActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ChangNeiFundActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.MySubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.NewStockSubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.OneKeySubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ReferActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ReverseRepoGuideActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.RevokeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TakeAPositionActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TranMoreActivity;
import com.tpyzq.mobile.pangu.adapter.myself.PopupWindowAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.login.AddUserConnect;
import com.tpyzq.mobile.pangu.http.doConnect.login.KeyBoardConnect;
import com.tpyzq.mobile.pangu.http.doConnect.login.KeyBoardTypeConnect;
import com.tpyzq.mobile.pangu.http.doConnect.login.LogInConnect;
import com.tpyzq.mobile.pangu.http.doConnect.login.SecurityCodeConnect;
import com.tpyzq.mobile.pangu.http.doConnect.login.ToAddUser;
import com.tpyzq.mobile.pangu.http.doConnect.login.ToKeyBoard;
import com.tpyzq.mobile.pangu.http.doConnect.login.ToKeyBoardType;
import com.tpyzq.mobile.pangu.http.doConnect.login.ToLogIn;
import com.tpyzq.mobile.pangu.http.doConnect.login.ToSecurityCode;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.keyboard.Constants;
import com.tpyzq.mobile.pangu.util.keyboard.HandleResponse;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.keyboard.PasswordKeyboardUtils;
import com.tpyzq.mobile.pangu.util.keyboard.ResponseInterface;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.CertificationDialog;
import com.tpyzq.mobile.pangu.view.dialog.CustomDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoginDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.MutualAuthenticationDialog;
import com.yzd.unikeysdk.OnInputDoneCallBack;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;
import com.yzd.unikeysdk.UnikeyUrls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by wangqi on 2016/8/18.
 * 绑定并更换帐号界面
 */
public class ChangeAccoutActivity extends BaseActivity implements ICallbackResult, View.OnClickListener, Handler.Callback {
    private static String TAG = "BindMore";
    private EditText mAccount_et;
    private EditText mPassword_et;
    private EditText mCaptcha_et;
    private Button mLogin_but;
    private ImageView mZhang_iv;
    private ImageView mClose_iv;
    private SimpleDraweeView mSecurityCode;
//    private TextView mCxaptcha_tv;

    //接口数据
    private String OLD_SRRC, OLD_TCC;
    private String IS_OVERDUE;
    private String CORP_RISK_LEVEL;
    private String CORP_END_DATE;
    private com.yzd.unikeysdk.PasswordKeyboard passwordKeyboard;
    private String mKeyboardInput;

    private UserEntity mUserEntity;

    private Bundle newintent;
    private String mVerificationcode;

    private boolean mExit;
    private int pageIndex = 0;
    private int mMarkbit = 0;
    private boolean isHttp = false;
    private String mIp = "";
    private String diviceId;
    private UnikeyUrls unikeyUrls;
    private boolean isLoginSuc = false;
    private boolean isKeyStatusControl = false;

    private CustomDialog.Builder mBuilder;
    private String LOADING = "加载中...";
    private String DOWNLOAD = "下载插件中...";
    private String ISCOMMIT = "正在提交...";

    private String passwordFormat;
    private String mSession;

    private Handler handler = new Handler(this);
    private Dialog mCommit;
    private SimpleRemoteControl simpleRemoteControl;

    DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                if (mBuilder.dialog != null && mBuilder.dialog.isShowing())
                    mBuilder.dialog.dismiss();
                if (mCommit != null && mCommit.isShowing())
                    mCommit.dismiss();
                OkHttpUtil.cancelSingleRequestByTag(ChangeAccoutActivity.this.getClass().getName());

                isLoginSuc = false;
                toSecurityCode();
//                        mPassword.setText("");
                mPassword_et.setText("");
                mCaptcha_et.setText("");
                mPassword_et.requestFocus();
                handler.sendEmptyMessage(0);
                showKeyboardWithHeader();

            }
            return false;
        }
    };

    @Override
    public void initView() {
        mCommit = LoadingDialog.initDialog(this, "正在提交...");
        mBuilder = new CustomDialog.Builder(ChangeAccoutActivity.this);
        mBuilder.create();

        mCommit.setOnKeyListener(onKeyListener);
        mBuilder.dialog.setOnKeyListener(onKeyListener);
        simpleRemoteControl = new SimpleRemoteControl(this);
        //绑定资金账号
        mAccount_et = (EditText) findViewById(R.id.BDAccount);
        //交易密码_明文
        mPassword_et = (EditText) findViewById(R.id.BDPasswordET);
        //填写验证码
        mCaptcha_et = (EditText) findViewById(R.id.BDCaptcha);
        //登录
        mLogin_but = (Button) findViewById(R.id.Bindingbtn);
        //历史账号
        mZhang_iv = (ImageView) findViewById(R.id.ZhangIV);
        //清除
        mClose_iv = (ImageView) findViewById(R.id.CloseIV);
        //验证码
        mSecurityCode = (SimpleDraweeView) findViewById(R.id.BDSecurityCode);

        findViewById(R.id.BMService).setOnClickListener(this);       //客服服务
        findViewById(R.id.BMublish_back).setOnClickListener(this);       //返回
        getIntentData();
        initLogic();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bindmore;
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            pageIndex = intent.getIntExtra("pageindex", 0);
            newintent = intent.getExtras();
        }
        mExit = getIntent().getBooleanExtra("EXIT", false);

    }

    private void initLogic() {
        mUserEntity = new UserEntity();
        inquireCertification();
        control();
        KeyListener();
        monitor();
        mAccount_et.setSelection(mAccount_et.getText().length());
        mCaptcha_et.setSelection(mCaptcha_et.getText().length());

        //验证码请求
        toSecurityCode();
        HttpIP();
        IsKeyboardRequestHttp();
    }

    private void HttpIP() {
        OkHttpUtils.get().url("http://ip.taobao.com/service/getIpInfo.php?ip=myip")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject data = json.getJSONObject("data");
                    mIp = data.getString("ip");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //网络请求图片验证码
    private void toSecurityCode() {
        mBuilder.setTitle(LOADING);
        mBuilder.create().show();
        simpleRemoteControl.setCommand(new ToSecurityCode(new SecurityCodeConnect(this, TAG)));
        simpleRemoteControl.startConnect();
    }

    //初始化的数据展示
    private void inquireCertification() {
        if (!mExit) {
            mUserEntity.setIslogin("false");
            Db_PUB_USERS.UpdateIslogin(mUserEntity);
            SpUtils.putString(ChangeAccoutActivity.this, "mSession", "");
        }

        mAccount_et.setHint("请输入账号");
        if (!TextUtils.isEmpty(UserUtil.capitalAccount)) {
            mAccount_et.setText(UserUtil.capitalAccount);
            mPassword_et.setFocusable(true);
            mPassword_et.setFocusableInTouchMode(true);
            mPassword_et.requestFocus();
        } else {
            mAccount_et.setFocusable(true);
            mAccount_et.setFocusableInTouchMode(true);
            mAccount_et.requestFocus();
        }

        if (!TextUtils.isEmpty(mAccount_et.getText().toString().trim()) && !TextUtils.isEmpty(UserUtil.capitalAccount)) {
            mClose_iv.setVisibility(View.VISIBLE);
        } else {
            mClose_iv.setVisibility(View.GONE);
        }
    }

    //删除控件展示
    private void control() {
        if (!TextUtils.isEmpty(mAccount_et.getText().toString().trim()) && !TextUtils.isEmpty(UserUtil.capitalAccount)) {
            mClose_iv.setVisibility(View.VISIBLE);
        } else {
            mClose_iv.setVisibility(View.GONE);
        }
    }

    private void KeyListener() {
        mPassword_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (mMarkbit == 0) {
                        if (!isKeyStatusControl) {
                            mBuilder.setTitle(LOADING);
                            mBuilder.create().show();
                        }

                        mMarkbit = 1;
                    }
                    if (mMarkbit == 1) {
                        mMarkbit = 1;
                        if ("1".equals(passwordFormat)) {
                            mPassword_et.setFocusable(true);
                            handler.sendEmptyMessage(0);
                            showKeyboardWithHeader();
                        }
                    }
                }

                return false;
            }
        });
    }

    private void IsKeyboardRequestHttp() {
        mBuilder.setTitle(LOADING);
        mBuilder.create().show();
        simpleRemoteControl.setCommand(new ToKeyBoard(new KeyBoardConnect(this, TAG)));
        simpleRemoteControl.startConnect();
    }

    private void IsKeyBoardTypeHttp() {
        mBuilder.setTitle(LOADING);
        mBuilder.create().show();
        simpleRemoteControl.setCommand(new ToKeyBoardType(new KeyBoardTypeConnect(this, TAG)));
        simpleRemoteControl.startConnect();
    }

    private void addAccountData() {
        mBuilder.setTitle(LOADING);
        mBuilder.create().show();
        simpleRemoteControl.setCommand(new ToAddUser(new AddUserConnect(this, TAG, mAccount_et.getText().toString())));
        simpleRemoteControl.startConnect();
    }

    //网络请求交易登录
    private void toLogInConnect() {
        if (TextUtils.isEmpty(UserUtil.Mobile)) {
            MistakeDialog.showDialog("登录入参异常", this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setMobile("");
                    userEntity.setIsregister("");
                    userEntity.setScno("");
                    userEntity.setTypescno("");
                    userEntity.setRegisterID("");
                    userEntity.setIslogin("");


                    Db_PUB_USERS.UpdateMobile(userEntity);
                    Db_PUB_USERS.UpdateIsregister(userEntity);
                    Db_PUB_USERS.UpdateScno(userEntity);
                    Db_PUB_USERS.UpdateTypescno(userEntity);
                    Db_PUB_USERS.UpdateRegister(userEntity);
                    Db_PUB_USERS.UpdateIslogin(userEntity);

                    finish();
                }
            });
        } else {
            isLoginSuc = false;
            mBuilder.setTitle(ISCOMMIT);
            mBuilder.create().show();
            String unikey = "0";
            try {
                unikey = UniKey.getInstance().getUnikeyId();
            } catch (UnikeyException e) {
                e.printStackTrace();
            }
            if ("0".equals(passwordFormat)) {
                mKeyboardInput = mPassword_et.getText().toString();
            } else {
                mKeyboardInput = mKeyboardInput;
            }
            simpleRemoteControl.setCommand(new ToLogIn(new LogInConnect(this, TAG, unikey, passwordFormat, mAccount_et.getText().toString(), mKeyboardInput)));
            simpleRemoteControl.startConnect();
        }
    }


    private void LogInLogic() {
        //第一次登录数据库交易账号无数据 添加到数据库
        if (!"".equals(OLD_SRRC) && !DeviceUtil.getDeviceId(CustomApplication.getContext()).equalsIgnoreCase(OLD_TCC) && !android.os.Build.MODEL.equals(OLD_SRRC)) { //换手机登录
            LoginDialog.showDialog("您更换了登录设备，上次使用的设备型号是" + OLD_SRRC, ChangeAccoutActivity.this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    if ("2".equalsIgnoreCase(IS_OVERDUE) || "3".equalsIgnoreCase(IS_OVERDUE)) {
                        //未做或过期弹出风险评测dialog
                        showCorpDialog();
                    } else {
                        finish();
                    }
                }
            });
            Gather();
        } else {//没有更换手机
            showDialogOrSaveData();
            Gather();
        }
    }


    //初始化点击事件
    private void monitor() {
        mClose_iv.setOnClickListener(this);
        mLogin_but.setOnClickListener(this);

        mZhang_iv.setOnClickListener(this);
        mLogin_but.setClickable(false);

        mAccount_et.addTextChangedListener(new MyTextWatcher());
        mPassword_et.addTextChangedListener(new MyTextWatcher());
        mCaptcha_et.addTextChangedListener(new MyTextWatcher());
        mSecurityCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.BMublish_back:
                if (!mExit) {
                    mUserEntity.setIslogin("false");
                    Db_PUB_USERS.UpdateIslogin(mUserEntity);
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.BMService:            //客服服务
                intent.setClass(this, HotlineActivity.class);
                startActivity(intent);
                break;
            case R.id.CloseIV:              //删除
                mAccount_et.setText("");
                break;
            case R.id.Bindingbtn:         //登录
                mLogin_but.setClickable(false);
                if (mCaptcha_et.getText().toString().trim().equalsIgnoreCase(mVerificationcode)) {
                    toLogInConnect();
                } else {
                    Helper.getInstance().showToast(ChangeAccoutActivity.this, "验证码错误");
                    toSecurityCode();
                    mPassword_et.setText("");
                    mCaptcha_et.setText("");
                }
                break;
            case R.id.ZhangIV:          //历史账号
                showPopupWindow();
                break;
            case R.id.BDSecurityCode:   //验证码
                toSecurityCode();
                break;
        }
    }


    @Override
    public void getResult(Object result, String tag) {
        if (KeyBoardConnect.TAG.equals(tag)) {
            isKeyStatusControl = true;
            if (mBuilder.dialog.isShowing())
                mBuilder.dialog.dismiss();
            if (result instanceof Boolean) {
                if ((Boolean) result) {
                    mUserEntity.setKeyboard("true");
                    UserUtil.Keyboard = "1";
                } else {
                    mUserEntity.setKeyboard("false");
                    UserUtil.Keyboard = "0";
                }
                Db_PUB_USERS.UpdateKeyboard(mUserEntity);
                if (ConstantUtil.USERFUL_KEYBOARD) {
                    initKeyBoard((Boolean) result);
                } else {
                    initKeyBoard(false);
                }
            }
        } else if (KeyBoardTypeConnect.TAG.equals(tag)) {
            mBuilder.dialog.dismiss();
            if (result instanceof Boolean) {
                if ((Boolean) result) {
                    mUserEntity.setKeyboard("false");
                    mUserEntity.setCertification("false");
                    Db_PUB_USERS.UpdateKeyboard(mUserEntity);
                    Db_PUB_USERS.UpdateCertification(mUserEntity);
                    setPassEdit();
                } else {
                    if (isHttp) {
                        if (!ChangeAccoutActivity.this.isFinishing()) {
                            MutualAuthenticationDialog.showDialog("双向认证失败点击退出", ChangeAccoutActivity.this);
                        }
                    } else {
                        if (!ChangeAccoutActivity.this.isFinishing()) {
                            MutualAuthenticationDialog.showDialog("系统异常", ChangeAccoutActivity.this);
                        }
                    }
                }
            }
        } else if (SecurityCodeConnect.TAG.equals(tag)) {
            mBuilder.dialog.dismiss();
            if (result instanceof String && SecurityCodeConnect.MSG.equals(result)) {
                mSecurityCode.setImageResource(R.mipmap.ic_again);
                Helper.getInstance().showToast(ChangeAccoutActivity.this, result.toString());
            } else if (result instanceof String && ConstantUtil.NETWORK_ERROR.equals(result)) {
                mSecurityCode.setImageResource(R.mipmap.ic_again);
                Helper.getInstance().showToast(ChangeAccoutActivity.this, ConstantUtil.NETWORK_ERROR);
            } else {
                try {
                    JSONObject jsonObject = (JSONObject) result;
                    mVerificationcode = jsonObject.getString("VERIFICATIONCODE");
                    Bitmap bitmap = Helper.base64ToBitmap(jsonObject.getString("VERIFICATIONIMAGE"));
                    if (bitmap != null && bitmap.equals(bitmap)) {
                        mSecurityCode.setVisibility(View.VISIBLE);
                        mSecurityCode.setImageBitmap(bitmap);
                        mSecurityCode.setAspectRatio(3.2f);
                    } else {
                        mSecurityCode.setImageResource(R.mipmap.ic_again);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (LogInConnect.TAG.equals(tag)) {
            mBuilder.dialog.dismiss();
            if (result instanceof String) {
                if (ConstantUtil.NETWORK_ERROR.equals(result)) {
                    Helper.getInstance().showToast(ChangeAccoutActivity.this, result.toString());
                    mPassword_et.setText("");
                    mCaptcha_et.setText("");
                } else if (LogInConnect.MSG.equals(tag)) {
                    Helper.getInstance().showToast(ChangeAccoutActivity.this, result.toString() + ",请重新输入");
                    mPassword_et.setText("");
                    mCaptcha_et.setText("");
                } else {
                    mPassword_et.setText("");
                    mCaptcha_et.setText("");
                    MistakeDialog.showDialog(result, ChangeAccoutActivity.this);
                }
                return;
            }
            isLoginSuc = true;
            JSONArray result_ary = (JSONArray) result;
            for (int i = 0; i < result_ary.length(); i++) {
                try {
                    mSession = result_ary.getJSONObject(i).getString("SESSION");
                    OLD_SRRC = result_ary.getJSONObject(i).getString("OLD_SRRC");
                    OLD_TCC = result_ary.getJSONObject(i).getString("OLD_TCC");

                    JSONObject Session = (JSONObject) result_ary.get(i);
                    IS_OVERDUE = Session.optString("IS_OVERDUE");//风险评测状态 0正常 1即将过期 2过期 3未做
                    CORP_RISK_LEVEL = Session.optString("CORP_RISK_LEVEL");//客户当前风险承受等级
                    CORP_END_DATE = Session.optString("CORP_END_DATE");//风险测评有效期到期时间

                    getAccountData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (AddUserConnect.TAG.equals(tag)) {
            if (result instanceof String) {
                mBuilder.dialog.dismiss();
                if (ConstantUtil.NETWORK_ERROR.equals(result)) {
                    isLoginSuc = false;
                    Helper.getInstance().showToast(ChangeAccoutActivity.this, result.toString());
                    mPassword_et.setText("");
                    mCaptcha_et.setText("");
                } else if ("0".equals(result)) {
                    HOLD_SEQ.deleteAll();
                    SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                    setAccountData();                      //修改资金账号数据
                } else {
                    isLoginSuc = false;
                    MistakeDialog.showDialog(result.toString(), ChangeAccoutActivity.this);
                    mPassword_et.setText("");
                    mCaptcha_et.setText("");
                }
            }
        }
    }

    /**
     * 显示弹框，绑定账号请求完成后不关闭页面
     * 不显示弹框，绑定账号请求完成后关闭页面
     */
    private void showDialogOrSaveData() {
        if ("2".equalsIgnoreCase(IS_OVERDUE) || "3".equalsIgnoreCase(IS_OVERDUE)) {
            //弹出风险评测dialog
            showCorpDialog();

        } else {
//            if (mCommit.isShowing()) {
//                mCommit.dismiss();
//            }
            finish();
        }
    }

    /**
     * 弹出风险测评弹框
     */
    private void showCorpDialog() {
//        if (mCommit != null) {
//            mCommit.dismiss();
//        }
        int style = 1000;
        if ("2".equalsIgnoreCase(IS_OVERDUE)) {
            //过期
            style = 2000;
        } else {//3的情况  未做
            style = 3000;
        }
        CancelDialog.cancleDialog(ChangeAccoutActivity.this, CORP_END_DATE, style, new CancelDialog.PositiveClickListener() {
            @Override
            public void onPositiveClick() {
                //现在测试按钮
                isLoginSuc = false;
                Intent intent = new Intent(ChangeAccoutActivity.this, RiskEvaluationActivity.class);
                intent.putExtra("isLogin", true);
                ChangeAccoutActivity.this.startActivity(intent);
                finish();
            }
        }, new CancelDialog.NagtiveClickListener() {
            @Override
            public void onNagtiveClick() {
                //以后再说
                    /*isLoginSuc = false;
                    UserEntity userEntity=new UserEntity();
                    userEntity.setIslogin("false");
                    Db_PUB_USERS.UpdateIslogin(userEntity);*/
                finish();
            }
        });
    }

    //判断 在数据库是否存在 资金账号
    private void getAccountData() {
        //查询资金账号
        List<UserEntity> list = KeyEncryptionUtils.getInstance().localDecryptTradescno();
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).getTradescno()).append(",");
            }
        }
        String mTradescno = sb.toString();
        if (TextUtils.isEmpty(mTradescno)) {
            addAccountData();
        } else {
            if (mTradescno.contains(mAccount_et.getText().toString())) {
                setAccountData();
            } else {
                addAccountData();
            }
        }
    }

    //博睿埋点
    private void Gather() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = mDateFormat.format(new java.util.Date());
        BRutil.menuLogIn(android.os.Build.VERSION.RELEASE, UserUtil.Mobile, DeviceUtil.getDeviceId(CustomApplication.getContext()), APPInfoUtils.getVersionName(ChangeAccoutActivity.this), mIp, UserUtil.capitalAccount, date);
    }

    //修改数据库资金账号并且修改登录状态
    private void setAccountData() {

        SpUtils.putString(ChangeAccoutActivity.this, "mSession", mSession);

        String mAccount_Str = mAccount_et.getText().toString().trim();
        //查询资金账号
        List<UserEntity> list = KeyEncryptionUtils.getInstance().localDecryptTradescno();
        String tradescno = "";
        if (list.size() > 0) {
            tradescno = list.get(0).getTradescno();
        }
        if (tradescno.contains(mAccount_Str)) {         //数据库数据是否包含 输入字段
            if (!tradescno.contains(",")) {             //数据库数据是否包含","
                tradescno = tradescno.replace(mAccount_Str, "");
                KeyEncryptionUtils.getInstance().localEncryptTradescno(mAccount_Str + "," + tradescno);
            } else {
                tradescno = tradescno.replace(mAccount_Str + ",", "");
                KeyEncryptionUtils.getInstance().localEncryptTradescno(mAccount_Str + "," + tradescno);
            }
        } else {
            KeyEncryptionUtils.getInstance().localEncryptTradescno(mAccount_Str + "," + tradescno);
        }

        //修改是否登录
        mUserEntity.setIslogin("true");
        //修改资金账号
        Db_PUB_USERS.UpdateIslogin(mUserEntity);
        LogInLogic();

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 0) {
            PasswordKeyboardUtils.hideSoftKeyboard(mAccount_et, ChangeAccoutActivity.this);
            PasswordKeyboardUtils.hideSoftKeyboard(mPassword_et, ChangeAccoutActivity.this);
            PasswordKeyboardUtils.hideSoftKeyboard(mCaptcha_et, ChangeAccoutActivity.this);
        } else if (msg.what == 1) {
            if ("0".equals(UserUtil.Keyboard)) {
//                PasswordKeyboardUtils.showSoftKeyboar(mPassword_et, ChangeAccoutActivity.this);
            } else {
                showKeyboardWithHeader();
            }

        }
        return true;
    }


    private void initKeyBoard(boolean status) {
        if (status) {
            getUnikey();
            if (PasswordKeyboardUtils.getKeyBoard(unikeyUrls, diviceId)) {
                biAuthBtnClick();       //双向认证
            } else {
                Lomboz();               //下载插件
            }
            setPassEdit();
            KeyListener();
            if (!TextUtils.isEmpty(mAccount_et.getText().toString())) {
                showKeyboardWithHeader();
            }
        } else {
            setPassEdit();
            handler.sendEmptyMessage(1);
        }

    }

    /**
     * 弹出 绑定资金账号ListView列表
     */
    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_listview, null);
        ListView mPWListView = (ListView) view.findViewById(R.id.PopupWindowListView);

        //查询资金账号
        List<UserEntity> userEntities = KeyEncryptionUtils.getInstance().localDecryptTradescno();
        final List<String> list = new ArrayList<>();
        String[] s = userEntities.get(0).getTradescno().split(",");
        if ("".equals(s[0])) {

        } else {
            for (int i = 0; i < s.length; i++) {
                list.add(s[i]);
            }
        }
        //弹出 资金账号列表
        PopupWindowAdapter adapter = new PopupWindowAdapter(this);
        adapter.setData(list);
        mPWListView.setAdapter(adapter);
        mPWListView.setEmptyView(view.findViewById(R.id.tv_null));
        // 创建一个PopuWidow对象
        final PopupWindow popupWindow = new PopupWindow(view, TransitionUtils.dp2px(290, this), TransitionUtils.dp2px(150, this), true);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xf0ffffff));
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(mAccount_et, TransitionUtils.dp2px(60, this), TransitionUtils.dp2px(15, this));
        mPWListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow != null) {
                    mAccount_et.addTextChangedListener(new MyTextWatcher());
                    mAccount_et.setText(list.get(position));
                    mAccount_et.setSelection(list.get(position).length());
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 初始化密码键盘
     */
    private void getUnikey() {
        diviceId = DeviceUtil.getDeviceId(CustomApplication.getContext());
        try {
            //初始化加密键盘
            unikeyUrls = new UnikeyUrls(Constants.APPLY_PLUGIN, Constants.GET_CHALLEAGE, Constants.GET_BIT_AUTH_DATA, Constants.CHECK_BIT_AUTH_DATA, Constants.VERIFY_APP, Constants.CONFIRM_APPLY_PLUGIN_FORMAT);
        } catch (UnikeyException e) {
            ToastUtils.showShort(this, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 插件下载
     */
    private void Lomboz() {
        mBuilder.dialog.dismiss();
        mBuilder.setTitle(DOWNLOAD);
        mBuilder.create().show();
        HandleResponse.HandleResponseHelper(getPluginDownloadReqHandler(), Constants.APPLY_PLUGIN_REQ);
    }

    /**
     * 双向认证
     */
    private void biAuthBtnClick() {
        mBuilder.dialog.dismiss();
        mBuilder.setTitle(LOADING);
        mBuilder.create().show();
        HandleResponse.HandleResponseHelper(BiAuthFirstInterface(), Constants.Bi_AUTH_FIRST_REQ);
    }

    /**
     * 双向认证回调
     *
     * @return
     */
    private ResponseInterface BiAuthFirstInterface() {
        mBuilder.dialog.dismiss();
        return new ResponseInterface() {
            @Override
            public void onGetResponse(Object response) {
                if (response == null) {
                    mUserEntity.setCertification("false");
                    Db_PUB_USERS.UpdateCertification(mUserEntity);
                    return;
                }
                String authData = String.valueOf(response);
                if ("true".equals(authData)) {
                    mUserEntity.setCertification("true");
                    Db_PUB_USERS.UpdateCertification(mUserEntity);
                    setPassEdit();
                } else {
                    if (!ChangeAccoutActivity.this.isFinishing()) {
                        CertificationDialog.showDialog("提示信息", "双向认证网络异常", "重新请求", "取消", ChangeAccoutActivity.this, new CertificationDialog.PositiveCliceListener() {
                            @Override
                            public void positiveClick(View v) {
                                biAuthBtnClick();
                            }
                        }, new CertificationDialog.CancleClickListener() {
                            @Override
                            public void cancleClick(View v) {
                                isHttp = true;
                                IsKeyBoardTypeHttp();
                            }
                        });
                    }
                }
            }
        };
    }

    /**
     * 下载插件回调
     *
     * @return
     */
    private ResponseInterface getPluginDownloadReqHandler() {
        return new ResponseInterface() {
            @Override
            public void onGetResponse(Object response) {
                //初始化用户对象修改数据库
                UserEntity userEntity = new UserEntity();
                if (response instanceof Boolean && (Boolean) response) {        //如果回调对象是boolean类型 且为true
                    userEntity.setPlugins("true");
                    Db_PUB_USERS.UpdatePlugins(userEntity);
                    biAuthBtnClick();
                    Helper.getInstance().showToast(ChangeAccoutActivity.this, "下载插件成功");
                } else {            //下载失败后的参数
                    mBuilder.dialog.dismiss();
                    if (!ChangeAccoutActivity.this.isFinishing()) {
                        //重复下载提示
                        CertificationDialog.showDialog("提示信息", "安全插件加载失败，为了您的安全考虑希望您重下载。", "重新下载", "取消", ChangeAccoutActivity.this, new CertificationDialog.PositiveCliceListener() {
                            @Override
                            public void positiveClick(View v) {
                                Lomboz();
                            }
                        }, new CertificationDialog.CancleClickListener() {
                            @Override
                            public void cancleClick(View v) {
                                isHttp = true;
                                IsKeyBoardTypeHttp();
                            }
                        });
                    }
                    userEntity.setPlugins("false");
                    Db_PUB_USERS.UpdatePlugins(userEntity);
                }
            }
        };
    }

    /**
     * 键盘插件数据获取
     */
    private void showKeyboardWithHeader() {

        try {
            //显示当前用户已经输入的字符串个数
            passwordKeyboard = UniKey.getInstance().getPasswordKeyboard(this, new OnInputDoneCallBack() {
                @Override
                public void getInputEncrypted(String s) {
                    mKeyboardInput = s;
                }

                @Override
                public void getCharNum(int i) {
                    //显示当前用户已经输入的字符串个数
                    LogUtil.d(TAG, "num:" + i);
                    String s = "";
                    for (int a = 0; a < i; a++) {
                        s += "*";
                    }
                    mPassword_et.setText(s);
                    mPassword_et.setSelection(mPassword_et.getText().length());
                }
            }, 6, true, "custom_keyboard_view");
            passwordKeyboard.show();
        } catch (UnikeyException e) {
            Helper.getInstance().showToast(ChangeAccoutActivity.this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
        }
    }

    /**
     * 判断是否密码键盘还是普通键盘处理
     *
     * @param : 1 密文  0 明文
     */
    private void setPassEdit() {
        UserUtil.refrushUserInfo();
        if ("0".equals(UserUtil.Keyboard)) {
            mPassword_et.setVisibility(View.VISIBLE);
            passwordFormat = "0";
        } else {
            mPassword_et.setVisibility(View.VISIBLE);
            passwordFormat = "1";
        }
    }


    @Override
    public void finish() {
        if (isLoginSuc && pageIndex > 0) {
            gotoPage();
        }
        super.finish();
    }

    private void gotoPage() {
        try {
            Intent intent = getIntent();
            switch (pageIndex) {
                case TransactionLoginActivity.PAGE_INDEX_BUY_SELL://买卖交易
                    intent.putExtra("status", newintent.getSerializable("status"));
                    intent.setClass(this, BuyAndSellActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_PRODUCTBUY://精选理财
                    intent.setClass(this, ProductBuyActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FINLIFE://金融生活
                    intent.setClass(this, FinancialLifeActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_NewStockSubscribe://新股申购
                    intent.setClass(this, NewStockSubscribeActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_AssetsAnalysis://资产分析
                    intent.setClass(this, AssetsAnalysisActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_TradingDynamics://交易动态
                    intent.setClass(this, TradingDynamicsActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_StockRecall://股市回忆录
                    intent.setClass(this, StockRecallActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_TakeAPosition://持仓股票
                    intent.setClass(this, TakeAPositionActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_BanksTransferAccounts://银证转账
                    intent.setClass(this, BanksTransferAccountsActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_StockBillActivity://股市月账单
                    intent.setClass(this, StockBillActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OneKeySubscribe://一键申购
                    intent.setClass(this, OneKeySubscribeActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTCTakeAPosition://OTC持仓
                    intent.setClass(this, OTC_ShareActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_UsableCapital://可用资金
                    intent.setClass(this, UsableCapitalActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FundShare://基金持仓
                    intent.setClass(this, FundShareActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_Revoke://撤单页面
                    intent.setClass(this, RevokeActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_Refer://查询主页
                    intent.setClass(this, ReferActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_ChangNeiFundActivity://场内基金列表
                    intent.setClass(this, ChangNeiFundActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_BusinessIndex://银证转账总列表
                    intent.setClass(this, BankBusinessIndexActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FundSubsActivity://基金认购
                    intent.setClass(this, FundSubsActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FundPurchaseActivity://基金申购
                    intent.setClass(this, FundPurchaseActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FundRedemptionActivity://基金购回
                    intent.setClass(this, FundRedemptionActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FundWithDrawActivity://基金撤单
                    intent.setClass(this, FundWithDrawActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FundInfoActivity://基金信息
                    intent.setClass(this, FundInfoActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_FundOpenAccountActivity://基金开户
                    intent.setClass(this, FundOpenAccountActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OpenFundActivity://更多页面
                    intent.setClass(this, OpenFundActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTC_SubscriptionActivity://OTC 认购
                    intent.setClass(this, OTC_SubscriptionActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTC_SubscribeActivity://OTC 申购
                    intent.setClass(this, OTC_SubscribeActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTC_RedeemActivity://OTC 赎回
                    intent.setClass(this, OTC_RedeemActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTC_RevokeActivity://OTC 撤单
                    intent.setClass(this, OTC_RevokeActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTC_QueryActivity://OTC 查询
                    intent.setClass(this, OTC_QueryActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTC_AccountActivity://OTC 开户
                    intent.setClass(this, OTC_AccountActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_OTC_ElectronicContractActivity://OTC 电子合同
                    intent.setClass(this, OTC_ElectronicContractActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_AccountPowerActivity://我的账户权限
                    intent.setClass(this, AccountPowerActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_Agreement://电子签名约定书
                    intent.setClass(this, AgreementActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_RiskEvaluation://风险测评
                    intent.setClass(this, RiskEvaluationActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_PersonalData://修改个人资料
                    intent.setClass(this, PersonalDataActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_AgreementSigned://退市和风险警示协议签署
                    intent.setClass(this, AgreementSignActvity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_Information://股东资料
                    intent.setClass(this, StockHolderInfoActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_Password://修改密码
                    intent.setClass(this, ChangePasswordActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_ManagerMoenyDetailActivity:
                    intent.setClass(this, ManagerMoenyDetailActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_ManagerMySubscribeActivity:
                    intent.setClass(this, MySubscribeActivity.class);
                    break;
                case TransactionLoginActivity.PAGE_INDEX_TranMoreActivity:
                    intent.setClass(this, TranMoreActivity.class);
                    break;
//                case TransactionLoginActivity.PAGE_INDEX_ChangBank:
//                    intent.setClass(this, ChangBank.class);
//                    break;
//                case TransactionLoginActivity.PAGE_INDEX_NIHUIGOU:   //逆回购
//                    intent.setClass(this, ReverseRepoGuideActivity.class);
//                    break;
//                case TransactionLoginActivity.PAGE_INDEX_StartyUpBoardActivity:  //创业板转签
//                    intent.setClass(this, StartyUpBoardActivity.class);
//                    break;
//                case TransactionLoginActivity.PAGE_INDEX_InputFoundInfoActivity://修改身份证有效期填写信息页面
//                    intent.setClass(this, LoadingIDCardUpdateActivity.class);
//                    break;
//                case TransactionLoginActivity.PAGE_INDEX_PrecontractLoadActivity://预约
//                    intent.putExtra("productCode", newintent.getSerializable("productCode"));
//                    intent.putExtra("prod_nhsy", newintent.getSerializable("prod_nhsy"));
//                    intent.putExtra("ofund_risklevel_name", newintent.getSerializable("ofund_risklevel_name"));
//                    intent.putExtra("prod_qgje", newintent.getSerializable("prod_qgje"));
//                    intent.putExtra("schema_id", newintent.getSerializable("schema_id"));
//                    intent.putExtra("prod_code", newintent.getSerializable("prod_code"));
//                    intent.putExtra("TYPE", newintent.getSerializable("TYPE"));
//                    intent.putExtra("prod_type", newintent.getSerializable("prod_type"));
//                    intent.putExtra("prod_status", newintent.getSerializable("prod_status"));
//                    intent.setClass(this, PrecontractLoadActivity.class);
//                    break;
                case TransactionLoginActivity.PAGE_INDEX_ReverseRepoGuideActivity:
                    intent.setClass(this, ReverseRepoGuideActivity.class);
                    break;
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * EditText监听
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
            initImageViewshow();
        }
    }

    /**
     * ImagVie 状态
     */
    public void initImageViewshow() {
        if (mAccount_et.getText().toString().equals("")) {
            mZhang_iv.setVisibility(View.VISIBLE);
            mClose_iv.setVisibility(View.GONE);
        } else {
            mZhang_iv.setVisibility(View.VISIBLE);
            mClose_iv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Button 状态
     */
    public void initButtonshow() {
        if (mAccount_et.getText().toString().equals("")) {
            mLogin_but.setClickable(false);
            mLogin_but.setBackgroundResource(R.drawable.button_login_unchecked);
        } else if (mPassword_et.getText().toString().equals("")) {
            mLogin_but.setClickable(false);
            mLogin_but.setBackgroundResource(R.drawable.button_login_unchecked);
        } else if (mCaptcha_et.getText().toString().equals("")) {
            mLogin_but.setClickable(false);
            mLogin_but.setBackgroundResource(R.drawable.button_login_unchecked);
        } else {
            mLogin_but.setClickable(true);
            mLogin_but.setBackgroundResource(R.drawable.button_login_pitchon);
        }
    }
}
