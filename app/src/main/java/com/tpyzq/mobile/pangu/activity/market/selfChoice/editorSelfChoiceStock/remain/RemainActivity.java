package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/17.
 * 股票提醒页面
 */
public class RemainActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private LinearLayout             mViewGroup;
    private Fragment                 mCurrentFragment = null;
    private StockPriceRemainFragment mRemainPriceTab;
    private MySelfRemainFragment     mRemainMySelfTab;
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<>();
    private FragmentBackListener backListener;
    private boolean isInterception = false;

    @Override
    public void initView() {

        Intent intent = getIntent();
        String stockNumber = intent.getStringExtra("stockNumber");
        String stockName = intent.getStringExtra("stockName");


        findViewById(R.id.remainBackbtn).setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.remainRadioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        mViewGroup = (LinearLayout) findViewById(R.id.remainParentLayout);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        mRemainPriceTab = new StockPriceRemainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stockNumber", stockNumber);
        bundle.putString("stockName", stockName);
        mRemainPriceTab.setArguments(bundle);
        mRemainMySelfTab = new MySelfRemainFragment();
        mCurrentFragment = mRemainPriceTab;

        transaction.add(R.id.remainParentLayout, mRemainPriceTab);
        transaction.add(R.id.remainParentLayout, mRemainMySelfTab).hide(mRemainMySelfTab);
        transaction.commit();


//        mTab1 = new StockPriceRemainTab(this);
//        mTab2 = new MySelfRemainTab(this);
//
//        mViewGroup.addView(mTab1.getContentView(),new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0
//        ));

    }

    public FragmentBackListener getBackListener() {
        return backListener;
    }

    public void registerMyTouchListener(MyTouchListener listener) {

        myTouchListeners.add(listener);

    }

    public void unRegisterMyTouchListener(MyTouchListener listener) {

        myTouchListeners.remove( listener );

    }

    public void setBackListener(FragmentBackListener backListener) {
        this.backListener = backListener;
    }

    public boolean isInterception() {
        return isInterception;
    }

    public void setInterception(boolean isInterception) {
        this.isInterception = isInterception;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.remain_radio1:

                switchContent(mRemainPriceTab);

//                mViewGroup.removeAllViews();
//
//                mViewGroup.addView(mTab1.getContentView(),new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0
//                ));
                break;
            case R.id.remain_radio2:
                switchContent(mRemainMySelfTab);

//                mViewGroup.removeAllViews();
//
//                mTab2.onResume();
//
//                mViewGroup.addView(mTab2.getContentView(),new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0
//                ));
                break;
        }
    }

    /**
     * 切换页面的重载，优化了fragment的切换
     * @param to
     */
    private void switchContent(Fragment to) {
        if (mCurrentFragment != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mCurrentFragment).add(R.id.fragmentLayout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mCurrentFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFragment = to;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        for (MyTouchListener listener : myTouchListeners) {

            listener.onTouchEvent(ev);

        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remainBackbtn:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isInterception()) {
                if (backListener != null) {
                    backListener.onbackForward();
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_remain;
    }

    public interface FragmentBackListener {

        void  onbackForward();
    }

    public interface MyTouchListener {
        void onTouchEvent(MotionEvent event);
    }
}
