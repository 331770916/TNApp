package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.ETFTransactrionQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;

import java.util.ArrayList;

/**
 * Created by 李雄 on 2017/7/5.
 * etf申赎交易查询
 */

public class ETFTrasactionQueryActivity extends BaseActivity {
    private ImageView iv_back;
    private PullToRefreshListView lv;
    private ImageView iv_null;
    private String position_str = "";//定位串
    private ArrayList<EtfDataEntity> mList;
    private ETFTransactrionQueryAdapter adapter;
    private final String TAG = "ETFTrasactionQueryActivity300746";
    private final int PAGESIZE = 30;//每页条数
    private String mSession;//手机识别码
    private Dialog mDialog;//等待条
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
        mList = new ArrayList<EtfDataEntity>();
        adapter = new ETFTransactrionQueryAdapter(this, mList);
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
        mDialog = LoadingDialog.initDialog(this, "正在查询...");

        initData(true);
    }

    /**
     * 请求数据
     * @param isRefresh 是否刷新或第一次进入发送请求
     */
    private void initData(final boolean isRefresh) {
        if (null!=mDialog && !mDialog.isShowing()) {
            mDialog.show();
        }
        String tempposition_str = position_str;
        if (isRefresh) {
            tempposition_str = "";
        }
        InterfaceCollection.getInstance().queryDeal(mSession, tempposition_str, PAGESIZE+"", TAG, new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                if ("0".equalsIgnoreCase(code)) {
                    if (isRefresh) {
                        mList.clear();
                        adapter.setPoint(-1);
                    }
                    ArrayList<EtfDataEntity> tempList = (ArrayList<EtfDataEntity>) info.getData();
                    //
                    /*if (null!=tempList&&tempList.size()>0) {
                        position_str = tempList.get(0).getPosition_str();
                    } else {
                        position_str = "";
                    }*/
                    if (null!=tempList&&tempList.size()>0) {
                        position_str = tempList.get(tempList.size()-1).getPosition_str();
                    }
                    mList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                    lv.onRefreshComplete();
                    //判断是否取到整页数据
                    if (null==tempList||tempList.size()<PAGESIZE){
                        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        lv.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                } else  if ("400".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {   //  网络错误 解析错误 其他
                    CentreToast.showText(ETFTrasactionQueryActivity.this, info.getMsg());
                }else if ("-6".equals(info.getCode())){
                    skip.startLogin(ETFTrasactionQueryActivity.this);
                } else {
                    lv.onRefreshComplete();
                    //在初始化时如果网络失败只允许下拉
                    if (isRefresh&&mList.size()==0) {
                        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
                    customCenterDialog.show(getFragmentManager(),ETFTrasactionQueryActivity.class.toString());
                }
                if (null!=mDialog && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_deal_query;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }
}
