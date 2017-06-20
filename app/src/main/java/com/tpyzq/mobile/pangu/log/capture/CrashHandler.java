package com.tpyzq.mobile.pangu.log.capture;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.util.Base64;

import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by zhangwenbo on 2016/5/17.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = CrashHandler.class.getName();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private static CrashHandler mCrashHandler;
    private Context             mContext;

    private String              mAppVersionName;
    private String              mAppVersionCode;

    private String              mOsVersion;  //系统版本号
    private String              mVendor;     //生产厂家
    private String              mModel;      //手机型号
    private String              mMid;



    private CrashHandler(Context context) {
        mContext = context.getApplicationContext();
        mAppVersionName = "app版本名称 : " + APPInfoUtils.getVersionName(context);
        mAppVersionCode = "app版本号   : " + APPInfoUtils.getVersionName(context);

        mOsVersion = "系统版本号:" + Build.VERSION.RELEASE;
        mVendor    = "生产厂家:" + Build.MANUFACTURER;
        mModel     = "手机型号 :" + Build.MODEL;

        mMid       = "imei号加其他的号" + DeviceUtil.getMid(context);


    }

    public static CrashHandler getInstance(Context context) {

        if (context == null) {
            LogHelper.e(TAG, "Context is null !");
            return null;
        }

        if (mCrashHandler == null) {
            mCrashHandler = new CrashHandler(context);
        }

        return mCrashHandler;
    }

    public void init() {
        if (mContext == null) {
            return;
        }

        boolean hasPermission = DeviceUtil.hasPermission(mContext);

        if (!hasPermission) {
            return;
        }

        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        handleException(ex);
        ex.printStackTrace();

        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    public void handleException(Throwable ex) {
        String _crashInfo = fomatCrashInfo(ex);
        LogHelper.d(TAG, _crashInfo);

        LogFileStorage.getInstance(mContext).saveLogFile2Internal(_crashInfo);
        if(ConstantUtil.LOG_DEBUG){
            LogFileStorage.getInstance(mContext).saveLogFile2SDcard(_crashInfo, true);
        }
    }

    /**
     * 格式化抓取的异常信息
     * @param ex
     * @return
     */
    private String fomatCrashInfo(Throwable ex) {

        String lineSeparator = "\r\n";

        StringBuilder sb = new StringBuilder();
        String logTime = "logTime:" + DeviceUtil.getCurrentTime();

        String exception = "exception:" + ex.toString();

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        String dump = info.toString();
        String crashMD5 = "crashMD5:"
                + DeviceUtil.getMD5Str(dump);

        String crashDump = "crashDump:" + "{" + dump + "}";
        printWriter.close();


        sb.append("&start---").append(lineSeparator);
        sb.append(logTime).append(lineSeparator);
        sb.append(mAppVersionName).append(lineSeparator);
        sb.append(mAppVersionCode).append(lineSeparator);
        sb.append(mOsVersion).append(lineSeparator);
        sb.append(mVendor).append(lineSeparator);
        sb.append(mModel).append(lineSeparator);
        sb.append(mMid).append(lineSeparator);
        sb.append(exception).append(lineSeparator);
        sb.append(crashMD5).append(lineSeparator);
        sb.append(crashDump).append(lineSeparator);
        sb.append("&end---").append(lineSeparator).append(lineSeparator)
                .append(lineSeparator);

        String _info = "";

        if (ConstantUtil.LOG_IS_BASE64) {
            _info = Base64.encodeToString(sb.toString().getBytes(), Base64.NO_WRAP);
        } else {
            _info = sb.toString();
        }



        return _info;

    }
}
