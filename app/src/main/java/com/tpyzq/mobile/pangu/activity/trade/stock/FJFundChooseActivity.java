package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FJFundChooseAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StockHolderInfoEntity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.List;

/**
 * wangq
 * 选择基金
 * tag 0 盘后分级基金选择  1 网络投票选择
 */

public class FJFundChooseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, InterfaceCollection.InterfaceCallback {
    public static String TAG = "FJFundChooseActivity";

    private ListView mListView;
    private List<StructuredFundEntity> list_SFE;
    private List<StockHolderInfoEntity> list_NVE;
    private String mSession;
    private InterfaceCollection ifc;
    private FJFundChooseAdapter adapter;
    private ImageView isEmpty;
    private Dialog mDialog;
    private int mTag;
    private TextView mTitl_tv;
    private LinearLayout isShow;

    @Override
    public void initView() {
        findViewById(R.id.ivCurrencyFundRedeem_back).setOnClickListener(this);
        mTitl_tv = (TextView) findViewById(R.id.tvTitle);
        isShow = (LinearLayout) findViewById(R.id.isShow);
        mListView = (ListView) findViewById(R.id.listView);
        isEmpty = (ImageView) findViewById(R.id.isEmpty);
        initData();
    }

    private void initData() {
        ifc = InterfaceCollection.getInstance();
        mSession = SpUtils.getString(this, "mSession", "");

        int mPoint = getIntent().getIntExtra("point", -1);
        mTag = getIntent().getIntExtra("tag", -1);
        mListView.setOnItemClickListener(this);


        switch (mTag) {
            case 0:
                mTitl_tv.setText(getString(R.string.Gradingfundmerger));
                break;
            case 1:
                mTitl_tv.setText(getString(R.string.voteEnterTitle));
                isShow.setVisibility(View.GONE);
                break;
        }


        adapter = new FJFundChooseAdapter(this, mTag);
        adapter.setPoint(mPoint);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(isEmpty);
        requestData();
    }

    private void requestData() {
        mDialog = LoadingDialog.initDialog(this, "加载中...");
        if (!this.isFinishing()) {
            mDialog.show();
        }

        switch (mTag) {
            case 0:
                ifc.Fundchoice(mSession, TAG, this);
                break;
            case 1:
                ifc.queryStockInfo(mSession,TAG,this);
                break;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_fund;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCurrencyFundRedeem_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (mTag) {
            case 0:
                intent.putExtra("Name", list_SFE.get(position).getStoken_name());
                intent.putExtra("Code", list_SFE.get(position).getStocken_code());
                intent.putExtra("Market", list_SFE.get(position).getMarket());
                intent.putExtra("point", position);
                break;
            case 1:
                intent.putExtra("Name", list_NVE.get(position).getShareholderSName());
                intent.putExtra("Code", list_NVE.get(position).getShareholderSCode());
                intent.putExtra("Market", list_NVE.get(position).getAccountType());
                intent.putExtra("point", position);
                break;
        }
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void callResult(ResultInfo info) {
        mDialog.dismiss();
        if ("400".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
            CentreToast.showText(this, info.getMsg());
        } else if ("0".equals(info.getCode())) {

            switch (mTag) {
                case 0:
                    list_SFE = (List<StructuredFundEntity>) info.getData();
                    adapter.setData(list_SFE);
                    break;
                case 1:
                    list_NVE = (List<StockHolderInfoEntity>) info.getData();
                    adapter.setData(list_NVE);
                    break;
            }
        } else if ("-6".equals(info.getCode())) {
            skip.startLogin(this);
        } else {
            final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(info.getMsg(),CustomCenterDialog.SHOWCENTER);
            customCenterDialog.show(getFragmentManager(),FJFundChooseActivity.class.toString());
            customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
                @Override
                public void confirmOnclick() {
                    finish();
                    customCenterDialog.dismiss();
                }
            });

        }
    }


    @Override
    public void destroy() {
        if (mDialog != null)
            mDialog.dismiss();
    }
}
