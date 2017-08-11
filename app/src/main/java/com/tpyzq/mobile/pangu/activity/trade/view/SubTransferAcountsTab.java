package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITab;
import com.tpyzq.mobile.pangu.interfac.TransferAcountObsever;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.UsefulKeyBoard;
import com.tpyzq.mobile.pangu.util.panguutil.PanguParameters;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.keybody.InputPasswordView;
import com.tpyzq.mobile.pangu.view.keybody.PopKeyBody;
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
 *
 * 银证转账，  证券转银行中ViewPagerD
 */
public class SubTransferAcountsTab extends BaseTransferObserverTabView implements
        View.OnClickListener, PopKeyBody.ContentListener, TransferAcountObsever, com.yzd.unikeysdk.OnInputDoneCallBack{

    private ImageView mPager_tab2_iv;
    private TextView  mZiji_Account;
    private TextView  mZiji_Use;


    private ImageView mImageView;
    private TextView  mBankName;
    private TextView  mBankNumber;
    private TextView  mBalanceTv;

    private LinearLayout  mLayout1;
    private RelativeLayout mLayout2;

    private String mPassword;
    private String  mSession;
    private Activity mActivity;
    private BanksTransferAccountsResultCode mBanksTransferAccountsResultCode;
    private BankAccountEntity.AccountInfo mAccountInfo;
    private ArrayList<BankAccountEntity.AccountInfo> mAccountInfos;
    private static final String TAG = "SubTransferAcountsTab";
    public  static final String QUERYBALANCE = "queryBalance";
    private Dialog mLoadingDialog;

    private PasswordKeyboard mKeyboard;
    private PopKeyBody mPopKeyBody;
    private boolean isKeyFlag;//是否使用插件键盘的状态
    private View mView;

    public SubTransferAcountsTab(Activity activity, ITab iTab, ArrayList<BaseTransferObserverTabView> tabs, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        super(activity, iTab, tabs, banksTransferAccountsResultCode);
        mBanksTransferAccountsResultCode = banksTransferAccountsResultCode;
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
        mAccountInfos = new ArrayList<>();
        mView = view;

        mLoadingDialog = LoadingDialog.initDialog(activity, "正在查询...");
        TransferAcountsTab.mTransferAcountObsevers.add(this);
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        mImageView = (ImageView) view.findViewById(R.id.pager_tab0_iv);
        mBankName = (TextView) view.findViewById(R.id.pager_bankName);
        mBankNumber = (TextView) view.findViewById(R.id.pager_bankNumber);

        mPager_tab2_iv = (ImageView) view.findViewById(R.id.pager_tab_iv);
        mZiji_Account = (TextView) view.findViewById(R.id.pager_zjName);
        mZiji_Use = (TextView) view.findViewById(R.id.pager_outPrice);

        mLayout1 = (LinearLayout) view.findViewById(R.id.banks_transfer_itemLayout1);
        mLayout2 = (RelativeLayout) view.findViewById(R.id.banks_transfer_itemLayout2);

        mBalanceTv = (TextView) view.findViewById(R.id.pager_query);
        mBalanceTv.setOnClickListener(this);

        Object object = UsefulKeyBoard.getInstance().initUserKeyBoard(true, this, true, null);
        if (object != null && object instanceof PopKeyBody) {
            mPopKeyBody = (PopKeyBody) object;
        } else {
            isKeyFlag = true;
        }
    }

    private int mPosition;
    @Override
    public void update(Object object,final boolean isMainTab, int position) {
        mPosition = position;

        connectBankCardsNumber(new CompliteListener() {
            @Override
            public void complite() {
                updateUI();

                if (!isMainTab) {
                    mLayout1.setVisibility(View.VISIBLE);
                    mLayout2.setVisibility(View.GONE);
                } else {
                    mLayout1.setVisibility(View.GONE);
                    mLayout2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_query:

                if (!TextUtils.isEmpty(mAccountInfo.getBANK_FUNDS_QUERY())) {
                    //0不校验1校验银行密码2不支持银行查询
                    if ("0".equals(mAccountInfo.getBANK_FUNDS_QUERY())) {

                        String queryType = mAccountInfo.getBANK_FUNDS_QUERY();

                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.initDialog(mActivity, "正在查询...");
                        } else {
                            mLoadingDialog.show();
                        }

                        queryBalance("", queryType);
                        return;
                    } else if ("2".equals(mAccountInfo.getBANK_FUNDS_QUERY())) {
                        return;
                    }
                }


                if (isKeyFlag) {
                    try {
                        mKeyboard = UniKey.getInstance().getPasswordKeyboard(mActivity, SubTransferAcountsTab.this, 6, ConstantUtil.USERFUL_KEYBOARD, "custom_keyboard_view2");
                        mKeyboard.show();

                    } catch (UnikeyException e) {
                        e.printStackTrace();
                        Helper.getInstance().showToast(CustomApplication.getContext(), "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
                    }
                } else {

                    hideSystemKeyBoard(mActivity.findViewById(R.id.transferPriceEdit));

                    mPopKeyBody.clearContent();
                    mPopKeyBody.setTitleText("请输入查询密码");
                    mPopKeyBody.setTitleColor(R.color.black);
                    Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.inputpasswor);
                    mPopKeyBody.setTitleImage(drawable, null, null, null);
                    mPopKeyBody.show(mActivity.findViewById(R.id.banksTFramLayout));
                }
                break;
        }
    }


    public void hideSystemKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) (mActivity)
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void getContent(String num) {
        mPassword = num;
    }

    @Override
    public void doPositive() {

        if (!TextUtils.isEmpty(mPassword) && mPassword.length() == 6) {

            if (mLoadingDialog == null) {
                mLoadingDialog = LoadingDialog.initDialog(mActivity, "正在查询...");
            } else {
                mLoadingDialog.show();
            }

            queryBalance(mPassword, "0");
        }

    }

    /**
     * 查询余额
     * @param queryType 0不校验1校验银行密码2不支持银行查询
     */
    private void queryBalance(String s, String queryType) {

        if (TextUtils.isEmpty(mAccountInfo.getBANK_NO())) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "银行代码为空");
            return;
        }

        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("CURRENCY","0");
        map2.put("BANK_NO",mAccountInfo.getBANK_NO());
        map2.put("ACCOUNT", mAccountInfo.getFUND_ACCOUNT());
        map2.put("BANK_PASSWORD", s);

        if ("0".equals(UserUtil.Keyboard) || "0".equals(queryType)) {
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


        map1.put("funcid","300310");

        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());

                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }

                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }


                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {
                    mBanksTransferAccountsResultCode.getCode("-6", QUERYBALANCE, false);
                    return;
                }

                if (!bean.getCode().equals("0")) {
                    showDialog(bean.getMsg());
                    return;
                }

                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    List<BankAccountEntity.AccountInfo> accountInfos = bean.getData();
                    for (BankAccountEntity.AccountInfo accountInfo : accountInfos) {

                        mBalanceTv.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                        mBalanceTv.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
                        mBalanceTv.setText("余额:" + accountInfo.getLAST_BALANCE());
                        mBalanceTv.setClickable(false);
                        mBalanceTv.setFocusable(false);
                    }
                }
            }
        });
    }


    private void connectBankCardsNumber(final CompliteListener compliteListener) {
        mAccountInfos.clear();
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        } else {
            mLoadingDialog = LoadingDialog.initDialog(mActivity, "正在查询...");
            mLoadingDialog.show();
        }

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
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }

                LogHelper.e(TAG, e.toString());
                compliteListener.complite();
            }

            @Override
            public void onResponse(String response, int id) {

                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }

                if (TextUtils.isEmpty(response)) {
                    return ;
                }

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {
                    mBanksTransferAccountsResultCode.getCode("-6", TAG, true);
                    return;
                }

                if (!bean.getCode().equals("0")) {
                    showDialog(bean.getMsg());
                    return;
                }

                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    for (int i = 0; i < bean.getData().size(); i++) {
                        BankAccountEntity.AccountInfo accountInfo = bean.getData().get(i);
                        mAccountInfos.add(accountInfo);
                    }

                }
                compliteListener.complite();
            }
        });
    }

    private void showDialog(String msg){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(mActivity.getFragmentManager(),SubTransferAcountsTab.class.toString());
        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
            @Override
            public void confirmOnclick() {
                customCenterDialog.dismiss();
                mActivity.finish();
            }
        });
    }


    @Override
    public void updateTransferAcount(Map<String, String> map, String tag) {
        connectBankCardsNumber(new CompliteListener() {
            @Override
            public void complite() {
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (mAccountInfos != null && mAccountInfos.size() > 0) {
            mAccountInfo = mAccountInfos.get(mPosition);
        } else {
            return;
        }

        mBankNumber.setText(Helper.transferBanksNumber(mAccountInfo.getBANK_ACCOUNT()));
        mBankName.setText(mAccountInfo.getBANK_NAME());

        if (!TextUtils.isEmpty(mAccountInfo.getBANK_NO())) {
            int res = PanguParameters.getBankLogo().get(mAccountInfo.getBANK_NO());
            mImageView.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), res));
        }

        if (!TextUtils.isEmpty(mAccountInfo.getBANK_FUNDS_QUERY())) {
            //0不校验1校验银行密码2不支持银行查询
            if ("2".equals(mAccountInfo.getBANK_FUNDS_QUERY())) {
                mBalanceTv.setBackgroundDrawable(null);
                mBalanceTv.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                mBalanceTv.setText("不支持余额查询");
                mBalanceTv.setClickable(false);
                mBalanceTv.setFocusable(false);
            } else {
                mBalanceTv.setClickable(true);
                mBalanceTv.setFocusable(true);
                mBalanceTv.setText("查询余额");
                mBalanceTv.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
                mBalanceTv.setBackgroundResource(R.drawable.bg_pagerbankquery);
            }
        }

        if ("0".equals(mAccountInfo.getMAIN_FLAG())) {
            mZiji_Account.setText("辅助资金账号：" + mAccountInfo.getFUND_ACCOUNT());
            mPager_tab2_iv.setImageResource(R.mipmap.assist);
        } else {
            mZiji_Account.setText("主资金账号：" + mAccountInfo.getFUND_ACCOUNT());
            mPager_tab2_iv.setImageResource(R.mipmap.main);
        }

        mZiji_Use.setText("可转出金额：" + mAccountInfo.getFETCH_BALANCE() + "元");

    }

    @Override
    public int getTabLayoutId() {
        return R.layout.fragment_banks_transfer;
    }

    @Override
    public void getInputEncrypted(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }

        mLoadingDialog.show();
        if (s.length() == 6) {
            queryBalance(s, "1");
        }
    }

    @Override
    public void getCharNum(int num) {
        InputPasswordView inputPasswordView = (InputPasswordView)mKeyboard.getView("inputPasswordView");
        //注意目前一定要判空，如下
        if ( inputPasswordView != null ) {
            String tmp = "";
            for( int i = 0; i < num; i++ ) {
                tmp += "*";
            }

            inputPasswordView.setText(tmp);
        }

    }

    private interface CompliteListener {
        public void complite();
    }
}
