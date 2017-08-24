package com.tpyzq.mobile.pangu.activity.market.selfChoice;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/11/23.
 * 指示器adapter
 */

public class SelfChoiceNavigatorAdapter extends CommonNavigatorAdapter {

    private ViewPager mViewPager;
    private List<String> mTitlelist;
    private Context mConext;


    public SelfChoiceNavigatorAdapter(List<String> titlelist, ViewPager viewPager, Context context) {
        mViewPager = viewPager;
        mTitlelist = titlelist;
        mConext = context;
    }

    @Override
    public int getCount() {
        return mTitlelist == null ? 0 : mTitlelist.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(mConext);
        simplePagerTitleView.setTextSize(14);
        simplePagerTitleView.setNormalColor(Color.BLACK);
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
        simplePagerTitleView.setText(mTitlelist.get(index));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index);
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
        linePagerIndicator.setColors(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
        return linePagerIndicator;
    }

    @Override
    public float getTitleWeight(Context context, int index) {
        if (index == 0) {
            return 1.0f;
        } else if (index == 1) {
            return 1.0f;
        } else if (index == 2) {
            return 1.0f;
        } else {
            return 1.0f;
        }
    }

}
