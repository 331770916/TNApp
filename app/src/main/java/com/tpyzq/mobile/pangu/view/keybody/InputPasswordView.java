package com.tpyzq.mobile.pangu.view.keybody;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by zhangwenbo on 2016/8/26.
 * 密码输入框
 */
public class InputPasswordView extends TextView {

    private Paint passwordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mTextLength;

    public InputPasswordView(Context context) {
        this(context, null);
    }

    public InputPasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public InputPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bgPaint.setStrokeWidth(Helper.dip2px(CustomApplication.getContext(), 0.5f));
        bgPaint.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));

        borderPaint.setStrokeWidth(Helper.dip2px(CustomApplication.getContext(), 0.5f));
        borderPaint.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
        passwordPaint.setStrokeWidth(Helper.dip2px(CustomApplication.getContext(), 1.0f));
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 外边框
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rect, 3, 3, borderPaint);

        RectF rectcontent = new RectF(1, 1, width -1, height -1);
        canvas.drawRoundRect(rectcontent, 3, 3, bgPaint);

        for (int i = 1; i < 6; i++) {
            float x = width * i / 6;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        float cx, cy = height/ 2;
        float half = width / 6 / 2;
        for(int i = 0; i < mTextLength; i++) {
            cx = half + (i) * 2 * half;
            canvas.drawCircle(cx, cy, 6, passwordPaint);
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        mTextLength = text.toString().length();
        invalidate();
    }
}
