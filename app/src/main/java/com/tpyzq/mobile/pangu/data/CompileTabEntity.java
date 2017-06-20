package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * 作者：刘泽鹏 on 2016/9/12 16:07
 * 资讯  编辑 Tab 页面   存储标题内容的  实体类
 */
public class CompileTabEntity implements Serializable {

    private String biaoTi;              //标题
    private String neiRong;             //内容

    private String fragmentName;             //内容
    private boolean isChecked;          //是否选中

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getBiaoTi() {
        return biaoTi;
    }

    public void setBiaoTi(String biaoTi) {
        this.biaoTi = biaoTi;
    }

    public String getNeiRong() {
        return neiRong;
    }

    public void setNeiRong(String neiRong) {
        this.neiRong = neiRong;
    }


    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

}
