package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IListViewScrollObserver;
import com.tpyzq.mobile.pangu.interfac.IListViewScrollSubject;
import com.tpyzq.mobile.pangu.interfac.ListViewScroller;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/6/30.
 * 各种股票的列表
 * 该页面需要getIntent返回值
 * <p/>
 * 返回值有
 * <p/>
 * filedNames  当前listview页面每个Item显示的属性   数据类型为String[]
 * compare1    所要比较的 filedNames 数组中的某项   数据类型int
 * compare2    所要比较的 filedNames 数组中的某项   数据类型int
 * title       头部的title
 * head1       悬浮框1
 * head2       悬浮框2
 * head3       悬浮框3
 * getViewId   加载哪一个布局
 */
public class StockListActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, ListViewScroller.ListViewScrollerListener,
        IListViewScrollSubject {
    private static final int mNum = 30;
    private TextView mHeadTv2;
    private TextView mHeadTv3;
    private ListView mListView;
    private StockListAdapter  mAdapter2;
    private boolean rightFlag = true;

    private static final String TAG = "StockListActivity";

    private String mOrder;                  //1 -->倒序 其他为正序
    private String mType;                   //1-->沪深A  2 -->沪深B  3 -->沪深基金  4 -->沪深债券   5 -->中小板  6 -->创业板   7 --> 深证A股  8 -->上证A股    9 -->指数
    private String mAsc;                    //1 -->倒序 其他为正序
    private String mFlag;                   //1-->行情   2-->成交金额  3-->换手率   4 -->股票代码
    private String mStartNumber;            //开始记录数
    private String mMarket;                 //市场
    private String mCode;                   //行业代码
    private String switchIndx;
    private String exponentCode;

    private boolean isIntOut;               //是否是资金流入流出板块列表
    private boolean isPlateList;            //是否是更多Tab页面，沪深Tab页的领涨股， 领跌股， 换手率
    private boolean isIndustryStockList;    //是否是行业下股票列表
    private boolean isRiskBackMarket;       //是否是风险退市
    private boolean isExponent;

    private boolean doSelectMiddle;         //是否第一次加载list列表
    private ArrayList<IListViewScrollObserver> mObservers;
//    public static ArrayList<StockListTimeObsever> mObserves = new ArrayList<>();
    private int mNetTotalCount;
    private ProgressBar mLoadDataProgress;
    private FrameLayout mEmptyLayout;
    private String mFromStockListTag;
    private Intent mServiceIntent;
    private boolean mJudegeGesture;
    private ArrayList<StockInfoEntity> mBeans;

    @Override
    public void initView() {
        LogHelper.e(TAG,"initView");
        Intent intent = getIntent();
        StockListIntent stockListIntent = intent.getParcelableExtra("stockIntent");
        if (stockListIntent == null) {
            return;
        }

        mEmptyLayout = (FrameLayout) findViewById(R.id.fl_stocklistLayout);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyLayout.setOnClickListener(this);

        mLoadDataProgress = (ProgressBar) findViewById(R.id.stocklist_progress);
        mLoadDataProgress.setVisibility(View.VISIBLE);
        mObservers = new ArrayList<>();

        mOrder = "1";
        mStartNumber = "0";

        String _order = intent.getStringExtra("order");
        if (!TextUtils.isEmpty(_order)) {
            mOrder = _order;
        }

        mFromStockListTag = intent.getStringExtra("tag");
        mType  = intent.getStringExtra("type");
        mAsc = intent.getStringExtra("asc");
        mFlag = intent.getStringExtra("flag");
        mMarket = intent.getStringExtra("market");
        isIntOut = intent.getBooleanExtra("isInOut", false);
        mCode = intent.getStringExtra("code");
        isPlateList = intent.getBooleanExtra("isPlateList", false);
        isIndustryStockList = intent.getBooleanExtra("isIndustryStockList", false);
        isRiskBackMarket = intent.getBooleanExtra("isRiskBackMarket", false);
        isExponent = intent.getBooleanExtra("isExponent", false);
        exponentCode = intent.getStringExtra("exponentCode");

        LogHelper.e(TAG,"mType:"+mType);
        if (TextUtils.isEmpty(mType)) {
            mType = "-1";
        }

        if (TextUtils.isEmpty(mAsc)) {
            mAsc = "1";
        }

        if (TextUtils.isEmpty(mFlag)) {
            mFlag = "-1";
        }

        if (TextUtils.isEmpty(mMarket)) {
            mMarket = "2";
        }

        if (TextUtils.isEmpty(mCode)) {
            mCode = "0";
        }


        findViewById(R.id.stocklist_back).setOnClickListener(this);
        findViewById(R.id.stocklist_search).setOnClickListener(this);

        setHeadTv(stockListIntent);

        if (isIntOut) {
            settingDrawble("1");
        } else if (isPlateList) {
            settingDrawble("2");
        } else if (isIndustryStockList) {
            settingDrawble("2");
        } else if (isRiskBackMarket) {
            settingDrawble("2");
        } else if (isExponent) {
            settingDrawble("2");
        }

        mListView = (ListView) findViewById(R.id.stockListView);
        mAdapter2 = new StockListAdapter();

        mListView.setAdapter(mAdapter2);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new ListViewScroller(TAG, mListView, mStartNumber, StockListActivity.this, StockListActivity.this));


    }

