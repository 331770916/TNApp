package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.webkit.WebView;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by 张彪 on 2017/7/12.
 * 资金流入流出webview
 */

public class CapitalWebView extends BaseStockDetailPager {
    private WebView webView;

    public CapitalWebView(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    @Override
    public void initData(String stockCode) {
        //
        String code = "";
        if (stockCode.startsWith("1")){
            code = stockCode.substring(2)+".SS";
        }else if(stockCode.startsWith("2")){
            code = stockCode.substring(2)+".SZ";
        }
        webView.loadUrl("https://spv2ei636.lightyy.com/index.html?code="+code);
    }


//    @Override
//    protected View initView() {
//        WebView webView = new WebView(mContext);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        webView.loadUrl("https://spv2ei636.lightyy.com/index.html?code=600570.SS");
//        return webView;
//    }

    @Override
    public int getLayoutId() {
        return R.layout.pager_webview;
    }

}
