package com.tpyzq.mobile.pangu.view.keybody;

import android.content.Context;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Field;

/**
 * Created by zhangwenbo on 2016/11/2.
 */
public class CustomGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private boolean ShowKeyboard;
    private ViewGroup mViewGroup;
    private Context mContext;

    public CustomGlobalLayoutListener(ViewGroup mianLayout, Context context) {
        mViewGroup = mianLayout;
        mContext = context;
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        mViewGroup.getWindowVisibleDisplayFrame(r);
        // 键盘最小高度
        int minKeyboardHeight = 150;
        // 获取状态栏高度
        int statusBarHeight = getStatusBarHeight(mContext);
        // 屏幕高度,不含虚拟按键的高度
        int screenHeight = mViewGroup.getRootView().getHeight();
        // 在不显示软键盘时，height 等于状态栏的高度
        int height = screenHeight - (r.bottom - r.top);


        if (ShowKeyboard) {
            // 如果软键盘是弹出的状态，并且 height 小于等于状态栏高度，
            // 说明这时软键盘已经收起
            if (height - statusBarHeight < minKeyboardHeight) {
                ShowKeyboard = false;
            }
        } else {
            // 如果软键盘是收起的状态，并且 height 大于状态栏高度，
            // 说明这时软键盘已经弹出
            if (height - statusBarHeight > minKeyboardHeight) {
                ShowKeyboard = true;
            }
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
