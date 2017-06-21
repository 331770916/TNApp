package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.BanksTransferAccountsActivity;
import com.tpyzq.mobile.pangu.adapter.trade.TransferAcountsAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITabDataObserver;
import com.tpyzq.mobile.pangu.interfac.TransferAcountObsever;
import com.tpyzq.mobile.pangu.interfac.TransferAcountsSubject;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.NoSoftInputEditText;
import com.tpyzq.mobile.pangu.util.keyboard.UsefulKeyBoard;
import com.tpyzq.mobile.pangu.util.panguutil.PanguParameters;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.tpyzq.mobile.pangu.view.keybody.InputPasswordView;
import com.tpyzq.mobile.pangu.view.keybody.PopKeyBody;
import com.yzd.unikeysdk.OnInputDoneCallBack;
import com.yzd.unikeysdk.PasswordKeyboard;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/24.
 * 银证转账， 证券转银行Tab页签
 */
public class TransferAcountsTab extends BaseTransferSubjectTabView implements
        ViewPager.OnPageChangeListener, View.OnClickListener, TransferAcountsSubject,
        OnInputDoneCallBack, BanksTransferAccountsActivity.BanksTransferBackListener,
        PopKeyBody.ContentListener, TextWatcher {

    private LinearLayout    mDotLayout;
    private RelativeLayout  mRootLayout;
    private ViewPager       mViewPager;
    private TextView        mHintTv;
    private ImageView       mHintIv;
    private EditText mPriceEidt;
    private NoSoftInputEditText        mBankPasswrodEdit;
    private TransferAcountsAdapter mAdapter;
    private String          mSession;
    private Activity        mActivity;
    private Dialog mLoadingDialog;
    private ArrayList<BaseTransferObserverTabView> mTabs;
    private List<BankAccountEntity.AccountInfo> mAccountInfos;
    private BanksTransferAccountsResultCode mBanksTransferAccountsResultCode;
    public static List<TransferAcountObsever> mTransferAcountObsevers;
    private PasswordKeyboard mPasswordKeyboard;
    private boolean isMainTab;
    private ViewGroup mViewGroup;
    private PopKeyBody mPopKeyBody;
    private boolean isKeyFlag;//是否使用插件键盘的状态
    private Button mTransferBtn;

    public static final String TAG = "TransferAcountsTab";

    public static final String CONNECTBANKCARDSNUMBER = "connectBankCardsNumber";
    public static final String TRANSFERTOBANK = "transferToBank";
    public static final String TRANFERTOSTOCK = "tranferToStock";

    public TransferAcountsTab(ViewGroup viewGroup, Activity activity, boolean isMainTab, ArrayList<ITabDataObserver> observers, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        super(viewGroup, activity, isMainTab, observers, banksTransferAccountsResultCode);
        mActivity = activity;
        mBanksTransferAccountsResultCode = banksTransferAccountsResultCode;
    }

    @Override
    public void initView(final ViewGroup viewGroup, View view, final Activity activity, boolean isMainTab, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        mActivity = activity;
        mViewGroup = viewGroup;
        if(activity instanceof BanksTransferAccountsActivity){
            ((BanksTransferAccountsActivity)activity).setBackListener(this);
        }

        mLoadingDialog = LoadingDialog.initDialog(activity, "正在提交...");
        mTransferAcountObsevers = new ArrayList<>();
        mAccountInfos = new ArrayList<>();
        mTabs = new ArrayList<>();

        mViewPager = (ViewPager) view.findViewById(R.id.banksTviewPager);
        mDotLayout = (LinearLayout) view.findViewById(R.id.banksTtop5Layout);
        mPriceEidt = (EditText) view.findViewById(R.id.transferPriceEdit);
        mPriceEidt.addTextChangedListener(this);
        mRootLayout = (RelativeLayout) view.findViewById(R.id.banksTFramLayout);

        mBankPasswrodEdit = (NoSoftInputEditText) view.findViewById(R.id.transferBankPassword);
        mBankPasswrodEdit.addTextChangedListener(this);

        mHintIv = (ImageView) view.findViewById(R.id.accountsHintIv);
        mHintTv = (TextView) view.findViewById(R.id.accountsHintTv);

        mTransferBtn = (Button) view.findViewById(R.id.banksTcommit);
        mTransferBtn.setOnClickListener(this);
        mTransferBtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.texts));
        mTransferBtn.setClickable(false);


        mAdapter = new TransferAcountsAdapter();

        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);  // 具体缓存页数自己订吧
        mViewPager.setPageMargin(60);
        mViewPager.addOnPageChangeListener(this);

        this.isMainTab = isMainTab;

        Object object = UsefulKeyBoard.getInstance().initUserKeyBoard(true, this, true, null);
        if (object != null && object instanceof PopKeyBody) {
            mPopKeyBody = (PopKeyBody) object;
        } else {
            isKeyFlag = true;
        }
    }

    /**
     * 展示银证转账界面， 还是 证券转银行界面
     * @param isMainTab
     */
    public void setMainTab (boolean isMainTab) {
        this.isMainTab = isMainTab;
    }

    private boolean juedgePriceAndPasswordIsNull(String price, String password, boolean isNeedPassword) {

        if (TextUtils.isEmpty(price)) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "请输入转账金额");

            return false;
        } else if (TextUtils.isEmpty(password) && isNeedPassword) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "请输入账户密码");

            return false;
        } else if (!Helper.isDecimal(price)) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "请输入正确的金额");
            return false;
        }

        return true;
    }

    private boolean juedgePriceIsOverfulfil(String price, String totalPrice){

        if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(totalPrice)) {

            if (Double.parseDouble(price) > Double.parseDouble(totalPrice)) {
                Helper.getInstance().showToast(CustomApplication.getContext(), "当前输入的金额大于可转出金额");
                return false;
            }
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        String _price = mPriceEidt.getText().toString();
        String _password = keyboardResult;
        int position = mViewPager.getCurrentItem();
        boolean isNeedPassword = !"0".equals(mAccountInfos.get(position).getMONEY_INTO());


        if (juedgePriceAndPasswordIsNull(_price, _password, isNeedPassword) && mAccountInfos != null && mAccountInfos.size() > 0) {

            mTransferBtn.setClickable(false);
            mTransferBtn.setFocusable(false);

            mLoadingDialog.show();
            if (!isMainTab) {
                tranferToStock(_price, mAccountInfos.get(position).getBANK_NO(), _password,
                        mAccountInfos.get(position).getFUND_ACCOUNT(), isNeedPassword);
            } else {
                if (juedgePriceIsOverfulfil(_price, mAccountInfos.get(position).getBALANCE())) {
                    transferToBank(_price, mAccountInfos.get(position).getBANK_NO(),_password,
                            mAccountInfos.get(position).getFUND_ACCOUNT(), isNeedPassword);
                }
            }

        }
    }

    @Override
    public void onbackForward() {
//        if(mPasswordKeyboard.isShow()){
//            mKeyBoardUtil.hideSystemKeyBoard();
//            mKeyBoardUtil.hideAllKeyBoard();
//            mKeyBoardUtil.hideKeyboardLayout();
//
//            if(mActivity instanceof BanksTransferAccounts){
//                ((BanksTransferAccounts)mActivity).setInterception(false);
//            }
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        settingTIV(position, mAccountInfos, isMainTab);

        for (int i = 0; i < mDotLayout.getChildCount(); i++) {
            if (i != position) {
                mDotLayout.getChildAt(i).setBackgroundResource(R.color.lineColor);
            } else {
                mDotLayout.getChildAt(position).setBackgroundResource(R.color.dotSelectd);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void isMainTab (boolean isMainTab) {

        mPriceEidt.setText("");
        mBankPasswrodEdit.setText("");

        if (mAccountInfos != null && mAccountInfos.size() > 0 && mTabs != null && mTabs.size() > 0) {
            for (int i = 0; i < mTabs.size(); i++ ) {
                mTabs.get(i).update(mAccountInfos.get(i), isMainTab, i);
            }
        }

        this.isMainTab = isMainTab;
        settingTIV(mViewPager.getCurrentItem(), mAccountInfos, isMainTab);
    }

    @Override
    public void onResume() {

        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        connectBankCardsNumber(this.isMainTab);
    }

    @Override
    public void onStop() {
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

    private void connectBankCardsNumber(final boolean isMainTab) {

        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("MONEY_TYPE","0");
        map2.put("ASSET_PROP","0");
        map1.put("funcid","300390");
        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }

                //{"code":"0","msg":"(资金账号列表查询成功)","data":[{"BANK_FUNDS_QUERY":"0","BANK_NO":"4","BALANCE":"804485019.82","BANK_ACCOUNT":"6212262507003148642","FUND_ACCOUNT":"240003875","MONEY_INTO":"0","MONEY_OUT":"1","MAIN_FLAG":"1","BANK_NAME":"工行存管"}]}
//{"code":"0","msg":"(资金账号列表查询成功)","data":[{"BANK_FUNDS_QUERY":"1","BANK_NO":"2","BALANCE":"1802919.54","BANK_ACCOUNT":"32039337","FUND_ACCOUNT":"610002680","MONEY_INTO":"1","MONEY_OUT":"1","MAIN_FLAG":"1","BANK_NAME":"农行存管"},{"BANK_FUNDS_QUERY":"1","BANK_NO":"B","BALANCE":"1000.00","BANK_ACCOUNT":"6226200105584701","FUND_ACCOUNT":"610500019","MONEY_INTO":"2","MONEY_OUT":"1","MAIN_FLAG":"0","BANK_NAME":"民生银行"}]}

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {
                    mBanksTransferAccountsResultCode.getCode("-6", CONNECTBANKCARDSNUMBER, true);
                    return;
                }

                if (!bean.getCode().equals("0")) {
                    MistakeDialog.showDialog(bean.getMsg(), mActivity, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
//                            mActivity.finish();
                        }
                    });
                    return;
                }

                ArrayList<BaseTransferObserverTabView> tabs = new ArrayList<BaseTransferObserverTabView>();
                mAccountInfos.clear();
                mTabs.clear();
                mDotLayout.removeAllViews();

                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    for (int i = 0; i < bean.getData().size(); i++) {
                        View view = new View(CustomApplication.getContext());
                        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(48, 12);
                        view.setLayoutParams(params);
                        setMargins(view, 10, 0, 10, 0);
                        view.setBackgroundResource(R.color.lineColor);

                        SubTransferAcountsTab subTransferAcountsTab = new SubTransferAcountsTab(mActivity, TransferAcountsTab.this, tabs, mBanksTransferAccountsResultCode);
                        mAccountInfos = bean.getData();

                        BankAccountEntity.AccountInfo accountInfo = bean.getData().get(i);

                        subTransferAcountsTab.update(accountInfo, isMainTab, i);

                        mTabs.add(subTransferAcountsTab);

                        mDotLayout.addView(view, i);
                    }

                    mDotLayout.getChildAt(0).setBackgroundResource(R.color.dotSelectd);

                    settingTIV(0, mAccountInfos, isMainTab);
                }

                if (mTabs.size() == 1) {
                    mDotLayout.setVisibility(View.GONE);
                }

                mAdapter.setDatas(mTabs);
            }
        });
    }

    /**
     * 设置输入金额密码以及显示
     * @param position
     */
    private void settingTIV (int position, List<BankAccountEntity.AccountInfo> mAccountInfos, boolean isMainTab) {
        if (!isMainTab && mAccountInfos != null && mAccountInfos.size() > 0) {
            mHintIv.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mAccountInfos.get(position).getMAIN_FLAG()) && "1".equals(mAccountInfos.get(position).getMAIN_FLAG())) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mHintIv.setLayoutParams(lp);

                mHintIv.setVisibility(View.VISIBLE);
                mHintIv.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.zhuacount_ic));
                mHintTv.setText("资金账号：" + mAccountInfos.get(position).getFUND_ACCOUNT());

            }

            if (!TextUtils.isEmpty(mAccountInfos.get(position).getMAIN_FLAG()) && "0".equals(mAccountInfos.get(position).getMAIN_FLAG())) {

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mHintIv.setLayoutParams(lp);
                mHintIv.setVisibility(View.VISIBLE);
                mHintIv.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.fuzhu_ic));
                mHintTv.setText("资金账号：" + mAccountInfos.get(position).getFUND_ACCOUNT());
            }


            if (!TextUtils.isEmpty(mAccountInfos.get(position).getMONEY_INTO())) {

                if ("0".equals(mAccountInfos.get(position).getMONEY_INTO())) {
                    mBankPasswrodEdit.setHint("不需要输入密码");
                    mBankPasswrodEdit.setClickable(false);
                    mBankPasswrodEdit.setFocusable(false);
                    mBankPasswrodEdit.setFocusableInTouchMode(false);
                    mBankPasswrodEdit.setHintTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.photoBg));
                    mBankPasswrodEdit.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.drawable.banks_transfer_noselectedbg));
                    mBankPasswrodEdit.setOnTouchListener(null);
                    mBankPasswrodEdit.setPasswordKeyboard(null);

                } else if ("1".equals(mAccountInfos.get(position).getMONEY_INTO())) {
                    mBankPasswrodEdit.setHint("校验银行密码");
                    mBankPasswrodEdit.setClickable(true);
                    mBankPasswrodEdit.setFocusable(true);
                    mBankPasswrodEdit.setFocusableInTouchMode(true);
                    mBankPasswrodEdit.setOnTouchListener(onTouchListener);
                } else if ("2".equals(mAccountInfos.get(position).getMONEY_INTO())) {
                    mBankPasswrodEdit.setHint("校验银行密码");
                    mBankPasswrodEdit.setClickable(true);
                    mBankPasswrodEdit.setFocusable(true);
                    mBankPasswrodEdit.setFocusableInTouchMode(true);
                    mBankPasswrodEdit.setOnTouchListener(onTouchListener);
                } else if ("3".equals(mAccountInfos.get(position).getMONEY_INTO())) {
                    mBankPasswrodEdit.setHint("校验银行密码");
                    mBankPasswrodEdit.setClickable(true);
                    mBankPasswrodEdit.setFocusable(true);
                    mBankPasswrodEdit.setFocusableInTouchMode(true);
                    mBankPasswrodEdit.setOnTouchListener(onTouchListener);
                }
            }

        } else if (isMainTab && mAccountInfos != null && mAccountInfos.size() > 0) {
            if (!TextUtils.isEmpty(mAccountInfos.get(position).getBANK_NO())) {
                mHintIv.setVisibility(View.VISIBLE);
                int res = PanguParameters.getBankLogo().get(mAccountInfos.get(position).getBANK_NO());
                mHintIv.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), res));
            }

            String account = "";
            if (!TextUtils.isEmpty(mAccountInfos.get(position).getBANK_ACCOUNT())){
                account = mAccountInfos.get(position).getBANK_ACCOUNT();
                mHintTv.setText(mAccountInfos.get(position).getBANK_NAME() + "(" + "**** " + account.substring(account.length() - 4, account.length()) + ")");
            } else {
                mHintTv.setText(mAccountInfos.get(position).getBANK_NAME() + "(" + "**** " + account + ")");
            }


            if (!TextUtils.isEmpty(mAccountInfos.get(position).getMONEY_OUT())) {

                if ("0".equals(mAccountInfos.get(position).getMONEY_OUT())) {
                    mBankPasswrodEdit.setHint("不需要输入密码");
                    mBankPasswrodEdit.setClickable(false);
                    mBankPasswrodEdit.setFocusable(false);
                    mBankPasswrodEdit.setFocusableInTouchMode(false);
                    mBankPasswrodEdit.setHintTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.photoBg));
                    mBankPasswrodEdit.setBackgroundDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.drawable.banks_transfer_noselectedbg));
                    mBankPasswrodEdit.setOnTouchListener(null);
                    mBankPasswrodEdit.setPasswordKeyboard(null);

                } else if ("1".equals(mAccountInfos.get(position).getMONEY_OUT())) {
                    mBankPasswrodEdit.setHint("校验资金密码");
                    mBankPasswrodEdit.setClickable(true);
                    mBankPasswrodEdit.setFocusable(true);
                    mBankPasswrodEdit.setFocusableInTouchMode(true);
                    mBankPasswrodEdit.setOnTouchListener(onTouchListener);
                } else if ("2".equals(mAccountInfos.get(position).getMONEY_OUT())) {
                    mBankPasswrodEdit.setHint("校验资金密码");
                    mBankPasswrodEdit.setClickable(true);
                    mBankPasswrodEdit.setFocusable(true);
                    mBankPasswrodEdit.setFocusableInTouchMode(true);
                    mBankPasswrodEdit.setOnTouchListener(onTouchListener);
                }
            }

        }
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (isKeyFlag) {
                InputMethodManager imm = (InputMethodManager) mActivity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(mViewGroup.getWindowToken(), 0);

                showKeyboardWithHeader(mActivity);
            } else {
                InputMethodManager imm = (InputMethodManager) mActivity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(mViewGroup.getWindowToken(), 0);

                if (mPopKeyBody != null && !mPopKeyBody.isShow()) {
                    mPopKeyBody.clearContent();
                    mPopKeyBody.setTitleText("请输入查询密码");
                    mPopKeyBody.setTitleColor(R.color.black);
                    Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.inputpasswor);
                    mPopKeyBody.setTitleImage(drawable, null, null, null);
                    mPopKeyBody.show(mRootLayout);
                }
            }
            return false;
        }
    };

    private void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    /**
     * 证券转银行转出资金接口
     */
    private void transferToBank(final String transferPrice, String bankNumber, String bankPassword,
                                String account, boolean isNeedPassword) {
        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("CURRENCY", "0");
        map2.put("CPTL_AMT", transferPrice);       //转账金额
        map2.put("EXT_INST",bankNumber);        //银行代码
        map2.put("ACC_PWD", bankPassword);    //银行密码
        map2.put("REMARK", "");
        if ("false".equals(Db_PUB_USERS.queryingCertification()) || isNeedPassword) {
            map2.put("PWD_TYPE", "0");
        } else {

            String unikey = "";
            try {
                unikey = UniKey.getInstance().getUnikeyId();
            } catch (UnikeyException e) {
                e.printStackTrace();
            }

            map2.put("PWD_TYPE", "1");
            map2.put("MOBILE", UserUtil.userId);
            map2.put("UNIKEYID", unikey);
            map2.put("APP_TYPE", "1");
        }
        map2.put("ACCOUNT", account);
        map1.put("funcid","300220");
        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(CustomApplication.getContext(), e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }

                mTransferBtn.setClickable(true);
                mTransferBtn.setFocusable(true);

                mLoadingDialog.dismiss();

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {
                    mBanksTransferAccountsResultCode.getCode("-6", TRANSFERTOBANK, false);
                }

                if (bean.getCode().equals("0")) {
                    ResultDialog.getInstance().show("转账申请已提交", R.mipmap.lc_success);
                    keyboardResult= "";
                    mPriceEidt.setText("");
                    mBankPasswrodEdit.setText("");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("transferToBank", transferPrice);
                    notifyObservers(map, "transferToBank");

                } else {
                    keyboardResult= "";
                    mPriceEidt.setText("");
                    mBankPasswrodEdit.setText("");
                    MistakeDialog.showDialog(bean.getMsg(), mActivity, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
//                            mActivity.finish();
                        }
                    });

                }
            }
        });
    }

    /**
     * 银行转证券转入资金接口
     */
    private void tranferToStock(final String transferPrice, String bankNumber, String bankPassword, String account, boolean isNeedPassword) {
        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("CURRENCY", "0");
        map2.put("CPTL_AMT", transferPrice);   //转账金额
        map2.put("EXT_INST",bankNumber);    //银行代码
        map2.put("EXT_ACC_PWD", bankPassword);    //银行密码
        map2.put("EXT_SERIAL_NO","");
        map2.put("REMARK", "");

        if ("false".equals(Db_PUB_USERS.queryingCertification()) || !isNeedPassword) {
            map2.put("PWD_TYPE", "0");
        } else {

            String unikey = "";
            try {
                unikey = UniKey.getInstance().getUnikeyId();
            } catch (UnikeyException e) {
                e.printStackTrace();
            }

            map2.put("PWD_TYPE", "1");
            map2.put("MOBILE", UserUtil.userId);
            map2.put("UNIKEYID", unikey);
            map2.put("APP_TYPE", "1");
        }


        map2.put("ACCOUNT", account);
        map1.put("funcid","300210");
        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(CustomApplication.getContext(), e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }

                mTransferBtn.setClickable(true);
                mTransferBtn.setFocusable(true);

                mLoadingDialog.dismiss();

                //{"code":"0","msg":"332200成功","data":[{"SERIAL_NO":"22"}]}

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {
                    mBanksTransferAccountsResultCode.getCode("-6", TRANFERTOSTOCK, false);
                }

                if (bean.getCode().equals("0")) {
                    ResultDialog.getInstance().show("转账申请已提交", R.mipmap.lc_success);

                    keyboardResult= "";
                    mPriceEidt.setText("");
                    mBankPasswrodEdit.setText("");

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("tranferToStock", transferPrice);
                    notifyObservers(map, "tranferToStock");
                } else {
                    keyboardResult= "";
                    mPriceEidt.setText("");
                    mBankPasswrodEdit.setText("");
                    MistakeDialog.showDialog(bean.getMsg(), mActivity, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
//                            mActivity.finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void registerObserver(TransferAcountObsever observer) {
        mTransferAcountObsevers.add(observer);
    }

    @Override
    public void removeObserver(TransferAcountObsever observer) {
        int num = mTransferAcountObsevers.indexOf(observer);
        if (num >= 0) {
            mTransferAcountObsevers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(Map<String, String> map, String tag) {
        LogHelper.e(TAG, "-----notifyObservers-----" + mTransferAcountObsevers.size());
        for (TransferAcountObsever observer : mTransferAcountObsevers) {
            observer.updateTransferAcount(map, tag);
        }
    }

    @Override
    public int getTabLayoutId() {
        return R.layout.tab_transfer_acounts;
    }



    /**
     * 键盘插件数据获取
     */
    private void showKeyboardWithHeader(Activity activity) {
        try {
            if (mPasswordKeyboard == null) {
                mPasswordKeyboard = UniKey.getInstance().getPasswordKeyboard(activity, this, 6, ConstantUtil.USERFUL_KEYBOARD, "custom_keyboard_view2");
            }

            if (!mPasswordKeyboard.isShow()) {
                mBankPasswrodEdit.setPasswordKeyboard(mPasswordKeyboard);
                mPasswordKeyboard.show();
            }
        } catch (UnikeyException e) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
        }
    }

    @Override
    public void getInputEncrypted(String s) {
        keyboardResult = s;

        mBankPasswrodEdit.setText(s);
        mBankPasswrodEdit.setSelection(mBankPasswrodEdit.getText().length());
    }


    private String keyboardResult = "";
    @Override
    public void getContent(String num) {
        keyboardResult = num;
    }

    @Override
    public void doPositive() {
        mBankPasswrodEdit.setText(keyboardResult);
        mBankPasswrodEdit.setSelection(mBankPasswrodEdit.getText().length());
    }

    @Override
    public void getCharNum(int num) {
        InputPasswordView inputPasswordView = (InputPasswordView)mPasswordKeyboard.getView("inputPasswordView");
        //注意目前一定要判空，如下
        if ( inputPasswordView != null ) {
            String tmp = "";
            for( int i = 0; i < num; i++ ) {
                tmp += "*";
            }

            inputPasswordView.setText(tmp);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTabs == null || mTabs.size() <= 0) {
            return;
        }
//        int position = mViewPager.getCurrentItem();
//        boolean isNeedPassword = !"0".equals(mAccountInfos.get(position).getMONEY_INTO());
        boolean isNeedPassword = mBankPasswrodEdit.isFocusable();
        if (isNeedPassword) {
            if (!TextUtils.isEmpty(mPriceEidt.getText().toString()) && !TextUtils.isEmpty(mBankPasswrodEdit.getText().toString())) {
                mTransferBtn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.calendarBtnColor));
                mTransferBtn.setClickable(true);
            } else {
                mTransferBtn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.texts));
                mTransferBtn.setClickable(false);
            }
        } else {
            if (!TextUtils.isEmpty(mPriceEidt.getText().toString())) {
                mTransferBtn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.calendarBtnColor));
                mTransferBtn.setClickable(true);
            } else {
                mTransferBtn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.texts));
                mTransferBtn.setClickable(false);
            }
        }

//        if (!TextUtils.isEmpty(mPriceEidt.getText().toString()) && !TextUtils.isEmpty(mBankPasswrodEdit.getText().toString())) {
//            mTransferBtn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.calendarBtnColor));
//            mTransferBtn.setClickable(true);
//        } else {
//            mTransferBtn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.texts));
//            mTransferBtn.setClickable(false);
//        }
    }
}
