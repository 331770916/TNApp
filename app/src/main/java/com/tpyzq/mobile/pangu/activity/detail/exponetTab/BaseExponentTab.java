package com.tpyzq.mobile.pangu.activity.detail.exponetTab;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;


/**
 * Created by zhangwenbo on 2016/10/26.
 * 指数
 */
public abstract class BaseExponentTab {

    private View mView;

    public BaseExponentTab(Activity activity, String exponentCode) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getLayotId(), null);
        initView(mView, activity, exponentCode);
    }

    public abstract void initView(View view, Activity activity,String exponentCode);

    public abstract int getLayotId();

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

}
