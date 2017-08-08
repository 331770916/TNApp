package com.tpyzq.mobile.pangu.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.view.dialog.ServiceDialog;

/**
 * Created by 张彪 on 2017/8/5.
 * 一个动态大小的dialog  已设置最大尺寸和最小尺寸
 */

public class CustomCenterDialog extends DialogFragment {
    public final static String SHOWCENTER = "SHOWCENTER";       // 显示一个Button
    public final static String SHOWCENTERBOTH = "SHOWCENTERBOTH";//显示两个BUtton
    private String context ;
    private Button mCommint;   //底部按钮
    private TextView mTvContext;  // 中间文字
    private TextView mistakeTitle;   // title
    private LinearLayout mLineaLayoutConetxt;
    private int i = 0 ;
    private String callPhone = "";
    private RelativeLayout mRelativeLayoutCenter;  //显示一个Button在中间
    private RelativeLayout mRelativelayoutCenterTwoButton;   // 显示两个Button
    private Button talkLater; // 以后再说  取消按钮
    private Button presentEvaluation; // 现在评测
    private String showcenter;
    private String titleText,centerButtonText,leftButtonText,rightButtonText;



    public static CustomCenterDialog CustomCenterDialog(String context,String SHOWCENTER){
        CustomCenterDialog customCenterDialog = new CustomCenterDialog();
        Bundle bundle = new Bundle();
//        if (map!=null){
//            bundle.putString("key","");
//            bundle.putString("value","");
//        }
        bundle.putString("context",context);
        bundle.putString("showcenter",SHOWCENTER);
        customCenterDialog.setArguments(bundle);
        return customCenterDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        context = bundle.getString("context");
        showcenter = bundle.getString("showcenter");
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);    // 设置没有标题栏
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_certer_dialog,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initViewText();
        initEvent();
        //设置窗口动画
        getDialog().getWindow().getAttributes().windowAnimations = R.style.CustomDialog;

    }

    private void initViewText() {
        if (!TextUtils.isEmpty(titleText)){
            mistakeTitle.setText(titleText);
        }

        if (!TextUtils.isEmpty(centerButtonText)){
            mTvContext.setText(centerButtonText);
        }

        if (!TextUtils.isEmpty(leftButtonText)){
            talkLater.setText(leftButtonText);
        }

        if (!TextUtils.isEmpty(rightButtonText)){
            presentEvaluation.setText(rightButtonText);
        }
    }

    private void initView(View view) {
        mistakeTitle = (TextView) view.findViewById(R.id.mistakeTitle);
        mCommint = (Button) view.findViewById(R.id.mistakePositive);
        mTvContext  = (TextView) view.findViewById(R.id.mistakeMsg);
        mLineaLayoutConetxt = (LinearLayout) view.findViewById(R.id.ll_center_layout);
        mTvContext.setText(context);
        mRelativeLayoutCenter = (RelativeLayout) view.findViewById(R.id.rl_center);
        mRelativelayoutCenterTwoButton = (RelativeLayout) view.findViewById(R.id.rl_center_two_button);
        talkLater = (Button) view.findViewById(R.id.talk_later);
        presentEvaluation = (Button) view.findViewById(R.id.present_evaluation);
        if (SHOWCENTER.equals(showcenter)){
            mRelativeLayoutCenter.setVisibility(View.VISIBLE);
            mRelativelayoutCenterTwoButton.setVisibility(View.GONE);
            callPhone = "(" + getActivity().getResources().getString(R.string.dh1) +
                    getActivity().getResources().getString(R.string.dh) + ")";
            // 设置电话拨打
            setTextClickColorForSplin();
        }else if (SHOWCENTERBOTH.equals(showcenter)){
            mRelativeLayoutCenter.setVisibility(View.GONE);
            mRelativelayoutCenterTwoButton.setVisibility(View.VISIBLE);
            callPhone = "";
        }
    }

    public void setTitleText(String titleText){    // 设置标题文字
        this.titleText = titleText;
    }

    public void setCenterButtonText(String centerButtonText){   //  设置按钮文字
        this.centerButtonText = centerButtonText;
    }

    public void setBothButtonText(String leftButtonText ,String rightButtonText){   //  设置两个按钮的问题
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
    }

    private void setTextClickColorForSplin() {
        String reust = context+callPhone;
        SpannableString spanText = new SpannableString(reust);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#1C86EE"));
                ds.setUnderlineText(true);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                getPermission(getActivity());

            }
        }, reust.length()- 6, reust.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvContext.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        mTvContext.setText(spanText);
        mTvContext.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
    }

    /**
     * 设置监听
     */
    private void initEvent() {

//        //在DialogFragment中传参数
//        DialogListener listener=(DialogListener)getActivity();
//        listener.onComplete(context); //传参数

        // 设置Dialog中间LineaLayout布局高度 。
        mLineaLayoutConetxt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                i++;
                Log.e("TAG",i+"");
                int mlinear = mLineaLayoutConetxt.getHeight();
                ViewGroup.LayoutParams layoutParams = mLineaLayoutConetxt.getLayoutParams();
                if (mlinear<dip2px(getActivity(),66)){
                    layoutParams.height = dip2px(getActivity(),66);
                    Log.e("LAYOUTPARAMS+HEIGHT",dip2px(getActivity(),66)+"");
                    mLineaLayoutConetxt.setLayoutParams(layoutParams);
//                    mLineaLayoutConetxt.getViewTreeObserver()
//                            .removeGlobalOnLayoutListener(this);
                }else if (mlinear>dip2px(getActivity(),162)){
                    Log.e("LAYOUTPARAMS+HEIGHT",dip2px(getActivity(),162)+"");
                    layoutParams.height  = dip2px(getActivity(),162);
                    mLineaLayoutConetxt.setLayoutParams(layoutParams);
//                    mLineaLayoutConetxt.getViewTreeObserver()
//                            .removeGlobalOnLayoutListener(this);
                }
            }
        });
        // 设置点击外围不消失
        getDialog().setCanceledOnTouchOutside(false);
        // 设置点击back不消失
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keycode, KeyEvent keyEvent) {
                if (keycode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });

        // 关闭当前弹窗
        mCommint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmOnClick!=null){
                    confirmOnClick.confirmOnclick();  // 确定按钮回调
                }else {
                    getDialog().dismiss();
                }
            }
        });
        // 现在评测 等确定按钮
        presentEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmOnClick!=null){
                    confirmOnClick.confirmOnclick();  // 确定按钮回调
                }
            }
        });

        talkLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.gravity = Gravity.BOTTOM; //底部
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(lp);
        //设置对话框宽高也可以使用lp.width和lp.height
    }

    @Override
    public void onResume() {
        Dialog dialog = getDialog();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);   //

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static void getPermission(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        ServiceDialog dialog = new ServiceDialog(activity);
        dialog.show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private ConfirmOnClick confirmOnClick;
    public void setOnClickListener(ConfirmOnClick confirmOnClick){
        this.confirmOnClick = confirmOnClick;
    }

    public interface DialogListener{
        void onComplete(String result);
    }

    public interface ConfirmOnClick{
       void confirmOnclick();
    }

}
