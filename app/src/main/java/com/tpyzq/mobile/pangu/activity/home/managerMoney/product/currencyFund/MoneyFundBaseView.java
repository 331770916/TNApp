package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;


/**
 * Created by zhangwenbo on 2016/9/27.
 */
public abstract class MoneyFundBaseView {

    private View mView;

    public MoneyFundBaseView(Activity activity, Object object) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getLayoutId(), null);

        initView(mView, activity, object);
    }

    public abstract void initView(View view, Activity activity, Object object);

    public abstract int getLayoutId();

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

}
