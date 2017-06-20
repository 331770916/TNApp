package com.tpyzq.mobile.pangu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by zhangwenbo on 2017/3/23.
 */

public class WaterVaveImageView extends ImageView {
    private final static int FPS = 1000 / 40;
    //设置画笔
    private final Paint mWavePaint1 = new Paint();
    {
        mWavePaint1.setAntiAlias(true);
        mWavePaint1.setStyle(Paint.Style.STROKE);
        mWavePaint1.setColor(Color.WHITE);
        mWavePaint1.setStrokeWidth(1f);
        mWavePaint1.setAlpha(255);
    }

    //设置画笔
    private final Paint mWavePaint2 = new Paint();
    {
        mWavePaint2.setAntiAlias(true);
        mWavePaint2.setStyle(Paint.Style.STROKE);
        mWavePaint2.setColor(Color.WHITE);
        mWavePaint2.setStrokeWidth(1f);
        mWavePaint2.setAlpha(150);
    }

    //设置画笔
    private final Paint mWavePaint3 = new Paint();
    {
        mWavePaint3.setAntiAlias(true);
        mWavePaint3.setStyle(Paint.Style.STROKE);
        mWavePaint3.setColor(Color.WHITE);
        mWavePaint3.setStrokeWidth(1f);
        mWavePaint3.setAlpha(100);
    }

    //设置画笔
    private final Paint mWavePaint4 = new Paint();
    {
        mWavePaint4.setAntiAlias(true);
        mWavePaint4.setStyle(Paint.Style.STROKE);
        mWavePaint4.setColor(Color.WHITE);
        mWavePaint4.setStrokeWidth(1f);
        mWavePaint4.setAlpha(70);
    }

    private float mPicWidth;
    private float mPicHeight;

    private float min_waveAreaRadius;
    private float max_waveAreaRadius;

    private ExecutorService pool = Executors.newSingleThreadExecutor();

    public WaterVaveImageView(Context context) {
        this(context, null);
    }

    public WaterVaveImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public WaterVaveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPicWidth = getDrawable().getBounds().width();
        mPicHeight = getDrawable().getBounds().height();

        //最小半径
        min_waveAreaRadius = (float) Math.sqrt(mPicWidth/3  * mPicWidth/3 + mPicHeight/3 * mPicHeight/3);

        min_waveAreaRadius = min_waveAreaRadius + 30;

        //最大半径
        max_waveAreaRadius = min_waveAreaRadius + 30;
    }

    private Thread mWaterLineThead1;
    private Thread mWaterLineThead2;
    private Thread mWaterLineThead3;
    private Thread mWaterLineThead4;
    private Canvas mCanvas;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;

        float [] radiuses = {min_waveAreaRadius, (max_waveAreaRadius + min_waveAreaRadius)/2, max_waveAreaRadius, max_waveAreaRadius + (max_waveAreaRadius * 0.05f)};
        Paint [] paints = {mWavePaint1, mWavePaint2, mWavePaint3, mWavePaint4};

        for (int i = 0; i < radiuses.length; i++) {
            canvas.drawCircle(getWidth()/2, getHeight()/2, radiuses[i], paints[i]);
        }



//        if (mWaterLineThead1 == null) {
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    mWaterLineThead1 = new WaterLineThead1(255, mWavePaint1);
//                    pool.execute(mWaterLineThead1);
//                }
//            }, 1000);
//
//        }


    }


    private void settingAlpha(int alpha, Paint paint) {

        try {
            while (alpha > 0) {
                alpha--;
                paint.setAlpha(alpha);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class WaterLineThead1 extends Thread{
        private int mAlpha;
        private Paint mPaint;

        public WaterLineThead1(int alpha, Paint paint) {
            mAlpha = alpha;
            mPaint = paint;
        }

        @Override
        public void run() {
            super.run();
            settingAlpha(mAlpha, mPaint);

            if (mWaterLineThead2 == null) {
                mWaterLineThead2 = new WaterLineThead2(150, mWavePaint2);
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pool.execute(mWaterLineThead2);
                }
            }, 1000);


        }
    }

    private class WaterLineThead2 extends Thread{
        private int mAlpha;
        private Paint mPaint;

        public WaterLineThead2(int alpha, Paint paint) {
            mAlpha = alpha;
            mPaint = paint;
        }

        @Override
        public void run() {
            super.run();
            settingAlpha(mAlpha, mPaint);

            if (mWaterLineThead3 == null) {
                mWaterLineThead3 = new WaterLineThead3(100, mWavePaint3);
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pool.execute(mWaterLineThead3);
                }
            }, 1000);

        }
    }

    private class WaterLineThead3 extends Thread{
        private int mAlpha;
        private Paint mPaint;

        public WaterLineThead3(int alpha, Paint paint) {
            mAlpha = alpha;
            mPaint = paint;
        }

        @Override
        public void run() {
            super.run();
            settingAlpha(mAlpha, mPaint);

            if (mWaterLineThead4 == null) {
                mWaterLineThead4 = new WaterLineThead4(70, mWavePaint4);
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pool.execute(mWaterLineThead4);
                }
            }, 1000);
        }
    }

    private class WaterLineThead4 extends Thread{
        private int mAlpha;
        private Paint mPaint;

        public WaterLineThead4(int alpha,  Paint paint) {
            mAlpha = alpha;
            mPaint = paint;
        }

        @Override
        public void run() {
            super.run();
            settingAlpha(mAlpha, mPaint);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mWaterLineThead1 == null) {
                        mWaterLineThead1 = new WaterLineThead1(255, mWavePaint1);
                    }
                    pool.execute(mWaterLineThead1);
                }
            }, 1000);

        }
    }



}
