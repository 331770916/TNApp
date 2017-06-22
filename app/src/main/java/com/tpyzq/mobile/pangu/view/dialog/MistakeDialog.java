package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by zhangwenbo on 2016/9/6.
 * 错误提示Dialog
 */
public class MistakeDialog {

    public static Dialog showDialog(Object msg, final Activity activity, final MistakeDialgoListener mistakeDialgoListener) {
        if (activity==null||activity.isFinishing())return null;
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

        if (msg instanceof String) {

            String msgData = (String) msg + "(" + activity.getResources().getString(R.string.dh1) + activity.getResources().getString(R.string.dh) + ")";
            SpannableString spanText = new SpannableString(msgData);
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#1C86EE"));
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    getPermission(activity);
                }
            }, msgData.length() - 6, msgData.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            textView.setText(spanText);
            textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件


        } else if (msg instanceof SpannableString) {
            String msgData = (SpannableString) msg + "(" + activity.getResources().getString(R.string.dh1) + activity.getResources().getString(R.string.dh) + ")";
            SpannableString spanText = new SpannableString(msgData);
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#1C86EE"));
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    getPermission(activity);

                }
            }, msgData.length() - 6, msgData.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            textView.setText(spanText);
            textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        }

        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false; //默认返回 false
                }
            }
        });
        view.findViewById(R.id.mistakePositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mistakeDialgoListener) {
                    mistakeDialgoListener.doPositive();
                }

                closeDialog(dialog);
            }
        });
        if (!Helper.isActivityRunning(activity,activity.getClass().getName())){
            return null;
        }
        if (!activity.isFinishing()) {
            dialog.show();
        }

        return dialog;
    }






    public static Dialog specialshowDialog(Object msg, final Activity activity, final MistakeDialgoListener mistakeDialgoListener) {
        if (activity == null||activity.isFinishing()) {
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

        if (msg instanceof String) {

            String msgData = (String) msg + "(" + activity.getResources().getString(R.string.dh1) + activity.getResources().getString(R.string.dh) + ")";
            SpannableString spanText = new SpannableString(msgData);
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#1C86EE"));
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    getPermission(activity);
                }
            }, msgData.length() - 6, msgData.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            textView.setText(spanText);
            textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件


        } else if (msg instanceof SpannableString) {
            String msgData = (SpannableString) msg + "(" + activity.getResources().getString(R.string.dh1) + activity.getResources().getString(R.string.dh) + ")";
            SpannableString spanText = new SpannableString(msgData);
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#1C86EE"));
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    getPermission(activity);

                }
            }, msgData.length() - 6, msgData.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            textView.setText(spanText);
            textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        }

        final Dialog dialog = new Dialog(activity, R.style.misTakedialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false; //默认返回 false
                }
            }
        });
        view.findViewById(R.id.mistakePositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mistakeDialgoListener) {
                    mistakeDialgoListener.doPositive();
                }

                closeDialog(dialog);
            }
        });
        dialog.show();

        return dialog;
    }

    private static void getPermission(Activity activity) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:" + activity.getResources().getString(R.string.dh)));
//        activity.startActivity(intent);
        if (activity == null || activity.isFinishing()) return;
        ServiceDialog dialog = new ServiceDialog(activity);
        dialog.show();
    }

    public static Dialog showDialog(Object msg, Activity activity) {
        if (activity == null || activity.isFinishing()) return null;

        return showDialog(msg, activity, null);
    }

    public static void closeDialog(Dialog dialog) {
        if (dialog.getContext() instanceof ContextWrapper){
            if(((ContextWrapper)dialog.getContext()).getBaseContext() instanceof Activity
                    &&((Activity)(((ContextWrapper)dialog.getContext()).getBaseContext())).isFinishing()){
                return;
            }
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public interface MistakeDialgoListener {
        void doPositive();
    }
}
