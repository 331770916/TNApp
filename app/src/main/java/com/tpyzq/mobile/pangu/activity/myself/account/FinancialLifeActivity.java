package com.tpyzq.mobile.pangu.activity.myself.account;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.OpeningAnAccountDialog;
import com.tpyzq.mobile.pangu.view.webview.ProgressWebView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/11/4.
 * 金融生活
 */
public class FinancialLifeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "FinancialLife";
    private ScrollView relativeLayout_item;
    private FrameLayout iv_isEmpty;
    private ProgressWebView webView;
    private Dialog mLoadDialog;
    private Button mSiKu_but;
    private TextView mKa;
    private LinearLayout mKaiHu_but;
    private LinearLayout mLonIn_but;
    private String url;
    private RelativeLayout el_biaoti;


    @Override
    public void initView() {
        UserUtil.refrushUserInfo();
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_back_finish).setOnClickListener(this);
        findViewById(R.id.AppDownload).setOnClickListener(this);
        el_biaoti = (RelativeLayout) findViewById(R.id.el_biaoti);
        relativeLayout_item = (ScrollView) findViewById(R.id.RelativeLayout_item);
        iv_isEmpty = (FrameLayout) findViewById(R.id.kong);
        webView = (ProgressWebView) findViewById(R.id.wb_assets_analysis);
        mKaiHu_but = (LinearLayout) findViewById(R.id.KaiHu_but);   //开户button
        mLonIn_but = (LinearLayout) findViewById(R.id.LonIn_but);   //登录button
        mSiKu_but = (Button) findViewById(R.id.SiKu_but);           //寺库Button
        mKa = (TextView) findViewById(R.id.tv_ka);                  //客户级别
    }

    private void initLogic() {
        if (!TextUtils.isEmpty(UserUtil.capitalAccount)) {
            mSiKu_but.setVisibility(View.VISIBLE);
            mKaiHu_but.setVisibility(View.GONE);
            mLonIn_but.setVisibility(View.GONE);
        } else {
            mSiKu_but.setVisibility(View.GONE);
            mKaiHu_but.setVisibility(View.VISIBLE);
            mLonIn_but.setVisibility(View.VISIBLE);
        }


        mKaiHu_but.setOnClickListener(this);
        mLonIn_but.setOnClickListener(this);
        mSiKu_but.setOnClickListener(this);
        iv_isEmpty.setOnClickListener(this);
    }

    /**
     * 客户等级
     */
    private void toGradeLevel() {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("FUNCTIONCODE", "HQTNG103");
        map.put("PARAMS", map1);
        map1.put("CUST_ID", UserUtil.capitalAccount);
        map.put("TOKEN", "");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_RS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mLoadDialog != null) {
                    mLoadDialog.dismiss();
                }
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if (mLoadDialog != null) {
                    mLoadDialog.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    JSONObject message = object.getJSONObject("message");
                    if (!message.equals("") && message != null) {
                        String consum_lvl = message.getString("consum_lvl");
                        if ("200".equals(code)) {
                            mKa.setText(consum_lvl);
                            mKa.setTextColor(getResources().getColor(R.color.financiallife_text));
                        } else {
                            mKa.setText("--");
                            mKa.setTextColor(getResources().getColor(R.color.financiallife_text));
                        }
                    } else {
                        mKa.setText("--");
                        mKa.setTextColor(getResources().getColor(R.color.financiallife_text));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 寺库 请求URL
     */
    private void toConnect() {
//        [parm setValue:@"1021029121" forKey:@"cust_id"];
//        [dic setValue:@"HQSNG002" forKey:@"FUNCTIONCODE"];
//        [dic setValue:@"" forKey:@"TOKEN"];
//        [dic setValue:parm forKey:@"PARAMS"];
        el_biaoti.setVisibility(View.VISIBLE);
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("FUNCTIONCODE", "HQSNG003");//HQSNG003  HQSNG002
        map.put("TOKEN", "");
        map.put("PARAMS", map1);
        map1.put("cust_id", UserUtil.capitalAccount);//1021029121  UserUtil.capitalAccount

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_H5, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mLoadDialog != null) {
                    mLoadDialog.dismiss();
                }
                LogUtil.e(TAG, e.toString());
                relativeLayout_item.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                iv_isEmpty.setVisibility(View.VISIBLE);
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
                    url = jsonObject.getString("url");
                    if ("200".equals(code)) {

                        if (!TextUtils.isEmpty(msg) && !TextUtils.isEmpty(url)) {
                            setWebView(url, msg);
                        } else {
                            relativeLayout_item.setVisibility(View.GONE);
                            webView.setVisibility(View.GONE);
                            iv_isEmpty.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (mLoadDialog != null) {
                            mLoadDialog.dismiss();
                        }
                        relativeLayout_item.setVisibility(View.GONE);
                        webView.setVisibility(View.GONE);
                        iv_isEmpty.setVisibility(View.VISIBLE);
                        LogUtil.e(TAG, response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void setWebView(String url, String msg) {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setSavePassword(false);
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.setWebViewClient(new WebViewClient() {
            //新连接打开的处理
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }
                return true;
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            //返回键后退
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {  //表示按返回键
                        webView.goBack();   //后退
                        //webview.goForward();//前进
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(false);

        //TODO 调用后台接口返回加密后的参数和
        String parameter = msg;
//        String url = "https://m.kutianxia.com/kubaitiao/merchant";

        parameter = "xml=" + parameter;
        LogUtil.e("ccccccccc", parameter);
        try {
            webView.postUrl(url, parameter.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_financiallife;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                WebVIewfinish();
                break;
            case R.id.iv_back_finish:
                finish();
                break;
            case R.id.KaiHu_but:
                OpeningAnAccountDialog openingAnAccount = new OpeningAnAccountDialog(this);
                openingAnAccount.show();
                break;
            case R.id.LonIn_but:
                intent.putExtra("pageindex", TransactionLoginActivity.PAGE_INDEX_FINLIFE);
                if (!Db_PUB_USERS.isRegister()) {
                    intent.setClass(this, ShouJiZhuCeActivity.class);
                } else {
                    intent.setClass(this, TransactionLoginActivity.class);
                }
                startActivity(intent);
                finish();
                break;
            case R.id.SiKu_but:
                relativeLayout_item.setVisibility(View.GONE);
                toConnect();
                break;
            case R.id.kong:
                mLoadDialog = LoadingDialog.initDialog(this, "正在加载...");
                mLoadDialog.show();
                relativeLayout_item.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                toConnect();
                break;
            case R.id.AppDownload:
                Uri uri = Uri.parse("http://m.secoo.com/d/secoo");
                Intent Newintent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(Newintent);
                break;
        }
    }

    public void WebVIewfinish() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            el_biaoti.setVisibility(View.GONE);
            relativeLayout_item.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            initLogic();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(UserUtil.capitalAccount)) {
            mLoadDialog = LoadingDialog.initDialog(this, "正在加载...");
            mLoadDialog.show();
            toGradeLevel();
        } else {
            mKa.setText("--");
            mKa.setTextColor(getResources().getColor(R.color.financiallife_text));
        }
        initLogic();
    }
}
