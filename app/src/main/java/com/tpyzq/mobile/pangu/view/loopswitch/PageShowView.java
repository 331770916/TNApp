package com.tpyzq.mobile.pangu.view.loopswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.tpyzq.mobile.pangu.log.LogUtil;

/**
 * Created by ltyhome on 02/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:PageShowView
 */
/**

 * 页码显示类

 *

 * @author ryze

 * @since 1.0  2016/07/17

 */

public class PageShowView extends View {
    private int drawType = 0;//0画圆，1画线
    private int colorCurrent = Color.GRAY;
    private int colorOther = Color.LTGRAY;
    DisplayMetrics displayMetrics;
    int total = 0;
    int current = 0;
    int ViewWidth,ViewHeight,ViewMargin;



    private Paint mPaint = null;



    public PageShowView(Context context) {

        this(context, null);

    }



    public PageShowView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mPaint = new Paint();
    }


    protected void setWidthHeightMargin(DisplayMetrics display,int w,int h,int m){
        this.displayMetrics = display;
        this.ViewWidth = w;
        this.ViewHeight = h;
        this.ViewMargin = m;
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





    @Override

    protected void dispatchDraw(Canvas canvas) {



        super.dispatchDraw(canvas);



        int view_height = getHeight() - getPaddingBottom() - getPaddingBottom();
        if (total > 1) {
            int posX = 0 ;
            int width = ViewWidth;
            int height = ViewHeight;
            switch (drawType){
                case 0:
                    posX = (displayMetrics.widthPixels - (ViewWidth * total + ViewMargin * (total -1)))/2;
                    break;
                case 1:
                    posX = displayMetrics.widthPixels - (ViewWidth * (total+1) + ViewMargin * total -22);
                    break;
            }
            mPaint.setStrokeWidth(height);
            for (int i = 0; i < total; i++) {

                if (i != current) {

                    mPaint.setColor(colorOther);

                } else {

                    mPaint.setColor(colorCurrent);

                }

                switch (drawType) {
                    case 0:
                        canvas.drawCircle(posX, view_height / 3, width / 2, mPaint);
                        posX += ViewMargin + width;
                        break;
                    case 1:
                        canvas.drawLine(posX, view_height / 2 , posX + width, view_height/2 , mPaint);
                        posX += ViewMargin + width;
                        break;
                }
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
