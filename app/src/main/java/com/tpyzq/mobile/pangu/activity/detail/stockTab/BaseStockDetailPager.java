package com.tpyzq.mobile.pangu.activity.detail.stockTab;

import android.content.Context;
import android.view.View;

/**
 * Created by 陈新宇 on 2016/8/10.
 * 股票详情小碎片
 */
public abstract class BaseStockDetailPager {
    protected Context mContext;// 子类使用的Context，用来创建布局
    public View rootView;
    public BaseStockDetailPager(Context context){
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
        View view = View.inflate(mContext, getLayoutId(), null);
        return view;
    }

    /**
     * 子类更新界面，不必须实现
     */
    public void initData(String stockCode){

    }
    public abstract void setView();
    public abstract int getLayoutId();

}
