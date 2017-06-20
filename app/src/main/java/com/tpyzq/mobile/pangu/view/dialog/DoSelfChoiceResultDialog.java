package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by zhangwenbo on 2016/12/8.
 * 添加 或 删除 自选股弹框提示
 */

public class DoSelfChoiceResultDialog {

    private static DoSelfChoiceResultDialog mDoSelfChoiceResultDialog;
    private DoSelfChoiceResultDialog() {}

    public synchronized static DoSelfChoiceResultDialog getInstance() {
        if (mDoSelfChoiceResultDialog  == null) {
            mDoSelfChoiceResultDialog = new DoSelfChoiceResultDialog();
        }
        return  mDoSelfChoiceResultDialog;
    }


    public void singleDialog(String msg, Activity activity) {
        boolean flag = !Helper.isActivityRunning(activity, activity.getClass().getName());
        if (flag) {
            return ;
        }

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LayoutInflater inflater = LayoutInflater.from(CustomApplication.getContext());
        final View view = inflater.inflate(R.layout.dialog_doselfchoice, null);

        int minWidth = (int) (width * 0.3);
        int minHeight = (int)(height * 0.1);

        view.setMinimumWidth(minWidth);
        view.setMinimumHeight(minHeight);

        final TextView textView = (TextView) view.findViewById(R.id.doSelfChoiceResultMsg);
        textView.setText(msg);

        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeDialog(dialog);
            }
        }, 1000);
    }

    public static void closeDialog(Dialog dialog){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
