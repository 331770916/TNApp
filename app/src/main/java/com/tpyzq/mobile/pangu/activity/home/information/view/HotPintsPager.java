package com.tpyzq.mobile.pangu.activity.home.information.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.HotAnalysisDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.information.HotAnalysisListActivity;
import com.tpyzq.mobile.pangu.activity.home.information.ImportantEventActivity;
import com.tpyzq.mobile.pangu.activity.home.information.ImportantEventDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.HotAnalysisListViewAdapter;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.HotPintsGridViewAdapter;
import com.tpyzq.mobile.pangu.data.InformationBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.gridview.MyGridView;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 热点模块 首页
 * 刘泽鹏
 */
public class HotPintsPager extends BaseInfoPager implements View.OnClickListener {


    public static final String TAG = "HotPintsPager";
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ScrollView mScrollView;
    private RelativeLayout rlJinRiZhongDa, rlHotAnalysis, rlZhongDaDiYi, rlHot;  //今日重大事件  热点公告解析   更多,重大事件第一条新闻
    private ImageView ivReOrZhong;                                   //重大热点
    private TextView EventTitle, EventDate, tvTouXian, tvTouXian1;                           //重大事件   标题    日期
    private MyGridView mGridView;                                    //GridView
    private MyListView mListView;                                    //ListView
    private HotPintsGridViewAdapter mGridViewAdapter;                //GridView 适配器
    private HotAnalysisListViewAdapter mListViewAdapter;             //listView 适配器
    private ArrayList<InformationBean> eventList;                    //存储  重大事件数据的  集合
    private ArrayList<InformationBean> analysisList;                //存储  热点解析数据的  集合
    private ArrayList<InformationBean> beans;
    private ArrayList<InformationBean> arrayList;                   //GridView的数据源
    private String mSession;
    private LinearLayout llHotJiaZai;                               //重新加载的图片
    private ProgressBar pb_HotPager;          //菊花
    private int count = 5;  //热点公告解析列表显示条数

    public HotPintsPager(Context context) {
        super(context);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.activity_hot_pints_pager;
    }


    public void initData() {
        //网络请求
        getEventData();                 //获取重大事件数据

        //上拉、下拉设定  Mode.PULL_FROM_START
        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);


