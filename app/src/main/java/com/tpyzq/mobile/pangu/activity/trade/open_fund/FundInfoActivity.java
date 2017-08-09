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
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import java.util.ArrayList;

/**
 * 基金信息
 */
public class FundInfoActivity extends BaseActivity implements View.OnClickListener,
        DialogInterface.OnCancelListener, PullToRefreshBase.OnRefreshListener2,
        FundInoConnect.FundInfoConnectListener, AdapterView.OnItemClickListener, FundInfoAdapter.DetailClickListener {
    public static final String TAG = FundInfoActivity.class.getSimpleName();
    private Dialog                  mProgressDialog;
    private EditText                mSearchContent_et;
    private PullToRefreshListView   mListView;
    private FundInfoAdapter         mAdapter;
    private ArrayList<FundSubsEntity>    mBeans;
    private int position = 0;

    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    private boolean isShowDetail = false;
    private boolean isClickItem = false;

    private FundInoConnect mFundInoConnect = new FundInoConnect();
    public static final String IS_SHOW = "isShow";//是否显示详情
    public static final String ITEM_CLICK = "itemClick";//item是否可以点击
    public static final String LIST_TYPE = "listType";//基金列表类型
    private String mListType = "";

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.bt_search).setOnClickListener(this);
        ImageView emptyIv = (ImageView) findViewById(R.id.emptyIv);

        Intent intent = getIntent();
        isShowDetail = intent.getBooleanExtra(IS_SHOW, false);
        isClickItem = intent.getBooleanExtra(ITEM_CLICK, false);
        mListType = intent.getStringExtra(LIST_TYPE);

        mSearchContent_et = (EditText) findViewById(R.id.et_search_fundcompany);
        mListView = (PullToRefreshListView) findViewById(R.id.lv_fund);
        mAdapter = new FundInfoAdapter(this, isShowDetail);
        mAdapter.setDetailClickListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);
        mListView.setEmptyView(emptyIv);
        mListView.setOnItemClickListener(this);

        mBeans = new ArrayList<FundSubsEntity>();


        initLoadDialog();
        mFundInoConnect.fundQueryConnect(mListType, "", 0, TAG, this);
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
                mFundInoConnect.fundQueryConnect(mListType, mSearchContent_et.getText().toString(), 0, TAG, this);
                break;
        }
    }

    @Override
    public void detailClick(FundSubsEntity entity) {
        Intent intent = new Intent();
        if ("0".equals(entity.FUND_TYPE)) {//基金 （非资管）
            intent.putExtra("TYPE", "1");
            //基金分 货币基金和非货币基金
            intent.putExtra("prod_type", entity.FUND_TYPE_CODE);
        } else {//14天（资管）
            intent.putExtra("TYPE", "2");
        }
        intent.putExtra("target", "fundInfoTarget");
        intent.putExtra("productCode", entity.FUND_CODE);
        intent.putExtra("prod_qgje", entity.OPEN_SHARE);
        intent.putExtra("ofund_risklevel_name", entity.OFUND_RISKLEVEL_NAME);
        intent.setClass(FundInfoActivity.this, ManagerMoenyDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - 1;
        if (isClickItem) {
            Intent intent = new Intent();
            intent.putExtra("point", position);
            if (mBeans != null && mBeans.size() > 0) {
                intent.putExtra("FUND_CODE", mBeans.get(position).FUND_CODE);
                intent.putExtra("FUND_COMPANY", mBeans.get(position).FUND_COMPANY);
                intent.putExtra("data", mBeans.get(position));
            }
            setResult(RESULT_OK, intent);
            finish();
        }

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
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (position > 0) {
            position--;
            mFundInoConnect.fundQueryConnect(mListType, mSearchContent_et.getText().toString(), position, TAG, this);
        } else {
            mFundInoConnect.fundQueryConnect(mListType, mSearchContent_et.getText().toString(), 0, TAG, this);
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        position++;
        mFundInoConnect.fundQueryConnect(mListType, mSearchContent_et.getText().toString(), position, TAG, this);
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
        CentreToast.showText(CustomApplication.getContext(),errorMsg);
//        MistakeDialog.showDialog(errorMsg, FundInfoActivity.this, listener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_info;
    }
}
