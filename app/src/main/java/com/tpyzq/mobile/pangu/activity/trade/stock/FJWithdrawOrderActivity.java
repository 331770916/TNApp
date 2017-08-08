package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.FJWithdrawOrderAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 分级基金撤单
 */

public class FJWithdrawOrderActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, StructuredFundDialog.Expression {
    public static final String TAG = "FJWithdrawOrder";
    private PullToRefreshListView mListView;
    private List<StructuredFundEntity> mList;
    private FJWithdrawOrderAdapter mFjwithdrawOrderAdapter;
    private int mPosition;
    private Dialog mDialog;
    private StructuredFundDialog mStructuredFundDialog;

    @Override
    public void initView() {
        ImageView imageView = (ImageView) findViewById(R.id.iv_isEmpty);
        mList = new ArrayList<>();
        mListView = (PullToRefreshListView) findViewById(R.id.mListView);
        ImageView image = (ImageView) findViewById(R.id.detail_back);
        image.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mFjwithdrawOrderAdapter = new FJWithdrawOrderAdapter(this, mList);
        mListView.setAdapter(mFjwithdrawOrderAdapter);
        requestData("");
        mListView.setEmptyView(imageView);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mList.clear();
                requestData("");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //  上拉加载
                if (mList!=null &&mList.size()>0){
                    String page = mList.get(mList.size()-1).getPosition_str();
                    requestData(page);
                }else {
                    mListView.onRefreshComplete();
                }

            }
        });

    }

    /**
     * 请求数据
     */
    private void requestData(String page) {
        mDialog = LoadingDialog.initDialog(this, "加载中...");
        if (!this.isFinishing()) {
            mDialog.show();
        }
        String token = SpUtils.getString(this, "mSession", "");
        InterfaceCollection ifc = InterfaceCollection.getInstance();
        ifc.queryTodayEntrust(token, page, "30", "1", TAG, new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                mListView.onRefreshComplete();
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if ("0".equals(info.getCode())) {
                    List<StructuredFundEntity> ses = (List<StructuredFundEntity>) info.getData();
                    mList.addAll(ses);
                    mFjwithdrawOrderAdapter.notifyDataSetChanged();
                } else if ("400".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
                    CentreToast.showText(FJWithdrawOrderActivity.this, info.getMsg());
                } else if ("-6".equals(info.getCode())) {
                    Intent intent = new Intent();
                    intent.setClass(FJWithdrawOrderActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                } else {
                    CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(info.getMsg(),CustomCenterDialog.SHOWCENTER);
                    customCenterDialog.show(getFragmentManager(),FJWithdrawOrderActivity.class.toString());
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fj_withdraw_order;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position - 1;
        StructuredFundEntity entity = mList.get(position - 1);
        if (ConstantUtil.list_item_flag) {
            mStructuredFundDialog = new StructuredFundDialog(this);
            mStructuredFundDialog.setData(TAG, this, entity, null, null);
            ConstantUtil.list_item_flag = false;
            mStructuredFundDialog.show();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_back:
                finish();
                break;
        }
    }

    @Override
    public void State() {
        StructuredFundEntity entity = mList.get(mPosition);
        // 请求撤单接口
        String token = SpUtils.getString(this, "mSession", "");
        InterfaceCollection.getInstance().fundWithdrawOrder(token, entity.getEntrust_no(), TAG, new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {

                if ("0".equals(info.getCode())) {
                    Helper.getInstance().showToast(FJWithdrawOrderActivity.this, "撤销此委托成功");
                    requestData("");
                } else if ("400".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
                    Helper.getInstance().showToast(FJWithdrawOrderActivity.this, info.getMsg());
                } else if ("-6".equals(info.getCode())) {
                    Intent intent = new Intent();
                    intent.setClass(FJWithdrawOrderActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                } else {
                    //   系统弹窗
                    MistakeDialog.specialshowDialog(info.getMsg(), FJWithdrawOrderActivity.this, null);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }
}
