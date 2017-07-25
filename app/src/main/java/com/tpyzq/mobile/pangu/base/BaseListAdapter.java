package com.tpyzq.mobile.pangu.base;
import android.widget.BaseAdapter;


/**
 * Created by lx on 2017/7/21.
 */

public abstract class BaseListAdapter extends BaseAdapter {

    //撤销点击回调
    public ItemOnClickListener itemOnClickListener;
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
}
