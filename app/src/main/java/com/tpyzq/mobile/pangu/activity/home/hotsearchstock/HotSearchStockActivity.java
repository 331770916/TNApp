package com.tpyzq.mobile.pangu.activity.home.hotsearchstock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * Created by wangqi on 2016/12/27.
 * 热搜股票页面
 */

public class HotSearchStockActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup mRadioGroup;
    private RadioButton mRbut_hour, mRbut_optionalHour;

    private int tabIds[] = new int[]{R.id.Rbut_1, R.id.Rbut_2};
    private Fragment[] tab_fragment = new Fragment[3];
    private View indicatorView, indicatorView1;

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        mRbut_hour = (RadioButton) findViewById(R.id.Rbut_1);
        mRbut_optionalHour = (RadioButton) findViewById(R.id.Rbut_2);

        indicatorView = findViewById(R.id.indicatorView);
        indicatorView1 = findViewById(R.id.indicatorView1);
        initLogIc();

    }


    /**
     * 初始化逻辑
     */
    private void initLogIc() {
        mRbut_hour.setChecked(true);
        replaceFragment(0);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Rbut_1) {
                    indicatorView.setVisibility(View.VISIBLE);
                    indicatorView1.setVisibility(View.INVISIBLE);
                    replaceFragment(0);
                }else if (checkedId == R.id.Rbut_2){
                    indicatorView.setVisibility(View.INVISIBLE);
                    indicatorView1.setVisibility(View.VISIBLE);
                    replaceFragment(1);
                }
            }
        });
    }

    // 切换main的碎片的方法
    private void replaceFragment(int i) {
        // 碎片管理器
        FragmentManager manger = getSupportFragmentManager();
        FragmentTransaction ft = manger.beginTransaction();
        hideAllFragments(ft);

        switch (i) {
            case 0:
                if (tab_fragment[0] == null) {
                    tab_fragment[0] = new TwentyFourHoursHotSearch();//TwentyFourHoursHotSearch   TwentyFourOptionalHotSearch


                    ((TwentyFourHoursHotSearch) tab_fragment[0]).setJumPageListener(new TwentyFourHoursHotSearch.JumpPageListener() {
                        @Override
                        public void onCheckedChanged(int position) {

                            if (position == 0) {
                                mRbut_hour.setChecked(true);
                            } else if (position == 1) {
                                mRbut_optionalHour.setChecked(true);
                            }
                        }
                    });
                    ft.add(R.id.mFrameLayout, tab_fragment[0]);
                } else {
                    ft.show(tab_fragment[0]);
                }
                break;


            case 1:
                if (tab_fragment[1] == null) {
                    tab_fragment[1] = new OptionalRanking();
                    ft.add(R.id.mFrameLayout, tab_fragment[1]);
                } else {
                    ft.show(tab_fragment[1]);
                }
                break;

        }
        ft.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (tab_fragment[0] != null) {
            ft.hide(tab_fragment[0]);
        }
        if (tab_fragment[1] != null) {
            ft.hide(tab_fragment[1]);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hotsearchstock;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}

