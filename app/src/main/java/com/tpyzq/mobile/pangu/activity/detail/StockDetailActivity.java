package com.tpyzq.mobile.pangu.activity.detail;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.chart.AppOper;
import com.tpyzq.mobile.pangu.activity.detail.chart.FiveDayMinChart;
import com.tpyzq.mobile.pangu.activity.detail.chart.FiveDayMinData;
import com.tpyzq.mobile.pangu.activity.detail.chart.KData;
import com.tpyzq.mobile.pangu.activity.detail.chart.KLine;
import com.tpyzq.mobile.pangu.activity.detail.chart.KPoints;
import com.tpyzq.mobile.pangu.activity.detail.chart.MinChart;
import com.tpyzq.mobile.pangu.activity.detail.chart.MinData;
import com.tpyzq.mobile.pangu.activity.detail.chart.MinPoint;
import com.tpyzq.mobile.pangu.activity.detail.chart.PankouItem;
import com.tpyzq.mobile.pangu.activity.detail.exponetTab.ExPonentTab;
import com.tpyzq.mobile.pangu.activity.detail.newsTab.DetailNews;
import com.tpyzq.mobile.pangu.activity.detail.stockTab.DetailView3;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain.RemainActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.detail.DetailAdapter;
import com.tpyzq.mobile.pangu.adapter.detail.LandKlineToolsAdapter;
import com.tpyzq.mobile.pangu.adapter.detail.MingxiAdapter;
import com.tpyzq.mobile.pangu.adapter.detail.PankouAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_OPTIONALHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_SEARCHHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.DeleteSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToDelteSlefChoiceStockConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.ScreenShot;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.panguutil.AddPosition;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.SelfChoiceStockTempData;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyGridView;
import com.tpyzq.mobile.pangu.view.spinner.CustomSpinner;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by guohuiz on 2016/10/11.
 * 该界面是股票详情，点击后进入该详情界面
 */
public class StockDetailActivity extends BaseActivity implements  View.OnClickListener, CustomSpinner.ChooseItemListener,AppOper {
    private static final String TAG = "StockDetailActivity";
    private static final int RISE = 0;
    private static final int DOWM = 1;
    private static final int EQUAL = 2;

    private String stk_titleStr = "";
    private int refleshTime = 0;
    private int screenWidth = 0,screenHeight=0;
    private String stkCode="",stkName="-";
    private int stkType =0;
    private String stopFlag = "0";
    private int HighAndLowColor[] = {0,0};
    private MinData m_minData = new MinData();
    private  boolean isLandScreen = false;
    private boolean isShow = false;
    private FiveDayMinData m_fiveDayminData =new FiveDayMinData();


    private int minDataLen = 0;

    private final  String MIN_START_TIME = "093000";
    private Timer mTimer;
    private String minDataLastTime = MIN_START_TIME;
    private boolean isCallBackSuccess = false;
    private MinChart minChart = null;
    private MinChart minChartLand = null;
    private KLine kLine,landKline = null;
    private FiveDayMinChart fiveDayMinChart,landFiveDayMinChart;
    private TextView pankouView,mingxiView,land_pankouView,land_mingxiView;
    private View pankouLayout,pankoutTabLayout,land_pankouLayout,land_pankoutTabLayout;
    private LinearLayout minChartWholeLayout,land_minChartWholeLayout;
    private View landItemDetailLayout,land_itemLayout5,land_itemLayout6;
    private TextView[] landItemDetailValueTvs = null;
    private TextView[] landItemDetailTitleTvs = null;
    private ShareDialog shareDialog;

    private String[] grideTopTitles() {
        String [] grideTvs1 = {"最高", "最低", "换手", "成交额"};
        if(stkType==0){
            grideTvs1 = new String[]{ "成交额","最高", "最低", "今开"};
        }
        return grideTvs1;
    }
    private String[] grideTopDefalutData() {
        String [] grideTvs1 = {"0.00", "0.00", "0.00%", "0"};
        if(stkType==0){
            grideTvs1 = new String[]{ "0","0.00", "0.00", "0.00"};
        }
        return grideTvs1;
    }

    private String[] grideBottomTitles() {
        String [] grideTvs2 = {"内盘", "外盘", "量比", "成交量", "市盈率", "市净率", "流通市值", "总市值"};
        if(stkType==0){
            grideTvs2 = new String[]{ "涨家数","跌家数", "平家数", "昨收"};
        }else if(!isStock()){
            grideTvs2 = new String[]{"内盘", "外盘", "量比", "成交量"};
        }
        return grideTvs2;
    }


    private Dialog loadingDialog;
    private float stk_close = 0;
    private float stk_open = 0;
    private String wholeTime="";
    private MyGridView mBottomGridView;
    private boolean isGrideViewGone = false;

    private ImageView  mTabLine,mTabLandLine;

    private ImageView receiveGridBtn;
    private FrameLayout chartFrameLayout;

    private LinearLayout mShareLayout;

    private int tabFocusGap = 0,landtabFocusGap=0;
    private int       mScreen1_6,mScreenLand1_6;
    private int       mCurrentIndex=0;

    private TextView[] chartLabs= new TextView[6];
    private TextView[] landchartLabs= new TextView[6];
    private TextView mTimeTv, mFiveTv, mDayTv, mWeekTv, mMounthTv, mMinutesTv;

    private TextView mDetailTitle;
    private TextView mTitleNumber;
    private TextView mTopNowPirce;
    private TextView mTopDiff;
    private TextView mTopRange;
    private TextView mTopNumber3;
    private TextView mTopPlantNumber;
    //    private DynamicWave waveView;
    private StockDetailEntity mStockEntity;
    private View detailTopLayout,detailTitleLayout,detail_top_Part2Layout,land_KlineLayout;
    private DetailAdapter mAdapter1, mAdapter2;

    /****** END *****/
    private ListView pankouItemListView,land_pankouItemListView;
    private PankouAdapter pankouAdapter;
    private MingxiAdapter mingxiAdapter;
    //是否显示成交明细
    private boolean isShowMingxi = false;

    private int mRed,mGreen,mGray,mGray2,mBlue,mGray3,mWhite;

    /***
     * K线相关变量声明区域
     */
    private int Kline_cycle = KLine.KLINE_CYCLE_DAY;//周期，0:日，1:周，2:月，3:5分钟，4：15分钟 5:30分钟  6:60分钟
    private int Kline_size = 230;//请求周期条数
    private int Kline_start = 0;//0 从最新的请求
    private int Kline_xrdrType = 0;//0 除复权标志   复权类型，0为除权，1为前复权，2为后复权

    private int MinuteKlineType = 0;//分钟K线选择按钮 索引
    /****** END *****/

    /****横屏处理***/
    private LinearLayout land_AllWholeLayout,port_WholeLayout,land_chartWholeLayout;
//    private TextView mLandRangTv,mLandVolTv,mLandTimeTv;
    private TextView mLandTitleTv,mLandRangTv,mLandVolTv,mLandTimeTv;
    private String landRangStr="-",landVolStr = "-";

    /********END******/

    private long time1 = 0;
    private View bottomLayout1, bottomLayout2, bottomLayout3;
    private ImageView bottomMyStockIv;
    private TextView bottomMyStockTv;
    private boolean isMyStock = false;




    @Override
    public void initView() {
        LogHelper.e(TAG,"initView");
        Intent intent = getIntent();
        initView(intent);
    }


