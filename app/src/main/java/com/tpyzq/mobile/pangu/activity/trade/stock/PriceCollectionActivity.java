package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.PriceCollectionAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/26.
 * 资金归集
 */
public class PriceCollectionActivity extends BaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, PriceCollectionAdapter.UpdateCollectionData,
        PriceCollectionAdapter.AfterTextChangedListener{

    private PriceCollectionAdapter mAdapter;
    private ArrayList<BankAccountEntity> mFuBeans;
    private TextView mAccountNo;
    private TextView mAccountBank;
    private TextView mAccountPrice;
    private String   mSession;
    private String   mOptionType = "";
    private BankAccountEntity mMainEntitiy;
    private CheckBox mCheckBoxforAll;
    private ImageView mEmpty;
    private TextView mPositonBtn;

    private  static final String TAG = "PriceCollection";

    @Override
    public void initView() {
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        mEmpty = (ImageView) findViewById(R.id.iv_collection);
        findViewById(R.id.collection_back).setOnClickListener(this);
        mPositonBtn = (TextView) findViewById(R.id.collectionToMainBtn);
        mPositonBtn.setOnClickListener(this);

        mAccountNo = (TextView) findViewById(R.id.collection_AccountNo);
        mAccountBank = (TextView) findViewById(R.id.collection_AccountBank);
        mAccountPrice = (TextView) findViewById(R.id.collection_AccountPrice);
        mCheckBoxforAll = (CheckBox) findViewById(R.id.editSelfAllcheckBox);
        mCheckBoxforAll.setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.collection_listView);
        mAdapter = new PriceCollectionAdapter();
        listView.setAdapter(mAdapter);
        mAdapter.setDatasListener(this);
        mAdapter.setAfterTextChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        getBanksList();
    }

    /**
     * 得到银行卡列表
     */
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

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mEmpty.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }

