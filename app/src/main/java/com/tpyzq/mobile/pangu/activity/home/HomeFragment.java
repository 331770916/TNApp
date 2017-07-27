package com.tpyzq.mobile.pangu.activity.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeInfomationObsever;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeInfomationSubject;
import com.tpyzq.mobile.pangu.activity.home.hotsearchstock.HotSearchStockActivity;
import com.tpyzq.mobile.pangu.activity.home.information.InformationHomeActivity;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.ManagerMoenyDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.OptionalFinancingActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.PreProductAdapter;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.adapter.home.HomeAdapter;
import com.tpyzq.mobile.pangu.adapter.home.HomeHotAdapter;
import com.tpyzq.mobile.pangu.adapter.home.NewHomeInformationAdapter;
import com.tpyzq.mobile.pangu.adapter.home.NewOptionalFinancingAdapter;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.MyHomePageEntity;
import com.tpyzq.mobile.pangu.data.NewOptionalFinancingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetHomeBanderConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetOptionalFinancingConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetHomeBanderConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToOptionalFinancingConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToTwentyFourHoursHotSearch;
import com.tpyzq.mobile.pangu.http.doConnect.home.TwentyFourHoursHotSearchConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.GetMyNomePage;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToMyNewsHomePage;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.imageUtil.ImageUtil;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.view.CarouselView;
import com.tpyzq.mobile.pangu.view.gridview.MyGridView;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.tpyzq.mobile.pangu.view.gridview.MyScrollView;
import com.tpyzq.mobile.pangu.view.pullrefreshrecyclerview.PullRefreshUtil;
import com.tpyzq.mobile.pangu.view.pullrefreshrecyclerview.PullRefreshView;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


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
    private CarouselView vp_carousel;
    private LinearLayout ll_point;
    private List<View> points;
    private int[] image = {R.mipmap.top1, R.mipmap.top2, R.mipmap.top4};

//    private ConvenientBanner mConvenientBanner;
//    private ArrayList<Bitmap> mBitmaps;
//    private ArrayList<String> mAdType;
    /**
     * 九宫格控件
     */
    private MyGridView gridView;
    private HomeAdapter mGrideAdapter;
    private ArrayList<Map<String, Object>> mDataSourceList;
    /**
     * 预约产品模块
     */
    private MyListView mProductReservationView;
    private PreProductAdapter productreservationadapter;
    /**
     * 稳赢模块，暂定
     */
    private MyListView mFinaceView;
    private NewOptionalFinancingAdapter optionalFinancingAdapter;
    private ArrayList<NewOptionalFinancingEntity> optionalFinancingList;

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
    private ImageView mMiddleImageView; //中间图片
    private TextView mTopTextView; //上推时显示应用标题

    private SimpleRemoteControl simpleRemoteControl;
    private PullRefreshView mPullDownScrollView;
    private ArrayList<HomeInfomationObsever> mOberservers;
    private ArrayList<MyHomePageEntity> messageList;
    private int hotNum = 5;
    private int zx_num = 4; //定义资讯显示条数
    private String count_count;
    private long startRefreshTime;
    private long endRefreshTime;


    @Override
    public void initView(View view) {
//        mBitmaps = new ArrayList<>();
//        mAdType = new ArrayList<>();
        startRefreshTime = System.currentTimeMillis();
        mDataSourceList = new ArrayList<>();
        optionalFinancingList = new ArrayList<>();
        mInformationEntities = new ArrayList<>();
        mHotEntities = new ArrayList<>();
        messageList = new ArrayList<>();
        mOberservers = new ArrayList<>();
        simpleRemoteControl = new SimpleRemoteControl(this);


        mTopTextView = (TextView) view.findViewById(R.id.homeLayoutTopTextView);

//        mConvenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
//        mConvenientBanner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
//        mConvenientBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
//        mConvenientBanner.startTurning(5000);
//        mConvenientBanner.setOnItemClickListener(this);
        vp_carousel = (CarouselView) view.findViewById(R.id.vp_carousel);
        ll_point = (LinearLayout) view.findViewById(R.id.ll_point);
        initCarouseView();

        mMiddleImageView = (ImageView) view.findViewById(R.id.home_middlebander);
        mMiddleImageView.setImageDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.openanaccount_shouye));
        mMiddleImageView.setOnClickListener(this);
        mMiddleImageView.setVisibility(View.VISIBLE);

        MyScrollView scrollView = (MyScrollView) view.findViewById(R.id.homeScrollView);
        scrollView.setOnScrollListener(this);

        gridView = (MyGridView) view.findViewById(R.id.homeGridView);
        mGrideAdapter = new HomeAdapter(true, getActivity());
        mOberservers.add(mGrideAdapter);
        gridView.setAdapter(mGrideAdapter);

        //下拉刷新
        mPullDownScrollView = (PullRefreshView) view.findViewById(R.id.selfchoice_PullDownScroll);

        mProductReservationView = (MyListView) view.findViewById(R.id.homeProductReservation);
        mFinaceView = (MyListView) view.findViewById(R.id.homeSelfNewsListView);
        mNewslistView = (MyListView) view.findViewById(R.id.homeListView);
        hotListView = (MyListView) view.findViewById(R.id.homeHotListView);
        //轮播图片目前实现为从asset文件下取，然后保存到ImageView文件夹下，后续需要变更从网络获取
