package com.tpyzq.mobile.pangu.activity.trade;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BanksTransferAccountsActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BuyAndSellActivity;
import com.tpyzq.mobile.pangu.adapter.trade.StayPaymentAdapter;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.data.StayPaymentBean;
import com.tpyzq.mobile.pangu.data.SubscribeHistoryFlowBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.tpyzq.mobile.pangu.view.progress.RoundProgressBar;
import com.tpyzq.mobile.pangu.view.pullDownGroup.PullDownElasticImp;
import com.tpyzq.mobile.pangu.view.pullDownGroup.PullDownScrollView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 刘泽鹏 on 2016/8/10.
 * 待缴款  Fragment
 */
public class StayPaymentFragment extends BaseFragment implements  View.OnClickListener {
    private static final String TAG = "StayPaymentFragment";
    private PullToRefreshScrollView mPullDownScrollView;         //下拉刷新
    private MyListView listView;
    private TextView tvDaiJiaoJinE, tvRenGouSuoXu = null;      //待缴金额   认购所需
    private RoundProgressBar mRoundProgressBar;             //圆形进度条
    private int progress = 0;                                //进度
    private int money;                                       //认购所需
    private int short_money;                                //未缴金额
    private String mSession;
    private StayPaymentAdapter adapter;
    private ImageView ivStayPaymentKong;
    private LinearLayout hdader;


    @Override
    public void initView(View view) {
        mSession = SpUtils.getString(getContext(), "mSession", "");
        getData(mSession);

        ivStayPaymentKong = (ImageView) view.findViewById(R.id.ivStayPaymentKong);      //没数据的时候的  空 图片
        hdader = (LinearLayout) view.findViewById(R.id.Hdader);
        tvDaiJiaoJinE = (TextView) view.findViewById(R.id.tvDaiJiaoJinE);
        tvRenGouSuoXu = (TextView) view.findViewById(R.id.tvRenGouSuoXu);
        mPullDownScrollView = (PullToRefreshScrollView) view.findViewById(R.id.xiaLaShuaXin);
        //更新进度条
        mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar2);
        if (short_money == 0 || money == 0) {
            mRoundProgressBar.setProgress(0);
        } else {
            int per = short_money % money;
            mRoundProgressBar.setProgress(per);
        }

        view.findViewById(R.id.llZhuanRu).setOnClickListener(this);
        view.findViewById(R.id.llZhuanChu).setOnClickListener(this);

        mPullDownScrollView.getRefreshableView().scrollTo(0,0);
        this.listView = (MyListView) view.findViewById(R.id.lvStayPayment);
        adapter = new StayPaymentAdapter(getContext());
        this.listView.setEmptyView(ivStayPaymentKong);
        this.listView.setAdapter(adapter);

