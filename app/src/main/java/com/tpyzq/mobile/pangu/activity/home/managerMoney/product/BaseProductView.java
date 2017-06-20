package com.tpyzq.mobile.pangu.activity.home.managerMoney.product;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;


/**
 * Created by zhangwenbo on 2016/9/24.
 * 六种产品的的基类
 */
public abstract class BaseProductView {

    private View mView;

    public BaseProductView(Activity activity, int type, Object object) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getLayoutId(), null);
        initView(mView, activity, type, object);
    }

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

    public abstract void initView(View view, Activity activity, int type, Object object);

    public abstract int getLayoutId();
}
