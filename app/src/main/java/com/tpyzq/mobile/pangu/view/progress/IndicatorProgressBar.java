package com.tpyzq.mobile.pangu.view.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by zhangwenbo on 2017/8/14.
 */

public class IndicatorProgressBar extends ProgressBar {

    private Paint mTextPaint;
    private Drawable mDrawableIndicator;
    private int offset=5;

    private Formatter m_formatter;

    public IndicatorProgressBar(Context context) {
        this(context, null);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#368de7"));
        mTextPaint.setTextSize(10);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setFakeBoldText(true);
    }

    public Drawable getmDrawableIndicator() {
        return mDrawableIndicator ;
    }

    public void setmDrawableIndicator(Drawable mDrawableIndicator) {
        this.mDrawableIndicator = mDrawableIndicator;
    }

    public int getOffset() {
        return offset ;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setProgressIndicator(Drawable indicator) {
        mDrawableIndicator = indicator;
    }

    public void setTextFormatter(Formatter formatter) {
        m_formatter = formatter;
    }

    /**
     * Set the text size.
     *
     * @param size
     */
    public void setTextSize(float size) {
        mTextPaint.setTextSize(size);
    }

    /**
     * Set the text bold.
     *
     * @param bold
     */
    public void setTextBold(boolean bold) {
        mTextPaint.setFakeBoldText(true);
    }

    /**
     * Set the alignment of the text.
     *
     * @param align
     */
    public void setTextAlign(Paint.Align align) {
        mTextPaint.setTextAlign(align);
    }

    /**
     * Set the paint object used to draw the text on to the canvas.
     *
     * @param paint
     */
    public void setPaint(TextPaint paint) {
        mTextPaint = paint;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mDrawableIndicator!=null){
            //获取系统进度条的宽度 这个宽度也是自定义进度条的宽度 所以在这里直接赋值
            final int width=getMeasuredWidth();
            final int height=getMeasuredHeight() + getIndicatorHeight();
            setMeasuredDimension(width, height);
        }
    }

    /**
     * @category 获取指示器的高度
     * @return
     */
    private int getIndicatorHeight(){
        if(mDrawableIndicator == null){
            return 0;
        }
        Rect r = mDrawableIndicator.copyBounds();
        int height = r.height();
        return height;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        Drawable progressDrawable = getProgressDrawable();

        if (mDrawableIndicator != null) {
            if (progressDrawable != null) {
                progressDrawable.getBounds().top = mDrawableIndicator.getIntrinsicHeight();
                progressDrawable.getBounds().bottom = progressDrawable .getBounds().height() + getIndicatorHeight();
            }
        }

        super.onDraw(canvas);

        // Draw the indicator to match the far right position of the progress
        // bar
        if (mDrawableIndicator != null) {
            canvas.save();
            int dx = 0;

            // get the position of the progress bar's right end
            if (progressDrawable != null) {
                dx = progressDrawable.getBounds().right;
            }

            // adjust for any additional offset
            dx = dx - getIndicatorWidth() / 2 - offset + getPaddingLeft();

            // translate the canvas to the position where we should draw the
            // indicator
            canvas.translate(dx, 0);

            mDrawableIndicator.draw(canvas);

            canvas.drawText(
                    m_formatter != null ? m_formatter.getText(getProgress())
                            : Math.round(getScale(getProgress()) * 100.0f)
                            + "%", getIndicatorWidth() / 2,
                    getIndicatorHeight() / 2 + 1, mTextPaint);

            // restore canvas to original
            canvas.restore();
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);

        // the setProgress super will not change the details of the progress bar
        // anymore so we need to force an update to redraw the progress bar
        invalidate();
    }

    private int getIndicatorWidth() {
        if (mDrawableIndicator == null) {
            return 0;
        }

        Rect r = mDrawableIndicator.copyBounds();
        int width = r.width();

        return width;
    }

    private float getScale(int progress) {
        float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;

        return scale;
    }

    /**
     * You must implement this interface if you wish to present a custom
     * formatted text to be used by the Progress Indicator. The default format
     * is X% where X [0,100]
     *
     * @author jsaund
     *
     */
    public interface Formatter {
        public String getText(int progress);
    }
}
