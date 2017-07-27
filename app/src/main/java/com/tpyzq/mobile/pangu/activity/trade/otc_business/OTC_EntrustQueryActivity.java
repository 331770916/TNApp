package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainCustom;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainOneMonth;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainOneWeek;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainThreeMonth;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_BargainToday;
import com.tpyzq.mobile.pangu.adapter.trade.OtcQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * OTC 委托查询界面
 * 刘泽鹏
 */
public class OTC_EntrustQueryActivity extends BaseActivity implements View.OnClickListener {

    private OTC_BargainToday        mOTC_BargainToday;
    private OTC_BargainOneWeek      mOTC_BargainOneWeek;
    private OTC_BargainOneMonth     mOTC_BargainOneMonth;
    private OTC_BargainThreeMonth   mOTC_BargainThreeMonth;
    private OTC_BargainCustom       mOTC_BargainCustom;

    @Override
    public void initView() {

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("委托查询");

        mOTC_BargainToday.type = mOTC_BargainToday.ENTRUST_TYPE;
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpOTCEntrustQuery);
        mOTC_BargainToday = new OTC_BargainToday();
        mOTC_BargainOneWeek = new OTC_BargainOneWeek();
        mOTC_BargainOneMonth = new OTC_BargainOneMonth();
        mOTC_BargainThreeMonth = new OTC_BargainThreeMonth();
        mOTC_BargainCustom = new OTC_BargainCustom();

        List<Fragment> pagers = new ArrayList<>();
        pagers.add(mOTC_BargainToday);
        pagers.add(mOTC_BargainOneWeek);
        pagers.add(mOTC_BargainOneMonth);
        pagers.add(mOTC_BargainThreeMonth);
        pagers.add(mOTC_BargainCustom);

        OtcQueryAdapter adapter = new OtcQueryAdapter(getSupportFragmentManager(), pagers);
        viewPager.setAdapter(adapter);

        String[] bargainQuery_tab = {"今日", "一周内", "一月内", "三月内", "自定义"};
        TabLayout tabLayout = (TabLayout) findViewById(R.id.entrustTablayout);
        for (String tabTitle : bargainQuery_tab) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitle));
        }

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onkeyflag = super.onKeyDown(keyCode, event);
        if (mOTC_BargainToday != null) {
            onkeyflag = mOTC_BargainToday.onKeyDown(keyCode, event);
        }

        if (mOTC_BargainOneWeek != null) {
            onkeyflag = mOTC_BargainOneWeek.onKeyDown(keyCode, event);
        }

        if (mOTC_BargainOneMonth != null) {
            onkeyflag =  mOTC_BargainOneMonth.onKeyDown(keyCode, event);
        }

        if (mOTC_BargainThreeMonth != null) {
            onkeyflag = mOTC_BargainThreeMonth.onKeyDown(keyCode, event);
        }

        if (mOTC_BargainCustom != null) {
            onkeyflag = mOTC_BargainCustom.onKeyDown(keyCode, event);
        }

        return onkeyflag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__entrust_query;
    }
}
