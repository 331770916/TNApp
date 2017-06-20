package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OptionalFundAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;
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
 * 基金信息
 */
public class FundInfoActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_search_fundcompany;
    private Button bt_search;
    private ImageView iv_back;
    private PullToRefreshListView lv_fund;
    private LinearLayout ll_title;
    private TextView tv_text1, tv_text2, tv_text3, tv_text4;
    private OptionalFundAdapter optionalFundAdapter;
    private List<FundSubsEntity> fundSubsBeans;
    private FundSubsEntity fundSubsBean;
    private int position = 0;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_search_fundcompany = (EditText) findViewById(R.id.et_search_fundcompany);
        bt_search = (Button) findViewById(R.id.bt_search);
        lv_fund = (PullToRefreshListView) findViewById(R.id.lv_fund);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        tv_text1 = (TextView) ll_title.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) ll_title.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) ll_title.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) ll_title.findViewById(R.id.tv_text4);
        initData();
    }

    private void initData() {
        fundSubsBeans = new ArrayList<FundSubsEntity>();
        optionalFundAdapter = new OptionalFundAdapter(this);
        tv_text1.setText("名称");
        tv_text2.setText("净值/最低投资");
        tv_text3.setText("风险等级");
        tv_text4.setText("状态");
        iv_back.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        fundQuery("", 0, false);
        lv_fund.setAdapter(optionalFundAdapter);

        lv_fund.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                            fundSubsBeans.clear();
                            fundQuery("", 0, false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //将下拉视图收起
                        lv_fund.onRefreshComplete();
                    }
                }.execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                position++;
                fundQuery(et_search_fundcompany.getText().toString(), position, false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_search:
                fundSubsBeans.clear();
                fundQuery(et_search_fundcompany.getText().toString(), 0, false);
                break;
        }
    }

    /**
     * 获取基金产品
     */
    private void fundQuery(String company_name, int i, final boolean flag) {

        HashMap map300441 = new HashMap();
        map300441.put("funcid", "300441");
        map300441.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300441_1 = new HashMap();
        map300441_1.put("SEC_ID", "tpyzq");
        map300441_1.put("FUND_TYPE", "");
//        map300441_1.put("FUND_CODE", "");
        map300441_1.put("FUND_COMPANY_NAME", company_name);
        map300441_1.put("PAGE_INDEX", i);
        map300441_1.put("PAGE_SIZE", "20");
        map300441_1.put("FLAG", "true");
        map300441.put("parms", map300441_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300441, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(FundInfoActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
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
                        if (flag){
                            fundSubsBeans.remove(fundSubsBeans.size()-1);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            fundSubsBean = new Gson().fromJson(jsonArray.getString(i), FundSubsEntity.class);
                            fundSubsBeans.add(fundSubsBean);
                        }
                        optionalFundAdapter.setData(fundSubsBeans);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundInfoActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(FundInfoActivity.this, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lv_fund.onRefreshComplete();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_info;
    }
}
