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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.AccoundSearchAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundAccountEntity;
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
 * 基金查询界面
 */
public class AccoundSearchActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lv_accound_search;
    private ImageView iv_back;
    private TextView tv_text1, tv_text2, tv_text3;
    private List<FundAccountEntity> fundAccountBeans;
    private AccoundSearchAdapter accoundSearchAdapter;
    private ImageView isEmpty;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_text1 = (TextView) findViewById(R.id.tv_text1);
        tv_text2 = (TextView) findViewById(R.id.tv_text2);
        tv_text3 = (TextView) findViewById(R.id.tv_text3);
        lv_accound_search = (PullToRefreshListView) findViewById(R.id.lv_accound_search);
        isEmpty = (ImageView) findViewById(R.id.isEmpty);
        initData();
    }

    private void initData() {
        fundAccountBeans = new ArrayList<FundAccountEntity>();
        accoundSearchAdapter = new AccoundSearchAdapter(this);
        lv_accound_search.setAdapter(accoundSearchAdapter);
        lv_accound_search.setEmptyView(isEmpty);
        iv_back.setOnClickListener(this);
        tv_text1.setText("开户日期");
        tv_text2.setText("基金账户");
        tv_text3.setText("基金公司/状态");
        getFundData(false);
        lv_accound_search.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_accound_search.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getFundData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    /**
     * 获取基金数据
     */
    private void getFundData(final boolean isClean) {
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
                lv_accound_search.onRefreshComplete();
                Helper.getInstance().showToast(AccoundSearchActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
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
                    if (isClean){
                        fundAccountBeans.clear();
                    }
                    if ("0".equals(code)) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FundAccountEntity fundAccountBean = new Gson().fromJson(jsonArray.getString(i), FundAccountEntity.class);
                            fundAccountBeans.add(fundAccountBean);
                        }
                        accoundSearchAdapter.setFundAccountBeans(fundAccountBeans);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(AccoundSearchActivity.this, TransactionLoginActivity.class));
                    } else {
                        MistakeDialog.showDialog(msg,AccoundSearchActivity.this);
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
