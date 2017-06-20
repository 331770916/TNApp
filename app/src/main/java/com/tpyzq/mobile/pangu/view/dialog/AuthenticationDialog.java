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
 * Created by wangqi on 2016/9/22.
 * 认证提示
 */
public class AuthenticationDialog {

    public static Dialog showDialog(String titles,String msg, String but, String but1, Activity activity, final AuthenticationPositiveCliceListener positiveCliceListener, final AuthenticationCancleClickListener cancleClickListener) {

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LayoutInflater inflater = LayoutInflater.from(CustomApplication.getContext());
        final View view = inflater.inflate(R.layout.dialog_certification, null);

        int minWidth = (int) (width * 0.7);
        int minHeight = (int) (height * 0.1);

        view.setMinimumWidth(minWidth);
        view.setMinimumHeight(minHeight);
        final TextView title = (TextView) view.findViewById(R.id.cTitle);
        title.setText(titles);
        final TextView textView = (TextView) view.findViewById(R.id.mistakeMsg);
        textView.setText(msg);

        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        final TextView cPositive = (TextView) view.findViewById(R.id.cPositive);
        cPositive.setText(but);
        cPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog(dialog);

                if (positiveCliceListener != null) {
                    positiveCliceListener.positiveClick(v);
                }
            }
        });

        TextView cCancle = (TextView) view.findViewById(R.id.cCancle);
        cCancle.setText(but1);
        cCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog(dialog);
                if (cancleClickListener != null) {
                    cancleClickListener.cancleClick(v);
                }
            }
        });

        dialog.show();


        return dialog;
    }



    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public interface AuthenticationPositiveCliceListener {
        void positiveClick(View v);
    }

    public interface AuthenticationCancleClickListener {
        void cancleClick(View v);
    }
}
