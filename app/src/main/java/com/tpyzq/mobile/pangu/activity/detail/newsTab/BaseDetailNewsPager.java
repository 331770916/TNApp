package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.content.Context;
import android.view.View;

/**
 * 作者：刘泽鹏 on 2016/10/26 10:24
 * 用于  详情页的  新闻公告研报  的 view 的 父类
 */
public abstract class BaseDetailNewsPager {

    protected Context mContext;// 子类使用的Context，用来创建布局
    public View rootView;
    public BaseDetailNewsPager(Context context, String stockCode){
        this.mContext = context;
        rootView = initView();
        setView(stockCode);
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
    public abstract void setView(String stockCode);
    public abstract int getLayoutId();

}
