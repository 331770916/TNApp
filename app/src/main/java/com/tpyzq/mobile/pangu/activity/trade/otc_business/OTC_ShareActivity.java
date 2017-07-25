package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_ShareAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OtcShareEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.trade.OTC_ShareConnect;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pulllayou.SimplePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.base.BasePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.head.TainiuRefreshHead;

import java.util.ArrayList;

/**
 * 刘泽鹏
 * OTC 份额查询界面
 */
public class OTC_ShareActivity extends BaseActivity implements View.OnClickListener,
        OTC_ShareConnect.OTC_ShareConnectInterface, BasePullLayout.OnPullCallBackListener,
        DialogInterface.OnCancelListener, OTC_ShareAdapter.OtcShareClick {

    private final String TAG = OTC_ShareActivity.class.getSimpleName();
    private Dialog                  mProgressDialog;
    private SimplePullLayout        mSimplePullLayout;
    private OTC_ShareAdapter        mAdapter;
    private String                  mSession;
    private ImageView               mKong_iv;
    private OTC_ShareConnect mConnect = new OTC_ShareConnect();
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求

    @Override
    public void initView() {

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("OTC份额");


        mSimplePullLayout = (SimplePullLayout) findViewById(R.id.id_swipe_ly);
        mSimplePullLayout.attachHeadView(new TainiuRefreshHead());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lvShareQuery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CustomDecoration(this));
        mAdapter = new OTC_ShareAdapter(OTC_ShareActivity.this);
        mKong_iv = (ImageView) this.findViewById(R.id.iv_ShareKong);
        mKong_iv.setVisibility(View.GONE);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setClick(this);

        mSimplePullLayout.setOnPullListener(this);
        mSession = SpUtils.getString(this, "mSession", "");
        initLoadDialog();
        mConnect.toOtcShareConnect(TAG, mSession, this);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    @Override
    public void onClick(int position) {
//        Intent intent = new Intent();
//        intent.setClass(OTC_ShareActivity.this, OTC_RedeemActivity.class);
//        intent.putExtra("prod_code",prod_code);
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        mConnect.toOtcShareConnect(TAG, mSession, this);
    }

    @Override
    public void onLoad() {
        //不处理
    }

    @Override
    public void closeRefresh() {
        mSimplePullLayout.finishPull();
    }

    @Override
    public void connectError(String error) {
        MistakeDialog.showDialog(error, OTC_ShareActivity.this);
    }

    @Override
    public void connectNoData() {
        ArrayList<OtcShareEntity> list = new ArrayList<OtcShareEntity>();
        mKong_iv.setVisibility(View.VISIBLE);
        mAdapter.setDatas(list);
    }

    @Override
    public void connectSuccess(ArrayList<OtcShareEntity> datas) {
        mKong_iv.setVisibility(View.GONE);
        mAdapter.setDatas(datas);
    }

    @Override
    public void sessionFaild() {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(OTC_ShareActivity.this, ShouJiZhuCeActivity.class);
        } else if (!Db_PUB_USERS.islogin()) {
            intent.setClass(OTC_ShareActivity.this, TransactionLoginActivity.class);
        } else {
            intent.setClass(OTC_ShareActivity.this, TransactionLoginActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void cloasLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(OTC_ShareActivity.this, "正在加载...");
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
        return R.layout.activity_otc__share;
    }

    private class CustomDecoration extends RecyclerView.ItemDecoration{
        private int dividerHeight;


        public CustomDecoration(Context context) {
            dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.size5);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = dividerHeight;
        }
    }

}
