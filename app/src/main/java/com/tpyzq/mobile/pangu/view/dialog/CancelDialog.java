package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by zhangwenbo on 2017/5/18.
 * 退出页面以及风险测评和不可购买提示框
 */

public class CancelDialog {

    public static final int RISK_SOONEXPIRE = 1000;//风险测评即将到期
    public static final int RISK_EXPIRE = 2000;//风险评测已经到期
    public static final int RISK_NOT = 3000;//未做风险评测
    public static final int NOT_BUY = 4000; //不可购买提示

    public static void cancleDialog(final Activity activity, String message, int style, final PositiveClickListener clickListener){
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_cancel, null);
        TextView positiveBtn = (TextView) view.findViewById(R.id.cancel_dialog_positiveBtn);
        TextView nagtiveBtn = (TextView) view.findViewById(R.id.cancel_dialog_nagtiveBtn);
        TextView tv_message = (TextView) view.findViewById(R.id.cancel_dialog_msg);

        switch (style) {
            case RISK_SOONEXPIRE:
                positiveBtn.setText("现在测评");
                nagtiveBtn.setText("以后再说");

                tv_message.setText(Html.fromHtml("<html><body> <p>尊敬的客户：</p>" +
                        "<p style='text-indent:2em'>您的风险承受能力评估结果即将过期，到期<br/>日期为"+ message +"，请重新测评。</p>" +
                        "</body></html>"));
                break;
            case RISK_EXPIRE:
                positiveBtn.setText("现在测评");
                nagtiveBtn.setText("以后再说");

                tv_message.setText(Html.fromHtml("<html><body> <p>尊敬的客户：</p>" +
                        "<p style='text-indent:2em'>您的风险承受能力评估结果已过期，到期<br/>日期为"+ message +"，请重新测评。</p>" +
                        "</body></html>"));
                break;
            case RISK_NOT:
                positiveBtn.setText("现在测评");
                nagtiveBtn.setText("以后再说");

                tv_message.setText(Html.fromHtml("<html><body> <p>尊敬的客户：</p>" +
                        "<p style=‘text-indent:2em’>您尚未填写《投资者风险承受能力问卷》。</p>" +
                        "<p style=’text-indent:2em‘>根据投资者适当性管理规则，要求风险承受<br/>能力必须与产品相匹配。</p>" +
                        "<p style=‘text-indent:2em’>为了不影响您的投资交易，请及时进行投资<br/>者风险承受能力测评。</p>" +
                        "</body></html>"));
                break;
            case NOT_BUY:
                view.findViewById(R.id.cancel_dialog_nagtiveBtn).setVisibility(View.GONE);
                tv_message.setText(Html.fromHtml("<html><body><p>尊敬的客户：</p>" +
                        "<p style='text-indent:2em'>根据您的风险承受能力等级评测，您选择的</br>产品与您的风险承受能力等级不匹配，您不能购</br>买此产品。</p>" +
                        "</body></html>"));
                break;
            default:
                tv_message.setGravity(Gravity.CENTER);
                tv_message.setText(message);
                break;
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

    /**
     * 风险测评即将到期dialog
     */
    private static void soonexpireDialog() {

    }

    public static void cancleDialog(final Activity activity, String message, final PositiveClickListener clickListener){
        cancleDialog(activity, message, -1, clickListener);
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

}
