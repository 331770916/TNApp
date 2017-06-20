package com.tpyzq.mobile.pangu.activity.home.information;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.EventDetailListAdapter;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.EventDetailRelatedInfoAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.InformationBean;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.OneTimiceAddSelfChoiceListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.ScreenShot;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialogTwo;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 重大事件  详情界面
 * 刘泽鹏
 */
public class ImportantEventDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ImportantEventDetail";
    private PullToRefreshScrollView mPullRefreshScrollView;
    private TextView EventDetailTitle, EventDetailRiQi, tvInformationNum, tvOneKey;  //标题  日期  相关资讯条数,一键自选
    private WebView mWebView;        //WebView
    private MyListView listViewUp, listViewDown;     //listView
    private LinearLayout llOneKey, llEventDetaiJiaZai;       //一键自选  ,重新加载的图片
    private RelativeLayout rlRelated, rlImportantEventDetail;    //20条更多 按钮 ,包裹所有布局的 RelativeLayout
    private ArrayList<InformationBean> list;
    private ArrayList<StockInfoEntity> beans;
    private ArrayList<InformationBean> data;
    private ArrayList<StockInfoEntity> stockList;
    private EventDetailRelatedInfoAdapter adapter;
    private EventDetailListAdapter listAdapter;
    private String requestId;
    private Dialog dialog;
    private Dialog loadingDialog;
    private String mType;
    private ShareDialog shareDialog;

    @Override
    public void initView() {
        this.findViewById(R.id.iv_EventDetailBack).setOnClickListener(this);            //返回
        this.findViewById(R.id.ivEventDetailFenXiang).setOnClickListener(this);         //分享
        mPullRefreshScrollView = (PullToRefreshScrollView) this.findViewById(R.id.psvEventDetail); //下拉刷新的 scrollView
        EventDetailTitle = (TextView) this.findViewById(R.id.EventDetailTitle);
        EventDetailRiQi = (TextView) this.findViewById(R.id.EventDetailRiQi);
        tvInformationNum = (TextView) this.findViewById(R.id.tvInformationNum);        //相关资讯的条数
        tvOneKey = (TextView) this.findViewById(R.id.tvOneKey);                         //一键自选的  文字
        mWebView = (WebView) this.findViewById(R.id.wvEventDetail);
        listViewUp = (MyListView) this.findViewById(R.id.lvEventDetail);
        listViewDown = (MyListView) this.findViewById(R.id.lvRelated);
        llOneKey = (LinearLayout) this.findViewById(R.id.llOneKey);
        rlRelated = (RelativeLayout) this.findViewById(R.id.rlRelated);

        listViewUp.setFocusable(false);
        listViewDown.setFocusable(false);
        initData();
    }

    private void initData() {
        shareDialog = new ShareDialog(this);
                //初始化   显示菊花  背景为灰色
        rlImportantEventDetail = (RelativeLayout) this.findViewById(R.id.rlImportantEventDetail);
        llEventDetaiJiaZai = (LinearLayout) this.findViewById(R.id.llEventDetaiJiaZai);
        dialog = LoadingDialogTwo.initDialog(ImportantEventDetailActivity.this); //菊花
        dialog.show();      //显示菊花
        mPullRefreshScrollView.setVisibility(View.GONE);     //隐藏下拉刷新的 scrollView


        stockList = new ArrayList<StockInfoEntity>();
        list = new ArrayList<InformationBean>();
        adapter = new EventDetailRelatedInfoAdapter(this);
        listAdapter = new EventDetailListAdapter(this);
        requestId = getIntent().getStringExtra("id");
        getData(requestId);     //获取获取重大详情页面的数据
        listViewDown.setAdapter(adapter);
        listViewUp.setAdapter(listAdapter);
        rlRelated.setOnClickListener(this);
        llOneKey.setOnClickListener(this);

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
                    //模拟加载数据线程休息3秒
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1000);
                                getData(requestId);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);

                            //完成对下拉刷新ListView的更新操作
