package com.tpyzq.mobile.pangu.view.pieChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * 饼状图表
 * author: chenxinyu
 * data: 20160714
 */
public class PieChart extends View {
    /**
     * 视图的宽和高
     */
    private int mTotalWidth, mTotalHeight;
    /**
     * 绘制区域的半径
     */
    private float mRadius;

    private Paint mPaint, mLinePaint, mLinePaint2;

    private TextPaint mTextPaint;
    private Path mPath;

    /**
     * 扇形的绘制区域
     */
    private RectF mRectF;
    /**
     * 点击之后的扇形的绘制区域
     */
    private RectF mRectFTouch;

    private List<PieDataEntity> mDataList;
    /**
     * 所有的数据加起来的总值
     */
    private float mTotalValue;
    /**
     * 起始角度的集合
     */
    private float[] angles;
    /**
     * 手点击的部分的position
     */
    private int position = -1;
    /**
     * 点击监听
     */
    private OnItemPieClickListener mOnItemPieClickListener;

    private Context context;

    public void setOnItemPieClickListener(OnItemPieClickListener onItemPieClickListener) {
        mOnItemPieClickListener = onItemPieClickListener;
    }
    public interface OnItemPieClickListener {
        void onClick(int position);
    }

    public PieChart(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.context = context;
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        this.context = context;
    }

