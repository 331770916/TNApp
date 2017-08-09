package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseTransactionPager;
import com.tpyzq.mobile.pangu.adapter.trade.FreeStockAdapter;
import com.tpyzq.mobile.pangu.data.OptionalStockEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.StockCodeCallBack;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/8/11.
 * 自选股页签
 */
public class FreeStockTransactionPager extends BaseTransactionPager implements AdapterView.OnItemClickListener {
    StockCodeCallBack stockCodeCallBack;

    public FreeStockTransactionPager(Context context, StockCodeCallBack stockCodeCallBack) {
        super(context);
        this.stockCodeCallBack = stockCodeCallBack;
    }

    LinearLayout ll_fourtext;
    TextView tv_text1, tv_text2, tv_text3, tv_text4;
    PullToRefreshListView lv_transaction;
    ImageView tv_empty;
    List<OptionalStockEntity> optionalStockBeen;
    FreeStockAdapter freeStockAdapter;

    @Override
    public void initData() {
//        getListData();
    }

    private void initEvent() {
        lv_transaction.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_transaction.setEmptyView(tv_empty);
        lv_transaction.setOnItemClickListener(this);
        lv_transaction.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData();
            }
        });

    }

    public void setRefresh(){
        getListData();
    }
    @Override
    public void setView() {
        ll_fourtext = (LinearLayout) rootView.findViewById(R.id.ll_fourtext);
        tv_text1 = (TextView) ll_fourtext.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) ll_fourtext.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) ll_fourtext.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) ll_fourtext.findViewById(R.id.tv_text4);
        lv_transaction = (PullToRefreshListView) rootView.findViewById(R.id.lv_transaction);
        tv_empty = (ImageView) rootView.findViewById(R.id.tv_empty);
        tv_text1.setText("证券名称");
        tv_text2.setText("现价");
        tv_text3.setText("涨跌幅/额");
        tv_text4.setText("最高/最低");

        optionalStockBeen = new ArrayList<OptionalStockEntity>();
        freeStockAdapter = new FreeStockAdapter(mContext);
        lv_transaction.setAdapter(freeStockAdapter);
        initEvent();
    }

    /**
     * 获取listView 的数据
     */
    private void getListData() {
        ArrayList<StockInfoEntity> stockInfoEntities = new ArrayList<StockInfoEntity>();
        stockInfoEntities = Db_PUB_STOCKLIST.queryStockListDatas();
        StringBuilder codes = new StringBuilder();
        if (stockInfoEntities != null) {
            for (int i = 0; i < stockInfoEntities.size(); i++) {
                if (i != stockInfoEntities.size() - 1) {
                    codes.append(stockInfoEntities.get(i).getStockNumber()).append("&");
                } else {
                    codes.append(stockInfoEntities.get(i).getStockNumber());
                }
            }
        }

        Map map = new HashMap();
        Object[] object = new Object[1];
        Map map2 = new HashMap();
        map2.put("code", codes.toString());
        map2.put("market", "0");
        map2.put("type", "4");
        map2.put("order", "0");
        object[0] = map2;

        Gson gson = new Gson();
        String strJson = gson.toJson(object);
        map.put("FUNCTIONCODE", "HQING005");
        map.put("PARAMS", strJson);
//        LogHelper.e("FreeStockTransactionPager","request:"+map.toString());
        NetWorkUtil.getInstence().okHttpForGet("", ConstantUtil.getURL_HQ_HHN(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                lv_transaction.onRefreshComplete();
                CentreToast.showText(mContext,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                lv_transaction.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                if ("null\n".equals(response)) {
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");
                    int count = TransitionUtils.string2int(jsonObject.optString("totalCount"));
                    if ("0".equals(code)) {
                        JSONArray jaData = new JSONArray(data);
                        optionalStockBeen.clear();
                        if(count==1){
                            OptionalStockEntity optionalStockBean = new OptionalStockEntity();
                            parseArrayList(optionalStockBean,jaData);
                            optionalStockBeen.add(optionalStockBean);
                        }else{
                            int len = jaData.length();
                            for (int i = 0; i < len; i++) {
                                OptionalStockEntity optionalStockBean = new OptionalStockEntity();
                                JSONArray array = jaData.getJSONArray(i);
                                parseArrayList(optionalStockBean,array);
                                optionalStockBeen.add(optionalStockBean);
                            }
                        }

                        freeStockAdapter.setOptionalStockBeen(optionalStockBeen);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void parseArrayList(OptionalStockEntity optionalStockBean,JSONArray jaData) throws JSONException{

        optionalStockBean.stockCode = jaData.getString(0);
        optionalStockBean.stockName = jaData.getString(1);
        optionalStockBean.nowPrice = jaData.getString(3);
        String nowPrice = jaData.getString(3);
        String beforePrice = jaData.getString(4);
        if (Helper.isDecimal(nowPrice) || Helper.isDecimal(beforePrice)) {
            double dnowPrice = TransitionUtils.string2double(nowPrice);
            double dbeforePrice = TransitionUtils.string2double(beforePrice);
            double dTTM = dnowPrice - dbeforePrice;
            double dTTMR = dTTM / dbeforePrice * 100;
            optionalStockBean.TTM = TransitionUtils.fundPirce(optionalStockBean.stockCode, dTTM + "");
            optionalStockBean.TTMR = TransitionUtils.fundPirce(optionalStockBean.stockCode, dTTMR + "") + "%";
        } else {
            optionalStockBean.TTM = "-";
            optionalStockBean.TTMR = "-";
        }
        optionalStockBean.beforePrice = beforePrice;
        optionalStockBean.hign = jaData.getString(5);
        optionalStockBean.low = jaData.getString(6);
    }
    @Override
    public int getLayoutId() {
        return R.layout.pager_transaction;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        stockCodeCallBack.setStockCode(optionalStockBeen.get(position).stockName,optionalStockBeen.get(position).stockCode);
    }
}
