package com.tpyzq.mobile.pangu.activity.market;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.log.LogHelper;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/8/19.
 * 行情View页面基类
 */
public abstract class BaseTabPager {
    protected boolean isShow = false;
    private View mView;
    private int mPosition;
    private ViewPager mViewPager;
    private int mCurrentTabIndex;
    public LoadInterface mLoadInterface;
    public MarketFragmentEditIsGoneListener mMarketFragmentEditIsGoneListener;
    private OnPageChangeListener mOnPageChangeListener;
    protected int pageTag = 11;
    public BaseTabPager(Activity activity, ArrayList<BaseTabPager> tabs) {

        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getFragmentLayoutId(), null);
        initView(mView, activity);
        tabs.add(this);
        mPosition = tabs.indexOf(this);
    }

    public abstract void initView(View view, Activity activity);

    //根据当前所在页面 通知OnResume
    public abstract void myTabonResume();

    public abstract void toRunConnect();

    public abstract void toStopConnect();

    public abstract int getFragmentLayoutId();

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

    public void setLoadInterface(LoadInterface loadInterface) {
        mLoadInterface = loadInterface;
    }

    public void setMarketFramgmentEditIsGoneInterface(MarketFragmentEditIsGoneListener marketFragmentEditIsGoneListener) {
        mMarketFragmentEditIsGoneListener = marketFragmentEditIsGoneListener;
    }

    private boolean mVisibleToUser;
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mVisibleToUser = isVisibleToUser;
    }

    public boolean getUserVisibleHint() {
        return mVisibleToUser;
    }

    public void addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void onResume(){
//        LogHelper.e("BaseTabPager","onResume");
//        isShow = true;
        if (mViewPager != null) {
            mCurrentTabIndex = mViewPager.getCurrentItem();

            if (mCurrentTabIndex == mPosition) {
                myTabonResume();
            }
        }
    }
    public void test(){
        LogHelper.e("BaseTabPager",mCurrentTabIndex+"  mPosition:  "+mPosition);
    }
    public void onStop(){
        toStopConnect();
        isShow = false;
//        LogHelper.e("BaseTabPager","onStop");
    }

    public void onDestory(){
        isShow = false;
//        toStopConnect();
//        LogHelper.e("BaseTabPager","onDestory");
    }
    public void onHiddenChanged(boolean hided){
        isShow = !hided;
    }
    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(arg0);
                }
//                LogHelper.e("BaseTabPager","onPageSelected index:"+arg0);
                if (mPosition == arg0) {
                    toRunConnect();
                    tabSelect();
                } else {
                    toStopConnect();
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(arg0, arg1, arg2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(arg0);
                }
            }
        });
    }
    protected void tabSelect(){

    }
    public interface LoadInterface {

        void loading();

        void complited();
    }

    public interface MarketFragmentEditIsGoneListener {
        void gone();

        void visible(String tag);
    }

    public interface OnPageChangeListener {

        void onPageSelected(int position);

        void onPageScrolled(int arg0, float arg1, int arg2);

        void onPageScrollStateChanged(int arg0);
    }

}
