package com.tpyzq.mobile.pangu.activity;

import android.text.TextUtils;
import android.webkit.WebView;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by 33920_000 on 2017/8/12.
 */

public class TNWebActivity extends WebViewActivity {
    private String baseUtl;

    @Override
    protected void doSomeBeforeFinish() {

    }

    @Override
    public boolean webViewShow() {
        mWebView.loadUrl(baseUtl);
        return true;
    }

    @Override
    protected boolean showViewChanged(WebView view, String url) {
        boolean bRet = false;
        try {
            if(!TextUtils.isEmpty(url)){
                mWebView.loadUrl(url);
            }
            bRet = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return bRet;
        }
    }

    @Override
    public void initView() {
        baseUtl = getIntent().getStringExtra("jump");
        initFatherWidget();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tn_web;
    }
}
