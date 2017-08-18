package com.tpyzq.mobile.pangu.activity;
/**
 * 类说明：这是一个webView的基类，子类需要继承，并且在view中加入布局，
 * 通过自定义url设置加载地址，通过自定义判断跳转不同的页面
 * Created by GongChen on 2014/12/31.
 */

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.Helper;

@SuppressLint("SetJavaScriptEnabled")
public abstract class WebViewActivity extends BaseActivity {
    public WebView mWebView = null;
    public ProgressBar mProgressBar;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private View vLoadingLayout;
    private View vNetWorkError;
    private ViewStub viewStub;
    public boolean isGoBack = true;
    private ImageView ivloadingAnim;
    private String mTitle;
    private FrameLayout ff_net_error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //子类继承父类，需要<include layout="@layout/activity_web"/>
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackOrFinish();
            }
        });
    }

    private void goBackOrFinish() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            isGoBack = true;
        } else {
            doSomeBeforeFinish();
            finish();
        }
    }

    protected abstract void doSomeBeforeFinish();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.loadUrl("about:blank");
        mWebView.destroy();
    }

    /**
     *  需要和js交互调用此方法，子类中需要和js交互调用此方法
     * @param obj 类名
     * @param interfaceName 别名，js中使用window.别名.obj类中的方法名()
     * @param url 本地html地址,如果有就加载，没有就不显示传入null就可以了
     */
    public boolean loadJs(Object obj,String interfaceName,String url){
        boolean bRet = false;
        try {
            mWebView.addJavascriptInterface(obj,interfaceName);
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

    //子类需要掉用此初始化方法
    public boolean initFatherWidget(){
        boolean bRet = false;
        try {
            mWebView = (WebView) findViewById(R.id.webview);
            mProgressBar = (ProgressBar) findViewById(R.id.pb);
//            vLoadingLayout = findViewById(R.id.rl_loading_layout);
            viewStub = (ViewStub)findViewById(R.id.vs_net_work_error);
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            mWebView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    doByTitle(title);
                    mTitle = title;
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100) {
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
            });

            //下拉刷新
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    showOnNetWorkState();
                    webViewShow();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

            //透明进度条
            mSwipeRefreshLayout.setColorScheme(R.color.translucent,R.color.translucent,R.color.translucent,R.color.translucent);
            // JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            // 更强的打开链接控制：自己覆写一个WebViewClient类：除了指定链接从WebView打开，其他的链接默认打开
            mWebView.setWebViewClient(new SubWebViewClient());
            webViewShow();
            bRet = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return bRet;
        }

    }
    /**
     * 禁用下拉刷新的方法
     */
    public boolean setFreshAble(boolean isFresh){
        boolean bRet = false ;
        try{
            if (isFresh){
                mSwipeRefreshLayout.setEnabled(true);
            }else{
                mSwipeRefreshLayout.setEnabled(false);
            }
        }catch (Exception e){

        }finally {
            return bRet;
        }

    }
    /**
     * 加载当前的webView,可以区分网络状态加载本地的html地址
     if (TextUtils.isEmpty(getNetState(getApplicationContext()))) {
     mWebView.loadUrl("file:///android_asset/XX.html");
     } else {
     mWebView.loadUrl("http://www.baidu.com/");
     }
     }*/
    public abstract boolean webViewShow();

    /**
     * 终端和网页交互，当网页发生点击出现重定向时，可以得到地址的变化，可以做一些我们想做的事情
     * 例如：
     //            if (Uri.parse(url)
     //                    .getHost()
     //                    .equals("http://www.cnblogs.com/mengdd/archive/2013/02/27/2935811.html")
     //                    || Uri.parse(url).getHost()
     //                    .equals("http://music.baidu.com/")) {
     //                return false;
     //            }
     //            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
     //            startActivity(intent);
     */
    protected abstract boolean showViewChanged(WebView view, String url);

    /**
     * 根据h5页面传入的title值进行ui操作 目前使用位置：社团详情页面的发布按钮 活动页面的评论按钮
     * @param mTitle
     */
    protected boolean doByTitle(String mTitle) {
        return true;
    }

    /**
     * 自定义的WebViewClient类，将特殊链接从WebView打开，其他链接仍然用默认浏览器打开
     *
     * @author 1
     */
//    Date startDate;
    private class SubWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            showViewChanged(view, url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            startDate   =   new   Date(System.currentTimeMillis());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            Date   endDate   =   new   Date(System.currentTimeMillis());
//            long diff = endDate.getTime() - startDate.getTime();
//            Log.e("wapTime", "网页加载时间：   " + String.valueOf(diff) + "毫秒");
            showOnNetWorkState();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
//            PubUtil.getInstance().writeLog(errorCode+"---"+description);
            showNetWorkError();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            /*super.onReceivedSslError(view, handler, error);*/
            handler.proceed();
        }
    }


    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack() && !isGoBack) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
//            mWebView.goBack();
//            isGoBack = true;
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    protected void showOnNetWorkState() {
        if(Helper.isNetWorked()) {
            dismissNetWorkError();
        } else {
            showNetWorkError();
        }
    }

    protected void showNetWorkError() {
        if(null == vNetWorkError) {
            vNetWorkError = viewStub.inflate();
        } else {
            vNetWorkError.setVisibility(View.VISIBLE);
        }
        ff_net_error = (FrameLayout) vNetWorkError.findViewById(R.id.ff_net_error);
        ff_net_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });
    }

    protected void dismissNetWorkError() {
        if(null != vNetWorkError) {
            vNetWorkError.setVisibility(View.INVISIBLE);
        }
    }

}