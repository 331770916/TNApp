package com.tpyzq.mobile.pangu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class RiskControlView extends View {
    private Context mContext;

    private Paint mPaint, textPaint, mLinePaint;
    private int screenWidth, screenHeight;
    private float raduis;
    private int pointX, pointY;
    private float percent = 0;
    private float maxpercent = 100;
    private float textScale;
    private RectF speedRectF;
    private float dp;
    private int progress = 0xFFF3AE28;
    private int bigprogress = 0xFFD1D1D1;
    private int bg = 0xFFFFFFFF;


    public RiskControlView(Context context) {
        super(context);
        initView(context);
    }

    public RiskControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RiskControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dp = displayMetrics.densityDpi / 320;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5 * dp);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(3 * dp);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(bigprogress);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#F6B04B"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        raduis = screenWidth / 2;
        pointX = pointY = screenWidth / 2;
        speedRectF = new RectF(pointX - raduis + 10 * dp, pointY - raduis + 10 * dp,
                pointX + raduis - 10 * dp, pointY + raduis - 10 * dp);
        setMeasuredDimension(screenWidth, screenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCicle(canvas);
        drawScale(canvas);
        drawCurrentRate(canvas);
    }

    private void drawCurrentRate(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(progress);
        mPaint.setStrokeWidth(8 * dp);
        if (percent / maxpercent >= 1) {
            canvas.drawArc(speedRectF, 135f, 270f, false, mPaint);
        } else {
            canvas.drawArc(speedRectF, 135f, 270f * percent / maxpercent, false, mPaint);
        }
        mPaint.setStrokeWidth(20 * dp);
        if (percent != 0) {
            if (percent / maxpercent >= 1) {
                canvas.drawArc(speedRectF, 135f + 270f, 0.05f, false, mPaint);
            }else {
                canvas.drawArc(speedRectF, 135f + 270f * (percent / maxpercent), 0.05f, false, mPaint);
            }
        }
    }

    public void setProgress(float percent) {
        this.percent = percent;
        postInvalidate();
    }

    public void setMaxProgress(float maxpercent) {
        this.maxpercent = maxpercent;
        postInvalidate();
    }

    public void setProgressColor(int color){
        this.progress = color;
        postInvalidate();
    }

    private void drawScale(Canvas canvas) {
        for (int i = 0; i < 120; i++) {
            if (i <= 75 || i>104) {
                canvas.drawLine(pointX - raduis + 25 * dp, pointY, pointX - raduis + 45 * dp, pointY, mLinePaint);
            }
            canvas.rotate(3, pointX, pointY);
        }
    }

    private void drawCicle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(bg);
        canvas.drawCircle(pointX, pointY, raduis, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(bigprogress);
        mPaint.setStrokeWidth(4 * dp);
        canvas.drawArc(speedRectF, 135, 270, false, mPaint);
    }
}