//    private Message sendMessage(String[] keys, String[] values) {
//        Message msg = new Message();
//        Bundle bundle = new Bundle();
//        for (int i = 0; i < keys.length; i++) {
//            bundle.putString(keys[i], values[i]);
//        }
//        msg.setData(bundle);
//        return msg;
//    }

    @Override
    protected void onResume() {
        super.onResume();
//
//        if (!TextUtils.isEmpty(mOrder) && !TextUtils.isEmpty(mType)) {
//            reStartConnect();
//        }
        if (!TextUtils.isEmpty(mOrder) && !TextUtils.isEmpty(mType)) {
            timerConnect();
            startTimerTask();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimerTask();
    }

    private void settingDrawble(String flag) {
        Drawable drawable = null;

        if (flag.equals(mOrder)) {

            drawable = ContextCompat.getDrawable(this, R.mipmap.sort_n);
            rightFlag = true;
        } else {
            drawable = ContextCompat.getDrawable(this, R.mipmap.sort_p);
            rightFlag = false;
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mHeadTv3.setCompoundDrawables(null, null, drawable, null);//画在右边
    }

    @Override
    public void registerObserver(IListViewScrollObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void removeObserver(IListViewScrollObserver observer) {
        int num = mObservers.indexOf(observer);
        if (num >= 0) {
            mObservers.remove(observer);
        }

    }

    @Override
    public void notifyObservers() {

        for (IListViewScrollObserver observer : mObservers) {
            observer.update(mNetTotalCount);
        }
    }

    @Override
    public void nextPage(String startNumber) {
        mStartNumber = startNumber;
        mLoadDataProgress.setVisibility(View.VISIBLE);
//        reStartConnect();
        startTimerTask();
        request();
    }

    @Override
    public void lastPage(String startNumber) {
        mStartNumber = startNumber;
        mLoadDataProgress.setVisibility(View.VISIBLE);
//        reStartConnect();
        startTimerTask();
        request();
    }

    @Override
    public void juedgeGesture(boolean isUp) {
        mJudegeGesture = isUp;
    }

    @Override
    public void connectBySrollStop() {
    }

    private void timerConnect() {

//        mServiceIntent = new Intent(StockListActivity.this, StockListService.class);
//
//        if (isIntOut) {
//            String [] keys = {"startNo", "order", "type", "switchIndx"};
//            String [] values = {mStartNumber, mOrder, mType, "2"};
//            StockListService.handler.sendMessage(sendMessage(keys, values));
//        } else if (isPlateList) {
//            String [] keys = {"order", "type", "flag", "startNo", "switchIndx"};
//            String [] values = {mOrder, mType, mFlag, mStartNumber, "1"};
//            StockListService.handler.sendMessage(sendMessage(keys, values));
//        } else if (isIndustryStockList) {
//            String [] keys = {"market", "type", "code", "asc", "startNo", "switchIndx"};
//            String [] values = {mMarket, mType, mCode, mAsc, mStartNumber, "3"};
//            StockListService.handler.sendMessage(sendMessage(keys, values));
//        } else if (isRiskBackMarket) {
//            String [] keys = {"order", "type", "startNo", "switchIndx"};
//            String [] values = {mOrder, mType, mStartNumber, "4"};
//            StockListService.handler.sendMessage(sendMessage(keys, values));
//        }
//
//        StockListService.mOberservers.add(this);
//        notifyObservers(3 * 1000);
//        StockListActivity.this.startService(mServiceIntent);
        LogHelper.e(TAG,"isIntOut:"+isIntOut+"  isPlateList:"+isPlateList+"  isIndustryStockList:"+isIndustryStockList+"  isRiskBackMarket:"+isRiskBackMarket);
        request();
//        if(isIndustryStockList){
//            requestIndustryStockList();
//        }
    }

    public void request(){
        loadingHandler.sendEmptyMessage(0);
        isCallBackSuccess = false;
        if (isIntOut) {
            requestMoneyInOutStockList();
        }else if(isPlateList){
            requestPlateStockList();
        }else if (isIndustryStockList) {
            requestIndustryStockList();
        }else if (isRiskBackMarket) {
            requestRiskBackStockList();
        } else if (isExponent) {
            requestExponentStockList();
        }
    }
    Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLoadDataProgress.setVisibility(View.VISIBLE);
        }
    };



    @Override
    public void doSelectMiddle(boolean doSelectMiddle) {
        this.doSelectMiddle = doSelectMiddle;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StockDetailEntity stockDetailEntity = new StockDetailEntity();
        stockDetailEntity.setStockName(mBeans.get(position).getStockName());
        stockDetailEntity.setStockCode(mBeans.get(position).getStockNumber());
        Intent intent = new Intent();
        intent.putExtra("stockIntent", stockDetailEntity);
        intent.setClass(StockListActivity.this, StockDetailActivity.class);
        startActivity(intent);
    }

    /**
     * 初始化头部信息
     *
     * @param bean
     */
    private void setHeadTv(StockListIntent bean) {
        TextView titleTv = (TextView) findViewById(R.id.stocklist_title);
        TextView headTv1 = (TextView) findViewById(R.id.stocklist_headTv1);
        mHeadTv2 = (TextView) findViewById(R.id.stocklist_headTv2);
        mHeadTv3 = (TextView) findViewById(R.id.stocklist_headTv3);

        mHeadTv3.setOnClickListener(this);

        String title = bean.getTitle();
        String head1 = bean.getHead1();
        String head2 = bean.getHead2();
        String head3 = bean.getHead3();

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }

        if (!TextUtils.isEmpty(head1)) {
            headTv1.setText(head1);
            headTv1.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
        }

        if (!TextUtils.isEmpty(head2)) {
            mHeadTv2.setText(head2);
            mHeadTv2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
        }

        if (!TextUtils.isEmpty(head3)) {
            mHeadTv3.setText(head3);
            mHeadTv3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stocklist_back:
                stopTimerTask();
                finish();
                break;
            case R.id.stocklist_search:
                Intent intent = new Intent();
                intent.setClass(StockListActivity.this, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.stocklist_headTv3:

                mLoadDataProgress.setVisibility(View.VISIBLE);
                mHeadTv3.setClickable(false);
                mHeadTv3.setFocusable(false);
                mStartNumber = "0";
                if (rightFlag) {
                    mOrder = "1";
                    mAsc = "1";

                    if (isIntOut) {
                        mOrder = "2";

                        request();

                    } else if (isPlateList) {

                        request();

                    } else if (isIndustryStockList) {

                        request();

                    } else if (isRiskBackMarket) {
                        mOrder = "1";
                        request();
                    } else if (isExponent) {
                        request();
                    }

                    Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_p);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    mHeadTv3.setCompoundDrawables(null, null, drawable, null);//画在右边
                    rightFlag = false;
                } else {
                    mOrder = "2";
                    mAsc = "0";
                    if (isIntOut) {
                        mOrder = "1";
                        request();

                    } else if (isPlateList){
                        request();

                    } else if (isIndustryStockList) {
                        request();

                    } else if (isRiskBackMarket) {
                        mOrder = "0";
                        request();
                    } else if (isExponent) {
                        request();
                    }

                    Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_n);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    mHeadTv3.setCompoundDrawables(null, null, drawable, null);//画在右边
                    rightFlag = true;
                }
                break;
            case R.id.fl_stocklistLayout:
                request();
                break;
        }
    }


    private String responseCode = "";
    public void fillArrayList(ArrayList<StockInfoEntity> entities) {
        mEmptyLayout.setVisibility(View.GONE);
        mLoadDataProgress.setVisibility(View.GONE);
        mHeadTv3.setClickable(true);
        mHeadTv3.setFocusable(true);

        if (entities == null || entities.size() <= 0 ) {
            if (mBeans == null || mBeans.size() <= 0) {
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
//            notifyObservers(60 * 1000);
            return ;
        }

        if (responseCode.equals("-5")) {
            Helper.getInstance().showToast(StockListActivity.this, "Code:-5");
            return;
        }

        mBeans = entities;

//        if (!TextUtils.isEmpty(mBeans.get(0).getTotalCount())) {
            mNetTotalCount = mBeans.size();//Integer.parseInt(mBeans.get(0).getTotalCount());
//        }

        notifyObservers();

        mAdapter2.setData(mBeans, mFromStockListTag);
        mLoadDataProgress.setVisibility(View.GONE);

        if (doSelectMiddle) {
            int index = 0;
            if (mJudegeGesture) {
                index = 10;
            } else {
                index = 20-(mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition() + 1);
            }
//            LogHelper.e(TAG,"mJudegeGesture:"+mJudegeGesture+"  index:"+index);
            View v = mListView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            mListView.setSelectionFromTop(index, top);
//            LogHelper.e(TAG,"index:"+index+",top："+top);
        }

        String time = mBeans.get(0).getTime();

//        if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
//            int intTIME = Integer.valueOf(time);
//            notifyObservers(intTIME * 1000);
//        }
    }
    public void requestPlateStockList() {
        Map<String, String> params = new HashMap();
        try {
            Object []  object = new Object[1];
            Map map2 = new HashMap();
            map2.put("type",mType);
            map2.put("flag",mFlag);
            map2.put("asc",mAsc);
            map2.put("startNo", mStartNumber);
            map2.put("number", "30");
            map2.put("exponential", "-1");
            params.put("FUNCTIONCODE","HQING001");
            object[0] = map2;
            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                fillArrayList(beans);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                LogHelper.e(TAG, "requestPlateStockList response:"+response);
//[{"time":"0","bytes":28,"totalCount":"30","data":[["20399238","2349.386","2388.704","餐饮指数","0.0","1.85776896E9","0.016735590994358063"],["10000006","5986.009","6081.110","地产指数","0.0","5.6098939584E9","0.01588730700314045"],["20399910","2410.205","2445.606","300 工业","0.0","3.3205342208E10","0.014687911607325077"],["10000910","2410.205","2445.606","300工业 ","0.0","3.32053414473E10","0.014687810093164444"],["10000072","2924.272","2963.213","工业等权","0.0","2.8672544289E10","0.013316279277205467"],["20399419","2305.176","2335.333","国证高铁","0.0","2.2682570752E10","0.013082398101687431"],["10000043","1986.151","2011.246","超大盘  ","0.0","1.947556366E10","0.012634974904358387"],["10000007","5730.938","5795.842","公用指数","0.0","2.12969312911E10","0.011325248517096043"],["10000050","2186.765","2211.240","50等权  ","0.0","3.114459546E10","0.011191927827894688"],["20399357","2882.834","2914.739","环渤海","0.0","2.7324751872E10","0.011066904291510582"],["10000052","2456.656","2483.812","50基本  ","0.0","3.114459546E10","0.011053953319787979"],["10000152","1747.373","1765.413","上央红利","0.0","1.2667086962E10","0.010324301198124886"],["20399925","3356.589","3390.684","基本面50","0.0","3.3083693056E10","0.010157696902751923"],["10000925","3356.589","3390.684","基本面50","0.0","3.30836921937E10","0.010157696902751923"],["10000042","1366.210","1380.012","上证央企","0.0","3.2526056731E10","0.010102578438818455"],["20399901","4663.482","4705.823","小康指数","0.0","4.046440448E10","0.009079333394765854"],["10000901","4663.482","4705.823","小康指数","0.0","4.0464402329E10","0.009079333394765854"],["20399369","1219.515","1230.094","CBN-兴全","0.0","3.5413282816E10","0.008674741722643375"],["10000053","9326.411","9407.047","180基本 ","0.0","6.685314243E10","0.00864595640450716"],["10000959","5382.088","5427.502","银河99  ","0.0","5.17880226757E10","0.008438091725111008"],["10000095","2812.863","2835.954","上证中游","0.0","5.4158970727E10","0.00820900872349739"],["10000098","3842.674","3873.554","上证F200","0.0","6.2707660303E10","0.008035976439714432"],["10000149","3088.968","3113.578","180红利 ","0.0","1.1853225492E10","0.00796709768474102"],["10000123","6855.396","6909.097","180动态 ","0.0","4.3209359286E10","0.007833416573703289"],["10000016","2223.424","2240.750","上证50  ","0.0","3.114459546E10","0.007792122662067413"],["20399383","2726.025","2747.169","1000工业","0.0","6.2336356352E10","0.007756453938782215"],["10000021","824.912","831.043","180治理 ","0.0","4.3100024538E10","0.007432654500007629"],["20399240","1178.732","1187.480","金融指数","0.0","8.467305472E9","0.007421365939080715"],["10000151","1091.026","1098.932","上国红利","0.0","7.108457217E9","0.007247069384902716"],["10000064","2409.645","2426.968","非周期  ","0.0","4.2490355677E10","0.00718892365694046"]],"code":"0","ceshi":"挂","isOpen":"false"}]
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                try {
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillArrayList(beanss);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);

                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("time");
                    refleshTime = TransitionUtils.string2int(time);
                    if (code.equals("-5") || code.equals("-1") || code.equals("-3")) {
                        code = "-5";
                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                        stockInfoEntity.setCode(code);
                        beanss.add(stockInfoEntity);
                    } else {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            StockInfoEntity _bean = new StockInfoEntity();
                            JSONArray obj =jsonArray.optJSONArray(j);
                            _bean.setStockNumber(obj.optString(0));
                            _bean.setClose(obj.optString(1));
                            _bean.setNewPrice(obj.optString(2));
                            _bean.setStockName(obj.optString(3));
                            _bean.setTurnover(obj.optString(4));
                            Helper.getZdfAndzdz(_bean,_bean.getClose(),_bean.getNewPrice());
                            beanss.add(_bean);
                        }
                    }
                    fillArrayList(beanss);
                } catch (Exception e) {
                    e.printStackTrace();
                    fillArrayList(beans);
                }
            }
        });
    }
    public void requestIndustryStockList() {
        Map<String, String> params = new HashMap();
        try {
            Object []  object = new Object[1];

            Map map2 = new HashMap();
            map2.put("market", mMarket);
            map2.put("type",mType);
            map2.put("code", mCode);
            map2.put("asc", mAsc);
            map2.put("startNo",mStartNumber);
            map2.put("number", "30");
            params.put("FUNCTIONCODE","HQING002");
            object[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogHelper.e(TAG, "requestIndustryStockList request:"+params.toString());
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                fillArrayList(beans);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
                LogHelper.e(TAG, "requestIndustryStockList response:"+response);
                try {
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillArrayList(beanss);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);

                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("time");
                    refleshTime = TransitionUtils.string2int(time);
                    if (code.equals("-5") || code.equals("-1") || code.equals("-3")) {
                        code = "-5";
                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                        stockInfoEntity.setCode(code);
                        beanss.add(stockInfoEntity);
                    } else {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            StockInfoEntity _bean = new StockInfoEntity();
                            JSONArray obj =jsonArray.optJSONArray(j);
                            _bean.setStockNumber(obj.optString(0));
                            _bean.setClose(obj.optString(1));
                            _bean.setTodayOpenPrice(obj.optString(2));
                            _bean.setNewPrice(obj.optString(3));
                            _bean.setHigh(obj.optString(4));
                            _bean.setLow(obj.optString(5));
                            _bean.setTotalVolume(obj.optString(6));
                            _bean.setTotalPrice(obj.optString(7));
                            _bean.setStockName(obj.optString(8));
                            Helper.getZdfAndzdz(_bean,_bean.getClose(),_bean.getNewPrice());
                            beanss.add(_bean);
                        }
                    }
                    fillArrayList(beanss);
                } catch (Exception e) {
                    e.printStackTrace();
                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                    fillArrayList(beans);
                }
            }
        });
    }
    public void requestMoneyInOutStockList() {
        Map<String, String> params = new HashMap();
        try {
            Object []  object = new Object[1];
            params.put("FUNCTIONCODE","HQING008");

            Map map2 = new HashMap();
            map2.put("inout",mType);
            map2.put("order",mOrder);
            map2.put("startNo",mStartNumber);
            map2.put("number","30");

            object[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                fillArrayList(beans);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                LogHelper.e(TAG,"requestMoneyInOutStockList response:"+ response);
                try {
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillArrayList(beanss);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);

                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("time");
                    refleshTime = TransitionUtils.string2int(time);
                    if (code.equals("-5") || code.equals("-1") || code.equals("-3")) {
                        code = "-5";
                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                        stockInfoEntity.setCode(code);
                        beanss.add(stockInfoEntity);
                    } else {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            StockInfoEntity _bean = new StockInfoEntity();
                            JSONArray obj =jsonArray.optJSONArray(j);
                            _bean.setStockNumber(obj.optString(0));
                            _bean.setStockName(obj.optString(1));
                            _bean.setInflowMoney(obj.optString(2));
                            _bean.setOutflowMoney(obj.optString(3));
                            _bean.setClose(obj.optString(4));
                            _bean.setNewPrice(obj.optString(5));
                            _bean.setPriceToal(obj.optString(6));
                            _bean.setTurnover(obj.optString(6));
                            Helper.getZdfAndzdz(_bean,_bean.getClose(),_bean.getNewPrice());
                            beanss.add(_bean);
                        }
                    }
                    fillArrayList(beanss);
                } catch (Exception e) {
                    e.printStackTrace();
                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                    fillArrayList(beans);
                }


            }
        });
    }
    public void requestRiskBackStockList(){
        Map<String ,String> params = new HashMap<>();
        try {
            Object []  object = new Object[1];
            Map map2 = new HashMap();
            map2.put("type",mType);
            map2.put("start",mStartNumber);
            map2.put("num", mNum);
            map2.put("order", mOrder);
            params.put("FUNCTIONCODE","HQING013");
            object[0] = map2;
            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogHelper.e(TAG, "request:"+params.toString());
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                fillArrayList(beans);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
                try {
                    LogHelper.e(TAG, "response:"+response);
                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillArrayList(beans);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);
                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("time");
                    if(TextUtils.isEmpty(time)){
                        refleshTime = -1;
                    }else{
                        refleshTime = TransitionUtils.string2int(time);
                    }
                    String totalCount = jsonObject.optString("totalCount");
                    JSONArray array1 = jsonObject.optJSONArray("data");
                        if (null != array1 &&array1.length() > 0) {
                            for (int i = 0; i < array1.length(); i++) {
                                JSONArray itemArr = array1.optJSONArray(i);
                                StockInfoEntity _bean = new StockInfoEntity();
                                _bean.setCode(code);
                                _bean.setTotalCount(totalCount);

//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(0))) {
                                    _bean.setStockNumber(itemArr.optString(0));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(1))) {
                                    _bean.setClose(itemArr.optString(1));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(2))) {
//                                    _bean.setTodayOpenPrice(itemArr.optString(2));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(3))) {
                                    _bean.setNewPrice(itemArr.optString(3));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(4))) {
//                                    _bean.setHigh(itemArr.optString(4));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(5))) {
//                                    _bean.setLow(itemArr.optString(5));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(6))) {
//                                    _bean.setTotalVolume(itemArr.optString(6));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(7))) {
                                    _bean.setTotalPrice(itemArr.optString(7));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(8))) {
                                    _bean.setStockName(itemArr.optString(8));
//                                }
//
//                                if (!TextUtils.isEmpty(bean.getData().get(i).get(9))) {
                                    _bean.setPriceChangeRatio(TransitionUtils.string2double(itemArr.optString(9)));
//                                }
                                beans.add(_bean);
                            }
                        }
                    fillArrayList(beans);
                } catch (Exception e) {
                    e.printStackTrace();
                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                    fillArrayList(beans);
                }
            }
        });
    }

    public void requestExponentStockList() {
        Map<String, String> params = new HashMap();

        try {
            Object []  object = new Object[1];

            Map map2 = new HashMap();
            map2.put("flag", mFlag);//1行情2换手率
            map2.put("asc", mAsc);//1为涨 其他为跌
            map2.put("startNo", mStartNumber);//起始的记录数，从0开始
            map2.put("number", mNum);//返回的记录数 -1从起始数到最后
            map2.put("exponential", exponentCode);
            object[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);

            params.put("FUNCTIONCODE","HQING015");

        } catch (Exception e) {
            e.printStackTrace();
        }

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                fillArrayList(beans);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                LogHelper.e(TAG, response);
                //[{"time":"0","bytes":28,"totalCount":"22","data":[["11603258","N电魂   ","22.49","15.62","0.4398207366466522","0.0","挂"],["11603859","能科股份","14.47","13.15","0.10038027912378311","1.8316308559351884E-4","挂"],["11600275","武昌鱼  ","18.54","16.85","0.10029676556587219","0.20067202707361603","挂"],["11603667","五洲新春","13.94","12.67","0.10023674368858337","0.0","挂"],["11600250","南纺股份","17.02","15.47","0.10019393265247345","0.10689333581659087","挂"],["11601020","华钰矿业","34.94","31.76","0.10012589395046234","0.32884423076923075","挂"],["11600759","洲际油气","8.79","7.99","0.10012518614530563","0.06519489448560066","挂"],["11603090","宏盛股份","66.94","60.85","0.10008223354816437","0.485716","挂"],["11603887","城地股份","54.85","49.86","0.10008017718791962","0.00941869918699187","挂"],["11600716","凤凰股份","8.69","7.9","0.09999993443489075","0.038905178685007716","挂"],["11600158","中体产业","18.27","16.61","0.09993978589773178","0.11290208403468172","挂"],["11603160","汇顶科技","54.48","49.53","0.09993945062160492","5.533333333333333E-4","挂"],["11603777","来伊份  ","43.59","39.63","0.09992427378892899","0.40541166666666667","挂"],["11600862","中航高科","14.11","12.83","0.09976615011692047","0.055240204290735485","挂"],["11600868","梅雁吉祥","6.74","6.13","0.0995105430483818","0.018821444492336314","挂"],["11603031","安德利  ","73.9","67.71","0.09141932427883148","0.38055","挂"],["11600313","农发种业","6.11","5.74","0.06445999443531036","0.04916413542350918","挂"],["11600747","大连控股","5.32","5.02","0.05976099520921707","0.06119173373668478","挂"],["11600051","宁波联合","12.0","11.34","0.05820104479789734","0.07636334501883633","挂"],["11603919","金徽酒  ","34.86","32.97","0.05732481926679611","0.20226714285714287","挂"],["11600809","山西汾酒","23.45","22.19","0.05678234249353409","0.032520594087555754","挂"],["11600887","伊利股份","18.43","17.46","0.05555562674999237","0.026707449133874166","挂"]],"code":"0","ceshi":"挂","isOpen":"true"}]
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject res = jsonArray.getJSONObject(0);
                    String code = res.optString("code") ;
                    if("0".equals(code)){
                        JSONArray json = res.getJSONArray("data");
                        if(null != json && json.length() > 0){
                            for(int i = 0 ; i < json.length();i++){
                                JSONArray data = json.getJSONArray(i);
//                                for(int j = 0; j < data.length();j++){
                                StockInfoEntity stockInfoEntity = new StockInfoEntity();
                                stockInfoEntity.setStockNumber(data.getString(0));
                                stockInfoEntity.setStockName(data.getString(1));
                                stockInfoEntity.setNewPrice(data.getString(2));
                                stockInfoEntity.setClose(data.getString(3));
                                stockInfoEntity.setPriceChangeRatio(TransitionUtils.string2double(data.getString(4)));
                                stockInfoEntity.setTurnover(data.getString(5));
                                stockInfoEntity.setCode(code);
                                stockInfoEntity.setType(mType);
                                beans.add(stockInfoEntity);
//                                }
                            }
                        }
                        fillArrayList(beans);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    ArrayList<StockInfoEntity> beans1 = new ArrayList<StockInfoEntity>();
                    fillArrayList(beans1);
                }
            }
        });
    }

    private int refleshCount = 0;
    private Timer mTimer;
    private int refleshTime = 0;
    private boolean isCallBackSuccess = false;
    private void startTimerTask() {
        if(mTimer!=null){
            mTimer.cancel();
        }
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
//                LogHelper.e(TAG, "Timer request:"+isCallBackSuccess+"|"+refleshTime+"  refleshCount:"+refleshCount);
                if(isCallBackSuccess&&refleshTime!=-1)
                {
//                    if(!"0".equals(refleshTime)){
                    if(refleshTime>0){
                        refleshCount +=3;
                        if(refleshCount>=refleshTime){
                            request();
                        }
                    }else{
                        refleshCount = 0;
                        request();
//                            LogHelper.e(TAG, "stockDetailData Timer request:"+isShowMingxi);
                    }
//                    }

                }
            }
        }, 3000, 3000/* 表示3000毫秒之後，每隔3000毫秒執行一次 */);
    }
    private void stopTimerTask(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_stocklist;
    }
}
