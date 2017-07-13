package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.ETFStockQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltyhome on 05/07/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: 成分股查询
 */

public class ETFStockQueryActivity extends BaseActivity implements View.OnClickListener, InterfaceCollection.InterfaceCallback, AdapterView.OnItemClickListener {
    private PullToRefreshListView etfList;
    private Dialog mDialog;
    private List<EtfDataEntity> mList;
    private final String TAG = "ETFStockQueryActivity";
    private ETFStockQueryAdapter adapter;
    private String token;
    private ImageView imageView;


    @Override
    public void initView() {
        mList = new ArrayList<>();
        findViewById(R.id.iv_back).setOnClickListener(this);
        etfList = (PullToRefreshListView) findViewById(R.id.etfList);
        imageView = (ImageView) findViewById(R.id.img_show);
        etfList.setEmptyView(imageView);
        etfList.setOnItemClickListener(this);
        adapter = new ETFStockQueryAdapter(this, mList, TAG);
        etfList.setAdapter(adapter);
        token = SpUtils.getString(this, "mSession", "");
        mDialog = LoadingDialog.initDialog(this, "正在查询...");
        requestData("", "30");
        etfList.setMode(PullToRefreshBase.Mode.BOTH);
        etfList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mList.clear();
                requestData("", "30");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mList!=null&& mList.size()>0) {
                    String position = mList.get(mList.size()-1).getPosition_str();
                    requestData(position, "30");
                }

            }
        });
    }

    private void requestData(String page, String num) {
        if (!mDialog.isShowing())
            mDialog.show();

        InterfaceCollection.getInstance().constituentStockList(token,page,num,TAG,this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_constituent_query;
    }


    @Override
    public void callResult(ResultInfo info) {
        if (mDialog.isShowing() && mDialog != null) {
            mDialog.dismiss();
        }
        if (TAG.equals(info.getTag())) {
            String code = info.getCode();
            if ("0".equals(code)) {
                List<EtfDataEntity> list = (List<EtfDataEntity>) info.getData();
                mList.addAll(list);
                adapter.notifyDataSetChanged();
            } else if ("-6".equals(code)) {
                skip.startLogin(this);
            } else  if ("400".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {   //  网络错误 解析错误 其他
                Helper.getInstance().showToast(this, info.getMsg());
            } else {
                MistakeDialog.showDialog(info.getMsg(),this);
            }

        }
        etfList.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ETFStockListActivity.class);
        intent.putExtra("stock_no", mList.get(position - 1).getComponent_code());
        startActivity(intent);
    }
}
