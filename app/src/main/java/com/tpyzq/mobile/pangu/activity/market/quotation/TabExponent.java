package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.app.Activity;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ListViewScroller;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhangwnebo on 2016/8/20.
 * 指数
 */
public class TabExponent extends BaseTabPager implements View.OnClickListener,
        AdapterView.OnItemClickListener,  ListViewScroller.ListViewScrollerListener {

    public TextView mHeadTv1;
    public TextView mHeadTv2;
    public TextView mHeadTv3;
    private HQExponentAdapter mAdapter;
    private boolean rightFlag = false;
    private static final String TAG = "BaseExponentList";
    private int    mNetTotalCount;
    private String mStartNum;
//    public static ArrayList<ExponentTimeObsever> mObserves = new ArrayList<>();
//    private ArrayList<IListViewScrollObserver> mObservers;
    private boolean doSelectMiddle;         //是否第一次加载list列表
    private ListView mListView;
    private Activity mActivity;
    private Intent mServiceIntent;
    private String  mOrder;
    private boolean mJudegeGesture;
    private ArrayList<StockInfoEntity> mBeans;
    private FrameLayout mBackgroundLayout;

    public TabExponent(Activity activity, ArrayList<BaseTabPager> tabs){
        super(activity, tabs);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
//        mObservers = new ArrayList<>();
        mOrder = "1";
        mStartNum = "0";
        mHeadTv1 = (TextView) view.findViewById(R.id.plate_headTv1);
        mHeadTv2 = (TextView) view.findViewById(R.id.plate_headTv2);
        mHeadTv3 = (TextView) view.findViewById(R.id.plate_headTv3);

        mHeadTv1.setText("证券名称");
        mHeadTv2.setText("现价");
        mHeadTv3.setText("涨跌幅");

        mHeadTv1.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
        mHeadTv2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));

        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_p);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mHeadTv3.setCompoundDrawables(null, null, drawable, null);//画在右边

        mHeadTv3.setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.plateListView);
        mAdapter = new HQExponentAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new ListViewScroller(TAG, mListView, mStartNum, TabExponent.this, null));

        mBackgroundLayout = (FrameLayout) view.findViewById(R.id.fl_fragmentIndustry);
        mBackgroundLayout.setVisibility(View.VISIBLE);
        mBackgroundLayout.setOnClickListener(this);

        if (mMarketFragmentEditIsGoneListener != null) {
            mMarketFragmentEditIsGoneListener.gone();
        }
    }

