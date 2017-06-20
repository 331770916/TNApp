package com.tpyzq.mobile.pangu.view.keybody;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by zhangwenbo on 2016/8/26.
 * 自定义交易键盘
 */
public class PopKeyBody implements View.OnClickListener{

    private View mView;
    private PopupWindow mPopupWindow = null;
    private ContentListener mContentListener;
    private InputPasswordView mInputPasswordView;
    private StringBuilder mStringBuilder;
    private TextView mTitle;
    private TextView mContentTv;
    private boolean isNeedInpoutPassword;

    public PopKeyBody(ContentListener listener, boolean isNeedInputPassword) {
        mContentListener = listener;
        mStringBuilder = new StringBuilder();
        this.isNeedInpoutPassword = isNeedInputPassword;
        initData(isNeedInputPassword);
    }

    private void initData(boolean isNeedInputPassword) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.banks_keybody_layout, null);
        LinearLayout inputPassWordLayout = (LinearLayout) mView.findViewById(R.id.inputPasswordLayout);
        mInputPasswordView = (InputPasswordView) mView.findViewById(R.id.inputPasswordView);
        mContentTv = (TextView) mView.findViewById(R.id.inputContentView);

        if (isNeedInputPassword) {
            mInputPasswordView.setVisibility(View.VISIBLE);
            mContentTv.setVisibility(View.GONE);
        } else {
            mContentTv.setVisibility(View.VISIBLE);
            mInputPasswordView.setVisibility(View.GONE);
        }

        mTitle = (TextView) mView.findViewById(R.id.titleKeybody);
        mView.findViewById(R.id.keyboard_num1).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num2).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num3).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num4).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num5).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num6).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num7).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num8).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num9).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_num0).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_delete).setOnClickListener(this);
        mView.findViewById(R.id.keyboard_positiveBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_num1:

                if(isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("1");
                    }
                } else {
                    mStringBuilder.append("1");
                }

                break;
            case R.id.keyboard_num2:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("2");
                    }
                } else {
                    mStringBuilder.append("2");
                }


                break;
            case R.id.keyboard_num3:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("3");
                    }
                } else {
                    mStringBuilder.append("3");
                }

                break;
            case R.id.keyboard_num4:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("4");
                    }
                } else {
                    mStringBuilder.append("4");
                }

                break;
            case R.id.keyboard_num5:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("5");
                    }
                } else {
                    mStringBuilder.append("5");
                }
                break;
            case R.id.keyboard_num6:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("6");
                    }
                } else {
                    mStringBuilder.append("6");
                }
                break;
            case R.id.keyboard_num7:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("7");
                    }
                } else {
                    mStringBuilder.append("7");
                }
                break;
            case R.id.keyboard_num8:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("8");
                    }
                } else {
                    mStringBuilder.append("8");
                }

                break;
            case R.id.keyboard_num9:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("9");
                    }
                } else {
                    mStringBuilder.append("9");
                }
                break;
            case R.id.keyboard_num0:

                if (isNeedInpoutPassword) {
                    if (mStringBuilder.toString().length() < 6) {
                        mStringBuilder.append("0");
                    }
                } else {
                    mStringBuilder.append("0");
                }
                break;
            case R.id.keyboard_delete:
                String _content = mStringBuilder.toString();

                if (_content.length() != 0) {
                    mStringBuilder.deleteCharAt(_content.length() -1 );
                }

                break;
            case R.id.keyboard_positiveBtn:
                mPopupWindow.dismiss();
                mContentListener.doPositive();
                break;
        }

        if (isNeedInpoutPassword) {
            mInputPasswordView.setText(mStringBuilder.toString());
        } else {
            mContentTv.setText(mStringBuilder.toString());
        }

        mContentListener.getContent(mStringBuilder.toString());
    }

    public void setContent(String content) {
        if (!isNeedInpoutPassword) {
            mContentTv.setText(content);
            mStringBuilder.append(content);
        }
    }

    public void clearContent() {
        mStringBuilder.delete(0, mStringBuilder.length());

        if (isNeedInpoutPassword) {
            mInputPasswordView.setText(mStringBuilder.toString());
        } else {
            mContentTv.setText(mStringBuilder.toString());
        }
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitleText(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置标题颜色
     * @param color
     */
    public void setTitleColor(@ColorRes int color) {
        mTitle.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), color));
    }

    /**
     * 设置标题大小
     * @param titleSize
     */
    public void setTitleSize(@DimenRes int titleSize) {
        mTitle.setTextSize(Helper.sp2px(CustomApplication.getContext(), titleSize));
    }

    /**
     * 设置标题图片
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setTitleImage(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight()); //设置边界
        }

        if (top != null) {
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight()); //设置边界
        }

        if (right != null) {
            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight()); //设置边界
        }

        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getMinimumWidth(), bottom.getMinimumHeight()); //设置边界
        }

        mTitle.setCompoundDrawablePadding(Helper.dip2px(CustomApplication.getContext(), 5)) ;
        mTitle.setCompoundDrawables(left, top, right, bottom);//画
    }


    /**
     * show the popwindow
     * @param viewGroup  It is one necessary param, don't set null
     */
    public void show(View viewGroup) {

        if (mPopupWindow  == null) {
            initPopwindow();
        }
        showPopUp(viewGroup);
    }

    public boolean isShow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return true;
        }
        return false;
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    private void showPopUp(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    private void initPopwindow() {
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//		mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);


    }

    public interface ContentListener {

        void getContent(String num);

        void doPositive();

    }
}
