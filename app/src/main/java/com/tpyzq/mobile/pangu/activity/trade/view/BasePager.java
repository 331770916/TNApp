package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.view.View;

/**
 * 作者：刘泽鹏 on 2016/8/24 15:16
 */
public abstract class BasePager {
    protected Context mContext;// 子类使用的Context，用来创建布局
    public View rootView;
    public BasePager(Context context){
        this.mContext = context;
        rootView = initView();
        setView();
        initData();
    }
    /**
     * 子类创建布局，必须实现
     * @return
     */
    protected View initView(){
        View view = View.inflate(mContext, getLayoutId(), null);
        return view;
    }

    /**
     * 子类更新界面，不必须实现
     */
    public void initData(){

    }
    public abstract void setView();
    public abstract int getLayoutId();
}
