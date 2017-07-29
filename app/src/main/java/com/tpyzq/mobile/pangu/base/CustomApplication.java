package com.tpyzq.mobile.pangu.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tpyzq.mobile.pangu.BuildConfig;
import com.tpyzq.mobile.pangu.log.LogCollector;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.umeng.socialize.PlatformConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 *
 */

public class CustomApplication extends MultiDexApplication {
    private static final String TAG = "CustomApplication";
    private static Context mContext;
    private static GetMessageListenr mGetMessageListenr;
    private List<Activity> activityList = new LinkedList<Activity>();
    private static CustomApplication instance;

    public CustomApplication(){
    }

    // 单例模式中获取唯一 CustomApplication 实例
    public static CustomApplication getInstance() {
        if (null == instance) {
            instance = new CustomApplication();
        }
        return instance;

    }

    {
//        PlatformConfig.setWeixin("wx44d3f5e9067579e0", "3322c38bf14842f12091efe405fab53e");
        PlatformConfig.setWeixin("wxc946db8321e55417", "ba63f77bd3bcb6ff555e0d875b388dbb");
        PlatformConfig.setSinaWeibo("3094102517", "990fa3af9adc75dd07bee0eb30d9b6d8");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }



    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish

    public void finish() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        String site_json = SpUtils.getString(CustomApplication.getContext(),"site_json","");
        if (!TextUtils.isEmpty(site_json)) {
            ConstantUtil.SITE_JSON = site_json;
        } else {
            ConstantUtil.SITE_JSON = ConstantUtil.setSiteJson();
        }
        String ip = SpUtils.getString(mContext, "market_ip", null);
        String jy_ip = SpUtils.getString(mContext, "jy_ip", null);
        if (!TextUtils.isEmpty(ip) || !TextUtils.isEmpty(jy_ip)) {
            ConstantUtil.setUrl(ip, jy_ip);
        }
        //收集错误日志
        LogUtil.logInit(BuildConfig.DEBUG);
        LogCollector.setDebugMode(true);
        LogCollector.initLog(getApplicationContext());
        //初始化图片加载框架
        Fresco.initialize(this);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        /** 设置具体的证书 **/
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null);
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, 本地证书的inputstream, 本地证书的密码)
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(client);
        OkHttpUtils.setUserAgent(generateUserAgent());
        final IntentFilter homeFilter = new IntentFilter();
        homeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        homeFilter.addAction(Intent.ACTION_SCREEN_ON);
        homeFilter.addAction(Intent.ACTION_SCREEN_OFF);
        homeFilter.addAction(Intent.ACTION_USER_PRESENT);
    }

    public static String generateUserAgent(){
        //        user-agent   PanGu/1.7.4 (iPhone; iOS 10.3.2; Scale/3.00)

        StringBuilder sb=new StringBuilder();
        sb.append("PanGu/")
                .append(BuildConfig.VERSION_NAME)
                .append(" (Linux; Android ")
                .append(Build.VERSION.RELEASE)
                .append(";")
                .append(Build.MODEL)
                .append(")");
        return sb.toString();
    }

    public static Context getContext() {
        return mContext;
    }


    public static void setGetMessageListenr(GetMessageListenr getMessageListenr) {
        mGetMessageListenr = getMessageListenr;
    }

    /*接收消息数量的传递*/
    public interface GetMessageListenr {
        void getMessage(int message);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
