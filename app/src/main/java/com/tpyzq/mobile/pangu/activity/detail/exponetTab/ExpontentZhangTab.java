package com.tpyzq.mobile.pangu.activity.detail.exponetTab;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.LazyBaseFragment;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.http.doConnect.detail.ExponentConnect;
import com.tpyzq.mobile.pangu.http.doConnect.detail.ToExponentConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2017/8/10.
 * 成份股涨幅
 */

public class ExpontentZhangTab extends LazyBaseFragment implements DialogInterface.OnCancelListener, ICallbackResult, ExponentConnect.ExpontentConnectInterface {

    private final String TAG = ExpontentZhangTab.class.getSimpleName();
    private ImageView mKongIv;
    private Dialog mProgressDialog;
    private boolean clickBackKey;//判断用户是否点击返回键取消网络请求

    public final String ZF_FLAG = "1";
    public final String DF_FLAG = "1";
    public final String HS_FLAG = "2";

    public final String ZF_ASC = "1";
    public final String DF_ASC = "2";
    public final String HS_ASC = "1";

    public final String ZF_TYPE = "1000";
    public final String DF_TYPE = "2000";
    public final String HS_TYPE = "3000";

    private String _exponentCode;

    private ExpontentAdapter mAdapter;
    private ArrayList<StockInfoEntity> mBeans;

    private SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);

    @Override
    public void initView(View view) {

        Bundle bundle = getArguments();
        _exponentCode = bundle.getString("exponentCode");

        mBeans = new ArrayList<>();
        mKongIv = (ImageView) view.findViewById(R.id.iv_Kong);
        mKongIv.setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lvExpontent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ExpontentAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        //用来标记是否正在向最后一个滑动
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        simpleRemoteControl.setCommand(new ToExponentConnect(new ExponentConnect("" + totalItemCount, "30", TAG, getFlag(), getAsc(), _exponentCode, getType(), ExpontentZhangTab.this)));
                        simpleRemoteControl.startConnect();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
    }

    @Override
    protected void onFragmentFirstVisible() {
        initDialog();
        simpleRemoteControl.setCommand(new ToExponentConnect(new ExponentConnect("0", "30", TAG, getFlag(), getAsc(), _exponentCode, getType(), this)));
        simpleRemoteControl.startConnect();
    }

    public String getFlag() {
        return ZF_FLAG;
    }

    public String getAsc() {
        return ZF_ASC;
    }

    public String getType() {
        return ZF_TYPE;
    }

    @Override
    public void getResult(Object result, String tag) {
        cloasLoading();

        ArrayList<StockInfoEntity> entities = (ArrayList<StockInfoEntity>) result;
        if (entities != null && entities.size() > 0) {
            mBeans.addAll(entities);
            mAdapter.setDatas(mBeans);
        }
    }

    @Override
    public void showError(String error) {
        CentreToast.showText(getActivity(), error, Toast.LENGTH_SHORT);
    }

    @Override
    public void showEmpty() {
        mKongIv.setVisibility(View.VISIBLE);
    }

    private void initDialog() {
        mProgressDialog = LoadingDialog.initDialog(getActivity(), "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void cloasLoading() {
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
    public int getFragmentLayoutId() {
        return R.layout.tab_expontent;
    }
}
