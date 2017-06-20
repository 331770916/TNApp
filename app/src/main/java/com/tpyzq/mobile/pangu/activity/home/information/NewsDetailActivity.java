package com.tpyzq.mobile.pangu.activity.home.information;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.EventDetailListAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.NewsDetailEntity;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.OneTimiceAddSelfChoiceListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.ScreenShot;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.FlowLayout;
import com.tpyzq.mobile.pangu.view.SimulateListView;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialogTwo;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;


/**
 * 新闻详情页
 * 刘泽鹏
 */

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "NewsDetail";
    private PullToRefreshScrollView mPullRefreshScrollView;
    private TextView tvNewsTitle, tvLaiYuanName, tvLaiYuanDate, tv_tags,
            tvShangYou, tvZhongJian, tvXiaYou, tvOneKeyXuan; //标题 ,来源,来源时间,标签,上游，中间，下游,一键自选
    private WebView myWebView;                                             //webView
    private FlowLayout myFlowLayout;                                      //标签
    private FrameLayout llRelevantIndustry;
    private LinearLayout llDetailYiJianZiXuan, llNewDetailJiaZai;      //相关行业 , 一键自选,重新加载的图片
    private SimulateListView tvDetailListView;                                 //listView
    private FlowLayout.LayoutParams layoutParams;                        //flowLayout  的属性设置
    private String requestId;
    private NewsDetailEntity newsDetailBean;                               //存储 网络请求回来数据的实体类
    private EventDetailListAdapter adapter;
    private ArrayList<StockInfoEntity> beans;
    private List<String> tags;
    private Dialog dialog;
    private Dialog loadingDialog;
    private LinearLayout llXiangGuanTitle, llListViewTitle;      //相关行业上面的  title , listView 上面的title
    private TextView tvListViewFenGe, tvChaKanQuanBu;           // listView 上面的分割线 , 查看全部
    private ShareDialog shareDialog ;
    @Override
    public void initView() {

        requestId = getIntent().getStringExtra("requestId");
        if (requestId == null) {
            return;
        }
        this.findViewById(R.id.ivNewsDetail_back).setOnClickListener(this);         //详情页返回按钮
        this.findViewById(R.id.ivDetailFenXiang).setOnClickListener(this);         //详情页分享按钮
        mPullRefreshScrollView = (PullToRefreshScrollView) this.findViewById(R.id.svNewsDetail);
        tvNewsTitle = (TextView) this.findViewById(R.id.tvNewsTitle);              //标题
        tvChaKanQuanBu = (TextView) this.findViewById(R.id.tvChaKanQuanBu);         //查看全部
        tvLaiYuanDate = (TextView) this.findViewById(R.id.tvLaiYuanDate);         //发布时间
        tvShangYou = (TextView) this.findViewById(R.id.tvShangYou);                 //上游
        tvZhongJian = (TextView) this.findViewById(R.id.tvZhongJian);               //中间
        tvXiaYou = (TextView) this.findViewById(R.id.tvXiaYou);                      //下游
        tvOneKeyXuan = (TextView) this.findViewById(R.id.tvOneKeyXuan);             //一键自选
        tvListViewFenGe = (TextView) this.findViewById(R.id.tvListViewFenGe);             //listView 上面的分割线
        myWebView = (WebView) this.findViewById(R.id.wvNewDetal);                   // 展示内容的 webView
        myFlowLayout = (FlowLayout) this.findViewById(R.id.FlowLayout);             // 展示标签的流失布局
        llRelevantIndustry = (FrameLayout) this.findViewById(R.id.llRelevantIndustry);     //存放 行业标签的 线性布局
        llDetailYiJianZiXuan = (LinearLayout) this.findViewById(R.id.llDetailYiJianZiXuan); //一键自选的 线性布局
        llXiangGuanTitle = (LinearLayout) this.findViewById(R.id.llXiangGuanTitle); //相关行业上面的  title;
        llListViewTitle = (LinearLayout) this.findViewById(R.id.llListViewTitle); //listView 上面的title
        tvDetailListView = (SimulateListView) this.findViewById(R.id.tvDetailListView);            //展示相关股票的  listView
        initData();

    }

    /**
     * 具体操作
     */
    private void initData() {
        mPullRefreshScrollView.scrollTo(0, 0);
        tvChaKanQuanBu.setOnClickListener(this);
        llDetailYiJianZiXuan.setOnClickListener(this);          //一键自选点击事件
        llNewDetailJiaZai = (LinearLayout) this.findViewById(R.id.llNewDetailJiaZai);   //重新加载图片
        llNewDetailJiaZai.setVisibility(View.GONE);     //初始化  隐藏
        shareDialog  = new ShareDialog(this);
        dialog = LoadingDialogTwo.initDialog(this);       //加载数据的   菊花
        dialog.show();      //显示菊花
        mPullRefreshScrollView.setVisibility(View.GONE);    //隐藏 scrollView

        newsDetailBean = new NewsDetailEntity();
        getData();
//        operation();        //给   UI赋值
//        setIndustry();      //给ind 字段下面的  UI 赋值

        adapter = new EventDetailListAdapter(this);
        tvDetailListView.setAdapter(adapter);
        layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, TransitionUtils.dp2px(40, this));
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
                    myFlowLayout.removeAllViews();   //清空  子View
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                if (beans != null && beans.size() > 0) {
                                    beans.clear();
                                }
                                if (tags != null && tags.size() > 0) {
                                    tags.clear();
                                }
                                getData();
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

    }


    /**
     * 拿到数据后的赋值操作
     */
    private void operation() {
        if (!isShow) {
            return;
        }
        String title = newsDetailBean.title;
        //标题赋值
        tvNewsTitle.setText(newsDetailBean.title);

        /**
         * long  转换 时间
         */
        Long dt = newsDetailBean.dt;
        Date date = new Date(dt);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String time = sdf.format(date);
        tvLaiYuanDate.setText(time);                      //来源时间赋值

        /**
         * webView 的数据
         */
        String newSum = newsDetailBean.sum.replaceAll("\n", "<br>");
        StringBuffer sb = new StringBuffer();

        /*sb.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head><meta charset=\\\"UTF-8\\\"<title></title></head>\n" +
                "<body>\n" +
                "<div style=\"width: 96%;margin-left: 2%;padding:20px 0;text-align:justify;letter-spacing: 0.6px;text-justify:inter-ideograph;line-height: 24px;color:#4c4c4c;font-size: 16px;\">");
        sb.append(newSum);
        sb.append("</div>\n" +
                "</body></html>");*/


        sb.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head><meta charset=\\\"UTF-8\\\"<title></title></head>\n" +
                "<body>\n" +
                "<div style=\"width: 96%;margin-left: 2%;padding:25px 0 0px;text-align:justify;letter-spacing: 0.6px;text-justify:inter-ideograph;line-height: 24px;color:#4c4c4c;font-size: 16px;\">");
        sb.append(newSum);
        sb.append("</div>\n" +
                "</body></html>");

        myWebView.getSettings().setDefaultTextEncodingName("utf-8");
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        myWebView.removeJavascriptInterface("accessibility");
        myWebView.removeJavascriptInterface("accessibilityTraversal");
        myWebView.getSettings().setSavePassword(false);
        myWebView.loadDataWithBaseURL("file://", sb.toString(), "text/html", "utf-8", "");

        /**
         * 相关股票的  listView
         */
        List<NewsDetailEntity.Stocks> stockList = newsDetailBean.stocks;      //存储相关股票的集合
        StockInfoEntity stockInfoEntity = new StockInfoEntity();
        ArrayList<StockInfoEntity> list = new ArrayList<StockInfoEntity>();

        String maker = null;
        if (stockList != null && stockList.size() > 0) {
            tvListViewFenGe.setVisibility(View.VISIBLE);        //listView有数据的话  显示分割线
            llListViewTitle.setVisibility(View.VISIBLE);        //listView有数据的话  显示显示标题
            llDetailYiJianZiXuan.setVisibility(View.VISIBLE);  //一键自选
            StringBuilder codes = new StringBuilder();          //存储 中间列表的入参
            for (int i = 0; i < stockList.size(); i++) {
                NewsDetailEntity.Stocks stocks = stockList.get(i);
                String name = stocks.name;
                String secu = stocks.secu;
                String tick = stocks.tick;

                String market = secu.substring(7, 9);
                if (market.equals("SZ")) {                        //判断是哪个市场的      2
                    maker = "90";
                } else if (market.equals("SH")) {          //1
                    maker = "83";
                }

                String newCode = Helper.getStockCode(secu, maker);
                String subCode = newCode.substring(0, 8);
                codes.append(subCode).append("&");
            }

            if (codes != null && codes.length() > 0) {
                codes.deleteCharAt(codes.length() - 1);
                getListData(codes, stockList);                  //获取中间列表的数据
            }
        } else {
            tvListViewFenGe.setVisibility(View.GONE);       //listView 没数据的话  隐藏标题 和  title  一键自选
            llListViewTitle.setVisibility(View.GONE);
            llDetailYiJianZiXuan.setVisibility(View.GONE);
        }

        tvDetailListView.setOnItemClickListener(new SimulateListView.OnItemClickListener() {
            @Override
            public void onItemClick(SimulateListView parent, View view, int position, long id) {
                StockInfoEntity stockInfoEntity1 = beans.get(position);
                String stockCode = stockInfoEntity1.getStockNumber();
                String stockName = stockInfoEntity1.getStockName();

                Intent intent1 = new Intent();
                intent1.setClass(NewsDetailActivity.this, StockDetailActivity.class);
                StockDetailEntity entity = new StockDetailEntity();
                entity.setStockName(stockName);
                entity.setStockCode(stockCode);
                intent1.putExtra("stockIntent", entity);
                startActivity(intent1);
            }
        });


        /**
         * 给行业标签赋值
         */
        tags = newsDetailBean.tags;
        LinearLayout linearLayout;
        //标签的  颜色
        int[] tagss = {R.drawable.tag01, R.drawable.tag02, R.drawable.tag03, R.drawable.tag04,
                R.drawable.tag05, R.drawable.tag06, R.drawable.tag07, R.drawable.tag08};
        int point = 0;      //设置循环的变量

        for (int i = 0; i < tags.size(); i++) {

            linearLayout = new LinearLayout(this);
            tv_tags = new TextView(this);
            tv_tags.setText(tags.get(i));       //给标签赋值
            tv_tags.setTextSize(13);            //设置字体大小
            tv_tags.setTextColor(ContextCompat.getColor(this, R.color.white));       //设置字体颜色
            tv_tags.setBackgroundResource(tagss[point]);                             //设置背景

            //让背景循环起来
            point++;
            if (point > 7) {
                point = 0;
            }
            //设置边距
            tv_tags.setPadding(TransitionUtils.dp2px(5, this), TransitionUtils.dp2px(3, this),
                    TransitionUtils.dp2px(5, this), TransitionUtils.dp2px(3, this));

            setNewsDetailsTagClick(tv_tags, tv_tags.getText().toString());      //设置标签监听

            linearLayout.setPadding(TransitionUtils.dp2px(10, this), 0, 0, TransitionUtils.dp2px(10, this));
            linearLayout.setLayoutParams(layoutParams);     //设置模式
            linearLayout.addView(tv_tags);                  //添加标签
            myFlowLayout.addView(linearLayout);
        }

    }


    /**
     * 给 行业的 数据  赋值
     */
    private void setIndustry() {
        String samName = newsDetailBean.ind.samName;    // 行业中间  的名称
        List<NewsDetailEntity.Ind.Chain> chainList = newsDetailBean.ind.chain;        //存储上游下游的集合
        tvXiaYou.setText("无");
        tvShangYou.setText("无");
        if (chainList != null & chainList.size() > 0) {
            llXiangGuanTitle.setVisibility(View.VISIBLE);          //如果没数据  相关行业的标题  显示
            llRelevantIndustry.setVisibility(View.VISIBLE);        // 没数据   相关行业 显示
            for (int i = 0; i < chainList.size(); i++) {
                NewsDetailEntity.Ind.Chain chain = chainList.get(i);
                String level = chain.level;
                if ("-1".equals(level)) {
                    tvXiaYou.setText(chain.name);
                } else if ("1".equals(level)) {
                    tvShangYou.setText(chain.name);
                }
            }
        } else {
            llXiangGuanTitle.setVisibility(View.GONE);          //如果没数据  相关行业的标题  隐藏
            llRelevantIndustry.setVisibility(View.GONE);        // 没数据   相关行业 隐藏
        }
        tvZhongJian.setText(samName);
    }


    /**
     * FlowLayout  子View的点击事件
     *
     * @param view    点击的子View
     * @param keyword 要传的值
     */
    private void setNewsDetailsTagClick(View view, final String keyword) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, IndustryRelevanceActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNewsDetail_back:        //销毁详情页
                finish();
                break;
            case R.id.ivDetailFenXiang:         //分享
                loadingDialog = LoadingDialog.initDialog(this, "加载中…");
                loadingDialog.show();       //显示菊花
                getShare();
                break;
            case R.id.llDetailYiJianZiXuan:     //一键 自选
                SelfStockHelper.oneTimiceAddSelfChoice(TAG, "", beans, new OneTimiceAddSelfChoiceListener() {
                    @Override
                    public void getResult(String result) {
                        SelfStockHelper.explanOneTimiceAddSelfChoiceResult(NewsDetailActivity.this, result);
                    }
                });
                break;
            case R.id.tvChaKanQuanBu:       //查看全部
                String url = newsDetailBean.url;
                Intent intent = new Intent();
                intent.setClass(this, NewsDetailMoreActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }
    }
    /**
     * 分享
     */
    private void getShare() {
        String capitalAccount = UserUtil.capitalAccount;                        //注册账户
        String base64 = ScreenShot.shoot((Activity) this);         //截屏 拿到图片的 base64
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("base64", base64);
        map2.put("account", capitalAccount);
        map2.put("type", "2");
        map2.put("phone_type","2");
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
     * 根据  新闻ID  请求数据
     */
    private void getData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("id", requestId);
        map1.put("FUNCTIONCODE", "HQONG003");
        map1.put("TOKEN", "");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                if (dialog != null) {
                    dialog.dismiss();   //关闭  菊花
                }
                mPullRefreshScrollView.setVisibility(View.GONE);    //关闭 scrollView
                llNewDetailJiaZai.setVisibility(View.VISIBLE);     // 显示  加载的图片
                llNewDetailJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llNewDetailJiaZai.setVisibility(View.GONE); //隐藏 重新加载图片
                        dialog.show();
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
                                getData();
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
                    String message = jsonObject.getString("message");       //json中主要数据
                    String type = jsonObject.getString("type");
                    if ("200".equals(code)) {                               //获取code的值判断是否为200
                        newsDetailBean.dt = new JSONObject(message).getLong("dt");        //获取第二层dt的 时间 值
                        ArrayList<NewsDetailEntity.Stocks> alStocks = new ArrayList<NewsDetailEntity.Stocks>();
                        NewsDetailEntity.Stocks stocksBean;                     //相关股票的对象
                        String stocks = new JSONObject(message).getString("stocks");
                        JSONArray joStocks = new JSONArray(stocks);
                        for (int i = 0; i < joStocks.length(); i++) {
                            stocksBean = new NewsDetailEntity().new Stocks();
                            stocksBean.name = new JSONObject(joStocks.getString(i)).getString("name");
                            stocksBean.secu = new JSONObject(joStocks.getString(i)).getString("code");
                            stocksBean.tick = new JSONObject(joStocks.getString(i)).getString("tick");
                            alStocks.add(stocksBean);
                        }
                        newsDetailBean.stocks = alStocks;       //赋值 stocks  存储相关股票的集合
                        newsDetailBean.sum = new JSONObject(message).getString("sum");     //摘要
                        String tags = new JSONObject(message).getString("tags");
                        JSONArray jaAags = new JSONArray(tags);
                        ArrayList<String> as = new ArrayList<String>();
                        for (int i = 0; i < jaAags.length(); i++) {
                            as.add(jaAags.getString(i));
                        }
                        newsDetailBean.tags = as;       //存储标签的集合
                        newsDetailBean.title = new JSONObject(message).getString("title");      //标题
                        newsDetailBean.url = new JSONObject(message).getString("url");          //url

//                        ToastUtils.showShort(NewsDetailActivity.this, indBean.samName);

                        //赋值前关闭  dialog
                        dialog.dismiss();
                        mPullRefreshScrollView.setVisibility(View.VISIBLE);
                        operation();        //给   UI赋值

                        String ind1 = new JSONObject(message).optString("ind");
                        if (null == ind1 || "".equals(ind1)) {
                            llXiangGuanTitle.setVisibility(View.GONE);          //如果没数据  相关行业的标题  隐藏
                            llRelevantIndustry.setVisibility(View.GONE);        // 没数据   相关行业 隐藏

                        } else {
                            JSONObject ind = new JSONObject(new JSONObject(message).optString("ind"));
                            Iterator<String> keys = ind.keys();                 //获取所有的键
                            ArrayList<String> alInd = new ArrayList<String>();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                alInd.add(key);                                 //遍历存储
                            }
                            JSONObject ind_0 = new JSONObject(ind.getString(alInd.get(0)));     //取第一个
                            JSONArray jaChain = ind_0.getJSONArray("chain");
                            NewsDetailEntity.Ind indBean = new NewsDetailEntity().new Ind();
                            ArrayList<NewsDetailEntity.Ind.Chain> alChain = new ArrayList<NewsDetailEntity.Ind.Chain>();
                            NewsDetailEntity.Ind.Chain chaBean;
                            Boolean flag_0 = true;
                            Boolean flag_1 = true;
                            for (int i = 0; i < jaChain.length(); i++) {
                                String level = new JSONObject(jaChain.getString(i)).getString("level");
                                if ("-1".equals(level)) {
                                    if (flag_0) {
                                        String related = new JSONObject(jaChain.getString(i)).getString("related");
                                        JSONObject joRelated = new JSONObject(related);
                                        chaBean = new NewsDetailEntity().new Ind().new Chain();
                                        chaBean.level = level;
                                        chaBean.name = joRelated.getString("name");
                                        alChain.add(chaBean);
                                        flag_0 = false;
                                    }
                                } else if ("1".equals(level)) {
                                    if (flag_1) {
                                        String related = new JSONObject(jaChain.getString(i)).getString("related");
                                        JSONObject joRelated = new JSONObject(related);
                                        chaBean = new NewsDetailEntity().new Ind().new Chain();
                                        chaBean.level = level;
                                        chaBean.name = joRelated.getString("name");
                                        alChain.add(chaBean);
                                        flag_1 = false;
                                    }
                                }
                            }
                            indBean.samName = ind_0.getString("samName");       //中间展示的  行业名称
                            indBean.chain = alChain;                             //存储  上游下游的  对象的集合
                            newsDetailBean.ind = indBean;                       // ind  对象

                            setIndustry();      //给ind 字段下面的  UI 赋值
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 获取listView 的数据
     */
    private void getListData(StringBuilder codes, final List<NewsDetailEntity.Stocks> codes1) {
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

            private String maker;

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
                try {
                    JSONArray array = new JSONArray(response);
                    beans = new ArrayList<StockInfoEntity>();
                    JSONObject res = array.getJSONObject(0);
                    if("0".equals(res.optString("code"))){
                        String totalCount = res.optString("totalCount");
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            //如果只有1条记录，报文格式不为array数组，故进行特殊处理  2017-03-14
                            if("1".equals(totalCount) || jsonArray.length() == 1){
                                StockInfoEntity _bean = new StockInfoEntity();
                                _bean.setStockNumber(jsonArray.getString(0));
                                _bean.setStockName(jsonArray.getString(1));
                                _bean.setTime(jsonArray.getString(2));
                                _bean.setNewPrice(jsonArray.getString(3));
                                _bean.setClose(jsonArray.getString(4));
                                beans.add(_bean);
                            }else{
                                for(int i = 0; i <jsonArray.length();i++){
                                    JSONArray json = jsonArray.getJSONArray(i);
                                    StockInfoEntity _bean = new StockInfoEntity();
                                    _bean.setTotalCount(totalCount);
                                    if(null != json && json.length() > 0){
                                        String mNum = json.getString(0);
                                        String mTime = json.getString(2);
                                        String mNewPrice = json.getString(3);
                                        String mClose = json.getString(4);
                                        for(int k = 0; k < codes1.size();k++){
                                            String mSecu = codes1.get(k).secu;
                                            String mName = codes1.get(k).name;
                                            String market = mSecu.substring(7, 9);
                                            if (market.equals("SZ")) {                        //判断是哪个市场的 2
                                                maker = "90";
                                            } else if (market.equals("SH")) {          //1
                                                maker = "83";
                                            }
                                            String newCode = Helper.getStockCode(mSecu, maker);
                                            String subCode = newCode.substring(0, 8);
                                            if(mNum.equals(subCode)){
                                                _bean.setStockNumber(mNum);
                                                _bean.setStockName(mName);
                                                _bean.setTime(mTime);
                                                _bean.setNewPrice(mNewPrice);
                                                _bean.setClose(mClose);
                                            }
                                        }
                                    }
                                    beans.add(_bean);
                                }
                            }
                        }
                    }
                }catch (JSONException e){
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
//                        if ("0".equals(code)) {
//                            if ("1".equals(totalCount)) {
//                                List<List<String>> data = (List<List<String>>) maps.get("data");
//                                JSONArray jsonArray = new JSONArray(response);
//                                for (int l = 0; l < jsonArray.length(); l++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(l);
//                                    JSONArray data1 = jsonObject.getJSONArray("data");
//                                    StockInfoEntity _bean = new StockInfoEntity();
//                                    data1.getString(0);
//                                    if (!TextUtils.isEmpty(data1.getString(0))) {
//                                        _bean.setStockNumber(data1.getString(0));
//                                    }
//                                    if (!TextUtils.isEmpty(data1.getString(1))) {
//                                        _bean.setStockName(data1.getString(1));
//                                    }
//                                    if (!TextUtils.isEmpty(data1.getString(2))) {
//                                        _bean.setTime(data1.getString(2));
//                                    }
//                                    if (!TextUtils.isEmpty(data1.getString(3))) {
//                                        _bean.setNewPrice(data1.getString(3));
//                                    }
//                                    if (!TextUtils.isEmpty(data1.getString(4))) {
//                                        _bean.setClose(data1.getString(4));
//                                    }
//                                    beans.add(_bean);
//                                }
//                            } else {
//                                List<List<String>> data = (List<List<String>>) maps.get("data");
//                                for (int j = 0; j < data.size(); j++) {
//                                    StockInfoEntity _bean = new StockInfoEntity();
//                                    _bean.setTotalCount(totalCount);
//                                    for (int k = 0; k < data.get(j).size(); k++) {
//                                        for (int l = 0; l < codes1.size(); l++) {
//                                            String mSecu = codes1.get(l).secu;
//                                            String mName = codes1.get(l).name;
//                                            String mData = data.get(j).get(0);
//                                            String mTime = data.get(j).get(2);
//                                            String mNewPrice = data.get(j).get(3);
//                                            String mClose = data.get(j).get(4);
//                                            String market = mSecu.substring(7, 9);
//                                            if (market.equals("SZ")) {                        //判断是哪个市场的      2
//                                                maker = "90";
//                                            } else if (market.equals("SH")) {          //1
//                                                maker = "83";
//                                            }
//                                            String newCode = Helper.getStockCode(mSecu, maker);
//                                            String subCode = newCode.substring(0, 8);
//                                            if (data.get(j).get(0).equals(subCode)) {
//                                                _bean.setStockNumber(mData);
//                                                _bean.setStockName(mName);
//                                                if (!TextUtils.isEmpty(mTime)) {
//                                                    _bean.setTime(mTime);
//                                                }
//                                                if (!TextUtils.isEmpty(mNewPrice)) {
//                                                    _bean.setNewPrice(mNewPrice);
//                                                }
//                                                if (!TextUtils.isEmpty(mClose)) {
//                                                    _bean.setClose(mClose);
//                                                }
//                                            }
//                                        }
//
//
//                                    }
//                                    beans.add(_bean);
//
//                                }
//
//                            }
//
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                adapter.setList(beans);
            }
        });
    }



    private boolean isShow = false;

    @Override
    public void onDestroy() {
        isShow = false;
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

    @Override
    protected void onResume() {
        isShow = true;
        super.onResume();
    }

}
