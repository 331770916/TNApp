package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/9/6.
 */
public abstract class BaseTab {

    private View mView;
    private int mPosition;
    private ViewPager mViewPager;
    private int mCurrentTabIndex;

    public BaseTab(Activity activity, ArrayList<BaseTab> tabs) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getLayoutId(), null);
        initView(mView, activity);

        tabs.add(this);
        mPosition = tabs.indexOf(this);
    }

    public abstract void initView(View view, Activity activity);

    public abstract int getLayoutId();


    public void onResuem(){
        if (mViewPager != null) {
            mCurrentTabIndex = mViewPager.getCurrentItem();

            if (mCurrentTabIndex == mPosition) {
                initToConnect();
            }
        }
    }

    public void onStop(){}

    public void onDestory(){}

    //根据当前所在页面 是否初始化网络连接
    public  void initToConnect(){

    }

    public  void toRunConnect(){

    }

    public  void toStopConnect(){

    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                if (mPosition == arg0) {
                    toRunConnect();
                } else {
                    toStopConnect();
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

}
