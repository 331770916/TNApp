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
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by wangqi on 2016/12/23.
 * 交易登录双向认证提示 退出交易登录
 */

public class MutualAuthenticationDialog {
    private static MutualAuthenticationDialog mMistakeDialog;

    private MutualAuthenticationDialog() {
    }

    public static MutualAuthenticationDialog getInstance() {
        if (mMistakeDialog == null) {
            mMistakeDialog = new MutualAuthenticationDialog();
        }

        return mMistakeDialog;
    }


    public Dialog singleDialog(String msg, final Activity activity, final MistakeDialog.MistakeDialgoListener mistakeDialgoListener) {

        if (!Helper.isActivityRunning(activity, activity.getLocalClassName())) {
            return null;
        }

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
                    mistakeDialgoListener.doPositive();
                }

                closeDialog(dialog,activity);
            }
        });

        return dialog;
    }


    public Dialog singleDialog(String msg, Activity activity) {

        return singleDialog(msg, activity, null);
    }


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
                    mistakeDialgoListener.doPositive();
                }

                closeDialog(dialog,activity);
            }
        });

        dialog.show();


        return dialog;
    }

    public static Dialog showDialog(String msg, Activity activity) {

        return showDialog(msg, activity, null);
    }

    public static void closeDialog(Dialog dialog,Activity activity) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        activity.finish();
    }

    public interface MistakeDialgoListener {
        void doPositive();
    }
}
