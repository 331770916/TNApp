package com.tpyzq.mobile.pangu.view.chart.gesture;

/**
 * Created by zhangwenbo on 2016/6/3.
 * 当前选择数据的监听类
 */
public interface IOnValueSelectedListener {

    void start();

    /**
     * 数据展示
     * @param open  今开
     * @param close 昨收
     * @param high  最高
     * @param low   最低
     */
    void data(float open, float close, float high, float low);

    void end();

}
