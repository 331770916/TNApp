package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.tpyzq.mobile.pangu.adapter.trade.PositionAdapter;
import com.tpyzq.mobile.pangu.data.PositionEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.StockCodeCallBack;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.AddPosition;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/8/12.
 * 持仓页签
 */
public class PositionTransactionPager extends BaseTransactionPager implements AdapterView.OnItemClickListener {
    private final String TAG = "PositionTransactionPager";
    StockCodeCallBack stockCodeCallBack;
    public PositionTransactionPager(Context context, StockCodeCallBack stockCodeCallBack) {
        super(context);
        this.stockCodeCallBack = stockCodeCallBack;
    }

    PositionAdapter positionAdapter;
    List<PositionEntity> datas;
    LinearLayout ll_fourtext;
    TextView tv_text1, tv_text2, tv_text3, tv_text4;
    ImageView tv_empty;
    PullToRefreshListView lv_transaction;

    @Override
    public void initData() {
//        getSecuritiesPositions(0, 1);
    }

    private void initEvent() {
        lv_transaction.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_transaction.setEmptyView(tv_empty);
        lv_transaction.setOnItemClickListener(this);
        lv_transaction.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getSecuritiesPositions(0, 1);
            }
        });
    }

    // 设置持仓刷新
    public void setRefresh(){
        getSecuritiesPositions(0, 1);
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
        tv_text1.setText("名称/市值");
        tv_text2.setText("盈亏/比例");
        tv_text3.setText("持仓/可用");
        tv_text4.setText("现价/成本");
        datas = new ArrayList<PositionEntity>();
        positionAdapter = new PositionAdapter(mContext);
        lv_transaction.setAdapter(positionAdapter);
        initEvent();
    }

    private void getSecuritiesPositions(int from, int to) {
        HashMap map300130 = new HashMap();
        map300130.put("funcid", "300200");
        map300130.put("token", SpUtils.getString(mContext, "mSession", null));
        HashMap map300130_1 = new HashMap();
        map300130_1.put("FLAG", true);
        map300130_1.put("SEC_ID", "tpyzq");
        map300130_1.put("KEY_STR", "");
//        map300130_1.put("REC_COUNT", 2);
        map300130.put("parms", map300130_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300130, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(mContext,ConstantUtil.NETWORK_ERROR);
                lv_transaction.onRefreshComplete();
            }

            @Override
            public void onResponse(String response, int id) {
                lv_transaction.onRefreshComplete();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if (code.equals("0")) {
                        JSONArray jsonArray = new JSONArray(data);
                        AddPosition.getInstance().setData(TAG,jsonArray);
                        tempArray = jsonArray;
                        saveMyHoldStkHandler.sendEmptyMessage(0);
                        datas.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PositionEntity positionBean = new Gson().fromJson(jsonArray.getString(i), PositionEntity.class);
                            datas.add(positionBean);
                        }
                        positionAdapter.setDatas(datas);
                        positionAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    JSONArray tempArray = null;
    Handler saveMyHoldStkHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(tempArray!=null){
                try{
                    HOLD_SEQ.deleteAll();
                    ArrayList<StockInfoEntity> stockInfoEntities = new ArrayList<StockInfoEntity>();
                    for (int j = 0; j < tempArray.length(); j++) {
                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                        if (tempArray.getJSONObject(j).getString("MARKET").equals("2")) {
                            stockInfoEntity.setSECU_CODE(Helper.getStockCode(tempArray.getJSONObject(j).getString("SECU_CODE"), "90"));
                        } else {
                            stockInfoEntity.setSECU_CODE(Helper.getStockCode(tempArray.getJSONObject(j).getString("SECU_CODE"), "83"));
                        }
                        stockInfoEntities.add(stockInfoEntity);
                    }
                    HOLD_SEQ.addHoldDatas(stockInfoEntities);
//                    String sss = HOLD_SEQ.getHoldCodes();
//                    LogHelper.e(TAG,"sss:"+sss);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.pager_transaction;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String stockCode = datas.get(position-1).SECU_CODE;
        String market = datas.get(position-1).MARKET;
        if ("2".equals(market)){
            stockCode = Helper.getStockCode(stockCode,"90");
        }else if ("1".equals(market)){
            stockCode = Helper.getStockCode(stockCode,"83");
        }
        stockCodeCallBack.setStockCode(stockCode);
    }
}
