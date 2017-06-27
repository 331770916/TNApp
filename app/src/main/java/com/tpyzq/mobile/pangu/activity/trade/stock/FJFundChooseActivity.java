package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Dialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.FJFundChooseAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
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
    private List<StructuredFundEntity> list;
    private String mSession;
    private InterfaceCollection ifc;
    private FJFundChooseAdapter adapter;
    private ImageView isEmpty;
    private Dialog mDialog;

    @Override
    public void initView() {
        findViewById(R.id.ivCurrencyFundRedeem_back).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.listView);
        isEmpty = (ImageView) findViewById(R.id.isEmpty);
        initData();
    }

    private void initData() {
        ifc = InterfaceCollection.getInstance();
        mSession = SpUtils.getString(this, "mSession", "");

        int mPoint = getIntent().getIntExtra("point", -1);
        mListView.setOnItemClickListener(this);

        adapter = new FJFundChooseAdapter(this);
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
        ifc.Fundchoice(mSession, TAG, this);
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
        intent.putExtra("Name", list.get(position).getStoken_name());
        intent.putExtra("Code", list.get(position).getStocken_code());
        intent.putExtra("Market", list.get(position).getMarket());
        intent.putExtra("point", position);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void callResult(ResultInfo info) {
        mDialog.dismiss();
        if ("-1".equals(info.getCode()) || "-2".equals(info.getCode()) || "-3".equals(info.getCode())) {
            Helper.getInstance().showToast(this, info.getMsg());
        } else if ("0".equals(info.getCode())) {
            list = (List<StructuredFundEntity>) info.getData();
            adapter.setData(list);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && mDialog.isShowing()) {
            mDialog.dismiss();
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
