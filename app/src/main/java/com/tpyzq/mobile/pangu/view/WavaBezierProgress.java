package com.tpyzq.mobile.pangu.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.tpyzq.mobile.pangu.R;


/**
 * Created by AIRY on 1/8/2016.
 */

public class WavaBezierProgress extends View implements View.OnClickListener {

    private final int DEFAULT_LOCATION_TESTSIZE = 100;
    private final int DEFAULT_PROGRESS_TESTSIZE = 10;
    private Paint mPaint;
    private Paint mTextPaint;
    /**
     * 视图的宽和高
     */
    private int mTotalWidth, mTotalHeight;
    private Path mPath;
    private int mWaveLength = 500;
    private int mOffset;
    private int mscreenHeight;
    private int mscreenWidth;
    private int mWaveCount;
    private int mCenterY;
    private int c = 10;
    private int textc;
    private int progressColor;
    private int textProgressColor;
    private int progerssTestSize;
    private int progerssTestLocation;
    private RectF mRectF;
   /* onProgress monProgress;


    public interface onProgress{
         void setprogress(int progress);

    }
    public void setonProgress(onProgress monProgress){
        this.monProgress=monProgress;
    }*/

    /*
    *
    * 设置进度多少
    * */

    public void setProgress(int a) {

        this.c = 100 - a;
        if (c < 0) c = 0;
        if (c > 100) c = 100;
        textc = a;
        mCenterY = (mscreenHeight / 100) * c;


    }

    public WavaBezierProgress(Context context) {
        super(context);

    }

    /*
    *
    * 测量初始化精度条
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mscreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        mscreenHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWaveCount = (int) Math.round(mscreenWidth / mWaveLength + 1.5);
        mCenterY = (mscreenHeight / 100) * c;

    }
    /*
    *
    * 初始化
    * */

    public WavaBezierProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WavaBezierProgress);
        textProgressColor = array.getColor
                (R.styleable.WavaBezierProgress_progressTextColor, Color.YELLOW);
        progressColor = array.getColor
                (R.styleable.WavaBezierProgress_progressColor, Color.RED);

        progerssTestSize = array.getDimensionPixelSize
                (R.styleable.WavaBezierProgress_progressTextSize, DEFAULT_PROGRESS_TESTSIZE);
        progerssTestLocation = array.getDimensionPixelSize
                (R.styleable.WavaBezierProgress_progressTextLocation, DEFAULT_LOCATION_TESTSIZE);
        array.recycle();


        mPath = new Path();

        mTextPaint = new Paint();
        mTextPaint.setColor(textProgressColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(progerssTestSize);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(progressColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        /*
        * 初始化动画发生器
        * */

        ValueAnimator animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w - getPaddingLeft() - getPaddingRight();
        mTotalHeight = h - getPaddingTop() - getPaddingBottom();
    }

    public WavaBezierProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //裁剪成圆区域
        Path path = new Path();
        canvas.save();
        path.reset();
        canvas.clipPath(path);
        mRectF = new RectF();
//        mRectF.left = 0 + dp2px(36) / 2;
//        mRectF.right = mscreenWidth - dp2px(36) / 2;
//        mRectF.top = 0 + dp2px(8);
//        mRectF.bottom = mscreenHeight - dp2px(8);
//        path.addRoundRect(mRectF, dp2px(36), dp2px(36), Path.Direction.CCW);
        mRectF.left = 0 ;
        mRectF.right = mscreenWidth;
        mRectF.top = 0;
        mRectF.bottom = mscreenHeight;
        path.addRoundRect(mRectF,dp2px(5),dp2px(5),Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);


        mPath.reset();
        mPath.moveTo(-mWaveLength + mOffset, mCenterY);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        for (int i = 0; i < mWaveCount; i++) {//50是波纹的大小

            mPath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 20,
                    (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);

            mPath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 20, i * mWaveLength + mOffset, mCenterY);
        }

        mPath.lineTo(mscreenWidth, mscreenHeight);
        mPath.lineTo(0, mscreenHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        canvas.drawText("已下载" + textc + "%", mTotalWidth / 2 - mTextPaint.measureText("已下载" + textc + "%") / 2, mTotalHeight / 2 + progerssTestSize / 2, mTextPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(2));
        mPaint.setAntiAlias(true);

        canvas.drawRoundRect(mRectF, dp2px(5), dp2px(5), mPaint);
    }

    private int dp2px(int dp) {
        // px = dp * 密度比
        float density = getContext().getResources().getDisplayMetrics().density;// 0.75 1 1.5 2

        return (int) (dp * density + 0.5f);// 4舍5入
    }

    @Override
    public void onClick(View v) {
       /* c--;
        mCenterY = (mscreenHeight / 100) * c;*/
    }
}
