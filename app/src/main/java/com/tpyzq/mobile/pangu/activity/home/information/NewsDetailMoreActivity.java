package com.tpyzq.mobile.pangu.activity.home.information;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * 新闻详情web页
 */
public class NewsDetailMoreActivity extends BaseActivity implements View.OnClickListener {

    private WebView myWebView;


    @Override
    public void initView() {
        this.findViewById(R.id.ivNewsDetailMove_back).setOnClickListener(this);
        myWebView = (WebView) this.findViewById(R.id.wvNewDetalMove);
        initData();
    }

    private void initData() {
        String url = getIntent().getStringExtra("url");
        WebSettings webSettings = myWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        myWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        myWebView.removeJavascriptInterface("accessibility");
        myWebView.removeJavascriptInterface("accessibilityTraversal");
        myWebView.getSettings().setSavePassword(false);
        //加载需要显示的网页
        myWebView.loadUrl(url);
        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_detail_more;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivNewsDetailMove_back) {
            finish();
        }
    }
}
