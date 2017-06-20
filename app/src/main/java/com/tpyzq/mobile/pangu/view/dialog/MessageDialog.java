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
 * Created by zhangwenbo on 2016/9/16.
 * 信息Dialog
 */
public class MessageDialog {

    public static Dialog showDialog(String msg, Activity activity, final PositiveCliceListener positiveCliceListener,final CancleClickListener cancleClickListener) {

        if (!Helper.isActivityRunning(activity, activity.getLocalClassName())) {
            return null;
        }

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LayoutInflater inflater = LayoutInflater.from(CustomApplication.getContext());
        final View view = inflater.inflate(R.layout.dialog_message, null);

        int minWidth = (int) (width * 0.7);
        int minHeight = (int)(height * 0.3);

        view.setMinimumWidth(minWidth);
        view.setMinimumHeight(minHeight);

        final TextView textView = (TextView) view.findViewById(R.id.mistakeMsg);
        textView.setText(msg);

        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        view.findViewById(R.id.msgPositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog(dialog);

                if (positiveCliceListener != null) {
                    positiveCliceListener.positiveClick(v);
                }
            }
        });

        view.findViewById(R.id.msgCancle).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                closeDialog(dialog);
                if (cancleClickListener != null) {
                    cancleClickListener.cancleClick(v);
                }
            }
        });

        dialog.show();


        return  dialog;
    }

    public static void closeDialog(Dialog dialog){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public interface PositiveCliceListener{
        void positiveClick(View v);
    }

    public interface CancleClickListener{
        void cancleClick(View v);
    }
}