    public void initView(Intent intent) {
        isOnCreate = true;
        time1 = System.currentTimeMillis();
//        Intent intent = new Intent();
//        intent.setClass(StockDetailActivity.this, DetailService.class);
//        startService(intent);
        shareDialog  = new ShareDialog(this);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        screenWidth =outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
        if(Math.max(screenHeight,screenWidth)>1920){
            Kline_size = 250;
        }
//        LogHelper.e(TAG, "Mobile Screen height:"+screenHeight+" |screenWidth:"+screenWidth);

        mStockEntity = intent.getParcelableExtra("stockIntent");

        mRed = ContextCompat.getColor(this, R.color.red);
        mGray = ContextCompat.getColor(this,R.color.colorMyGray);
        mGreen = ContextCompat.getColor(this,R.color.colorMyGreen);
        mGray2 = ContextCompat.getColor(this,R.color.normalRadioBtnColor);

        mBlue= ContextCompat.getColor(this,R.color.colorMyBlue);
        mGray3= ContextCompat.getColor(this,R.color.stkDetailBottomTextColor);
        mWhite= ContextCompat.getColor(this,R.color.white);

        HighAndLowColor[0] = mGray2;
        HighAndLowColor[1] = mGray2;

        pankouItemListView =(ListView)findViewById(R.id.pankouItemListView);
        pankouAdapter = new PankouAdapter(this);
        pankouItemListView.setAdapter(pankouAdapter);

        land_pankouItemListView = (ListView)findViewById(R.id.land_pankouItemListView);

        mingxiAdapter = new MingxiAdapter(this);
        mingxiAdapter.setData(dataMingxi);
//        mObservers = new ArrayList<>();
//        mEntitys = new HashMap<>();

//        waveView = (DynamicWave)findViewById(R.id.detail_wave);
        findViewById(R.id.detail_back).setOnClickListener(this);
        findViewById(R.id.detail_search).setOnClickListener(this);
        detail_top_Part2Layout = findViewById(R.id.detail_top_Part2Layout);

        minChartWholeLayout = (LinearLayout) findViewById(R.id.minChartWholeLayout);
        land_minChartWholeLayout = (LinearLayout) findViewById(R.id.land_minChartWholeLayout);
        detailTopLayout = findViewById(R.id.rl_top_bar);
        detailTitleLayout = findViewById(R.id.detailTitleLayout);

        mShareLayout = (LinearLayout) findViewById(R.id.detail_share);
        mShareLayout.setOnClickListener(this);

        mTimeTv = (TextView) findViewById(R.id.timeTv);
        mFiveTv = (TextView) findViewById(R.id.fiveTv);
        mDayTv = (TextView)  findViewById(R.id.dayTv);
        mWeekTv = (TextView)  findViewById(R.id.weekTv);
        mMounthTv = (TextView) findViewById(R.id.mounthTv);
        mMinutesTv = (TextView) findViewById(R.id.minutesTv);
        chartLabs[0] = mTimeTv;
        chartLabs[1] = mFiveTv;
        chartLabs[2] = mDayTv;
        chartLabs[3] = mWeekTv;
        chartLabs[4] = mMounthTv;
        chartLabs[5] = mMinutesTv;
        for (int i =0;i<chartLabs.length;i++){
            chartLabs[i].setTag(i);
            chartLabs[i].setOnClickListener(this);
        }

        landchartLabs[0] = (TextView) findViewById(R.id.land_timeTv);
        landchartLabs[1] = (TextView) findViewById(R.id.land_fiveTv);
        landchartLabs[2] = (TextView) findViewById(R.id.land_dayTv);
        landchartLabs[3] = (TextView) findViewById(R.id.land_weekTv);
        landchartLabs[4] = (TextView) findViewById(R.id.land_mounthTv);
        landchartLabs[5] = (TextView) findViewById(R.id.land_minutesTv);

        for (int i =0;i<landchartLabs.length;i++){
            landchartLabs[i].setTag(i);
            landchartLabs[i].setOnClickListener(this);
        }

        pankouLayout = findViewById(R.id.pankouLayout);
        pankoutTabLayout = findViewById(R.id.pankouTabLayout);
        land_pankouLayout = findViewById(R.id.land_pankouLayout);
        land_pankoutTabLayout = findViewById(R.id.land_pankouTabLayout);

        pankouView = (TextView) findViewById(R.id.pankouView);
        mingxiView = (TextView) findViewById(R.id.mingxiView);

        land_pankouView = (TextView) findViewById(R.id.land_pankouView);
        land_mingxiView = (TextView) findViewById(R.id.land_mingxiView);

        pankouView.setOnClickListener(this);
        mingxiView.setOnClickListener(this);
        land_pankouView.setOnClickListener(this);
        land_mingxiView.setOnClickListener(this);

        land_AllWholeLayout = (LinearLayout) findViewById(R.id.land_AllWholeLayout);
        land_AllWholeLayout.setVisibility(View.GONE);
        port_WholeLayout = (LinearLayout) findViewById(R.id.portWholeLayout);
        land_chartWholeLayout = (LinearLayout) findViewById(R.id.land_ChartWholeLayout);
        land_KlineLayout = findViewById(R.id.land_KlineLayout);
        landKline = (KLine) findViewById(R.id.land_klineView);
        landKline.setListener(this);
        landKline.setLandFalg(true);

        mDetailTitle = (TextView) findViewById(R.id.detail_topTitle);
        mTitleNumber = (TextView) findViewById(R.id.detail_topTitleNumber);

        mTopNowPirce = (TextView) findViewById(R.id.detail_top_nowpirce);
        mTopDiff = (TextView) findViewById(R.id.detail_top_diff);
        mTopRange = (TextView) findViewById(R.id.detail_top_range);

        mTopPlantNumber = (TextView) findViewById(R.id.detail_top_Number4);
        mTopNumber3 = (TextView) findViewById(R.id.detail_top_Number3);
        fiveDayMinChart = new FiveDayMinChart(this,null);
        fiveDayMinChart.setListener(this);

        bottomLayout1 = findViewById(R.id.stk_detail_bottomBtn1);
        bottomLayout2 = findViewById(R.id.stk_detail_bottomBtn2);
        bottomLayout3 = findViewById(R.id.stk_detail_bottomBtn3);
//        bottomLayout4 = findViewById(R.id.detail_More);
        bottomLayout1.setOnClickListener(this);
        bottomLayout2.setOnClickListener(this);
        bottomLayout3.setOnClickListener(this);
//        bottomLayout4.setOnClickListener(this);
        bottomMyStockIv = (ImageView)findViewById(R.id.stk_detail_bottomIV);
        bottomMyStockTv = (TextView)findViewById(R.id.stk_detail_bottomTV);

        receiveGridBtn = (ImageView) findViewById(R.id.detail_gridBtn);
        receiveGridBtn.setOnClickListener(this);

        MyGridView topGridView = (MyGridView) findViewById(R.id.detail_topGride1);
        mBottomGridView = (MyGridView) findViewById(R.id.detail_topGride2);
        chartFrameLayout = (FrameLayout) findViewById(R.id.chartFrameLayout);
        minChart = (MinChart) findViewById(R.id.chartView);
        minChart.setListener(this);

        minChartLand = (MinChart) findViewById(R.id.land_chartView);
        minChartLand.setListener(this);
        minChartLand.setViewLand(true);
        mTabLine = (ImageView) findViewById(R.id.tab_img);
        intTabLine();
        mTabLandLine = (ImageView) findViewById(R.id.land_tab_img);

        mAdapter1 = new DetailAdapter();
        mAdapter1.setIsChangeColor(true);
        mAdapter2 = new DetailAdapter();
        topGridView.setAdapter(mAdapter1);
        mBottomGridView.setAdapter(mAdapter2);
        DetailScrollView dsv = ((DetailScrollView)findViewById(R.id.stkDetailScrollView));
        dsv.setupByWhichView(detail_top_Part2Layout);
        dsv.setListener(this);
//        topGridView.setVisibility(View.GONE);
//        mBottomGridView.setVisibility(View.GONE);

        topGridView.setOnItemClickListener(topClosListener);

        findViewById(R.id.close_land).setOnClickListener(this);
        mLandTitleTv = (TextView)findViewById(R.id.land_detail_Title);
        mLandRangTv= (TextView)findViewById(R.id.land_detail_rang);
        mLandVolTv= (TextView)findViewById(R.id.land_detail_vol);
        mLandTimeTv= (TextView)findViewById(R.id.land_detail_time);
        landItemDetailLayout = findViewById(R.id.landItemDetailLayout);
        land_itemLayout5 = findViewById(R.id.land_itemLayout5);
        land_itemLayout6 = findViewById(R.id.land_itemLayout6);
        myHandlerInit.sendEmptyMessage(0);
        myHandler.sendEmptyMessageDelayed(0,500);
//        myHandlerInit.sendEmptyMessage(0);
//        LogHelper.e(TAG,"initTime:"+( System.currentTimeMillis()-time1)+"  holdPrice:"+holdPrice);
    }
    private String m_strHoldPrice= "0";//持仓成本价， 0为没有
    private boolean isNeedRequsetHold = false;//
    Handler myHandlerInit = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setTitle();
            if(stkCode==null||stkCode.length()!=8){
                return;
            }
            request();
            initPankou();
            setPankouFocus(true);
            setBottomTools(true);
            boolean isLogintrade = "true".equals(Db_PUB_USERS.queryingIslogin());
            if(isLogintrade){
                boolean isHaveHoldData = AddPosition.getInstance().isHaveData();
                if(isHaveHoldData){
                    HashMap mp = AddPosition.getInstance().getData(stkCode);
                    if(mp!=null){
                        m_strHoldPrice = (String) mp.get("MKT_PRICE");
                    }else{
                        m_strHoldPrice= "0";
                    }
                }else{
                    boolean tempFlag =AddPosition.getInstance().isSuccessGetData();
                    isNeedRequsetHold = false;
                    if (!tempFlag){
                        isNeedRequsetHold = true;
                    }
                }
            }
            LogHelper.e(TAG,"doListenLookSelfStock:"+stkCode+","+stkName);
            if(stkCode==null){
                return;
            }
            BRutil.doListenLookSelfStock(stkCode,stkName,"0");
        }
    };

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int index =msg.what;
            if(index == 0){
                setTimerTask();
                setLandTitle();
                if(isStock()){
                    DetailNews dn = new DetailNews(StockDetailActivity.this,stkCode);
                    View rootViews = dn.rootView;
                    ((LinearLayout)findViewById(R.id.newsYanbaosLayout)).addView(rootViews);
                    DetailView3 detailView3 = new DetailView3(StockDetailActivity.this);
                    detailView3.initData(stkCode);
                    ((LinearLayout)findViewById(R.id.F10Layout)).addView(detailView3.rootView);
                }else if(stkType == 0){
                    ExPonentTab view = new ExPonentTab(StockDetailActivity.this, stkCode);
                    ((LinearLayout)findViewById(R.id.newsYanbaosLayout)).addView(view.getContentView());
//            linearLayout.addView(view.getContentView());
                }else{
                    DetailNews dn = new DetailNews(StockDetailActivity.this,stkCode);
                    View rootViews = dn.rootView;
                    ((LinearLayout)findViewById(R.id.newsYanbaosLayout)).addView(rootViews);
                }
            }else if(index==1){
                if(!TextUtils.isEmpty(backStkName)&&!"-".equals(backStkName)){
                    if(!backStkName.equals(stkName)){//股票名字有改变
                        stkName = backStkName;
                        mDetailTitle.setText(stkName);
                        Db_PUB_OPTIONALHISTORYSTOCK.updateStockNameByCode(stkName,stkCode);//更新一下最近浏览+自选股 股票名称
                    }else if(!backStkName.equals(myStockDbName)){
                        Db_PUB_OPTIONALHISTORYSTOCK.updateStockNameByCode(stkName,stkCode);//更新一下最近浏览+自选股 股票名称
                    }
                }
            }

        }
    };
    private String myStockDbName = "";
    private void setBottomTools(boolean isGetDB){
        try{
            if(isGetDB){
                //判断是不是自选股
                StockInfoEntity tempBean = Db_PUB_STOCKLIST.queryStockFromID(stkCode);
                if (tempBean != null) {
                    isMyStock = true;
                    myStockDbName = tempBean.getStockName();
                }else {
                    isMyStock = false;
                }
            }
            if (isMyStock) {
                if(stkType==0){
                    bottomMyStockIv.setImageResource(R.drawable.stk_icon_delself);
                    bottomMyStockTv.setText("删除自选");
                }else{
                    bottomMyStockIv.setImageResource(R.drawable.stk_icon_more);
                    bottomMyStockTv.setText("更多");
                }
            } else {
                bottomMyStockIv.setImageResource(R.drawable.stk_icon_addself);
                bottomMyStockTv.setText("添加自选");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initPankou(){
        HighAndLowColor[0] = mGray2;
        HighAndLowColor[1] = mGray2;
        mAdapter1.setColors(HighAndLowColor);
        mAdapter1.setDatas(getGrideDatas1(grideTopDefalutData()));
    }
    private void setTitle(){
        stkName = mStockEntity.getStockName();
        if (!TextUtils.isEmpty(stkName)) {
            mDetailTitle.setText(stkName);
        }else{
            stkName = "-";
        }
        String codeTemp = mStockEntity.getStockNumber();
        stkCode = codeTemp;

        if (!TextUtils.isEmpty(codeTemp)) {
            String code =codeTemp.substring(2);
            String market = "";
            if(codeTemp.startsWith("1")){
                market = "SH ";
            }else if(codeTemp.startsWith("2")){
                market = "SZ ";
            }
//            mTitleNumber.setText(market+code);
            stk_titleStr = mStockEntity.getStockName()+" "+market+code;
            mDetailTitle.setText(stk_titleStr);
            try {
                stkType =Integer.parseInt(codeTemp.substring(1,2));
            }catch (Exception exx){
                exx.printStackTrace();
            }
            mAdapter1.setStkType(stkType);

            LogHelper.e(TAG, "stockDetail stkType:"+stkType+"|"+codeTemp);
            setTopItemShowStatus();
        }
//        waveView.setBackgroundColor(mGray);
//        waveView.setWaveColor(0xFF6B6B6B,mGray);
        if(detail_top_Part2Layout!=null)
        {
            detail_top_Part2Layout.setBackgroundColor(mGray);
        }
        Db_PUB_OPTIONALHISTORYSTOCK.addOneData(stkName,stkCode);
    }

    private void setTopItemShowStatus(){
        if(!isStock()){
            mTopNumber3.setVisibility(View.GONE);
            mTopPlantNumber.setVisibility(View.GONE);
        }
        if(stkType==2&&stkCode.startsWith("1")||(stkType==3)||(stkType==4)||(stkType==5)
                || stkCode.startsWith("29") || stkCode.startsWith("17")){
            mFormat1 = new DecimalFormat("#0.000");
        }else{
            mFormat1 = new DecimalFormat("#0.00");
        }
        //设置盘口显示
        if(stkType==0){
            pankouLayout.setVisibility(View.GONE);
            land_pankouLayout.setVisibility(View.GONE);
            mShareLayout.setVisibility(View.GONE);
            bottomLayout1.setVisibility(View.GONE);
            bottomLayout2.setVisibility(View.GONE);
//            bottomLayout1.setVisibility(View.GONE);
        }else{
            pankouLayout.setVisibility(View.VISIBLE);
            land_pankouLayout.setVisibility(View.VISIBLE);
            initPankouData();
        }
    }

    static DecimalFormat mFormat3 = new DecimalFormat("#0.00");//金额，固定2位小数
    DecimalFormat mFormat1 = new DecimalFormat("#0.00");
    DecimalFormat mFormat2 = new DecimalFormat("#0.00%");

    //用户滚动界面后，替换标题内容
    private String titleContent1 = "";
    private String titleContent2 = "";
    private void setTextViewValue(StockInfoEntity stockEntity) {
        String time1 = stockEntity.getTime();
        String timeR = time1.substring(11,13)+":"+time1.substring(13,15)+":"+time1.substring(15);
        if(refleshTime==0){
            titleContent1 = "交易中 "+timeR;
        }else{
            titleContent1 = "已收盘 "+timeR;
        }


        //现价
        if (!TextUtils.isEmpty(stockEntity.getNewPrice())) {
            landRangStr = mFormat1.format(Float.parseFloat(stockEntity.getNewPrice()));
            mTopNowPirce.setText(landRangStr);
        }
        if(stkType!=0&&!"0".equals(stopFlag)){
            mTopNowPirce.setText("停牌");
        }

        int forwordFlag = EQUAL;//0  平  1 zhang  2 die
        if (!TextUtils.isEmpty(stockEntity.getNewPrice()) && !TextUtils.isEmpty(stockEntity.getClose())) {
            //涨跌
            float zdTv = Float.parseFloat(stockEntity.getNewPrice()) - Float.parseFloat(stockEntity.getClose());
//            BigDecimal new_price = new BigDecimal(stockEntity.getNewPrice());
//            BigDecimal close_price = new BigDecimal(stockEntity.getClose());
//            double zdTv = new_price.subtract(close_price).doubleValue();
            String fuhao = "";
            if(zdTv>0){
                fuhao = "+";
                forwordFlag = RISE;
            }else if(zdTv<0){
                forwordFlag = DOWM ;
            }
            mTopDiff.setText(fuhao+mFormat1.format(zdTv));
            //涨跌幅
//            float zdfTv = zdTv / Float.parseFloat(stockEntity.getClose());
            float zdfTv = Helper.getDiffPercent(stockEntity.getNewPrice(),stockEntity.getClose());
//            double zdfTv = zdTv / close_price.doubleValue();
            mTopRange.setText(fuhao+mFormat2.format(zdfTv));
            titleContent2 = landRangStr+"("+fuhao+mFormat1.format(zdTv)+" "+fuhao+mFormat2.format(zdfTv)+")";

            landRangStr += "("+fuhao+mFormat2.format(zdfTv)+")";
        }
        if(isModifyTitle){
            mTitleNumber.setText(titleContent2);
        }else{
            mTitleNumber.setText(titleContent1);
        }
        if(isStock()){
            if (!TextUtils.isEmpty(stockEntity.getIndustryName())) {
                mTopPlantNumber.setText("相关行业\n"+stockEntity.getIndustryName());
            }
            String tempIUD = stockEntity.getIndustryUpAndDown();
            mTopNumber3.setText(kxjsfPercent(tempIUD));

        }

//    int color11 =0x7CCD7C;

        setTopColor(forwordFlag);
//        LogHelper.i(TAG, "调用设置数据："+forwordFlag);
    }
    /**
     * 设置沉浸式标题颜色
     */
    private void setTopColor(int flag){
        if(flag==DOWM){
            detailTitleLayout.setBackgroundColor(mGreen);
//            detail_top_Part2Layout.setBackgroundColor(mGreen);
            if(detail_top_Part2Layout!=null)
            detail_top_Part2Layout.setBackgroundResource(R.drawable.stk_detail_title_fall);
        }else if(flag==RISE){
            detailTitleLayout.setBackgroundColor(mRed);
//            detail_top_Part2Layout.setBackgroundColor(mRed);
            if(detail_top_Part2Layout!=null)
            detail_top_Part2Layout.setBackgroundResource(R.drawable.stk_detail_title_rise);
        }else{
            detailTitleLayout.setBackgroundColor(mGray);
//            detail_top_Part2Layout.setBackgroundColor(mGray);
            if(detail_top_Part2Layout!=null)
            detail_top_Part2Layout.setBackgroundResource(R.drawable.stk_detail_title_same);
        }
        if(detailTopLayout!=null){
            if(flag==DOWM){
                detailTopLayout.setBackgroundColor(mGreen);
            }else if(flag==RISE){
                detailTopLayout.setBackgroundColor(mRed);
            }else{
                detailTopLayout.setBackgroundColor(mGray);
            }
        }

    }

//    private void switchContent1(Fragment to) {
//        if (mCurrentFragment1 != to) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            if (!to.isAdded()) { // 先判断是否被add过
//                transaction.hide(mCurrentFragment1).add(R.id.detail_newsLayout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
//            } else {
//                transaction.hide(mCurrentFragment1).show(to).commit(); // 隐藏当前的fragment，显示下一个
//            }
//            mCurrentFragment1 = to;
//        }
//    }

//    private void switchContent2(Fragment to) {
//        if (mCurrentFragment2 != to) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            if (!to.isAdded()) { // 先判断是否被add过
//                transaction.hide(mCurrentFragment2).add(R.id.detail_commanyLayout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
//            } else {
//                transaction.hide(mCurrentFragment2).show(to).commit(); // 隐藏当前的fragment，显示下一个
//            }
//            mCurrentFragment2 = to;
//        }
//    }

    private void intTabLine(){
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScreen1_6 = outMetrics.widthPixels/6;
        tabFocusGap = mScreen1_6/8;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mTabLine.getLayoutParams();

        lp.width = mScreen1_6-tabFocusGap*2;
//        lp.leftMargin = tabFocusGap;
        mTabLine.setLayoutParams(lp);
        mCurrentIndex=0;
        lastChartIndex = -1;
        setChartIndexFocus(0);
    }
    private void intLandTabLine(){
        mScreenLand1_6 = screenHeight/6;
        landtabFocusGap = mScreenLand1_6/6;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mTabLandLine.getLayoutParams();

        lp.width = mScreenLand1_6-landtabFocusGap*2;
        lp.leftMargin = landtabFocusGap;
        mTabLandLine.setLayoutParams(lp);
        setChartIndexFocus(mCurrentIndex);
    }
//    private void intTabLine1(){
//        Display display = getWindow().getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//        mNews1_3 = outMetrics.widthPixels/3;
//        ViewGroup.LayoutParams lp = mTabLine1.getLayoutParams();
//        lp.width = mNews1_3;
//        mTabLine1.setLayoutParams(lp);
//    }

//    private void intTabLine2(){
//        Display display = getWindow().getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//        mCommpany1_3 = outMetrics.widthPixels/3;
//        ViewGroup.LayoutParams lp = mTabLine2.getLayoutParams();
//        lp.width = mCommpany1_3;
//        mTabLine2.setLayoutParams(lp);
//    }
//
//    /**
//     * 获取顶部gridview数据
//     * @return
//     */
//    public abstract String[] gridTopDatas1();
//
//    /**
//     * 获取底部gridview数据
//     * @return
//     */
//    public abstract  String[] gridBottomDatas2();
//
//    /**
//     * 获取顶部Titles
//     * @return
//     */
//    public abstract String[] grideTopTitles();
//
//
//    /**
//     * 获取底部Titles
//     * @return
//     */
//    public abstract String[] grideBottomTitles();
//
//    /**
//     * 获取股票代码
//     * @return
//     */
//    public abstract String getStockNumber();
//
//    /**
//     * 获取市场代码
//     * @return
//     */
//    public abstract String getMarketNumber();

    /**
     * 顶部4个item的值
     * @param datas
     * @return
     */
    private ArrayList<Map<String, Object>> getGrideDatas1(String[] datas){
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < grideTopTitles().length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("top", grideTopTitles()[i]);
            map.put("bottom", datas[i]);
            maps.add(map);
        }
        return maps;
    }

    /**
     * 底部4个item的值
     * @param datas
     * @return
     */
    private ArrayList<Map<String, Object>> getGrideDatas2(String[] datas){
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < grideBottomTitles().length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("top", grideBottomTitles()[i]);
            map.put("bottom", datas[i]);
            maps.add(map);
        }
        return maps;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_back:
                finish();
                break;
            case R.id.detail_search:
                Intent intent = new Intent();
                intent.setClass(StockDetailActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.detail_gridBtn:
                if (isGrideViewGone) {
                    receiveGridBtn.setImageResource(R.mipmap.receive_p);
                    mBottomGridView.setVisibility(View.GONE);
                    isGrideViewGone = false;
                } else {
                    receiveGridBtn.setImageResource(R.mipmap.receive_n);
                    mBottomGridView.setVisibility(View.VISIBLE);
                    isGrideViewGone = true;
                }
                break;
            case R.id.timeTv:
            case R.id.fiveTv:
            case R.id.dayTv:
            case R.id.weekTv:
            case R.id.mounthTv:
            case R.id.land_timeTv:
            case R.id.land_fiveTv:
            case R.id.land_dayTv:
            case R.id.land_weekTv:
            case R.id.land_mounthTv:
                setChartIndexFocus((Integer) v.getTag());
                break;
            case R.id.minutesTv:
            case R.id.land_minutesTv:
                showMinutesSpinner();
                break;
            case R.id.pankouView:
            case R.id.land_pankouView:
                setPankouFocus(true);
                break;
            case R.id.mingxiView:
            case R.id.land_mingxiView:
                setPankouFocus(false);
                break;
            case R.id.close_land:
                changeScreenOriental();
                break;

            case R.id.fuquan_noTv:
            case R.id.fuquan_beforeTv:
            case R.id.fuquan_afterTv:
                this.Kline_xrdrType = (Integer) v.getTag();
                changeXrdrType();
                kdata = null;
                requestKlineData();
                break;
            case R.id.stk_detail_bottomBtn1://buy
                gotoStkBuyAndSell("买");
                break;
            case R.id.stk_detail_bottomBtn2://sell
                gotoStkBuyAndSell("卖");
                break;
            case R.id.detail_share://分享
                loadingDialog = LoadingDialog.initDialog(this, "加载中…");
                loadingDialog.show();       //显示菊花
                getShare();
                break;
            case R.id.stk_detail_bottomBtn3:
                dealBottomMystockBtn();
                break;
        }
    }

    private void dealBottomMystockBtn(){
        if(stkType==0){//指数
            dealMyStock();
        }else{
            if(isMyStock){//弹出选择框
                showMoreSpinner();
            }else{//add myself
                dealMyStock();
            }
        }
    }
    private void dealMyStock(){
            //添加数据库， 处理shared文件， 转换图片显示， 转换值状态
//            final String stockNumber = mDatas.get(position).getStockNumber();
//            final String stockName = mDatas.get(position).getStockName();
//            final String price = mDatas.get(position).getNewPrice();

//            boolean isSelfChoiceStock = mDatas.get(position).getIsSelfChoiceStock();

            if (isMyStock) {

                BRutil.doListenAddOrRemoveSelfStock(stkCode, stkName, BRutil.ACTIONREMOVESELFSTOCK, "0");
                if (Db_PUB_USERS.isRegister()) {
                    final Dialog dialog = LoadingDialog.initDialog(this, "正同步到云端");
                    dialog.show();
                    //同步网络 从云端删除刚才删除的自选股
                    SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(new ICallbackResult() {
                        @Override
                        public void getResult(Object result, String tag) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            String msg = (String) result;

                            try {
                                JSONObject jsonObject = new JSONObject(msg);
                                String _msg = jsonObject.getString("msg");
                                String code = jsonObject.getString("code");
//                                String totalcount = jsonObject.getString("totalcount");

//                                if ("0".equals(totalcount)) {
//                                    MistakeDialog.showDialog("totalCount:" + totalcount + ",服务器无该数据", StockDetailActivity.this);
//                                } else
                                if("0".equals(code)){
                                    StockInfoEntity stockInfoEntity = Db_PUB_STOCKLIST.queryStockFromID(stkCode);
                                    stockInfoEntity.setStock_flag(StockTable.STOCK_HISTORY_OPTIONAL);
                                    Db_PUB_SEARCHHISTORYSTOCK.addOneData(stockInfoEntity);
                                    Db_PUB_STOCKLIST.deleteStockFromID(stkCode);
                                    Db_HOME_INFO.deleteOneSelfNewsData(stkCode);//删除自选股新闻
                                    SelfChoiceStockTempData.getInstance().removeSelfchoicestockTempValue(stkCode);

                                    bottomMyStockIv.setImageResource(R.drawable.stk_icon_addself);
                                    bottomMyStockTv.setText("添加自选");
                                    isMyStock = false;
                                }else {
                                    if(isShow)
                                    {
                                        showDialog("删除自选股失败");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    simpleRemoteControl.setCommand(new ToDelteSlefChoiceStockConnect(new DeleteSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stkCode, UserUtil.userId)));
                    simpleRemoteControl.startConnect();
                } else {
                    StockInfoEntity stockInfoEntity = Db_PUB_STOCKLIST.queryStockFromID(stkCode);
                    stockInfoEntity.setStock_flag(StockTable.STOCK_HISTORY_OPTIONAL);
                    Db_PUB_SEARCHHISTORYSTOCK.addOneData(stockInfoEntity);
                    Db_PUB_STOCKLIST.deleteStockFromID(stkCode);
                    Db_HOME_INFO.deleteOneSelfNewsData(stkCode);//删除自选股新闻

//                    mDatas.get(position).setSelfChoicStock(false);
//                    Db_PUB_SEARCHHISTORYSTOCK.addOneData(mDatas.get(position));
                    SelfChoiceStockTempData.getInstance().removeSelfchoicestockTempValue(stkCode);

                    bottomMyStockIv.setImageResource(R.drawable.stk_icon_addself);
                    bottomMyStockTv.setText("添加自选");
                    isMyStock = false;
                }
            } else {
                BRutil.doListenAddOrRemoveSelfStock(stkCode, stkName, BRutil.ACTIONADDSELFSTOCK, "0");
                if (Db_PUB_USERS.isRegister()) {
                    //同步网络 从云端增加刚才增加的自选股
                    final Dialog dialog = LoadingDialog.initDialog(this, "正同步到云端");
                    dialog.show();
                    SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(new ICallbackResult() {
                        @Override
                        public void getResult(Object result, String tag) {
                            StockInfoEntity _bean = new StockInfoEntity();
                            _bean.setStockNumber(stkCode);
                            _bean.setStockName(stkName);
                            _bean.setStock_flag(StockTable.STOCK_OPTIONAL);
                            if (dialog != null) {
                                dialog.dismiss();
                            }

                            String msg = (String) result;
                            try {
                                JSONObject jsonObject = new JSONObject(msg);
                                String code = jsonObject.getString("code");
                                if (!"0".equals(code)) {
                                    if(isShow) {
                                        showDialog("添加失败");
                                    }
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            boolean isNotFull = Db_PUB_STOCKLIST.addOneStockListData(_bean);
                            if (!isNotFull) {
                                CentreToast.showText(CustomApplication.getContext(),"自选股超出50条上线，请删除再添加");
                                return;
                            } else {
                                Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stkCode);
                                SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), stkCode);
                                SelfChoiceStockTempData.getInstance().setSelfchoicestockTempValue(stkCode, stkName);
                            }


                            isMyStock = true;
                            setBottomTools(false);

                        }
                    });
                    simpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stkCode, UserUtil.userId, stkName, "")));
                    simpleRemoteControl.startConnect();
                } else {
                    StockInfoEntity _bean = new StockInfoEntity();
                    _bean.setStockNumber(stkCode);
                    _bean.setStockName(stkName);
                    _bean.setStock_flag(StockTable.STOCK_OPTIONAL);
                    boolean isNotFull = Db_PUB_STOCKLIST.addOneStockListData(_bean);
                    if (!isNotFull) {
                        if(isShow) {
                            CentreToast.showText(CustomApplication.getContext(), "自选股超出50条上线，请删除再添加");
                        }
                        return;
                    } else {
                        Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stkCode);
                        SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), stkCode);
                        SelfChoiceStockTempData.getInstance().setSelfchoicestockTempValue(stkCode, stkName);
                    }

                    isMyStock = true;
                    setBottomTools(false);
                }
            }
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),StockDetailActivity.class.toString());
    }

    private void gotoStkBuyAndSell(String optType) {
        if (stkType==0||stkType==2) {
            CentreToast.showText(StockDetailActivity.this, "当前股票代码不可交易");

            return;
        }
        Intent intent = new Intent();
        intent.putExtra("stockcode", stkCode);
        intent.putExtra("status", optType);
//        intent.putExtra("pageindex", TransactionLoginActivity.PAGE_INDEX_BUY_SELL);
//        if (!Db_PUB_USERS.isRegister()) {
//            intent.setClass(this, ShouJiZhuCeActivity.class);
//            startActivity(intent);
//        } else {
//            if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
//                intent.setClass(this, BuyAndSellActivity.class);
//                startActivity(intent);
//            }else{
//                intent.setClass(this, TransactionLoginActivity.class);
//                startActivity(intent);
//            }
//        }
        HomeFragmentHelper.getInstance().gotoPage(this,TransactionLoginActivity.PAGE_INDEX_BUY_SELL,intent);
    }
    private void showMoreSpinner() {
//        LogHelper.e(TAG,"showMoreSpinner");
        ArrayList<String> datas = new ArrayList<String>();
        datas.add("添加提醒");
        datas.add("取消自选");
        ArrayList<Integer> dataIds = new ArrayList<Integer>();
        dataIds.add(R.drawable.stk_icon_addalarm);
        dataIds.add(R.drawable.stk_icon_delself);
        CustomSpinner customSpinner = new CustomSpinner(StockDetailActivity.this, datas, true,bottomChooseListener);
        customSpinner.setDefaultWidth(dip2px(40));
        CustomSpinner.ISDROP = false;
        customSpinner.setImgIds(dataIds);
        LinearLayout ll = (LinearLayout) findViewById(R.id.detail_BottomLayout);
        int pos = screenHeight-dip2px(100);
        customSpinner.showAtlocal2(ll,pos);
    }
    String[] popWindowStrs = {"5分钟","15分钟","30分钟","60分钟"};
    private void showMinutesSpinner() {
        ArrayList<String> datas = new ArrayList<String>();
        for (int i=0;i<popWindowStrs.length;i++)
        {
            datas.add(popWindowStrs[i]);
        }

        CustomSpinner customSpinner = new CustomSpinner(StockDetailActivity.this, datas, false, StockDetailActivity.this);
        CustomSpinner.ISDROP = true;
        customSpinner.setDefaultWidth(dip2px(30));
//        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.minutesLayout);
        if(isLandScreen){
            customSpinner.show(landchartLabs[5]);
        }else{
            customSpinner.show(mMinutesTv);
        }

    }
    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    @Override
    public void onChooseItem(int position,String str) {
        MinuteKlineType = position;
        setChartIndexFocus(5);
        if(isLandScreen) {
            landchartLabs[5].setText(str);
        }
        chartLabs[5].setText(str);
    }

    private CustomSpinner.ChooseItemListener bottomChooseListener = new CustomSpinner.ChooseItemListener() {
        @Override
        public void onChooseItem(int position, String view) {
            if(position==0){
                Intent intent = new Intent();
                String stockNumber = stkCode;
                String stockName = stkName;

                if (!TextUtils.isEmpty(stockNumber)) {
                    intent.putExtra("stockNumber", stockNumber);
                }

                if (!TextUtils.isEmpty(stockName)) {
                    intent.putExtra("stockName", stockName);
                }
                intent.setClass(StockDetailActivity.this, RemainActivity.class);
                startActivity(intent);
            }else{
                dealMyStock();
            }
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.activity_stockdetail;
    }

    @Override
    protected void onDestroy() {
//        LogHelper.e(TAG,"onDestroy");
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(this);
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShow = false;
//        LogHelper.e(TAG,"onPause");
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }
    private boolean isOnCreate = true;
    @Override
    protected void onResume() {
        super.onResume();
        isShow = true;
//        LogHelper.e(TAG,"onResume:"+isOnCreate);
        if(!isOnCreate)
        {
            minDataLastTime = "093000";//重新恢复，分时从头请求
            request();
            setTimerTask();
            setBottomTools(true);
        }
//        LogHelper.e(TAG,"onResume initTime:"+( System.currentTimeMillis()-time1));
        isOnCreate = false;
    }

    private void request() {
        isCallBackSuccess = false;
        Map<String, String> params = new HashMap();
        try {

//            String strJson = gson.toJson(object);
//            byte reqType = 0;
//            if(stkType==0){
//                reqType = 1;
//            }
            params.put("PARAMS", "[{\"market\":\""+stkCode.substring(0,1)+"\",\"stockcode\":\""+stkCode+"\",\"time\":\""+minDataLastTime+"\",\"type\":\"0\"}]");
            params.put("FUNCTIONCODE","HQING003");

//            LogHelper.i(TAG, ""+params.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // QutataionConnectUtil.HANGQING_URL 对应 ConstantUtil.URL 外网
        //QutataionConnectUtil.NEWSTOCK_URL 对应 ConstantUtil.URL_NEW  //新股日历页的网络接口
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                LogHelper.e(TAG, "stockDetailData call Back");
                try{
                    JSONObject jsonObject = new JSONObject(response.substring(1,response.length()-1));
                    if(!"0".equals(jsonObject.optString("code"))){
                        LogHelper.e(TAG, "stockDetailData 数据异常:"+response);
                        return;
                    }
                    String time = jsonObject.optString("time");
//                    LogHelper.e(TAG, "stockDetailData call Back response:"+response);
                    refleshTime = Integer.parseInt(time);
                    JSONArray dataArr = jsonObject.optJSONArray("data");
//                    LogHelper.e(TAG, "**********"+dataArr.get(2));
                    String a_close = dataArr.get(2).toString();
                    String a_newPrice = dataArr.get(46).toString();
                    wholeTime = dataArr.get(47).toString();
                    float newP = 0f;
                    float clostF = 0f;
                    try{
                        newP = Float.parseFloat(a_newPrice);
                        clostF = Float.parseFloat(a_close);
                        stk_open = Float.parseFloat(dataArr.get(40).toString());
                    }catch (Exception exx ){
                    }
                    stk_close = clostF;

                    StockInfoEntity stockInfoEntity = new StockInfoEntity();
                    stockInfoEntity.setNewPrice(a_newPrice);
                    stockInfoEntity.setClose(a_close);
                    stockInfoEntity.setIndustryName(dataArr.get(11).toString());
                    stockInfoEntity.setIndustryUpAndDown(dataArr.get(16).toString());
                    stockInfoEntity.setTime(wholeTime);
                    stopFlag = dataArr.get(43).toString();
                    setTextViewValue(stockInfoEntity);
//                    LogHelper.e(TAG, "**********TPY:"+dataArr.get(9)+"|"+dataArr.get(5));
                    String[]gridTopDatas1 = new String[4];
                    String[]gridTopDatas2 = new String[8];
                    String a_high = dataArr.get(3).toString();
                    String a_low = dataArr.get(4).toString();

                    if(isShowMingxi){
                        String curVol = dataArr.get(8).toString();
                        String forword = dataArr.get(41).toString();
//                        appendTradeDetail(wholeTime,a_newPrice,curVol,forword);
                    }
                    if(stkType==0){
                        gridTopDatas1[1] =mFormat1.format(Float.parseFloat(a_high));
                        gridTopDatas1[2] =mFormat1.format(Float.parseFloat(a_low));

                        String am = dataArr.get(5).toString();
                        BigDecimal bd = new BigDecimal(am);
//                        LogHelper.e(TAG, "**********TPY:"+bd2.toPlainString()+"|"+bd.toPlainString()+"|||"+DealAmount(bd.toPlainString()));
                        gridTopDatas1[0] =DealAmount(bd.toPlainString());
                        gridTopDatas1[3]=mFormat1.format(Float.parseFloat(dataArr.get(40).toString()));
                        landVolStr = DealAmount(dataArr.get(18).toString());
                        gridTopDatas2[0]=dataArr.get(48).toString();//涨
                        gridTopDatas2[1]=dataArr.get(49).toString();//跌
                        gridTopDatas2[2]=dataArr.get(50).toString();//平
                        gridTopDatas2[3]=mFormat1.format(Float.parseFloat(dataArr.get(2).toString()));//昨收
                    }else{
                        gridTopDatas1[0] =mFormat1.format(Float.parseFloat(a_high));
                        gridTopDatas1[1] =mFormat1.format(Float.parseFloat(a_low));
                        String am = dataArr.get(5).toString();
                        BigDecimal bd = new BigDecimal(am);
                        gridTopDatas1[3] =DealAmount(bd.toPlainString());
                        try{
                            String ra = dataArr.get(9).toString();
                            BigDecimal bd2 = new BigDecimal(ra);
                            gridTopDatas1[2]=mFormat2.format(Double.parseDouble(bd2.toPlainString()));
                        }catch (Exception exx ){
                            gridTopDatas1[2]="--";
                        }
                        String zgb = dataArr.get(14).toString();
                        String ltgb = dataArr.get(13).toString();
//                        LogHelper.e(TAG, "**********TPY :"+bd2.toPlainString()+"|"+bd.toPlainString()+"|||"+DealAmount(bd.toPlainString()));
                        gridTopDatas2[0]=DealAmount(dataArr.get(6).toString());//内盘
                        gridTopDatas2[1]=DealAmount(dataArr.get(7).toString());//外盘
                        gridTopDatas2[2]=kxjsf(dataArr.get(17).toString());//量比
                        gridTopDatas2[3]=DealAmount(dataArr.get(18).toString());//成交量
                        landVolStr = gridTopDatas2[3];
                        gridTopDatas2[4]=kxjsf(dataArr.get(12).toString());//市盈率
                        gridTopDatas2[5]=kxjsf(dataArr.get(15).toString());//市净率
                        try{
                            gridTopDatas2[6]=DealAmount(Long.parseLong(ltgb)*newP,false);//流通市值
                            gridTopDatas2[7]=DealAmount(Long.parseLong(zgb)*newP,false);//总市值
                        }catch (Exception loE){
                            gridTopDatas2[6]="--";//流通市值
                            gridTopDatas2[7]="--";//总市值
                        }
                    }
                    backStkName = dataArr.optString(19);
                    setHighAndLowColor(a_high,a_low,a_close);
                    mAdapter1.setColors(HighAndLowColor);
                    mAdapter1.setDatas(getGrideDatas1(gridTopDatas1));

                    mAdapter2.setDatas(getGrideDatas2(gridTopDatas2));

                    JSONArray minDataArray = dataArr.getJSONArray(0);
                    fillMinData(minDataArray,clostF);
                    if(isLandScreen){
                        setLandData();
                    }
                    if(stkType!=0){
                        setPankouData(dataArr,clostF);
                    }
                    myHandler.sendEmptyMessage(1);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    private String backStkName = "";

    //    private double minTotalPrice = 0;
//    private double minLastVol = 0;
    private synchronized  void fillMinData(JSONArray minDataArray,float a_close){
        try {
//            LogHelper.e(TAG,minDataArray.toString());
            int lenT = minDataArray.length();
            m_minData.m_fPreClosePrice= a_close;

            if (lenT > 0) {
                if (MIN_START_TIME.equals(minDataLastTime)) {
//                    String volStr = "" ;
                    minDataLen = lenT;
//                    double lastVol =0;
                    for (int i=0;i<lenT;i++){
                        JSONArray itemObj = minDataArray.getJSONArray(i);
                        MinPoint mp = new MinPoint();

                        mp.m_fCurPrice = Float.parseFloat(itemObj.optString(1));

                        if(mp.m_fCurPrice==0){
                            if(i==0){
                                mp.m_fCurPrice = a_close;
                            }else{
                                mp.m_fCurPrice = m_minData.minPoints[i-1].m_fCurPrice;
                            }
                        }
                        mp.m_fAvgPrice = Float.parseFloat(itemObj.optString(3));//(float) minTotalPrice/(i+1f);
                        if(mp.m_fAvgPrice==0){
                            if(i==0){
                                mp.m_fAvgPrice =  mp.m_fCurPrice;
//                                LogHelper.e(TAG,"均价是0,置当前价为均价 mp.m_fCurPrice："+mp.m_fCurPrice);
                            }else{
                                mp.m_fAvgPrice = m_minData.minPoints[i-1].m_fAvgPrice;
                            }
                        }
//                        LogHelper.e(TAG, (i)  +"  partOne MinData avgPrice:"  +mp.m_fCurPrice);
//                        if(i==0){
//                            LogHelper.e(TAG,"firstPointValue："+Float.parseFloat(itemObj.optString(1))+","+Float.parseFloat(itemObj.optString(3)));
//                        }

//                        minTotalPrice += mp.m_fCurPrice;
                        mp.m_nMinuteNum = Integer.parseInt(itemObj.optString(0));
//                        if(i>lenT-20)
//                            LogHelper.e(TAG,i+"MinData:"+mp.m_nMinuteNum+"|"+mp.m_fCurPrice+"|"+mp.m_fAvgPrice+"|"+mp.m_nVol);
                        if(mp.m_nMinuteNum==0){
                            if(i==0){
                                mp.m_nMinuteNum = 93000;
                            }else
                            {
                                mp.m_nMinuteNum = Integer.parseInt(correctMinTime(i));
                            }
                        }
//                        mp.m_fAvgPrice = (float) minTotalPrice/(i+1f);
                        double volT = Double.parseDouble(itemObj.optString(2));
                        mp.m_dTotalVol = volT;
                        if(i==0){
                            if(mp.m_fCurPrice==0){
                                mp.m_fCurPrice = a_close;
                            }
                            mp.m_nVol = volT;
                        }else{
                            if(volT==0){
                                mp.m_nVol = 0;
                                mp.m_dTotalVol = m_minData.minPoints[i-1].m_dTotalVol;
                            }/*else
                            {
                                mp.m_nVol = volT-lastVol;
                            }*/
                        }
                        if(i>0){
//                            if(mp.m_nVol!=mp.m_dTotalVol-m_minData.minPoints[i-1].m_dTotalVol){
//                                LogHelper.e(TAG,"vol Error:"+i+"  mp.m_dTotalVol"+ "  "+mp.m_nVol);
//                            }
                            mp.m_nVol = mp.m_dTotalVol-m_minData.minPoints[i-1].m_dTotalVol;
                        }else{
                            mp.m_nVol = volT;
                        }
//                        if(volT>0) {
//                            lastVol = volT;
//                            minLastVol = volT;
//                        }
//                        if(i>lenT-10)
//                        LogHelper.e(TAG,i+"MinData:"+mp.m_nMinuteNum+"|"+mp.m_fCurPrice+"|"+mp.m_fAvgPrice+"|"+mp.m_nVol+"|"+mp.m_dTotalVol);
                        m_minData.minPoints[i]=mp;
                    }
//                    LogHelper.e(TAG,"VOL:"+volStr);
                } else {
                    minDataLen = m_minData.minPointsLen;
                    int posTemp = minDataLen-1;
//                    double lastVol =minLastVol;
                        for (int i=0;i<lenT;i++) {
                            JSONArray itemObj = minDataArray.getJSONArray(i);
                            MinPoint mp = new MinPoint();
                            mp.m_fCurPrice = (Float.parseFloat(itemObj.optString(1)));
                            if (mp.m_fCurPrice == 0 ) {
                                mp.m_fCurPrice = m_minData.minPoints[posTemp+i - 1].m_fCurPrice;
                            }
                            mp.m_fAvgPrice = (Float.parseFloat(itemObj.optString(3)));
                            if (mp.m_fAvgPrice == 0 ) {
                                mp.m_fAvgPrice = m_minData.minPoints[posTemp+i - 1].m_fAvgPrice;
                            }
                            mp.m_nMinuteNum = Integer.parseInt(itemObj.optString(0));
                            if (mp.m_nMinuteNum == 0) {
                                mp.m_nMinuteNum = Integer.parseInt(correctMinTime(posTemp+i));
                            }
                            double volT = Double.parseDouble(itemObj.optString(2));
                            mp.m_dTotalVol = volT;
                            if (volT == 0) {
                                mp.m_nVol = 0;
                                if(posTemp+i-1>=0)
                                {
                                    mp.m_dTotalVol = m_minData.minPoints[posTemp+i-1].m_dTotalVol;
                                }
                            }/* else {
                                mp.m_nVol = volT - lastVol;
                            }*/
                            if(posTemp+i-1>=0)
                            {
                                mp.m_nVol = mp.m_dTotalVol-m_minData.minPoints[posTemp+i-1].m_dTotalVol;
                            }else{
                                mp.m_nVol = volT;
                            }
//                        LogHelper.e(TAG, (posTemp+i) +"  "+i +"  MinData:" + mp.m_fCurPrice + "|" + mp.m_fAvgPrice +mp.m_nVol);
//                            if (volT > 0) {
//                                lastVol = volT;
//                                minLastVol = volT;
//                            }
                            m_minData.minPoints[posTemp+i] = mp;
                    }

                    minDataLen = minDataLen + lenT - 1;
                }

                m_minData.minPointsLen = minDataLen;
                m_minData.m_ftodayOpenPrice = stk_open;
//                LogHelper.e(TAG, "**********stk_open:"+stk_open);
                if(isLandScreen)
                {
                    minChartLand.setData(m_minData,mFormat1);
                }else{
                    minChart.setData(m_minData,mFormat1);
                }
                JSONArray lastItemObj = minDataArray.getJSONArray(lenT - 1);
                minDataLastTime = lastItemObj.optString(0);
                if(minDataLastTime.startsWith("15")){
                    minDataLastTime = "150000";
                }
                if("0".equals(minDataLastTime)){
                    minDataLastTime = correctMinTime(minDataLen-1);
                }
                if(minDataLen==241){//如果给足一天，分时从开始请求
                    minDataLastTime = "093000";
                }
//                LogHelper.e(TAG, "stockDetailData minDataLastTime:" + minDataLastTime);
//                fromMinCreateFiveDayMinData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    private void fromMinCreateFiveDayMinData(){
//        if(m_minData.minPointsLen>0){
//
//        }
//    }

    public static String DealAmount(double val,boolean isNoPoint){
        String ret = "";
        DecimalFormat mFormatT = mFormat3;
        try{
            if(isNoPoint){
                mFormatT = new DecimalFormat("0");
            }
            double temp = val;
            if(temp>100000000){
                double d = temp/100000000d;
                ret = mFormat3.format(d)+"亿";
            }else
            if(temp>100000){
                double d = temp/10000d;
                ret = mFormat3.format(d)+"万";
            } else {
                ret = mFormatT.format(temp);
            }

        }catch (Exception e){
            ret = "0.00";
            if(isNoPoint){
                ret = "0";
            }
            e.printStackTrace();
        }
        return  ret;
    }
    private String DealAmount2(String val){
        String ret = "";
        try{
            double temp = Double.parseDouble(val);
            ret = DealAmount(temp,true);
        }catch (Exception e){
            ret = "0";
            e.printStackTrace();
        }
        return  ret;
    }
    private String DealAmount(String val){
        String ret = "";
        try{
            double temp = Double.parseDouble(val);
            ret = DealAmount(temp,false);
        }catch (Exception e){
            ret = "0.00";
            e.printStackTrace();
        }
        return  ret;
    }

    private void setHighAndLowColor(String a_high,String a_low,String a_close){
        try {
            Float h = Float.parseFloat(a_high);
            Float l = Float.parseFloat(a_low);
            Float c = Float.parseFloat(a_close);
            if(a_high.equals(a_close)){
                HighAndLowColor[0] = mGray2;
            }else{
                HighAndLowColor[0] = h>c?mRed:mGreen;
            }

            if(a_low.equals(a_close)){
                HighAndLowColor[1] = mGray2;
            }else{
                HighAndLowColor[1] = l>c?mRed:mGreen;
            }
            if(h==0){
                HighAndLowColor[0] = mGray2;
            }
            if(l==0){
                HighAndLowColor[1] = mGray2;
            }
        }catch (Exception e){
            HighAndLowColor[0] = mGray2;
            HighAndLowColor[1] = mGray2;
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener topClosListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setDetailTopGridStatus();
        }
    };
    private void setDetailTopGridStatus(){
        if (isGrideViewGone) {
            receiveGridBtn.setImageResource(R.mipmap.receive_p);
            mBottomGridView.setVisibility(View.GONE);
            isGrideViewGone = false;
        } else {
            receiveGridBtn.setImageResource(R.mipmap.receive_n);
            mBottomGridView.setVisibility(View.VISIBLE);
            isGrideViewGone = true;
        }
    }
    //转化科学计数法
    private String kxjsf (String val){
        String ret = "";
        try{
            if(val.isEmpty()||"-".equals(val)||"Infinity".equals(val)){
                return "--";
            }
            BigDecimal bd2 = new BigDecimal(val);
            ret = bd2.toPlainString();
            Double d = Double.parseDouble(ret);
            ret= mFormat3.format(d);
        }catch (Exception e){
            //e.printStackTrace();
            LogHelper.e(TAG,"科学计数法异常:"+val);
            ret = "-";
        }
        return ret;
    }
    private double kxjsf (double val){
        double ret = 0;
        try{
            BigDecimal bd2 = new BigDecimal(val);
            Double d = Double.parseDouble(bd2.toPlainString());
            ret = d;
        }catch (Exception e){
            //e.printStackTrace();
            LogHelper.e(TAG,"科学计数法异常:"+val);
            ret =0;
        }
        return ret;
    }
    //转化科学计数法
    private String kxjsfPercent (String val){
        String ret = "";
        try{
            if(val.isEmpty()||"-".equals(val)||"Infinity".equals(val)){
                return "--";
            }
            BigDecimal bd2 = new BigDecimal(val);
            ret = bd2.toPlainString();
            Double d = Double.parseDouble(ret);
            ret= mFormat2.format(d);
            if(d>0){
                ret ="+"+ret;
            }
        }catch (Exception e){
            //e.printStackTrace();
            LogHelper.e(TAG,"科学计数法异常:"+val);
            ret = "-";
        }
        return ret;
    }
    private int refleshCount = 0;
    private void setTimerTask() {
        if(mTimer!=null){
            mTimer.cancel();
        }
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogHelper.e(TAG, "stockDetailData Timer request:"+isCallBackSuccess+"|"+refleshTime+"  refleshCount:"+refleshCount);
                if(isCallBackSuccess)
                {
                    if(!"0".equals(refleshTime)){
                        if(refleshTime>0){
                            refleshCount +=3;
                            if(refleshCount>=refleshTime){
                                request();
                            }
                        }else{
                            refleshCount = 0;
                            request();
//                            LogHelper.e(TAG, "stockDetailData Timer request:"+isShowMingxi);
                            //没显示成交明细不刷新
                            if(mCurrentIndex==0&&isShowMingxi)
                            {
                                requestTradeDetail();
                            }
                        }
                    }

                }
            }
        }, 3000, 3000/* 表示3000毫秒之後，每隔3000毫秒執行一次 */);
    }


    private void setLandChartLayout(){
        switch (mCurrentIndex){
            case 0:
                land_itemLayout5.setVisibility(View.GONE);
                land_itemLayout6.setVisibility(View.GONE);
                if(land_chartWholeLayout.getChildCount()>0){
                    land_chartWholeLayout.removeAllViews();
                }
                land_chartWholeLayout.addView(land_minChartWholeLayout);
                land_KlineLayout.setVisibility(View.GONE);
                break;
            case 1:
                land_itemLayout5.setVisibility(View.GONE);
                land_itemLayout6.setVisibility(View.GONE);
                land_KlineLayout.setVisibility(View.GONE);
                land_chartWholeLayout.removeAllViews();
                if(landFiveDayMinChart==null){
                    landFiveDayMinChart = new FiveDayMinChart(this,null);
                    landFiveDayMinChart.setViewLand(true);
                    landFiveDayMinChart.setListener(this);
                }
                land_chartWholeLayout.addView(landFiveDayMinChart);
                landFiveDayMinChart.setData(m_fiveDayminData,mFormat1);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                land_itemLayout5.setVisibility(View.VISIBLE);
                land_itemLayout6.setVisibility(View.VISIBLE);
//                if(land_chartWholeLayout.getChildCount()>0){
//                    land_chartWholeLayout.removeAllViews();
//                }
                land_KlineLayout.setVisibility(View.VISIBLE);
//                if(landKline==null){
//                    landKline = new KLine(this,null);
//                    landKline.setLandFalg(true);
//                    landKline.setListener(this);
//                }
                landKline.setKlineCycle(Kline_cycle);
                landKline.clearKlineData();
//                land_chartWholeLayout.addView(landKline);
                landKline.setData(kdata,mFormat1);
                break;

        }
    }

    private int lastChartIndex = 0;
    private void setChartIndexFocus(int a_index){
        if(lastChartIndex!=-1){
            chartLabs[lastChartIndex].setTextColor(mGray3);
            landchartLabs[lastChartIndex].setTextColor(mGray3);
        }
        if(a_index!=5){
            mMinutesTv.setText("分钟");
            MinuteKlineType = 0;
        }
        chartLabs[a_index].setTextColor(mBlue);
        mCurrentIndex = a_index;
        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams) mTabLine.getLayoutParams();
        lp.leftMargin = mScreen1_6*(mCurrentIndex) +tabFocusGap;
        mTabLine.setLayoutParams(lp);

        switch (a_index){
            case 0:
                if(chartFrameLayout.getChildCount()>0){
                    chartFrameLayout.removeAllViews();
                }
                chartFrameLayout.addView(minChartWholeLayout);
                fiveDataTempDate = "";
                break;
            case 1:
                chartFrameLayout.removeAllViews();
                chartFrameLayout.addView(fiveDayMinChart);
                requestFiveDay();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                if(chartFrameLayout.getChildCount()>0){
                    chartFrameLayout.removeAllViews();
                }
                fiveDataTempDate = "";
                if(kLine==null){
                    kLine = new KLine(this,null);
                    kLine.setListener(this);
                }

                Kline_cycle = a_index-2+MinuteKlineType;
                Kline_xrdrType = 0;//切换周期时 初始化 除复权标志、指标标志

                kLine.setKlineCycle(Kline_cycle);
                kLine.clearKlineData();
                requestKlineData();

                if (kLine.getParent() != null && kLine.getParent() instanceof FrameLayout) {
                    FrameLayout parentLayout = (FrameLayout) kLine.getParent();
                    parentLayout.removeView(kLine);
                }
                chartFrameLayout.addView(kLine);
                Kline_xrdrType = 0;
                break;

        }
        if(isLandScreen){
            setLandIndexFocus();
            setLandChartLayout();
            initLandItemDetailTvs();
            switch (mCurrentIndex){
                case 0:
                    if(minChartLand!=null)
                    {
                        fiveDataTempDate = "";
                        minChartLand.setDetailTvs(landItemDetailValueTvs);
                    }
                    break;
                case 1:
                    if(landFiveDayMinChart!=null)
                    {
                        landFiveDayMinChart.setDetailTvs(landItemDetailValueTvs);
                    }
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    if(landKline!=null){
                        fiveDataTempDate = "";
                        landKline.setDetailTvs(landItemDetailValueTvs);
                    }
                    break;
            }
        }
        lastChartIndex = a_index;
    }
    private void setLandIndexFocus(){
        if(mCurrentIndex!=5){
            landchartLabs[5].setText("分钟");
            MinuteKlineType = 0;
            landchartLabs[5].setText("分钟");
        }else{
            landchartLabs[5].setText(popWindowStrs[MinuteKlineType]);
        }
        landchartLabs[mCurrentIndex].setTextColor(mBlue);
        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams) mTabLandLine.getLayoutParams();
        lp.leftMargin = mScreenLand1_6*(mCurrentIndex)+landtabFocusGap ;
        mTabLandLine.setLayoutParams(lp);
    }
    private boolean isStock(){
        return stkType == 1 || stkType == 2 || stkType == 6 || stkType == 7;
    }
    //时间修正，根据索引生成分时分钟数
    private String correctMinTime(int a_index){
//        String ret = "093000";
        String tempStr = "";
        int nBase = 89; //开市基础时间
        if(a_index<121){
            nBase=0;
        }
        int start = 9*60+30+a_index+nBase;
        int nHour = start / 60 ;
        int nMin  = (start% 60);
        if(nHour<10){
            tempStr = "0"+nHour;
        }else{
            tempStr += nHour;
        }
        if(nMin<10){
            tempStr += "0"+nMin;
        }else{
            tempStr += nMin;
        }
        tempStr += "00";
//        System.out.println(a_index+"|"+tempStr);
        return tempStr;
    }
//    public boolean isNumeric(String str){
//        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
//        Matcher isNum = pattern.matcher(str);
//        if( !isNum.matches() ){
//            return false;
//        }
//        return true;
//    }

    private void setPankouFocus(boolean isPankou){
        if(isPankou){
            pankoutTabLayout.setBackgroundResource(R.drawable.stk_detail_left);
            pankouView.setTextColor(mWhite);
            mingxiView.setTextColor(mBlue);
            pankouItemListView.setAdapter(pankouAdapter);
            isShowMingxi = false;
            if(isLandScreen){
                land_pankouItemListView.setAdapter(pankouAdapter);
            }
            land_pankoutTabLayout.setBackgroundResource(R.drawable.stk_detail_left);
            land_pankouView.setTextColor(mWhite);
            land_mingxiView.setTextColor(mBlue);
        }else{
            pankoutTabLayout.setBackgroundResource(R.drawable.stk_detail_right);
            mingxiView.setTextColor(mWhite);
            pankouView.setTextColor(mBlue);
            requestTradeDetail();
            pankouItemListView.setAdapter(mingxiAdapter);
            if(isLandScreen){
                land_pankouItemListView.setAdapter(mingxiAdapter);
            }
            isShowMingxi = true;

            land_pankoutTabLayout.setBackgroundResource(R.drawable.stk_detail_right);
            land_mingxiView.setTextColor(mWhite);
            land_pankouView.setTextColor(mBlue);
//            pankouAdapter.setData(dataMingxi);
//            pankouAdapter.notifyDataSetChanged();
        }

    }

    private static final String pankouTitles[] = {"卖5","卖4","卖3","卖2","卖1","",
            "买1","买2","买3","买4","买5"};

    ArrayList<PankouItem> dataWudang = new ArrayList<>();
    ArrayList<PankouItem> dataMingxi = new ArrayList<>();
    private void initPankouData(){
        for (int i=0;i<pankouTitles.length;i++) {
            PankouItem pki = new PankouItem();
            pki.m_Str1 = pankouTitles[i];
            pki.m_Color1 = mGray;

            pki.m_Str2 = "-";
            pki.m_Color2 = mGray;

            pki.m_Str3 = "-";
            pki.m_Color3 = mGray2;
            dataWudang.add(pki);
        }
        pankouAdapter.setData(dataWudang);
    }

    private void setPankouData(JSONArray minDataArray,float a_close){
        float curPrice = 0;
        //卖5数据处理
        for (int i=0;i<5;i++) {
            PankouItem pki = dataWudang.get(i);
            pki.m_Str1 = pankouTitles[i];
            pki.m_Color1 = mGray;
            curPrice = Float.parseFloat(minDataArray.optString(29-i));
            pki.m_Str2 =mFormat1.format(curPrice) ;
            if(curPrice==0||curPrice==a_close){
                pki.m_Color2 = mGray;
            }else{
                pki.m_Color2 = curPrice>a_close?mRed:mGreen;
            }

            pki.m_Str3 =  DealAmount2(minDataArray.optString(39-i));
            dataWudang.set(i,pki);
        }
        //买5数据处理
        for (int i=0;i<5;i++) {
            PankouItem pki = dataWudang.get(6+i);
//            pki.m_Str1 = pankouTitles[i];
//            pki.m_Color1 = mGray;
            curPrice = Float.parseFloat(minDataArray.optString(20+i));
            pki.m_Str2 = mFormat1.format(curPrice);
            if(curPrice==0||curPrice==a_close){
                pki.m_Color2 = mGray;
            }else{
                pki.m_Color2 = curPrice>a_close?mRed:mGreen;
            }
            pki.m_Str3 =  DealAmount2(minDataArray.optString(30+i));//;
            dataWudang.set(6+i,pki);
        }

        pankouAdapter.notifyDataSetChanged();
    }


    private void requestTradeDetail() {
        Map<String, String> params = new HashMap();
        try {
            params.put("PARAMS", "[{\"market\":\""+stkCode.substring(0,1)+"\",\"code\":\""+stkCode+"\",\"pageIndex\":\"-1\",\"num\":\"11\"}]");
            params.put("FUNCTIONCODE","HQING017");

//            LogHelper.e(TAG, ""+params.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        NetWorkUtil.getInstence().okHttpForGet(TAG+"detail", ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(1, response.length() - 1));
                    if (!"0".equals(jsonObject.optString("code"))) {
                        LogHelper.e(TAG, "stockDetailData 数据异常:" + response);
                        return;
                    }
                    int totalCount = jsonObject.optInt("totalcount");
                    if(totalCount>0){
                        String obj = jsonObject.optString("data");
                        JSONArray dataArr = new JSONArray(obj);
                        analysisTradeDetail(dataArr,totalCount);
                        mingxiAdapter.notifyDataSetChanged();
                    }
//                    LogHelper.e(TAG, "stockDetailData call Back totalCount:"+totalCount);

                }catch (Exception e){
                    e.printStackTrace();
                }
//                LogHelper.e(TAG, "stockDetailTradeData :"+response);
            }
        });
    }
    private void appendTradeDetail(String time,String price,String vol1,String forword1) throws JSONException{
//        LogHelper.e(TAG,time+"|"+price+"|"+vol1+"|"+forword1);

        try{
            String t = time.substring(11);
            int curSize = dataMingxi.size();
            String itemTime = t.substring(0,2)+":"+t.substring(2,4);
            if(curSize>0){
                PankouItem pki1 = dataMingxi.get(curSize-1);
                if(pki1.m_bak.equals(t)){
//                    LogHelper.e(TAG,"相同不追加："+pki1.m_bak+"|"+pki1.m_Str2);
                    return;
                }
            }
//            else{
//
//            }
            float curPrice = Float.parseFloat(price);
            double vol = Double.parseDouble(vol1);
//            int forword = Integer.parseInt(forword1);
            PankouItem pki = new PankouItem();
            pki.m_bak=t;
            pki.m_Str1 = itemTime;
            pki.m_Color1 = mGray;

            pki.m_Str2 = mFormat1.format(curPrice);
            if(stk_close==0){//不知道昨收
                pki.m_Color2 = mGray;
            }else{
                if(curPrice==0||curPrice==stk_close){
                    pki.m_Color2 = mGray;
                }else{
                    pki.m_Color2 = curPrice>stk_close?mRed:mGreen;
                }
            }
            pki.m_Str3 = DealAmount(vol,true);
            pki.m_Color3=mGray2;
            if(forword1.equals("B")){
                pki.m_Str4 = "B";
                pki.m_Color4=mRed;
            }else if(forword1.equals("S")){
                pki.m_Str4 = "S";
                pki.m_Color4=mGreen;
            }else{
                pki.m_Str4="";
                pki.m_Color4=mGray;
            }
            dataMingxi.add(pki);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(dataMingxi.size()>11){
            dataMingxi.remove(0);
            PankouItem ppp = dataMingxi.get(dataMingxi.size()-1);
//            LogHelper.e(TAG,"追加超长，移除第一个，最后一个为："+ppp.m_Str1+"|"+ppp.m_Str2);
        }
        mingxiAdapter.notifyDataSetChanged();
    }
    private void analysisTradeDetail(JSONArray arry,int count) throws JSONException{
        dataMingxi.clear();
//        int len = arry.length();
//        byte [] b = new byte[len];
//        for (int i = 0;i<len;i++){
//            b[i] = (byte)arry.optInt(i);
//        }
//        ByteBuffer buf = ByteBuffer.wrap(b);

        int k = 0;
        if(count>11){
            k=count-11;
        }
//        int curDate = 0;
//        if(!wholeTime.equals("")){
//            curDate = Integer.parseInt(wholeTime.substring(0,10).replace("-",""));
//        }
//        LogHelper.e(TAG,"wholeTime："+wholeTime+"|curDate:"+curDate);
            while(k<count){
                JSONObject objItem = arry.optJSONObject(k);
                PankouItem pki = new PankouItem();
//                int dd = buf.getInt();
//                String d = dd+"";
//                if(curDate!=0&&curDate!=dd){
//                    LogHelper.e(TAG,"日期不相等，不填充d："+d+"|curDate:"+curDate);
//                    break;
//                }
                String t = objItem.optString("time");//buf.getInt()+"";
                double curPrice = Double.parseDouble(objItem.optString("curprice"));//buf.getDouble();
                long vol = Long.parseLong(objItem.optString("curAmount"));//buf.getLong();
//                int forword = buf.getInt();
                String forword = objItem.optString("pricrStatus");
                if(t.length() == 5)t = "0"+t;
                pki.m_bak = t;
                pki.m_Str1 = t.substring(0,2)+":"+t.substring(2,4);
                pki.m_Color1 = mGray;

                pki.m_Str2 = mFormat1.format(curPrice);
                if(stk_close==0){//不知道昨收
                    pki.m_Color2 = mGray;
                }else{
                    if(curPrice==0||curPrice==stk_close){
                        pki.m_Color2 = mGray;
                    }else{
                        pki.m_Color2 = curPrice>stk_close?mRed:mGreen;
                    }
                }
                pki.m_Str3 = DealAmount(vol,true);
                pki.m_Color3=mGray2;
                if(forword.equals("B")){
                    pki.m_Str4 = forword;
                    pki.m_Color4=mRed;
                }else if(forword.equals("S")){
                    pki.m_Str4 = forword;
                    pki.m_Color4=mGreen;
                }else{
                    pki.m_Str4="";
                    pki.m_Color4=mGray;
                }
//                LogHelper.e(TAG,"new detail："+k+"|forword:"+forword);
                dataMingxi.add(pki);
                k++;
            }
    }
    private void requestFiveDay() {
        Map<String, String> params = new HashMap();
        try {
            params.put("PARAMS", "[{\"market\":\""+stkCode.substring(0,1)+"\",\"code\":\""+stkCode+"\"}]");
            params.put("FUNCTIONCODE","HQING018");

            LogHelper.i(TAG, ""+params.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        NetWorkUtil.getInstence().okHttpForGet(TAG+"detail", ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
//                    if(true){
//
//                        LogHelper.e(TAG, "stockDetailData 数据:" + response);
//                        return;
//                    }
                    JSONObject jsonObject = new JSONObject(response.substring(1, response.length() - 1));
                    if (!"0".equals(jsonObject.optString("code"))) {
                        LogHelper.e(TAG, "stockDetailData 数据异常:" + response);
                        return;
                    }
                    int totalCount = jsonObject.optInt("totalcount");
                    if(totalCount>0){
//                        String obj = jsonObject.optString("data");
//                        JSONArray dataArr = new JSONArray(obj);
                        JSONArray dataArr =jsonObject.optJSONArray("data");

                        analysisFiveDayData(dataArr,totalCount);
                        try {
//                            JSONArray dataArrOther = jsonObject.optJSONArray("data");
//                            m_fiveDayminData.m_fPreClosePrice = Float.parseFloat(dataArrOther.getString(4));
//                            LogHelper.e(TAG, "stockDetailData call Back m_fiveDayminData.m_fPreClosePrice:"+m_fiveDayminData.m_fPreClosePrice);
                        }catch (Exception eeee)
                        {
                            eeee.printStackTrace();
                        }
//                        mingxiAdapter.notifyDataSetChanged();
                    }
//                    LogHelper.e(TAG, "stockDetailData call Back totalCount:"+totalCount);

                }catch (Exception e){
                    e.printStackTrace();
                }
//                LogHelper.e(TAG, "stockDetailTradeData :"+response);
            }
        });
    }

    private String fiveDataTempDate="";
    private void analysisFiveDayData(JSONArray arry,int count) throws JSONException{
        JSONArray temp = null;
        fiveDataTempDate="";
//        m_fiveDayminData.DecimalNum = m_minData.DecimalNum;
        int len = arry.length();//arry.length();
//        byte [] b = new byte[len];
//        for (int i = 0;i<len;i++){
//            b[i] = (byte)arry.optInt(i);
//        }
//        ByteBuffer buf = ByteBuffer.wrap(b);
        int k = 0;
        int countT = Math.min(count,m_fiveDayminData.MAX_LEN);
        m_fiveDayminData.dayPosVec.removeAllElements();
//        double avgTemp = 0;
        int tempPos=0;
        long tempVol = 0;
        String ddd = "";
        try{
//            while(k<countT){
            for (int i=0;i<countT;i++){
                temp = arry.getJSONArray(i);
                k=i;
//                if("nil".equals(temp)){
//                    temp = arry.get(tempPos).toString();
//                }else{
//                    tempPos = i;
//                }
//                String[] tt = temp.split(",");
//                String tt =temp.
                MinPoint mp = new MinPoint();
                String d = temp.get(0).toString();//buf.getInt()+"";
                ddd =d;
//                int time = buf.getInt();
                String t = temp.get(1).toString();//time+"";
                double curPrice = Float.parseFloat(temp.get(2).toString());//buf.getDouble();

                long vol = Long.parseLong(temp.get(3).toString())-tempVol;//buf.getLong();
                tempVol = Long.parseLong(temp.get(3).toString());
                if(tempVol==0&&i!=0){
                    tempVol =  (long)m_fiveDayminData.minPoints[k-1].m_nVol;
                    vol = 0;
                }
//                int forword = buf.getInt();
                mp.m_nVol = vol;
                mp.m_fCurPrice=(float)curPrice;
                if(curPrice==0){
                    if(i!=0)
                    {
                        mp.m_fCurPrice = m_fiveDayminData.minPoints[k-1].m_fCurPrice;
                    }else{
                        mp.m_fCurPrice =Float.parseFloat(temp.get(5).toString());
                    }
                }
                mp.m_nDateNum =d.substring(4,6)+"-"+d.substring(6);
//
                mp.m_nMinuteNum = Integer.parseInt(t);
//
//                avgTemp +=curPrice;
                mp.m_fAvgPrice = Float.parseFloat(temp.get(4).toString());//(float)avgTemp/(k-tempPos+1);
                if(mp.m_fAvgPrice==0){
                    if(i!=0)
                    {
                        mp.m_fAvgPrice = m_fiveDayminData.minPoints[k-1].m_fAvgPrice;
                    }else{
                        mp.m_fAvgPrice =mp.m_fCurPrice;
                    }
                }
//
                m_fiveDayminData.minPoints[k] = mp;
                if(!d.equals(fiveDataTempDate)){
//                    tempPos=i;
                    mp.isJumpPoint = true;
                    mp.m_nVol = Long.parseLong(temp.get(3).toString());
//                    avgTemp = curPrice;
                    m_fiveDayminData.dayPosVec.add(k);
//                    LogHelper.e(TAG, mFormat1.format(k)+"stockDetailFiveDayData :"+d+" "+t);
                }
//                if(i>158){
//                    LogHelper.e(TAG, mFormat1.format(i)+":"+mFormat1.format(m_fiveDayminData.minPoints[i-1].m_nVol)+"|"+tempVol);
//                }
//
                fiveDataTempDate =d;
                if(i==0){
                    m_fiveDayminData.m_fPreClosePrice= Float.parseFloat(temp.get(5).toString());
                }

            }
        }catch (Exception e){
//            LogHelper.e(TAG, "数据解析异常"+mFormat1.format(k)+"stockDetailFiveDayData :"+temp+" ");
//            e.printStackTrace();
        }
//        LogHelper.e(TAG, mFormat1.format(k)+"************stockDetailFiveDayData :"+m_fiveDayminData.dayPosVec+"  count:"+countT);
        m_fiveDayminData.minPointsLen=countT;
        fiveDayMinChart.setData(m_fiveDayminData,mFormat1);
        if(isLandScreen&&landFiveDayMinChart!=null){
            landFiveDayMinChart.setData(m_fiveDayminData,mFormat1);
        }
    }

    private void requestKlineData() {
        if(kLine!=null){
            kLine.setData(null,mFormat1);
        }

        Map<String, String> params = new HashMap();
        try {
            params.put("PARAMS", "[{\"market\":\""+stkCode.substring(0,1)+"\",\"code\":\""+stkCode+"\",\"cycle\":\""+Kline_cycle+"\",\"start\":\""+Kline_start+"\",\"number\":\""+Kline_size+"\",\"xrdrType\":\""+Kline_xrdrType+"\"}]");
            params.put("FUNCTIONCODE","HQING010");

            LogHelper.e(TAG, ""+params.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        NetWorkUtil.getInstence().okHttpForGet(TAG+"kline",ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response.substring(1, response.length() - 1));
                    if (!"0".equals(jsonObject.optString("code"))) {
                        LogHelper.e(TAG, "stockDetailData 数据异常:" + response);
                        return;
                    }
//                    int totalCount = jsonObject.optInt("totalCount");
                    String cycle = jsonObject.optString("cycle");
                    String IssuePrice = jsonObject.optString("IssuePrice");
                    String IssueDate = jsonObject.optString("listdate");
//                    if(totalCount>0){
                        String obj = jsonObject.optString("data");
                        JSONArray dataArr = new JSONArray(obj);
                        analysisKlineData(dataArr);
                    if(kdata!=null)
                    {
                        if(!TextUtils.isEmpty(IssueDate))
                        {
                            kdata.listDate = IssueDate.replace("-","");
                        }
                        kdata.m_fIssuePrice = getStrFloat(IssuePrice);
                    }
//                        mingxiAdapter.notifyDataSetChanged();
//                    }
//                    LogHelper.e(TAG, "stockKlineData call Back totalCount:"+IssueDate+","+IssuePrice);

                }catch (Exception e){
                    e.printStackTrace();
                }
//                LogHelper.e(TAG, "stockDetailTradeData :"+response);
            }
        });


        if(isNeedRequsetHold){
            requestHoldStocks();
        }
    }
    private KData kdata = null;
    private void analysisKlineData(JSONArray arry) throws JSONException{
        if(kdata==null)
        {
            kdata = new KData();
        }

        int len = arry.length();

        byte [] b = new byte[len];
        for (int i = 0;i<len;i++){
            b[i] = (byte)arry.optInt(i);
        }
        ByteBuffer buf = ByteBuffer.wrap(b);
//        LogHelper.e(TAG, "stockDetailTradeData itemLen :"+len/(count+1));
        int count = len/36;
        if(Kline_cycle!=KLine.KLINE_CYCLE_DAY){
            count = len/40;
        }
        kdata.m_KPoints = new KPoints[count];
        kdata.kPointsLen = count;
        LogHelper.e(TAG, "stockDetailTradeData itemLen :"+count);
        int k = 0;
        while(k<count){
            KPoints pki = new KPoints();
//            String d = buf.getInt()+"";
            int date = buf.getInt();
            int date2 = 0;
            if(Kline_cycle!= KLine.KLINE_CYCLE_DAY){
                date2 = buf.getInt();
            }
            String t = date+"";

            float openPrice = buf.getFloat();
            float highPrice = buf.getFloat();
            float lowPrice = buf.getFloat();
            float closePrice = buf.getFloat();
            double amount = buf.getDouble();
            long vol = buf.getLong();
            if(t.length() == 5)t = "0"+t;

            pki.m_fCurPrice = closePrice;
            pki.m_fHighPrice = highPrice;
            pki.m_fLowPrice = lowPrice;
            pki.m_fOpenPrice = openPrice;
            pki.m_dAmount = kxjsf(amount);
            pki.m_lVol = vol;
            if(Kline_cycle!=KLine.KLINE_CYCLE_DAY){
                pki.m_nDate = date2;
                pki.m_nDate2 = date;
            }else{
                pki.m_nDate = date;
            }

            kdata.m_KPoints[k] = pki;

            pki.m_nM5Price = MAKline(kdata,pki,5,k);
            pki.m_nM10Price = MAKline(kdata,pki,10,k);
            pki.m_nM20Price = MAKline(kdata,pki,20,k);

//            if(k>count-20)
//            {
//                LogHelper.e(TAG, k+":stockDetail KlineData :"+t+","+mFormat1.format(pki.m_nM5Price)+"||"+mFormat1.format(openPrice)+","+mFormat1.format(highPrice)
//                    +","+mFormat1.format(lowPrice)+","+mFormat1.format(closePrice)+","+DealAmount(amount,true)+","+vol);
//            }
            k++;
        }
        kLine.setHoldPrice(m_strHoldPrice);
        kLine.setData(kdata,mFormat1);
        if(isLandScreen&&landKline!=null){
            landKline.setHoldPrice(m_strHoldPrice);
            landKline.setData(kdata,mFormat1);
        }
    }

    private float MAKline(KData kdata, KPoints points,int cycleMA,int pos){
        float result = 0;
        int pointLen = kdata.kPointsLen;
        if(pos>pointLen-1){
            return 0;
        }
        int tempPos = pos+1-cycleMA;
        if(tempPos<0){
            return 0;
        }
        double tempD = 0;
        for (int i=0;i<cycleMA;i++){
            tempD += kdata.m_KPoints[tempPos+i].m_fCurPrice;
        }
        result = (float)tempD/cycleMA;
        return result;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
        //land
//            LogHelper.e(TAG, "Land:"+Configuration.ORIENTATION_LANDSCAPE);
            //去掉Activity上面的状态栏
            getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                    WindowManager.LayoutParams. FLAG_FULLSCREEN);
            land_AllWholeLayout.setVisibility(View.VISIBLE);
            port_WholeLayout.setVisibility(View.GONE);
            isLandScreen = true;
            setLandData();
            minChartLand.setData(m_minData,mFormat1);
            if(stkType!=0){
                if(isShowMingxi){
                    land_pankouItemListView.setAdapter(mingxiAdapter);
                }else{
                    land_pankouItemListView.setAdapter(pankouAdapter);
                }
            }
            intLandTabLine();
            setLandIndexFocus();
            setLandChartLayout();
            initLandItemDetailTvs();


        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
        //port
