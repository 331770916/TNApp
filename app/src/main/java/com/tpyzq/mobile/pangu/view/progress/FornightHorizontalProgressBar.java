package com.tpyzq.mobile.pangu.view.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by zhangwenbo on 2016/10/6.
 */
public class FornightHorizontalProgressBar extends ProgressBar {

    private String mTextContent1 = "起购";
    private String mTextContent2 = "起息";
    private String mTextContent3 = "到期";
    private String mTextContent4 = "到账";

    private String mTextContent5 = "";
    private String mTextContent6 = "";
    private String mTextContent7 = "";
    private String mTextContent8 = "";

    private Paint mPaint1;
    private Paint mDrawPaint1;
    private Paint mPaint2;
    private Paint mDrawPaint2;
    private Paint mPaint3;
    private Paint mDrawPaint3;
    private Paint mPaint4;
    private Paint mDrawPaint4;
    private Paint mPaint5;
    private Paint mPaint6;
    private Paint mPaint7;
    private Paint mPaint8;

    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private Bitmap mBitmap3;
    private Bitmap mBitmap4;


    public FornightHorizontalProgressBar(Context context) {
        this(context, null);
    }

    public FornightHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FornightHorizontalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initText();
    }

    @Override
    public void setProgress(int progress)
    {
        setText(progress);
        super.setProgress(progress);

    }

    @Override
    public synchronized int getProgress() {
        return super.getProgress();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Rect rect1 = new Rect();
        Rect rect2 = new Rect();
        Rect rect3 = new Rect();
        Rect rect4 = new Rect();
        Rect rect5 = new Rect();
        Rect rect6 = new Rect();
        Rect rect7 = new Rect();
        Rect rect8 = new Rect();

        mPaint1.getTextBounds(mTextContent1, 0, mTextContent1.length(), rect1);
        mPaint2.getTextBounds(mTextContent2, 0, mTextContent2.length(), rect2);
        mPaint3.getTextBounds(mTextContent3, 0, mTextContent3.length(), rect3);
        mPaint4.getTextBounds(mTextContent4, 0, mTextContent4.length(), rect4);
        mPaint5.getTextBounds(mTextContent5, 0, mTextContent5.length(), rect5);
        mPaint6.getTextBounds(mTextContent6, 0, mTextContent6.length(), rect6);
        mPaint7.getTextBounds(mTextContent7, 0, mTextContent7.length(), rect7);
        mPaint8.getTextBounds(mTextContent8, 0, mTextContent8.length(), rect8);

        int x1 = 0;
        int x11 = 0;
        int y1 = (getHeight() / 2) - getPaddingTop()/3;
        int y11 = (getHeight() / 2) - mBitmap1.getHeight()/ 2;

        int x2 = (getWidth() / 4) - rect2.centerX();//
        int x6 = (getWidth() / 4) - rect6.centerX();
        int x22 = (getWidth() / 4) - mBitmap2.getWidth()/2;

        int x3 = (2 * getWidth() / 3) - rect3.centerX();
        int x7 = (2 * getWidth() / 3) - rect7.centerX();
        int x33 = (2 * getWidth() / 3) - mBitmap3.getWidth()/2;

        int x4 = getWidth() - (2 * rect4.centerX());
        int x8 = getWidth() - (2 * rect8.centerX());
        int x44 = getWidth() - mBitmap4.getWidth();

        int y2 = (getHeight()) - getPaddingTop()/3;

        canvas.drawText(mTextContent1, x1, y1, mPaint1);
        canvas.drawBitmap(mBitmap1, x11, y11, mDrawPaint1);

        canvas.drawText(mTextContent2, x2, y1, mPaint2);
        canvas.drawBitmap(mBitmap2, x22, y11, mDrawPaint2);

        canvas.drawText(mTextContent3, x3, y1, mPaint3);
        canvas.drawBitmap(mBitmap3, x33, y11, mDrawPaint3);


        canvas.drawText(mTextContent4, x4, y1, mPaint4);
        canvas.drawBitmap(mBitmap4, x44, y11, mDrawPaint4);


        canvas.drawText(mTextContent5, x1, y2, mPaint5);
        canvas.drawText(mTextContent6, x6, y2, mPaint6);
        canvas.drawText(mTextContent7, x7, y2, mPaint7);
        canvas.drawText(mTextContent8, x8, y2, mPaint8);

        invalidate();
    }

    // 初始化，画笔
    private void initText() {
        mPaint1 = new Paint();
        mPaint2 = new Paint();
        mPaint3 = new Paint();
        mPaint4 = new Paint();
        mPaint5 = new Paint();
        mPaint6 = new Paint();
        mPaint7 = new Paint();
        mPaint8 = new Paint();

        mDrawPaint1 = new Paint();
        mDrawPaint2 = new Paint();
        mDrawPaint3 = new Paint();
        mDrawPaint4 = new Paint();

        mPaint1.setAntiAlias(true);// 设置抗锯齿;;;;
        mPaint2.setAntiAlias(true);// 设置抗锯齿;;;;
        mPaint3.setAntiAlias(true);// 设置抗锯齿;;;;
        mPaint4.setAntiAlias(true);// 设置抗锯齿;;;;
        mPaint5.setAntiAlias(true);// 设置抗锯齿;;;;
        mPaint6.setAntiAlias(true);// 设置抗锯齿;;;;
        mPaint7.setAntiAlias(true);// 设置抗锯齿;;;;
        mPaint8.setAntiAlias(true);// 设置抗锯齿;;;;

        mPaint1.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
        mPaint2.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
        mPaint3.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
        mPaint4.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
        mPaint5.setColor(Color.BLACK);
        mPaint6.setColor(Color.BLACK);
        mPaint7.setColor(Color.BLACK);
        mPaint8.setColor(Color.BLACK);

        mPaint1.setTextSize(Helper.sp2px(getContext(), 12));
        mPaint2.setTextSize(Helper.sp2px(getContext(), 12));
        mPaint3.setTextSize(Helper.sp2px(getContext(), 12));
        mPaint4.setTextSize(Helper.sp2px(getContext(), 12));
        mPaint5.setTextSize(Helper.sp2px(getContext(), 12));
        mPaint6.setTextSize(Helper.sp2px(getContext(), 12));
        mPaint7.setTextSize(Helper.sp2px(getContext(), 12));
        mPaint8.setTextSize(Helper.sp2px(getContext(), 12));

    }

    //    // 设置文字内容
    private void setText(int progress) {

        if (mPaint1 == null || mPaint2== null || mPaint3== null || mPaint4== null) {
            return;
        }

        if (progress >= 0) {
            mPaint1.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
            mBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_p);
        } else {
            mPaint1.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
            mBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_n);
        }

        if (progress >= 25) {
            mPaint2.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
            mBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_p);
        } else {
            mPaint2.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
            mBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_n);
        }

        if (progress >= 66) {
            mPaint3.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
            mBitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_p);
        } else {
            mPaint3.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
            mBitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_n);
        }

        if (progress == 100) {
            mPaint4.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
            mBitmap4 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_p);
        } else {
            mPaint4.setColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
            mBitmap4 = BitmapFactory.decodeResource(getResources(), R.mipmap.seekbar_n);
        }
    }

    /**
     * @param strDate           起购时间
     * @param strEarnDate       起息时间
     * @param stopDate          到期时间
     * @param intoBankCard      到账时间
     */
    public void setStartDate(String strDate, String strEarnDate, String stopDate, String intoBankCard) {
        mTextContent5 = strDate;
        mTextContent6 = strEarnDate;
        mTextContent7 = stopDate;
        mTextContent8 = intoBankCard;
        invalidate();
    }

}
