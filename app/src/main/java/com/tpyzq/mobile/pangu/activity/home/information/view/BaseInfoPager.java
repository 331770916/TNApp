package com.tpyzq.mobile.pangu.activity.home.information.view;

import android.content.Context;
import android.view.View;

/**
 * Created by 陈新宇 on 2016/11/30.
 */

public abstract class BaseInfoPager {
    protected Context mContext;// 子类使用的Context，用来创建布局
    public View rootView;
    public BaseInfoPager(Context context){
        this.mContext = context;
        rootView = initView();
        setView();
//        initData();
    }
    /**
     * 子类创建布局，必须实现
     * @return
     */
    protected View initView(){
        View view = View.inflate(mContext, getFragmentLayoutId(), null);
        return view;
    }

    /**
     * 子类更新界面，不必须实现
     */
    public void initData(){

    }
    /**
     * 子类更新界面，不必须实现
     */
    public void setRefresh(){

    }
    public abstract void setView();
    public abstract int getFragmentLayoutId();
}
