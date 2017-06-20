package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeObsever;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.activity.market.QuotaionAdapter;
import com.tpyzq.mobile.pangu.activity.market.quotation.newstock.TabNewStock;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.tabLineIndicator.TabLineIndicator;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/6/24.\
 * guohuiz 重构 2017/5/22
 * 行情
 */
public class QuotaionFragment extends BaseFragment implements BaseTabPager.LoadInterface,
        BaseTabPager.MarketFragmentEditIsGoneListener, HomeObsever {

    private TabHuShen mTabHuShen;
    private TabPlate    mTabPlate;
    private TabNewStock mTabNewStock;
    private TabExponent mTabExponent;
    private TabOther   mTabOther;
    private Activity    mActivity;

//    private TabHuShenView mTabHuShenView;
    private TabLineIndicator mTabLineIndicator;
    private ViewPager mViewPager;
    private final String TAG = QuotaionFragment.class.getSimpleName();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void initView(View view) {

        HomeFragmentHelper.mOberservers.add(this);

        mTabLineIndicator = (TabLineIndicator) view.findViewById(R.id.tabPageIndicator);
        mViewPager = (ViewPager) view.findViewById(R.id.selfchoice_viewPager);
        ArrayList<BaseTabPager> tabs1 = new ArrayList<>();
        mTabHuShen = new TabHuShen(mActivity, tabs1);
        mTabPlate = new TabPlate(mActivity, tabs1);
        mTabNewStock = new TabNewStock(mActivity, tabs1);
        mTabExponent = new TabExponent(mActivity, tabs1);
        mTabOther = new TabOther(mActivity, tabs1);
//
        mTabHuShen.setLoadInterface(this);
        mTabPlate.setLoadInterface(this);
        mTabNewStock.setLoadInterface(this);
        mTabExponent.setLoadInterface(this);
//
        mTabHuShen.setMarketFramgmentEditIsGoneInterface(this);
        mTabPlate.setMarketFramgmentEditIsGoneInterface(this);
        mTabNewStock.setMarketFramgmentEditIsGoneInterface(this);
        mTabExponent.setMarketFramgmentEditIsGoneInterface(this);
        mTabOther.setMarketFramgmentEditIsGoneInterface(this);

        QuotaionAdapter adapter = new QuotaionAdapter();
        ArrayList<BaseTabPager> tabs = new ArrayList<>();
        tabs.add(mTabHuShen);
        tabs.add(mTabPlate);
        tabs.add(mTabNewStock);
        tabs.add(mTabExponent);
        tabs.add(mTabOther);
        adapter.setDatas(tabs);
        mTabHuShen.setViewPager(mViewPager);
        mTabPlate.setViewPager(mViewPager);
        mTabNewStock.setViewPager(mViewPager);
        mTabExponent.setViewPager(mViewPager);
        mTabOther.setViewPager(mViewPager);
        mViewPager.setAdapter(adapter);
        if (ConstantUtil.jumpHangqing) {
            mTabLineIndicator.setViewPager(mViewPager, 2);
        } else {
            mTabLineIndicator.setViewPager(mViewPager, 0);
        }
    }

    @Override
    public void loading() {
        MarketFragment marketFragment = ((MarketFragment)(getParentFragment()));
        if (marketFragment != null) {
            marketFragment.loading();
        }
    }

    @Override
    public void complited() {
        MarketFragment marketFragment = ((MarketFragment)(getParentFragment()));
        if (marketFragment != null) {
            marketFragment.complited();
        }
    }

    @Override
    public void gone() {
        MarketFragment marketFragment = ((MarketFragment)(getParentFragment()));
        if (marketFragment != null) {
            marketFragment.editSelfivGone();
        }
    }

    @Override
    public void visible(String tag) {
        MarketFragment marketFragment = ((MarketFragment)(getParentFragment()));
        if (marketFragment != null) {
            marketFragment.editSelfivVisibile(tag);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        mTabHuShenView.setUserVisibleHint(isVisibleToUser);
        mTabHuShen.setUserVisibleHint(isVisibleToUser);
        mTabPlate.setUserVisibleHint(isVisibleToUser);
        mTabNewStock.setUserVisibleHint(isVisibleToUser);
        mTabExponent.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isStartConnect = SpUtils.getBoolean(CustomApplication.getContext(), MarketFragment.MARKET_TAG, true);

        if (!isStartConnect) {
//            mTabHuShenView.onResume();
        if(mTabHuShen != null) {
            mTabHuShen.onResume();
        }
        if (mTabPlate != null) {
            mTabPlate.onResume();
        }
        if (mTabNewStock != null) {
            mTabNewStock.onResume();
        }
        if (mTabExponent != null) {
            mTabExponent.onResume();
        }
        if (mTabOther != null) {
            mTabOther.onResume();
        }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mTabHuShen != null){
            mTabHuShen.onStop();
        }
        if(mTabPlate != null){
           mTabPlate.onStop();
        }
        if(mTabNewStock != null){
           mTabNewStock.onStop();
        }
        if(mTabExponent != null){
            mTabExponent.onStop();
        }
        if(mTabOther != null){
           mTabOther.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTabHuShen.onDestory();
        mTabPlate.onDestory();
        mTabNewStock.onDestory();
        mTabExponent.onDestory();
        mTabOther.onDestory();
    }

    @Override
    public void update(int position, String tag) {
        if ("hangqing".equals(tag)) {
            mTabLineIndicator.setViewPager(mViewPager, 2);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            MarketFragment.pageIndex=1;
        }
        if(mTabHuShen!=null)
        {
            mTabHuShen.onHiddenChanged(hidden);
        }
        if(mTabPlate!=null){
            mTabPlate.onHiddenChanged(hidden);
        }
        if(mTabNewStock!=null){
            mTabNewStock.onHiddenChanged(hidden);
        }
        if(mTabExponent!=null)
        {
            mTabExponent.onHiddenChanged(hidden);
        }
        if(mTabOther!=null)
        {
            mTabOther.onHiddenChanged(hidden);
        }
//        LogHelper.e(TAG,"hidden:"+hidden);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_quotation;
    }

}
