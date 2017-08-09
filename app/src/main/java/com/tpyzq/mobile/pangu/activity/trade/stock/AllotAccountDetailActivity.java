package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.BalanceQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/29.
 * 资金调拨详情页面
 */
public class AllotAccountDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private BalanceQueryAdapter mAdapter;
    private String      mSession;
    private ArrayList<BankAccountEntity> mBeans;
    private ImageView mEmpty;
    public static final String TAG = "AllotAccountDetail";

    @Override
    public void initView() {

        Intent intent = getIntent();
        mBeans = intent.getParcelableArrayListExtra("fuzhuBankcards");
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        mEmpty = (ImageView) findViewById(R.id.iv_allotQueryTab);
        findViewById(R.id.allot_detail_back).setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.allotAccountDetailListView);
        mAdapter = new BalanceQueryAdapter(null, true);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        if (mBeans != null && mBeans.size() > 0) {
            mAdapter.setDatas(mBeans);
        } else {
            getBanksList();
        }

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

                if (!bean.getCode().equals("0")) {
                    mEmpty.setVisibility(View.VISIBLE);
                    return;
                }


                if (bean.getData() != null &&  bean.getData().size() > 0) {
                    mBeans = new ArrayList<BankAccountEntity>();
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
                        }
                    }

                    if (mBeans == null || mBeans.size() <= 0) {
                        mEmpty.setVisibility(View.VISIBLE);
                    }

                    mAdapter.setDatas(mBeans);
                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allot_detail_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (mBeans != null && mBeans.size() > 0) {
            BankAccountEntity _bean  = mBeans.get(position);
            intent.putExtra("entitiy", _bean);
            intent.putExtra("index", position);
        }
        AllotAccountDetailActivity.this.setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_allotaccount_detail;
    }
}
