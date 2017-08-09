package com.tpyzq.mobile.pangu.activity.trade.view;

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
import com.tpyzq.mobile.pangu.activity.trade.LazyBaseFragment;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_EntrustTodayAdapter;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.trade.OTC_BargainConnect;
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
 * 作者：刘泽鹏 on 2016/9/7 13:25
 */
public class OTC_BargainToday extends LazyBaseFragment implements BasePullLayout.OnPullCallBackListener,
        DialogInterface.OnCancelListener, OTC_BargainConnect.OTC_BargainConnectListener {

    private final String TAG = OTC_BargainToday.class.getSimpleName();
    private Dialog                    mProgressDialog;
    private SimplePullLayout          mSimplePullLayout;
    private OTC_EntrustTodayAdapter   mAdapter;
    private ImageView                 mKong_iv;
    protected OTC_BargainConnect      mConnect = new OTC_BargainConnect();

    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求
    // 标志位，标志已经初始化完成。

    public static final int BARGAIN_TYPE = 0;
    public static final int ENTRUST_TYPE = 1;
    public static int type = BARGAIN_TYPE;

    @Override
    public void initView(View view) {

        if (type == ENTRUST_TYPE) {
            TextView titl2 = (TextView) view.findViewById(R.id.otc_bargainTitle2);
            TextView titl3 = (TextView) view.findViewById(R.id.otc_bargainTitle3);
            TextView titl4 = (TextView) view.findViewById(R.id.otc_bargainTitle4);

            titl2.setText("委托时间");
            titl3.setText("金额/份额");
            titl4.setText("类型/状态");
        }

        showBargainCustom(view);
        mSimplePullLayout = (SimplePullLayout) view.findViewById(R.id.id_swipe_ly);
        mSimplePullLayout.attachHeadView(new TainiuRefreshHead());
        mSimplePullLayout.setPullUpEnable(false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lvShareQuery);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new LinearDividerItemDecoration(getActivity(), LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL));
        mAdapter = new OTC_EntrustTodayAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        mKong_iv = (ImageView) view.findViewById(R.id.iv_Kong);        //空 图片
        mKong_iv.setVisibility(View.GONE);

        mSimplePullLayout.setOnPullListener(this);
    }

    /**
     * 自定义查询时会用到，在此不调用
     */
    protected void showBargainCustom(View view) {

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
//        if (isVisible) {
//        } else {
//        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        onVisible();
    }

    protected void onVisible(){
        initLoadDialog();
        toConnect();
    }

    protected void toConnect() {
        if (type == BARGAIN_TYPE) {
            mConnect.toToadyConnect(TAG, this);
        } else if (type == ENTRUST_TYPE) {
            mConnect.toEnturstToadyConnect(TAG, this);
        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (clickBackKey) {
            OkHttpUtil.cancelSingleRequestByTag(TAG);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                clickBackKey = true;
                mProgressDialog.cancel();
                return false;
            } else {
                return true;
            }
        }else {
            return true;
        }
    }

    @Override
    public void onRefresh() {
        toConnect();
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void connectError(String error) {
        CentreToast.showText(getActivity(),error);
    }

    @Override
    public void closeRefresh() {
        mSimplePullLayout.finishPull();
    }

    @Override
    public void sessionFaild() {
        Intent intent = new Intent();
        if (!Db_PUB_USERS.isRegister()) {
            intent = new Intent(getActivity(), ShouJiZhuCeActivity.class);
        } else if (!Db_PUB_USERS.islogin()) {
            intent.setClass(getActivity(), TransactionLoginActivity.class);
        } else {
            intent.setClass(getActivity(), TransactionLoginActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void connectNoData() {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        mKong_iv.setVisibility(View.VISIBLE);
        mAdapter.setDatas(list);
    }

    @Override
    public void connectSuccess(ArrayList<Map<String, String>> datas) {
        mKong_iv.setVisibility(View.GONE);
        mAdapter.setDatas(datas);
    }

    @Override
    public void cloasLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(getActivity(), "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.otc_bargain_query;
    }

}
