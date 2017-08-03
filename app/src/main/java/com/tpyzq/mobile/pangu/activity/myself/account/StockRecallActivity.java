package com.tpyzq.mobile.pangu.activity.myself.account;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.StockRecallEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.webview.ProgressWebView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 股市回忆录
 */
public class StockRecallActivity extends BaseActivity implements View.OnClickListener {
    public static String TAG = "StockRecallActivity";
    private ProgressWebView wb_stock_recall;
    private String path = "file:///android_asset/stock_history/stock_history.html";
    private ImageView iv_up;
    private ObjectAnimator anim;
    private  TimerTask task;
    private Timer timer;
    @Override
    public void initView() {
        wb_stock_recall = (ProgressWebView) findViewById(R.id.wb_stock_recall);
        findViewById(R.id.iv_back).setOnClickListener(this);
        iv_up = (ImageView) findViewById(R.id.iv_up);
        initData();
        toConnect();
    }

    private void toConnect() {
        UserUtil.refrushUserInfo();
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("FUNCTIONCODE", "HQTNG001");
        map.put("TOKEN", "");
        map.put("PARAMS", map1);
        map1.put("cust_id", UserUtil.capitalAccount);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_BB(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                Helper.getInstance().showToast(StockRecallActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("MSG");
                    JSONObject data = jsonObject.getJSONObject("DATA");
                    String code = jsonObject.getString("CODE");
                    if ("200".equals(code)) {
                        initData();
                        final StockRecallEntity bean = new StockRecallEntity();
                        bean.open_date = data.getString("open_date");
                        bean.first_stock_name = data.getString("first_stock_name");
                        bean.first_stock_code = data.getString("first_stock_code");
                        bean.most_profit_name = data.getString("most_profit_name");
                        bean.most_profit_code = data.getString("most_profit_code");
                        bean.most_profit_sum_profit = data.getString("most_profit_sum_profit");
                        bean.most_loss_name = data.getString("most_loss_name");
                        bean.most_loss_code = data.getString("most_loss_code");
                        bean.most_loss_profit = data.getString("most_loss_profit");
                        bean.most_hold_name = data.getString("most_hold_name");
                        bean.most_hold_code = data.getString("most_hold_code");
                        bean.most_hold_days = data.getInt("most_hold_days");
                        bean.trade_sum_num = data.getInt("trade_sum_num");
                        bean.trade_num_rank = data.getInt("trade_num_rank");


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {

                                        private String mDate = "";

                                        @Override
                                        public void run() {
                                            if (!TextUtils.isEmpty(bean.open_date) && !"--".equals(bean.open_date)) {
                                                mDate = Helper.getMyDateYMD(bean.open_date);
                                            }

                                            wb_stock_recall.loadUrl("javascript:replace('" + mDate + "','" + bean.first_stock_name + "(" + bean.first_stock_code + ")" + "','" +
                                                    bean.most_profit_name + "(" + bean.most_profit_code + ")" + "','" + TransitionUtils.string2doubleS(bean.most_profit_sum_profit) + "','" + bean.most_loss_name + "(" + bean.most_loss_code + ")" + "','" +
                                                    TransitionUtils.string2doubleS(bean.most_loss_profit) + "','" + bean.most_hold_name + "(" + bean.most_hold_code + ")" + "','" + bean.most_hold_days + "天" + "','" + bean.trade_num_rank + "','" + bean.trade_sum_num + "')");
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else if ("0".equals(code)) {
                        wb_stock_recall.loadUrl(path);
                    } else if ("-6".equals(code)) {
                        Intent intent = new Intent(StockRecallActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    } else {
                        wb_stock_recall.loadUrl(path);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initData() {
        wb_stock_recall.loadUrl(path);
        WebSettings webSettings = wb_stock_recall.getSettings();
        webSettings.setJavaScriptEnabled(true);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        showAnimation();
                    }
                });
            }
        };
        timer.schedule(task, 0, 5000);
    }


    private void showAnimation() {
        anim =ObjectAnimator.ofFloat(iv_up, "alpha", 1f, 0.1f, 1f, 0.1f, 1f);
        anim.setDuration(5000);// 动画持续时间
        anim.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_stock_recall;
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
