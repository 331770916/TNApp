package com.tpyzq.mobile.pangu.activity.myself.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
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
import com.tpyzq.mobile.pangu.util.keyboard.NoSoftInputEditText;
import com.tpyzq.mobile.pangu.util.keyboard.ResponseInterface;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.CertificationDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoginDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.MutualAuthenticationDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.yzd.unikeysdk.OnInputDoneCallBack;
import com.yzd.unikeysdk.PasswordKeyboard;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;
import com.yzd.unikeysdk.UnikeyUrls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.bouncycastle.asn1.x509.sigi.PersonalData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.umeng.socialize.Config.dialog;

/**
 * Created by wangqi on 2016/8/18.
 * 绑定并更换帐号界面
 */
public class ChangeAccoutActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "BindMore";
    //控件
    private EditText mBDAccount, mBDCaptcha, mBDPasswordET;
    private NoSoftInputEditText mBDPassword;
    private ImageView mZhangIV, mCloseIV;
    private PopupWindow popupWindow;
    private SimpleDraweeView mBDSecurityCode;
    private Button mBindingbtn;
    //数据赋值
    private String mVERIFICATIONCODE, keyboardpassword;
    //标记状态记录
    private String passwordFormat;
    //接口数据
    private String OLD_SRRC, OLD_TCC, mSession;

    private String IS_OVERDUE;
    private String CORP_RISK_LEVEL;
    private String CORP_END_DATE;
    //插件
    private UnikeyUrls unikeyUrls;
    private String diviceId;
    private boolean isLoginSuc = false;
    private int pageIndex = 0;
    private boolean isHttp;
    private String Exit;
    private Dialog isKeyboardDialog;
    private Bundle newintent;
    private Dialog mCommit;
    private Dialog mLoad;
    private Dialog mDownload;
    private Dialog mKeyboardRequest;
    private TextView mCxaptcha;
    private String ip;

    @Override
    public void initView() {
        findViewById(R.id.BMublish_back).setOnClickListener(this);
        findViewById(R.id.BMService).setOnClickListener(this);
        mBDAccount = (EditText) findViewById(R.id.BDAccount);               //绑定资金账号
        mBDPassword = (NoSoftInputEditText) findViewById(R.id.BDPassword);  //交易密码_暗文
        mBDPasswordET = (EditText) findViewById(R.id.BDPasswordET);         //交易密码_明文
        mBDCaptcha = (EditText) findViewById(R.id.BDCaptcha);               //填写验证码
        mBindingbtn = (Button) findViewById(R.id.Bindingbtn);               //登录
        mZhangIV = (ImageView) findViewById(R.id.ZhangIV);                  //历史账号
        mCloseIV = (ImageView) findViewById(R.id.CloseIV);                  //清除
        mBDSecurityCode = (SimpleDraweeView) findViewById(R.id.BDSecurityCode);//验证码

        mCxaptcha = (TextView) findViewById(R.id.tvCaptcha);//重新获取验证码

        isKeyboardDialog = LoadingDialog.initDialog(this, "加载中...");
        if (!ChangeAccoutActivity.this.isFinishing()) {
            isKeyboardDialog.show();
        }
        Exit = getIntent().getStringExtra("Exit");
        if (!"true".equals(Exit)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setIslogin("false");
            Db_PUB_USERS.UpdateIslogin(userEntity);
        }
        //验证码请求
        toSecurityCode(null);
        if (ConstantUtil.USERFUL_KEYBOARD) {
            IsKeyboardRequestHttp();
        } else {
            initYN(false);
        }
        //账号监听处理
        EditTextMonitor();
    }

    private void IsKeyboardRequestHttp() {
        final UserEntity userEntity = new UserEntity();
        HashMap map = new HashMap();
        map.put("funcid", "400102");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_SXRZ, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                isKeyboardDialog.dismiss();
                userEntity.setKeyboard("false");
                Db_PUB_USERS.UpdateKeyboard(userEntity);
                initYN(false);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    isKeyboardDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");

                    if ("0".equals(code)) {
                        String status = jsonObject.getString("status");
                        if ("2".equals(status)) {
                            userEntity.setKeyboard("true");
                            Db_PUB_USERS.UpdateKeyboard(userEntity);
                            initYN(true);
                        } else {
                            userEntity.setKeyboard("false");
                            Db_PUB_USERS.UpdateKeyboard(userEntity);
                            initYN(false);
                        }
                    } else {
                        userEntity.setKeyboard("false");
                        Db_PUB_USERS.UpdateKeyboard(userEntity);
                        initYN(false);
                    }
                } catch (JSONException e) {
                    if (isKeyboardDialog != null) {
                        isKeyboardDialog.dismiss();
                    }
                    userEntity.setKeyboard("false");
                    Db_PUB_USERS.UpdateKeyboard(userEntity);
                    initYN(false);
                    e.printStackTrace();
                }
            }
        });
    }

    private void initYN(boolean is) {
        if (is) {
            UserUtil.Keyboard = "1";  //启用sessionkey加密
            //键盘插件URL
            getUnikey();
            //数据更新
            UserUtil.refrushUserInfo();

            Intent intent = getIntent();
            if (intent != null) {
                pageIndex = intent.getIntExtra("pageindex", 0);
                newintent = intent.getExtras();
            }
            Exit = getIntent().getStringExtra("Exit");
            if (!"true".equals(Exit)) {
                SpUtils.putString(ChangeAccoutActivity.this, "mSession", "");
            }
            //判断插件是否存在
            getKeyBoard();
            //查询加密键盘是否显示
            inquireCertification();
            //插件键盘数据
            showKeyboardWithHeader();

        } else {
            UserUtil.Keyboard = "0";   //不启用sessionkey加密
            //数据更新
            UserUtil.refrushUserInfo();
            Intent intent = getIntent();
            if (intent != null) {
                pageIndex = intent.getIntExtra("pageindex", 0);
                newintent = intent.getExtras();
            }
            Exit = getIntent().getStringExtra("Exit");
            if (!"true".equals(Exit)) {
                SpUtils.putString(ChangeAccoutActivity.this, "mSession", "");
            }
            //查询加密键盘是否显示
            inquireCertification();

        }

    }

    private void EditTextMonitor() {
        mCxaptcha.setOnClickListener(this);
        mBindingbtn.setOnClickListener(this);
        mBDAccount.addTextChangedListener(new MyTextWatcher());
        mBDPassword.addTextChangedListener(new MyTextWatcher());
        mBDPasswordET.addTextChangedListener(new MyTextWatcher());
        mBDCaptcha.addTextChangedListener(new MyTextWatcher());

        mBDAccount.setOnClickListener(this);
        mBDCaptcha.setOnClickListener(this);

        mZhangIV.setOnClickListener(this);
        mCloseIV.setOnClickListener(this);

        mBDSecurityCode.setOnClickListener(this);
        mBDPassword.setOnClickListener(this);

        mBDAccount.setSelection(mBDAccount.getText().length());
        mBDCaptcha.setSelection(mBDCaptcha.getText().length());
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_bindmore;
    }

    /**
     * ID 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BMublish_back:
                if (!"true".equals(Exit)) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setIslogin("false");
                    Db_PUB_USERS.UpdateIslogin(userEntity);
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.BMService:
                Intent intent = new Intent(this, HotlineActivity.class);
                startActivity(intent);
                break;
            case R.id.ZhangIV:
                showPopupWindow();
                break;
            case R.id.CloseIV:
                mBDAccount.setText("");
                break;
            case R.id.BDSecurityCode:
                if (!ChangeAccoutActivity.this.isFinishing() && isKeyboardDialog != null) {
                    isKeyboardDialog.show();
                }
                toSecurityCode(isKeyboardDialog);
                break;
            case R.id.Bindingbtn:
                mCommit = LoadingDialog.initDialog(this, "正在提交...");
                if (!ChangeAccoutActivity.this.isFinishing()) {
                    mCommit.show();
                }
                setIslogin();
                if (mBDCaptcha.getText().toString().trim().equalsIgnoreCase(mVERIFICATIONCODE)) {
                    mBindingbtn.setFocusable(false);
                    getIPtoLogin();
                } else {
                    toSecurityCode(null);
                    if (mCommit != null) {
                        mCommit.dismiss();
                    }
                    Helper.getInstance().showToast(this, "验证码错误");
                    mBDPassword.setText("");
                    mBDPasswordET.setText("");
                    mBDCaptcha.setText("");
                }
                break;
            case R.id.tvCaptcha:
                if (!ChangeAccoutActivity.this.isFinishing() && isKeyboardDialog != null) {
                    isKeyboardDialog.show();
                }
                toSecurityCode(isKeyboardDialog);
        }
    }


    /**
     * 修改登录状态
     */
    private void setIslogin() {

        UserEntity userEntity = new UserEntity();

        //修改是否登录
        userEntity.setIslogin("false");
        Db_PUB_USERS.UpdateIslogin(userEntity);
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
            ToastUtils.showShort(ChangeAccoutActivity.this, e.toString());
            e.printStackTrace();
        }
    }

    private void getIPtoLogin() {
        OkHttpUtils.get().url("http://ip.taobao.com/service/getIpInfo.php?ip=myip")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toConnect();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    toConnect();
                    return;
                }
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject data = json.getJSONObject("data");
                    ip = data.getString("ip");
                    toConnect();
                } catch (JSONException e) {
                    toConnect();
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 登录网络请求
     */
    private void toConnect() {
        if (TextUtils.isEmpty(UserUtil.Mobile)) {
            MistakeDialog.showDialog("登录入参异常", this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setMobile("");
                    Db_PUB_USERS.UpdateMobile(userEntity);
                    finish();
                }
            });
        } else {
            isLoginSuc = false;
            String unikey = "0";
            try {
                unikey = UniKey.getInstance().getUnikeyId();
            } catch (UnikeyException e) {
                e.printStackTrace();
            }
            if (passwordFormat.equals("0")) {
                keyboardpassword = mBDPasswordET.getText().toString().trim();
            } else {
                keyboardpassword = keyboardpassword;
            }
            Map map1 = new HashMap<>();
            map1.put("funcid", "300010");
            map1.put("token", SpUtils.getString(this, mSession, ""));
            Map map2 = new HashMap<>();
            map2.put("SEC_ID", "tpyzq");
            map2.put("LOGIN_CODE", mBDAccount.getText().toString().trim());//610002680     101000913 //用户账号
            map2.put("USER_PWD", keyboardpassword);                       //密码
            map2.put("PWD_TYPE", passwordFormat); //密码类型 0：明文 1：密文
            map2.put("MOBILE", DeviceUtil.getDeviceId(CustomApplication.getContext()));                       //绑定UNIKEYID的手机号
            map2.put("UNIKEYID", unikey);                       //UNIKEY插件ID
            map2.put("APP_TYPE", "1");                       //手机类型 0：ios        1：android
            map2.put("TCC", DeviceUtil.getDeviceId(CustomApplication.getContext()));
            map2.put("SRRC", android.os.Build.MODEL);
            map2.put("OP_STATION", "Android" + "," + UserUtil.Mobile + "," + DeviceUtil.getDeviceId(CustomApplication.getContext()) + "," + APPInfoUtils.getVersionName(this) + ",");
            map2.put("APP_ID", ConstantUtil.APP_ID);
            map1.put("parms", map2);

            NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    if (mCommit != null) {
                        mCommit.dismiss();
                    }
                    LogHelper.e(TAG, e.toString());
                    ResultDialog.getInstance().showText("网络异常");
                    mBindingbtn.setFocusable(true);
                    mBDPassword.setText("");
                    mBDPasswordET.setText("");
                    mBDCaptcha.setText("");
                }

                @Override
                public void onResponse(String response, int id) {
                    if (TextUtils.isEmpty(response)) {
                        return;
                    }
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        String code_Str = jsonObj.optString("code");
                        String msg_Str = jsonObj.optString("msg");
                        JSONArray data = jsonObj.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject Session = (JSONObject) data.get(i);
                            mSession = Session.optString("SESSION");
                            OLD_SRRC = Session.optString("OLD_SRRC");
                            OLD_TCC = Session.optString("OLD_TCC");
                            IS_OVERDUE = Session.optString("IS_OVERDUE");//风险评测状态 0正常 1即将过期 2过期 3未做
                            CORP_RISK_LEVEL = Session.optString("CORP_RISK_LEVEL");//客户当前风险承受等级
                            CORP_END_DATE = Session.optString("CORP_END_DATE");//风险测评有效期到期时间
                        }
                        if ("0".equalsIgnoreCase(code_Str)) {
                            isLoginSuc = true;
                            if (mCommit != null) {
                                mCommit.dismiss();
                            }
                            //存储风险测试结果 测评状态--测评等级--有效期结束日期
                            SpUtils.putString(ChangeAccoutActivity.this,"IS_OVERDUE",IS_OVERDUE);
                            SpUtils.putString(ChangeAccoutActivity.this,"CORP_RISK_LEVEL",CORP_RISK_LEVEL);
                            SpUtils.putString(ChangeAccoutActivity.this,"CORP_END_DATE",CORP_END_DATE);
                            //第一次登录数据库交易账号无数据 添加到数据库
                            if (!DeviceUtil.getDeviceId(CustomApplication.getContext()).equals(OLD_TCC) && !android.os.Build.MODEL.equals(OLD_SRRC)) {
                                getData(mBDAccount.getText().toString().trim(), "false",mSession);
                                LoginDialog.showDialog("您更换了登录设备，上次使用的设备型号是" + OLD_SRRC, ChangeAccoutActivity.this, new MistakeDialog.MistakeDialgoListener() {
                                    @Override
                                    public void doPositive() {
                                        if ("2".equalsIgnoreCase(IS_OVERDUE)||"3".equalsIgnoreCase(IS_OVERDUE)) {
                                            //弹出风险评测dialog
                                            showCorpDialog();
                                        } else {
                                            finish();
                                        }
                                    }
                                });
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                final String date = sDateFormat.format(new java.util.Date());
                                BRutil.menuLogIn(android.os.Build.VERSION.RELEASE, UserUtil.Mobile, DeviceUtil.getDeviceId(CustomApplication.getContext()), APPInfoUtils.getVersionName(ChangeAccoutActivity.this), ip, UserUtil.capitalAccount, date);
                            } else {
                                showDialogOrSaveData();
                            }
                        } else {
                            mBindingbtn.setFocusable(true);
                            toSecurityCode(null);
                            if ("密码键盘解密失败".equals(msg_Str)) {
                                if (mCommit != null) {
                                    mCommit.dismiss();
                                }
                                mBDPassword.setText("");
                                mBDPasswordET.setText("");
                                mBDCaptcha.setText("");
                                Helper.getInstance().showToast(ChangeAccoutActivity.this, msg_Str.toString() + ",请重新输入");
//                                isHttp = true;
//                                KeyboardRequestHttp(isHttp);
                            } else {
                                if (mCommit != null) {
                                    mCommit.dismiss();
                                }
                                mBDPassword.setText("");
                                mBDPasswordET.setText("");
                                mBDCaptcha.setText("");
                                MistakeDialog.showDialog(msg_Str.toString(), ChangeAccoutActivity.this);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    /**
     * 显示弹框，绑定账号请求完成后不关闭页面
     * 不显示弹框，绑定账号请求完成后关闭页面
     */
    private void showDialogOrSaveData() {
        if ("2".equalsIgnoreCase(IS_OVERDUE)||"3".equalsIgnoreCase(IS_OVERDUE)) {
            //弹出风险评测dialog
            showCorpDialog();
            getData(mBDAccount.getText().toString().trim(), "false",mSession);
        } else {
            getData(mBDAccount.getText().toString().trim(), "true", mSession);
        }
    }
    /**
     * 弹出风险测评弹框
     */
    private void showCorpDialog() {
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
                Intent intent = new Intent(ChangeAccoutActivity.this,RiskEvaluationActivity.class);
                intent.putExtra("isLogin",true);
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
    /**
     * 是否需要返回业务类进行弹框操作
     * @return true需要 false不需要
     */
    private boolean isNeedShowCropDialog() {
        boolean flag = false;
        try {
            String isDialogShow = SpUtils.getString(this,"ISDIALOGSHOW","");//测评状态 存储形式：日期
            //状态码--风险等级--到期日期 如：20160606--1--中等--20170606
//            if (!TextUtils.isEmpty(isDialogShow)) {
            // TODO: 2017/6/26 确定返回日期的格式
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = sDateFormat.format(new java.util.Date());
            SpUtils.putString(this,"ISDIALOGSHOW",date);
            if (!date.equalsIgnoreCase(isDialogShow)) {
                //今天是第一次登录了，返回业务类进行弹框
                flag = true;
            }
//            } else {
//                flag = true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return flag;
        }
    }
    /**
     * 请求验证码
     */
    private void toSecurityCode(final Dialog dialog) {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("FUNCTIONCODE ", "HQING002");
        map.put("parms", map1);
        map1.put("limit", "");
        map1.put("offset ", "");
        map1.put("tag ", "");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JYYZM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Helper.getInstance().showToast(ChangeAccoutActivity.this, "网络器异常");
                mCxaptcha.setVisibility(View.VISIBLE);
                mBDSecurityCode.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String mCODE = object.getString("CODE");
                    mVERIFICATIONCODE = object.getString("VERIFICATIONCODE");
                    String mVERIFICATIONIMAGE = object.getString("VERIFICATIONIMAGE");
                    if (mCODE.equals("0")) {
                        mBDSecurityCode.setVisibility(View.VISIBLE);
                        mCxaptcha.setVisibility(View.GONE);
                        Bitmap bitmap = Helper.base64ToBitmap(mVERIFICATIONIMAGE);
                        mBDSecurityCode.setImageBitmap(bitmap);
                    } else {
                        mCxaptcha.setVisibility(View.VISIBLE);
                        mBDSecurityCode.setVisibility(View.GONE);
                        Helper.getInstance().showToast(ChangeAccoutActivity.this, "网络器异常");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mBDSecurityCode.setAspectRatio(3.2f);
    }

    /**
     * 获取加密键盘
     */
    private void getKeyBoard() {
        try {
            UniKey.getInstance().init(CustomApplication.getContext(), unikeyUrls, diviceId);
            //获取加密键盘插件是否存在
            boolean pluginExist = UniKey.getInstance().isPluginExist();
            if (!pluginExist) {     //如果不存在
                //下载插件
                HandleResponse.HandleResponseHelper(getPluginDownloadReqHandler(), Constants.APPLY_PLUGIN_REQ);
            } else {                //如果存在
                //如果加密键盘组建存在就验证APP完整性
                biAuthBtnClick();       //双向认证
            }
        } catch (UnikeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 双向认证
     */
    private void biAuthBtnClick() {
        HandleResponse.HandleResponseHelper(BiAuthFirstInterface(), Constants.Bi_AUTH_FIRST_REQ);
    }

    /**
     * 下载插件回调
     *
     * @return
     */
    private ResponseInterface getPluginDownloadReqHandler() {
        mDownload = LoadingDialog.initDialog(this, "下载双向认证安全插件...");
        if (!ChangeAccoutActivity.this.isFinishing()) {
            mDownload.show();
        }
        return new ResponseInterface() {
            @Override
            public void onGetResponse(Object response) {
                //初始化用户对象修改数据库
                UserEntity userEntity = new UserEntity();
                if (response instanceof Boolean && (Boolean) response) {        //如果回调对象是boolean类型 且为true
                    userEntity.setPlugins("true");
                    Db_PUB_USERS.UpdatePlugins(userEntity);
                    biAuthBtnClick();
                    if (mDownload != null) {
                        mDownload.dismiss();
                    }
                    ToastUtils.showShort(ChangeAccoutActivity.this, "下载插件成功");
                } else {            //下载失败后的参数
                    if (mDownload != null) {
                        mDownload.dismiss();
                    }
                    //重复下载提示框
                    //重复下载提示
                    if (!ChangeAccoutActivity.this.isFinishing()) {
                        CertificationDialog.showDialog("提示信息", "安全插件加载失败，为了您的安全考虑希望您重下载。", "重新下载", "取消", ChangeAccoutActivity.this, new CertificationDialog.PositiveCliceListener() {
                            @Override
                            public void positiveClick(View v) {
                                HandleResponse.HandleResponseHelper(getPluginDownloadReqHandler(), Constants.APPLY_PLUGIN_REQ);
                            }
                        }, new CertificationDialog.CancleClickListener() {
                            @Override
                            public void cancleClick(View v) {
                                CertificationDialog.closeDialog(dialog);
                                isHttp = false;
                                KeyboardRequestHttp(isHttp);
                            }
                        });
                    }
                }
            }
        };
    }

    /**
     * 双向认证回调
     *
     * @return
     */
    private ResponseInterface BiAuthFirstInterface() {
        mLoad = LoadingDialog.initDialog(this, "加载中...");
        if (!ChangeAccoutActivity.this.isFinishing()) {
            mLoad.show();
        }
        return new ResponseInterface() {
            @Override
            public void onGetResponse(Object response) {
                UserEntity userEntity = new UserEntity();
                if (response == null) {
                    if (mLoad != null) {
                        mLoad.dismiss();
                    }
                    userEntity.setCertification("false");
                    Db_PUB_USERS.UpdateCertification(userEntity);
                    return;
                }
                if (mLoad != null) {
                    mLoad.dismiss();
                }
                String authData = String.valueOf(response);
                if ("true".equals(authData)) {
                    userEntity.setCertification("true");
                    Db_PUB_USERS.UpdateCertification(userEntity);
                    setPassEdit(true);          //改变键盘
                } else if (authData.contains("-2146959355")) {
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
                                KeyboardRequestHttp(isHttp);
                            }
                        });
                    }
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
                                KeyboardRequestHttp(isHttp);
                            }
                        });
                    }
                }
            }
        };
    }

    /**
     * 键盘显示
     *
     * @param flag
     */
    private void setPassEdit(Boolean flag) {     // true 密文  false 明文
        if (flag) {
            mBDPasswordET.setVisibility(View.GONE);
            mBDPassword.setVisibility(View.VISIBLE);
            passwordFormat = "1";
        } else {
            mBDPasswordET.setVisibility(View.VISIBLE);
            mBDPassword.setVisibility(View.GONE);
            passwordFormat = "0";
        }
    }

    /**
     * 请求键盘 明文  密文
     */
    private void KeyboardRequestHttp(final boolean isHttp) {
        mKeyboardRequest = LoadingDialog.initDialog(this, "加载中...");
        if (!ChangeAccoutActivity.this.isFinishing()) {
            mKeyboardRequest.show();
        }
        HashMap map = new HashMap();
        map.put("funcid", "400102");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_SXRZ, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mCommit != null) {
                    mCommit.dismiss();
                }
                mKeyboardRequest.dismiss();
                LogHelper.e(TAG, e.toString());
                if (isHttp == true) {
                    if (!ChangeAccoutActivity.this.isFinishing()) {
                        MutualAuthenticationDialog.showDialog("双向认证失败点击退出", ChangeAccoutActivity.this);
                    }
                } else {
                    if (!ChangeAccoutActivity.this.isFinishing()) {
                        MutualAuthenticationDialog.showDialog("系统异常", ChangeAccoutActivity.this);
                    }
                }
            }

            @Override
            public void onResponse(String response, int id) {
                mKeyboardRequest.dismiss();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    final UserEntity userEntity = new UserEntity();
                    if (code.equals("0")) {
                        String status = jsonObject.getString("status");
                        mBDCaptcha.setText("");
                        if (mCommit != null) {
                            mCommit.dismiss();
                        }
                        if ("1".equals(status)) {
                            setPassEdit(false);
                            userEntity.setKeyboard("false");
                            userEntity.setCertification("false");
                            Db_PUB_USERS.UpdateKeyboard(userEntity);
                            Db_PUB_USERS.UpdateCertification(userEntity);
                        } else {
                            if (isHttp == true) {
                                if (!ChangeAccoutActivity.this.isFinishing()) {
                                    MutualAuthenticationDialog.showDialog("双向认证失败点击退出", ChangeAccoutActivity.this);
                                }
                            } else {
                                if (!ChangeAccoutActivity.this.isFinishing()) {
                                    MutualAuthenticationDialog.showDialog("系统异常", ChangeAccoutActivity.this);
                                }
                            }
                        }
                    } else {
                        if (mCommit != null) {
                            mCommit.dismiss();
                        }
                        if (isHttp == true) {
                            if (!ChangeAccoutActivity.this.isFinishing()) {
                                MutualAuthenticationDialog.showDialog("双向认证失败点击退出", ChangeAccoutActivity.this);
                            }
                        } else {
                            if (!ChangeAccoutActivity.this.isFinishing()) {
                                MutualAuthenticationDialog.showDialog("系统异常", ChangeAccoutActivity.this);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 新增绑定用户
     */
    private void setAdded(final String isfinish,String session) {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "800104");
        map.put("token", session);
        map.put("parms", map1);
        map1.put("type", "1");
        map1.put("account", mBDAccount.getText().toString().trim());
        map1.put("user_type", KeyEncryptionUtils.getInstance().Typescno());
        map1.put("user_account", UserUtil.userId);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JYBD, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mCommit != null) {
                    mCommit.dismiss();
                }
                LogUtil.e(TAG, e.toString());
                Helper.getInstance().showToast(ChangeAccoutActivity.this, "网络器异常");
                mBDPassword.setText("");
                mBDPasswordET.setText("");
                mBDCaptcha.setText("");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    String code_Str = jsonObj.getString("code");
                    String msg_Str = jsonObj.getString("msg");
                    if ("0".equals(code_Str)) {         //新增绑定 成功
                        HOLD_SEQ.deleteAll();
                        SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);

                        setData(isfinish);                      //修改资金账号数据
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final String date = sDateFormat.format(new java.util.Date());
                        BRutil.menuLogIn(android.os.Build.VERSION.RELEASE, UserUtil.Mobile, DeviceUtil.getDeviceId(CustomApplication.getContext()), APPInfoUtils.getVersionName(ChangeAccoutActivity.this), ip, UserUtil.capitalAccount, date);
                    } else {
                        toSecurityCode(null);
                        if (mCommit != null) {
                            mCommit.dismiss();
                        }

                        if (mBDPassword != null) {
                            mBDPassword.setText("");
                        } else if (mBDPasswordET != null) {
                            mBDPasswordET.setText("");
                        }
                        mBDCaptcha.setText("");
                        MistakeDialog.showDialog(msg_Str.toString(), ChangeAccoutActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 修改数据库字段数据
     */
    private void setData(String isfinish) {
        UserEntity userEntity = new UserEntity();
        String mAccount_Str = mBDAccount.getText().toString().trim();

        //查询资金账号
        String tradescno = KeyEncryptionUtils.getInstance().localDecryptTradescno().get(0).getTradescno();
        if (tradescno.contains(mAccount_Str)) {
            if (!tradescno.contains(",")) {
                tradescno = tradescno.replace(mAccount_Str, "");
                KeyEncryptionUtils.getInstance().localEncryptTradescno(mAccount_Str + "," + tradescno);
            } else {
                tradescno = tradescno.replace(mAccount_Str + ",", "");
                KeyEncryptionUtils.getInstance().localEncryptTradescno(mAccount_Str + "," + tradescno);
            }
        } else {
            KeyEncryptionUtils.getInstance().localEncryptTradescno(mAccount_Str + "," + tradescno);
        }

        //修改资金账号
        SpUtils.putString(ChangeAccoutActivity.this, "mSession", mSession);
        SpUtils.putString(ChangeAccoutActivity.this, "First", "1");
        //修改是否登录
        userEntity.setIslogin("true");
        Db_PUB_USERS.UpdateIslogin(userEntity);


        if ("true".equals(isfinish)) {
            if (mCommit.isShowing()) {
                mCommit.dismiss();
            }
            finish();
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
                    intent.setClass(this, PersonalData.class);
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
     * 查询数据库账号判断
     */
    private void getData(String data, String isfinish,String session) {
        //查询资金账号
        List<UserEntity> userEntities = KeyEncryptionUtils.getInstance().localDecryptTradescno();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userEntities.size(); i++) {
            String Tradescno = userEntities.get(i).getTradescno();
            sb.append(Tradescno).append(",");
        }

        String Tradescno = sb.toString();
        if (TextUtils.isEmpty(Tradescno)) {
            setAdded(isfinish,session);
        } else {
            if (Tradescno.contains(data)) {
                setData(isfinish);
            } else {
                setAdded(isfinish,session);
            }
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
        popupWindow = new PopupWindow(view, TransitionUtils.dp2px(290, this), TransitionUtils.dp2px(150, this), true);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xf0ffffff));
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(mBDAccount, TransitionUtils.dp2px(60, this), TransitionUtils.dp2px(15, this));
        mPWListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow != null) {
                    mBDAccount.addTextChangedListener(new MyTextWatcher());
                    mBDAccount.setText(list.get(position));
                    mBDAccount.setSelection(list.get(position).length());
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 获取插件数据
     */
    private void showKeyboardWithHeader() {
        try {
            PasswordKeyboard passwordKeyboard = UniKey.getInstance().getPasswordKeyboard(this, new OnInputDoneCallBack() {
                @Override
                public void getInputEncrypted(String s) {
                    keyboardpassword = s;

                    LogUtil.e("加密数据", s);
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
                    mBDPassword.setText(s);
                    mBDPassword.setSelection(mBDPassword.getText().length());
                }
            }, 6, true, "custom_keyboard_view");
            mBDPassword.setPasswordKeyboard(passwordKeyboard);

        } catch (UnikeyException e) {
            ToastUtils.showShort(ChangeAccoutActivity.this, "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
        }
    }


    /**
     * 查询加密键盘是否显示
     * 初始化TextView数据处理
     */
    private void inquireCertification() {
        if ("false".equals(Db_PUB_USERS.queryingKeyboard())) {
            setPassEdit(false);
        } else {
            setPassEdit(true);

        }
        if (UserUtil.capitalAccount != null) {
            mBDAccount.setText(UserUtil.capitalAccount);
        } else {
            mBDAccount.setHint("请输入账号");
        }
        if (!TextUtils.isEmpty(mBDAccount.getText().toString().trim()) && !TextUtils.isEmpty(UserUtil.capitalAccount)) {
            mCloseIV.setVisibility(View.VISIBLE);
        } else {
            mCloseIV.setVisibility(View.GONE);
        }
    }

    /**
     * Button 状态
     */
    public void initButtonshow() {
        if (mBDAccount.getText().toString().equals("")) {
            mBindingbtn.setEnabled(false);
            mBindingbtn.setBackgroundResource(R.drawable.button_login_unchecked);
        } else if (mBDPassword.getText().toString().equals("") && mBDPasswordET.getText().toString().equals("")) {
            mBindingbtn.setEnabled(false);
            mBindingbtn.setBackgroundResource(R.drawable.button_login_unchecked);
        } else if (mBDCaptcha.getText().toString().equals("")) {
            mBindingbtn.setEnabled(false);
            mBindingbtn.setBackgroundResource(R.drawable.button_login_unchecked);
        } else {
            mBindingbtn.setEnabled(true);
            mBindingbtn.setBackgroundResource(R.drawable.button_login_pitchon);
        }
    }

    /**
     * ImagVie 状态
     */
    public void initImageViewshow() {
        if (mBDAccount.getText().toString().equals("")) {
            mZhangIV.setVisibility(View.VISIBLE);
            mCloseIV.setVisibility(View.GONE);
        } else {
            mZhangIV.setVisibility(View.VISIBLE);
            mCloseIV.setVisibility(View.VISIBLE);
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

            if (s.equals("")) {
                initButtonshow();
            } else {
                initButtonshow();
            }
            if (start == 0) {
                initImageViewshow();
                initButtonshow();
            } else {
                initImageViewshow();
                initButtonshow();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }


    @Override
    protected void onDestroy() {
        dismissDownload();
        super.onDestroy();
    }


    private void dismissDownload() {
        if (isKeyboardDialog != null) {
            isKeyboardDialog.dismiss();
        }
        if (mDownload != null) {
            mDownload.dismiss();
        }
        if (mLoad != null) {
            mLoad.dismiss();
        }
        if (mCommit != null) {
            mCommit.dismiss();
        }
        if (mKeyboardRequest != null) {
            mKeyboardRequest.dismiss();
        }
    }
}
