package com.tpyzq.mobile.pangu.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * anthor:Created by tianchen on 2017/5/16.
 * email:963181974@qq.com
 * 轮播图
 */

public class CarouselView extends ViewPager {
    //Fresco图片加载，可换成ImageView
    private SimpleDraweeView[] simpleDraweeViews;
    int currentItem;
    private ScheduledExecutorService executor;
    private int origSize;

    public CarouselView(Context context) {
        super(context);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置轮播速度
     */
    public void startAutoScroll() {
        startAutoScroll(5, 5, TimeUnit.SECONDS);
    }

    /**
     * 开启自动轮播
     *
     * @param initialDelay  首次执行延迟时间
     * @param period        多少秒执行一次
     * @param unit         轮播速度
     */
    public void startAutoScroll(long initialDelay,
                                long period,
                                TimeUnit unit) {
        if (origSize > 1) {
            stopAutoScroll();
            executor = Executors.newSingleThreadScheduledExecutor();
            Runnable command = new Runnable() {
                @Override
                public void run() {
                    selectNextItem();
                }

                private void selectNextItem() {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            setCurrentItem(++currentItem);
                        }
                    });
                }
            };
            executor.scheduleAtFixedRate(command, initialDelay, period, unit);
        }
    }

    /**
     * 停止轮播
     */
    public void stopAutoScroll() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    /**
     * 初始化轮播图
     * @param size  轮播图片个数
     * @param handler   回调
     */
    public void init(int size, final SimpleDraweeViewHandler handler) {
        this.origSize = size;
        int fitSize = initSize(size);
        initSimpleDraweeViews(fitSize);

        this.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE; // 取一个大数字
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                SimpleDraweeView t = simpleDraweeViews[position % simpleDraweeViews.length];
                container.addView(t);

                handler.handle(position % origSize, t);

                return t;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        this.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                handler.select(position % origSize);
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;

                startAutoScroll(); // 手动切换完成后恢复自动播放
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        currentItem = origSize * 1000000; // 取一个中间的大数字, 防止接近边界
        this.setCurrentItem(currentItem);
    }

    private void initSimpleDraweeViews(int size) {
        SimpleDraweeView[] tvs = new SimpleDraweeView[size];
        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = new SimpleDraweeView(getContext());
            tvs[i].getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tvs[i].setLayoutParams(layoutParams);
            simpleDraweeViews = tvs;
        }
    }

    private int initSize(int origSize) {
        int size;
        if (origSize > 3) {
            size = origSize;
        } else if (origSize > 1) {
            size = origSize * 2; // 小于等于3个时候, 需要扩大一倍, 防止出错
        } else {
            size = 4;
        }
        return size;
    }


    public interface SimpleDraweeViewHandler {
        /**
         * 设置相应的ImageView内容，要轮播的图片，以及点击事件
         * @param index 预加载指针位置
         * @param view 预加载ImageVIew
         */
        void handle(int index, SimpleDraweeView view);

        /**
         * 返回具体的轮播指针
         * @param position  轮播指针位置
         */
        void select(int position);
    }
}