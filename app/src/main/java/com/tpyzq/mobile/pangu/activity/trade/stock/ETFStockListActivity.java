package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.ETFStockQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.ArrayList;

/**
 * Created by 李雄 on 05/07/2017.
 * Describe: 成分股列表
 */

public class ETFStockListActivity extends BaseActivity {

    private ImageView iv_back;
    private PullToRefreshListView lv;
    private ImageView iv_null;
    private String position_str = "";//定位串
    private ArrayList<EtfDataEntity> mList;
    private ETFStockQueryAdapter adapter;
    public static final String TAG = "ETFStockListActivity";
    private final int PAGESIZE = 30;//每页条数
    private String mSession;//手机识别码
    private Dialog mDialog;//等待条
    private TextView tv_title,tv_stock_code, tv_etf_code,tv_stock_name;
    private String stock_no;

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        stock_no = getIntent().getStringExtra("stock_no");//股票代码
        tv_title = (TextView)findViewById(R.id.tv_title);//标题
        tv_title.setText("ETF("+stock_no+")成分股查询");
        tv_stock_code = (TextView)findViewById(R.id.tv_stock_code);//成分股名称
        tv_stock_code.setText("成分股名称");
        tv_stock_name = (TextView)findViewById(R.id.tv_stock_name);//成分股代码
        tv_stock_name.setText("成分股代码");
        tv_etf_code = (TextView)findViewById(R.id.tv_etf_code);//替代金额
        tv_etf_code.setText("替代金额");
        iv_back = (ImageView)findViewById(R.id.iv_back);//返回按钮
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_null = (ImageView)findViewById(R.id.img_show);
        lv = (PullToRefreshListView)findViewById(R.id.etfList);
        lv.setEmptyView(iv_null);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        mList = new ArrayList<EtfDataEntity>();
        adapter = new ETFStockQueryAdapter(this, mList, TAG);
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
        InterfaceCollection.getInstance().constituentStock(mSession, stock_no, tempposition_str, PAGESIZE+"", TAG, new InterfaceCollection.InterfaceCallback() {
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
                    //判断是否取到整页数据
                    if (null==tempList||tempList.size()<PAGESIZE){
                        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        lv.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    //
                    /*if (null!=tempList&&tempList.size()>0) {
                        position_str = tempList.get(0).getPosition_str();
                    } else {
                        position_str = "";
                    }*/
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
        return R.layout.activity_constituent_query;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }
}