//                            adapter.notifyDataSetChanged();
                            //将下拉视图收起
                            mPullRefreshScrollView.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });


        //添加  条目点击事件
        listViewUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockInfoEntity stockInfoEntity = beans.get(position);
                String stockCode = stockInfoEntity.getStockNumber();
                String stockName = stockInfoEntity.getStockName();
                Intent intent1 = new Intent();
                intent1.setClass(ImportantEventDetailActivity.this, StockDetailActivity.class);
                StockDetailEntity entity = new StockDetailEntity();
                entity.setStockName(stockName);
                entity.setStockCode(stockCode);
                intent1.putExtra("stockIntent", entity);
                startActivity(intent1);

            }
        });

        //添加  条目点击事件
        listViewDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ImportantEventDetailActivity.this, NewsDetailActivity.class);
                InformationBean informationBean = data.get(position);
                String requestId = informationBean.getRequestId();
                intent.putExtra("requestId", requestId);
                startActivity(intent);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_important_event_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_EventDetailBack:       //点击返回  销毁当前界面
                finish();
                break;

            case R.id.ivEventDetailFenXiang:    //点击  分享
                loadingDialog = LoadingDialog.initDialog(this, "加载中…");
                loadingDialog.show();       //显示菊花
                mType = "2";
                getShare(mType);
                break;
            case R.id.rlRelated:                //点击  20条  更多
                Intent intent = new Intent();
                intent.setClass(this, EventDetailRelatedInfoListActivity.class);
                intent.putExtra("list", list);
                startActivity(intent);
                break;
            case R.id.llOneKey:    //点击  一鍵自選

                SelfStockHelper.oneTimiceAddSelfChoice(TAG, "", beans, new OneTimiceAddSelfChoiceListener() {
                    @Override
                    public void getResult(String result) {
                        SelfStockHelper.explanOneTimiceAddSelfChoiceResult(ImportantEventDetailActivity.this, result);
                    }
                });
                break;
        }
    }


    /**
     * 截屏 获取图片
     *
     * @param type 1自选股 4行情
     */
    private void getShare(String type) {
        String capitalAccount = UserUtil.capitalAccount;                        //注册账户
        String base64 = ScreenShot.shoot(this);         //截屏 拿到图片的 base64
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("base64", base64);
        map2.put("account", capitalAccount);
        map2.put("type", type);
        map2.put("phone_type", "2");
        map1.put("FUNCTIONCODE", "HQFNG001");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString("MarketFragment", ConstantUtil.URL_FX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        String url = jsonObject.getString("msg");
                        loadingDialog.dismiss();
                        shareDialog.setUrl(url);
                        shareDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取中间列表的数据
     */
    private void getListData(StringBuilder codes) {
        HashMap map3 = new HashMap();
        HashMap map4 = new HashMap();
        Object[] obj = new Object[1];
        map4.put("market", "0");
        map4.put("code", codes.toString());
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
                    JSONArray array = new JSONArray(response);
                    beans = new ArrayList<StockInfoEntity>();
                    JSONObject res = array.getJSONObject(0);
                    if ("0".equals(res.optString("code"))) {
                        String totalCount = res.optString("totalCount");
                        JSONArray jsonArray = res.getJSONArray("data");
                        if (null != jsonArray && jsonArray.length() > 0) {
                            //如果只有1条记录，报文格式不为array数组，故进行特殊处理  2017-03-14
                            if ("1".equals(totalCount) || jsonArray.length() == 1) {
                                StockInfoEntity _bean = new StockInfoEntity();
                                _bean.setStockNumber(jsonArray.getString(0));
                                _bean.setStockName(jsonArray.getString(1));
                                _bean.setTime(jsonArray.getString(2));
                                _bean.setNewPrice(jsonArray.getString(3));
                                _bean.setClose(jsonArray.getString(4));
                                beans.add(_bean);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray json = jsonArray.getJSONArray(i);
                                    StockInfoEntity _bean = new StockInfoEntity();
                                    _bean.setTotalCount(totalCount);
                                    if (null != json && json.length() > 0) {
                                        _bean.setStockNumber(json.getString(0));
                                        _bean.setStockName(json.getString(1));
                                        _bean.setTime(json.getString(2));
                                        _bean.setNewPrice(json.getString(3));
                                        _bean.setClose(json.getString(4));
                                    }
                                    beans.add(_bean);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    ObjectMapper objectMapper = JacksonMapper.getInstance();
//                    ArrayList<Map<String, Object>> responseValues = objectMapper.readValue(response, new ArrayList<Map<String, Object>>().getClass());
//
//                    beans = new ArrayList<StockInfoEntity>();
//                    for (int i = 0; i < responseValues.size(); i++) {
//                        Map<String, Object> maps = responseValues.get(i);
//                        String code = (String) maps.get("code");
//                        String totalCount = (String) maps.get("totalCount");
//
//                        if ("0".equals(code)) {
//                            if ("1".equals(totalCount)) {
//                                List<String> data = (List<String>) maps.get("data");
//                                StockInfoEntity _bean = new StockInfoEntity();
//                                _bean.setTotalCount(totalCount);
//                                if (!TextUtils.isEmpty(data.get(0))) {
//                                    _bean.setStockNumber(data.get(0));
//                                }
//                                if (!TextUtils.isEmpty(data.get(1))) {
//                                    _bean.setStockName(data.get(1));
//                                }
//                                if (!TextUtils.isEmpty(data.get(2))) {
//                                    _bean.setTime(data.get(2));
//                                }
//                                if (!TextUtils.isEmpty(data.get(3))) {
//                                    _bean.setNewPrice(data.get(3));
//                                }
//                                if (!TextUtils.isEmpty(data.get(4))) {
//                                    _bean.setClose(data.get(4));
//                                }
//                                beans.add(_bean);
//                            } else {
//                                List<List<String>> data = (List<List<String>>) maps.get("data");
//                                for (int j = 0; j < data.size(); j++) {
//                                    StockInfoEntity _bean = new StockInfoEntity();
//                                    _bean.setTotalCount(totalCount);
//
//                                    for (int k = 0; k < data.get(j).size(); k++) {
//                                        if (!TextUtils.isEmpty(data.get(j).get(0))) {
//                                            _bean.setStockNumber(data.get(j).get(0));
//                                        }
//
//                                        if (!TextUtils.isEmpty(data.get(j).get(1))) {
//                                            _bean.setStockName(data.get(j).get(1));
//                                        }
//
//                                        if (!TextUtils.isEmpty(data.get(j).get(2))) {
//                                            _bean.setTime(data.get(j).get(2));
//                                        }
//
//                                        if (!TextUtils.isEmpty(data.get(j).get(3))) {
//                                            _bean.setNewPrice(data.get(j).get(3));
//                                        }
//
//                                        if (!TextUtils.isEmpty(data.get(j).get(4))) {
//                                            _bean.setClose(data.get(j).get(4));
//                                        }
//                                    }
//                                    beans.add(_bean);
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                listAdapter.setList(beans);
            }
        });
    }

    /**
     * 获取20条相关资讯数据
     */

    private void getRelatedInfoData(StringBuilder codeArray) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("code", codeArray.toString());
//        String code = codeArray.substring(2, codeArray.length());
//        map2.put("code", code);
        map1.put("FUNCTIONCODE", "HQONG002");
        map1.put("TOKEN", "");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                if (list != null && list.size() > 0) {
                    list.clear();
                }

                if (data != null && data.size() > 0) {
                    data.clear();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        JSONArray message = jsonObject.getJSONArray("message");
                        int size = message.length();
                        String num = String.valueOf(size);
                        tvInformationNum.setText(num + "条相关资讯");
                        for (int i = 0; i < size; i++) {
                            JSONObject item = message.getJSONObject(i);
                            String title = item.getString("title");
                            String requestId = item.getString("id");
                            long dt = item.getLong("dt");
                            Date date = new Date(dt);
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
                            String formatTime = sdf.format(date);               //时间
                            JSONArray stockList = item.getJSONArray("stockList");
                            ArrayList<String> stockNameList = new ArrayList<String>();
                            for (int j = 0; j < stockList.length(); j++) {
                                JSONObject jsonObject1 = stockList.getJSONObject(j);
                                String comp = jsonObject1.getString("name");
//                                String comp = jsonObject1.getString("comp");
                                stockNameList.add(comp);
                            }
                            InformationBean informationBean = new InformationBean();
                            informationBean.setPublishTitle(title);
                            informationBean.setTimes(formatTime);
                            informationBean.setRequestId(requestId);
                            informationBean.setSopCastList(stockNameList);
                            list.add(informationBean);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                data = new ArrayList<InformationBean>();

                if (list != null && list.size() > 0) {
                    for (int j = 0; j < 3; j++) {
                        data.add(list.get(j));
                    }
                    adapter.setList(data);
                }
            }
        });
    }

    /**
     * 获取获取重大详情页面的数据
     */
    private void getData(final String requestId) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("id", requestId);
        map1.put("FUNCTIONCODE", "HQONG011");
        map1.put("TOKEN", "");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                rlImportantEventDetail.setBackgroundColor(ContextCompat.getColor(ImportantEventDetailActivity.this,
                        R.color.white));        //白色
                dialog.dismiss();    //隐藏 菊花
                mPullRefreshScrollView.setVisibility(View.GONE);     //隐藏 下拉刷新的 scrollView
                llEventDetaiJiaZai.setVisibility(View.VISIBLE);     //显示  重新加载
                llEventDetaiJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llEventDetaiJiaZai.setVisibility(View.GONE);     //隐藏  重新加载
                        rlImportantEventDetail.setVisibility(View.VISIBLE); //显示 背景
                        rlImportantEventDetail.setBackgroundColor(ContextCompat.getColor(ImportantEventDetailActivity.this,
                                R.color.dividerColor));        //灰色
                        dialog.show();    //显示 菊花
                        //模拟加载数据线程休息1秒
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                super.onPostExecute(result);
                                getData(requestId);
                            }
                        }.execute();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        //拿到数据 后 背景设置为白色  隐藏菊花  展示数据
                        rlImportantEventDetail.setBackgroundColor(ContextCompat.getColor(ImportantEventDetailActivity.this,
                                R.color.white));
                        dialog.dismiss();    //隐藏菊花
                        mPullRefreshScrollView.setVisibility(View.VISIBLE);     //显示 下拉刷新的 scrollView

                        JSONObject message = jsonObject.getJSONObject("message");
                        String name = message.getString("name");
                        String pdt = message.getString("pdt");
                        String desc = message.getString("desc");
                        String tip = message.getString("tip");

                        JSONArray secus = message.getJSONArray("stocks");//  secus
                        StringBuilder codeArray = new StringBuilder();      //存储20条  的入参
                        StringBuilder codes = new StringBuilder();          //存储 中间列表的入参
                        String maker = null;
                        for (int i = 0; i < secus.length(); i++) {
                            JSONObject item = secus.getJSONObject(i);
                            String name1 = item.getString("name");
                            String code1 = item.getString("code");

                            if (!TextUtils.isEmpty(code1)) {
                                String market = code1.substring(7, 9);
                                if ("SZ".equals(market)) {
                                    maker = "90";
                                } else if ("SH".equals(market)) {
                                    maker = "83";
                                }
                                String newCode = Helper.getStockCode(code1, maker);
                                String subCode = newCode.substring(0, 8);
                                codeArray.append(subCode).append(",");
                            }


//
//                            String stockcode = item.getString("code");
//                            String market = stockcode.substring(7, 9);      //获取判断市场的  部分代码
//
//                            if (market.equals("SZ")) {                        //判断是哪个市场的
//                                maker = "90";
//                            } else if (market.equals("SH")) {
//                                maker = "83";
//                            }

                            String newCode = Helper.getStockCode(code1, maker);
                            String subCode = newCode.substring(0, 8);
                            codes.append(subCode).append("&");
                        }

                        if (codeArray != null && codeArray.length() > 0) {
                            codeArray.deleteCharAt(codeArray.length() - 1);
                            getRelatedInfoData(codeArray);   //获取20条相关资讯数据
                        }

                        if (codes != null && codes.length() > 0) {
                            codes.deleteCharAt(codes.length() - 1);
                            getListData(codes);                  //获取中间列表的数据
                        }

                        EventDetailTitle.setText(name);
                        EventDetailRiQi.setText(pdt);

                        StringBuffer sb = new StringBuffer();
                        // 驱动事件
                        sb.append("<html>\n" +
                                "\t<head>\n" +
                                "\t\t<meta charset=\"UTF-8\">\n" +
                                "\t\t<title>word</title>\n" +
                                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=no\">\n" +
                                "\t\t<style>\n" +
                                "\t\t\t.new, .news{\n" +
                                "\t\t\t\twidth: 94%;\n" +
                                "\t\t\t\tmargin-left: 3%;\n" +
                                "\t\t\t\ttext-align:justify;\n" +
                                "\t\t\t\tletter-spacing:2px;\n" +
                                "\t\t\t\tline-height: 25px;\n" +
                                "\t\t\t\tfont-size: 14px;\n" +
                                "\t\t\t\tcolor:#4c4c4c;\n" +
                                "\t\t\t}\n" +
                                "\t\t\t.news{\n" +
                                "\t\t\t\tmargin-top: 10px;\n" +
                                "\t\t\t}\n" +
                                "\t\t\t.title{\n" +
                                "\t\t\t\tcolor: #e84242; \n" +
                                "\t\t\t\tfont-size: 14px;\n" +
                                "\t\t\t\tfont-weight: bold;\n" +
                                "\t\t\t\tletter-spacing: 1px;\n" +
                                "\t\t\t}\n" +
                                "\t\t</style>\n" +
                                "\t</head>\n" +
                                "\t<body>\t\n" +
                                "\t\t<div class=\"new\">\n" +
                                "\t\t\t<span class=\"title\">[驱动事件]</span>\n");

                        sb.append(desc);        //服务器返回的字符串

                        sb.append("</div>\n" +
                                "\t\t<div class=\"news\">\n" +
                                "\t\t\t<span class=\"title\">[机会提示]</span>\n");


                        //机会提示
                        sb.append(tip);     //服务器返回的字符串

                        sb.append("</div>\t\n" +
                                "\t</body>\n" +
                                "\n" +
                                "</html>\n");


                        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
                        mWebView.getSettings().setJavaScriptEnabled(true);
                        mWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

}
