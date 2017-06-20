package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;


/**
 * 作者：刘泽鹏 on 2016/10/17 15:41
 */
public class LoadingDialogTwo {


    public static Dialog initDialog(final Activity activity) {
        // 首先得到整个View
        View view = LayoutInflater.from(activity).inflate(R.layout.loading_dialog_view_two, null);

        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.lldialog_view_two);

        // 创建自定义样式的Dialog
        Dialog dialog = new Dialog(activity, R.style.loading_dialog);

        //给返回键  添加监听 ， 点击的时候  销毁当前界面
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK  && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                    activity.finish();
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
