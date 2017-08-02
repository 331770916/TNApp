package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell.recommend.RecommendFragment;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.hotsell.safebet.SafeBetFragment;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2017/7/28.
 * 热卖理财
 */

public class HotSellActivity extends BaseActivity implements View.OnClickListener {

    private SafeBetFragment mSafeBetFragment;
    private RecommendFragment mRecommendFragment;

    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("热卖理财");

        ViewPager viewPager = (ViewPager) findViewById(R.id.hotSellViewPager);

        List<Fragment> pagers = new ArrayList<>();
        mSafeBetFragment = new SafeBetFragment();
        mRecommendFragment = new RecommendFragment();

        pagers.add(mSafeBetFragment);
        pagers.add(mRecommendFragment);

        HotSellAdapter adapter = new HotSellAdapter(getSupportFragmentManager(), pagers);
        viewPager.setAdapter(adapter);

        String[] hotSell_tab = {"推荐", "稳赢"};
        TabLayout tabLayout = (TabLayout) findViewById(R.id.hotSellTablayout);

        for (String tabTitle : hotSell_tab) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitle));
        }

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onkeyflag = super.onKeyDown(keyCode, event);
        if (mSafeBetFragment != null) {
            onkeyflag = mSafeBetFragment.onKeyDown(keyCode, event);
        }

        if (mRecommendFragment != null) {
            onkeyflag = mRecommendFragment.onKeyDown(keyCode, event);
        }

        return onkeyflag;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hotsell;
    }
}
