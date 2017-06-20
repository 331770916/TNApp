package com.tpyzq.mobile.pangu.activity.myself.view;

import android.content.Context;
import android.view.View;

/**
 * Created by 陈新宇 on 2016/8/10.
 * 我的主界面小碎片
 */
public abstract class BaseMySelfPager {
    protected Context mContext;// 子类使用的Context，用来创建布局
    public View rootView;
    public BaseMySelfPager(Context context){
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