//        copyFundOpenUserPdf();
//        getSdcardImageViewForAdvertisement();

        initGridView();  //九宫格初始化
        initProductReservation(); //产品预约初始化
//        initAmazingLifeListView();  //稳赢初始化
        initNewsListView(view);       //资讯初始化
        initHotSearchStockListView();  //热搜初始化
        CustomApplication.setGetMessageListenr(this);  //消息的Handler
        gridView.setOnItemClickListener(this);
        initScrollView(); //下拉刷新初始化

        initData();
    }

    @Override
    public void callResult(ResultInfo info) {
        if(info.getCode().equals("200")){
            if(info.getTag().equals("GetImportant")){
                mInformationEntities = (ArrayList<InformationEntity>)info.getData();
                mNewslistView.setVisibility(View.VISIBLE);
                mAdapter.setDatas(mInformationEntities);
            }
        }else{
            mNewslistView.setVisibility(View.GONE);
        }
    }

    private void initCarouseView() {
        points = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            View view = new View(getContext());
            LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(16, 16);
            vParams.setMargins(5, 0, 5, 0);
            view.setLayoutParams(vParams);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.shape_white_round);
            } else {
                view.setBackgroundResource(R.drawable.shape_transparent_round);
            }
            points.add(view);
            ll_point.addView(view);
        }
        vp_carousel.init(image.length, new CarouselView.SimpleDraweeViewHandler() {
            @Override
            public void handle(final int index, SimpleDraweeView view) {
                Uri uri = Uri.parse("res:///"+image[index]);
                view.setImageURI(uri);
//                view.setImageResource(image[index]);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        switch (index) {
                            case 0:
                                BRutil.menuSelect("N022");
                                intent.setClass(getActivity(), OptionalFinancingActivity.class);
                                getActivity().startActivity(intent);
                                break;
                            case 1:
                                intent.setClass(getActivity(), AdvertActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                BRutil.menuSelect("N004");
                                intent.setClass(getActivity(), InformationHomeActivity.class);
                                getActivity().startActivity(intent);
                                break;
                        }
                    }
                });
            }

            @Override
            public void select(int position) {
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        points.get(i).setBackgroundResource(R.drawable.shape_white_round);
                    } else {
                        points.get(i).setBackgroundResource(R.drawable.shape_transparent_round);
                    }
                }
            }
        });
    }

    public void initData() {
        getFinaceConnect(simpleRemoteControl);  //请求稳赢数据
        //请求资讯数据 1级30条
        InterfaceCollection.getInstance().queryImportant("3","1","3","GetImportant",this);
        getHotConnect(simpleRemoteControl);      //请求热搜数据
        getNews(simpleRemoteControl);            //请求消息
        if (mInformationEntities == null || mInformationEntities.size() <= 0) {
            getInfoListByDb();     //从数据库获取资讯数据
        }
    }


    //产品预约列表
    private void initProductReservation() {
        View selfNewsHeadView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.home_item_head, null);
        TextView textView = (TextView) selfNewsHeadView.findViewById(R.id.homeHeadName);
        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.wenyinglicai);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        textView.setCompoundDrawables(drawable, null, null, null);//画在右边
        textView.setText("稳赢理财");
        mProductReservationView.addHeaderView(selfNewsHeadView);

        productreservationadapter = new PreProductAdapter(getContext());
        mProductReservationView.setAdapter(productreservationadapter);
        mProductReservationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                String type;
                boolean register;
                if (position == 0) {
                    HomeFragmentHelper.getInstance().gotoPager("稳赢理财", getActivity(), mJumpPageListener, null);
                } else {    //稳赢 item
                    intent = new Intent();
                    String prod_status = optionalFinancingList.get(position - 1).getProd_status();
                    intent.putExtra("productCode", optionalFinancingList.get(position - 1).getProd_code());
                    type = optionalFinancingList.get(position - 1).getType();
                    intent.putExtra("TYPE", type);
                    intent.putExtra("prod_type", optionalFinancingList.get(position - 1).getProd_type());
                    intent.putExtra("prod_nhsy", optionalFinancingList.get(position - 1).getProd_nhsy());
                    intent.putExtra("prod_qgje", optionalFinancingList.get(position - 1).getProd_qgje());
                    intent.putExtra("schema_id", optionalFinancingList.get(position - 1).getSchema_id());
                    intent.putExtra("prod_code", optionalFinancingList.get(position - 1).getProd_code());
                    intent.putExtra("ofund_risklevel_name", optionalFinancingList.get(position - 1).getOfund_risklevel_name());
                    register = Db_PUB_USERS.isRegister();
                    BRutil.menuNewSelect("Z1-4-4", optionalFinancingList.get(position - 1).getSchema_id(), optionalFinancingList.get(position - 1).getProd_code(), "2", new Date(), "-1", "-1");
                    if ("3".equals(type)) {
                        if (!register) {
                            intent.putExtra("Identify", "2");
                            intent.setClass(getActivity(), ShouJiZhuCeActivity.class);
                        } else {
                            intent.putExtra("prod_status", prod_status);
                            intent.setClass(getActivity(), ManagerMoenyDetailActivity.class);
                        }
                    } else {
                        intent.setClass(getActivity(), ManagerMoenyDetailActivity.class);

                    }
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    //稳赢列表
    private void initAmazingLifeListView() {
        View selfNewsHeadView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.home_item_head, null);
        TextView textView = (TextView) selfNewsHeadView.findViewById(R.id.homeHeadName);
        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.wenyinglicai);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        textView.setCompoundDrawables(drawable, null, null, null);//画在右边
        textView.setText("稳赢理财");
        mFinaceView.addHeaderView(selfNewsHeadView);

        optionalFinancingAdapter = new NewOptionalFinancingAdapter(getContext());
        mFinaceView.setAdapter(optionalFinancingAdapter);
        mFinaceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    HomeFragmentHelper.getInstance().gotoPager("稳赢理财", getActivity(), mJumpPageListener, null);
                } else {
                    ToastUtils.centreshow(getContext(), "1");
                }
            }
        });


    }


    //资讯列表
    private void initNewsListView(View view) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_homeInfoListLayout);
        mAdapter = new NewHomeInformationAdapter(getActivity());
        View headView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.home_item_head, null);
        headView.findViewById(R.id.homeInfoHeadLayout).setOnClickListener(this);
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
        PullRefreshUtil.setRefresh(mPullDownScrollView, true, false);
        mPullDownScrollView.setOnPullDownRefreshListener(
                new PullRefreshView.OnPullDownRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPullDownScrollView.isMore(true);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                initData();
                                mPullDownScrollView.refreshFinish();
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
                startActivity(intent);
                BRutil.menuSelect("N004");
                break;
        }
    }

    private void getFinaceConnect(SimpleRemoteControl simpleRemoteControl) {   //稳赢
        simpleRemoteControl.setCommand(new ToOptionalFinancingConnect(new GetOptionalFinancingConnect(TAG)));
        simpleRemoteControl.startConnect();
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

    /**
     * 获取广告
     *
     * @param simpleRemoteControl
     */
    private void getBanderConnect(SimpleRemoteControl simpleRemoteControl) {
        simpleRemoteControl.setCommand(new ToGetHomeBanderConnect(new GetHomeBanderConnect(TAG)));
        simpleRemoteControl.startConnect();
    }

    @Override
    public void getResult(Object result, String tag) {
        if (result instanceof String) {
//            MistakeDialog.showDialog("网络异常", getActivity());
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
        } else if ("GetOptionalFinancingConnect".equals(tag)) {
            optionalFinancingList = (ArrayList<NewOptionalFinancingEntity>) result;
            if (optionalFinancingList == null || optionalFinancingList.size() <= 0) {
                mProductReservationView.setVisibility(View.GONE);
            } else {
                mProductReservationView.setVisibility(View.VISIBLE);
                productreservationadapter.setData(optionalFinancingList);
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

//    private void copyFundOpenUserPdf() {
//        File file = Helper.getExternalFileDir(CustomApplication.getContext(), "pdf");
//        File[] files = file.listFiles();
//        if (files == null || files.length == 0) {
//            Helper.getAssestFile("pdf", "fundOpenUser.pdf");
//        }
//    }

//    /**
//     * 获取SDcard下的广告图片
//     */
//    private void getSdcardImageViewForAdvertisement() {
//        File file = Helper.getExternalFileDir(CustomApplication.getContext(), "ImageView");
//        File[] files = file.listFiles();
//        ArrayList<Bitmap> bitmaps = new ArrayList<>();
//        if (files == null || files.length == 0) {
//            //从Assets下获取默认图片
//            Bitmap bitmap1 = Helper.getAssetBitmap("money.png");
//            Bitmap bitmap2 = Helper.getAssetBitmap("account.png");
//            Bitmap bitmap3 = Helper.getAssetBitmap("infomation.png");
//            Bitmap bitmap4 = Helper.getAssetBitmap("hotSell.png");
//            bitmaps.add(bitmap1);
//            bitmaps.add(bitmap2);
//            bitmaps.add(bitmap3);
//            bitmaps.add(bitmap4);
//            //保存图片到imagview文件夹下
//            Helper.getAssestFile("ImageView", "money.png");
//            Helper.getAssestFile("ImageView", "account.png");
//            Helper.getAssestFile("ImageView", "infomation.png");
//            Helper.getAssestFile("ImageView", "hotSell.png");
//        }
//        else{
//            for (File _file : files) {
//                String _ivPath = _file.getPath();
//                Bitmap bitmap = ImageUtil.decodeBitmap(_ivPath);
//                bitmaps.add(bitmap);
//            }
//        }
//        mConvenientBanner.setPages(new BanderHolderCreator(), bitmaps);
//    }

    private void getBitmapConnect(String imgUrl) {

        NetWorkUtil.getInstence().okHttpForShowBitmap(TAG, imgUrl, new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(getActivity(), "" + e.toString());
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                if (response != null) {
                    long time = System.currentTimeMillis();
                    ImageUtil.saveBitmap(response, Helper.getExternalDirPath(CustomApplication.getContext(), "ImageView", time + ".png"), 3);
//                    mBitmaps.add(response);
//                    mConvenientBanner.setPages(new BanderHolderCreator(), mBitmaps);
                }
            }
        });

    }
//
//    @Override
//    public void onItemClick(int position) {
//        Intent intent = new Intent();
//
//        if (position == 0) {
//            BRutil.menuSelect("N022");
//            intent.setClass(getActivity(), OptionalFinancingActivity.class);
//            getActivity().startActivity(intent);
//        } else if (position == 1) {
//            Intent intent1 = new Intent();
//            intent1.setClass(getActivity(), AdvertActivity.class);
//            startActivity(intent1);
//
//        } else if (position == 2) {
//
//            if (mJumpPageListener != null) {
//                intent.setClass(getActivity(), LovingHeartActivity.class);
//                getActivity().startActivity(intent);
//            }
//
//        } else if (position == 3) {
//            intent.setClass(getActivity(), InformationHomeActivity.class);
//            getActivity().startActivity(intent);
//            BRutil.menuSelect("N004");
//        }
//    }

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
