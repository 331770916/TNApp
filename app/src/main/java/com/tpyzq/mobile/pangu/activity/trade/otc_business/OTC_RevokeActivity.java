package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_RevokePopupWindow;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_RevokeAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.trade.OTC_RevokeConnect;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pulllayou.SimplePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.base.BasePullLayout;
import com.tpyzq.mobile.pangu.view.pulllayou.head.TainiuRefreshHead;

import java.util.ArrayList;
import java.util.Map;

/**
 * 、
 * OTC 撤单界面
 * 刘泽鹏
 */
public class OTC_RevokeActivity extends BaseActivity implements View.OnClickListener,
        OTC_RevokePopupWindow.PositionListener, BasePullLayout.OnPullCallBackListener,
        DialogInterface.OnCancelListener, OTC_RevokeConnect.OTC_RevokeConnectListener, OTC_RevokeAdapter.OnItemClickListener {

    private final String TAG = OTC_RevokeActivity.class.getSimpleName();

    private OTC_RevokeAdapter mAdapter;
    private String            mSession;
    private ImageView         mKong_iv;
    private Dialog            mProgressDialog;
    private SimplePullLayout  mSimplePullLayout = null;
    private OTC_RevokeConnect mOTC_RevokeConnect = new OTC_RevokeConnect();

    private ArrayList<Map<String, String>> mDatas;
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    public static FragmentManager oTC_RevokeFragmentMananger;

    @Override
    public void initView() {
        oTC_RevokeFragmentMananger = getFragmentManager();
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("OTC撤单");

        mSimplePullLayout = (SimplePullLayout) findViewById(R.id.id_swipe_ly);
        mSimplePullLayout.attachHeadView(new TainiuRefreshHead());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lvRevoke);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OTC_RevokeAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mDatas = new ArrayList<>();
        mKong_iv = (ImageView) this.findViewById(R.id.iv_RevokeKong);
        mKong_iv.setVisibility(View.GONE);

        mSimplePullLayout.setOnPullListener(this);
        mSession = SpUtils.getString(this, "mSession", "");
        initLoadDialog();

        mOTC_RevokeConnect.toOTC_RevokeConnect(mSession, TAG, this);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(int position) {

        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }

        Map<String, String> map = mDatas.get(position);

        OTC_RevokePopupWindow popupWindow = new OTC_RevokePopupWindow(OTC_RevokeActivity.this,
                OTC_RevokeActivity.this, map, position, OTC_RevokeActivity.this);

        popupWindow.setFocusable(true);     //获取焦点
        ColorDrawable dw = new ColorDrawable(0xf0000000);     //0x60000000
        popupWindow.setBackgroundDrawable(dw);      //设置背景颜色
        popupWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //消失的时候设置窗体背景变亮
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        //显示窗口
        popupWindow.showAtLocation(OTC_RevokeActivity.this.findViewById(R.id.otc_revokeParent),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__revoke;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(OTC_RevokeActivity.this, "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    @Override
    public void onRefresh() {
        mOTC_RevokeConnect.toOTC_RevokeConnect(mSession, TAG, this);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void connectError(String error) {
        CentreToast.showText( OTC_RevokeActivity.this,error);
    }

    @Override
    public void closeRefresh() {
        mSimplePullLayout.finishPull();
    }

    @Override
    public void cloasLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void sessionFaild() {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(OTC_RevokeActivity.this, ShouJiZhuCeActivity.class);
        } else if (!Db_PUB_USERS.islogin()) {
            intent.setClass(OTC_RevokeActivity.this, TransactionLoginActivity.class);
        } else {
            intent.setClass(OTC_RevokeActivity.this, TransactionLoginActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void connectNoData() {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        mKong_iv.setVisibility(View.VISIBLE);
        mAdapter.setList(list);
    }

    @Override
    public void connectSuccess(ArrayList<Map<String, String>> datas) {
        mKong_iv.setVisibility(View.GONE);
        mDatas = datas;
        mAdapter.setList(datas);
    }

    @Override
    public void callBack(int position) {
        mDatas.clear();
        mOTC_RevokeConnect.toOTC_RevokeConnect(mSession, TAG, this);
    }
}
