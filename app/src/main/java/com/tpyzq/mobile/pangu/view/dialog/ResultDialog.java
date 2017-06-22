package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.TransitionUtils;


/**
 * Created by zhangwenbo on 2016/9/6.
 * 提交结果弹框
 */
public class ResultDialog {

    private static ResultDialog mResultDialog;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long towTime = 0;
    private static Context context;
    private Toast mToastNew;//new的吐司，实现自定义背景
    private Toast mToastNormal;//普通背景居中吐司

    private ResultDialog() {

    }

    public static ResultDialog getInstance() {
        if (mResultDialog == null) {
            mResultDialog = new ResultDialog();
        }

        return mResultDialog;
    }

    public void show(String content, @DrawableRes int resmipmap) {
        Context context = CustomApplication.getContext();
//        if (mToast == null) {
            int IMAGE = R.id.dialog_image;
            int TEXT = R.id.dialog_text;
//          mToast = Toast.makeText(CustomApplication.getContext(), Html.fromHtml(content), Toast.LENGTH_SHORT);
            mToastNew = new Toast(context);
//          Toast.makeText(CustomApplication.getContext(), content, Toast.LENGTH_SHORT);
            mToastNew.setGravity(Gravity.CENTER, 0, 0);
            RelativeLayout toastView = new RelativeLayout(context);
            toastView.setBackgroundResource(R.drawable.toasts);
            toastView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            RelativeLayout con = new RelativeLayout(context);
            con.setLayoutParams(new ViewGroup.LayoutParams(TransitionUtils.dp2px(240, context), TransitionUtils.dp2px(60, context)));
            toastView.addView(con);

            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(ContextCompat.getDrawable(context, resmipmap));
            imageView.setPadding(0, 0, 0, TransitionUtils.dp2px(30, context));
            imageView.setId(IMAGE);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp1.addRule(RelativeLayout.CENTER_IN_PARENT);
            imageView.setLayoutParams(lp1);
            toastView.addView(imageView);

            TextView textView = new TextView(context);
            textView.setText(content);
            textView.setTextSize(TransitionUtils.dp2px(7, context));
            textView.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.BELOW, IMAGE);
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            textView.setLayoutParams(lp2);
            toastView.addView(textView);

            mToastNew.setView(toastView);
            mToastNew.setDuration(Toast.LENGTH_SHORT);
            mToastNew.show();
//        }
    }

    public void showText(String content) {
        if (mToastNormal == null) {
            content = "<font color='#FFFFFF'>" + content + "</font>";
            mToastNormal = Toast.makeText(CustomApplication.getContext(), Html.fromHtml(content), Toast.LENGTH_SHORT);
            mToastNormal.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) mToastNormal.getView();
            toastView.setBackgroundResource(R.drawable.toasts);
            toastView.setPadding(140, 70, 140, 70);
            mToastNormal.show();
            oldMsg = content;
            oneTime = System.currentTimeMillis();
        } else {
            towTime = System.currentTimeMillis();
            if (content.equals(oldMsg)) {
                if (towTime - oneTime > Toast.LENGTH_LONG) {
                    mToastNormal.show();
                }
            } else {
                oldMsg = content;
                mToastNormal.setText(content);
                mToastNormal.show();
            }
        }
        oneTime = towTime;
    }
}
