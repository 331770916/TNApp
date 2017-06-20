package com.tpyzq.mobile.pangu.data;

/**
 * 作者：刘泽鹏 on 2016/9/23 14:42
 */
public class ImportantEventValueEntity {

    private int itemType;           //多布局展示    判断那种布局
    private String date;            //日期
    private String tiele;           //标题
    private String time;            //时间
    private int imgType;          //判断  使用哪张图片用的
    private String id;              //重大详情页面  请求是入参用到的id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTiele() {
        return tiele;
    }

    public void setTiele(String tiele) {
        this.tiele = tiele;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }
}
