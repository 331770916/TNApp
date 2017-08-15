package com.tpyzq.mobile.pangu.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by huweidong on 2017/08/15
 * email : huwwds@gmail.com
 */

public class ViewUtil {
    public static void unbindView(View v){
        ViewParent parent = v.getParent();
        if (parent==null)return;
        if (parent instanceof ViewGroup)
            ((ViewGroup)parent).removeAllViews();
    }
}
