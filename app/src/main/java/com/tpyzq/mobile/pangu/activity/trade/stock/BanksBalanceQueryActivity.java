package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.BalanceQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.UsefulKeyBoard;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
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
 * Created by zhangwenbo on 2016/8/26.
 * 多银行余额查询
 */
public class BanksBalanceQueryActivity extends BaseActivity implements View.OnClickListener,
        BalanceQueryAdapter.BalanceQueryListener, OnInputDoneCallBack, PopKeyBody.ContentListener {

    private static final String TAG = "BanksBalanceQuery";
    private BalanceQueryAdapter mAdapter;
    private TextView mQueryTv;
    private String     mAccount;
    private String      mSession;
    private String      mBankNo;
    private PasswordKeyboard mKeyboard;
    private Dialog mLoadingDialog;
    private ImageView mEmpty;
    private PopKeyBody mPopKeyBody;
    FrameLayout mVieGroup;
    private boolean isKeyFlag;//是否使用插件键盘的状态
    private String mKeyboardResult;

    @Override
    public void initView() {
        mLoadingDialog = LoadingDialog.initDialog(this, "正在查询...");
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        mVieGroup = (FrameLayout) findViewById(R.id.banks_viewGroup);
        mEmpty = (ImageView) findViewById(R.id.iv_allotQueryTab);
        findViewById(R.id.balance_back).setOnClickListener(this);
        ListView listview = (ListView) findViewById(R.id.balance_listview);
        mAdapter = new BalanceQueryAdapter(this, false);
        listview.setAdapter(mAdapter);

        Object object = UsefulKeyBoard.getInstance().initUserKeyBoard(true, this, true, null);
        if (object != null && object instanceof PopKeyBody) {
            mPopKeyBody = (PopKeyBody) object;
        } else {
            isKeyFlag = true;



        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        getBanksList();
    }

    private void getBanksList() {
        mEmpty.setVisibility(View.GONE);

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
                mEmpty.setVisibility(View.VISIBLE);

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
                    Intent intent = new Intent();
                    intent.setClass(BanksBalanceQueryActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                }

                if (!bean.getCode().equals("0")) {
                    mEmpty.setVisibility(View.VISIBLE);
                    MistakeDialog.showDialog(bean.getMsg(), BanksBalanceQueryActivity.this, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            finish();
                        }
                    });
                    return;
                }

                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    ArrayList<BankAccountEntity> beans = new ArrayList<BankAccountEntity>();
                    for (BankAccountEntity.AccountInfo accountInfo : bean.getData()) {
                        BankAccountEntity _bean = new BankAccountEntity();
                        _bean.setBANK_NO(accountInfo.getBANK_NO());
                        _bean.setBALANCE(accountInfo.getBALANCE());
                        _bean.setBANK_ACCOUNT(accountInfo.getBANK_ACCOUNT());
                        _bean.setFUND_ACCOUNT(accountInfo.getFUND_ACCOUNT());
                        _bean.setMAIN_FLAG(accountInfo.getMAIN_FLAG());
                        _bean.setBANK_NAME(accountInfo.getBANK_NAME());
                        _bean.setQueryType(accountInfo.getBANK_FUNDS_QUERY());
                        beans.add(_bean);
                    }

                    mAdapter.setDatas(beans);
                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ConstantUtil.SEEEIOIN_FAILED && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onClick(View view, TextView textView, String bankNo, String account, String queryType) {
        mBankNo = bankNo;
        mQueryTv = textView;
        mAccount  = account;

        if (!TextUtils.isEmpty(queryType)) {
            //0不校验1校验银行密码2不支持银行查询
            if ("0".equals(queryType)) {

                if (mLoadingDialog == null) {
                    mLoadingDialog = LoadingDialog.initDialog(BanksBalanceQueryActivity.this, "正在查询...");
                } else {
                    mLoadingDialog.show();
                }

                queryBalance("", queryType);
                return;
            } else if ("2".equals(queryType)) {
                return;
            }
        }


        if (isKeyFlag) {
            try {
                mKeyboard = UniKey.getInstance().getPasswordKeyboard(this, this, 6, ConstantUtil.USERFUL_KEYBOARD, "custom_keyboard_view2");
                mKeyboard.show();

            } catch (UnikeyException e) {
                e.printStackTrace();
                Helper.getInstance().showToast(CustomApplication.getContext(), "弹出密码键盘失败：" + Integer.toHexString(e.getNumber()));
            }
        } else {
            mPopKeyBody.clearContent();
            mPopKeyBody.show(mVieGroup);
        }

    }


    @Override
    public void getContent(String num) {
        mKeyboardResult = num;
    }

    @Override
    public void doPositive() {
        mLoadingDialog.show();
        if (!TextUtils.isEmpty(mKeyboardResult) && mKeyboardResult.length() == 6) {
            mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
            queryBalance(mKeyboardResult, "0");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.balance_back:
                finish();
                break;
        }
    }

    /**
     * 查询余额
     * @param queryType 0不校验1校验银行密码2不支持银行查询
     */
    private void queryBalance(String s, String queryType) {

        if (TextUtils.isEmpty(mBankNo)) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "银行代码为空");
            return;
        }

        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("CURRENCY","0");
        map2.put("BANK_NO",mBankNo);
        map2.put("ACCOUNT", mAccount);
        map2.put("BANK_PASSWORD", s);

        if ("false".equals(Db_PUB_USERS.queryingCertification()) || "0".equals(queryType)) {
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

                MistakeDialog.showDialog("" + e.toString(), BanksBalanceQueryActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        finish();
                    }
                });
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

                    Intent intent = new Intent();
                    intent.setClass(BanksBalanceQueryActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);

                    return;
                }

                if (!bean.getCode().equals("0")) {
                    MistakeDialog.showDialog(bean.getMsg(), BanksBalanceQueryActivity.this, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            finish();
                        }
                    });
                    return;
                }

                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    List<BankAccountEntity.AccountInfo> accountInfos = bean.getData();
                    for (BankAccountEntity.AccountInfo accountInfo : accountInfos) {

                        mQueryTv.setBackgroundResource(0);
                        mQueryTv.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                        mQueryTv.setText("余额:" + accountInfo.getLAST_BALANCE());
                        mQueryTv.setClickable(false);
                        mQueryTv.setFocusable(false);
                    }


                }

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.banks_balance_query ;
    }

    @Override
    public void getInputEncrypted(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }

        mLoadingDialog.show();
        if (s.length() == 6) {
            mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
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
}
