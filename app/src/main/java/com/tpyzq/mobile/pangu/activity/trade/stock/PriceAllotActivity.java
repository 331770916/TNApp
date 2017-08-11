package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by zhangwenbo on 2016/8/26.
 * 资金调拨
 */
public class PriceAllotActivity extends BaseActivity implements View.OnClickListener ,TextWatcher{

    private RelativeLayout mAccountContentLayout;
    private ImageView mAccountIv1;
    private ImageView mAccountIv2;
    private TextView mAccountTv1;
    private TextView mAccountTv2;
    private LinearLayout mClickChangeLayout;
    private TextView mChangeAccountTv;
    private EditText mBalanceTv;
    private RelativeLayout mViewGroup;
    private boolean changeAccountFlag;      //true时  辅转主， false 主转辅

    private ArrayList<BankAccountEntity> mBeans;
    private BankAccountEntity mMainEntitiy;
    private BankAccountEntity _mFuEntity;
    private int mIndex = -1;

    private TextView mFundTv;
    private TextView mUsefulBalanceTv;
    private TextView mBelongToBank;
    private TextView mBankCountTv;

    private String mFundAccountSrc = "";
    private String mFundAccountDest = "";

    private boolean  mIsFirst = true;
    private String      mSession;

    private boolean choiceAcount = true;
    private static final int REQUELS_CODE = 10000;
    public static final String TAG = "PriceAllot";
    private TextView mSubmitBtn;

