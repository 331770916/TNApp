package com.tpyzq.mobile.pangu.activity.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeInfomationObsever;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeInfomationSubject;
import com.tpyzq.mobile.pangu.activity.home.hotsearchstock.HotSearchStockActivity;
import com.tpyzq.mobile.pangu.activity.home.information.InformationHomeActivity;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.homeSafeBetView.HomeSafeBetView;
import com.tpyzq.mobile.pangu.adapter.home.HomeAdapter;
import com.tpyzq.mobile.pangu.adapter.home.HomeHotAdapter;
import com.tpyzq.mobile.pangu.adapter.home.NewHomeInformationAdapter;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.MyHomePageEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToTwentyFourHoursHotSearch;
import com.tpyzq.mobile.pangu.http.doConnect.home.TwentyFourHoursHotSearchConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.GetMyNomePage;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToMyNewsHomePage;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.view.gridview.HomeGridView;
import com.tpyzq.mobile.pangu.view.gridview.MyGridView;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.tpyzq.mobile.pangu.view.gridview.MyScrollView;
import com.tpyzq.mobile.pangu.view.listview.HomeSwitchAdapter;
import com.tpyzq.mobile.pangu.view.loopswitch.AutoSwitchView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by longfeng on 2016/5/12.
 * 重写首页功能，主要改变显示和数据加载方式
 * 首页Fragment
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        ICallbackResult, MyScrollView.OnScrollListener,
        CustomApplication.GetMessageListenr, View.OnClickListener, HomeInfomationSubject,InterfaceCollection.InterfaceCallback {

    private static final String TAG = "HomeFragment";
    private JumpPageListener mJumpPageListener; //页面跳转监听
    /**
     * 轮播控件
     */
    private RelativeLayout vp_carousel;
    private AutoSwitchView loopView;
    private HomeSwitchAdapter adapter;

//    private ConvenientBanner mConvenientBanner;
//    private ArrayList<Bitmap> mBitmaps;
//    private ArrayList<String> mAdType;
    /**
     * 九宫格控件
     */
    private HomeGridView gridView;
    private HomeAdapter mGrideAdapter;
    private ArrayList<Map<String, Object>> mDataSourceList;
    /**
     * 预约产品模块
     */
    private LinearLayout mProductReservationView;

    /**
     * 资讯模块
     */
    private MyListView mNewslistView;
    private NewHomeInformationAdapter mAdapter;
    private ArrayList<InformationEntity> mInformationEntities;
    private ArrayList<String> mInfos;

    /**
     * 热搜模块
     */
    private MyListView hotListView;
    private HomeHotAdapter mHotAdapter;
    private ArrayList<StockInfoEntity> mHotEntities;

    /**
     * 其他功能，如下拉刷新、首页消息等
     */
    private SimpleDraweeView mMiddleImageView; //中间图片
    private TextView mTopTextView; //上推时显示应用标题

    private SimpleRemoteControl simpleRemoteControl;
    private PullToRefreshScrollView mPullDownScrollView;
    private ArrayList<HomeInfomationObsever> mOberservers;
    private ArrayList<MyHomePageEntity> messageList;
    private int hotNum = 5;
    private int zx_num = 4; //定义资讯显示条数
    private String count_count;
    private long startRefreshTime;
    private long endRefreshTime;
    private View headView;


    @Override
    public void initView(View view) {
        startRefreshTime = System.currentTimeMillis();
        mDataSourceList = new ArrayList<>();
        mInformationEntities = new ArrayList<>();
        mHotEntities = new ArrayList<>();
        messageList = new ArrayList<>();
        mOberservers = new ArrayList<>();
        simpleRemoteControl = new SimpleRemoteControl(this);


        mTopTextView = (TextView) view.findViewById(R.id.homeLayoutTopTextView);

        vp_carousel = (RelativeLayout) view.findViewById(R.id.vp_carousel);
        initCarouseView();
        mMiddleImageView = (SimpleDraweeView) view.findViewById(R.id.home_middlebander);
        mMiddleImageView.setBackgroundResource(R.mipmap.openanaccount_shouye);
        mMiddleImageView.setOnClickListener(this);
        mMiddleImageView.setVisibility(View.VISIBLE);


        gridView = (HomeGridView) view.findViewById(R.id.homeGridView);
        mGrideAdapter = new HomeAdapter(true, getActivity());
        mOberservers.add(mGrideAdapter);
        gridView.setAdapter(mGrideAdapter);

        //下拉刷新
        mPullDownScrollView = (PullToRefreshScrollView) view.findViewById(R.id.selfchoice_PullDownScroll);

        mProductReservationView = (LinearLayout) view.findViewById(R.id.homeProductReservation);
        mNewslistView = (MyListView) view.findViewById(R.id.homeListView);
        hotListView = (MyListView) view.findViewById(R.id.homeHotListView);

        initGridView();  //九宫格初始化
        initProductReservation(); //产品预约初始化
        initNewsListView(view);       //资讯初始化
        initHotSearchStockListView();  //热搜初始化
        CustomApplication.setGetMessageListenr(this);  //消息的Handler
        gridView.setOnItemClickListener(this);
        initScrollView(); //下拉刷新初始化
        initData();
    }

    @Override
    public void callResult(ResultInfo info) {
        if ("GetImportant".equals(info.getTag())){
            if(info.getCode().equals("200")){
                headView.setVisibility(View.VISIBLE);
                if(info.getTag().equals("GetImportant")){
                    mInformationEntities = (ArrayList<InformationEntity>)info.getData();
                    mNewslistView.setVisibility(View.VISIBLE);
                    mAdapter.setDatas(mInformationEntities);
                }
            }else{
                mNewslistView.setVisibility(View.GONE);
                headView.setVisibility(View.GONE);
            }
        }else if ("getCarouselImg".equals(info.getTag())){
            if ("0".equals(info.getCode())){
                if (info.getData()!=null){
                   List<List<Map<String,String>>> list = (List<List<Map<String, String>>>) info.getData();
                        if (list.size()>0){
                            List<Map<String,String>> centerImage = list.get(0);   // 中间广告图
                            if(!centerImage.isEmpty())
                                 mMiddleImageView.setImageURI(Uri.parse(centerImage.get(0).get("show_url")));
                        }
                        if (list.size()>1){
                            List<Map<String,String>> topImage = list.get(1);     // 顶部轮播图
                            adapter.setDatas(topImage);
                        }else
                            setDefault();
                }else
                    setDefault();
            }else
                setDefault();
        }

    }

    public void setDefault(){
        List<Map<String,String>> defaultList = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("show_url","default");
        defaultList.add(map);
        adapter.setDatas(defaultList);
    }

    private void initCarouseView() {
        loopView = new AutoSwitchView(mContext);
        loopView.setLayoutParams(new AbsListView.LayoutParams(-1,mContext.getResources().getDimensionPixelSize(R.dimen.size250)));
        vp_carousel.addView(loopView);
        adapter = new HomeSwitchAdapter(getActivity());
        adapter.setListener(mJumpPageListener);
        loopView.setAdapter(adapter);
    }


    public void initData() {
        //请求资讯数据 1级30条
        InterfaceCollection.getInstance().queryImportant("3","1","3","GetImportant",this);
        getHotConnect(simpleRemoteControl);      //请求热搜数据
        getNews(simpleRemoteControl);            //请求消息
        if (mInformationEntities == null || mInformationEntities.size() <= 0) {
            getInfoListByDb();     //从数据库获取资讯数据
        }
        requestCarouselImg(); // 请求轮播图
    }

    private void requestCarouselImg() {
        InterfaceCollection.getInstance().requestCarouselImg("getCarouselImg","7","","",this);
    }


    //产品预约列表
    private void initProductReservation() {
        HomeSafeBetView homeSafeBetView = new HomeSafeBetView(getActivity(), mJumpPageListener);
        mProductReservationView.addView(homeSafeBetView.getContentView());
    }


    //资讯列表
    private void initNewsListView(View view) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_homeInfoListLayout);
        mAdapter = new NewHomeInformationAdapter(getActivity());
        headView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.home_item_head, null);
        headView.setOnClickListener(this);
        ll.addView(headView, 0);
        mNewslistView.setAdapter(mAdapter);
        mNewslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                if (!TextUtils.isEmpty(mInformationEntities.get(position).getNewsno()))
                    intent.putExtra("requestId", mInformationEntities.get(position).getNewsno());
                getActivity().startActivity(intent);
            }
        });
    }

    //热搜股票列表
    private void initHotSearchStockListView() {
        mHotAdapter = new HomeHotAdapter();
        hotListView.setAdapter(mHotAdapter);


        //热搜股票
        hotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        BRutil.menuSelect("N024");
                        intent.setClass(getActivity(), HotSearchStockActivity.class);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        break;
                    default:
                        if (mHotEntities != null && mHotEntities.size() > 0) {
                            StockDetailEntity stockDetailEntity = new StockDetailEntity();
                            stockDetailEntity.setStockName(mHotEntities.get(position).getStockName());
                            stockDetailEntity.setStockCode(mHotEntities.get(position).getStockNumber());
                            intent.putExtra("stockIntent", stockDetailEntity);
                            intent.setClass(getActivity(), StockDetailActivity.class);
                            getActivity().startActivity(intent);
                        }
                        break;
                }
            }
        });
    }

    private void initScrollView() {
         mPullDownScrollView.setOnScrollChangedListener(new PullToRefreshScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
                onScroll(y);
            }
        });
        mPullDownScrollView.setFocusableInTouchMode(true);
        mPullDownScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullDownScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        initData();
                        mPullDownScrollView.onRefreshComplete();
                    }
                }, 2000);
            }
        });
    }


    private void getHotConnect(SimpleRemoteControl simpleRemoteControl) {
        simpleRemoteControl.setCommand(new ToTwentyFourHoursHotSearch(new TwentyFourHoursHotSearchConnect(getContext(), TAG, "1")));
        simpleRemoteControl.startConnect();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.home_middlebander:
//                intent.setClass(getActivity(), FinancialLife.class);
                intent.putExtra("type", 0);//开户 ，开户传此，
                intent.putExtra("channel", ConstantUtil.OPEN_ACCOUNT_CHANNEL);// 开户id
                intent.setClass(getActivity(), com.cairh.app.sjkh.MainActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.homeInfoHeadLayout:
                intent.setClass(getActivity(), InformationHomeActivity.class);
                intent.putExtra("currentItem", 0);
                SpUtils.putString(getActivity(),"currentTitle","要闻");
                startActivity(intent);
                BRutil.menuSelect("N004");
                break;
        }
    }

    private void getInfoListByDb() {
        mInfos = Db_HOME_INFO.getHomeInfos();
        if (mInfos == null || mInfos.size() <= 0) {
            return;
        }
        try {
            for (String json : mInfos) {
                mInformationEntities = HomeFragmentHelper.getInstance().getInfoListfromJson(json, zx_num);
            }
            mAdapter.setDatas(mInformationEntities);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private boolean flag = false;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        flag = hidden;
        if (!hidden) {
            refreshData();
        }
    }
    public void refreshData(){
        endRefreshTime = System.currentTimeMillis();
        if (endRefreshTime - startRefreshTime >= 900000){
            initData();
            startRefreshTime = System.currentTimeMillis();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!flag){
            refreshData();
        }
//        initGridView();
    }

    @Override
    public void onScroll(int scrollY) {
        int topLayotpx = Helper.dip2px(getActivity(), 200);
        int _height = Helper.dip2px(getActivity(), 44);
        if (scrollY >= topLayotpx - _height && scrollY <= topLayotpx) {
            float scale = (float) scrollY / topLayotpx;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            mTopTextView.setBackgroundColor(Color.argb((int) alpha, 28, 134, 238));
        } else if (scrollY > topLayotpx) {
            mTopTextView.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
            mTopTextView.setText("太牛");
        } else {
            mTopTextView.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), android.R.color.transparent));
            mTopTextView.setText("");
        }
    }

    public void setJumPageListener(JumpPageListener listener) {
        mJumpPageListener = listener;
    }


    private void initGridView() {
        ArrayList<Map<String, Object>> dataSourceList = new ArrayList<>();
        ArrayList<Map<String, Object>> localSourceList = new ArrayList<>();

        Map<String, Object> mywWallet = new HashMap<>();
        mywWallet.put("img", R.mipmap.sy_wodezhanghu);
        mywWallet.put("title", "我的账户");

//        Map<String, Object> fundLife = new HashMap<>();
//        fundLife.put("img", R.mipmap.sy_jinrongshenghuo);
//        fundLife.put("title", "金融生活");

        Map<String, Object> selfStock = new HashMap<>();
        selfStock.put("img", R.mipmap.sy_zixuangu);
        selfStock.put("title", "自选股");

        Map<String, Object> searchStock = new HashMap<>();
        searchStock.put("img", R.mipmap.sy_sousuogupiao);
        searchStock.put("title", "搜索股票");

        Map<String, Object> myNewStock = new HashMap<>();
        myNewStock.put("img", R.mipmap.sy_xingu);
        myNewStock.put("title", "新股");

        Map<String, Object> hotStock = new HashMap<>();
        hotStock.put("img", R.mipmap.sy_zichanfenxi);
        hotStock.put("title", "资产分析");

        Map<String, Object> win = new HashMap<>();
        win.put("img", R.mipmap.sy_wenying);
        win.put("title", "稳赢理财");

        localSourceList.add(mywWallet);
//        localSourceList.add(fundLife);
        localSourceList.add(selfStock);
        localSourceList.add(searchStock);
        localSourceList.add(myNewStock);
        localSourceList.add(hotStock);
        localSourceList.add(win);

        dataSourceList.addAll(localSourceList);

        ArrayList<Map<String, Object>> allSource = new ArrayList<>();
        Map<String, Object> all = new HashMap<>();
        all.put("img", R.mipmap.home_quanbu);
        all.put("title", "全部");
        allSource.add(all);

        String homeTitleTab = SpUtils.getString(CustomApplication.getContext(), "homeTitleTab", "");

        ArrayList<Map<String, Object>> subSourceList = HomeFragmentHelper.titleNames(homeTitleTab);

        if (subSourceList != null && subSourceList.size() > 0) {
            dataSourceList.addAll(subSourceList);
        }

        Map<Integer, Object> tipsMap = new HashMap<>();
        Integer[] tips = {R.mipmap.home_morecontent};

        dataSourceList.addAll(allSource);

        int lastPosition = dataSourceList.indexOf(all);
        tipsMap.put(lastPosition, tips[0]);

        mDataSourceList = dataSourceList;


        int balanceNum = dataSourceList.size() % 4;

        if (balanceNum != 0) {
            for (int j = 0; j < (4 - balanceNum); j++) {
                Map<String, Object> map = new HashMap<>();
                dataSourceList.add(map);
            }
        }


        mGrideAdapter.setDatas(dataSourceList, tipsMap);

    }


    @Override
    public void getResult(Object result, String tag) {
        if (result instanceof String) {
            return;
        }  else if ("TwentyFourHoursHotSearchConnect".equals(tag)) {
            ArrayList<StockInfoEntity> entities = (ArrayList<StockInfoEntity>) result;   //返回结果可能存在多条，但只展示5条
            if (entities == null || entities.size() <= 0) {
                hotListView.setVisibility(View.GONE);
                return;
            } else {
                hotListView.setVisibility(View.VISIBLE);
                mHotEntities.clear();
                /**
                 * 处理listview前2个标题栏
                 */
                StockInfoEntity bean1 = new StockInfoEntity();
                StockInfoEntity bean2 = new StockInfoEntity();
                mHotEntities.add(bean1);
                mHotEntities.add(bean2);

                for (int i = 0; i < hotNum; i++) {
                    mHotEntities.add(entities.get(i));
                }
                mHotAdapter.setDatas(mHotEntities);
            }
        } else if ("GetMyHomePage".equals(tag)) {
            messageList = (ArrayList<MyHomePageEntity>) result;
            if (messageList == null || messageList.size() <= 0) {
                return;
            } else {
                count_count = messageList.get(0).getCount_count();
            }
        }
    }


    /**
     * 我的消息
     *
     * @param simpleRemoteControl
     */
    private void getNews(SimpleRemoteControl simpleRemoteControl) {
        simpleRemoteControl.setCommand(new ToMyNewsHomePage(new GetMyNomePage(TAG)));
        simpleRemoteControl.startConnect();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = String.valueOf(mDataSourceList.get(position).get("title"));
        if (title.equals("我的消息")) {
            HomeFragmentHelper.getInstance().gotoPager(title, getActivity(), mJumpPageListener, messageList);
        } else {
            HomeFragmentHelper.getInstance().gotoPager(title, getActivity(), mJumpPageListener, null);
        }
    }

    //王奇添加消息逻辑
    @Override
    public void getMessage(final int message) {
        if (!TextUtils.isEmpty(count_count) && Helper.isNumberDimc(count_count)) {
            int count = Integer.valueOf(count_count) + message;
            notifyObservers(count);
        }
    }

    //王奇添加消息逻辑
    @Override
    public void onStart() {
        super.onStart();
        initGridView();
        String flag = SpUtils.getString(getActivity(), "mDivnum", null);
        if ("true".equals(flag)) {
            notifyObservers(0);
        }
    }

    @Override
    public void registerObserver(HomeInfomationObsever observer) {
        mOberservers.add(observer);
    }

    @Override
    public void removeObserver(HomeInfomationObsever observer) {
        int num = mOberservers.indexOf(observer);
        if (num >= 0) {
            mOberservers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(int count) {
        for (HomeInfomationObsever observer : mOberservers) {
            observer.update(count);
        }
    }


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_home;
    }


    public interface JumpPageListener {
        void onCheckedChanged(int position);
    }
}
