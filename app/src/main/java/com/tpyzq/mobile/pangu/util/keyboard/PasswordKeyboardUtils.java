package com.tpyzq.mobile.pangu.util.keyboard;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;
import com.yzd.unikeysdk.UnikeyUrls;

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


    public static void hideSoftKeyboard(EditText editText, Context context) {
        if (editText != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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
}
