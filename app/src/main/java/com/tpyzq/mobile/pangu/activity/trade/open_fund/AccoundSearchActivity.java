package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.AccoundSearchAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 基金查询界面
 */
public class AccoundSearchActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_accound_search;
    private ImageView iv_back;
    private LinearLayout ll_fourtext;
    private TextView tv_text1, tv_text2, tv_text3, tv_text4;
    private List<FundAccountEntity> fundAccountBeans;
    private AccoundSearchAdapter accoundSearchAdapter;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_fourtext = (LinearLayout) findViewById(R.id.ll_fourtext);
        tv_text1 = (TextView) ll_fourtext.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) ll_fourtext.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) ll_fourtext.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) ll_fourtext.findViewById(R.id.tv_text4);
        lv_accound_search = (ListView) findViewById(R.id.lv_accound_search);
        initData();
    }

    private void initData() {
        fundAccountBeans = new ArrayList<FundAccountEntity>();
        accoundSearchAdapter = new AccoundSearchAdapter(this);
        lv_accound_search.setAdapter(accoundSearchAdapter);
        iv_back.setOnClickListener(this);
        tv_text1.setText("开户日期");
        tv_text2.setText("基金账户");
        tv_text3.setText("基金公司/状态");
        tv_text4.setVisibility(View.GONE);
        getFundData();
    }

    /**
     * 获取基金数据
     */
    private void getFundData() {
        HashMap map300435 = new HashMap();
        map300435.put("funcid", "300435");
        map300435.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300435_1 = new HashMap();
        map300435_1.put("SEC_ID", "tpyzq");
        map300435_1.put("FLAG", "true");
        map300435.put("parms", map300435_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300435, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(AccoundSearchActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
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
                    JSONArray jsonArray = new JSONArray(data);
                    if ("0".equals(code)) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FundAccountEntity fundAccountBean = new Gson().fromJson(jsonArray.getString(i), FundAccountEntity.class);
                            fundAccountBeans.add(fundAccountBean);
                        }
                        accoundSearchAdapter.setFundAccountBeans(fundAccountBeans);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(AccoundSearchActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(AccoundSearchActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_search;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
