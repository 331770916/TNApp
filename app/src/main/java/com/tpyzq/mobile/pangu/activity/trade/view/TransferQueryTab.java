package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.TransferAcountsAdapter;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITabDataObserver;
import com.tpyzq.mobile.pangu.view.tabLineIndicator.TabLineIndicator;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/24.
 * 转账查询
 */
public class TransferQueryTab extends BaseTransferSubjectTabView {

    public TransferQueryTab(ViewGroup viewGroup, Activity activity, boolean isMainTab,
                            ArrayList<ITabDataObserver> observers,
                            BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        super(viewGroup, activity, isMainTab, observers, banksTransferAccountsResultCode);
    }

    private TodayTab mTab1;
    private WeekTab mTab2;
    private MounthTab mTab3;
    private ThreeMounthTab mTab4;
    private boolean isFromIndexActivity;
    public static final String TAG = "TransferQueryTab";

    @Override
    public void initView(ViewGroup viewGroup, View view, Activity activity, boolean isMainTab, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        TabLineIndicator indicator = (TabLineIndicator) view.findViewById(R.id.transferPageIndicator);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.transferQueryViewPager);
        TransferAcountsAdapter adapter = new TransferAcountsAdapter();
        viewPager.setAdapter(adapter);

        indicator.setViewPager(viewPager, 0);

        ArrayList<BaseTransferObserverTabView> queryTabs = new ArrayList<>();
        mTab1 = new TodayTab(activity, this, queryTabs, banksTransferAccountsResultCode);
        mTab2= new WeekTab(activity, this, queryTabs, banksTransferAccountsResultCode);
        mTab3 = new MounthTab(activity, this, queryTabs, banksTransferAccountsResultCode);
        mTab4 = new ThreeMounthTab(activity, this, queryTabs, banksTransferAccountsResultCode);
        CustomTab  tab5 = new CustomTab(activity, this, queryTabs, banksTransferAccountsResultCode);

        mTab1.setViewPager(viewPager);
        mTab2.setViewPager(viewPager);
        mTab3.setViewPager(viewPager);
        mTab4.setViewPager(viewPager);
        tab5.setViewPager(viewPager);

        ArrayList<BaseTransferObserverTabView> tabs = new ArrayList<>();
        tabs.add(mTab1);
        tabs.add(mTab2);
        tabs.add(mTab3);
        tabs.add(mTab4);
        tabs.add(tab5);

        adapter.setDatas(tabs);
    }

    @Override
    public void onResume() {
        if (mTab1 != null) {
            mTab1.setFromIndexActivity(isFromIndexActivity);
            mTab1.onResume();
        }

        if (mTab2 != null) {
            mTab2.onResume();
        }

        if (mTab3 != null) {
            mTab3.onResume();
        }

        if (mTab4 != null) {
            mTab4.onResume();
        }
    }

    @Override
    public void onStop() {
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }


    public void setIsFromIndexActivity(boolean isFromIndexActivity) {
        this.isFromIndexActivity = isFromIndexActivity;
    }

    /**
     * 更新
     */
    public void setUpdateData() {

        if (mTab1 != null) {
            mTab1.update(null, false, 0);
        }

        if (mTab2 != null) {
            mTab2.update(null, false, 0);
        }

        if (mTab3 != null) {
            mTab3.update(null, false, 0);
        }

        if (mTab4 != null) {
            mTab4.update(null, false, 0);
        }
    }

    @Override
    public int getTabLayoutId() {
        return R.layout.bank_transfer_query;
    }
}
