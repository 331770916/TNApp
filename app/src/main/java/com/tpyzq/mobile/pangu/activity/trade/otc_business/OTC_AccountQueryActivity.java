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
import com.tpyzq.mobile.pangu.adapter.trade.OTC_AccountQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.trade.OTC_AccountConnect;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.itemdecoration.LinearDividerItemDecoration;
import com.tpyzq.mobile.pangu.view.pulllayou.SimplePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.base.BasePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.head.TainiuRefreshHead;
import java.util.ArrayList;
import java.util.Map;

/**
 * OTC 账户查询 页面
 */
public class OTC_AccountQueryActivity extends BaseActivity implements View.OnClickListener,
        BasePullLayout.OnPullCallBackListener, DialogInterface.OnCancelListener,
        OTC_AccountConnect.OTC_AccountConnectListener {

    private static final String TAG = OTC_AccountQueryActivity.class.getSimpleName();
    private OTC_AccountQueryAdapter mAdapter;
    private ImageView               ivKong;
    private Dialog                  mProgressDialog;
    private SimplePullLayout        mSimplePullLayout;
    private String                  mSession;
    private OTC_AccountConnect mOTC_AccountConnect = new OTC_AccountConnect();
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求

    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("OTC账户");

        ivKong = (ImageView) this.findViewById(R.id.iv_Kong);
        ivKong.setVisibility(View.GONE);
        mSimplePullLayout = (SimplePullLayout) findViewById(R.id.id_swipe_ly);
        mSimplePullLayout.attachHeadView(new TainiuRefreshHead());
        mSimplePullLayout.setPullUpEnable(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lvOTC_AccountQuery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LinearDividerItemDecoration(this, LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL));
        mAdapter = new OTC_AccountQueryAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mSimplePullLayout.setOnPullListener(this);
        mSession = SpUtils.getString(this, "mSession", "");

        initLoadDialog();
        mOTC_AccountConnect.toOtcAccountConnect(TAG, mSession, this);
    }

    @Override
    public void onRefresh() {
        mOTC_AccountConnect.toOtcAccountConnect(TAG, mSession, this);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void connectError(String error) {
        CentreToast.showText(OTC_AccountQueryActivity.this,error);
    }

    @Override
    public void closeRefresh() {
        mSimplePullLayout.finishPull();
    }

    @Override
    public void sessionFaild() {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(OTC_AccountQueryActivity.this, ShouJiZhuCeActivity.class);
        } else if (!Db_PUB_USERS.islogin()) {
            intent.setClass(OTC_AccountQueryActivity.this, TransactionLoginActivity.class);
        } else {
            intent.setClass(OTC_AccountQueryActivity.this, TransactionLoginActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void connectNoData() {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        ivKong.setVisibility(View.VISIBLE);
        mAdapter.setDatas(list);
    }

    @Override
    public void connectSuccess(ArrayList<Map<String, String>> datas) {
        ivKong.setVisibility(View.GONE);
        mAdapter.setDatas(datas);
    }

    @Override
    public void cloasLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(OTC_AccountQueryActivity.this, "正在加载...");
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
        return R.layout.activity_otc__account_query;
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
