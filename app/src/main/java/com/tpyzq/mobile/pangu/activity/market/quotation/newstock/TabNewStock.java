package com.tpyzq.mobile.pangu.activity.market.quotation.newstock;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.CalendarNewStockActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.OneKeySubscribeActivity;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhanwenbo on 2016/8/20.
 * 新股Tab页面
 */
public class TabNewStock extends BaseTabPager implements
        View.OnClickListener, ICallbackResult,AdapterView.OnItemClickListener {

    private static final String TAG = "TabNewStock";

    private NewStockEnitiy mTypeBean1;
    private NewStockEnitiy mTypeBean2;
    private ArrayList<NewStockEnitiy> mWillIntoMarketList;  //待上市
    private ArrayList<NewStockEnitiy> mNewIssueBeanList;        //新股发行数据集合
//    public static ArrayList<NewStockTimeObserver> mObserves = new ArrayList<>();
    private NewStockAdapter mAdapter;
    private WillIntoAdapter mWillIntoAdapter;
    private TodayNewStockListAdapter mTodayNewStockListAdapter;
    private MyListView mIntoMarketListView, mNewListView;

//    private Intent mServiceIntent;
    private Activity mActivity;
    private TextView mPublisNewStockTv;
    private TextView mOverNewStockBtn;
    private NewStockEnitiy mCalendarEntitiy;
    private LinearLayout mBackgroud;
    private MyListView mTodayNewStockListView;
    private LinearLayout mShenGouBtnLayout;


    public TabNewStock(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;

        mBackgroud = (LinearLayout) view.findViewById(R.id.bgLayout);
        mShenGouBtnLayout = (LinearLayout) view.findViewById(R.id.ll_OneTimeShenGou);

        mTodayNewStockListView= (MyListView) view.findViewById(R.id.todayNewStockListView);
        mTodayNewStockListAdapter = new TodayNewStockListAdapter();
        mTodayNewStockListView.setAdapter(mTodayNewStockListAdapter);

        mIntoMarketListView = (MyListView) view.findViewById(R.id.intToMarketListView);
        mNewListView = (MyListView) view.findViewById(R.id.NewListView);

        mWillIntoAdapter = new WillIntoAdapter(this);
        mIntoMarketListView.setAdapter(mWillIntoAdapter);

        mAdapter = new NewStockAdapter(this);
        mNewListView.setAdapter(mAdapter);

        mPublisNewStockTv = (TextView) view.findViewById(R.id.newStockHint);
        mOverNewStockBtn = (TextView) view.findViewById(R.id.calendarBtnColor);
        view.findViewById(R.id.calendarLayout).setOnClickListener(this);
        mOverNewStockBtn.setOnClickListener(this);

        mCalendarEntitiy = new NewStockEnitiy();
        mWillIntoMarketList = new ArrayList<>();
        mNewIssueBeanList = new ArrayList<>();

        mTypeBean1 = new NewStockEnitiy();
        mTypeBean1.setAdapterType("0");

        mTypeBean2 = new NewStockEnitiy();
        mTypeBean2.setAdapterType("1");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calendarLayout:
                Intent intent = new Intent();
                intent.putExtra("entiity", mCalendarEntitiy);
                intent.setClass(mActivity, CalendarNewStockActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.calendarBtnColor:
                Intent intent1 = new Intent();
                intent1.putExtra("pageindex", TransactionLoginActivity.PAGE_INDEX_OneKeySubscribe);
                if (!Db_PUB_USERS.isRegister()) {
                    intent1.setClass(mActivity, ShouJiZhuCeActivity.class);
                } else if (!Db_PUB_USERS.islogin()) {
                    intent1.setClass(mActivity, TransactionLoginActivity.class);
                } else {
                    intent1.setClass(mActivity, OneKeySubscribeActivity.class);
                }
                mActivity.startActivity(intent1);
                break;
            case R.id.daishangshiTxte:
                intent = new Intent(mActivity, NewStockListActivity.class);
                intent.putExtra("title_tv", "待上市");
                intent.putExtra("newStockFlag", "0");
                mActivity.startActivity(intent);
                break;
            case R.id.XinGuText:
                intent = new Intent(mActivity, NewStockListActivity.class);
                intent.putExtra("title_tv", "次新股行情");
                intent.putExtra("newStockFlag", "1");
                mActivity.startActivity(intent);
                break;
        }
    }

    /**
     * 新股日历网络请求
     */
//    private void newStockCalenderConnect() {

//        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
//        simpleRemoteControl.setCommand(new ToNewStockCalenderConnect(new NewStockCalenderConnect(TAG)));
//        simpleRemoteControl.startConnect();
//    }

    /**
     * 即将上市网络请求
     */
//    private void intoWillMarketConnect() {

//        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
//        simpleRemoteControl.setCommand(new ToWillInToMarketConnect(new WillInToMarketConnect(TAG)));
//        simpleRemoteControl.startConnect();
//    }

    @Override
    public void getResult(Object result, String tag) {

        if (mLoadInterface != null) {
            mLoadInterface.complited();
        }

        if ("NewStockCalenderConnect".equals(tag)) {
            NewStockEnitiy bean = (NewStockEnitiy) result;

            if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                mCalendarEntitiy = bean;
                mPublisNewStockTv.setText("今日发行" + bean.getNewStockSize() + "只新股");

                ArrayList<NewStockEnitiy.DataBeanToday> datas = getTodayListData(bean);
                mTodayNewStockListAdapter.setDatas(datas);

                if (datas == null || datas.size() <= 1) {
                    mTodayNewStockListView.setVisibility(View.GONE);
                    mShenGouBtnLayout.setVisibility(View.GONE);
                }

                mTodayNewStockListView.setOnItemClickListener(new TodayNewStockOnItemClick(mActivity, datas));
                mOverNewStockBtn.setVisibility(View.VISIBLE);
            } else {
                mOverNewStockBtn.setVisibility(View.GONE);
            }

        } else if ("WillInToMarketConnect".equals(tag)) {
            NewStockEnitiy bean = (NewStockEnitiy) result;

            if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                mWillIntoMarketList.clear();

                NewStockEnitiy enptyBean = new NewStockEnitiy();
                enptyBean.setAdapterType("0");
                mWillIntoMarketList.add(enptyBean);

                if (bean.getData().size() >= 2) {
                    for (int i = 0; i < 2; i++) {
                        setWillMarketData(bean, i);
                    }
                } else {
                    for (int i = 0; i < bean.getData().size(); i++) {
                        setWillMarketData(bean, i);
                    }
                }
                mIntoMarketListView.setOnItemClickListener(willOnItemClick);
                mWillIntoAdapter.setDatas(mWillIntoMarketList);
                if (mWillIntoMarketList != null && mWillIntoMarketList.size() <= 1) {
                    mIntoMarketListView.setVisibility(View.GONE);
                }
            }
        }
    }
    private AdapterView.OnItemClickListener willOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != 0) {
                Intent intent = new Intent();
                intent.putExtra("name", mWillIntoMarketList.get(position).getIsSueNameBbrOnlIne());
                intent.putExtra("number", mWillIntoMarketList.get(position).getSecuCode());
                intent.setClass(mActivity, com.tpyzq.mobile.pangu.activity.trade.stock.PublishNewStockDetail.class);
                mActivity.startActivity(intent);
            }
        }
    };
    private ArrayList<NewStockEnitiy.DataBeanToday> getTodayListData(NewStockEnitiy bean) {
        if (bean == null ) {
            return null;
        }
//
        ArrayList<NewStockEnitiy.DataBeanToday> beans = new ArrayList<>();
//
        NewStockEnitiy.DataBeanToday _subBean = new NewStockEnitiy.DataBeanToday();
        _subBean.setSOURCETYPE("0");
        beans.add(_subBean);

        for (NewStockEnitiy.DataBeanToday _bean : bean.getData()) {
            try {
                if ("N".equalsIgnoreCase(_bean.getISTODAY())) {
                    _bean.setSOURCETYPE("1");
                    beans.add(_bean);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogHelper.e(TAG, e.toString());
            }
        }
        return beans;
    }

    private void setWillMarketData(NewStockEnitiy bean, int i) {
        NewStockEnitiy _bean = new NewStockEnitiy();
        _bean.setIsSueNameBbrOnlIne(bean.getData().get(i).getISSUENAMEABBR_ONLINE());
        _bean.setSecuCode(bean.getData().get(i).getSECUCODE());
        _bean.setlIstaAte(bean.getData().get(i).getLISTDATE());
        DecimalFormat df = new DecimalFormat("0.00");
        String ISSUEPRICE = bean.getData().get(i).getISSUEPRICE();

        if (Helper.isDecimal(ISSUEPRICE) || Helper.isENum(ISSUEPRICE)) {
            _bean.setIssueprICE(df.format(Double.parseDouble(ISSUEPRICE)) );  //价格
        } else {
            _bean.setIssueprICE(ISSUEPRICE );  //价格
        }


        DecimalFormat format2 = new DecimalFormat("#0.00%");
        String StrNum = bean.getData().get(i).getLOTRATEONLINE();
        _bean.setAdapterType("1");

        if (Helper.isDecimal(StrNum) || Helper.isENum(StrNum)) {
            _bean.setLoTrateonlIne(format2.format(Double.parseDouble(StrNum)));
        } else {
            _bean.setLoTrateonlIne(StrNum);
        }


        _bean.setTypeTag("daishangshi");

        mWillIntoMarketList.add(_bean);
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
        request();
//        mServiceIntent = new Intent(mActivity, NewStockService.class);
//        NewStockService.mOberservers.add(this);
//        notifyObservers(Helper.getTime());
//        mActivity.startService(mServiceIntent);

    }

    public void myTabonResume() {
        if(MarketFragment.pageIndex==1){
            timerConnct();
            startTimerTask();
        }
    }

    @Override
    public void toRunConnect() {
        if (mMarketFragmentEditIsGoneListener != null) {
            mMarketFragmentEditIsGoneListener.visible(TAG);
        }

        if (mLoadInterface != null) {
            mLoadInterface.loading();
        }

//        newStockCalenderConnect();
//        intoWillMarketConnect();
        request();
        startTimerTask();
//        if (mServiceIntent != null) {
//            NewStockService.mOberservers.add(this);
//            mActivity.startService(mServiceIntent);
//        } else {
//            timerConnct();
//        }
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
//        NetWorkUtil.cancelSingleRequestByTag(TAG);

//        NewStockService.mOberservers.remove(this);
//        if (mActivity != null && mServiceIntent != null) {
//            mActivity.stopService(mServiceIntent);
//        }
    }

    public void fillListData(ArrayList<NewStockEnitiy> entities) {
        if (mLoadInterface != null) {
            mLoadInterface.complited();
        }
        mNewIssueBeanList = entities;

        if (mNewIssueBeanList != null && mNewIssueBeanList.size() > 0) {

            String time = mNewIssueBeanList.get(0).getConnectTime();
//            if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
//                int _time = Integer.valueOf(time);
//                notifyObservers(_time * 1000);
            }

//        } else {
//            notifyObservers(60 * 1000);
//        }

        mNewListView.setOnItemClickListener(this);
        if (mNewIssueBeanList!= null && mNewIssueBeanList.size() == 1) {
            mNewListView.setVisibility(View.GONE);
        }
        mAdapter.setDatas(mNewIssueBeanList);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position != 0) {
            Intent intent = new Intent();
            StockDetailEntity data = new StockDetailEntity();
            data.setStockName(mNewIssueBeanList.get(position).getName());
            data.setStockCode(mNewIssueBeanList.get(position).getNumber());

            intent.putExtra("stockIntent", data);

            intent.setClass(this.mActivity, StockDetailActivity.class);
            this.mActivity.startActivity(intent);
        }
    }
    private void request(){
        requestNewIssue();
        requestWillInToMarket();
        requestNewStkCalender();
    }
    private void requestNewStkCalender() {
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "100213");
        map.put("token", "");
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//                LogHelper.e(TAG, "requestNewStkCalender:"+response);
                //{"totalcount":"12","data":[{"SECUCODE":"21300557","ONLINESTARTDATE":"2016-10-21","ISSUENAMEABBR_ONLINE":"理工光科","APPLYMAXONLINE":"14000","ISTODAY":"N","DILUTEDPERATIO":"22.99000000","APPLYCODEONLINE":"300557","PREPAREDLISTEXCHANGE":"90","ISSUEPRICE":"13.9100","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"140000"},{"SECUCODE":"21002818","ONLINESTARTDATE":"2016-10-25","ISSUENAMEABBR_ONLINE":"富 森 美","APPLYMAXONLINE":"13000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"002818","PREPAREDLISTEXCHANGE":"90","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"130000"},{"SECUCODE":"11601882","ONLINESTARTDATE":"2016-10-25","ISSUENAMEABBR_ONLINE":"海天精工","APPLYMAXONLINE":"15000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"780882","PREPAREDLISTEXCHANGE":"83","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"150000"},{"SECUCODE":"21300556","ONLINESTARTDATE":"2016-10-26","ISSUENAMEABBR_ONLINE":"丝路视觉","APPLYMAXONLINE":"11000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"300556","PREPAREDLISTEXCHANGE":"90","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"110000"},{"SECUCODE":"11603323","ONLINESTARTDATE":"2016-10-26","ISSUENAMEABBR_ONLINE":"吴江银行","APPLYMAXONLINE":"33000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"732323","PREPAREDLISTEXCHANGE":"83","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"330000"},{"SECUCODE":"21300558","ONLINESTARTDATE":"2016-10-27","ISSUENAMEABBR_ONLINE":"贝达药业","APPLYMAXONLINE":"12000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"300558","PREPAREDLISTEXCHANGE":"90","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"120000"},{"SECUCODE":"11603203","ONLINESTARTDATE":"2016-10-27","ISSUENAMEABBR_ONLINE":"快克股份","APPLYMAXONLINE":"9000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"732203","PREPAREDLISTEXCHANGE":"83","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"90000"},{"SECUCODE":"21002819","ONLINESTARTDATE":"2016-10-28","ISSUENAMEABBR_ONLINE":"东方中科","APPLYMAXONLINE":"11000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"002819","PREPAREDLISTEXCHANGE":"90","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"110000"},{"SECUCODE":"11603060","ONLINESTARTDATE":"2016-10-28","ISSUENAMEABBR_ONLINE":"国检集团","APPLYMAXONLINE":"22000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"732060","PREPAREDLISTEXCHANGE":"83","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"220000"},{"SECUCODE":"11603319","ONLINESTARTDATE":"2016-10-31","ISSUENAMEABBR_ONLINE":"湘 油 泵","APPLYMAXONLINE":"7000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"732319","PREPAREDLISTEXCHANGE":"83","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"70000"},{"SECUCODE":"11603556","ONLINESTARTDATE":"2016-10-31","ISSUENAMEABBR_ONLINE":"海兴电力","APPLYMAXONLINE":"37000","ISTODAY":"W","DILUTEDPERATIO":"","APPLYCODEONLINE":"732556","PREPAREDLISTEXCHANGE":"83","ISSUEPRICE":"","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"370000"},{"SECUCODE":"11601229","ONLINESTARTDATE":"2016-11-02","ISSUENAMEABBR_ONLINE":"上海银行","APPLYMAXONLINE":"180000","ISTODAY":"W","DILUTEDPERATIO":"8.26000000","APPLYCODEONLINE":"780229","PREPAREDLISTEXCHANGE":"83","ISSUEPRICE":"17.7700","WEIGHTEDPERATIO":"","APPLYMAXONLINEMONEY":"1800000"}],"code":"0","msg":"查询成功"}
                NewStockEnitiy bean = new NewStockEnitiy();
                try {
                    ArrayList<NewStockEnitiy> beanss = new ArrayList<NewStockEnitiy>();
//                    JSONArray array = new JSONArray(response);
//                    if(array==null||array.length()==0){
//                        fillListData(beanss);
//                        return;
//                    }
                    JSONObject jsonObject = new JSONObject(response);

                    String totalcount = jsonObject.optString("totalcount");;
                    if (null != totalcount) {
                        bean.setTotalcount(totalcount);
                    }
                    String code = jsonObject.optString("code");
                    if (null != code) {
                        bean.setCode(code);
                    }
                    String msg = jsonObject.optString("msg");;
                    if (null != msg) {
                        bean.setMsg(msg);
                    }

                    JSONArray data = jsonObject.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        List<NewStockEnitiy.DataBeanToday> dataBeanTodays = new ArrayList<NewStockEnitiy.DataBeanToday>();
                        int publishNum = 0;
                        for (int i = 0;i<data.length();i++) {
                            JSONObject item = data.optJSONObject(i);

                                NewStockEnitiy.DataBeanToday dataBeanToday = new NewStockEnitiy.DataBeanToday();

                                String secucode = item.optString("SECUCODE");
                                if (null != secucode) { //证券代码
                                    dataBeanToday.setSECUCODE(secucode);
                                }

                                String onlineStartdate = item.optString("ONLINESTARTDATE");
                                if (null != onlineStartdate) {//上市时间
                                    dataBeanToday.setONLINESTARTDATE(onlineStartdate);
                                }

                                String issueNameAbbr_OnLine =item.optString("ISSUENAMEABBR_ONLINE");
                                if (null != issueNameAbbr_OnLine) {//股票名称 上网发行申购简称
                                    dataBeanToday.setISSUENAMEABBR_ONLINE(issueNameAbbr_OnLine);
                                }

                                String  applyMaxOnLine=item.optString("APPLYMAXONLINE");
                                if (null != applyMaxOnLine) {//上网发行申购上限(股)
                                    dataBeanToday.setAPPLYMAXONLINE(applyMaxOnLine);
                                }

                                String isToday =item.optString("ISTODAY");
                                if (null != isToday) {//W N
                                    dataBeanToday.setISTODAY(isToday);
                                    if ("N".equals(isToday)) {
                                        publishNum++;
                                    }
                                }
                                String dilutedperatio = item.optString("DILUTEDPERATIO");
                                if (null != dilutedperatio) {//发行市盈率(全面摊薄)(倍)
                                    dataBeanToday.setDILUTEDPERATIO(dilutedperatio);
                                }

                                String applyCodeOnLine =  item.optString("APPLYCODEONLINE");
                                if (null != applyCodeOnLine) {//上网发行申购代码
                                    dataBeanToday.setAPPLYCODEONLINE(applyCodeOnLine);
                                }

                                String preparedListExchange = item.optString("PREPAREDLISTEXCHANGE");
                                if (null != preparedListExchange) {
                                    dataBeanToday.setPREPAREDLISTEXCHANGE(preparedListExchange);
                                }

                                String issuePrice = item.optString("ISSUEPRICE");
                                if (null != issuePrice) {//每股发行价(元)
                                    dataBeanToday.setISSUEPRICE(issuePrice);
                                }

                                String weightEndPeratio = item.optString("WEIGHTEDPERATIO");
                                if (null != weightEndPeratio) {//发行市盈率(加权平均)(倍)
                                    dataBeanToday.setWEIGHTEDPERATIO(weightEndPeratio);
                                }

                                String applyMaxOnLineMoney = item.optString("APPLYMAXONLINEMONEY");
                                if (null != applyMaxOnLineMoney) {//顶格申购市值（ ISSUEPRICE* APPLYMAXONLINE）
                                    dataBeanToday.setAPPLYMAXONLINEMONEY(applyMaxOnLineMoney);
                                }
                                dataBeanTodays.add(dataBeanToday);
                            }
                        bean.setNewStockSize(publishNum);
                        bean.setData(dataBeanTodays);
                    }
                    getResult(bean, "NewStockCalenderConnect");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void requestWillInToMarket() {
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "100211");
        map.put("token", "");
        map.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//                LogHelper.e(TAG, "requestWillInToMarket:"+response);
                NewStockEnitiy bean = new NewStockEnitiy();
                try {
                    ArrayList<NewStockEnitiy> beanss = new ArrayList<NewStockEnitiy>();
//                    JSONArray array = new JSONArray(response);
//                    if(array==null||array.length()==0){
//                        fillListData(beanss);
//                        return;
//                    }
                    JSONObject jsonObject = new JSONObject(response);

                    String totalcount = jsonObject.optString("totalcount");;
                    if (null != totalcount) {
                        bean.setTotalcount(totalcount);
                    }
                    String code = jsonObject.optString("code");
                    if (null != code) {
                        bean.setCode(code);
                    }
                    String msg = jsonObject.optString("msg");;
                    if (null != msg) {
                        bean.setMsg(msg);
                    }

                    JSONArray data = jsonObject.optJSONArray("data");

                    if (data != null && data.length() > 0) {
                        List<NewStockEnitiy.DataBeanToday> dataBeanTodays = new ArrayList<NewStockEnitiy.DataBeanToday>();
                        for (int i=0;i<data.length();i++) {
                                NewStockEnitiy.DataBeanToday dataBeanToday = new NewStockEnitiy.DataBeanToday();
                                JSONObject item = data.getJSONObject(i);
                                String listDate = item.optString("LISTDATE");
                                if (null != listDate) {
                                    dataBeanToday.setLISTDATE(listDate);
                                }
                                String lotrateonLine =item.optString("LOTRATEONLINE");
                                if (null != lotrateonLine) {
                                    dataBeanToday.setLOTRATEONLINE(lotrateonLine);
                                }

                                String secucode = item.optString("SECUCODE");
                                if (null != secucode) {
                                    dataBeanToday.setSECUCODE(secucode);
                                }

                                String onlineStartdate =item.optString("ONLINESTARTDATE");;
                                if (null != onlineStartdate) {
                                    dataBeanToday.setONLINESTARTDATE(onlineStartdate);
                                }

                                String issueNameAbbr_OnLine =item.optString("ISSUENAMEABBR_ONLINE");
                                if (null != issueNameAbbr_OnLine) {
                                    dataBeanToday.setISSUENAMEABBR_ONLINE(issueNameAbbr_OnLine);
                                }

                                String applyMaxOnLine =item.optString("APPLYMAXONLINE");
                                if (null !=applyMaxOnLine) {//上网发行申购上限(股)
                                    dataBeanToday.setAPPLYMAXONLINE(applyMaxOnLine);
                                }

                                String isToday =item.optString("ISTODAY");
                                if (null != isToday) {//W N
                                    dataBeanToday.setISTODAY(isToday);
                                }

                                String dilutedperatio = item.optString("DILUTEDPERATIO");
                                if (null != dilutedperatio) {//发行市盈率(全面摊薄)(倍)
                                    dataBeanToday.setDILUTEDPERATIO(dilutedperatio);
                                }
                                String applyCodeOnLine = item.optString("APPLYCODEONLINE");
                                if (null != applyCodeOnLine) {//上网发行申购代码
                                    dataBeanToday.setAPPLYCODEONLINE(applyCodeOnLine);
                                }

                                String preparedListExchange = item.optString("PREPAREDLISTEXCHANGE");
                                if (null != preparedListExchange) {
                                    dataBeanToday.setPREPAREDLISTEXCHANGE(preparedListExchange);
                                }

                                String issuePrice = item.optString("ISSUEPRICE");
                                if (null != issuePrice) {//每股发行价(元)
                                    dataBeanToday.setISSUEPRICE(issuePrice);
                                }

                                String weightEndPeratio = item.optString("WEIGHTEDPERATIO");//发行市盈率(加权平均)(倍)
                                if (null != weightEndPeratio) {
                                    dataBeanToday.setWEIGHTEDPERATIO(weightEndPeratio);
                                }
                                String applyMaxOnLineMoney = item.optString("APPLYMAXONLINEMONEY");//顶格申购市值（ ISSUEPRICE* APPLYMAXONLINE）
                                if (null != applyMaxOnLineMoney) {
                                    dataBeanToday.setAPPLYMAXONLINEMONEY(applyMaxOnLineMoney);
                                }
                                dataBeanTodays.add(dataBeanToday);
                        }
                        bean.setData(dataBeanTodays);
                    }
                    getResult(bean, "WillInToMarketConnect");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestNewIssue() {
        isCallBackSuccess = false;
        Map<String, String> map = new HashMap();
        try {
            Object[] obj = new Object[1];
//        "2", "1", "0", "5"
            Map map2 = new HashMap();
            map2.put("order", "2");
            map2.put("asc", "1");
            map2.put("start", "0");
            map2.put("number", "5");

            obj[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(obj);
            map.put("PARAMS", strJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("FUNCTIONCODE", "HQING014");
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL, map, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<NewStockEnitiy> beans = new ArrayList<NewStockEnitiy>();
                fillListData(beans);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogHelper.e(TAG,"response:"+response);
                ArrayList<NewStockEnitiy> beans = new ArrayList<NewStockEnitiy>();
                NewStockEnitiy enpty = new NewStockEnitiy();
                enpty.setAdapterType("0");
                beans.add(enpty);

                if ("[null]".equals(response)) {
                    fillListData(beans);
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillListData(beans);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);
                    String code = jsonObject.optString("code");
                    String totalCount = jsonObject.optString("totalCount");
                    String time = jsonObject.optString("time");
                    refleshTime = TransitionUtils.string2int(time);
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i=0;i<jsonArray.length();i++){
                            NewStockEnitiy _bean = new NewStockEnitiy();
                            JSONArray array1 = jsonArray.optJSONArray(i);
                            _bean.setConnectTime(time);
                            _bean.setCode(code);
                            _bean.setTotalcount(totalCount);
                            _bean.setAdapterType("1");
                            _bean.setNumber(array1.optString(0));//代码
                            String stockName = array1.optString(1);
                            if (null != stockName) {
                                _bean.setName(stockName);  //名称
                            }
                            String date = array1.optString(2);
                            if (null != date) {
                                Date _date = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
                                SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
                                date = sdf.format(_date);
                                _bean.setmTime(date);
                            }
                            String priceNewPric = array1.optString(4);
                            if (null != priceNewPric) {
                                DecimalFormat df = new DecimalFormat("#0.00");
                                priceNewPric = df.format(Double.valueOf(priceNewPric));
                                _bean.setNewPrice(priceNewPric);  //价格
                            }

                            String price_CNY = array1.optString(5);
                            if (null != price_CNY) {
                                double _price_CNY = Double.parseDouble(price_CNY);
                                DecimalFormat df = new DecimalFormat("#0.00%");
                                price_CNY = df.format(_price_CNY);
                                _bean.setAmountOfIncrease(price_CNY);
                            }
                            String close = array1.optString(6);
                            if (null != close) {
                                if (!TextUtils.isEmpty(priceNewPric)) {
                                    DecimalFormat df = new DecimalFormat("#0.00");
                                    if (Helper.isDecimal(priceNewPric) && Helper.isDecimal(close)) {
                                        double d = Double.parseDouble(priceNewPric)- Double.parseDouble(close);
                                        String zdz = df.format(d);
                                        _bean.setZdz(zdz);
                                    }
                                }
                            }
                            beans.add(_bean);
                        }
                    }
                    fillListData(beans);
                } catch (Exception e) {
                    e.printStackTrace();
                    ArrayList<NewStockEnitiy> beans1 = new ArrayList<NewStockEnitiy>();
                    fillListData(beans1);
                }
            }
        });
    }
    private int refleshCount = 0;
    private Timer mTimer;
    private int refleshTime = 0;
    private boolean isCallBackSuccess = false;
    ///新股 只刷新 次新股表现，其他2项不刷新
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
                            loadingHandler.sendEmptyMessage(0);
                            requestNewIssue();
                        }
                    }else{
                        refleshCount = 0;
                        loadingHandler.sendEmptyMessage(0);
                        requestNewIssue();
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
        return R.layout.fragment_new_stock;
    }
}
