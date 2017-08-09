package com.tpyzq.mobile.pangu.activity.home.information;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.InfoPagerAdapter;
import com.tpyzq.mobile.pangu.activity.home.information.view.HotPintsPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.SopCastPager;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 资讯首页
 * 修改MagicIndicator 控件预加载方式，以及资讯栏目的编辑保存   by   longfeng
 */
public class InformationHomeActivity extends BaseActivity implements View.OnClickListener,InterfaceCollection.InterfaceCallback{
    private static final String TAG = "InformationHomeActivity";
    private MagicIndicator mi_info;
    private ViewPager mViewPager;
    private ArrayList<String> mTitleList,mClassNoList ;            //显示页卡标题集合
    private ArrayList<String> listTab;                              //页卡Tab的数据源，可编辑选中条数
    private int INFOMATION = 5;
    private int currentItem ;
    private String currentTitle;
    private SimplePagerTitleView sptv;
    private CommonNavigator commonNavigator;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    HotPintsPager hotPintsPager = null;                     //要闻
    SopCastPager sopCastPager = null;                       //直播
    InfoPagerAdapter tabAdapter;

    @Override
    public void initView() {
        currentItem = getIntent().getIntExtra("currentItem", 0);
        mInterface.queryClasslist(TAG,this);
        this.findViewById(R.id.ivInformationHome_back).setOnClickListener(this);        //返回按钮
        this.findViewById(R.id.ivCompile).setOnClickListener(this);                      //编辑按钮
        mi_info = (MagicIndicator) findViewById(R.id.mi_info);
        mViewPager = (ViewPager) this.findViewById(R.id.vpStoryHome);
        commonNavigator = new CommonNavigator(this);
        mTitleList = new ArrayList<>();
        mClassNoList = new ArrayList<>();
        listTab = new ArrayList<>();
        tabAdapter = new InfoPagerAdapter(mTitleList,this);   //初始化适配器
        mViewPager.setAdapter(tabAdapter);                   //适配
        mViewPager.setCurrentItem(currentItem);
        ViewPager.OnPageChangeListener pc = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                currentTitle = tabAdapter.getPageTitle(position).toString();
                tabAdapter.getPager(currentTitle).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mViewPager.addOnPageChangeListener(pc);
        initMagicIndicator();
    }


