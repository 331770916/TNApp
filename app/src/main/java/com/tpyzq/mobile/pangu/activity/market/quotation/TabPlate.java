package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.activity.market.quotation.plate.PlateAdapter;
import com.tpyzq.mobile.pangu.activity.market.quotation.plate.PlateIndexActivity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/20.
 * 板块Tab页
 */
public class TabPlate extends BaseTabPager implements AdapterView.OnItemClickListener,View.OnClickListener{
    private static final String TAG = "TabPlate";
    private PlateAdapter mAdapter;
    private ArrayList<StockInfoEntity> mIndustryDatas;
    private ArrayList<StockInfoEntity> mConceptDatas;
    private ArrayList<StockInfoEntity> mAreaDatas;
    private ArrayList<StockInfoEntity> mAdapterDatas;
//    public static ArrayList<PlateTimeObserver> mObserves = new ArrayList<>();
    private StockInfoEntity temp0, temp1, temp2;
    private Activity mActivity;
//    private Intent mServiceIntent;
    private FrameLayout mEmptyLayout;

    public TabPlate(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
//        LogHelper.e(TAG,"initView");
        mActivity = activity;
        mIndustryDatas = new ArrayList<>();
        mConceptDatas = new ArrayList<>();
        mAreaDatas = new ArrayList<>();
        mAdapterDatas = new ArrayList<>();

        mEmptyLayout = (FrameLayout) view.findViewById(R.id.fl_fragmentPlateLayout);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyLayout.setOnClickListener(this);

        ListView listView = (ListView) view.findViewById(R.id.plate_listView);
        mAdapter = new PlateAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        initConfigView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_fragmentPlateLayout:
                toRunConnect();
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hided) {
        LogHelper.e(TAG,"onHiddenChanged hided:"+hided);
        super.onHiddenChanged(hided);
        if(hided){
            stopTimerTask();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String type = mAdapterDatas.get(position).getType();

        if (type.equals("1")) {
//            Intent intent = new Intent();
//            StockListIntent data = new StockListIntent();
            String titleName = mAdapterDatas.get(position).getTitleTv();
//            data.setTitle(titleName);
//            data.setHead2("涨跌幅");
//            data.setHead3("领涨股");
//            data.setHead1("名称");
//            data.setFiledNames(new String[]{"industryName", "industryNumber", "industryUpAndDown", "stockName"});
//            intent.putExtra("stockIntent", data);
//            intent.setClass(mActivity, PlateListActivity.class);
//            mActivity.startActivity(intent);
//
            Intent intent = new Intent();
            intent.setClass(mActivity, PlateIndexActivity.class);

            if ("行业板块".equals(titleName)) {
                intent.putExtra("pageIndex", 0);
            } else if ("概念板块".equals(titleName)) {
                intent.putExtra("pageIndex", 1);
            } else if ("地域板块".equals(titleName)) {
                intent.putExtra("pageIndex", 2);
            }
//
            mActivity.startActivity(intent);
        } else {
            Intent intent2 = new Intent();
            StockListIntent data = new StockListIntent();


            if (!TextUtils.isEmpty(mAdapterDatas.get(position).getIndustryNumber())) {

                String industryNumber = mAdapterDatas.get(position).getIndustryNumber();

                if (!TextUtils.isEmpty(mAdapterDatas.get(position).getIndustryName())) {
                    data.setTitle(mAdapterDatas.get(position).getIndustryName());
                }

                data.setHead2("现价");
                data.setHead3("涨跌幅");
                data.setHead1("股票名称");

                intent2.putExtra("code", industryNumber);

                intent2.putExtra("market", "0");

                intent2.putExtra("tag", "lingzhang");

                if (mIndustryDatas != null && mIndustryDatas.size() > 0 && mIndustryDatas.contains(mAdapterDatas.get(position))) {
                    intent2.putExtra("type", "1");
                } else if (mConceptDatas != null && mConceptDatas.size() > 0 && mConceptDatas.contains(mAdapterDatas.get(position))){
                    intent2.putExtra("type", "3");
                } else if (mAreaDatas != null && mAreaDatas.size() > 0 && mAreaDatas.contains(mAdapterDatas.get(position))) {
                    intent2.putExtra("type", "2");
                    String industryName = mAdapterDatas.get(position).getIndustryName();
                    if (!TextUtils.isEmpty(industryName)) {
                        intent2.putExtra("code", industryName);
                    }
                }

                intent2.putExtra("isIndustryStockList", true);
                intent2.putExtra("stockIntent", data);

                intent2.setClass(mActivity, StockListActivity.class);
                mActivity.startActivity(intent2);
            }
        }
    }

    private void initConfigView() {
        temp0 = new StockInfoEntity();
        temp0.setType("1");
        temp0.setTitleTv("行业板块");

        temp1 = new StockInfoEntity();
        temp1.setType("1");
        temp1.setTitleTv("概念板块");

        temp2 = new StockInfoEntity();
        temp2.setType("1");
        temp2.setTitleTv("地域板块");
    }

    /**
     * 区域 行业 概念板块赋值
     *
     * @param infos
     * @param entity
     */
    private void setPlateData(ArrayList<StockInfoEntity> infos, StockInfoEntity entity) {

        if (entity != null && !entity.getCode().equals("-5") && entity.getData() != null && entity.getData().length() > 0) {
            for (int i = 0; i < entity.getData().length(); i++) {
                JSONArray array = entity.getData().optJSONArray(i);
                StockInfoEntity _bean = new StockInfoEntity();
                _bean.setType("2");
                _bean.setCode(entity.getCode());
                _bean.setTotalCount(entity.getTotalCount());

                //行业代码
                if (!TextUtils.isEmpty(array.optString(0))) {
                    _bean.setIndustryNumber(array.optString(0));
                }

                //行业名称
                if (!TextUtils.isEmpty(array.optString(1))) {
                    _bean.setIndustryName(array.optString(1));
                }

                //行业涨跌幅
                if (!TextUtils.isEmpty(array.optString(2))) {
                    _bean.setIndustryUpAndDown(array.optString(2));
                }

                //领涨/跌的股票代码
                if (!TextUtils.isEmpty(array.optString(3))) {
                    _bean.setStockNumber(array.optString(3));
                }

                //领涨/跌的股票名
                if (!TextUtils.isEmpty(array.optString(4))) {
                    _bean.setStockName(array.optString(4));
                }

                //领涨/跌股票的涨跌幅

                _bean.setPriceChangeRatio(TransitionUtils.string2double(array.optString(5)));
                if (!TextUtils.isEmpty(array.optString(6))) {
                    _bean.setNewPrice(array.optString(6));
                }

                if (!TextUtils.isEmpty(array.optString(7))) {
                    _bean.setClose(array.optString(7));
                }

                infos.add(_bean);
            }
        }
    }

    @Override
    public void myTabonResume() {
        if(MarketFragment.pageIndex==1){
            timerConnct();
            startTimerTask();
        }

        LogHelper.e(TAG,"myTabonResume");
    }

    @Override
    public void toRunConnect() {
        LogHelper.e(TAG,"toRunConnect");
        if (mMarketFragmentEditIsGoneListener != null) {
            mMarketFragmentEditIsGoneListener.gone();
        }
        timerConnct();
        startTimerTask();
//        if (mServiceIntent != null) {
//            PlateService.mOberservers.add(this);
//            mActivity.startService(mServiceIntent);
//        } else {
//            timerConnct();
//        }
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
    private void timerConnct() {
        loadingHandler.sendEmptyMessage(0);
//        mServiceIntent = new Intent(mActivity, PlateService.class);
//        PlateService.mOberservers.add(this);
//        notifyObservers(Helper.getTime());
//        mActivity.startService(mServiceIntent);
        request();

    }
    public void request() {
        isCallBackSuccess = false;
        Map<String, String> params = new HashMap();
        Object []  object = new Object[3];
        Map map2 = new HashMap();
        map2.put("Type","1");
        map2.put("order","1");
        map2.put("start","0");
        map2.put("num", "5");

        Map map3 = new HashMap();
        map3.put("Type","2");
        map3.put("order","1");
        map3.put("start","0");
        map3.put("num", "5");


        Map map4 = new HashMap();
        map4.put("Type","3");
        map4.put("order","1");
        map4.put("start","0");
        map4.put("num", "5");
        params.put("FUNCTIONCODE","HQING009_HQING009_HQING009");

        object[0] = map2;
        object[1] = map3;
        object[2] = map4;
        params.put("PARAMS", Arrays.toString(object));
        LogHelper.e(TAG, "request:"+params.toString());
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                fillArrayList(beanss);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
//                LogHelper.e(TAG, "response:"+response);
                if (TextUtils.isEmpty(response)) {
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                    fillArrayList(beanss);
                    return ;
                }

                try {
//                    ArrayList<Map<String, Object>> responseValues = objectMapper.readValue(response, new ArrayList<Map<String, Object>>().getClass());
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillArrayList(beanss);
                        return;
                    }
                    for (int i=0;i<array.length();i++) {
                        JSONObject jsonObject = array.optJSONObject(i);
                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                        String code = jsonObject.optString("code");

                        if (code.equals("-5") || code.equals("-1") || code.equals("-3")) {
                            code = "-5";
                            stockInfoEntity.setCode(code);
                        } else {
                            String time = jsonObject.optString("date");
                            int bytes = TransitionUtils.string2int(jsonObject.optString("bytes"));
                            String totalCount = jsonObject.optString("totalCount");
                            JSONArray array1 = jsonObject.optJSONArray("data");
                            refleshTime = TransitionUtils.string2int(time);
                            stockInfoEntity.setCode(code);
                            stockInfoEntity.setTime(time);
                            stockInfoEntity.setBytes(bytes);
                            stockInfoEntity.setTotalCount(totalCount);
                            stockInfoEntity.setData(array1);
                        }
                        beanss.add(stockInfoEntity);
                    }
                    fillArrayList(beanss);
                } catch (Exception e) {
                    e.printStackTrace();
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                    fillArrayList(beanss);
                }
            }
        });

    }
    @Override
    public void toStopConnect() {
        LogHelper.e(TAG,"toStopConnect");
        cancelTimer();
    }

    private void cancelTimer() {
//        PlateService.mOberservers.remove(this);
//        if (mActivity != null && mServiceIntent != null) {
//            mActivity.stopService(mServiceIntent);
//        }
        stopTimerTask();
    }

    public void fillArrayList(ArrayList<StockInfoEntity> entities) {

        mEmptyLayout.setVisibility(View.GONE);

        if (mLoadInterface != null) {
            mLoadInterface.complited();
        }

        ArrayList<StockInfoEntity> beans = entities;

        if (beans != null && beans.size() > 0) {

            mIndustryDatas.clear();
            mConceptDatas.clear();
            mAreaDatas.clear();

            for (int i = 0; i < beans.size(); i++) {
                switch (i) {
                    case 0:
                        setPlateData(mIndustryDatas, beans.get(i));
                        break;
                    case 1:
                        setPlateData(mConceptDatas, beans.get(i));
                        break;
                    case 2:
                        setPlateData(mAreaDatas, beans.get(i));
                        break;
                }
            }

            mAdapterDatas.clear();

            mAdapterDatas.add(temp0);
            mAdapterDatas.addAll(mIndustryDatas);
            mAdapterDatas.add(temp1);
            mAdapterDatas.addAll(mConceptDatas);
            mAdapterDatas.add(temp2);
            mAdapterDatas.addAll(mAreaDatas);
            mAdapter.setDatas(mAdapterDatas);


            String time = beans.get(0).getTime();

            if (!TextUtils.isEmpty(time) && !"0".equals(time)) {

                int _time = Integer.valueOf(time);
//                notifyObservers(_time * 1000);
            }

        } else {

            if (mAdapterDatas == null || mAdapterDatas.size() <= 0) {
                mEmptyLayout.setVisibility(View.VISIBLE);
            }

//            notifyObservers(60 * 1000);
        }
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
    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_plate;
    }

}
