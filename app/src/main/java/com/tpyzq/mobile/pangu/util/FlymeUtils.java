package com.tpyzq.mobile.pangu.util;

/**
 * Created by 33920_000 on 2017/8/24.
 */

// 检测Flyme
import android.os.Build;

import java.lang.reflect.Method;

public final class FlymeUtils {

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

}
