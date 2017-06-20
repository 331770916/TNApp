package com.tpyzq.mobile.pangu.util.keyboard;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.view.keybody.PopKeyBody;
import com.yzd.unikeysdk.UnikeyException;
import com.yzd.unikeysdk.UnikeyUrls;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangwenbo on 2016/11/26.
 * 使用键盘的工具类， 银证转账模块专用
 */

public class UsefulKeyBoard {

    private static final String TAG = "UsefulKeyBoard";
    private static UsefulKeyBoard mUsefulKeyBoard;

    private UsefulKeyBoard(){

    };

    public static UsefulKeyBoard getInstance() {

        if (mUsefulKeyBoard == null) {
            mUsefulKeyBoard = new UsefulKeyBoard();
        }

        return mUsefulKeyBoard;
    }


    /**
     *  初始化键盘配置
     * @param userCustomKeyBoard 如果插件键盘认证失败， 判断使用自定义键盘还是系统键盘，
     *                           true 使用自定义键盘， false使用系统键盘
     *
     */
    public Object initUserKeyBoard(boolean userCustomKeyBoard, PopKeyBody.ContentListener listener,
                                   boolean isNeedInputPassword, EditText editText) {

        if ("false".equals(Db_PUB_USERS.queryingCertification())) {
            //认证失败
            //调用 系统键盘 还是自定义键盘
            if (userCustomKeyBoard) {
                //true 使用自定义键盘
                PopKeyBody popKeyBody = new PopKeyBody(listener, isNeedInputPassword);

                return popKeyBody;
            } else {
                //false使用系统键盘

                openInputMethod(editText);

                return null;
            }

        } else {
            String diviceId = DeviceUtil.getDeviceId(CustomApplication.getContext());
            try {
                //初始化加密键盘
                UnikeyUrls unikeyUrls = new UnikeyUrls(Constants.APPLY_PLUGIN, Constants.GET_CHALLEAGE,
                        Constants.VERIFY_CODE, Constants.GET_BIT_AUTH_DATA,
                        Constants.CHECK_BIT_AUTH_DATA, Constants.VERIFY_APP);
            } catch (UnikeyException e) {
                e.printStackTrace();
                LogHelper.e(TAG, e.toString());
            }

            return null;
        }
    }


    /**
     * @throws
     * @MethodName:openInputMethod
     * @Description:打开系统软键盘
     */

    private void openInputMethod(final EditText editText) {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            public void run() {

                InputMethodManager inputManager = (InputMethodManager) editText

                        .getContext().getSystemService(

                                Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(editText, 0);

            }

        }, 200);
    }

}
