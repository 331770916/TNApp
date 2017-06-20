package com.tpyzq.mobile.pangu.activity.market.selfChoice;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeObsever;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.activity.market.QuotaionAdapter;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews.SelfChoiceNewsTab;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.childQuotation.SelfChoiceQuotationTab;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.defaultSelfChoic.SelfChoiceDefaultTab;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.magicindicator.FragmentContainerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.ViewPagerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/6/24.
 */
public class SelfChoiceFragment extends BaseFragment implements BaseTabPager.LoadInterface,BaseTabPager.OnPageChangeListener,
        View.OnClickListener, SelfChoiceDefaultTab.SelfChoicDefaultImportListener,
        HomeObsever, SelfChoiceQuotationTab.SelfChoiceDataCallback {
    boolean jumpSelfChoiceNews = false;
    private static final String TAG = "SelfChoiceFragment";
    private SelfChoiceQuotationTab mSelfChoiceQuotationTab;
    private SelfChoiceNewsTab mSelfChoiceNewsTab;
    private SelfChoiceDefaultTab mSelfChoiceDefaultTab;
    private Animation mAnimation, mAnimation2;
    private ArrayList<BaseTabPager> mTabs;
    private QuotaionAdapter mAdapter;
    private ViewPager mViewPager;
    private Activity mActivity;
    private RelativeLayout mBottomLayout;
    private SelfChoiceCallback mSelfChoiceCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void initView(View view) {
        isShow = true;
        HomeFragmentHelper.mOberservers.add(this);

        mAnimation    = AnimationUtils.loadAnimation(mActivity, R.anim.drawer_up_animation);
        mAnimation2   = AnimationUtils.loadAnimation(mActivity, R.anim.drawer_down_animation);

        mViewPager = (ViewPager) view.findViewById(R.id.choice_viewPager);
        mBottomLayout = (RelativeLayout) view.findViewById(R.id.selfchoiceRegisterLayout);
        mBottomLayout.setOnClickListener(this);

        MagicIndicator tabLineDisClickIndicator = (MagicIndicator) view.findViewById(R.id.choice_tabPageIndicator);
        String [] strings = {"自选股行情", "自选股新闻"};
        List<String> titleList = Arrays.asList(strings);

        FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper();
        fragmentContainerHelper.handlePageSelected(0, false);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(true);


        commonNavigator.setAdapter(new SelfChoiceNavigatorAdapter(titleList, mViewPager, getActivity()));
        tabLineDisClickIndicator.setNavigator(commonNavigator);
        fragmentContainerHelper.attachMagicIndicator(tabLineDisClickIndicator);
        ViewPagerHelper.bind(tabLineDisClickIndicator, mViewPager);

        ArrayList<BaseTabPager> tabs1 = new ArrayList<>();
        mAdapter = new QuotaionAdapter();
        mTabs = new ArrayList<>();

        mSelfChoiceQuotationTab = new SelfChoiceQuotationTab(mActivity, tabs1);
        mSelfChoiceNewsTab = new SelfChoiceNewsTab(mActivity, tabs1);
        mSelfChoiceDefaultTab = new SelfChoiceDefaultTab(mActivity, tabs1);
        mSelfChoiceDefaultTab.setImportListener(this);

        mSelfChoiceQuotationTab.setLoadInterface(this);
        mSelfChoiceNewsTab.setLoadInterface(this);
        mSelfChoiceDefaultTab.setLoadInterface(this);
        ArrayList<StockInfoEntity> beans = Db_PUB_STOCKLIST.queryStockListDatas();
        if (beans != null && beans.size() > 0) {
            mTabs.add(mSelfChoiceQuotationTab);
        } else {
            mTabs.add(mSelfChoiceDefaultTab);
        }

        mTabs.add(mSelfChoiceNewsTab);

        mAdapter.setDatas(mTabs);

        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(0);

        if (jumpSelfChoiceNews) {
            mViewPager.setCurrentItem(1);
        } else {
            mViewPager.setCurrentItem(0);
        }

        mSelfChoiceDefaultTab.setViewPager(mViewPager);
        mSelfChoiceQuotationTab.setViewPager(mViewPager);
        mSelfChoiceNewsTab.setViewPager(mViewPager);

        mSelfChoiceDefaultTab.addOnPageChangeListener(this);
        mSelfChoiceQuotationTab.addOnPageChangeListener(this);
        mSelfChoiceNewsTab.addOnPageChangeListener(this);

        if (!Db_PUB_USERS.isRegister()) {
            mBottomLayout.startAnimation(mAnimation);
            mBottomLayout.setVisibility(View.VISIBLE);
        }

        mSelfChoiceQuotationTab.setSelfChoiceDataCallback(this);

    }

    public void setCurrentItem(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
        MarketFragment.pageIndex = position;
    }
    boolean isShow = false;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        LogHelper.e(TAG,"onHiddenChanged *********:"+hidden);
        if(!hidden){
            MarketFragment.pageIndex=0;
        }
        isShow = !hidden;
        if (mSelfChoiceNewsTab != null) {
            mSelfChoiceNewsTab.setUserVisibleHint(hidden);
        }

        if (mSelfChoiceQuotationTab != null) {
            if(hidden)
            {
                mSelfChoiceQuotationTab.toStopConnect();
            }
            mSelfChoiceQuotationTab.onHiddenChanged(hidden);
        }

        if (mViewPager != null) {
            if (jumpSelfChoiceNews) {
                mViewPager.setCurrentItem(1);
            } else {
                mViewPager.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.e(TAG,"onHiddenChanged *******isShow:**:"+isShow);
        boolean isStartConnect = SpUtils.getBoolean(CustomApplication.getContext(), MarketFragment.MARKET_TAG, true);
        if(isShow){
            refrushData();
            if (!isStartConnect) {
                mSelfChoiceQuotationTab.onResume();
                //            mSelfChoiceNewsTab.onResume();
            }

            if (jumpSelfChoiceNews) {
                mViewPager.setCurrentItem(1);
            } else {
                mViewPager.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mActivity, ShouJiZhuCeActivity.class);
        intent.putExtra("Identify","2");
        mActivity.startActivity(intent);
    }

    /**
     * 刷新数据库内容， 根据内容显示不同布局
     */
    private void refrushData() {
        mTabs.clear();
        ArrayList<StockInfoEntity> beans = Db_PUB_STOCKLIST.queryStockListDatas();

        if (beans != null && beans.size() > 0) {
            mTabs.add(mSelfChoiceQuotationTab);
        } else {
            mTabs.add(mSelfChoiceDefaultTab);
        }

        mTabs.add(mSelfChoiceNewsTab);
        mAdapter.setDatas(mTabs);
        mViewPager.setAdapter(mAdapter);


        if (!Db_PUB_USERS.isRegister()) {
            mBottomLayout.startAnimation(mAnimation);
            mBottomLayout.setVisibility(View.VISIBLE);
        } else {
            mBottomLayout.startAnimation(mAnimation2);
            mBottomLayout.setVisibility(View.GONE);
        }
    }

    private ShareTypeListener mShareTypeListener;
    public void setShareTypeListener(ShareTypeListener shareTypeListener) {
        mShareTypeListener = shareTypeListener;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSelfChoiceQuotationTab != null) {
            mSelfChoiceQuotationTab.onStop();
        }

        if (mSelfChoiceNewsTab != null) {
            mSelfChoiceNewsTab.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSelfChoiceQuotationTab.onDestory();
        mSelfChoiceNewsTab.onDestory();
    }

    @Override
    public void onPageSelected(int position) {

        if (!Db_PUB_USERS.isRegister()) {
            if (position == 0) {
                mBottomLayout.startAnimation(mAnimation);
                mBottomLayout.setVisibility(View.VISIBLE);
                jumpSelfChoiceNews = false;
            } else if (position == 1) {
                mBottomLayout.startAnimation(mAnimation2);
                mBottomLayout.setVisibility(View.GONE);
                jumpSelfChoiceNews = true;
            }
        } else {
            if (position == 0) {
                jumpSelfChoiceNews = false;
                if (mShareTypeListener != null) {
                    mShareTypeListener.getShareType("1");
                }
            } else if (position == 1) {
                jumpSelfChoiceNews = true;
                if (mShareTypeListener != null) {
                    mShareTypeListener.getShareType("2");
                }
            }
        }
    }

    public void setSelfChoiceCallback(SelfChoiceCallback selfChoiceCallback) {
        mSelfChoiceCallback = selfChoiceCallback;
    }

    @Override
    public void callbackSelfchoiceDatas(ArrayList<StockInfoEntity> beans) {
        if (mSelfChoiceCallback != null) {
            mSelfChoiceCallback.callbackSelfchoiceDatas(beans);
        }
    }

    @Override
    public void importHoldResult() {
        refrushData();
        mSelfChoiceQuotationTab.onResume();
        mSelfChoiceNewsTab.onResume();
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_selfchoice;
    }

    @Override
    public void update(int position, String tag) {
        if ("selfChoice".equals(tag) || "selfChoiceNews".equals(tag)) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(position);
            }
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

    public interface SelfChoiceCallback{
        public void callbackSelfchoiceDatas(ArrayList<StockInfoEntity> beans);
    }

    public interface ShareTypeListener {
        public void getShareType(String type);
    }

}
