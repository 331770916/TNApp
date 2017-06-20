package com.tpyzq.mobile.pangu.view.radiobutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.tpyzq.mobile.pangu.R;


/**
 * Created by zhangwenbo on 2016/5/12.
 * 自定义RadioButton
 */
public class MyRadioButton extends RadioButton {

    private int mDrawableSize;// xml文件中设置的大小

    public MyRadioButton(Context context) {
        this(context, null, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) { return; }
        Drawable drawableLeft   = null;
        Drawable drawableTop    = null;
        Drawable drawableRight  = null;
        Drawable drawableBottom = null;

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            Log.i("MyRadioButton", "attr:" + attr);
            switch (attr) {
                case R.styleable.MyRadioButton_pictureSize:
                    mDrawableSize = typedArray.getDimensionPixelSize(R.styleable.MyRadioButton_pictureSize, 50);
                    Log.i("MyRadioButton", "mDrawableSize:" + mDrawableSize);
                    break;
                case R.styleable.MyRadioButton_pictureTop:
                    drawableTop = typedArray.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_pictureBottom:
                    drawableRight = typedArray.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_pictureRight:
                    drawableBottom = typedArray.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_pictureLeft:
                    drawableLeft = typedArray.getDrawable(attr);
                    break;
                default :
                    break;
            }
        }
        typedArray.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }
}
