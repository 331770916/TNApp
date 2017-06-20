package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;


/**
 * Created by zhangwenbo on 2016/9/6.
 * 注册设备提示Dialog
 */
public class SuccessDialog {

    private static SuccessDialog mMistakeDialog;

    private SuccessDialog() {
    }

    public static SuccessDialog getInstance() {
        if (mMistakeDialog == null) {
            mMistakeDialog = new SuccessDialog();
        }

        return mMistakeDialog;
    }


    public Dialog singleDialog(String msg, final Activity activity, final MistakeDialog.MistakeDialgoListener mistakeDialgoListener) {
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

                closeDialog(dialog, activity);
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

                closeDialog(dialog, activity);
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
            Intent intent = activity.getIntent();
            if (intent != null) {
                int tt = intent.getIntExtra("pageindex", 0);
//                LogHelper.e("test-----","tt:"+tt);
            } else {
                intent = new Intent();
            }
            UserUtil.refrushUserInfo();
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                intent.setClass(activity, TransactionLoginActivity.class);
            } else if("3".equals(KeyEncryptionUtils.getInstance().Typescno())){
                intent.setClass(activity, ShouJiVerificationActivity.class);
            }
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public interface MistakeDialgoListener {
        void doPositive();
    }
}
