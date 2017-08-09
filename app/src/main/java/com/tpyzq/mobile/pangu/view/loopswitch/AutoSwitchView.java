package com.tpyzq.mobile.pangu.view.loopswitch;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by ltyhome on 02/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:AutoSwitchView
 */

public class AutoSwitchView extends AutoLoopSwitchBaseView {
    private int mType;
    public AutoSwitchView(Context context) {
        super(context);
    }

    public void setType(int mType) {
        this.mType = mType;
        initView();
    }

    @Override
    protected void initView() {
        mViewPager = new ViewPager(getContext());
        mViewPager.addOnPageChangeListener(this);
        addView(mViewPager, generalLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        controlViewPagerSpeed();
        mPageShowView = new PageShowView(getContext());
        LayoutParams params;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        params = generalLayoutParams(LayoutParams.WRAP_CONTENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, displayMetrics));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        switch (mType){
            case 0://首页轮播
                mPageShowView.initColor(getResources().getColor(R.color.white),getResources().getColor(R.color.dark));
                mPageShowView.scale(5);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                addView(mPageShowView, params);
                break;
            case 1://要闻轮播
                mPageShowView.initColor(getResources().getColor(R.color.blue),getResources().getColor(R.color.text));
                mPageShowView.drawType(1);
                mPageShowView.scale(15);
                params.addRule(RelativeLayout.ALIGN_RIGHT);
                mPageShowView.setPadding(-750,0,0,-10);
                addView(mPageShowView, params);
                break;
        }
        mHandler = new LoopHandler(this);
    }


    public Handler getHandler() {
        return mHandler;
    }

    @Override
    protected void onSwitch(int index, Object o) {
    }


    @Override
    protected View getFailtView() {
        return null;
    }

    @Override
    protected long getDurtion() {
        return 3000;
    }

    @Override
    public void setAdapter(AutoLoopSwitchBaseAdapter adapter) {
        super.setAdapter(adapter);
        mHandler.sendEmptyMessage(LoopHandler.MSG_REGAIN);
    }
}