package com.tpyzq.mobile.pangu.activity.trade;

import android.content.Context;
import android.view.View;

import com.tpyzq.mobile.pangu.data.StockInfoBean;

import java.util.List;

/**
 * anthor:Created by tianchen on 2017/4/1.
 * email:963181974@qq.com
 */

public abstract class BaseTraPager {
    protected Context mContext;// 子类使用的Context，用来创建布局
    public View rootView;
    public BaseTraPager(Context context){
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
    public abstract void setData(List<StockInfoBean> data);
    public abstract int getLayoutId();

}
