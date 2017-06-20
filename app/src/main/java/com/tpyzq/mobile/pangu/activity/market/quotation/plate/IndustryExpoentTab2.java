package com.tpyzq.mobile.pangu.activity.market.quotation.plate;

import android.app.Activity;
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
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.base.CustomApplication;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/12/5.
 * 行业板块
 */

public class IndustryExpoentTab2 extends BaseTabPager implements View.OnClickListener,
        ListViewScroller.ListViewScrollerListener, IListViewScrollSubject {

    private ListView mListView;
    public TextView  mHeadTv2;
    private String   mOrder;
    private String   mStartNo;
    private int      mNetTotalCount;
    private PlateListAdapter mAdapter;
    private String mType = "1";
    TextView mHeadTv3;
//    private PlateBaseListTab.LoadInterface mLoadInterface;
    private boolean middleFlag = false;
    private static final String TAG = "IndustryExpoentTab2";
    private boolean doSelectMiddle;         //是否第一次加载list列表
    private ArrayList<IListViewScrollObserver> mObservers;
    private boolean mJudegeGesture;
    private ArrayList<StockInfoEntity> mBeans;
    private Activity mActivity;
    private FrameLayout mEmptyLayout;
    public IndustryExpoentTab2(Activity activity, ArrayList<BaseTabPager> tabs,String type) {
        super(activity, tabs);
        this.mType = type;
        mActivity = activity;
        LogHelper.e(TAG,"mType:"+mType);
    }
//    public IndustryExpoentTab2(Activity activity, ArrayList<BaseTabPager> tabs) {
//        super(activity, tabs);
//        mActivity = activity;
//    }
    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;

        mObservers = new ArrayList<>();
        mBeans = new ArrayList<>();
        mStartNo = "0";
        mOrder = "1";

        mEmptyLayout = (FrameLayout) view.findViewById(R.id.fl_fragmentIndustry);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyLayout.setOnClickListener(this);


        TextView mHeadTv1 = (TextView) view.findViewById(R.id.plate_headTv1);
        mHeadTv2 = (TextView) view.findViewById(R.id.plate_headTv2);
        mHeadTv3 = (TextView) view.findViewById(R.id.plate_headTv3);

        mHeadTv1.setText("名称");
        mHeadTv2.setText("涨跌幅");
        mHeadTv3.setText("领涨股");

        mHeadTv1.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
        mHeadTv3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));

        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_p);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mHeadTv2.setCompoundDrawables(null, null, drawable, null);//画在右边

        mHeadTv2.setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.plateListView);
        mAdapter = new PlateListAdapter(activity);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.e(TAG,"onItemClick position:"+position);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new ListViewScroller(TAG, mListView, mStartNo, IndustryExpoentTab2.this, IndustryExpoentTab2.this));
    }

    public void setLoadListener(LoadInterface loadListener) {
        mLoadInterface = loadListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plate_headTv2:

                if (mLoadInterface != null) {
                    mLoadInterface.loading();
                }
                mHeadTv2.setClickable(false);
                mHeadTv2.setFocusable(false);

                if (middleFlag) {
                    cancel();
                    mHeadTv3.setText("领涨股");
                    mOrder = "1";
                    mStartNo = "0";
//                    IndustryExponentService.mOberservers.add(this);
                    request();

                    Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_p);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    mHeadTv2.setCompoundDrawables(null, null, drawable, null);//画在右边
                    middleFlag = false;

                } else {

                    cancel();
                    mOrder = "0";
                    mStartNo = "0";
                    mHeadTv3.setText("领跌股");
//                    IndustryExponentService.mOberservers.add(this);
                    request();

                    Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), R.mipmap.sort_n);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    mHeadTv2.setCompoundDrawables(null, null, drawable, null);//画在右边
                    middleFlag = true;
                }

                break;
            case R.id.fl_fragmentIndustry:
                toRunConnect();
                break;
        }
    }

    @Override
    public void myTabonResume() {
        LogHelper.e(TAG,"myTabonResume");
        timerConnct();
        startTimerTask();
    }

    @Override
    public void toRunConnect() {
        LogHelper.e(TAG,"toRunConnect");
        timerConnct();
        startTimerTask();
    }

    @Override
    public void toStopConnect() {
        stopTimerTask();
        cancel();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_industry;
    }


    private void timerConnct() {
        loadingHandler.sendEmptyMessage(0);
        request();
//        mServiceIntent = new Intent(mActivity, IndustryExponentService.class);
//        String [] keys = {"startNo", "order", "type"};
//        String [] values = {mStartNo, mOrder, "1"};
//        IndustryExponentService.handler.sendMessage(sendMessage(keys, values));
//        IndustryExponentService.mOberservers.add(this);
//        notifyObservers(3 * 1000);
//        mActivity.startService(mServiceIntent);
    }

//    private void reStartConnect() {

//        String [] keys = {"startNo", "order", "type"};
//        String [] values = {mStartNo, mOrder, "1"};
//        IndustryExponentService.handler.sendMessage(sendMessage(keys, values));
//        if (mServiceIntent != null) {
//            mActivity.startService(mServiceIntent);
//        }
//        request();
//    }


    private void cancel() {

//        IndustryExponentService.mOberservers.remove(this);
//        if (mServiceIntent != null) {
//            mActivity.stopService(mServiceIntent);
//        }
    }


    //////////////////// 翻页相关实现

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
        mStartNo = startNumber;
        if (mLoadInterface != null) {
            mLoadInterface.loading();
        }

        cancel();
        startTimerTask();