        initScrollView(); //下拉刷新初始化

    }

    private void initScrollView() {
        mPullDownScrollView.setFocusableInTouchMode(true);
        mPullDownScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullDownScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        progress = 0;
                        money = 0;
                        short_money = 0;
                        getData(mSession);
                        mPullDownScrollView.onRefreshComplete();
                    }
                }, 2000);
            }
        });
    }


    /**
     * 获取listView 中的数据
     */
    private void getData(final String session) {
        final Map map3 = new HashMap();
        Map map4 = new HashMap();
        map4.put("SEC_ID", "tpyzq");
        map4.put("FLAG", "true");
        map3.put("funcid", "300387");
        map3.put("token", session);
        map3.put("parms", map4);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map3, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                listView.setVisibility(View.GONE);                //如果请求数据失败   隐藏listView
                ivStayPaymentKong.setVisibility(View.VISIBLE);  //显示 空
            }

            @Override
            public void onResponse(String response, int id) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                if (!response.equals("") && response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<StayPaymentBean>() {
                    }.getType();
                    StayPaymentBean bean = gson.fromJson(response, type);
                    String code = bean.getCode();
                    String msg = bean.getMsg();
                    List<StayPaymentBean.DataBean> data = bean.getData();
                    if (code.equals("-6")) {
                        Intent intent = new Intent(getContext(), TransactionLoginActivity.class);
                        getContext().startActivity(intent);
                        getActivity().finish();
                    } else if (!"0".equals(code)) {
                        listView.setVisibility(View.GONE);                //如果请求数据失败   隐藏listView
                        ivStayPaymentKong.setVisibility(View.VISIBLE);  //显示 空
                    }

                    if (code != null && code.equals("0")) {

                        if (data.size() == 0) {
                            listView.setVisibility(View.GONE);                //如果请求数据失败   隐藏listView
                            ivStayPaymentKong.setVisibility(View.VISIBLE);  //显示 空
                        }

                        for (int i = 0; i < data.size(); i++) {
                            StayPaymentBean.DataBean dataBean = data.get(i);
                            String total_money = dataBean.getTOTAL_MONEY();                 //总金额 ,认购所需
                            String total_short_money = dataBean.getTOTAL_SHORT_MONEY();     //总缺口金额 , 未缴金额
                            List<StayPaymentBean.DataBean.IPOLISTBean> ipo_list = dataBean.getIPO_LIST();
                            for (int j = 0; j < ipo_list.size(); j++) {
                                StayPaymentBean.DataBean.IPOLISTBean ipolistBean = ipo_list.get(j);
                                HashMap<String, String> map = new HashMap<String, String>();
                                String stock_name = ipolistBean.getSTOCK_NAME();                      //股票名称
                                String stock_code = ipolistBean.getSTOCK_CODE();                      //股票代码
                                String init_date = ipolistBean.getINIT_DATE();                      //交易日期  申购日期
                                String issue_price = ipolistBean.getISSUE_PRICE();                    //发行价    申购价格
                                String total_amount = ipolistBean.getTOTAL_AMOUNT();                  //申购股数
                                String ipo_lucky_amount = ipolistBean.getIPO_LUCKY_AMOUNT();          //中签数量
                                String stock_step = ipolistBean.getSTOCK_STEP();                      //状态  今日申购
                                String t1 = ipolistBean.getT1();                                      //配号日期
                                String t2 = ipolistBean.getT2();                                        //中签日期
                                map.put("stock_name", stock_name);                 //股票名称
                                map.put("stock_code", stock_code);                 //股票代码
                                map.put("entrust_date", init_date);               //申购日期
                                map.put("entrust_price", issue_price);            //申购价格
                                map.put("entrust_amount", total_amount);          //申购数量
                                map.put("occur_amount", ipo_lucky_amount);        //中签数量
                                if (!TextUtils.isEmpty(stock_step)) {
                                    switch (stock_step) {
                                        case "0":
                                            map.put("stock_step", "今日申购");                //状态
                                            break;
                                        case "1":
                                            map.put("stock_step", "配号");                //状态
                                            break;
                                        case "2":
                                            map.put("stock_step", "中签");                //状态
                                            break;
                                    }
                                }
                                map.put("t1", t1);                                 //配号日期
                                map.put("t2", t2);                                 //中签日期

                                //如果 状态为  0  的时候 只请求 中签
                                if ("0".equals(stock_step)) {
                                    if (!TextUtils.isEmpty(stock_code)) {
                                        getDtor(stock_code, session, map, list);
                                    }
                                } else if ("1".equals(stock_step) || "2".equals(stock_step)) {
                                    if (!TextUtils.isEmpty(stock_code) && !TextUtils.isEmpty(init_date)) {
                                        getMsg(stock_code, init_date, session, map, list);
                                    }
                                }
                                list.add(map);
                            }

                            //如果 认购所需 和未交金额   都为 0.00 时  隐藏头布局
                            if ("0.00".equals(total_money) && "0.00".equals(total_short_money)) {
                                hdader.setVisibility(View.GONE);
                            } else {
                                hdader.setVisibility(View.VISIBLE);
                            }

                            if (total_money != null && total_money.length() > 0) {
                                Integer totalMoney = Integer.valueOf(total_money.substring(0, total_money.indexOf(".")));
                                money = totalMoney;
                            }
                            if (total_short_money != null && total_short_money.length() > 0) {
                                Integer totalShortMoney = Integer.valueOf(total_short_money.substring(0, total_short_money.indexOf(".")));
                                short_money = totalShortMoney;
                            }
                        }

                    } else {
                        MistakeDialog.showDialog(msg, getActivity());
                    }
                    adapter.setList(list);
                }

                DecimalFormat mFormat1 = new DecimalFormat("#0.000");
                tvDaiJiaoJinE.setText(mFormat1.format(Double.parseDouble(String.valueOf(short_money))));     //给代缴金额  赋值
                tvRenGouSuoXu.setText(mFormat1.format(Double.parseDouble(String.valueOf(money))));           //给认购所需  赋值
            }
        });
    }

    /**
     * 获取申购流程数据
     */
    private void getMsg(final String stockCode, String initDate, final String session, final HashMap<String, String> map
            , final ArrayList<HashMap<String, String>> list) {
        Map map5 = new HashMap();
        Map map6 = new HashMap();
        map6.put("SEC_ID", "tpyzq");
        map6.put("FLAG", "true");
        map6.put("STOCK_CODE", stockCode);
        map6.put("ENTRUST_DATE", initDate);
        map5.put("funcid", "300384");
        map5.put("token", session);
        map5.put("parms", map6);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map5, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                Type type = new TypeToken<SubscribeHistoryFlowBean>() {
                }.getType();
                SubscribeHistoryFlowBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                List<SubscribeHistoryFlowBean.DataBean> data = bean.getData();
//                if (code.equals("-6")) {
//                    Intent intent = new Intent(getContext(), TransactionLoginActivity.class);
//                    getContext().startActivity(intent);
//                    getActivity().finish();
//                } else
                if (code != null && code.equals("0")) {
                    for (int i = 0; i < data.size(); i++) {
                        SubscribeHistoryFlowBean.DataBean dataBean = data.get(i);
                        map.put("remark", dataBean.getREMARK());                        // 起始号
                        map.put("business_amount", dataBean.getBUSINESS_AMOUNT());     //配号数量
                        map.put("next_trade_date", dataBean.getNEXT_TRADE_DATE());     //配好日期
                    }
                } else {
                    MistakeDialog.showDialog(msg, getActivity());
                }
                if (!TextUtils.isEmpty(stockCode)){
                    getDtor(stockCode, session, map, list);
                }
                mPullDownScrollView.getRefreshableView().scrollTo(0,0);
            }
        });

    }


    /**
     * 中签
     */
    private void getDtor(String stockCode, String session, final HashMap<String, String> map
            , final ArrayList<HashMap<String, String>> list) {
        //转换股票 代码
        String STOCK_CODE = stockCode;
        String start = "";
        if (STOCK_CODE.startsWith("730")) {
            start = "600";
        } else if (STOCK_CODE.startsWith("780")) {
            start = "601";
        } else if (STOCK_CODE.startsWith("732")) {
            start = "603";
        } else {
            start = STOCK_CODE.substring(0, 3);
        }
        STOCK_CODE = start + STOCK_CODE.substring(3, 6);

        Map map5 = new HashMap();
        Map map6 = new HashMap();
        map6.put("secucode", STOCK_CODE);
        map5.put("funcid", "100210");
        map5.put("token", session);
        map5.put("parms", map6);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map5, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                String issueresultpubldate = item.getString("ISSUERESULTPUBLDATE");     //中签时间
                                String lotrateonline = item.getString("LOTRATEONLINE");     //中签率
                                String listdate = item.getString("LISTDATE");       //上市 时间
                                map.put("lotrateonline", lotrateonline);
                                map.put("listdate", listdate);
                                if(!TextUtils.isEmpty(issueresultpubldate)){
                                    map.put("ISSUERESULTPUBLDATE", Helper.getMyDate(issueresultpubldate));
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_stay_payment;
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.llZhuanRu:
                intent.setClass(getContext(), BanksTransferAccountsActivity.class);
                intent.putExtra("tag", "100");
                startActivity(intent);
                break;
            case R.id.llZhuanChu:
                intent.setClass(getContext(), BuyAndSellActivity.class);
                intent.putExtra("status", "卖");
                startActivity(intent);
                break;
        }
    }
}