        //上拉监听函数
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (refreshView.isShownHeader()) {
                    //判断头布局是否可见，如果可见执行下拉刷新
                    //设置尾布局样式文字
                    mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    getEventData();
                    mPullRefreshScrollView.onRefreshComplete();
//                    //模拟加载数据线程休息3秒
//                new AsyncTask<Void, Void, Void>() {
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        try {
//                            Thread.sleep(100);
//                            getEventData();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        super.onPostExecute(result);
//
//                        //完成对下拉刷新ListView的更新操作
////                            adapter.notifyDataSetChanged();
//                        //将下拉视图收起
//                        mPullRefreshScrollView.onRefreshComplete();
//                    }
//                }.execute();
            }
            }
        });


        mGridViewAdapter = new HotPintsGridViewAdapter(mContext);               //实例化 GridView 适配器
        mListViewAdapter = new HotAnalysisListViewAdapter(mContext);            //实例化 ListView 适配器
        mGridView.setAdapter(mGridViewAdapter);                                  //适配
        mListView.setAdapter(mListViewAdapter);                                  //适配

        //给更多  添加点击事件
        rlJinRiZhongDa.setOnClickListener(this);

        rlZhongDaDiYi.setOnClickListener(this);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String requestId = arrayList.get(position).getRequestId();
                Intent intent = new Intent();
                intent.setClass(mContext, ImportantEventDetailActivity.class);
                intent.putExtra("id", requestId);
                mContext.startActivity(intent);

            }
        });

        // 给热点公告解析列表添加   item 的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InformationBean informationBean = analysisList.get(position);
                InformationBean informationBeanPrice = beans.get(position);
                Intent intent = new Intent();
                intent.putExtra("informationBean", informationBean);
                intent.putExtra("informationBeanPrice", informationBeanPrice);
                intent.setClass(mContext, HotAnalysisDetailActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void setView() {
        eventList = new ArrayList<InformationBean>();
        analysisList = new ArrayList<InformationBean>();
        beans = new ArrayList<InformationBean>();

        mPullRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.svPullToRefresh);
        rlJinRiZhongDa = (RelativeLayout) rootView.findViewById(R.id.rlJinRiZhongDa);
        rlHotAnalysis = (RelativeLayout) rootView.findViewById(R.id.rlHotAnalysis);
        rlZhongDaDiYi = (RelativeLayout) rootView.findViewById(R.id.rlZhongDaDiYi);
        ivReOrZhong = (ImageView) rootView.findViewById(R.id.ivReOrZhong);
        EventTitle = (TextView) rootView.findViewById(R.id.tvImportantEventTitle);
        tvTouXian = (TextView) rootView.findViewById(R.id.tvTouXian);
        tvTouXian1 = (TextView) rootView.findViewById(R.id.tvTouXian1);
        EventDate = (TextView) rootView.findViewById(R.id.tvImportantEventDate);
        mGridView = (MyGridView) rootView.findViewById(R.id.gvHotPints);
        mListView = (MyListView) rootView.findViewById(R.id.lvHotAnalysis);

        //获取ScrollView布局，此文中用不到
        mScrollView = mPullRefreshScrollView.getRefreshableView();

        mPullRefreshScrollView.setVisibility(View.GONE);

        rlHot = (RelativeLayout) rootView.findViewById(R.id.rlHot);     //包裹所有布局的 RelativeLayout  默认为灰色
        llHotJiaZai = (LinearLayout) rootView.findViewById(R.id.llHotJiaZai);
        pb_HotPager = (ProgressBar) rootView.findViewById(R.id.pb_HotPager);    //初始化  默认显示菊花
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rlZhongDaDiYi:        //点击  重大事件 第一条新闻
                String requestId = eventList.get(0).getRequestId();
                intent.putExtra("id", requestId);
                intent.setClass(mContext, ImportantEventDetailActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.rlJinRiZhongDa:       //点击  今日重大事件    更多
                intent.setClass(mContext, ImportantEventActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.rlHotAnalysis:        //点击  热点公告解析   更多
                intent.setClass(mContext, HotAnalysisListActivity.class);
                intent.putExtra("analysisList", analysisList);
                intent.putExtra("beans", beans);
                mContext.startActivity(intent);
                break;
            case R.id.llHotJiaZai:          //点击  重新加载的时候

                llHotJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                pb_HotPager.setVisibility(View.VISIBLE);      //显示菊花
                rlHot.setVisibility(View.VISIBLE);//显示背景
                rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dividerColor)); //设置为灰色
                getEventData();
                //模拟加载数据线程休息1秒
//                new AsyncTask<Void, Void, Void>() {
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        super.onPostExecute(result);
//                        getEventData();
//                    }
//                }.execute();

                break;
        }
    }


    /**
     * 获取最新价格
     */
    private void getPrice() {
        StringBuilder sb = new StringBuilder();
        String maker = null;
        for (int i = 0; i < analysisList.size(); i++) {
            InformationBean entity = analysisList.get(i);
            String stockcode = entity.getStockcode();

            if (stockcode != null && stockcode.length() > 0) {

                String market = stockcode.substring(7, 9);
                if ("SZ".equals(market)) {
                    maker = "90";
                } else if ("SH".equals(market)) {
                    maker = "83";
                }

                String newCode = Helper.getStockCode(stockcode, maker);
                String subCode = newCode.substring(0, 8);
                sb.append(subCode).append("&");
            }
        }
        if (sb != null && sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);

        }

        HashMap map3 = new HashMap();
        HashMap map4 = new HashMap();
        Object[] obj = new Object[1];
        map4.put("market", "0");
        map4.put("code", sb.toString());
        map4.put("type", "4");
        map4.put("order", "0");
        obj[0] = map4;
        Gson gson = new Gson();
        String strJson = gson.toJson(obj);
        map3.put("FUNCTIONCODE", "HQING005");
        map3.put("PARAMS", strJson);
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL, map3, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if ("null\n".equals(response)) {
                    return;
                }

                if (beans != null && beans.size() > 0) {
                    beans.clear();
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(null != jsonArray && jsonArray.length() > 0){
                        JSONObject res = jsonArray.getJSONObject(0);
                        if("0".equals(res.optString("code"))){
                            JSONArray array = res.getJSONArray("data");
                            if(null != array && jsonArray.length() > 0){
                                for (int i = 0; i < analysisList.size(); i++) {
                                    for (int j = 0; j < array.length(); j++) {
                                        JSONArray json = array.getJSONArray(j);
                                        InformationBean _bean = new InformationBean();
                                        _bean.setPublishAboutStock(json.getString(1));
                                        _bean.setPrice(json.getString(3));
                                        _bean.setClose(json.getString(4));
                                        beans.add(_bean);
                                    }
                                }
                            }
                        }
                    }
                    ArrayList<InformationBean> setList = new ArrayList<InformationBean>();
                    ArrayList<InformationBean> setPirceList = new ArrayList<InformationBean>();
                    if (analysisList != null && analysisList.size() > 0 && beans.size() >0) {
                        int length = analysisList.size() >= count ? count : analysisList.size();
                        for (int i = 0; i < length; i++) {
                            setList.add(analysisList.get(i));
                            setPirceList.add(beans.get(i));
                        }
                        mListViewAdapter.setList(setList);
                        mListViewAdapter.setPirceList(setPirceList);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                try {
//                    ObjectMapper objectMapper = JacksonMapper.getInstance();
//                    ArrayList<Map<String, Object>> responseValues = objectMapper.readValue(response, new ArrayList<Map<String, Object>>().getClass());
//
//                    for (int i = 0; i < responseValues.size(); i++) {
//                        Map<String, Object> maps = responseValues.get(i);
//                        String code = (String) maps.get("code");
//                        String totalCount = (String) maps.get("totalCount");
//
//                        List<List<String>> data = (List<List<String>>) maps.get("data");
//                        if ("0".equals(code) && data.size() > 0) {
//                            for (int z = 0; z < analysisList.size(); z++) {
//                                for (int j = 0; j < data.size(); j++) {
//                                    InformationBean _bean = new InformationBean();
//
//                                    for (int k = 0; k < data.get(j).size(); k++) {
//
//                                        _bean.setPublishAboutStock(data.get(j).get(1));
//                                        _bean.setPrice(data.get(j).get(3));
//                                        _bean.setClose(data.get(j).get(4));
//                                    }
//                                    beans.add(_bean);
//                                }
//                            }
//                        }
//                    }
//
//                    ArrayList<InformationBean> setList = new ArrayList<InformationBean>();
//                    ArrayList<InformationBean> setPirceList = new ArrayList<InformationBean>();
//                    if (analysisList != null && analysisList.size() > 0) {
//                        for (int i = 0; i < analysisList.size(); i++) {
//                            if (i <5) {
//                                setList.add(analysisList.get(i));
//                                setPirceList.add(beans.get(i));
//                            }
//                        }
//
//                        mListViewAdapter.setList(setList);
//                        mListViewAdapter.setPirceList(setPirceList);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });

    }


    /**
     * 获取重大事件数据和热点公告
     */
    private void getEventData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("FUNCTIONCODE", "HQONG014");
        map1.put("TOKEN", "");
        map1.put("PARAMS", map2);
//        LogUtil.e(TAG,map1.toString());
        String a=map1.toString();

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            private InformationBean entity_name;

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());

                pb_HotPager.setVisibility(View.GONE);  //隐藏菊花
                rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));    //背景 为 白色
                mPullRefreshScrollView.setVisibility(View.GONE);
                llHotJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
                llHotJiaZai.setOnClickListener(HotPintsPager.this);

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response) && response.equals("null")) {
                    return;
                }

                if ("null\n".equals(response)) {
                    return;
                }

                if (eventList != null && eventList.size() > 0) {
                    eventList.clear();
                }


                if (analysisList != null && analysisList.size() > 0) {
                    analysisList.clear();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        JSONArray message = jsonObject.getJSONArray("message");
                        if (message != null && message.length() > 0) {
                            for (int s = 0; s < message.length(); s++) {

                                String ZDSJcode = message.getJSONObject(s).getString("code");
                                String ZDSJsuccess = message.getJSONObject(s).getString("type");

                                if ("200".equals(ZDSJcode) && "success".equals(ZDSJsuccess)) {
                                    JSONObject Newmessage_0 = message.getJSONObject(s).getJSONObject("message");  //重大事件
                                    JSONArray stocks_0 = Newmessage_0.getJSONArray("stocks");
                                    if (stocks_0 != null && stocks_0.length() > 0) {
                                        for (int i = 0; i < stocks_0.length(); i++) {
                                            InformationBean entity = new InformationBean();
                                            JSONObject item = stocks_0.getJSONObject(i);
                                            entity.setPublishTitle(item.getString("titl"));
                                            entity.setTime(item.getLong("pdt"));
                                            entity.setType(item.getInt("type"));
                                            entity.setRequestId(item.getString("id"));
                                            eventList.add(entity);
                                        }
                                        if (eventList != null && eventList.size() > 0) {
                                            //给今日重大事件  第一条数据 赋值
                                            InformationBean entity = eventList.get(0);
                                            String publishTitle = entity.getPublishTitle();
                                            long time = entity.getTime();
                                            Date date = new Date(time);
                                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
                                            String formatTime = sdf.format(date);
                                            EventTitle.setText(publishTitle);
                                            EventDate.setText(formatTime);
                                            arrayList = new ArrayList<InformationBean>();
                                            for (int i = 1; i < 5; i++) {
                                                arrayList.add(eventList.get(i));
                                            }
                                            mGridViewAdapter.setList(arrayList);
                                        }
                                        tvTouXian.setVisibility(View.VISIBLE);
                                        rlZhongDaDiYi.setVisibility(View.VISIBLE);
                                        rlJinRiZhongDa.setVisibility(View.VISIBLE);
                                        pb_HotPager.setVisibility(View.GONE);  //隐藏菊花
                                        rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                                        mPullRefreshScrollView.setVisibility(View.VISIBLE);
                                    }
                                } else if ("200".equals(ZDSJcode) && "SUCCESS".equals(ZDSJsuccess)) {
                                    JSONArray Newmessage_1 = message.getJSONObject(s).getJSONArray("message");    //热点公告
                                    if (Newmessage_1 != null && Newmessage_1.length() > 0) {
                                        for (int i = 0; i < Newmessage_1.length(); i++) {
                                            JSONObject item = Newmessage_1.getJSONObject(i);
                                            ArrayList name = new ArrayList();
                                            InformationBean entity = new InformationBean();
                                            DecimalFormat format2 = new DecimalFormat("#0%");

                                            String tname = item.getString("tname");              //状态
                                            double prob = item.getDouble("prob");               //百分比  0.52 需要转
                                            String probs = format2.format(prob);
                                            int days = item.getInt("days");                     //建议天数
                                            String day = String.valueOf(days);
                                            String state = item.getString("state");             //描述  WebView 展示的数据
                                            String date = item.getString("date");

                                            JSONArray mArray_stocks = item.getJSONArray("stocks");
                                            StringBuilder sb = new StringBuilder();
                                            String maker = null;
                                            for (int j = 0; j < mArray_stocks.length(); j++) {

                                                entity_name = new InformationBean();

                                                String mStockCode = mArray_stocks.getJSONObject(j).getString("code");    //市场 股票代码
                                                String mStockTick = mArray_stocks.getJSONObject(j).getString("tick");    //6位股票代码
                                                String mStockName = mArray_stocks.getJSONObject(j).getString("name");    //股票名称

                                                String mStockCode_0 = mArray_stocks.getJSONObject(0).getString("code");    // 获取第一条 市场 股票代码
                                                String mStockName_0 = mArray_stocks.getJSONObject(0).getString("name");    // 获取第一条  股票名称

                                                String market = mStockCode.substring(7, 9);
                                                if ("SZ".equals(market)) {
                                                    maker = "90";
                                                } else if ("SH".equals(market)) {
                                                    maker = "83";
                                                }

                                                String newCode = Helper.getStockCode(mStockCode, maker);
                                                String subCode = newCode.substring(0, 8);
                                                sb.append(subCode).append("&");


                                                if (!"null".equals(mStockTick)) {
                                                    entity.setJsonArray(sb.toString());
                                                    entity.setTname(tname);
                                                    entity.setProb(probs);
                                                    entity.setDays(day);
                                                    entity.setState(state);
                                                    entity.setPublishAboutStock(mStockName_0);      //股票名称
                                                    entity.setStockcode(mStockCode_0);              //市场 股票代码
                                                    entity.setStocks(mStockCode);                   //所有股票  市场 股票代码
                                                    entity.setPublishTime(date);
                                                    entity.setStockslength(mArray_stocks.length() + "");
                                                    name.add(mStockName);
                                                    entity.setName(name);
                                                }
                                            }
                                            analysisList.add(entity);
                                        }
                                    }
                                    tvTouXian1.setVisibility(View.VISIBLE);
                                    rlHotAnalysis.setVisibility(View.VISIBLE);
                                    pb_HotPager.setVisibility(View.GONE);  //隐藏菊花
                                    rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                                    mPullRefreshScrollView.setVisibility(View.VISIBLE);
                                    rlHotAnalysis.setOnClickListener(HotPintsPager.this);
                                    getPrice();        //获取 价格
                                }
                            }
                        } else {
                            pb_HotPager.setVisibility(View.GONE);  //隐藏菊花
                            rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));    //背景 为 白色
                            mPullRefreshScrollView.setVisibility(View.GONE);
                            llHotJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
                            llHotJiaZai.setOnClickListener(HotPintsPager.this);
                        }
                    } else {
                        pb_HotPager.setVisibility(View.GONE);  //隐藏菊花
                        rlHot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));    //背景 为 白色
                        mPullRefreshScrollView.setVisibility(View.GONE);
                        llHotJiaZai.setVisibility(View.VISIBLE);       //当请求失败的时候  显示重新加载图片
                        llHotJiaZai.setOnClickListener(HotPintsPager.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }
}
