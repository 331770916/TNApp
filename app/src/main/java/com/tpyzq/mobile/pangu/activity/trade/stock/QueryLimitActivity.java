package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 刘泽鹏
 * 查询可用额度  界面
 */
public class QueryLimitActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "QueryLimit";
    private ImageView queryLimit_back;
    private WebView wvSubscribeLimitRule;
    private TextView tvQueryLimitHuANum, tvQueryLimitShenANum, tvHuGDID, tvShenGDID = null;
    private String session;

    @Override
    public void initView() {
        this.queryLimit_back = (ImageView) this.findViewById(R.id.activityQueryLimit_back);
        this.queryLimit_back.setOnClickListener(this);
        this.wvSubscribeLimitRule = (WebView) this.findViewById(R.id.wvSubscribeLimitRule);
        this.wvSubscribeLimitRule.getSettings().setJavaScriptEnabled(true);
        this.wvSubscribeLimitRule.loadUrl("file:///android_asset/Purchase_quota_rules.html");
        this.tvQueryLimitHuANum = (TextView) this.findViewById(R.id.tvQueryLimitHuANum);
        this.tvQueryLimitShenANum = (TextView) this.findViewById(R.id.tvQueryLimitShenANum);
        this.tvHuGDID = (TextView) this.findViewById(R.id.tvHuGDID);
        this.tvShenGDID = (TextView) this.findViewById(R.id.tvShenGDID);
//        Intent intent = getIntent();
//        session = intent.getStringExtra("session");      //从上个界面传过来的   token  值
        session = SpUtils.getString(this, "mSession", "");
        getData();
    }

    private void getData() {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("funcid", "300380");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (!response.equals("") && response != null) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<OneKeySubscribeBean>() {
                    }.getType();
                    OneKeySubscribeBean bean = gson.fromJson(response, type);
                    List<OneKeySubscribeBean.DataBean> data = bean.getData();
                    String code = bean.getCode();
                    if (code.equals("-6")) {
                        Intent intent = new Intent(QueryLimitActivity.this, TransactionLoginActivity.class);
                        QueryLimitActivity.this.startActivity(intent);
                        finish();
                    } else if (data != null && code.equals("0")) {
                        for (int i = 0; i < data.size(); i++) {
                            OneKeySubscribeBean.DataBean dataBean = data.get(i);
                            String market = dataBean.getMARKET();
                            if (market.equals("1")) {
                                tvQueryLimitHuANum.setText(dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf(".")) + "股");
                                tvHuGDID.setText(dataBean.getSTOCK_ACCOUNT());//这个位置展示的是  接口里的证券账号
                            } else if (market.equals("2")) {
                                tvQueryLimitShenANum.setText(dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf(".")) + "股");
                                tvShenGDID.setText(dataBean.getSTOCK_ACCOUNT());
                            }
                        }
                    } else {
                        ResultDialog.getInstance().showText("网络异常");
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvSubscribeLimitRule.canGoBack()) {
            wvSubscribeLimitRule.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_limit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityQueryLimit_back:
                this.finish();
                break;
        }
    }
}
