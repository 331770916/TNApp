package com.tpyzq.mobile.pangu.view.tabLineIndicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/8/16.
 * tab指示器
 */
public class TabLineIndicator extends LinearLayout {

    private Paint mPaint;           //画笔
    private Path  mPath;            //tabLine

    private int mTriangleWidth;     //tabLine宽度
    private int mTriangleHeight;    //tabLine高度
    private  int mInitTranslationX; //初始化偏移位置
    private int mTranslationX;      //移动的偏移位置
    private int mTabVisibleCount;   //可见的Tab数量
    private static final int COUNT_DEFAULT_TAB = 2;//默认的可见Tab值
    private List<String> mTitles;

    private ViewPager mViewPager;
    private PageOnChangeListener mPageOnChangeListener;

    private static float RADIO_TRIANGLE_WIDTH = 1/2F; //tabLine与每个tab宽度的比例
    private final int DIMENSION_TRIANGLE_WIDTH_MAX = (int)(getScreenWidth() / 3 * RADIO_TRIANGLE_WIDTH); //最大宽度



    public TabLineIndicator(Context context) {
        this(context, null);
    }

    public TabLineIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TabLineIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取可见tab数量
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableLineIndicator);

        mTabVisibleCount = a.getInt(R.styleable.TableLineIndicator_tab_count, COUNT_DEFAULT_TAB);

        if (mTabVisibleCount < 0) {
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }

        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(28, 134, 238));
        mPaint.setPathEffect(new CornerPathEffect(3.0f));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int)(w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);

//        mTriangleWidth = Math.min(mTriangleWidth, DIMENSION_TRIANGLE_WIDTH_MAX);

        mInitTranslationX = w/mTabVisibleCount/2 - mTriangleWidth /2;

        initTraingle();
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
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(lp);
        }

        setItemClickEvent();
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
     * 初始花Tabline
     */
    private void initTraingle() {

        if (mTabVisibleCount <= 3) {
            mTriangleHeight = mTriangleWidth / 30;
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
     * 指示器跟随手指进行滚动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / mTabVisibleCount;
        mTranslationX = (int) (tabWidth * (offset + position));

        //容器移动， 当tab处于移动至最后一个时
        if (position >= (mTabVisibleCount - 2) && offset > 0 && getChildCount() > mTabVisibleCount) {

            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth + (int)(tabWidth * offset), 0);
            } else {
                this.scrollTo(position * tabWidth + (int)(tabWidth * offset),0);
            }
        }

        invalidate();
    }

    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            mTitles = titles;
            for (String title : titles) {
                addView(generateTextView(title));
            }

            setItemClickEvent();
        }
    }

    private View generateTextView(String title){
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = getScreenWidth()/ mTabVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTextColor(Color.WHITE);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 设置关联的viewPager
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, int pos) {
        mViewPager = viewPager;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);

                if (mPageOnChangeListener != null) {
                    mPageOnChangeListener.onPageScrolled(position,  positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                hiLightTextView(position);

                if (mPageOnChangeListener != null) {
                    mPageOnChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPageOnChangeListener != null) {
                    mPageOnChangeListener.onPageScrollStateChanged(state);
                }
            }
        });

        mViewPager.setCurrentItem(pos);
        hiLightTextView(pos);
    }


    public void setOnPageChangeListener(PageOnChangeListener listener) {
        mPageOnChangeListener = listener;
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

    /**
     * 高亮某个Tab文本
     * @param pos
     */
    private void hiLightTextView(int pos) {
        restTextViewColor();

        View view = getChildAt(pos);

        if (view instanceof  TextView) {
            ((TextView) view).setTextColor(Color.rgb(28, 134, 238));
        }
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
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    public interface PageOnChangeListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}