//        IndustryExponentService.mOberservers.add(this);
        request();
    }

    @Override
    public void lastPage(String startNumber) {
        mStartNo = startNumber;
        if (mLoadInterface != null) {
            mLoadInterface.loading();
        }

        cancel();
        startTimerTask();
//        IndustryExponentService.mOberservers.add(this);
        request();
    }

    @Override
    public void connectBySrollStop() {

    }

    @Override
    public void doSelectMiddle(boolean doSelectMiddle) {
        this.doSelectMiddle = doSelectMiddle;
    }

    @Override
    public void juedgeGesture(boolean isUp) {
        mJudegeGesture = isUp;
    }

    public void fillStockList(ArrayList<StockInfoEntity> entities) {
        mEmptyLayout.setVisibility(View.GONE);
        if (mLoadInterface != null) {
            mLoadInterface.complited();
        }

        mHeadTv2.setClickable(true);
        mHeadTv2.setFocusable(true);


        if (entities.size() <= 0 && mBeans.size() <= 0 || entities == null && mBeans.size() <= 0) {
            mAdapter.setDatas(null, mType);
            mEmptyLayout.setVisibility(View.VISIBLE);
//            notifyObservers(60 * 1000);
            return;
        }


        if (entities!= null && entities.size() > 0 && entities.get(0).getCode().equals("-5")) {
            Helper.getInstance().showToast(mActivity, "Code:-5");

            if (mBeans == null || mBeans.size() <= 0) {
                mAdapter.setDatas(null,mType);
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (entities == null || entities.size() <= 0) {
            return;
        }

        mBeans = entities;//得到返回的结果

        if (!TextUtils.isEmpty(mBeans.get(0).getTotalCount())) {
            mNetTotalCount = Integer.parseInt(mBeans.get(0).getTotalCount());
        }

        notifyObservers();

        for (StockInfoEntity bean : mBeans) {
            ArrayList<StockInfoEntity> datas = initData(bean);
            mAdapter.setDatas(datas, mType);
            if (doSelectMiddle) {
                if (mJudegeGesture) {
                    int index = 10;//mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition()-1;
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
        }

        String time = mBeans.get(0).getTime();

        if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
            int intTIME = Integer.valueOf(time);
//            notifyObservers(intTIME * 1000);
        }
    }

    /**
     * 处理网络返回的板块数据
     * @param entity
     * @return
     */
    private ArrayList<StockInfoEntity> initData(StockInfoEntity entity) {
        ArrayList<StockInfoEntity> entities = new ArrayList<>();
        JSONArray array = entity.getData();
        if (entity != null && !entity.getCode().equals("-5") && array != null && array.length()> 0) {
            for (int i = 0; i < array.length(); i++) {
                StockInfoEntity _bean = new StockInfoEntity();
                _bean.setType("4");
                _bean.setCode(entity.getCode());
                _bean.setTotalCount(entity.getTotalCount());
                JSONArray itemArr = array.optJSONArray(i);
                if (!TextUtils.isEmpty(itemArr.optString(0))) {
                    _bean.setIndustryNumber(itemArr.optString(0));
                }

                if (!TextUtils.isEmpty(itemArr.optString(1))) {
                    _bean.setIndustryName(itemArr.optString(1));
                }

                if (!TextUtils.isEmpty(itemArr.optString(2))) {
                    _bean.setIndustryUpAndDown(itemArr.optString(2));
                }

                if (!TextUtils.isEmpty(itemArr.optString(3))) {
                    _bean.setStockNumber(itemArr.optString(3));
                }

                if (!TextUtils.isEmpty(itemArr.optString(4))) {
                    _bean.setStockName(itemArr.optString(4));
                }

                if (!TextUtils.isEmpty(itemArr.optString(5))) {
                    _bean.setPriceChangeRatio(TransitionUtils.string2double(itemArr.optString(5)));
                }
                entities.add(_bean);
            }
        }
        return entities;
    }
    Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadInterface != null) {
                mLoadInterface.loading();
            }
        }
    };
    public void request() {
        loadingHandler.sendEmptyMessage(0);
        isCallBackSuccess = false;
        Map<String, String> params = new HashMap();

        try {
            Object []  object = new Object[1];

            Map map2 = new HashMap();
            map2.put("Type", mType);
            map2.put("order", mOrder);
            map2.put("start", mStartNo);
            map2.put("num", "30");
            object[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);

            params.put("FUNCTIONCODE","HQING009");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogHelper.e(TAG, mType+"  request :"+params.toString());
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                fillStockList(beanss);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                LogHelper.e(TAG, mType+" --response:"+response);
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
                try {
                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillStockList(beans);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);
                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("date");
                    refleshTime = TransitionUtils.string2int(time);
                    String totalCount = jsonObject.optString("totalCount");
                    JSONArray array1 = jsonObject.optJSONArray("data");
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                        if (code.equals("-5") || code.equals("-1") || code.equals("-3")) {
                            code = "-5";
                            stockInfoEntity.setCode(code);
                        } else {
                            stockInfoEntity.setCode(code);
                            stockInfoEntity.setTime(time);
//                            stockInfoEntity.setBytes(bytes);
                            stockInfoEntity.setTotalCount(totalCount);
                            stockInfoEntity.setData(array1);
                            beanss.add(stockInfoEntity);
                        }
                    fillStockList(beanss);
                } catch (Exception e) {
                    e.printStackTrace();
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
//                    mCallbackResult.getResult(beanss, TAG);
                    fillStockList(beanss);
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
//                LogHelper.e(TAG, mType+" Timer request:"+isCallBackSuccess+"|"+refleshTime+"  refleshCount:"+refleshCount);
                if(isCallBackSuccess)
                {
//                    if(!"0".equals(refleshTime)){
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

}