    @Override
    public void callResult(ResultInfo info) {
        if(info.getCode().equals("200")){
            Object obj = info.getData();
            if(obj!=null&&obj instanceof List){
                List<InformationEntity> list = new ArrayList<>();
                InformationEntity yw = new InformationEntity();
                yw.setClassname("要闻");
                yw.setClassno("1");
                list.add(yw);
                InformationEntity zb = new InformationEntity();
                zb.setClassname("直播");
                zb.setClassno("2");
                list.add(zb);
                list.addAll((List)obj);
                if(list.size()>0)
                    putTabList(list);
            }
        }else
            getTabList();
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
                        currentItem = index;
                        currentTitle = mTitleList.get(index);
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

    private void putTabList(List<InformationEntity> list) {
        listTab.clear();
        mClassNoList.clear();
        String sortTab = SpUtils.getString(this,"sortTab","");
        String ziXunTab = SpUtils.getString(this,"ziXunTab","");
        StringBuffer sb = new StringBuffer();
        if(!TextUtils.isEmpty(sortTab)){
            List<String> tabs = Arrays.asList(sortTab.split(","));
            for (InformationEntity entity:list) {
                if(tabs.contains(entity.getClassname())){//列表中包含列名
                    if(TextUtils.isEmpty(SpUtils.getString(this,entity.getClassname(),"")))
                        SpUtils.putString(this,entity.getClassname(),entity.getClassno());
                }else
                    SpUtils.putString(this,entity.getClassname(),entity.getClassno());
                listTab.add(entity.getClassname());
                mClassNoList.add(entity.getClassno());
                sb.append(entity.getClassname()).append(",");
            }
            String mSortTab = sb.substring(0,sb.length()-1);
            if(!sortTab.equals(mSortTab)) {//存储列表和返回列表不相等（新加的无所谓，因为列表会覆盖，删除的必须删除本地存储的key-value）
                SpUtils.putString(this, "sortTab", mSortTab);
                List<String> mTabs = Arrays.asList(mSortTab.split(","));
                List<String> zixunTabs = Arrays.asList(ziXunTab.split(","));
                for (String tab:tabs){
                     if(!mTabs.contains(tab)) {
                         SpUtils.removeKey(this, tab);
                         ziXunTab = removeZiXun(zixunTabs,tab);
                     }
                }
                SpUtils.putString(this, "ziXunTab", ziXunTab);
            }
        }else{
            for (InformationEntity entity:list) {
                SpUtils.putString(this,entity.getClassname(),entity.getClassno());
                listTab.add(entity.getClassname());
                mClassNoList.add(entity.getClassno());
                sb.append(entity.getClassname()).append(",");
            }
            SpUtils.putString(this, "sortTab", sb.substring(0,sb.length()-1));
        }
        sortTab = SpUtils.getString(this,"sortTab","");
        List<String> tabs = Arrays.asList(sortTab.split(","));
        for (int i = 0; i< tabs.size();i++) {
            if(currentItem==i)
                currentTitle = tabs.get(i);
        }
        addData();
    }

    /**
     * 如果保存的列表里没有服务器删除数据则从列表里删除
     * @param zixunTabs
     * @param tab
     * @return
     */
    public String removeZiXun(List<String> zixunTabs,String tab){
        StringBuffer sb = new StringBuffer();
        for (String zixun:zixunTabs){
            if(!zixun.equals(tab))
                sb.append(zixun).append(",");
        }
        return sb.substring(0,sb.length()-1);
    }

    private void getTabList(){
        String sortTab = SpUtils.getString(this, "sortTab", "");
        listTab.clear();
        listTab.add("要闻");
        listTab.add("直播");
        mClassNoList.clear();
        mClassNoList.add("1");
        mClassNoList.add("2");
        SpUtils.putString(this,"要闻","1");
        SpUtils.putString(this,"直播","2");
        currentTitle = SpUtils.getString(this,"currentTitle","");
        if(!TextUtils.isEmpty(sortTab)&&sortTab.contains(",")){
            String[] title = sortTab.split(",");
            for (int i = 0; i < title.length; i++) {
                if(title[i].equals(currentTitle))
                    currentItem = i;
                listTab.add(title[i]);
                mClassNoList.add(SpUtils.getString(this,title[i],""));
            }
        }
        addData();
    }

    private void addData() {
        mTitleList.clear();
        String zixuntab = SpUtils.getString(this, "ziXunTab", "");      //获取  选中的Tab 的名称
        if (!TextUtils.isEmpty(zixuntab)) {
            mClassNoList.clear();
            if(zixuntab.contains(",")){
                String[] title = zixuntab.split(",");
                for (int i = 0; i < title.length; i++) {
                    if(title[i].equals(currentTitle))
                        currentItem = i;
                    mTitleList.add(title[i]);
                    mClassNoList.add(SpUtils.getString(this,title[i],""));
                }
            }else {
                mTitleList.add(zixuntab);
                mClassNoList.add(SpUtils.getString(this,zixuntab,""));
            }
        }else{
            mTitleList.addAll(listTab);
        }
        if(mTitleList.size() < 5){
            commonNavigator.setAdjustMode(true);
        }else{
            commonNavigator.setAdjustMode(false);
        }
        commonNavigator.notifyDataSetChanged();
        tabAdapter.setClassNoList(mClassNoList);
        mViewPager.setCurrentItem(currentItem);
        tabAdapter.getPager(currentTitle).initData();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivInformationHome_back:       //点击销毁当前界面
                this.finish();
                break;
            case R.id.ivCompile:                     //点击跳转编辑  Tab  界面
                listTab = new ArrayList(new HashSet(listTab));
                SpUtils.putString(this,"currentTitle",currentTitle);
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
        if (requestCode == INFOMATION && resultCode == RESULT_OK)
            getTabList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(hotPintsPager!=null){
            hotPintsPager.destroy();
            hotPintsPager = null;
        }
        if(sopCastPager!=null){
            sopCastPager.destroy();
            sopCastPager = null;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_information_home;
    }

}
