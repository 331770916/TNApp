package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;


/**
 * Created by zhangwenbo on 2016/9/10.
 * 自选股提醒页面Tab基类
 */
public abstract class BaseRemainTab {

    private View mView;

    public BaseRemainTab (Activity activity) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getLayoutId(), null);

        initView(mView, activity);
    }



    public abstract void initView(View view, Activity activity);

    public abstract int getLayoutId();

    public void onResume() {}

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

}
