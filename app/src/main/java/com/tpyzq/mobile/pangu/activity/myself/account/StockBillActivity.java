package com.tpyzq.mobile.pangu.activity.myself.account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.JSAPI;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.tpyzq.mobile.pangu.view.webview.ProgressWebView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 股市月账单
 */
public class StockBillActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "StockBillActivity";
    ProgressWebView wb_stock_bill;
    private JSAPI mJsapi;
    private String mURL;
    private Dialog loadingDialog, HC_LoadingDialog;
    private RelativeLayout layout;
    private ShareDialog shareDialog;
    @Override
    public void initView() {

        initJSAPI();
        wb_stock_bill = (ProgressWebView) findViewById(R.id.wb_stock_bill);
        layout = (RelativeLayout) findViewById(R.id.stockNewsLayout);
        layout.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_fenxiang).setOnClickListener(this);
        UserUtil.refrushUserInfo();
        shareDialog = new ShareDialog(this);
        toConnect();
    }

    /**
     * 月账单
     */
    private void toConnect() {
        final HashMap map = new HashMap();
        map.put("FUNCTIONCODE", "HQFNG003");
        HashMap map1 = new HashMap();
        map.put("PARAMS", map1);
        map1.put("code", UserUtil.capitalAccount);
//                                                              URL_H5_New  URL_H5
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_S_BB(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                setShow();
                Helper.getInstance().showToast(StockBillActivity.this, "网络异常");
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
                        if (HC_LoadingDialog != null) {
                            HC_LoadingDialog.dismiss();
                        }
                        setData(msg);
                        mURL = msg;
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        startActivity(new Intent(StockBillActivity.this, TransactionLoginActivity.class));
                    } else {
                        setShow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setData(String url) {

        WebSettings setting = wb_stock_bill.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wb_stock_bill.setScrollbarFadingEnabled(true);
        wb_stock_bill.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wb_stock_bill.requestFocus();
        wb_stock_bill.addJavascriptInterface(mJsapi, "Android");
        wb_stock_bill.removeJavascriptInterface("searchBoxJavaBridge_");
        wb_stock_bill.removeJavascriptInterface("accessibility");
        wb_stock_bill.removeJavascriptInterface("accessibilityTraversal");
        wb_stock_bill.getSettings().setSavePassword(false);
        wb_stock_bill.loadUrl(url);

    }


    private void initJSAPI() {
        mJsapi = JSAPI.getInctance();
        mJsapi.setActivity(StockBillActivity.this);
        mJsapi.setHandler(handler);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
//                wb_stock_bill.loadUrl("javascript:goBack()");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


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
                toConnect();
                wb_stock_bill.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                break;
        }
    }

    private void setShow() {
        if (HC_LoadingDialog != null) {
            HC_LoadingDialog.dismiss();
        }
        wb_stock_bill.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }


    /**
     * 截屏 获取图片
     */
    private void getShare() {
        loadingDialog.dismiss();
        shareDialog.setUrl(mURL+"?hidden=1");
        shareDialog.show();

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_stock_bill;
    }
}
