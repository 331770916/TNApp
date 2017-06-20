package com.tpyzq.mobile.pangu.log;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogHelper {

    public static boolean enableDefaultLog = false;

    private static final int RETURN_NOLOG = -1;

    public static int i(String tag, String msg) {
        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.i(tag, msg) : RETURN_NOLOG;
    }

    public static int d(String tag, String msg) {

        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.d(tag, msg) : RETURN_NOLOG;
    }

    public static int e(String tag, String msg) {
        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.e("--TPY"+tag, msg) : RETURN_NOLOG;
    }

    public static int w(String tag, String msg) {
        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.w(tag, msg) : RETURN_NOLOG;
    }
}
