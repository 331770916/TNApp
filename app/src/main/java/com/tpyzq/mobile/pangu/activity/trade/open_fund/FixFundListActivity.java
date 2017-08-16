package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FixFundAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.base.ItemOnClickListener;
import com.tpyzq.mobile.pangu.data.FixFundEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;

import java.util.ArrayList;

/**
 * 基金定投页面
 * Created by lx on 2017/7/19.
 */

public class FixFundListActivity extends BaseActivity implements View.OnClickListener,InterfaceCollection.InterfaceCallback, ItemOnClickListener, StructuredFundDialog.Expression {
    public static final String TAG = "FixFundListActivity";//请求数据
    private static final String TAG_LIST = "LIST";//请求数据
    private static final String TAG_LIST_FIRST = "LIST_FIRST";//进入页面第一次请求
    public static final String TAG_REVOKE = "REVOKE";//请求数据
    private static int REQUEST_MODIFY = 10001;
    private static int REQUEST_ADD = 1002;
    private TextView tv_title_click;
    private ImageView iv_back;
    private PullToRefreshListView lv;
    private RelativeLayout rl_null;
    private ArrayList<FixFundEntity> mList = new ArrayList<FixFundEntity>();//数据源
    private FixFundAdapter fixFundAdapter;
    private boolean isRefresh = true;//是否是刷新
    private boolean isRequest = true;//是否在请求
    private boolean isAll = false;//是否没有更多
    private String POSITION_STR = " ";//定位串
    private Dialog mDialog,mRevokeDialog;
    private StructuredFundDialog mStructuredFundDialog;
    private int position;//点击的条目，修改或撤销

