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

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.activity.market.quotation.plate.PlateListActivity;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/20.
 * 沪深页面View
 */
public class TabHuShen extends BaseTabPager implements AdapterView.OnItemClickListener,
         View.OnClickListener {
    private static final String TAG = "TabHuShen";
    private ArrayList<StockInfoEntity> mTopGridDatas;
    private ArrayList<StockInfoEntity> mMiddleGridDatas;
    private ArrayList<StockInfoEntity> mLedDatas;
    private ArrayList<StockInfoEntity> mDropDatas;
    private ArrayList<StockInfoEntity> mFundImportDatas;
    private ArrayList<StockInfoEntity> mFundOutDatas;
    private ArrayList<StockInfoEntity> mChangeHandDatas;
    private StockInfoEntity temp0, temp1, temp2, temp3, temp4, temp5;
//    private boolean isLeave = false;//是否从当前页面跳转离开，返回时刷新数据
    private ArrayList<StockInfoEntity> mAdapterDatas;
//    public static ArrayList<HuShenTimeObserver> mObserves = new ArrayList<>();
    private FrameLayout mBackgroudLayout;

    private HuShenAdapter mAdapter;
    private Activity mActivity;
    private Intent mServiceIntent;

    public TabHuShen(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        pageTag = 21;
        mAdapterDatas = new ArrayList<>();
        isShow = false;
        ListView listView = (ListView) view.findViewById(R.id.hushen_ListView);
        mBackgroudLayout = (FrameLayout) view.findViewById(R.id.hushenLayout);
        mBackgroudLayout.setVisibility(View.VISIBLE);
        mBackgroudLayout.setOnClickListener(this);

        mAdapter = new HuShenAdapter(activity);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);

        initConfigView();
    }

    @Override
    protected void tabSelect() {
        super.tabSelect();
        isShow = true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.hushenLayout:
                timerConnct();
                break;
        }
    }

    private void initConfigView() {
        isShow = false;
        mTopGridDatas = new ArrayList<>();
        mMiddleGridDatas = new ArrayList<>();
        mLedDatas = new ArrayList<>();
        mDropDatas = new ArrayList<>();
        mFundImportDatas = new ArrayList<>();
        mFundOutDatas = new ArrayList<>();
        mChangeHandDatas = new ArrayList<>();

        temp0 = new StockInfoEntity();
        temp0.setType("1");
        temp0.setTitleTv("领涨板块");

        temp1 = new StockInfoEntity();
        temp1.setType("1");
        temp1.setTitleTv("领涨股");

        temp2 = new StockInfoEntity();
        temp2.setType("1");
        temp2.setTitleTv("领跌股");

        temp3 = new StockInfoEntity();
        temp3.setType("1");
        temp3.setTitleTv("资金流入");

        temp4 = new StockInfoEntity();
        temp4.setType("1");
        temp4.setTitleTv("资金流出");

        temp5 = new StockInfoEntity();
        temp5.setType("1");
        temp5.setTitleTv("换手率");
    }

    /**
     * 为 深证指数 沪深指数 创业指数  3个grid块 的数据
     */
    private void setHushenTopData(ArrayList<StockInfoEntity> infos, StockInfoEntity entity) {
        if (entity != null && !entity.getCode().equals("-5") && entity.getData() != null && entity.getData().length() > 0) {
            for (int i = 0; i < 3; i++) {
                StockInfoEntity _bean = new StockInfoEntity();
                if (i < entity.getData().length()) {
                    _bean.setCode(entity.getCode());
                    _bean.setTotalCount(entity.getTotalCount());
                    _bean.setType("2");
                    JSONArray jsonArray =  entity.getData().optJSONArray(i);
                    if (!TextUtils.isEmpty(jsonArray.optString(0))) {
                        _bean.setStockNumber(jsonArray.optString(0));
                    }

                    if (!TextUtils.isEmpty(jsonArray.optString(1))) {
                        _bean.setStockName(jsonArray.optString(1));
                    }

                    if (!TextUtils.isEmpty(jsonArray.optString(2))) {
                        _bean.setDate(jsonArray.optString(2));
                    }

                    if (!TextUtils.isEmpty(jsonArray.optString(3))) {
                        _bean.setNewPrice(jsonArray.optString(3));
                    }

                    if (!TextUtils.isEmpty(jsonArray.optString(4))) {
                        _bean.setClose(jsonArray.optString(4));
                    }
                }

                infos.add(_bean);
            }

        }
    }

    /**
     * 6个grid块的数据
     *
     * @param infos
     * @param entity
     */
    private void setHushenMiddleData(ArrayList<StockInfoEntity> infos, StockInfoEntity entity) {
        if (entity != null && !entity.getCode().equals("-5") && entity.getData() != null && entity.getData().length() > 0) {
            for (int i = 0; i < entity.getData().length(); i++) {
                StockInfoEntity _bean = new StockInfoEntity();
                JSONArray jsonArray = entity.getData().optJSONArray(i);
                _bean.setType("4");
                _bean.setCode(entity.getCode());
                _bean.setTotalCount(entity.getTotalCount());

                if (!TextUtils.isEmpty(jsonArray.optString(0))) {
                    _bean.setIndustryNumber(jsonArray.optString(0));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(1))) {
                    _bean.setIndustryName(jsonArray.optString(1));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(2))) {
                    _bean.setIndustryUpAndDown(jsonArray.optString(2));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(3))) {
                    _bean.setStockNumber(jsonArray.optString(3));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(4))) {
                    _bean.setStockName(jsonArray.optString(4));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(5))) {
                    _bean.setPriceChangeRatio(TransitionUtils.string2double(jsonArray.optString(5)));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(6))) {
                    _bean.setNewPrice(jsonArray.optString(6));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(7))) {
                    _bean.setClose(jsonArray.optString(7));
                }

                infos.add(_bean);

            }
        }
    }

    /**
     * -
     * 资金流入流出 数据模块赋值
     *
     * @param foundFlag 1000.资金流入， 1001.资金流出
     */
    private void setHushenFundData(ArrayList<StockInfoEntity> infos, StockInfoEntity entity, int foundFlag) {
        if (entity != null && !entity.getCode().equals("-5") && entity.getData() != null && entity.getData().length() > 0) {
            for (int i = 0; i < entity.getData().length(); i++) {
                StockInfoEntity _bean = new StockInfoEntity();
                JSONArray jsonArray = entity.getData().optJSONArray(i);
                _bean.setType("4");
                _bean.setCode(entity.getCode());
                _bean.setTotalCount(entity.getTotalCount());

                if (!TextUtils.isEmpty(jsonArray.optString(0))) {
                    _bean.setStockNumber(jsonArray.optString(0));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(1))) {
                    _bean.setStockName(jsonArray.optString(1));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(2))) {
                    _bean.setInflowMoney(jsonArray.optString(2));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(3))) {
                    _bean.setOutflowMoney(jsonArray.optString(3));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(4))) {
                    _bean.setClose(jsonArray.optString(4));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(5))) {
                    _bean.setNewPrice(jsonArray.optString(5));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(6))) {
                    _bean.setPriceToal(jsonArray.optString(6));
                }

                if (!TextUtils.isEmpty(_bean.getNewPrice()) && !TextUtils.isEmpty(_bean.getClose())
                        && Helper.isDecimal(_bean.getNewPrice()) && Helper.isDecimal(_bean.getClose())) {

                    double agoClosePrice = Double.parseDouble(_bean.getClose());
                    double zd = Double.parseDouble(_bean.getNewPrice()) - agoClosePrice;
                    double zdf = zd / agoClosePrice;
                    _bean.setPriceChangeRatio(zdf);
                }

                _bean.setFoundFlag(foundFlag);


                infos.add(_bean);

            }
        }
    }

    /**
     * 为 领涨股 领跌股  换手率 数据模块赋值
     *
     * @param infos
     * @param entity
     * @param isChangeHand 是否是换手率 1004不是换手率选项， 1003是
     */
    private void setHushenData(ArrayList<StockInfoEntity> infos, StockInfoEntity entity, int isChangeHand) {
        if (entity != null && !entity.getCode().equals("-5") && entity.getData() != null && entity.getData().length() > 0) {

            for (int i = 0; i < entity.getData().length(); i++) {
                StockInfoEntity _bean = new StockInfoEntity();
                JSONArray jsonArray = entity.getData().optJSONArray(i);
                _bean.setType("4");
                _bean.setCode(entity.getCode());
                _bean.setTotalCount(entity.getTotalCount());

                if (!TextUtils.isEmpty(jsonArray.optString(0))) {
                    _bean.setStockNumber(jsonArray.optString(0));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(1))) {
                    _bean.setClose(jsonArray.optString(1));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(2))) {
                    _bean.setNewPrice(jsonArray.optString(2));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(3))) {
                    _bean.setStockName(jsonArray.optString(3));
                }

                if (!TextUtils.isEmpty(jsonArray.optString(4))) {
                    _bean.setTurnover(jsonArray.optString(4));
                }

                //成交金额
                if (!TextUtils.isEmpty(jsonArray.optString(5))) {
                    _bean.setTotalPrice(jsonArray.optString(5));
                }

                if (!TextUtils.isEmpty(_bean.getNewPrice()) && !TextUtils.isEmpty(_bean.getClose()) && Helper.isDecimal(_bean.getNewPrice()) && Helper.isDecimal(_bean.getClose())) {
                    double agoClosePrice = Double.parseDouble(_bean.getClose());
                    double zd = Double.parseDouble(_bean.getNewPrice()) - agoClosePrice;
                    double zdf = zd / agoClosePrice;
                    _bean.setPriceChangeRatio(zdf);
                }

                _bean.setIsChangeHand(isChangeHand);

                infos.add(_bean);

            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        StockListIntent data = new StockListIntent();

        if (!TextUtils.isEmpty(mAdapterDatas.get(position).getTitleTv()) && mAdapterDatas.get(position).getTitleTv().equals("领涨板块")) {
            data.setTitle("沪深今日领涨板块");
            data.setHead2("涨跌幅");
            data.setHead3("领涨股");
            data.setHead1("行业名称");
            data.setFiledNames(new String[]{"industryName", "industryNumber", "industryUpAndDown", "stockName"});
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, PlateListActivity.class);
            mActivity.startActivity(intent);
        } else if (!TextUtils.isEmpty(mAdapterDatas.get(position).getTitleTv()) && mAdapterDatas.get(position).getTitleTv().equals("领涨股")) {
            data.setTitle("领涨股");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            data.setHead1("股票名称");
            intent.putExtra("type", "1");
            intent.putExtra("flag", "1");
            intent.putExtra("asc", "1");
            intent.putExtra("tag", "lingzhang");
            intent.putExtra("isPlateList", true);
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, StockListActivity.class);
            mActivity.startActivity(intent);
        } else if (!TextUtils.isEmpty(mAdapterDatas.get(position).getTitleTv()) && mAdapterDatas.get(position).getTitleTv().equals("领跌股")) {
            data.setTitle("领跌股");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            data.setHead1("股票名称");
            intent.putExtra("asc", "2");
            intent.putExtra("type", "1");
            intent.putExtra("flag", "1");
            intent.putExtra("tag", "lingdie");
            intent.putExtra("isPlateList", true);
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, StockListActivity.class);
            mActivity.startActivity(intent);
        } else if (!TextUtils.isEmpty(mAdapterDatas.get(position).getTitleTv()) && mAdapterDatas.get(position).getTitleTv().equals("资金流入")) {
            data.setTitle("资金流入");
            data.setHead2("现价");
            data.setHead3("资金流入");
            data.setHead1("股票名称");
            intent.putExtra("order", "0");
            intent.putExtra("type", "1");
            intent.putExtra("tag", "zijinliuru");
            intent.putExtra("isInOut", true);
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, StockListActivity.class);
            mActivity.startActivity(intent);
        } else if (!TextUtils.isEmpty(mAdapterDatas.get(position).getTitleTv()) && mAdapterDatas.get(position).getTitleTv().equals("资金流出")) {
            data.setTitle("资金流出");
            data.setHead2("现价");
            data.setHead3("资金流出");
            data.setHead1("股票名称");
            intent.putExtra("isInOut", true);
            intent.putExtra("tag", "zijinliuchu");
            intent.putExtra("order", "1");
            intent.putExtra("type", "2");
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, StockListActivity.class);
            mActivity.startActivity(intent);
        } else if (!TextUtils.isEmpty(mAdapterDatas.get(position).getTitleTv()) && mAdapterDatas.get(position).getTitleTv().equals("换手率")) {
            data.setTitle("换手率");
            data.setHead2("现价");
            data.setHead3("换手率");
            data.setHead1("股票名称");
            intent.putExtra("flag", "3");
            intent.putExtra("order", "1");
            intent.putExtra("type", "1");
            intent.putExtra("tag", "huanshou");
            intent.putExtra("isPlateList", true);
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, StockListActivity.class);
            mActivity.startActivity(intent);
        } else {
            if (position > 2) {

                StockDetailEntity stockDetailEntity = new StockDetailEntity();
                stockDetailEntity.setStockName(mAdapterDatas.get(position).getStockName());
                stockDetailEntity.setStockCode(mAdapterDatas.get(position).getStockNumber());
                intent.putExtra("stockIntent", stockDetailEntity);
                intent.setClass(mActivity, StockDetailActivity.class);
                mActivity.startActivity(intent);
            }
        }
    }

    @Override
    public void myTabonResume() {
        if(MarketFragment.pageIndex==1){
            isShow = true;
            timerConnct();
            startTimerTask();
        }
    }

    @Override
    public void onHiddenChanged(boolean hided) {
        super.onHiddenChanged(hided);
        if(!hided){
            timerConnct();
            startTimerTask();
        }else{
            stopTimerTask();
        }
    }

    @Override
    public void toRunConnect() {
        LogHelper.e(TAG,"toRunConnect");
        if (mMarketFragmentEditIsGoneListener != null) {
            mMarketFragmentEditIsGoneListener.gone();
        }
        timerConnct();
        startTimerTask();
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
//        LogHelper.e(TAG,"timerConnct:"+mLoadInterface);

//        notifyObservers(Helper.getTime());

//        mServiceIntent = new Intent(mActivity, HuShenService.class);
//        HuShenService.mOberservers.add(this);
//        mActivity.startService(mServiceIntent);
        request();
    }

    public void fillArrayList(ArrayList<StockInfoEntity> entities) {

        mBackgroudLayout.setVisibility(View.GONE);
        ArrayList<StockInfoEntity> beans = entities;
        if (mLoadInterface != null) {
            mLoadInterface.complited();
        }
        if (beans != null && beans.size() > 0) {
            mTopGridDatas.clear();
            mMiddleGridDatas.clear();
            mLedDatas.clear();
            mDropDatas.clear();
            mFundImportDatas.clear();
            mFundOutDatas.clear();
            mChangeHandDatas.clear();

            for (int i = 0; i < beans.size(); i++) {

                switch (i) {
                    case 0:
                        setHushenTopData(mTopGridDatas, beans.get(i));
                        break;
                    case 1:
                        setHushenMiddleData(mMiddleGridDatas, beans.get(i));
                        break;
                    case 2:
                        setHushenData(mLedDatas, beans.get(i), 1004);
                        break;
                    case 3:
                        setHushenData(mDropDatas, beans.get(i), 1004);
                        break;
                    case 4:
                        setHushenFundData(mFundImportDatas, beans.get(i), 1000);
                        break;
                    case 5:
                        setHushenFundData(mFundOutDatas, beans.get(i), 1001);
                        break;
                    case 6:
                        setHushenData(mChangeHandDatas, beans.get(i), 1003);
                        break;
                }
            }

            StockInfoEntity subTop = new StockInfoEntity();
            subTop.setType("2");
            subTop.setSubDatas(mTopGridDatas);

            StockInfoEntity subMiddle = new StockInfoEntity();
            subMiddle.setType("3");
            subMiddle.setSubDatas(mMiddleGridDatas);

            mAdapterDatas.clear();

            mAdapterDatas.add(subTop);
            mAdapterDatas.add(temp0);
            mAdapterDatas.add(subMiddle);

            mAdapterDatas.add(temp1);
            mAdapterDatas.addAll(mLedDatas);
            mAdapterDatas.add(temp2);
            mAdapterDatas.addAll(mDropDatas);
            mAdapterDatas.add(temp3);
            mAdapterDatas.addAll(mFundImportDatas);
            mAdapterDatas.add(temp4);
            mAdapterDatas.addAll(mFundOutDatas);
            mAdapterDatas.add(temp5);
            mAdapterDatas.addAll(mChangeHandDatas);

            mAdapter.setDatas(mAdapterDatas);

            String time = beans.get(0).getTime();
            if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
                int _time = Integer.valueOf(time);
//                notifyObservers(_time * 1000);
            }

        } else {

            if (mAdapterDatas == null || mAdapterDatas.size() <=0) {
                mBackgroudLayout.setVisibility(View.VISIBLE);
            }

//            notifyObservers(60 * 1000);
        }
    }

    @Override
    public void toStopConnect() {
        cancelTimer();
        LogHelper.e(TAG,"toStopConnect isshow:"+isShow);
    }

    private void cancelTimer() {
        LogHelper.e(TAG,"cancelTimer");
        stopTimerTask();
//        HuShenService.mOberservers.remove(this);
//        if (mActivity != null && mServiceIntent != null) {
//            mActivity.stopService(mServiceIntent);
//        }
    }
    public void request() {
        isCallBackSuccess = false;
        Map<String, String> params = new HashMap();
        try {
            Object []  object = new Object[7];
            Map map2 = new HashMap();
            map2.put("market","0");
            map2.put("type", "1");
            map2.put("code","10000001&20399001&20399006");
            map2.put("order", "1");

            Map map3 = new HashMap();
            map3.put("Type","1");
            map3.put("order", "1");
            map3.put("start", "0");
            map3.put("num", "6");

            Map map4 = new HashMap();
            map4.put("type", "1");
            map4.put("flag", "1");
            map4.put("asc", "1");
            map4.put("startNo", "0");
            map4.put("number", "5");


            Map map5 = new HashMap();
            map5.put("type", "1");
            map5.put("flag", "1");
            map5.put("asc", "0");
            map5.put("startNo", "0");
            map5.put("number", "5");

            Map map6 = new HashMap();
            map6.put("inout", "1");
            map6.put("order", "0");
            map6.put("startNo", "0");
            map6.put("number", "5");

            Map map7 = new HashMap();
            map7.put("inout", "2");
            map7.put("order", "1");
            map7.put("startNo", "0");
            map7.put("number", "5");

            Map map8 = new HashMap();
            map8.put("type", "1");
            map8.put("flag", "3");
            map8.put("asc", "1");
            map8.put("startNo", "0");
            map8.put("number", "5");

            object[0] = map2;
            object[1] = map3;
            object[2] = map4;
            object[3] = map5;
            object[4] = map6;
            object[5] = map7;
            object[6] = map8;


            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);
            params.put("FUNCTIONCODE","HQING005_HQING009_HQING001_HQING001_HQING008_HQING008_HQING001");
//            LogHelper.i(TAG, ""+params.toString());

        } catch (Exception e) {
            e.printStackTrace();
            ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
//            mCallbackResult.getResult(beanss, TAG);
        }
        LogHelper.e(TAG, params.toString());
        //FileUtil.URL 外网
        //FileUtil.ULR_HANGQING
        String url = ConstantUtil.getURL_HQ_HHN();
        NetWorkUtil.getInstence().okHttpForGet(TAG, url, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                LogHelper.e(TAG, e.toString());
                isCallBackSuccess = true;
                ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                fillArrayList(beanss);
//                mCallbackResult.getResult(beanss, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//                LogHelper.e(TAG, "response:" + response);
                //[{"time":"0","bytes":28,"totalCount":"3","data":[["20399001","深证成指","20161021104709","10799.802","10784.328","10802.536","10766.283"],["10000001","上证指数","20161021104706","3100.4138","3084.4578","3101.8486","3077.9968"],["20399006","创业板指","20161021104709","2195.6365","2193.2559","2202.4878","2194.0168"]],"code":"0","isOpen":"true"},{"bytes":28,"totalCount":"6","data":[["620000","建筑装饰","0.038658227913226276","11601669","中国电建","0.09984404969699638","7.050000190734863","6.409999847412109",null],["640000","机械设备","0.011260454701370095","11603012","创力集团","0.09967051103194549","13.350000381469727","12.140000343322754",null],["490000","非银金融","0.010595085189402352","27002797","第一创业","0.05111554309773026","37.220001220703125","35.40999984741211",null],["420000","交通运输","0.009183999236994915","26300240","飞力达","0.10044445461697049","12.380000114440918","11.25",null],["610000","建筑材料","0.007899407853460815","11600321","国栋建设","0.10093457410820532","5.889999866485596","5.349999904632568",null],["650000","国防军工","0.006144105851453674","26300424","航新科技","0.024856602390375692","58.959999084472656","57.529998779296875",null]],"code":"0","date":"0","isOpen":"true"},{"time":"0","bytes":28,"totalCount":"5","data":[["26300553","14.08","20.28","N集智","0.0","31991.69921875","0.44034096598625183"],["11603859","7.54","10.86","N能科   ","0.0","75839.0","0.4403182566165924"],["26300552","12.25","17.64","N万集","0.0","121728.15625","0.43999993801116943"],["26300010","19.07","19.99","立思辰","0.03538629863237294","3.07970112E8","0.04824331775307655"],["26300018","12.62","13.22","中元股份","0.04682392534863205","1.82232256E8","0.047543611377477646"]],"code":"0","isOpen":"true"},{"time":"0","bytes":28,"totalCount":"5","data":[["11600758","19.1","17.19","红阳能源","0.10136218581069889","2.02836511E8","-0.09999998658895493"],["11600076","13.75","12.87","康欣新材","0.1265634574980303","5.98428425E8","-0.06400001049041748"],["27002753","32.77","30.8","永东股份","0.1290223978919631","2.32212432E8","-0.060115996748209"],["27002591","16.78","15.87","恒大高新","0.05280024399713772","1.40934128E8","-0.054231274873018265"],["26300242","17.55","16.65","明家联合","0.052630063974291166","2.30938352E8","-0.05128203332424164"]],"code":"0","isOpen":"true"},{"bytes":150,"totalCount":5,"data":[["11601766","中国中车","964721865.0000","624804473.0000","9.29","9.8","339917392.0000"],["11601668","中国建筑","1216866601.0000","962704461.0000","6.98","7.3","254162140.0000"],["21000935","四川双马","859090145.0000","650299975.0000","24.07","26.33","208790170.0000"],["11601618","中国中冶","533886122.0000","332158537.0000","4.22","4.52","201727585.0000"],["21000567","海德股份","1175700666.0000","1032339312.0000","45.97","47.99","143361354.0000"]],"code":"0"},{"bytes":150,"totalCount":5,"data":[["11600076","康欣新材","170193079.0000","412452874.0000","13.75","12.87","-242259795.0000"],["11600149","廊坊发展","624736719.0000","850266880.0000","29.61","29.0","-225530161.0000"],["21000413","东旭光电","251313794.0000","441346054.0000","14.92","14.7","-190032260.0000"],["21000838","财信发展","181626407.0000","313079777.0000","10.8","11.06","-131453370.0000"],["11600528","中铁二局","719533280.0000","850501700.0000","13.27","13.87","-130968420.0000"]],"code":"0"},{"time":"0","bytes":27,"totalCount":"5","data":[["26300381","31.48","32.9","溢多利","0.7719567123994872","7.78218112E8","0.045108068734407425"],["27002782","29.28","31.09","可立克","0.29367981220657274","9.5578752E8","0.06181691959500313"],["26300428","31.65","30.91","四通新材","0.26335096153846155","5.1828736E8","-0.023380719125270844"],["21000567","45.97","47.99","海德股份","0.22538847215804328","1.662636032E9","0.04394171014428139"],["26300505","68.98","75.88","川金诺","0.2151948608137045","3.71195392E8","0.10002890229225159"]],"code":"0","isOpen":"true"}]

//                ObjectMapper objectMapper = JacksonMapper.getInstance();
                try {
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
//                    ArrayList<Map<String, Object>> responseValues = objectMapper.readValue(response, new ArrayList<Map<String, Object>>().getClass());
                    if (TextUtils.isEmpty(response)) {
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                        JSONObject jsonO = jsonArray.optJSONObject(i);
                        String code = (String) jsonO.optString("code");
                        if (code.equals("-5") || code.equals("-1") || code.equals("-3")) {
                            code = "-5";
                            stockInfoEntity.setCode(code);
                        } else {
                            String time = "0";
                            int bytes = 0;
                            String totalCount = "0";
                            List<List<String>> data = null;

                            time = jsonO.optString("time");
                            refleshTime = TransitionUtils.string2int(time);
                            bytes = (Integer) jsonO.optInt("bytes");

                            totalCount = jsonO.optString("totalCount");
                            JSONArray arrayT = jsonO.optJSONArray("data");

                            stockInfoEntity.setCode(code);
                            stockInfoEntity.setTime(time);
                            stockInfoEntity.setBytes(bytes);
                            stockInfoEntity.setTotalCount(totalCount);
                            stockInfoEntity.setData(arrayT);
                        }
                        beanss.add(stockInfoEntity);
                    }
                    fillArrayList(beanss);
//
                } catch (Exception e) {
                    e.printStackTrace();
                    ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                    fillArrayList(beanss);
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
        return R.layout.fragment_hushen;
    }

}
