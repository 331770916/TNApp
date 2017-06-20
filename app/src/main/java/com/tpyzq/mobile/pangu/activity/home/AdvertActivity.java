package com.tpyzq.mobile.pangu.activity.home;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;


public class AdvertActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    ImageView iv_share;
    WebView wv_share;
    String url1,url2;
    ShareDialog dialog;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        wv_share = (WebView) findViewById(R.id.wv_share);
        initData();
    }

    private void initData() {
        dialog = new ShareDialog(this);
        iv_back.setOnClickListener(this);
        url1 = "http://bj-tnhq.tpyzq.com/APPMAP";
        url2 = "http://bj-tnhq.tpyzq.com/APPMAP/?1=1";
        wv_share.loadUrl(url1);
        wv_share.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv_share.getSettings().setJavaScriptEnabled(true);
        wv_share.getSettings().setSavePassword(false);
        iv_share.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_advert;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                dialog.setUrl(url2);
                dialog.show();
                break;
        }
    }
}
