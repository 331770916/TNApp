package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;

/**
 * Created by zhangwenbo on 2017/5/18.
 * 退出页面以及风险测评和不可购买提示框
 */

public class CancelDialog {

    public static final int RISK_SOONEXPIRE = 1000;//风险测评即将到期
    public static final int RISK_EXPIRE = 2000;//风险评测已经到期
    public static final int RISK_NOT = 3000;//未做风险评测
    public static final int NOT_BUY = 4000; //不可购买提示
    private static final int DELIST = 5000;//买卖界面的退市提醒

    public static void cancleDialog(final Activity activity, String message, int style, final PositiveClickListener positiveClickListener, final NagtiveClickListener nagtiveClickListener){
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_cancel, null);
        TextView positiveBtn = (TextView) view.findViewById(R.id.cancel_dialog_positiveBtn);
        TextView nagtiveBtn = (TextView) view.findViewById(R.id.cancel_dialog_nagtiveBtn);
        TextView tv_message = (TextView) view.findViewById(R.id.cancel_dialog_msg);
        TextView tv_center_text = (TextView) view.findViewById(R.id.tv_center_text);

        switch (style) {
            case RISK_SOONEXPIRE:
                positiveBtn.setText("现在测评");
                nagtiveBtn.setText("以后再说");
                view.findViewById(R.id.view_flag).setVisibility(View.GONE);
                tv_message.setText(Html.fromHtml("<html><body> <p>尊敬的客户：</p>" +
                        "<p style='text-indent:2em'>您的风险承受能力评估结果即将过期，到期<br/>日期为"+ message +"，请重新测评。</p>" +
                        "</body></html>"));
                tv_center_text.setVisibility(View.GONE);
                break;
            case RISK_EXPIRE:
                positiveBtn.setText("现在测评");
                nagtiveBtn.setText("以后再说");
                view.findViewById(R.id.view_flag).setVisibility(View.GONE);
                tv_message.setText(Html.fromHtml("<html><body> <p>尊敬的客户：</p>" +
                        "<p style='text-indent:2em'>您的风险承受能力评估结果已过期，到期<br/>日期为"+ message +"，请重新测评。</p>" +
                        "</body></html>"));
                tv_center_text.setVisibility(View.GONE);
                break;
            case RISK_NOT:
                positiveBtn.setText("现在测评");
                nagtiveBtn.setText("以后再说");
                view.findViewById(R.id.view_flag).setVisibility(View.GONE);
                tv_message.setText(Html.fromHtml("<html><body> <p>尊敬的客户：</p>" +
                        "<p style=‘text-indent:2em’>您尚未填写《投资者风险承受能力问卷》。</p>" +
                        "<p style=’text-indent:2em‘>根据投资者适当性管理规则，要求风险承受<br/>能力必须与产品相匹配。</p>" +
                        "<p style=‘text-indent:2em’>为了不影响您的投资交易，请及时进行投资<br/>者风险承受能力测评。</p>" +
                        "</body></html>"));
                tv_center_text.setVisibility(View.GONE);
                break;
            case NOT_BUY:

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                layoutParams.width = width/3;
                int margin = Helper.dip2px(CustomApplication.getContext(), 10);
                layoutParams.setMargins(0, margin, 0, margin);
                positiveBtn.setLayoutParams(layoutParams);
                view.findViewById(R.id.view_flag).setVisibility(View.GONE);
                view.findViewById(R.id.cancel_dialog_nagtiveBtn).setVisibility(View.GONE);
                if (TextUtils.isEmpty(message)) {
                    tv_message.setText(Html.fromHtml("<html><body><p>尊敬的客户：</p>" +
                            "<p style='text-indent:2em'>根据您的风险承受能力等级评测，您选择的</br>产品与您的风险承受能力等级不匹配，您不能购</br>买此产品。</p>" +
                            "</body></html>"));
                } else {
                    tv_message.setText(message);
                }
                tv_center_text.setVisibility(View.GONE);
                break;
            case DELIST:
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int margin2 = Helper.dip2px(CustomApplication.getContext(), 20);
                layoutParams2.setMargins(margin2, margin2, margin2, margin2);
                tv_message.setLayoutParams(layoutParams2);
                tv_message.setGravity(Gravity.LEFT);
                tv_message.setText(message);
                tv_center_text.setVisibility(View.VISIBLE);
                break;
            default:
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int margin1 = Helper.dip2px(CustomApplication.getContext(), 20);
                layoutParams1.setMargins(margin1, margin1, margin1, margin1);
                tv_message.setLayoutParams(layoutParams1);
                tv_message.setGravity(Gravity.CENTER);
                tv_message.setText(message);
                tv_center_text.setVisibility(View.GONE);
                break;
        }


        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveClickListener == null) {
                    alertDialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.finishAfterTransition();
                    } else {
                        activity.finish();
                    }
                } else {
                    positiveClickListener.onPositiveClick();
                    alertDialog.dismiss();

                }

            }
        });

        nagtiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nagtiveClickListener == null) {
                    alertDialog.dismiss();
                } else {
                    nagtiveClickListener.onNagtiveClick();
                    alertDialog.dismiss();

                }
            }
        });

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void cancleDialog(final Activity activity, String message, final PositiveClickListener clickListener){
        cancleDialog(activity, message, -1, clickListener,null);
    }

    public static void cancleDialog(final Activity activity,  PositiveClickListener clickListener) {
        cancleDialog(activity, "您确定要退出吗？",  clickListener);
    }

    public static void cancleDialog(final Activity activity, String message) {
        cancleDialog(activity, message,  null);
    }

    public static void cancleDialog(final Activity activity) {
        cancleDialog(activity, "您确定要退出吗？",  null);
    }

    public interface PositiveClickListener {
        public void onPositiveClick();
    }
    public interface NagtiveClickListener {
        public void onNagtiveClick();
    }

}
