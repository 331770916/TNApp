package com.tpyzq.mobile.pangu.log.capture;

import android.content.Context;
import android.util.Log;


import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.DeviceUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Log日志写入SDCard
 */
public class LogFileStorage {

    private static final String TAG        = "LogFileStorage";
    public static final  String LOG_SUFFIX = ".log";
    private static final String CHARSET    = "UTF-8";

    private static LogFileStorage mLogFileStorage;
    private Context               mContext;


    private LogFileStorage(Context context){
        mContext = context.getApplicationContext();
    }

    public static synchronized LogFileStorage getInstance(Context context) {

        if (context == null) {
            LogHelper.e(TAG, "Context is null");
            return null;
        }

        if (mLogFileStorage == null) {
            mLogFileStorage = new LogFileStorage(context);
        }

        return mLogFileStorage;
    }

    public boolean saveLogFile2Internal(String logString) {
        try {
            File dir = mContext.getFilesDir();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File logFile = new File(dir, DeviceUtil.getMid(mContext) + LOG_SUFFIX);
            FileOutputStream fos = new FileOutputStream(logFile , true);
            fos.write(logString.getBytes(CHARSET));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.e(TAG, "saveLogFile2Internal failed!");
            return false;
        }
        return true;
    }

    public boolean saveLogFile2SDcard(String logString, boolean isAppend) {
        if (!DeviceUtil.isSDcardExsit()) {
            LogHelper.e(TAG, "sdcard not exist");
            return false;
        }
        try {
            File logDir = getExternalLogDir();
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            File logFile = new File(logDir, DeviceUtil.getMid(mContext)
                    + LOG_SUFFIX);
			/*if (!isAppend) {
				if (logFile.exists() && !logFile.isFile())
					logFile.delete();
			}*/
            LogHelper.d(TAG, logFile.getPath());

            FileOutputStream fos = new FileOutputStream(logFile , isAppend);
            fos.write(logString.getBytes(CHARSET));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "saveLogFile2SDcard failed!");
            return false;
        }
        return true;
    }

    private File getExternalLogDir() {
        File logDir = DeviceUtil.getExternalDir(mContext, "Log");
        LogHelper.d(TAG, logDir.getPath());
        return logDir;
    }

}