    @Override
    public void initView() {
        tv_title_click = (TextView) findViewById(R.id.tv_title_click);//添加
        findViewById(R.id.rl_null).setOnClickListener(this);

        tv_title_click.setOnClickListener(this);
        rl_null = (RelativeLayout) findViewById(R.id.rl_null);//空页面
        lv = (PullToRefreshListView) findViewById(R.id.lv);//基金列表
//        lv.setEmptyView(rl_null);
//        FixFundEntity fixFundEntity = new FixFundEntity("000333","加速度快乐健康拉速度","2000.00","2000000.00","每月1号","2017-12-30","","申请编号","1","未处理","2017-01-01","30");
//        FixFundEntity fixFundEntity1 = new FixFundEntity("000334","加速度快乐健康拉速度1","2001.00","2000001.00","每月30号","2017-12-30","","申请编号","1","未处理","2017-01-01","30");
//        mList.add(fixFundEntity);
//        mList.add(fixFundEntity1);
        fixFundAdapter = new FixFundAdapter(this, mList);
        fixFundAdapter.setItemOnClickListener(this);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (lv.isShownHeader()) {
                    lv.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    lv.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    isRequest = true;
                    isRefresh = true;
                    InterfaceCollection.getInstance().getFixFundList("",TAG_LIST,FixFundListActivity.this);
                } else if (lv.isShownFooter()) {
//                    if (!isMore) {//没有更多数据了
//                        return;
//                    }
                    isRequest = true;
                    isRefresh = false;
                    lv.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    lv.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    InterfaceCollection.getInstance().getFixFundList("",TAG_LIST,FixFundListActivity.this);
                }
            }
        });

        lv.setAdapter(fixFundAdapter);
        iv_back = (ImageView) findViewById(R.id.iv_back);//返回
        iv_back.setOnClickListener(this);
        mDialog = LoadingDialog.initDialog(this, "正在查询...");
        mRevokeDialog = LoadingDialog.initDialog(this, "操作中...");
        mDialog.show();
        InterfaceCollection.getInstance().getFixFundList("",TAG_LIST,FixFundListActivity.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fix_fund_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_click:
            case R.id.rl_null:
                Intent intent = new Intent(this, AddOrModFixFundActivity.class);
                startActivityForResult(intent,REQUEST_ADD);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        String code = info.getCode();
        String tag = info.getTag();
        String msg = info.getMsg();
        if (TAG_LIST.equalsIgnoreCase(tag)) {//刷新或者上拉获取数据
            if ("0".equalsIgnoreCase(code)) {
                lv.setVisibility(View.VISIBLE);
                rl_null.setVisibility(View.GONE);
                ArrayList<FixFundEntity> tempList;
                if (isRefresh) {
                    mList.clear();
                }
                isAll = false;
                tempList = (ArrayList<FixFundEntity>) info.getData();
                mList.addAll(tempList);
                lv.onRefreshComplete();
                if (tempList.size() == 0) {
                    isAll = true;
                    if (isRefresh) {
                        lv.setVisibility(View.GONE);
                        rl_null.setVisibility(View.VISIBLE);
                    }
                    if (lv.getMode()==PullToRefreshBase.Mode.BOTH) {
                        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    fixFundAdapter.setData(mList,true);
                } else if (tempList.size() == 30) {
                    lv.onRefreshComplete();
                    isAll = false;
                    if (lv.getMode()==PullToRefreshBase.Mode.PULL_FROM_START) {
                        lv.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    fixFundAdapter.setData(mList,false);
                } else {
                    lv.onRefreshComplete();
                    //没有更多了
                    isAll = true;
                    if (lv.getMode()==PullToRefreshBase.Mode.BOTH) {
                        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    fixFundAdapter.setData(mList,true);
                }
            } else if ("-6".equalsIgnoreCase(code)) {
                skip.startLogin(FixFundListActivity.this);
                lv.onRefreshComplete();
            } else {
                lv.onRefreshComplete();
                CentreToast.showText(this,msg);
            }
        }
        /*else if (TAG_LIST_FIRST.equalsIgnoreCase(tag)) {//进入第一次请求
            if ("0".equalsIgnoreCase(code)) {
                ArrayList<FixFundEntity> tempList;
                tempList = (ArrayList<FixFundEntity>) info.getData();
                mList.addAll(tempList);
                lv.onRefreshComplete();
                if (tempList.size() == 0) {
                    lv.setVisibility(View.GONE);
                    rl_null.setVisibility(View.VISIBLE);
                } else if (tempList.size() == 30) {
                    if (lv.getMode()==PullToRefreshBase.Mode.PULL_FROM_START) {
                        lv.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    fixFundAdapter.setData(mList,false);
                } else {
                    //没有更多了
                    if (lv.getMode()==PullToRefreshBase.Mode.BOTH) {
                        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    fixFundAdapter.setData(mList,true);
                }
            } else if ("-6".equalsIgnoreCase(code)) {

            } else {
                CentreToast.showText(this,msg,false);
            }
        }*/
        else if (TAG_REVOKE.equalsIgnoreCase(tag)) {//撤销
            if ("0".equalsIgnoreCase(code)) {
                CentreToast.showText(this, msg, true);
                mList.remove(position);
                fixFundAdapter.notifyDataSetChanged();
                if (mList.size()==0) {
                    rl_null.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                }
            } else if ("-6".equalsIgnoreCase(code)) {
                skip.startLogin(FixFundListActivity.this);
            } else {
                CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
                customCenterDialog.show(getFragmentManager(),FixFundListActivity.class.toString());
            }
            lv.onRefreshComplete();
        }
        isRequest = false;
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (null != mRevokeDialog && mRevokeDialog.isShowing()) {
            mRevokeDialog.dismiss();
        }
    }

    //适配器中点击撤销的回调
    @Override
    public void onItemClick(int type, int position) {
        this.position = position;
        switch (type) {
            case FixFundAdapter.TAG_ADD:
                Intent addIntent = new Intent(this, AddOrModFixFundActivity.class);
                startActivityForResult(addIntent,REQUEST_ADD);
                break;
            case FixFundAdapter.TAG_REVOKE:
                FixFundEntity fixFundEntity = mList.get(position);
                if (ConstantUtil.list_item_flag) {
                    ConstantUtil.list_item_flag = false;
                    mStructuredFundDialog = new StructuredFundDialog(this);
                    mStructuredFundDialog.setData(TAG, this, fixFundEntity, null, null);
                    mStructuredFundDialog.show();
                }
                break;
            case FixFundAdapter.TAG_MODIFY:
                Intent intent = new Intent(this, AddOrModFixFundActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("fixFundEntity",mList.get(position));
                startActivityForResult(intent,REQUEST_MODIFY);
                break;
        }

        /*if (null != mRevokeDialog && !mRevokeDialog.isShowing()) {
            mRevokeDialog.show();
        }
        FixFundEntity fixFundEntity = mList.get(position);
        InterfaceCollection.getInstance().revokeFixFund(fixFundEntity.getFUND_CODE(), fixFundEntity.getALLOTNO(),TAG_REVOKE,FixFundListActivity.this);*/
    }
    //弹框点击确定回调函数
    @Override
    public void State() {
        mStructuredFundDialog.dismiss();
        mStructuredFundDialog = null;
        if (null != mRevokeDialog && !mRevokeDialog.isShowing()) {
            mRevokeDialog.show();
        }
        FixFundEntity fixFundEntity = mList.get(position);
        InterfaceCollection.getInstance().revokeFixFund(fixFundEntity.getFUND_CODE(), fixFundEntity.getALLOTNO(),TAG_REVOKE,FixFundListActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MODIFY && resultCode == RESULT_OK) {
            FixFundEntity fixFundEntity = (FixFundEntity) data.getSerializableExtra("fixFundEntity");
            mList.set(position,fixFundEntity);
            fixFundAdapter.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            /*FixFundEntity fixFundEntity = (FixFundEntity) data.getSerializableExtra("fixFundEntity");
            ArrayList<FixFundEntity> tempList = new ArrayList<>();
            tempList.addAll(mList);
            mList.clear();
            mList.add(0, fixFundEntity);
            mList.addAll(tempList);
            fixFundAdapter.notifyDataSetChanged();
            tempList.clear();
            tempList = null;
            rl_null.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);*/
            mDialog.show();
            InterfaceCollection.getInstance().getFixFundList("",TAG_LIST,FixFundListActivity.this);
        }
    }
}
