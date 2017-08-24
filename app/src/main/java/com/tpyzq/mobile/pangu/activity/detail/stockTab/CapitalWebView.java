package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;

/**
 * Created by 张彪 on 2017/7/12.
 * 资金流入流出webview
 */

public class CapitalWebView extends BaseStockDetailPager implements Handler.Callback{
    private WebView webView;
    private Context context ;
    private Handler handler = new Handler(this);
    private String mHeight ;   // 全局高度  Js 回传webView 高度
    public CapitalWebView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setView() {
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JsInterface(context),"egos");
    }

    @Override
    public void initData(String stockCode) {
        //
//        String code = "";
//        if (stockCode.startsWith("1")){
//            code = stockCode.substring(2)+".SS";
//        }else if(stockCode.startsWith("2")){
//            code = stockCode.substring(2)+".SZ";
//        }
        String code =stockCode.substring(2);
        String url = "https://dx0pd8mgy.lightyy.com/capitalflow.html?s="+code+"&p=HSJY_1047";
        webView.loadUrl(url);
    }


    @Override
    public int getLayoutId() {
        return R.layout.pager_webview;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what==1){
            ViewGroup.LayoutParams  layoutParams = webView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height   = Helper.dip2px(context,Float.parseFloat(mHeight));
            webView.setLayoutParams(layoutParams);
        }
        return true;
    }

    public class JsInterface{
        private Context context;

        public JsInterface(Context context){
            this.context = context;
        }

        @JavascriptInterface
        public void returnHeight(String height){
            mHeight = height;
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

}