//                response = "{\"code\":\"0\",\"msg\":\"(资金账号列表查询成功)\",\"data\":[{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256140\",\"FUND_ACCOUNT\":\"101000910\",\"MAIN_FLAG\":\"1\",\"BANK_NAME\":\"建行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256141\",\"FUND_ACCOUNT\":\"101000911\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"农行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256142\",\"FUND_ACCOUNT\":\"101000912\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"工行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256143\",\"FUND_ACCOUNT\":\"101000913\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"招商行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256143\",\"FUND_ACCOUNT\":\"101000913\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"招商行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256143\",\"FUND_ACCOUNT\":\"101000913\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"招商行存管\"},{\"BANK_NO\":\"3\",\"BALANCE\":\"378547.93\",\"BANK_ACCOUNT\":\"8888034001256143\",\"FUND_ACCOUNT\":\"101000913\",\"MAIN_FLAG\":\"0\",\"BANK_NAME\":\"招商行存管\"}]}";

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {
                    Intent intent = new Intent();
                    intent.setClass(PriceCollectionActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);

                    return;
                }

                if (!bean.getCode().equals("0")) {
                    MistakeDialog.showDialog(bean.getMsg(), PriceCollectionActivity.this, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            finish();
                        }
                    });
                    mEmpty.setVisibility(View.GONE);
                    return;
                }


                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    mFuBeans = new ArrayList<BankAccountEntity>();
                    mMainEntitiy = new BankAccountEntity();
                    for (BankAccountEntity.AccountInfo accountInfo : bean.getData()) {
                        BankAccountEntity _bean = new BankAccountEntity();
                        _bean.setBANK_NO(accountInfo.getBANK_NO());
                        _bean.setBALANCE(accountInfo.getBALANCE());
                        _bean.setENABLE_BALANCE(accountInfo.getENABLE_BALANCE());
                        _bean.setFETCH_BALANCE(accountInfo.getFETCH_BALANCE());
                        _bean.setBANK_ACCOUNT(accountInfo.getBANK_ACCOUNT());
                        _bean.setFUND_ACCOUNT(accountInfo.getFUND_ACCOUNT());
                        _bean.setMAIN_FLAG(accountInfo.getMAIN_FLAG());
                        _bean.setBANK_NAME(accountInfo.getBANK_NAME());

                        if (!"1".equals(accountInfo.getMAIN_FLAG())) {
                            mFuBeans.add(_bean);
                        } else {
                            mMainEntitiy = _bean;
                        }
                    }

                    if (mFuBeans == null || mFuBeans.size() <= 0) {
                        mEmpty.setVisibility(View.GONE);
                    }

                    mAdapter.setData(mFuBeans);

                    if (!TextUtils.isEmpty(mMainEntitiy.getFUND_ACCOUNT())) {
                        mAccountNo.setText("" + mMainEntitiy.getFUND_ACCOUNT());
                    }

                    String content = "";
                    if (!TextUtils.isEmpty(mMainEntitiy.getBANK_NAME())) {
                        content = mMainEntitiy.getBANK_NAME();
                    }

                    if (!TextUtils.isEmpty(mMainEntitiy.getBANK_ACCOUNT())) {
                        content = content + "(" + Helper.getBanksAccountNumber(mMainEntitiy.getBANK_ACCOUNT()) + ")";
                    }

                    mAccountBank.setText(content);

                    mAccountPrice.setText("可用资金(元)" + mMainEntitiy.getFETCH_BALANCE());
                } else {
                    mEmpty.setVisibility(View.GONE);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


    }

    @Override
    public void updateDatas(ArrayList<BankAccountEntity> datas) {

        ArrayList<Boolean> flags = new ArrayList<>();

        for (BankAccountEntity bean : datas) {
            flags.add(bean.getCollected());
        }

        if (flags.contains(false)) {
            mCheckBoxforAll.setChecked(false);
            mOptionType = "2";
        } else {
            mCheckBoxforAll.setChecked(true);
            mOptionType = "2";
        }
    }

    @Override
    public void afterTextChanged(String text, int position) {
        mFuBeans.get(position).setOccurBalance(text);


        if (!TextUtils.isEmpty(text)) {
            mPositonBtn.setClickable(true);
            mPositonBtn.setFocusable(true);
            mPositonBtn.setBackgroundColor(getResources().getColor(R.color.calendarBtnColor));
        } else {
            mPositonBtn.setClickable(false);
            mPositonBtn.setFocusable(false);
            mPositonBtn.setBackgroundColor(getResources().getColor(R.color.hideTextColor));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection_back:
                finish();
                break;
            case R.id.editSelfAllcheckBox:

                for (BankAccountEntity bean : mFuBeans) {
                    bean.setCollected(mCheckBoxforAll.isChecked());
                }

                if (mCheckBoxforAll.isChecked()) {
                    mOptionType = "2";
                } else {
                    mOptionType = "2";
                }

                mAdapter.setData(mFuBeans);

                break;
            case R.id.collectionToMainBtn:

                StringBuilder sb = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                for (BankAccountEntity bean : mFuBeans) {
                    if (bean.getCollected()) {
                        sb.append(bean.getFUND_ACCOUNT()).append(",");
                        sb1.append(bean.getOccurBalance()).append(",");
                        sb2.append(bean.getBALANCE()).append(",");
                    }
                }

                String fuAccounts = sb.toString();
                String occurBalance = sb1.toString();
                String balance = sb2.toString();

                if (fuAccounts.length() > 1) {
                    fuAccounts = fuAccounts.substring(0, fuAccounts.length() - 1);
                }

                if (occurBalance.length() > 1) {
                    occurBalance = occurBalance.substring(0, occurBalance.length() - 1);
                }

                if (balance.length() > 1) {
                    balance = balance.substring(0, balance.length() - 1);
                }



                if (juedge(fuAccounts, occurBalance, balance)) {
                    mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

                    fuAccounts = fuAccounts.replaceAll(",", "\\|");
                    occurBalance = occurBalance.replaceAll(",", "\\|");
                    collectPriceConnect(mOptionType, fuAccounts, mMainEntitiy.getFUND_ACCOUNT(), occurBalance);
                }


                break;
        }
    }

    private boolean juedge(String accounts, String occurBalances, String balances) {

        if (TextUtils.isEmpty(accounts)) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "请选择辅助资金账号");
            return false;
        }

        if (TextUtils.isEmpty(occurBalances)) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "请输入转账金额");
            return false;
        }

        if (!TextUtils.isEmpty(balances) && !TextUtils.isEmpty(occurBalances)) {

            if (balances.contains(",") && occurBalances.contains(",")) {
                String[] obs = occurBalances.split(",");
                String [] bs = balances.split(",");

                for (int i = 0; i < obs.length; i++) {
                    if (Helper.isDecimal(obs[i]) && Helper.isDecimal(bs[i])
                            && Double.parseDouble(obs[i]) > Double.parseDouble(bs[i])) {

                        Helper.getInstance().showToast(CustomApplication.getContext(), "输入金额超出可用余额");

                        return false;
                    }
                }

            } else {
                if (Helper.isDecimal(occurBalances) && Helper.isDecimal(balances)
                        && Double.parseDouble(occurBalances) > Double.parseDouble(balances)) {
                    Helper.getInstance().showToast(CustomApplication.getContext(), "输入金额超出可用余额");
                    return false;
                }
            }
        }

        return true;
    }

    private void collectPriceConnect(String operType, String fundAccountSrc, String fundAccountDest, String occurBalance) {
        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("MONEY_TYPE","0");
        map2.put("OPER_TYPE",operType);
        map2.put("FUND_ACCOUNT_SRC",fundAccountSrc);
        map2.put("FUND_ACCOUNT_DEST", fundAccountDest);
        map2.put("OCCUR_BALANCE", occurBalance);
        map1.put("funcid","300392");
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

//                response = "{\"code\":\"0\",\"msg\":\"(资金一键调拨成功)\",\"data\":[{\"CURRENT_BALANCE_DEST\":\"109.81\",\"ENABLE_BALANCE_DEST\":\"7.82\"}]}";

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {

                    Intent intent = new Intent();
                    intent.setClass(PriceCollectionActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);

                    return;
                }

                if (!bean.getCode().equals("0")) {
                    MistakeDialog.showDialog(bean.getMsg(), PriceCollectionActivity.this, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            finish();
                        }
                    });
                    return;
                }

                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    getBanksList();
                    ResultDialog.getInstance().show("" + bean.getMsg(), R.mipmap.lc_success);
                }

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_price_collection;
    }
}
