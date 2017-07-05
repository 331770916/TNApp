package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.ETFRevokeAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;

import java.util.ArrayList;

/**
 * Created by 李雄 on 2017/7/5.
 * 申赎撤单
 */

public class ETFRevokeActivity extends BaseActivity implements  StructuredFundDialog.Expression {
    private ImageView iv_back;
    private PullToRefreshListView lv;
    private ImageView iv_null;
    private String position_str = "";//定位串
    private ArrayList<EtfDataEntity> mList;
    private ETFRevokeAdapter adapter;
    public static final String TAG = "ETFRevokeActivity";
    private final int PAGESIZE = 30;//每页条数
    private String mSession;//手机识别码
    private Dialog mDialog;//等待条
    public EtfDataEntity clickEntity;//点击的条目实体
    //    private boolean isMore = true;//是否还有更多
    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_null = (ImageView)findViewById(R.id.iv_null);
        lv = (PullToRefreshListView)findViewById(R.id.lv);
        lv.setEmptyView(iv_null);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickEntity = mList.get(position);
                StructuredFundDialog dialog = new StructuredFundDialog(ETFRevokeActivity.this, TAG, ETFRevokeActivity.this, mList.get(position), null, null);
                dialog.show();
            }
        });
        mList = new ArrayList<EtfDataEntity>();
        adapter = new ETFRevokeAdapter(this, mList);
        lv.setAdapter(adapter);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (lv.isShownHeader()) {
                    lv.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    lv.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    initData(true);
                } else if (lv.isShownFooter()) {
//                    if (!isMore) {//没有更多数据了
//                        return;
//                    }
                    lv.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    lv.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    initData(false);
                }
            }
        });
        mDialog = LoadingDialog.initDialog(this, "");

        initData(true);
    }

    private void initData(final boolean isRefresh) {
        if (null!=mDialog && !mDialog.isShowing()) {
            mDialog.show();
        }
        String tempposition_str = position_str;
        if (isRefresh) {
            tempposition_str = "";
        }
        InterfaceCollection.getInstance().queryEntrust(mSession, "1",tempposition_str, PAGESIZE+"", TAG, new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                if ("0".equalsIgnoreCase(code)) {
                    if (isRefresh) {
                        mList.clear();
                    }
                    ArrayList<EtfDataEntity> tempList = (ArrayList<EtfDataEntity>) info.getData();
                    //判断是否取到整页数据
                    if (null==tempList||tempList.size()<PAGESIZE&&lv.getMode()== PullToRefreshBase.Mode.BOTH){
                        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else if (null!=tempList||tempList.size()==PAGESIZE&&lv.getMode()== PullToRefreshBase.Mode.PULL_FROM_START){
                        lv.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    //获取定位串
                    if (null!=tempList&&tempList.size()>0) {
                        position_str = tempList.get(0).getPosition_str();
                    }
                    mList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(msg);
                }
                lv.onRefreshComplete();
                if (null!=mDialog && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_etf_revoke_order;
    }

    @Override
    public void State() {
        if (null == clickEntity) {
            return;
        }
        if (null!=mDialog && !mDialog.isShowing()) {
            mDialog.show();
        }
        InterfaceCollection.getInstance().revokeOrder(mSession, clickEntity.getExchange_type(), clickEntity.getEntrust_no(), clickEntity.getStock_code(), TAG, new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                if ("0".equalsIgnoreCase(code)) {
                    MistakeDialog.showDialog(msg,ETFRevokeActivity.this,null);
                } else {
                    MistakeDialog.showDialog(msg,ETFRevokeActivity.this,null);
                }
                if (null!=mDialog && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }
}