    @Override
    public void initView() {

        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        mAccountContentLayout = (RelativeLayout) findViewById(R.id.allot_priceContentLayout);

        mClickChangeLayout = (LinearLayout) findViewById(R.id.allot_clickChange);
        mViewGroup = (RelativeLayout) findViewById(R.id.inputViewGroup);
        mAccountIv1 = (ImageView) findViewById(R.id.changeAcIv1);
        mAccountIv2 = (ImageView) findViewById(R.id.changeAcIv2);

        mAccountTv1 = (TextView) findViewById(R.id.changeAcText1);
        mAccountTv2 = (TextView) findViewById(R.id.changeAcText2);

        mAccountTv1.setText("辅助资金账号");
        mAccountTv2.setText("主资金账号");


        mChangeAccountTv = (TextView) findViewById(R.id.clickChangeTv);
        mBalanceTv = (EditText) findViewById(R.id.allot_inputPrice);
        mBalanceTv.addTextChangedListener(this);
        mBankCountTv = (TextView) findViewById(R.id.allot_Account);
        mUsefulBalanceTv = (TextView) findViewById(R.id.allotYe);
        mClickChangeLayout.setOnClickListener(this);
        findViewById(R.id.allot_price_back).setOnClickListener(this);
        findViewById(R.id.allot_choiceAcLayout).setOnClickListener(this);

        mSubmitBtn = (TextView) findViewById(R.id.allotPositiveBtn);
        mSubmitBtn.setBackgroundResource(R.mipmap.nagtive_btn_ic);
        mSubmitBtn.setEnabled(false);
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsFirst) {
            mIsFirst = false;
            mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
            getBanksList();
        }

    }

    /**
     * 初始化顶部布局
     * @param layout
     */
    private void initTopContentView(RelativeLayout layout, String account, String bank, String bankNo) {

        mFundTv = new TextView(this);
        mBelongToBank = new TextView(this);
        mFundTv.setId(R.id.allot_fundTv);
        mFundTv.setPadding(0, 10, 0, 10);
        mFundTv.setGravity(Gravity.CENTER_VERTICAL);
        mBelongToBank.setGravity(Gravity.CENTER_VERTICAL);
        mBelongToBank.setPadding(0, 10, 0, 10);

        layout.addView(mFundTv);

        String content1 = "我的主资金账号\n" + account;
        SpannableString ss = new SpannableString(content1);
        ss.setSpan(new RelativeSizeSpan(0.8f),0,7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(1.5f),7,content1.length() ,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        if (!TextUtils.isEmpty(bankNo) && bankNo.length() > 4) {
            bankNo = bankNo.substring(bankNo.length() - 4, bankNo.length());
        }

        String content2 = "存管银行:" + bank + "(" + "****" + bankNo + ")";
        SpannableString ss1 = new SpannableString(content2);
        ss1.setSpan(new RelativeSizeSpan(0.9f),0,content2.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        mFundTv.setTextColor(Color.WHITE);
        mBelongToBank.setTextColor(Color.WHITE);
        mFundTv.setGravity(Gravity.CENTER_VERTICAL);
        mFundTv.setText(ss);
        mBelongToBank.setText(ss1);

        layout.addView(mBelongToBank);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.CENTER_VERTICAL);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lp1.setMargins(Helper.dip2px(CustomApplication.getContext(), 20), 0, 0, 0);
        mFundTv.setLayoutParams(lp1);


        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lp2.setMargins(0, 0, Helper.dip2px(CustomApplication.getContext(), 24), 0);
        mBelongToBank.setLayoutParams(lp2);
        // btn1 位于父 View 的顶部，在父 View 中水平居中

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.setMargins(0, 22, 0, 0);
        layout.setLayoutParams(lp3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allot_price_back:
                finish();
                break;
            case R.id.allot_clickChange:

                if (changeAccountFlag) {
                    choiceAcount = true;
                    updateChangeAccount(changeAccountFlag);

                    if (_mFuEntity != null && !TextUtils.isEmpty(_mFuEntity.getFETCH_BALANCE())) {
                        mUsefulBalanceTv.setText( "可取余额：" + _mFuEntity.getFETCH_BALANCE());
                    }else {
                        mUsefulBalanceTv.setText("");
                    }

                    if (mMainEntitiy != null && !TextUtils.isEmpty(mMainEntitiy.getFUND_ACCOUNT())) {
                        mFundAccountDest = mMainEntitiy.getFUND_ACCOUNT();
                    }

                    if (_mFuEntity != null && !TextUtils.isEmpty(_mFuEntity.getFUND_ACCOUNT())) {
                        mFundAccountSrc = _mFuEntity.getFUND_ACCOUNT();
                    }
                    changeAccountFlag = false;
                } else {
                    choiceAcount = false;

                    updateChangeAccount(changeAccountFlag);

                    if (mMainEntitiy != null && !TextUtils.isEmpty(mMainEntitiy.getFETCH_BALANCE())) {
                        mUsefulBalanceTv.setText("可取余额：" + mMainEntitiy.getFETCH_BALANCE());
                    } else {
                        mUsefulBalanceTv.setText("");
                    }

                    if (mMainEntitiy != null && !TextUtils.isEmpty(mMainEntitiy.getFUND_ACCOUNT())) {
                        mFundAccountSrc = mMainEntitiy.getFUND_ACCOUNT();
                    }

                    if (_mFuEntity != null && !TextUtils.isEmpty(_mFuEntity.getFUND_ACCOUNT())) {
                        mFundAccountDest = _mFuEntity.getFUND_ACCOUNT();
                    }

                    changeAccountFlag = true;
                }
                break;

            case R.id.allot_choiceAcLayout:

                Intent intent = new Intent();

                if (mBeans != null && mBeans.size() > 0) {
                    intent.putParcelableArrayListExtra("fuzhuBankcards", mBeans);
                }

                intent.setClass(PriceAllotActivity.this, AllotAccountDetailActivity.class);
                startActivityForResult(intent, REQUELS_CODE);

                break;
            case R.id.allotPositiveBtn:
                if (juedgeInput(mBalanceTv.getText().toString(), mUsefulBalanceTv.getText().toString())) {
                    mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
                    allotPriceConnect(mFundAccountSrc, mFundAccountDest, mBalanceTv.getText().toString());
                }
                break;
        }
    }

    /**
     * 判断输入金额是否大于可取金额
     * @return
     */
    private boolean juedgeInput(String input, String balance) {
        double _input = 0;
        double _balance = 0;
        if (!TextUtils.isEmpty(input)) {
            _input = Double.parseDouble(input);
        } else {
            CentreToast.showText(CustomApplication.getContext(), "输入金额不能为空");
            return false;
        }

        if (!TextUtils.isEmpty(balance) && balance.length() > 5) {
            balance = balance.substring(5, balance.length());
            _balance = Double.parseDouble(balance);
        } else {
            CentreToast.showText(CustomApplication.getContext(), "无可取余额");
            return false;
        }

        if (TextUtils.isEmpty(mFundAccountDest)) {
            CentreToast.showText(CustomApplication.getContext(), "请选择转入资金账号");
            return false;
        }

        if (TextUtils.isEmpty(mFundAccountSrc)) {
            CentreToast.showText(CustomApplication.getContext(), "请选择转出资金账号");
            return false;
        }

        if (_balance >= _input) {
            return true;
        } else {
            CentreToast.showText(CustomApplication.getContext(), "输入金额大于可取金额");
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUELS_CODE && resultCode == Activity.RESULT_OK) {

            if (!TextUtils.isEmpty(mMainEntitiy.getFUND_ACCOUNT())) {
                changeUItopLayout(mMainEntitiy.getFUND_ACCOUNT());
            }

            if (null != data) {
                _mFuEntity = data.getParcelableExtra("entitiy");
                mIndex = data.getIntExtra("index", -1);
                String bankNo = "";

                if (!TextUtils.isEmpty(_mFuEntity.getBANK_ACCOUNT())) {
                    bankNo = Helper.getBanksAccountNumber(_mFuEntity.getBANK_ACCOUNT());
                }

                int fundAccountLength = 0;

                if (!TextUtils.isEmpty(_mFuEntity.getFUND_ACCOUNT())) {

                    if (choiceAcount) {
                        mFundAccountSrc = _mFuEntity.getFUND_ACCOUNT();
                    } else {
                        mFundAccountDest = _mFuEntity.getFUND_ACCOUNT();
                    }

                    fundAccountLength = _mFuEntity.getFUND_ACCOUNT().length();
                }

                if (!TextUtils.isEmpty(_mFuEntity.getFETCH_BALANCE()) && !changeAccountFlag) {
                    mUsefulBalanceTv.setText("可取余额："  + _mFuEntity.getFETCH_BALANCE());
                }


                String content = "辅助资金账号\u2000" + _mFuEntity.getFUND_ACCOUNT() + "\n" + _mFuEntity.getBANK_NAME() + "(" + bankNo + ")";
                mBankCountTv.setText(formateContent(content, fundAccountLength));
            }
        } else if (requestCode == ConstantUtil.SEEEIOIN_FAILED && resultCode == RESULT_OK) {
            finish();
        }
    }

    private SpannableString formateContent(String content, int fundAccountLength) {
        SpannableString ss1 = new SpannableString(content);

        ss1.setSpan(new TextAppearanceSpan(CustomApplication.getContext(), R.style.priceAllotAcountColor),0,7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new TextAppearanceSpan(CustomApplication.getContext(), R.style.priceAllotAcountColor1),7,7 + fundAccountLength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new TextAppearanceSpan(CustomApplication.getContext(), R.style.priceAllotAcountColor2),7 + fundAccountLength, content.length() ,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss1;
    }

    private void changeUItopLayout(String account) {

        String content1 = "我的主资金账号:" + " " + account;
        SpannableString ss = new SpannableString(content1);
        ss.setSpan(new RelativeSizeSpan(1f),0,8,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(2f),8,content1.length() ,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mFundTv.setText(ss);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(Helper.dip2px(CustomApplication.getContext(), 20), 0, 0, 0);
        mFundTv.setLayoutParams(lp1);


        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.BELOW, R.id.allot_fundTv);
        lp2.addRule(RelativeLayout.ALIGN_LEFT, R.id.allot_fundTv);
        lp2.setMargins(0, 0, Helper.dip2px(CustomApplication.getContext(), 20), 0);
        mBelongToBank.setLayoutParams(lp2);

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp3.setMargins(0, 22, 0, 0);
        mAccountContentLayout.setLayoutParams(lp3);
    }

    private void updateChangeAccount(boolean falg) {

        if (falg) {
            mChangeAccountTv.setText("辅转主");
            mAccountIv1.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.price_allot_faccount));
            mAccountIv2.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.price_allot_maccount));
            mAccountTv1.setText("辅助资金账号");
            mAccountTv2.setText("主资金账号");

        } else {
            mChangeAccountTv.setText("主转辅");
            mAccountIv1.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.price_allot_maccount));
            mAccountIv2.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.price_allot_faccount));
            mAccountTv2.setText("辅助资金账号");
            mAccountTv1.setText("主资金账号");
        }
    }

    /**
     * 资金调拨网络连接
     * @param fundAccountSrc    转出资金账号
     * @param fundAccountDest   转入资金账号
     * @param  occurBalance     转账金额
     */
    private void allotPriceConnect(final String fundAccountSrc, final String fundAccountDest, String occurBalance) {
        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("FUND_ACCOUNT_SRC",fundAccountSrc);    //转出资金账号
        map2.put("FUND_ACCOUNT_DEST",fundAccountDest);   //转入资金账号
        map2.put("OCCUR_BALANCE", occurBalance);      //转账金额
        map2.put("MONEY_TYPE", "0");
        map1.put("funcid","300391");
        map1.put("token",mSession);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }
            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
