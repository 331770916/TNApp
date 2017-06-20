package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock;

import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;


/**
 * Created by zhangwenbo on 2016/8/17.
 * 加载View
 */
public abstract class LayoutInflaterBaseView {

    public View LayoutInflater() {
        View view = LayoutInflater.from(CustomApplication.getContext()).inflate(getViewLayoutId(), null);
        initView(view);
        return view;
    }

    public abstract int getViewLayoutId();

    public abstract void initView(View view);


}
