package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_ElectronicContractAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.trade.OTC_ElectronicConnect;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.pulllayou.SimplePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.base.BasePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.head.TainiuRefreshHead;

import java.util.ArrayList;

/**
 * OTC 电子合同界面
 * 刘泽鹏
 */
public class OTC_ElectronicContractActivity extends BaseActivity implements View.OnClickListener,
        BasePullLayout.OnPullCallBackListener, DialogInterface.OnCancelListener,
        OTC_ElectronicConnect.OTC_ElectronicConnectListener, OTC_ElectronicContractAdapter.OnItemClickListener {

    private static final String TAG = OTC_ElectronicContractActivity.class.getSimpleName();
    private OTC_ElectronicContractAdapter mAdapter;
    private Dialog              mProgressDialog;
    private SimplePullLayout    mSimplePullLayout;
    private OTC_ElectronicConnect mConnect = new OTC_ElectronicConnect();
    private ImageView           ivKong;
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求

    @Override
    public void initView() {

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("电子合同查询");

        ivKong = (ImageView) this.findViewById(R.id.iv_Kong);
        ivKong.setVisibility(View.GONE);
        mSimplePullLayout = (SimplePullLayout) findViewById(R.id.id_swipe_ly);
        mSimplePullLayout.attachHeadView(new TainiuRefreshHead());
        mSimplePullLayout.setPullUpEnable(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lvElectronicContract);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OTC_ElectronicContractAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mSimplePullLayout.setOnPullListener(this);

        initLoadDialog();
        mConnect.toElectronicConnect(TAG, this);
    }


    @Override
    public void onItemClick(OTC_ElectronicContractEntity electronicContractEntity) {
        ArrayList<OTC_ElectronicContractEntity> listBean = new ArrayList<OTC_ElectronicContractEntity>();
        listBean.add(electronicContractEntity);
        String is_sign = electronicContractEntity.getIs_sign();
        Intent intent = new Intent();
        if(is_sign.equals("1")){
            intent.setClass(OTC_ElectronicContractActivity.this,OTC_ContractSignActivity.class);
            intent.putExtra("list",listBean);
            startActivity(intent);
        }else if(is_sign.equals("0")){
            intent.setClass(OTC_ElectronicContractActivity.this,OTC_ContractFlowWaterActivity.class);
            intent.putExtra("list",listBean);
            startActivity(intent);
        }
    }

    @Override
    public void connectError(String error) {
        CentreToast.showText(OTC_ElectronicContractActivity.this,error);
    }

    @Override
    public void closeRefresh() {
        mSimplePullLayout.finishPull();
    }

    @Override
    public void sessionFaild() {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(OTC_ElectronicContractActivity.this, ShouJiZhuCeActivity.class);
        } else if (!Db_PUB_USERS.islogin()) {
            intent.setClass(OTC_ElectronicContractActivity.this, TransactionLoginActivity.class);
        } else {
            intent.setClass(OTC_ElectronicContractActivity.this, TransactionLoginActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void connectNoData() {
        ArrayList<OTC_ElectronicContractEntity> datas = new ArrayList<>();
        ivKong.setVisibility(View.VISIBLE);
        mAdapter.setList(datas);
    }

    @Override
    public void connectSuccess(ArrayList<OTC_ElectronicContractEntity> datas) {
        ivKong.setVisibility(View.GONE);
        mAdapter.setList(datas);
    }

    @Override
    public void cloasLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        mConnect.toElectronicConnect(TAG, this);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(OTC_ElectronicContractActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
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
    public int getLayoutId() {
        return R.layout.activity_otc__electronic_contract;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }
}
