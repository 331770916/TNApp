package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITab;
import com.tpyzq.mobile.pangu.interfac.ITabDataObserver;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/24.
 */
public abstract class BaseTransferObserverTabView implements ITabDataObserver {

    private View mView;
    private int mPosition;
    private ViewPager mViewPager;
    private int mCurrentTabIndex;

    public BaseTransferObserverTabView(Activity activity, ITab iTab, ArrayList<BaseTransferObserverTabView> tabs, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getTabLayoutId(), null);

        initView(mView, activity);

        if (iTab != null) {
            iTab.registerObserver(this);
        }

        tabs.add(this);
        mPosition = tabs.indexOf(this);
    }

    public void onResume(){
        if (mViewPager != null) {
            mCurrentTabIndex = mViewPager.getCurrentItem();

            if (mCurrentTabIndex == mPosition) {
                initToConnect();
            }
        }
    }

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

    @Override
    public void update(Object object, boolean isMaintab, int position) {

    }

    //根据当前所在页面 是否初始化网络连接
    public  void initToConnect(){

    }

    public  void toRunConnect(){

    }

    public  void toStopConnect(){

    }

    public abstract void initView(View view, Activity activity);

    public abstract int getTabLayoutId();

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
}
