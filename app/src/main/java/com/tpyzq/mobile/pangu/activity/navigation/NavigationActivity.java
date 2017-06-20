package com.tpyzq.mobile.pangu.activity.navigation;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.ViewPagerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.scaleCircleNavigator.ScaleCircleNavigator;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/6/16.
 * 该页面为导航页面，当用户第一次安装或更新功能的时候启动该页面
 */
public class NavigationActivity extends BaseActivity {

    public static final String FIRST_INTO_APP = "firstIntoApp";

    @Override
    public void initView() {

        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.navigationMagicIndicatior);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.navigationViewPager);
        NavigationTab1 tab1 = new NavigationTab1();
        NavigationTab2 tab2 = new NavigationTab2();
        NavigationTab3 tab3 = new NavigationTab3();

        NavigationAdapter adapter = new NavigationAdapter(getSupportFragmentManager());
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(tab1);
        fragments.add(tab2);
        fragments.add(tab3);
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);


        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(this);
        scaleCircleNavigator.setCircleCount(3);
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(ContextCompat.getColor(this, R.color.blue3));

        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navigation;
    }
}
