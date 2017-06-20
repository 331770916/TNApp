package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseTraPager;
import com.tpyzq.mobile.pangu.activity.trade.presenter.TraChooseBreedActivityPresenter;
import com.tpyzq.mobile.pangu.activity.trade.view.HuPager;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.activity.trade.view.SzPager;
import com.tpyzq.mobile.pangu.adapter.trade.TraVpAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.StockInfoBean;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.ViewPagerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;


/**
 * 选择品种交易
 */
public class TraChooseBreedActivity extends BaseActivity implements View.OnClickListener {
    private MagicIndicator mi_choose;
    private ViewPager vp_view;
    private TraVpAdapter traVpAdapter;
    private List<BaseTraPager> baseTraPagers;
    private ImageView iv_back;
    private TimeCount timeCount;

    TraChooseBreedActivityPresenter presenter;

    @Override
    public void initView() {
        mi_choose = (MagicIndicator) findViewById(R.id.mi_choose);
        vp_view = (ViewPager) findViewById(R.id.vp_view);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        initData();
    }

    private void initData() {
        presenter = new TraChooseBreedActivityPresenter(this);
        timeCount = new TimeCount(Helper.getTime(), 1000);
        presenter.getTraBreed();
        timeCount.start();
        iv_back.setOnClickListener(this);
        List<String> list = new ArrayList<>();
        list.add("沪市(10万起)");
        list.add("深市(1千起)");
        setIndicatorListen(list);
        baseTraPagers = new ArrayList<>();
        baseTraPagers.add(HuPager.newInstance(this, presenter));
        baseTraPagers.add(SzPager.newInstance(this, presenter));
        traVpAdapter = new TraVpAdapter(baseTraPagers);
        vp_view.setAdapter(traVpAdapter);
        vp_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                baseTraPagers.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            presenter.getTraBreed();
            timeCount.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

        }
    }


    public void setHuPager(List<StockInfoBean> data) {
        baseTraPagers.get(0).setData(data);
    }

    public void setSzPager(List<StockInfoBean> data) {
        baseTraPagers.get(1).setData(data);
    }

    /**
     * 设置底部页签切换监听器
     */
    private void setIndicatorListen(final List<String> list) {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.blue));
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vp_view.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(getResources().getColor(R.color.blue));
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

        });
        mi_choose.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mi_choose, vp_view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tra_choose_breed;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }
}
