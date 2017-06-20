package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by wangqi on 2016/9/19.
 */
public class LoadingDialog {


    public static Dialog initDialog(final Activity activity, String msg) {
        // 首先得到整个View
        View view = LayoutInflater.from(activity).inflate(R.layout.loading_dialog_view, null);

        // 获取整个布局
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.dialog_view);
        // 页面中显示文本
        TextView loadingText = (TextView) view.findViewById(R.id.loading_text);
        // 显示文本
        loadingText.setText(msg);
        // 创建自定义样式的Dialog
        Dialog dialog = new Dialog(activity, R.style.loading_dialog);

        //给返回键  添加监听 ， 点击的时候  销毁当前界面
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (dialog != null){
                        dialog.dismiss();
                    }
//                    ((Activity) activity).finish();
                }
                return false;
            }
        });
        // 设置返回键无效（点击dialog 其他位置无效）
        dialog.setCancelable(false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(layout, lp);

        return dialog;
    }


    public static Dialog initShareDialog(final Activity activity, String msg) {
        if (!Helper.isActivityRunning(activity, activity.getLocalClassName())) {
            return null;
        }
        // 首先得到整个View
        View view = LayoutInflater.from(activity).inflate(R.layout.loading_dialog_view, null);

        // 获取整个布局
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.dialog_view);
        // 页面中显示文本
        TextView loadingText = (TextView) view.findViewById(R.id.loading_text);
        // 显示文本
        loadingText.setText(msg);
        // 创建自定义样式的Dialog
        Dialog dialog = new Dialog(activity, R.style.loading_dialog);

        //给返回键  添加监听 ， 点击的时候  销毁当前界面
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });
        // 设置返回键无效（点击dialog 其他位置无效）
        dialog.setCancelable(false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(layout, lp);

        return dialog;
    }

}