//                    CentreToast.showText(CustomApplication.getContext(), "" + response);
                    return ;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    String msg = res.optString("msg");
                    if("-6".equals(code)){
                        Intent intent = new Intent();
                        intent.setClass(PriceAllotActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                        return;
                    }else if("0".equals(code)){
                        JSONArray array = res.getJSONArray("data");
                        if(null != array && array.length() > 0){
                            for(int i = 0; i < array.length();i++){
                                JSONObject json =  array.getJSONObject(i);
                                String FUND_ACCOUNT = json.optString("FUND_ACCOUNT");
                                String ENABLE_BALANCE = json.optString("ENABLE_BALANCE");
                                String FETCH_BALANCE = json.optString("FETCH_BALANCE");
                                if (fundAccountSrc.equals(FUND_ACCOUNT)) {
                                    getResultAccountPrice(FETCH_BALANCE, FUND_ACCOUNT);
                                    mUsefulBalanceTv.setText("可取余额：" + FETCH_BALANCE);
                                    mBalanceTv.setText("");
                                    CentreToast.showText(PriceAllotActivity.this,"转账申请已提交", true);
                                }
                                if (fundAccountDest.equals(FUND_ACCOUNT)) {
                                    getResultAccountPrice(FETCH_BALANCE, FUND_ACCOUNT);
                                }
                            }
                        }
                    } else {

                        showDialog(msg);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//                    String msg = "";
//                    if (null != responseValues.get("msg")) {
//                        msg = String.valueOf(responseValues.get("msg"));
//                    }
//                    if (code.equals("-6")) {
//                        Intent intent = new Intent();
//                        intent.setClass(PriceAllot.this, TransactionLoginActivity.class);
//                        startActivity(intent);
//                        return;
//                    } else if (code.equals("0")) {
//                        if (null == responseValues.get("data")) {
//                            return;
//                        }
//                        List<Map<String, Object>> data = (List<Map<String,Object>>) responseValues.get("data");
//                        if (data != null && data.size() > 0) {
//                            for (Map<String, Object> subData : data) {
//                                String FUND_ACCOUNT = "";//资金账号
//                                if (null != subData.get("FUND_ACCOUNT")) {
//                                    FUND_ACCOUNT = String.valueOf(subData.get("FUND_ACCOUNT"));
//                                }
//                                String ENABLE_BALANCE = "";//可用资金
//                                if (null != subData.get("ENABLE_BALANCE")) {
//                                    ENABLE_BALANCE = String.valueOf(subData.get("ENABLE_BALANCE"));
//                                }
//                                String FETCH_BALANCE = "";//可取金额
//                                if (null != subData.get("FETCH_BALANCE")) {
//                                    FETCH_BALANCE = String.valueOf(subData.get("FETCH_BALANCE"));
//                                }
//                                if (fundAccountSrc.equals(FUND_ACCOUNT)) {
//                                    getResultAccountPrice(FETCH_BALANCE, FUND_ACCOUNT);
//                                    mUsefulBalanceTv.setText("可取余额：" + FETCH_BALANCE);
//                                    mBalanceTv.setText("");
//
//                                }
//                                if (fundAccountDest.equals(FUND_ACCOUNT)) {
//                                    getResultAccountPrice(FETCH_BALANCE, FUND_ACCOUNT);
//                                }
//                            }
//                        }
//                    } else {
//                        MistakeDialog.showDialog(msg, PriceAllot.this);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    private void getResultAccountPrice(String FETCH_BALANCE, String ACOUNT) {


        if (mMainEntitiy.getFUND_ACCOUNT().equals(ACOUNT)) {
            mMainEntitiy.setFETCH_BALANCE(FETCH_BALANCE);
        } else if (_mFuEntity.getFUND_ACCOUNT().equals(ACOUNT)) {
            _mFuEntity.setFETCH_BALANCE(FETCH_BALANCE);
        }

        if (mIndex != -1) {
            mBeans.remove(mIndex);
            mBeans.add(mIndex, _mFuEntity);
        }

    }


    /**
     * 得到银行卡列表
     */
    private void getBanksList() {
        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("MONEY_TYPE","0");
        map2.put("ASSET_PROP","0");
        map1.put("funcid","300390");
        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                response = "{\"code\":\"0\",\"msg\":\"(资金账号列表查询成功)\",\"data\":[{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256140\",\"FUND_ACCOUNT\":\"101000910\",\"MAIN_FLAG\":\"1\",\"BANK_NAME\":\"建行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256141\",\"FUND_ACCOUNT\":\"101000911\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"农行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256142\",\"FUND_ACCOUNT\":\"101000912\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"工行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256143\",\"FUND_ACCOUNT\":\"101000913\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"招商行存管\"}]}";

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {
                    mIsFirst = true;
                    Intent intent = new Intent();
                    intent.setClass(PriceAllotActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);

                    return;
                }

                if (!bean.getCode().equals("0")) {
                    showDialog(bean.getMsg());
                    return;
                }


                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    mBeans = new ArrayList<BankAccountEntity>();
                    mMainEntitiy = new BankAccountEntity();
                    for (BankAccountEntity.AccountInfo accountInfo : bean.getData()) {
                        BankAccountEntity _bean = new BankAccountEntity();
                        _bean.setBANK_NO(accountInfo.getBANK_NO());
                        _bean.setBALANCE(accountInfo.getBALANCE());
                        _bean.setFETCH_BALANCE(accountInfo.getFETCH_BALANCE());
                        _bean.setENABLE_BALANCE(accountInfo.getENABLE_BALANCE());
                        _bean.setBANK_ACCOUNT(accountInfo.getBANK_ACCOUNT());
                        _bean.setFUND_ACCOUNT(accountInfo.getFUND_ACCOUNT());
                        _bean.setMAIN_FLAG(accountInfo.getMAIN_FLAG());
                        _bean.setBANK_NAME(accountInfo.getBANK_NAME());

                        if (!"1".equals(accountInfo.getMAIN_FLAG())) {
                            mBeans.add(_bean);
                        } else {
                            mMainEntitiy = _bean;
                        }
                    }

                    if (!TextUtils.isEmpty(mMainEntitiy.getFUND_ACCOUNT())) {
                        mFundAccountDest = mMainEntitiy.getFUND_ACCOUNT();
                    }

                    initTopContentView(mAccountContentLayout,""+ mMainEntitiy.getFUND_ACCOUNT(),"" + mMainEntitiy.getBANK_NAME(), mMainEntitiy.getBANK_ACCOUNT());
                }
            }
        });
    }

    public void showDialog(String msg){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),PriceAllotActivity.class.toString());
        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
            @Override
            public void confirmOnclick() {
                customCenterDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(mBalanceTv.getText().toString())) {
            mSubmitBtn.setEnabled(true);
            mSubmitBtn.setBackgroundResource(R.mipmap.price_allot_positivebtn);
        } else {
            mSubmitBtn.setEnabled(false);
            mSubmitBtn.setBackgroundResource(R.mipmap.nagtive_btn_ic);

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_price_allot;
    }
}
