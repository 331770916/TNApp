package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;


/**
 * Created by zhangwenbo on 2016/9/6.
 * 切换注册手机Dialog
 */
public class LoginDialog {
    public static Dialog showDialog(String msg, final Activity activity, final MistakeDialog.MistakeDialgoListener mistakeDialgoListener) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LayoutInflater inflater = LayoutInflater.from(CustomApplication.getContext());
        final View view = inflater.inflate(R.layout.dialog_mistake, null);

        int minWidth = (int) (width * 0.7);
        int minHeight = (int) (height * 0.3);

        view.setMinimumWidth(minWidth);
        view.setMinimumHeight(minHeight);

        final TextView textView = (TextView) view.findViewById(R.id.mistakeMsg);
        textView.setText(msg);

        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        view.findViewById(R.id.mistakePositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mistakeDialgoListener) {
                    if (null!=dialog && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    mistakeDialgoListener.doPositive();
                } else {
                    closeDialog(dialog, activity);
                }
            }
        });
        dialog.show();
        return dialog;
    }

    public static Dialog showDialog(String msg, Activity activity) {

        return showDialog(msg, activity, null);
    }

    public static void closeDialog(Dialog dialog, Activity activity) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            activity.finish();
        }
    }

    public interface MistakeDialgoListener {
        void doPositive();
    }
}
