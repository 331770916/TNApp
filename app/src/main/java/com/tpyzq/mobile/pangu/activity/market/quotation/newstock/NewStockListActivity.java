package com.tpyzq.mobile.pangu.activity.market.quotation.newstock;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.PublishNewStockDetail;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/15.
 * 新股列表
 */
public class NewStockListActivity extends BaseActivity implements View.OnClickListener,
        ListViewScroller.ListViewScrollerListener, IListViewScrollSubject,AdapterView.OnItemClickListener {

    private final String TAG = "NewStockListActivity";
    private ProgressBar mProgressBar;
    private ListView mListView;
    private String mFlag;
    private NewStockListAdapter mNewStockListAdapter;
    private ArrayList<NewStockEnitiy> mWillIntoMarketList;
    private ArrayList<IListViewScrollObserver> mObservers;

//    public static ArrayList<NewStockListTimeObserver> mObserves = new ArrayList<>();

    private boolean doSelectMiddle;         //是否第一次加载list列表
    private int mNetTotalCount;
    private String mStart;

    private Intent mServiceIntent;
    private boolean mJudegeGesture;
    private ArrayList<NewStockEnitiy> mNewIssueBeanList;

    @Override
    public void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title_tv");
        mFlag = intent.getStringExtra("newStockFlag");
        mStart = "0";
        mWillIntoMarketList = new ArrayList<>();
        mObservers = new ArrayList<>();

        findViewById(R.id.newstock_back).setOnClickListener(this);
        findViewById(R.id.newstock_search).setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.newstock_progress);
        mListView = (ListView) findViewById(R.id.newStockList);
        mNewStockListAdapter = new NewStockListAdapter();
        TextView titlTv = (TextView) findViewById(R.id.newstock_title);
        TextView mHead1 = (TextView) findViewById(R.id.newStock_head1);
        TextView mHead2 = (TextView) findViewById(R.id.newStock_head2);
        TextView mHead3 = (TextView) findViewById(R.id.newStock_head3);
        TextView mHead4 = (TextView) findViewById(R.id.newStock_head4);

        if (!TextUtils.isEmpty(title)) {
            titlTv.setText(title);
        }

        mListView.setAdapter(mNewStockListAdapter);
        mListView.setOnScrollListener(new ListViewScroller(TAG, mListView, mStart, NewStockListActivity.this, NewStockListActivity.this));

        if ("0".equals(mFlag)) {
            mHead1.setText("名称代码");
            mHead2.setText("发行价");
            mHead3.setText("中签率");
            mHead4.setText("上市日期");

            intoWillMarketConnect();
        } else {
            mHead1.setText("名称代码");
            mHead2.setText("上市日期");
            mHead3.setText("现价");
            mHead4.setText("累计涨幅");

//            intoNewStockMarketConnect();
        }

        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if ("0".equals(mFlag)) {
            Intent intent = new Intent();
            intent.putExtra("name", mWillIntoMarketList.get(position).getIsSueNameBbrOnlIne());
            intent.putExtra("number", mWillIntoMarketList.get(position).getSecuCode());
            intent.setClass(NewStockListActivity.this, PublishNewStockDetail.class);
            NewStockListActivity.this.startActivity(intent);
        } else {
            Intent intent = new Intent();
            StockDetailEntity data = new StockDetailEntity();
            data.setStockName(mNewIssueBeanList.get(position).getName());
            data.setStockCode(mNewIssueBeanList.get(position).getNumber());

            intent.putExtra("stockIntent", data);

            intent.setClass(this, StockDetailActivity.class);
            NewStockListActivity.this.startActivity(intent);
        }
    }

    /**
     * 待上市网络请求
     */
    private void intoWillMarketConnect() {
        mProgressBar.setVisibility(View.VISIBLE);
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "100211");
        map.put("token", "");
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_HS(), map, new StringCallback() {
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
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<NewStockEnitiy>() {
                    }.getType();

                    NewStockEnitiy bean = gson.fromJson(response, type);
                    bean.setAdapterType("3");

                    if (bean.getData() != null && bean.getData().size() > 0) {

                        for (int i = 0; i < bean.getData().size(); i++) {
                            setWillMarketData(bean, i);
                            mProgressBar.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setWillMarketData(NewStockEnitiy bean, int i) {
        NewStockEnitiy _bean = new NewStockEnitiy();
        _bean.setIsSueNameBbrOnlIne(bean.getData().get(i).getISSUENAMEABBR_ONLINE());
        _bean.setSecuCode(bean.getData().get(i).getSECUCODE());
        _bean.setlIstaAte(bean.getData().get(i).getLISTDATE());
        DecimalFormat df = new DecimalFormat("#0.00");
        String ISSUEPRICE=bean.getData().get(i).getISSUEPRICE();
        _bean.setIssueprICE(df.format(Double.parseDouble(ISSUEPRICE)));  //价格
        _bean.setAdapterType("3");

        String StrNum = bean.getData().get(i).getLOTRATEONLINE();

        if (Helper.isDecimal(StrNum)) {
            DecimalFormat df2 = new DecimalFormat("#0.00%");
            _bean.setLoTrateonlIne(df2.format(Double.parseDouble(StrNum)));
        } else {
            _bean.setLoTrateonlIne(bean.getData().get(i).getLOTRATEONLINE());
        }

        mWillIntoMarketList.add(_bean);
        mNewStockListAdapter.setDatas(mWillIntoMarketList);
    }
    Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    };
    /**
     * 新股行情网络请求
     */
    private void intoNewStockMarketConnect() {
        loadingHandler.sendEmptyMessage(0);
        isCallBackSuccess = false;
        Map<String, String> map = new HashMap();
        try {
            Object[] obj = new Object[1];
//        "2", "1", "0", "5"
            Map map2 = new HashMap();
            map2.put("order", "2");
            map2.put("asc", "1");
            map2.put("start", mStart);
            map2.put("number", "30");

            obj[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(obj);
            map.put("PARAMS", strJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("FUNCTIONCODE", "HQING014");

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                fillStockList(null);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                if (TextUtils.isEmpty(response)) {
                    fillStockList(null);
                    return;
                }
                LogHelper.e(TAG, "response:"+response);
                ArrayList<NewStockEnitiy> beanss = new ArrayList<NewStockEnitiy>();
                //{"totalcount":"9","data":[{"LOTRATEONLINE":"0.001279910000000","SECUCODE":"11600926","ISSUENAMEABBR_ONLINE":"杭银申购","LISTDATE":"","APPLYCODEONLINE":"730926","ISSUEPRICE":"14.3900"},{"LOTRATEONLINE":"0.000256916000000","SECUCODE":"21002816","ISSUENAMEABBR_ONLINE":"和科达","LISTDATE":"","APPLYCODEONLINE":"002816","ISSUEPRICE":"8.2900"},{"LOTRATEONLINE":"0.000423150000000","SECUCODE":"11603667","ISSUENAMEABBR_ONLINE":"五洲申购","LISTDATE":"","APPLYCODEONLINE":"732667","ISSUEPRICE":"8.8000"},{"LOTRATEONLINE":"0.000247478000000","SECUCODE":"21002817","ISSUENAMEABBR_ONLINE":"黄山胶囊","LISTDATE":"","APPLYCODEONLINE":"002817","ISSUEPRICE":"13.8800"},{"LOTRATEONLINE":"0.000454465000000","SECUCODE":"11603258","ISSUENAMEABBR_ONLINE":"电魂申购","LISTDATE":"","APPLYCODEONLINE":"732258","ISSUEPRICE":"15.6200"},{"LOTRATEONLINE":"0.000429243000000","SECUCODE":"11603888","ISSUENAMEABBR_ONLINE":"新华申购","LISTDATE":"","APPLYCODEONLINE":"732888","ISSUEPRICE":"27.6900"},{"LOTRATEONLINE":"0.000161346286000","SECUCODE":"21300559","ISSUENAMEABBR_ONLINE":"佳发安泰","LISTDATE":"","APPLYCODEONLINE":"300559","ISSUEPRICE":"17.5600"},{"LOTRATEONLINE":"0.000155693400000","SECUCODE":"11603716","ISSUENAMEABBR_ONLINE":"塞力申购","LISTDATE":"","APPLYCODEONLINE":"732716","ISSUEPRICE":"26.9100"},{"LOTRATEONLINE":"0.000158985319000","SECUCODE":"21300560","ISSUENAMEABBR_ONLINE":"中富通","LISTDATE":"","APPLYCODEONLINE":"300560","ISSUEPRICE":"10.2600"}],"code":"0","msg":"查询成功"}

                try {
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
//                        fillListData(beanss);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);
                    String totalcount = jsonObject.optString("totalcount");
                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("time");
                    refleshTime = TransitionUtils.string2int(time);
                    JSONArray data = jsonObject.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        ArrayList<NewStockEnitiy> beans = new ArrayList<NewStockEnitiy>();
                        for (int i = 0;i<data.length();i++) {
                            NewStockEnitiy _bean = new NewStockEnitiy();
                            JSONArray subItem = data.optJSONArray(i);
                            String socktNumber = "";
                            if (null != subItem.optString(0)) {
                                socktNumber = String.valueOf(subItem.optString(0));
                                _bean.setNumber(socktNumber);//代码
                            }
                            String stockName = "";
                            if (null != subItem.optString(1)) {
                                stockName = String.valueOf(subItem.optString(1));
                                _bean.setName(stockName);  //名称
                            }

                            String date = "";
                            if (null != subItem.optString(2)) {
                                date = String.valueOf(subItem.optString(2));
                                Date _date = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
                                SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
                                date = sdf.format(_date);
                                _bean.setmTime(date);
                            }

                            String priceNewPric = "";

                            if (null != subItem.optString(4)) {
                                priceNewPric = String.valueOf(subItem.optString(4));
                                DecimalFormat df = new DecimalFormat("#0.00");
                                priceNewPric = df.format(Double.valueOf(priceNewPric));
                                _bean.setNewPrice(priceNewPric);  //价格
                            }

                            String price_CNY = "";

                            if (null != subItem.optString(5)) {
                                price_CNY = String.valueOf(subItem.optString(5));
                                double _price_CNY = Double.parseDouble(price_CNY);
                                DecimalFormat df = new DecimalFormat("#0.00%");
                                price_CNY = df.format(_price_CNY);
                                _bean.setAmountOfIncrease(price_CNY);
                            }

                            String close = "";
                            if (null != subItem.optString(6)) {
                                close = String.valueOf(subItem.optString(6));
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
                        fillStockList(beans);
                    }
                } catch (Exception e) {
                    fillStockList(null);
                    e.printStackTrace();
                }
            }
        });
    }


    private void reStartConnect() {

//        String [] keys = {"startNo"};
//        String [] values = {mStart};
//        NewStockListService.handler.sendMessage(sendMessage(keys, values));
//        if (mServiceIntent != null) {
//            NewStockListActivity.this.startService(mServiceIntent);
//        }
        intoNewStockMarketConnect();
    }

    private void cancel() {
//        NewStockListService.mOberservers.remove(this);
//        if (mServiceIntent != null) {
//            stopService(mServiceIntent);
//        }
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
//        LogHelper.e(TAG,"onResume");
        if("1".equals(mFlag)){
            intoNewStockMarketConnect();
            startTimerTask();
        }
//        cancel();
//        NewStockListService.mOberservers.add(this);
//        reStartConnect();
    }

    @Override
    protected void onPause() {
//        LogHelper.e(TAG,"onPause");
        super.onPause();
        if("1".equals(mFlag)){
            stopTimerTask();
        }
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
        if ("1".equals(mFlag)) {
            mStart = startNumber;
            cancel();
//            NewStockListService.mOberservers.add(this);
            startTimerTask();
            reStartConnect();

        }
    }

    @Override
    public void lastPage(String startNumber) {
        if ("1".equals(mFlag)) {
            mStart = startNumber;
            cancel();
            startTimerTask();
//            NewStockListService.mOberservers.add(this);
            reStartConnect();
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newstock_back:
                NetWorkUtil.cancelSingleRequestByTag(TAG);
                finish();
                break;
            case R.id.newstock_search:
                Intent intent = new Intent();
                intent.setClass(NewStockListActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void fillStockList(ArrayList<NewStockEnitiy> entities) {

        if (entities == null || entities.size() <=0) {
            mProgressBar.setVisibility(View.GONE);
            return;
        }
        mNewIssueBeanList = entities;

//        mNewIssueBeanList.remove(0);

            mNetTotalCount = mNewIssueBeanList.size();

        notifyObservers();

        mNewStockListAdapter.setDatas(mNewIssueBeanList);

        mProgressBar.setVisibility(View.GONE);

        if (doSelectMiddle) {
            if (mJudegeGesture) {

                int index = 10;//mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition()
                View v = mListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                mListView.setSelectionFromTop(index, top);

            } else {
                int index = 20-(mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition() + 1);
                View v = mListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                mListView.setSelectionFromTop(index, top);
            }

            String time = mNewIssueBeanList.get(0).getConnectTime();

            if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
                int intTime = Integer.parseInt(time);
//                notifyObservers(intTime * 1000);

            }
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
                LogHelper.e(TAG, "Timer request:"+isCallBackSuccess+"|"+refleshTime+"  refleshCount:"+refleshCount);
                if(isCallBackSuccess)
                {
                    if(refleshTime>0){
                        refleshCount +=3;
                        if(refleshCount>=refleshTime){
                            intoNewStockMarketConnect();
                        }
                    }else{
                        refleshCount = 0;
                        intoNewStockMarketConnect();
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
    public int getLayoutId() {
        return R.layout.activity_newstock_list;
    }
}