//            LogHelper.e(TAG, "Port:"+Configuration.ORIENTATION_PORTRAIT);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            isLandScreen = false;
            minChart.setData(m_minData,mFormat1);
            land_AllWholeLayout.setVisibility(View.GONE);
            port_WholeLayout.setVisibility(View.VISIBLE);
            if(stkType!=0){
                land_pankouItemListView.setAdapter(null);
             }
        }
    }

    private void changeScreenOriental(){
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
    private void setLandData(){
        mLandRangTv.setText(landRangStr);
        if(landRangStr.indexOf("+")!=-1){
            mLandRangTv.setTextColor(mRed);
        }else if(landRangStr.indexOf("-")!=-1){
            mLandRangTv.setTextColor(mGreen);
        }else{
            mLandRangTv.setTextColor(mGray);
        }

        mLandVolTv.setText(landVolStr);
        try{
            int tempPos =wholeTime.length()-6;
            if(tempPos<0)return;
            mLandTimeTv.setText(wholeTime.substring(tempPos,tempPos+2)+":"+wholeTime.substring(tempPos+2,tempPos+4));
        }catch (Exception e){}

        switch (mCurrentIndex){
            case 0:

                break;
        }
    }
    @Override
    public void OnAction(int type, Object arg) {
        switch (type){
            case 0:
                changeScreenOriental();
                break;
            case 1://分时详细
                boolean arg1 = (Boolean)arg;
                if(arg1){
                    if(landItemDetailLayout.getVisibility()!=View.VISIBLE)
                    {
                        landItemDetailLayout.setVisibility(View.VISIBLE);
                    }
                }else{
                    landItemDetailLayout.setVisibility(View.GONE);
                }
                break;
            case 10://滚动修改标题
                boolean arg2 = (Boolean)arg;

                if(arg2!=isModifyTitle){
                    if(arg2){
                        mTitleNumber.setText(titleContent2);
                    }else{
                        mTitleNumber.setText(titleContent1);
                    }

                }
                isModifyTitle  = arg2;

                break;
        }
    }
    private boolean isModifyTitle = false;

    private void setLandTitle(){
        mLandTitleTv.setText(stk_titleStr);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isLandScreen&&keyCode == KeyEvent.KEYCODE_BACK){
            changeScreenOriental();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initLandItemDetailTvs(){
        if(landItemDetailValueTvs==null){
            landItemDetailValueTvs = new TextView[7];
            landItemDetailValueTvs[0] = (TextView)findViewById(R.id.land_itemtimeTv);
            landItemDetailValueTvs[1] = (TextView)findViewById(R.id.land_itemValueTv1);
            landItemDetailValueTvs[2] = (TextView)findViewById(R.id.land_itemValueTv2);
            landItemDetailValueTvs[3] = (TextView)findViewById(R.id.land_itemValueTv3);
            landItemDetailValueTvs[4] = (TextView)findViewById(R.id.land_itemValueTv4);
            landItemDetailValueTvs[5] = (TextView)findViewById(R.id.land_itemValueTv5);
            landItemDetailValueTvs[6] = (TextView)findViewById(R.id.land_itemValueTv6);
        }
        if(landItemDetailTitleTvs==null){
            landItemDetailTitleTvs = new TextView[6];
            landItemDetailTitleTvs[0] = (TextView)findViewById(R.id.land_itemTitleTv1);
            landItemDetailTitleTvs[1] = (TextView)findViewById(R.id.land_itemTitleTv2);
            landItemDetailTitleTvs[2] = (TextView)findViewById(R.id.land_itemTitleTv3);
            landItemDetailTitleTvs[3] = (TextView)findViewById(R.id.land_itemTitleTv4);
            landItemDetailTitleTvs[4] = (TextView)findViewById(R.id.land_itemTitleTv5);
            landItemDetailTitleTvs[5] = (TextView)findViewById(R.id.land_itemTitleTv6);
        }

        if(mCurrentIndex<2){//两个分时
            String [] titleStrs = {"价 ","均 ","幅 ","量 "};
            for(int i=0;i<titleStrs.length;i++){
                landItemDetailTitleTvs[i].setText(titleStrs[i]);
            }
        }else{//k线 详细 标题多2个
            String [] titleStrs = {"开 ","收 ","低 ","高 ","幅 ","量 "};
            for(int i=0;i<titleStrs.length;i++){
                landItemDetailTitleTvs[i].setText(titleStrs[i]);
            }
            initLandKlineToolsArea();
        }

    }
    private ListView land_ToolsItemListView;
    private LandKlineToolsAdapter toolsAdapter;
    private TextView fuquanView1,fuquanView2,fuquanView3;
    private void initLandKlineToolsArea(){
        if(land_ToolsItemListView==null){
            land_ToolsItemListView = (ListView)findViewById(R.id.land_ToolsItemListView);
            toolsAdapter = new LandKlineToolsAdapter(this);
            ArrayList<String> dataT = new ArrayList<>(6);
            dataT.add("成交量");
            dataT.add("RSI");
            dataT.add("KDJ");
            dataT.add("MACD");
            dataT.add("WR");
            dataT.add("VR");
            toolsAdapter.setData(dataT);
            land_ToolsItemListView.setOnItemClickListener(KlineIndex);
            land_ToolsItemListView.setAdapter(toolsAdapter);
        }
        fuquanView1 = (TextView)findViewById(R.id.fuquan_noTv);
        fuquanView2=(TextView)findViewById(R.id.fuquan_beforeTv);
        fuquanView3=(TextView)findViewById(R.id.fuquan_afterTv);
        fuquanView1.setOnClickListener(this);
        fuquanView2.setOnClickListener(this);
        fuquanView3.setOnClickListener(this);
        fuquanView1.setTag(0);
        fuquanView2.setTag(1);
        fuquanView3.setTag(2);
        changeXrdrType();
//        land_ToolsItemListView.setAdapter();

    }

    private void changeXrdrType(){
        switch (Kline_xrdrType){
            case 0:
            fuquanView1.setTextColor(mBlue);
            fuquanView2.setTextColor(mGray);
            fuquanView3.setTextColor(mGray);
                break;
            case 1:
                fuquanView2.setTextColor(mBlue);
                fuquanView1.setTextColor(mGray);
                fuquanView3.setTextColor(mGray);
                break;
            case 2:
                fuquanView3.setTextColor(mBlue);
                fuquanView2.setTextColor(mGray);
                fuquanView1.setTextColor(mGray);

                break;
        }
    }

    private AdapterView.OnItemClickListener KlineIndex = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            LogHelper.e(TAG,"OnItemClickListener position:"+position);
            if(toolsAdapter!=null){
                toolsAdapter.setFocusIndex(position);
                toolsAdapter.notifyDataSetChanged();
                landKline.setIndexType(position);
            }
        }
    };
    /**
     * 分享
     */
    private void getShare() {
        String capitalAccount = UserUtil.capitalAccount;                        //注册账户
        String base64 = ScreenShot.shoot( this);         //截屏 拿到图片的 base64
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("base64", base64);
        map2.put("account", capitalAccount);
        map2.put("type", "3");
        map2.put("phone_type","2");
        map1.put("FUNCTIONCODE", "HQFNG001");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString("MarketFragment", ConstantUtil.getURL_HQ_BB(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("shareUpLoad:", e.toString());
                if (loadingDialog!=null&&!isFinishing()){
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.i("shareUpLoad", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        String url = jsonObject.optString("msg");
                        if (!isFinishing()){
                            loadingDialog.dismiss();
                        }
                        shareDialog.setUrl(url);
                        if (!StockDetailActivity.this.isFinishing()){
                            shareDialog.show();
                        }
                    }else{
                        if (loadingDialog != null&&!isFinishing()) {
                            loadingDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private float getStrFloat(String value){
        float ret = 0;
        try{
            if(!"".equals(value) && null != value){
                ret = Float.parseFloat(value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  ret;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        LogHelper.e(TAG,"onNewIntent");
        setContentView(getLayoutId());
        minDataLastTime = "093000";
        initView(intent);

//        m_minData = null;
//        minChart.setData(null,mFormat1);
    }
    //为了画持仓线，增加获取持仓数据方法。
    private void requestHoldStocks(){
        HashMap map300130 = new HashMap();
        map300130.put("funcid", "300200");
        map300130.put("token", SpUtils.getString(this, "mSession", null));
        HashMap map300130_1 = new HashMap();
        map300130_1.put("FLAG", true);
        map300130_1.put("SEC_ID", "tpyzq");
        map300130_1.put("KEY_STR", "");
//        map300130_1.put("REC_COUNT", 2);
        map300130.put("parms", map300130_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300130, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(String response, int id) {
//                LogHelper.e(TAG,TAG+"getHoldData:"+response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    isNeedRequsetHold = false;
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if (code.equals("0")) {
                        JSONArray jsonArray = new JSONArray(data);
                        AddPosition.getInstance().setData(TAG,jsonArray);
                        HashMap mp = AddPosition.getInstance().getData(stkCode);
                        if(mp!=null){
                            m_strHoldPrice = (String) mp.get("MKT_PRICE");
                            if(kLine!=null)
                            {
                                kLine.setHoldPrice(m_strHoldPrice);
                                kLine.repaint();
                            }
                            if(isLandScreen&&landKline!=null){
                                landKline.setHoldPrice(m_strHoldPrice);
                                landKline.repaint();
                            }
                        }else{
                            m_strHoldPrice= "0";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}