    private void init(Context context) {
        mRectF = new RectF();
        mRectFTouch = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth((float) 20.0);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(2);

        mLinePaint2 = new Paint();
        mLinePaint2.setAntiAlias(true);
        mLinePaint2.setStyle(Paint.Style.FILL);
        mLinePaint2.setStrokeWidth(2);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(dp2px(12));

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w - getPaddingLeft() - getPaddingRight();
        mTotalHeight = h - getPaddingTop() - getPaddingBottom();

        mRadius = (float) (Math.min(mTotalWidth, mTotalHeight) / 2 * 0.3);

        mRectF.left = -mRadius;
        mRectF.top = -mRadius;
        mRectF.right = mRadius;
        mRectF.bottom = mRadius;

        mRectFTouch.left = -mRadius - 10;
        mRectFTouch.top = -mRadius - 10;
        mRectFTouch.right = mRadius + 10;
        mRectFTouch.bottom = mRadius + 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDataList == null)
            return;
        canvas.translate(mTotalWidth / 2, mTotalHeight / 2);
        //绘制饼图的每块区域
        drawPiePath(canvas);
    }

    /**
     * 绘制饼图的每块区域 和文本
     *
     * @param canvas
     */
    private void drawPiePath(Canvas canvas) {
        try {
            //起始地角度
            float startAngle = 180;
            for (int i = 0; i < mDataList.size(); i++) {
                float sweepAngle = mDataList.get(i).getValue() / mTotalValue * 360 - 1;//每个扇形的角度
                mPath.moveTo(0, 0);
                if (position - 1 == i) {
                    mPath.arcTo(mRectFTouch, startAngle, sweepAngle);
                } else {
                    mPath.arcTo(mRectF, startAngle, sweepAngle);
                }
                mPaint.setColor(mDataList.get(i).getColor());

//            canvas.drawPath(mPath,mPaint);
                canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
                mPath.reset();
//            Log.i("toRadians", (startAngle + sweepAngle / 2) + "****" + Math.toRadians(startAngle + sweepAngle / 2));
                float pxs = (float) (mRadius * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
                float pys = (float) (mRadius * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
                float pxt = (float) ((mRadius + 30) * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
                float pyt = (float) ((mRadius + 30) * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
                angles[i] = startAngle;
                startAngle += sweepAngle + 1;
                mLinePaint.setColor(mDataList.get(i).getColor());
                mLinePaint2.setColor(mDataList.get(i).getColor());
                mTextPaint.setColor(mDataList.get(i).getColor());
//                mTextPaint.setColor(ColorUtils.TEXT);           //设置文字颜色
                //绘制线和文本
                canvas.drawLine(pxs, pys, pxt, pyt, mLinePaint);
                float res = mDataList.get(i).getValue() / mTotalValue * 100;
                //提供精确的小数位四舍五入处理。
                double resToRound = CalculateUtil.round(res, 2);
                float v = startAngle % 360;
                StaticLayout layout = new StaticLayout(resToRound + "%" + "\n" + mDataList.get(i).getTitle(), mTextPaint,
                        (int) mTextPaint.measureText(mDataList.get(i).getTitle()), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);
                if (pxt - pxs >= 0) {
                    if (pyt - pys >= -20 && pyt - pys <= 20) {
                        canvas.drawLine(pxt, pyt, pxt + 15, pyt + 15, mLinePaint2);
                        canvas.save();
                        canvas.translate(pxt + 15, pyt - 15);
                        layout.draw(canvas);
                        canvas.restore();
                    } else {
                        canvas.drawLine(pxt, pyt, pxt + 15, pyt, mLinePaint2);
                        canvas.save();
                        canvas.translate(pxt + 15, pyt);
                        layout.draw(canvas);
                        canvas.restore();
                    }

                } else {
                    if (pyt - pys >= -5 && pyt - pys <= 5) {
                        canvas.drawLine(pxt, pyt, pxt - 15, pyt - 15, mLinePaint2);
                        canvas.save();
                        canvas.translate(pxt - 15 - mTextPaint.measureText("默认默认"), pyt - 30);
                        layout.draw(canvas);
                        canvas.restore();
                    } else {
                        if (pys < 0) {
                            canvas.drawLine(pxt, pyt, pxt - 15, pyt, mLinePaint2);
                            canvas.save();
                            canvas.translate(pxt - 15 - mTextPaint.measureText("默认默认"), pyt - 30);
                            layout.draw(canvas);
                            canvas.restore();
                        } else {
                            canvas.drawLine(pxt, pyt, pxt - 15, pyt, mLinePaint2);
                            canvas.save();
                            canvas.translate(pxt - 15 - mTextPaint.measureText(resToRound + "%"), pyt);
                            layout.draw(canvas);
                            canvas.restore();
                        }

                    }
                }
            }
        }catch (Exception eeex){
            eeex.printStackTrace();
        }
    }

    public void setDataList(List<PieDataEntity> dataList) {
        this.mDataList = dataList;
        mTotalValue = 0;
        for (PieDataEntity pieData : mDataList) {
            mTotalValue += pieData.getValue();
        }
        angles = new float[mDataList.size()];
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                float x = event.getX() - (mTotalWidth / 2);
//                float y = event.getY() - (mTotalHeight / 2);
//                float touchAngle = 0;
//                if (x < 0 && y < 0) {  //2 象限
//                    touchAngle += 180;
//                } else if (y < 0 && x > 0) {  //1象限
//                    touchAngle += 360;
//                } else if (y > 0 && x < 0) {  //3象限
//                    touchAngle += 180;
//                }
//                //Math.atan(y/x) 返回正数值表示相对于 x 轴的逆时针转角，返回负数值则表示顺时针转角。
//                //返回值乘以 180/π，将弧度转换为角度。
//                touchAngle += Math.toDegrees(Math.atan(y / x));
//                if (touchAngle < 0) {
//                    touchAngle = touchAngle + 360;
//                }
//                float touchRadius = (float) Math.sqrt(y * y + x * x);
//                if (touchRadius < mRadius) {
//                    position = -Arrays.binarySearch(angles, (touchAngle)) - 1;
//                    invalidate();
//                    if (mOnItemPieClickListener != null) {
//                        mOnItemPieClickListener.onClick(position - 1);
//                    }
//                }
//                break;
//        }
        return super.onTouchEvent(event);
    }

    // dp转换px
    private int dp2px(int dp) {
        // px = dp * 密度比
        float density = getContext().getResources().getDisplayMetrics().density;// 0.75 1 1.5 2
        return (int) (dp * density + 0.5f);// 4舍5入
    }

}
