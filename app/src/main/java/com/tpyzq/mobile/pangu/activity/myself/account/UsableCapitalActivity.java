package com.tpyzq.mobile.pangu.activity.myself.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BanksTransferAccountsActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.UserMoneyEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 可取资金
 */
public class UsableCapitalActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    TextView tv_usable_money,tv_title,tv_other_title;
    LinearLayout ll_shiftto, ll_rollout, ll_capital_detail;
    String money;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_usable_money = (TextView) findViewById(R.id.tv_usable_money);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_other_title = (TextView) findViewById(R.id.tv_other_title);
        ll_shiftto = (LinearLayout) findViewById(R.id.ll_shiftto);
        ll_rollout = (LinearLayout) findViewById(R.id.ll_rollout);
        ll_capital_detail = (LinearLayout) findViewById(R.id.ll_capital_detail);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        money = intent.getStringExtra("money");
        if (!TextUtils.isEmpty(money)) {
            tv_usable_money.setText(money);
            tv_title.setText("可用资金");
            tv_other_title.setText("可用资金（元）");
        }else {
            getUserInfo();
        }
        ll_shiftto.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_rollout.setOnClickListener(this);
        ll_capital_detail.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_usable_capital;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_shiftto:
                intent.putExtra("tag", "100");
                intent.setClass(UsableCapitalActivity.this, BanksTransferAccountsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_rollout:
                intent.putExtra("tag", "200");
                intent.setClass(UsableCapitalActivity.this, BanksTransferAccountsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_capital_detail:
                intent.putExtra("tag", "300");
                intent.setClass(UsableCapitalActivity.this, BanksTransferAccountsActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void getUserInfo() {
        HashMap map300608 = new HashMap();
        map300608.put("funcid", "300608");
        map300608.put("token", SpUtils.getString(this, "mSession", null));
        HashMap map300608_1 = new HashMap();
        map300608_1.put("SEC_ID", "tpyzq");
        map300608_1.put("FLAG", "true");
        map300608.put("parms", map300608_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300608, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Toast.makeText(mContext, "网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        UserMoneyEntity userMoneyBean = new Gson().fromJson(jsonArray.getString(0), UserMoneyEntity.class);
                        settingView(userMoneyBean);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(UsableCapitalActivity.this, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(UsableCapitalActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void settingView(UserMoneyEntity userMoneyBean) {
        tv_usable_money.setText(userMoneyBean.FETCH_BALANCE);
    }
}
