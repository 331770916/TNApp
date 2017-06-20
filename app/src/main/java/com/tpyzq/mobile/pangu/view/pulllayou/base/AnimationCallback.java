package com.tpyzq.mobile.pangu.view.pulllayou.base;

/**
 * Created by Administrator on 2016/10/17.
 */
public abstract class AnimationCallback {

    /**
     * 动画开始
     */
    public void onAnimationStart() {
    }

    /**
     * 动画结束
     */
    public void onAnimationEnd() {
    }

    /**
     * 动画进行中
     *
     * @param fraction 动画执行的程度[0,1]
     */
    public void onAnimation(float fraction) {
    }

}
