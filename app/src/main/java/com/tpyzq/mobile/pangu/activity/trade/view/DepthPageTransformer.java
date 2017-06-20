package com.tpyzq.mobile.pangu.activity.trade.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhangwenbo on 2016/8/6.
 * zhangwenbo
 * viewpager动画
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.8f;

    // A页面 切换到B页面 A页面 的 position值 是 0 ~ -1 这样的一个变化， B页面 的position是1 ~ 0

    @Override
    public void transformPage(View view, float position) {

        if (position <= -3.1666667) {
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (3 - Math.abs(position));
            view.setScaleY(scaleFactor);

        } else if (position <= -1.0555556) {

            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            view.setScaleY(scaleFactor);

        } else if (-1.0555556 < position && position <= 0) {

            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));

            view.setScaleY(scaleFactor);

        } else if (position < 0.0 && position < 2.1111112) {

            view.setScaleX(1f);
            view.setScaleY(1f);

        } else if (position >= 3.1666667) {
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (3 - Math.abs(position));
            view.setScaleY(scaleFactor);
        } else {

            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            view.setScaleY(scaleFactor);
        }

    }
}
