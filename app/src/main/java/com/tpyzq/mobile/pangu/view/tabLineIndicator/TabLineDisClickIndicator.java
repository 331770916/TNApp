package com.tpyzq.mobile.pangu.view.tabLineIndicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/9/26.
 */
public class TabLineDisClickIndicator extends LinearLayout {

    private Paint mPaint;           //画笔
    private Path mPath;            //tabLine

    private Scroller mScroller;
    private int mTriangleWidth;     //tabLine宽度
    private int mTriangleHeight;    //tabLine高度
    private  int mInitTranslationX; //初始化偏移位置
    private int mTranslationX;      //移动的偏移位置
    private int mTabVisibleCount;   //可见的Tab数量
    private static final int COUNT_DEFAULT_TAB = 2;//默认的可见Tab值
    private List<String> mTitles;

    private static float RADIO_TRIANGLE_WIDTH = 1/2F; //tabLine与每个tab宽度的比例
    private final int DIMENSION_TRIANGLE_WIDTH_MAX = (int)(getScreenWidth() / 3 * RADIO_TRIANGLE_WIDTH); //最大宽度
    private int mTabWidth ;
    private OnTitleClickListener mOnTitleClickListener;

    private ArrayList<TextView> mTextViews ;


    public TabLineDisClickIndicator(Context context) {
        this(context, null);
    }

    public TabLineDisClickIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TabLineDisClickIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取可见tab数量
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableLineIndicator);

        mTabVisibleCount = a.getInt(R.styleable.TableLineIndicator_tab_count, COUNT_DEFAULT_TAB);

        a.recycle();

        mTextViews = new ArrayList<>();

        mScroller = new Scroller(context);

        mTabWidth = getScreenWidth()/mTabVisibleCount;

        if (mTabVisibleCount < 0) {
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(28, 134, 238));
        mPaint.setPathEffect(new CornerPathEffect(3.0f));
    }

    public void setTitles(String [] titles) {
        if (titles != null && titles.length > 0) {
            for (int i = 0; i< titles.length; i++) {
                TextView tv = generateTextView(titles[i]);

                if (i == 0) {
                    tv.setTextColor(Color.rgb(28, 134, 238));
                }
                mTextViews.add(tv);
                addView(tv);
            }

            setItemClickEvent();
        }
    }

    private TextView generateTextView(String title){
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = getScreenWidth()/ mTabVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
        int paddCode = Helper.dip2px(CustomApplication.getContext(), 10);
        tv.setPadding(paddCode, paddCode, paddCode, paddCode);
        tv.setLayoutParams(lp);
        return tv;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();
        if (cCount == 0) {
            return;
        }

        for (int i = 0; i < cCount; i++){
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(lp);
        }

        setItemClickEvent();
    }

    /**
     * 设置点击事件
     */
    private void setItemClickEvent() {
        for (int i = 0; i< getChildCount(); i++) {
            final int j = i;
            View view = getChildAt(i);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTranslationX = j * mTabWidth;

//                    smoothScrollTo(j * mTabWidth + mInitTranslationX);
                    if (j == 0) {
                        smoothScrollTo(j * mTabWidth);
                    } else if (j == getChildCount() -1) {
                        smoothScrollTo((j - 1) * mTabWidth);
                    } else {
                        smoothScrollTo(j * mTabWidth - (mInitTranslationX + mInitTranslationX/2));
                    }

                    if (mOnTitleClickListener != null) {
                        mOnTitleClickListener.click(j);
                        hiLightTextView(j);
                    }
                }

            });
        }
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        mOnTitleClickListener = onTitleClickListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int)(w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);

        mTriangleWidth = Math.min(mTriangleWidth, DIMENSION_TRIANGLE_WIDTH_MAX);

        mInitTranslationX = w/mTabVisibleCount/2 - mTriangleWidth /2;

        initTraingle();
    }

    private void smoothScrollTo(int destX) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;

        if (getChildCount() > mTabVisibleCount) {
            mScroller.startScroll(scrollX, 0, delta, 0, 1000);
        }

        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }


    /**
     * 初始花Tabline
     */
    private void initTraingle() {

        if (mTabVisibleCount <= 3) {
            mTriangleHeight = mTriangleWidth / 24;
        } else if (3 < mTabVisibleCount && mTabVisibleCount < 10) {
            mTriangleHeight = mTriangleWidth / 14;
        } else {
            mTriangleHeight = mTriangleWidth / 10;
        }

        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth, -mTriangleHeight);
        mPath.lineTo(0, -mTriangleHeight);
        mPath.lineTo(0, 0);
        mPath.close();

    }

    /**
     * 获取屏幕的宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return  outMetrics.widthPixels;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();

        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();

        super.dispatchDraw(canvas);
    }

    /**
     * 高亮某个Tab文本
     * @param pos
     */
    private void hiLightTextView(int pos) {
        restTextViewColor();

        View view = getChildAt(pos);

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.rgb(28, 134, 238));
        }
    }

    /**
     * 重置颜色
     */
    private void restTextViewColor() {
        for (int i = 0; i< getChildCount(); i++) {
            View view = getChildAt(i);

            if (view instanceof  TextView) {
                ((TextView) view).setTextColor(Color.BLACK);
            }
        }
    }

    public interface OnTitleClickListener {
        void click(int position);
    }
}
