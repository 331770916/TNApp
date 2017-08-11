package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.FundProductAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundEntity;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 基金产品
 */
public class FundProductActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    ListView lv_fund_product;
    FundProductAdapter fundProductAdapter;
    private ImageView iv_null;

    private FundSubsEntity fundSubsBean;
    private List<FundSubsEntity> fundSubsBeans;
    private FundEntity fundBean;
    private List<FundEntity> fundBeans;
    private Dialog mDialog;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_null = (ImageView) findViewById(R.id.Null);
        lv_fund_product = (ListView) findViewById(R.id.lv_fund_product);
        initData();
        fundQuery();
    }

    private void initData() {
        mDialog = LoadingDialog.initDialog(this, "加载中...");
        fundBeans = new ArrayList<FundEntity>();
        fundSubsBeans = new ArrayList<FundSubsEntity>();
        iv_back.setOnClickListener(this);
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        int point = bundle.getInt("point");
        fundProductAdapter = new FundProductAdapter(this);

        fundProductAdapter.setPoint(point);
        lv_fund_product.setAdapter(fundProductAdapter);
        lv_fund_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("point", position);
                intent.putExtra("FUND_CODE", fundBeans.get(position).fund_code);
                intent.putExtra("FUND_COMPANY", fundBeans.get(position).fund_company);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        lv_fund_product.setEmptyView(iv_null);
    }


    /**
     * 获取基金产品
     */
    private void fundQuery() {
        if (mDialog != null && !this.isFinishing()) {
            mDialog.show();
        }
        HashMap map300441 = new HashMap();
        map300441.put("funcid", "300441");
        map300441.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300441_1 = new HashMap();
        map300441_1.put("SEC_ID", "tpyzq");
        map300441_1.put("FUND_TYPE", "1");
        map300441_1.put("FLAG", "true");
        map300441.put("parms", map300441_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300441, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mDialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                mDialog.dismiss();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            fundSubsBean = new Gson().fromJson(jsonArray.getString(i), FundSubsEntity.class);
                            fundSubsBeans.add(fundSubsBean);
                        }
                        for (int i = 0; i < fundSubsBeans.size(); i++) {
                            fundBean = new FundEntity();
                            fundBean.fund_code = fundSubsBeans.get(i).FUND_CODE;
                            fundBean.fund_name = fundSubsBeans.get(i).FUND_NAME;
                            fundBean.fund_company = fundSubsBeans.get(i).FUND_COMPANY;
                            fundBeans.add(fundBean);
                        }
                        fundProductAdapter.setData(fundBeans);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundProductActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(FundProductActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        return R.layout.activity_fund_product;
    }


}