//    @Override
//    public void registerObserver(IListViewScrollObserver observer) {
//        mObservers.add(observer);
//    }
//
//    @Override
//    public void removeObserver(IListViewScrollObserver observer) {
//        int num = mObservers.indexOf(observer);
//        if (num >= 0) {
//            mObservers.remove(observer);
//        }
//
//    }
//
//    @Override
//    public void notifyObservers() {
//
//        for (IListViewScrollObserver observer : mObservers) {
//            observer.update(mNetTotalCount);
//        }
//    }

    @Override
    public void nextPage(String startNumber) {
        mStartNum = startNumber;
        startTimerTask();
        reStartConnect();
    }

    @Override
    public void lastPage(String startNumber) {
        mStartNum = startNumber;
        startTimerTask();
        reStartConnect();

    }

    @Override
    public void juedgeGesture(boolean isUp) {
        mJudegeGesture = isUp;
    }

    @Override
    public void connectBySrollStop() {

    }

    @Override
    public void doSelectMiddle(boolean doSelectMiddle) {
        this.doSelectMiddle = doSelectMiddle;
    }


    @Override
    public void myTabonResume() {
        if(MarketFragment.pageIndex==1){
            timerConnct();
            startTimerTask();
        }
    }

    @Override
    public void toRunConnect() {

        if (mMarketFragmentEditIsGoneListener != null) {
            mMarketFragmentEditIsGoneListener.gone();
        }
        timerConnct();
        startTimerTask();
    }

    Handler laodingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadInterface != null) {
                mLoadInterface.loading();
            }
        }
    };
    private void timerConnct() {
        laodingHandler.sendEmptyMessage(0);
        LogHelper.e(TAG,"timerConnct");
        request();
    }

    private void reStartConnect() {
        request();
        startTimerTask();
        LogHelper.e(TAG,"reStartConnect");
    }


    @Override
    public void toStopConnect() {
        cancelTimer();
        stopTimerTask();
    }
    @Override
    public void onHiddenChanged(boolean hided) {
        LogHelper.e(TAG,"onHiddenChanged hided:"+hided);
        super.onHiddenChanged(hided);
        if(hided){
            stopTimerTask();
        }
    }
    private void cancelTimer() {
//        ExponentService.mOberservers.remove(this);
//        if (mActivity != null && mServiceIntent != null) {
//            mActivity.stopService(mServiceIntent);
//        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.plate_headTv3:
                Drawable drawable = null;
                if (rightFlag) {
//                    cancelTimer();
//                    ExponentService.mOberservers.add(this);
                    try {
                        mOrder = "1";
                        reStartConnect();
                        drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_p);
                        rightFlag = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
//                    cancelTimer();
//                    ExponentService.mOberservers.add(this);
                    try {
                        mOrder = "2";
                        reStartConnect();
                        drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_n);
                        rightFlag = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                mHeadTv3.setCompoundDrawables(null, null, drawable, null);//画在右边
                break;
            case R.id.fl_fragmentIndustry:

                toRunConnect();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        StockDetailEntity data = new StockDetailEntity();
        data.setStockName(mBeans.get(position).getStockName());
        data.setStockCode(mBeans.get(position).getStockNumber());

        intent.putExtra("stockIntent", data);

        intent.setClass(this.mActivity, StockDetailActivity.class);
        this.mActivity.startActivity(intent);
    }
//
//    @Override
//    public void registerObserver(ExponentTimeObsever observer) {
//        mObserves.add(observer);
//    }
//
//    @Override
//    public void removeObserver(ExponentTimeObsever observer) {
//        int num = mObserves.indexOf(observer);
//        if (num >= 0) {
//            mObserves.remove(observer);
//        }
//    }
//
//    @Override
//    public void notifyObservers(long time) {
//        for (ExponentTimeObsever observer : mObserves) {
//            observer.update(time);
//        }
//    }

    public void fillStockList(ArrayList<StockInfoEntity> entities) {

        mBackgroundLayout.setVisibility(View.GONE);
        if (mLoadInterface != null) {
            mLoadInterface.complited();
        }

        if (entities == null || entities.size() <= 0) {

            if (mBeans == null || mBeans.size() <= 0) {
                mBackgroundLayout.setVisibility(View.VISIBLE);
            }

//            notifyObservers(60 * 1000);

        } else {
            mBeans = entities;

            if (mBeans.get(0).getCode().equals("-5")) {
                mBackgroundLayout.setVisibility(View.VISIBLE);
//                notifyObservers(60 * 1000);
                return;
            }

            if (!TextUtils.isEmpty(mBeans.get(0).getTotalCount())) {
                mNetTotalCount = Integer.parseInt(mBeans.get(0).getTotalCount());
            }

//            notifyObservers();

            mAdapter.setData(mBeans);

            if (doSelectMiddle) {

                if (mJudegeGesture) {

                    int index = 10;//mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition()
                    View v = mListView.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();
                    mListView.setSelectionFromTop(index, top);

                } else {
                    int index = 20-(mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition() +1);
                    View v = mListView.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();
                    mListView.setSelectionFromTop(index, top);
                }


            }

            String time = mBeans.get(0).getTime();

            if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
                int _time = Integer.valueOf(time);
//                notifyObservers(_time * 1000);
            }
        }
    }
    public void request() {
        Map<String, String> params = new HashMap();
        isCallBackSuccess = false;
        try {
            Object []  object = new Object[1];
            Map map2 = new HashMap();
            map2.put("type","9");
            map2.put("flag","1");
            map2.put("asc",mOrder);
            map2.put("startNo", mStartNum);
            map2.put("number", "30");
            map2.put("exponential", "3");
            params.put("FUNCTIONCODE","HQING001");

            object[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogHelper.e(TAG, "request:"+params.toString());
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                fillStockList(beans);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                LogHelper.e(TAG, "response:"+response);
//[{"time":"0","bytes":28,"totalCount":"30","data":[["20399238","2349.386","2388.704","餐饮指数","0.0","1.85776896E9","0.016735590994358063"],["10000006","5986.009","6081.110","地产指数","0.0","5.6098939584E9","0.01588730700314045"],["20399910","2410.205","2445.606","300 工业","0.0","3.3205342208E10","0.014687911607325077"],["10000910","2410.205","2445.606","300工业 ","0.0","3.32053414473E10","0.014687810093164444"],["10000072","2924.272","2963.213","工业等权","0.0","2.8672544289E10","0.013316279277205467"],["20399419","2305.176","2335.333","国证高铁","0.0","2.2682570752E10","0.013082398101687431"],["10000043","1986.151","2011.246","超大盘  ","0.0","1.947556366E10","0.012634974904358387"],["10000007","5730.938","5795.842","公用指数","0.0","2.12969312911E10","0.011325248517096043"],["10000050","2186.765","2211.240","50等权  ","0.0","3.114459546E10","0.011191927827894688"],["20399357","2882.834","2914.739","环渤海","0.0","2.7324751872E10","0.011066904291510582"],["10000052","2456.656","2483.812","50基本  ","0.0","3.114459546E10","0.011053953319787979"],["10000152","1747.373","1765.413","上央红利","0.0","1.2667086962E10","0.010324301198124886"],["20399925","3356.589","3390.684","基本面50","0.0","3.3083693056E10","0.010157696902751923"],["10000925","3356.589","3390.684","基本面50","0.0","3.30836921937E10","0.010157696902751923"],["10000042","1366.210","1380.012","上证央企","0.0","3.2526056731E10","0.010102578438818455"],["20399901","4663.482","4705.823","小康指数","0.0","4.046440448E10","0.009079333394765854"],["10000901","4663.482","4705.823","小康指数","0.0","4.0464402329E10","0.009079333394765854"],["20399369","1219.515","1230.094","CBN-兴全","0.0","3.5413282816E10","0.008674741722643375"],["10000053","9326.411","9407.047","180基本 ","0.0","6.685314243E10","0.00864595640450716"],["10000959","5382.088","5427.502","银河99  ","0.0","5.17880226757E10","0.008438091725111008"],["10000095","2812.863","2835.954","上证中游","0.0","5.4158970727E10","0.00820900872349739"],["10000098","3842.674","3873.554","上证F200","0.0","6.2707660303E10","0.008035976439714432"],["10000149","3088.968","3113.578","180红利 ","0.0","1.1853225492E10","0.00796709768474102"],["10000123","6855.396","6909.097","180动态 ","0.0","4.3209359286E10","0.007833416573703289"],["10000016","2223.424","2240.750","上证50  ","0.0","3.114459546E10","0.007792122662067413"],["20399383","2726.025","2747.169","1000工业","0.0","6.2336356352E10","0.007756453938782215"],["10000021","824.912","831.043","180治理 ","0.0","4.3100024538E10","0.007432654500007629"],["20399240","1178.732","1187.480","金融指数","0.0","8.467305472E9","0.007421365939080715"],["10000151","1091.026","1098.932","上国红利","0.0","7.108457217E9","0.007247069384902716"],["10000064","2409.645","2426.968","非周期  ","0.0","4.2490355677E10","0.00718892365694046"]],"code":"0","ceshi":"挂","isOpen":"false"}]
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                try {
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillStockList(beans);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);
                    StockInfoEntity stockInfoEntity = new StockInfoEntity();
                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("time");
                    LogHelper.e(TAG, "response time:"+time);
                    refleshTime = TransitionUtils.string2int(time);
                    String totalCount = jsonObject.optString("totalCount");
                    JSONArray data = jsonObject.optJSONArray("data");
                    if (data!= null) {
                        for (int j = 0; j < data.length(); j ++) {
                            StockInfoEntity _bean = new StockInfoEntity();
                            JSONArray array1 = data.optJSONArray(j);
                            _bean.setTime(time);
                            _bean.setTotalCount(totalCount);
                            _bean.setCode(code);
                            String stockNumber = array1.optString(0);
                            if (null != stockNumber) {
                                _bean.setStockNumber(stockNumber);
                            }
                            String close = array1.optString(1);
                            if (null != close) {
                                _bean.setClose(close);
                            }

                            String newPrice = array1.optString(2);
                            if (null != newPrice) {
                                _bean.setNewPrice(newPrice);
                            }

                            String stockName = array1.optString(3);
                            if (null != stockName) {
                                _bean.setStockName(stockName);
                            }

                            String turnover = array1.optString(4);
                            if (null != turnover) {
                                _bean.setTurnover(turnover);
                            }
                            Helper.getZdfAndzdz(_bean);
//                            LogHelper.e(TAG,j+":"+newPrice+","+close+"  "+_bean.getPriceChangeRatio());
//                            if (!TextUtils.isEmpty(_bean.getNewPrice()) && !TextUtils.isEmpty(_bean.getClose())) {
//                                double agoClosePrice = Double.parseDouble(_bean.getClose());
//                                double zd = Double.parseDouble(_bean.getNewPrice()) - agoClosePrice;
//                                double zdf = zd / agoClosePrice ;
//                                _bean.setPriceChangeRatio(zdf);
//                            }
                            beans.add(_bean);
                        }
                    }
                    fillStockList(beans);

                } catch (Exception e) {
                    e.printStackTrace();

                    ArrayList<StockInfoEntity> beans1 = new ArrayList<StockInfoEntity>();
                    fillStockList(beans1);
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
                LogHelper.e(TAG, "Timer request:"+isCallBackSuccess+"|"+refleshTime+"  refleshCount:"+refleshCount);
                if(isCallBackSuccess)
                {
                    if(refleshTime>0){
                        refleshCount +=3;
                        if(refleshCount>=refleshTime){
                            timerConnct();
                        }
                    }else{
                        refleshCount = 0;
                        timerConnct();
//                            LogHelper.e(TAG, "stockDetailData Timer request:"+isShowMingxi);
                    }

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
    public int getFragmentLayoutId() {
        return R.layout.fragment_industry;
    }
}
