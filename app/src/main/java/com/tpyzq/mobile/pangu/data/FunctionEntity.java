package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * 作者：刘泽鹏 on 2016/10/21 13:55
 */
public class FunctionEntity implements Serializable {
    private int iconId;             //图片 id
    private String title;           //标题
    private boolean isChild;       //是否选中
    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }
}
