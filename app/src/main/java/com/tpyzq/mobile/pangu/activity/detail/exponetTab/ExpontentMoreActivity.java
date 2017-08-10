package com.tpyzq.mobile.pangu.activity.detail.exponetTab;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2017/8/10.
 * 指数成分股更多
 */

public class ExpontentMoreActivity extends BaseActivity implements View.OnClickListener{

    private ExpontentZhangTab  expontentZhangTab;
    private ExpontentDieTab    expontentDieTab;
    private ExpontentChangeTab expontentChangeTab;

    @Override
    public void initView() {

        String exponentCode = getIntent().getStringExtra("exponentCode");

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("成份股");

        ViewPager viewPager = (ViewPager) findViewById(R.id.expontentViewPager);

        Bundle bundle = new Bundle();
        bundle.putString("exponentCode", exponentCode);

        expontentZhangTab = new ExpontentZhangTab();
        expontentDieTab = new ExpontentDieTab();
        expontentChangeTab = new ExpontentChangeTab();

        expontentZhangTab.setArguments(bundle);
        expontentDieTab.setArguments(bundle);
        expontentChangeTab.setArguments(bundle);

        List<Fragment> pagers = new ArrayList<>();
        pagers.add(expontentZhangTab);
        pagers.add(expontentDieTab);
        pagers.add(expontentChangeTab);

        ExpontentMoreAdapter adapter = new ExpontentMoreAdapter(getSupportFragmentManager(), pagers);
        viewPager.setAdapter(adapter);

        String[] bargain_tab = {"成份股涨幅", "成份股跌幅", "成份股换手率"};
        TabLayout tabLayout = (TabLayout) findViewById(R.id.expontentTablayout);
        for (String tabTitle : bargain_tab) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitle));
        }

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onkeyflag = super.onKeyDown(keyCode, event);
        if (expontentZhangTab != null) {
            onkeyflag = expontentZhangTab.onKeyDown(keyCode, event);
        }

        if (expontentDieTab != null) {
            onkeyflag = expontentDieTab.onKeyDown(keyCode, event);
        }

        if (expontentChangeTab != null) {
            onkeyflag =  expontentChangeTab.onKeyDown(keyCode, event);
        }
        return onkeyflag;
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
    public int getLayoutId() {
        return R.layout.activity_expontent_more;
    }
}
