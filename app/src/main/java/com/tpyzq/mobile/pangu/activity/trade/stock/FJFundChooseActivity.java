package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.FJFundChooseAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;

import java.util.List;

/**
 * wangq
 * 选择基金
 */

public class FJFundChooseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, InterfaceCollection.InterfaceCallback {
    public static String TAG = "FJFundChooseActivity";

    private ListView mListView;
    private List<StructuredFundEntity> list_SFE;
    private List<NetworkVotingEntity> list_NVE;
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


        switch (mTag){
            case 0:
                break;
            case 1:
                mTitl_tv.setText("投票查询");
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
//                ifc.
                ifc.getData(10, this);
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
                intent.putExtra("Code", list_NVE.get(position).getStock_code());
                intent.putExtra("point", position);
                break;
        }

        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void callResult(ResultInfo info) {
        mDialog.dismiss();
        if ("-1".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
            Helper.getInstance().showToast(this, info.getMsg());
        } else if ("0".equals(info.getCode())) {

            switch (mTag) {
                case 0:
                    list_SFE = (List<StructuredFundEntity>) info.getData();
                    adapter.setData(list_SFE);
                    break;
                case 1:
                    list_NVE = (List<NetworkVotingEntity>) info.getData();
                    adapter.setData(list_NVE);
                    break;
            }
        } else if ("-6".equals(info.getCode())) {
            Intent intent = new Intent();
            intent.setClass(this, TransactionLoginActivity.class);
            startActivity(intent);
        } else {
            MistakeDialog.showDialog(info.getMsg(), this, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            finish();
        }
        return false;
    }
}
