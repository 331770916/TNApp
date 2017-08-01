package com.tpyzq.mobile.pangu.activity.trade.open_fund;

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
import com.tpyzq.mobile.pangu.data.FundRedemptionEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 *     持仓基金
 */
public class PositionFundActivity extends BaseActivity {
    ImageView iv_back,iv_kong;
    ListView lv_fund_product;
    FundProductAdapter fundProductAdapter;
    List<FundEntity> fundBeans;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        lv_fund_product = (ListView) findViewById(R.id.lv_fund_product);
        initData();
        fundQuery();
    }

    private void initData() {
        fundBeans = new ArrayList<FundEntity>();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        final int point = bundle.getInt("point");
        fundProductAdapter = new FundProductAdapter(this);

        fundProductAdapter.setPoint(point);
        lv_fund_product.setAdapter(fundProductAdapter);
        lv_fund_product.setEmptyView(iv_kong);
        lv_fund_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("point",position);
                intent.putExtra("fund_code",fundBeans.get(position).fund_code);
                intent.putExtra("fund_nav",fundBeans.get(position).fund_nav);
                intent.putExtra("fund_share",fundBeans.get(position).fund_share);
                intent.putExtra("fund_company",fundBeans.get(position).fund_company);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    /**
     * 获取基金份额
     */
    private void fundQuery() {
        HashMap map720260 = new HashMap();
        map720260.put("funcid", "720260");
        map720260.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map720260_1 = new HashMap();
        map720260_1.put("SEC_ID", "tpyzq");
        map720260_1.put("FLAG", "true");
        map720260.put("parms", map720260_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map720260, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Helper.getInstance().showToast(PositionFundActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
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
                        FundRedemptionEntity fundRedemptionBean = new Gson().fromJson(jsonArray.getString(0), FundRedemptionEntity.class);
                        for (int i = 0; i < fundRedemptionBean.RESULT_LIST.size(); i++) {
                            FundEntity fundBean = new FundEntity();
                            fundBean.fund_code = fundRedemptionBean.RESULT_LIST.get(i).FUND_CODE;
                            fundBean.fund_company = fundRedemptionBean.RESULT_LIST.get(i).FUND_COMPANY_CODE;
                            fundBean.fund_name = fundRedemptionBean.RESULT_LIST.get(i).FUND_NAME;
                            fundBean.fund_nav = fundRedemptionBean.RESULT_LIST.get(i).NAV;
                            fundBean.fund_share = fundRedemptionBean.RESULT_LIST.get(i).CURRENT_SHARE;
                            fundBeans.add(fundBean);
                        }
                        fundProductAdapter.setData(fundBeans);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(PositionFundActivity.this, TransactionLoginActivity.class));
                    } else {
                        MistakeDialog.showDialog(msg,PositionFundActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_position_fund;
    }
}
