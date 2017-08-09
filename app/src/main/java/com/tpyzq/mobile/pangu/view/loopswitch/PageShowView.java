package com.tpyzq.mobile.pangu.view.loopswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by ltyhome on 02/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:PageShowView
 */

public class PageShowView extends View {
    private int drawType = 0;//0画圆，1画线
    private int colorCurrent = Color.GRAY;
    private int colorOther = Color.LTGRAY;
    private int mScale = 1;
    int total = 0;
    int current = 0;
    private Paint mPaint = null;

    public PageShowView(Context context) {
        this(context, null);
    }

    public PageShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    protected void initColor(int current,int other) {
        colorCurrent = current;
        colorOther = other;
    }

    protected void drawType(int type){
        this.drawType = type;
    }

    public void setCurrentView(int current, int total) {
        this.current = current;
        this.total = total;
        invalidate();
    }

    protected void scale(int scale){
        this.mScale = scale;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int view_height = getHeight() - getPaddingBottom() - getPaddingBottom();
        int view_width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = view_height / 10;
        int width = height * mScale;
        if (total > 1) {
            if (width * total + height * (total - 1) > view_width) {
                width = (view_width - (height * (total - 1))) / total;
            }
            int posX = view_width / 2 - (width * total + height * (total - 1) * 3) / 2;
            mPaint.setStrokeWidth(height);
            for (int i = 0; i < total; i++) {
                if (i != current) {
                    mPaint.setColor(colorOther);
                } else {
                    mPaint.setColor(colorCurrent);
                }
                switch (drawType){
                    case 0:
                        canvas.drawCircle(posX, view_height / 2, width / 2  , mPaint);
                        break;
                    case 1:
                        canvas.drawLine(posX, view_height / 2, posX + width, view_height / 2, mPaint);
                        break;
                }
                posX += height * 3 + width;
            }
        }

    }

    /**
     * 获取当前显示的位置
     */
    public int getCurrent() {
        return this.current;
    }
}
