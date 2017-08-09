package com.tpyzq.mobile.pangu.activity.myself.account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.JSAPI;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.tpyzq.mobile.pangu.view.webview.ProgressWebView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 交易动态
 */
public class TradingDynamicsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TradingDynamicsActivity";
    ProgressWebView wb_trading_dynamics;
    private JSAPI mJsapi;
    private HashMap<String, String> hashmap;
    private RelativeLayout layout;
    private String mURL;
    private Dialog loadingDialog, HC_LoadingDialog;
    private ShareDialog shareDialog;
    private ImageView iv_fenxiang;
    @Override
    public void initView() {
        initJSAPI();
        wb_trading_dynamics = (ProgressWebView) findViewById(R.id.wb_trading_dynamics);
        layout = (RelativeLayout) findViewById(R.id.stockNewsLayout);
        layout.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        iv_fenxiang = (ImageView)findViewById(R.id.iv_fenxiang);
        iv_fenxiang.setOnClickListener(this);
        shareDialog = new ShareDialog(this);
        toConnechistory();
    }

    /**
     * 历史成交
     */
    private void toConnechistory() {
        String mSession = SpUtils.getString(this, "mSession", "");
        final HashMap map = new HashMap();
        map.put("funcid", "300192");
        map.put("token", mSession);
        HashMap map1 = new HashMap();
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", true);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                Helper.getInstance().showToast(TradingDynamicsActivity.this, "网络异常");
                setShow();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    JSONArray data = object.getJSONArray("data");
                    if ("0".equals(code)) {
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                String total_buy_times = data.getJSONObject(i).getString("TOTAL_BUY_TIMES");
                                String total_sell = data.getJSONObject(i).getString("TOTAL_SELL");
                                String total_sell_times = data.getJSONObject(i).getString("TOTAL_SELL_TIMES");
                                String totle_buy = data.getJSONObject(i).getString("TOTLE_BUY");
                                JSONArray list = data.getJSONObject(i).getJSONArray("LIST");

                                List<HashMap<String, String>> list_map = new ArrayList<HashMap<String, String>>();
                                for (int j = 0; j < list.length(); j++) {
                                    hashmap = new HashMap();
                                    String entrust_bs = list.getJSONObject(j).getString("ENTRUST_BS");
                                    String secu_name = list.getJSONObject(j).getString("SECU_NAME");
                                    String secu_code = list.getJSONObject(j).getString("SECU_CODE");
                                    String matched_date = list.getJSONObject(j).getString("MATCHED_DATE");
                                    String matched_amt = list.getJSONObject(j).getString("MATCHED_AMT");

                                    hashmap.put("ENTRUST_BS", entrust_bs);
                                    hashmap.put("SECU_NAME", secu_name);
                                    hashmap.put("SECU_CODE", secu_code);
                                    hashmap.put("MARKET", matched_date);
                                    hashmap.put("MATCHED_AMT", matched_amt);
                                    list_map.add(hashmap);
                                }
                                toConnect(total_buy_times, total_sell, total_sell_times, totle_buy, list_map);
                            }
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(TradingDynamicsActivity.this, TransactionLoginActivity.class));
                    } else {
                        setShow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(TradingDynamicsActivity.this, "网络异常");
                }
            }
        });
    }

    /**
     * 交易记录
     */
    private void toConnect(String total_buy_times, String total_sell, String total_sell_times, String totle_buy, List<HashMap<String, String>> content_list) {

        HashMap map = new HashMap();
        map.put("FUNCTIONCODE", "HQFNG004");
        HashMap map1 = new HashMap();
        map.put("PARAMS", map1);
        map1.put("account", UserUtil.capitalAccount);
        map1.put("TOTAL_BUY_TIMES", total_buy_times);
        map1.put("TOTLE_BUY", totle_buy);
        map1.put("TOTAL_SELL_TIMES", total_sell_times);
        map1.put("TOTAL_SELL", total_sell);
        map1.put("content_list", content_list);
//                                                              URL_H5  URL_H5_New
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_S_BB(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Helper.getInstance().showToast(TradingDynamicsActivity.this, "网络异常");
                LogUtil.e(TAG, e.toString());
                setShow();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if ("0".equals(code)) {
                        iv_fenxiang.setVisibility(View.VISIBLE);
                        LogUtil.e("ccccc", msg);
                        if (!TextUtils.isEmpty(msg)) {
                            if (HC_LoadingDialog != null) {
                                HC_LoadingDialog.dismiss();
                            }
                            setData(msg);
                            mURL = msg;
                        } else {
                            setShow();
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        startActivity(new Intent(TradingDynamicsActivity.this, TransactionLoginActivity.class));
                    } else {
                        setShow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(TradingDynamicsActivity.this, "网络异常");
                }
            }
        });
    }

    private void setData(String url) {

        WebSettings setting = wb_trading_dynamics.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wb_trading_dynamics.setScrollbarFadingEnabled(true);
        wb_trading_dynamics.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wb_trading_dynamics.requestFocus();
        wb_trading_dynamics.addJavascriptInterface(mJsapi, "Android");
        wb_trading_dynamics.removeJavascriptInterface("searchBoxJavaBridge_");
        wb_trading_dynamics.removeJavascriptInterface("accessibility");
        wb_trading_dynamics.removeJavascriptInterface("accessibilityTraversal");
        wb_trading_dynamics.getSettings().setSavePassword(false);
        wb_trading_dynamics.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_fenxiang:
                loadingDialog = LoadingDialog.initDialog(this, "加载中…");
                loadingDialog.show();       //显示菊花
                getShare();
                break;
            case R.id.stockNewsLayout:
                HC_LoadingDialog = LoadingDialog.initDialog(this, "加载中…");
                HC_LoadingDialog.show();
                toConnechistory();
                wb_trading_dynamics.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                break;
        }
    }


    private void initJSAPI() {
        mJsapi = JSAPI.getInctance();
        mJsapi.setActivity(TradingDynamicsActivity.this);
        mJsapi.setHandler(handler);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            try {
//                wb_trading_dynamics.loadUrl("javascript:goBack()");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    /**
     * 截屏 获取图片
     */
    private void getShare() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        shareDialog.setUrl(mURL+"?hidden=1");
        shareDialog.show();

    }


    private void setShow() {
        if (HC_LoadingDialog != null) {
            HC_LoadingDialog.dismiss();
        }
        wb_trading_dynamics.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        iv_fenxiang.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trading_dynamics;
    }
}
