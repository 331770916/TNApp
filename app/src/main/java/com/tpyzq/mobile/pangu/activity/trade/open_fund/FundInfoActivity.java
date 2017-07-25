package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.ManagerMoenyDetailActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.FundInfoAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.trade.FundInoConnect;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import java.util.ArrayList;

/**
 * 基金信息
 */
public class FundInfoActivity extends BaseActivity implements View.OnClickListener,
        DialogInterface.OnCancelListener, PullToRefreshBase.OnRefreshListener2,
        FundInoConnect.FundInfoConnectListener, AdapterView.OnItemClickListener {
    public static final String TAG = FundInfoActivity.class.getSimpleName();
    private Dialog                  mProgressDialog;
    private EditText                mSearchContent_et;
    private PullToRefreshListView   mListView;
    private FundInfoAdapter         mAdapter;
    private ArrayList<FundSubsEntity>    mBeans;
    private String mSession;
    private int position = 0;

    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求

    private FundInoConnect mFundInoConnect = new FundInoConnect();

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.bt_search).setOnClickListener(this);
        ImageView emptyIv = (ImageView) findViewById(R.id.emptyIv);

        mSearchContent_et = (EditText) findViewById(R.id.et_search_fundcompany);
        mListView = (PullToRefreshListView) findViewById(R.id.lv_fund);
        mAdapter = new FundInfoAdapter(this, false);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);
        mListView.setEmptyView(emptyIv);
        mListView.setOnItemClickListener(this);

        mBeans = new ArrayList<FundSubsEntity>();


        initLoadDialog();
        mSession = SpUtils.getString(getApplication(), "mSession", null);
        mFundInoConnect.fundQueryConnect("", 0, mSession, TAG, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_search:
                mBeans.clear();
                mAdapter.setDatas(mBeans);
                mFundInoConnect.fundQueryConnect(mSearchContent_et.getText().toString(), 0, mSession, TAG, this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.setClass(FundInfoActivity.this, TargetInvestmentRecordActivity.class);
//        startActivity(intent);
        Intent intent = new Intent();
        intent.putExtra("productCode", mBeans.get(position).FUND_CODE);
        intent.putExtra("TYPE", "1");
        intent.setClass(FundInfoActivity.this, ManagerMoenyDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                clickBackKey = true;
                mProgressDialog.cancel();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (position > 0) {
            position--;
            mFundInoConnect.fundQueryConnect(mSearchContent_et.getText().toString(), position, mSession, TAG, this);
        } else {
            mFundInoConnect.fundQueryConnect(mSearchContent_et.getText().toString(), 0, mSession, TAG, this);
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        position++;
        mFundInoConnect.fundQueryConnect(mSearchContent_et.getText().toString(), position, mSession, TAG, this);
    }


    @Override
    public void onRefreshComplete() {
        mListView.onRefreshComplete();
    }

    @Override
    public void sessionFailed() {
        closeLoadDialog();
        startActivity(new Intent(FundInfoActivity.this, TransactionLoginActivity.class));
    }

    @Override
    public void getFundResult(ArrayList<FundSubsEntity> entities) {
        closeLoadDialog();
        mBeans = entities;
        mAdapter.setDatas(mBeans);
    }

    @Override
    public void showError(String error) {
        closeLoadDialog();
        showMistackDialog(error);
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(FundInfoActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void closeLoadDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showMistackDialog(String errorMsg) {
        Toast.makeText(CustomApplication.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
//        MistakeDialog.showDialog(errorMsg, FundInfoActivity.this, listener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_info;
    }
}
