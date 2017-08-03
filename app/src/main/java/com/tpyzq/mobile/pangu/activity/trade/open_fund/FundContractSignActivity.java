package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.ContractAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.ContractEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 基金电子合同签署
 */
public class FundContractSignActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshListView lv_accound_search;
    private ImageView iv_back;
    private ImageView iv_kong;
    private TextView tv_text1, tv_text2, tv_text3, tv_text4;
    private List<ContractEntity> contractBeans;
    private ContractAdapter contractAdapter;
    private Dialog mDialog;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        tv_text1 = (TextView) findViewById(R.id.tv_text1);
        tv_text2 = (TextView) findViewById(R.id.tv_text2);
        tv_text3 = (TextView) findViewById(R.id.tv_text3);
        lv_accound_search = (PullToRefreshListView) findViewById(R.id.lv_accound_search);
        initData();
    }

    private void initData() {
        mDialog = LoadingDialog.initDialog(this, "加载中...");
        lv_accound_search.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        contractBeans = new ArrayList<ContractEntity>();
        contractAdapter = new ContractAdapter(this);
        lv_accound_search.setAdapter(contractAdapter);
        lv_accound_search.setEmptyView(iv_kong);
        iv_back.setOnClickListener(this);
        tv_text1.setText("产品名称");
        tv_text2.setText("代码/状态");
        tv_text3.setText("产品公司");
        lv_accound_search.setOnItemClickListener(this);

        lv_accound_search.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getFundData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {


            }
        });
        getFundData();
    }

    /**
     * 获取基金数据
     */
    private void getFundData() {
        if (mDialog != null && !this.isFinishing()) {
            mDialog.show();
        }
        HashMap map300437 = new HashMap();
        map300437.put("funcid", "300437");
        map300437.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300437_1 = new HashMap();
        map300437_1.put("SEC_ID", "tpyzq");
        map300437_1.put("FLAG", "true");
        map300437_1.put("PAGE_INDEX", "0");
        map300437_1.put("PAGE_SIZE", "100");
        map300437.put("parms", map300437_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300437, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mDialog.dismiss();
                lv_accound_search.onRefreshComplete();
                Helper.getInstance().showToast(FundContractSignActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                mDialog.dismiss();
                lv_accound_search.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    JSONArray jsonArray = new JSONArray(data);
                    if ("0".equals(code)) {
                        contractBeans.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ContractEntity contractBean = new Gson().fromJson(jsonArray.getString(i), ContractEntity.class);
                            contractBeans.add(contractBean);
                        }
                        contractAdapter.setContractBeans(contractBeans);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundContractSignActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(FundContractSignActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==0 && resultCode == 1){
            getFundData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_contract_sign;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String IS_SIGN = contractBeans.get(position-1).IS_SIGN;
        Intent intent = new Intent();
        if ("0".equals(IS_SIGN)){
            intent.setClass(this,FlowActivity.class);
            intent.putExtra("sign",contractBeans.get(position-1));
            startActivity(intent);
        }else {
            intent.setClass(this,SignActivity.class);
            intent.putExtra("sign",contractBeans.get(position-1));
            startActivityForResult(intent,0);
        }
    }
}
