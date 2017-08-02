package com.tpyzq.mobile.pangu.util.keyboard;

import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;
import com.yzd.unikeysdk.UnikeyUrls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wangqi on 2017/6/8.
 */

public class PasswordKeyboardUtils {


    /**
     * 获取加密键盘
     */
    public static boolean getKeyBoard(UnikeyUrls unikeyUrls, String diviceId) {
        try {
            UniKey.getInstance().init(CustomApplication.getContext(), unikeyUrls, diviceId);
            //获取加密键盘插件是否存在
            boolean pluginExist = UniKey.getInstance().isPluginExist();
            return pluginExist;
        } catch (UnikeyException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void hideSoftKeyboard(EditText editText, Context context ,boolean is) {
        if (editText != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            if (is){
                setshow(context,editText);
            }
        }
    }

    public static void showSoftKeyboar(EditText editText, Context context) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.findFocus();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 显示输入法
    }


    private static void setshow(Context context,EditText editText){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//                passwordEt.setInputType(0);
        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            //4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            //4.0
            methodName = "setSoftInputShowOnFocus";
        }

        if (methodName == null) {
            editText.setInputType(0);
        }else{
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (NoSuchMethodException e) {
                editText.setInputType(0);
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}
