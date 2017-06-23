package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by zhangwenbo on 2017/5/18.
 * 退出页面提示框
 */

public class CancelDialog {

    public static void cancleDialog(final Activity activity, String message, final PositiveClickListener clickListener){
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_cancel, null);
        TextView tv_message = (TextView) view.findViewById(R.id.cancel_dialog_msg);
        if (!TextUtils.isEmpty(message)) {
            tv_message.setText(message);
        }
        view.findViewById(R.id.cancel_dialog_positiveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) {
                    alertDialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.finishAfterTransition();
                    } else {
                        activity.finish();
                    }
                } else {
                    clickListener.onPositiveClick();
                    alertDialog.dismiss();

                }

            }
        });

        view.findViewById(R.id.cancel_dialog_nagtiveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void cancleDialog(final Activity activity,  PositiveClickListener clickListener) {
        cancleDialog(activity, null,  clickListener);
    }

    public static void cancleDialog(final Activity activity, String message) {
        cancleDialog(activity, message,  null);
    }

    public static void cancleDialog(final Activity activity) {
        cancleDialog(activity, null,  null);
    }

    public interface PositiveClickListener {
        public void onPositiveClick();
    }

}
