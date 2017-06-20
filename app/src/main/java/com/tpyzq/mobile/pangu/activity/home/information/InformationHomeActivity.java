package com.tpyzq.mobile.pangu.activity.home.information;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.InfoPagerAdapter;
import com.tpyzq.mobile.pangu.activity.home.information.view.BaseInfoPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.CommercePager;
import com.tpyzq.mobile.pangu.activity.home.information.view.ConsumePager;
import com.tpyzq.mobile.pangu.activity.home.information.view.EnergyPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.FinancialPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.HotPintsPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.IndustryPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.InformationPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.MaterialsPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.MedicalPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.PublicUtilitiesPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.RealtyPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.SopCastPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.TelecomPager;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.ViewPagerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯首页
 * 修改MagicIndicator 控件预加载方式，以及资讯栏目的编辑保存   by   longfeng
 */
public class InformationHomeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "InformationHomeActivity";
    private int point = 0;
    private MagicIndicator mi_info;
    private ViewPager mViewPager;
    private ArrayList<String> mTitleList ;            //显示页卡标题集合
    private List<BaseInfoPager> mViewList ;   //页卡视图集合
    private ArrayList<String> listTab;                              //页卡Tab的数据源，可编辑选中条数
    private ArrayList<String> fragmentList;                         //Fragment的名称集合
    private int INFOMATION = 5;
    private int more;
    private int currentItem ;

    private SimplePagerTitleView sptv;
    private CommonNavigator commonNavigator;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    //pager
    HotPintsPager hotPintsPager = null;                     //热点
    SopCastPager sopCastPager = null;                       //直播
    FinancialPager financialPager = null;                   //金融业
    RealtyPager realtyPager = null;                         //房地产
    InformationPager informationPager = null;               //信息技术
    TelecomPager telecomPager = null;                       //电信
    MedicalPager medicalPager = null;                       //医疗
    IndustryPager industryPager = null;                     //工业
    EnergyPager energyPager = null;                         //能源
    PublicUtilitiesPager publicUtilitiesPager = null;      //公共事业
    ConsumePager consumePager = null;                       //消费
    MaterialsPager materialsPager = null;                   //原材料
    CommercePager commercePager = null;                     //商贸
    InfoPagerAdapter tabAdapter;

    @Override
    public void initView() {
        more = getIntent().getIntExtra("more", -1);
        currentItem = getIntent().getIntExtra("currentItem", 0);
        this.findViewById(R.id.ivInformationHome_back).setOnClickListener(this);        //返回按钮
        this.findViewById(R.id.ivCompile).setOnClickListener(this);                      //编辑按钮
        mi_info = (MagicIndicator) findViewById(R.id.mi_info);
        mViewPager = (ViewPager) this.findViewById(R.id.vpStoryHome);
        commonNavigator = new CommonNavigator(this);
        mTitleList = new ArrayList<>();
        mViewList = new ArrayList<BaseInfoPager>();
        listTab = new ArrayList<String>();
        getTabList();
        addData();

//        hotPintsPager = new HotPintsPager(this);
//        sopCastPager = new SopCastPager(this);
//        financialPager = new FinancialPager(this);
//        realtyPager = new RealtyPager(this);
//        informationPager = new InformationPager(this);
//        telecomPager = new TelecomPager(this);
//        medicalPager = new MedicalPager(this);
//        industryPager = new IndustryPager(this);
//        energyPager = new EnergyPager(this);
//        publicUtilitiesPager = new PublicUtilitiesPager(this);
//        consumePager = new ConsumePager(this);
//        materialsPager = new MaterialsPager(this);
//        commercePager = new CommercePager(this);

//        mViewList.add(hotPintsPager);
//        mViewList.add(sopCastPager);
//        mViewList.add(financialPager);
//        mViewList.add(realtyPager);
//        mViewList.add(informationPager);
//        mViewList.add(telecomPager);
//        mViewList.add(medicalPager);
//        mViewList.add(industryPager);
//        mViewList.add(energyPager);
//        mViewList.add(publicUtilitiesPager);
//        mViewList.add(consumePager);
//        mViewList.add(materialsPager);
//        mViewList.add(commercePager);

        tabAdapter = new InfoPagerAdapter(mTitleList,this);   //初始化适配器
        tabAdapter.setBaseInfoPager(mViewList);
        mViewPager.setAdapter(tabAdapter);                   //适配
        mViewPager.setCurrentItem(currentItem);
        ViewPager.OnPageChangeListener pc = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
//                mViewList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mViewPager.addOnPageChangeListener(pc);
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleList == null ? 0 : mTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                final SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mTitleList.get(index));
                simplePagerTitleView.setNormalColor(ColorUtils.BLACK);
                simplePagerTitleView.setSelectedColor(ColorUtils.BLUE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sptv != simplePagerTitleView) {
                            sptv = simplePagerTitleView;
                        } else {
                            return;
                        }
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.TRANSPARENT);
                return indicator;
            }
        };
        commonNavigator.setSkimOver(true);

        commonNavigator.setAdapter(commonNavigatorAdapter);
        mi_info.setNavigator(commonNavigator);
        mi_info.onPageSelected(currentItem);
        ViewPagerHelper.bind(mi_info, mViewPager);
    }

    private void getTabList() {
        String sortTab = SpUtils.getString(this, "sortTab", "");
        if (TextUtils.isEmpty(sortTab)) {
            listTab.add("热点");
            listTab.add("要闻直播");
            listTab.add("金融业");
            listTab.add("房地产业");
            listTab.add("信息技术");
            listTab.add("电信");
            listTab.add("医疗");
            listTab.add("工业");
            listTab.add("能源");
            listTab.add("公共事业");
            listTab.add("消费");
            listTab.add("原材料");
            listTab.add("商贸");
        }else{
            listTab.clear();
            String[] title = sortTab.split(",");
            for (int i = 0; i < title.length; i++) {
                listTab.add(title[i]);
            }
        }
    }

    private void addData() {
        String zixuntab = SpUtils.getString(this, "ziXunTab", "");       //获取  选中的Tab 的名称
        if (TextUtils.isEmpty(zixuntab)) {
            mTitleList.addAll(listTab);
            return;
        }
        mTitleList.clear();
        String[] title = zixuntab.split(",");
        for (int i = 0; i < title.length; i++) {
            mTitleList.add(title[i]);
        }
        if(mTitleList.size() < 5){
            commonNavigator.setAdjustMode(true);
        }else{
            commonNavigator.setAdjustMode(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivInformationHome_back:       //点击销毁当前界面
                this.finish();
                break;
            case R.id.ivCompile:                     //点击跳转编辑  Tab  界面
                Intent intent = new Intent();
                intent.setClass(this, CompileTabActivity.class);
                intent.putStringArrayListExtra("listTab", listTab);
                intent.putStringArrayListExtra("myTab", mTitleList);
                startActivityForResult(intent, INFOMATION);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == INFOMATION && resultCode == RESULT_OK) {
            point = intent.getIntExtra("point", 0);
//            String zixuntab = SpUtils.getString(this, "ziXunTab", "");       //获取  选中的Tab 的名称
//            mTitleList.clear();
//            String[] title = zixuntab.split(",");
//            for (int i = 0; i < title.length; i++) {
//                mTitleList.add(title[i]);
//            }
            getTabList();
            addData();
            commonNavigator.notifyDataSetChanged();
            tabAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(currentItem);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(HotPintsPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(SopCastPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(FinancialPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(RealtyPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(InformationPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(TelecomPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(MedicalPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(IndustryPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(EnergyPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(PublicUtilitiesPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(ConsumePager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(MaterialsPager.TAG);
        NetWorkUtil.cancelSingleRequestByTag(CommercePager.TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_information_home;
    }

}
