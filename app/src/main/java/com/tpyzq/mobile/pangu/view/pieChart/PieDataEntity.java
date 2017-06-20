package com.tpyzq.mobile.pangu.view.pieChart;

/**
 * Created by 陈新宇 on 2016/10/26.
 * 饼状图数据
 */
public class PieDataEntity {

    private String name;
    private float value;
    private float percent;
    private int color = 0;
    private float angle = 0;
    private String title;

    public PieDataEntity(String name, float value, int color,String title) {
        this.name = name;
        this.value = value;
        this.color = color;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getpercent() {
        return percent;
    }

    public void setpercent(float percent) {
        this.percent = percent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

}
