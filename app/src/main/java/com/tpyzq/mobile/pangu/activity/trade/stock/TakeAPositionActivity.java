package com.tpyzq.mobile.pangu.activity.trade.stock;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.data.TakeAPositionEntity;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.AddPosition;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by wangqi on 2016/7/26.
 * 持仓  页面
 */
public class TakeAPositionActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private String TAG = "TakeAPositionActivity";
    private int mExpandedMenuPos = -1;
    private MyAdapter adapter;
    private TextView mZczjAmount, mSzAmount, mKyAmount, mCcykAmount, mKqAmount;
    private LinearLayout mBackground;
    private List<TakeAPositionEntity> beans;
    private ImageView iv_isEmpty;
    private AutoListview mCcListView;
    private ScrollView scroll_view;
    private RelativeLayout RelativeLayout_color_1, RelativeLayout_color_2;

    private PullToRefreshScrollView  mPullToRefreshScrollView;

    @Override
    public void initView() {

        beans = new ArrayList<TakeAPositionEntity>();
        findViewById(R.id.detail_back).setOnClickListener(this);
        mBackground = (LinearLayout) findViewById(R.id.LL);
//        scroll_view = (ScrollView)findViewById(R.id.takeapostion_scroll);
        mPullToRefreshScrollView  = (PullToRefreshScrollView) findViewById(R.id.svPullToRefresh);
        RelativeLayout_color_1 = (RelativeLayout) findViewById(R.id.rl_top_bar);
        RelativeLayout_color_2 = (RelativeLayout) findViewById(R.id.RelativeLayout_color_2);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);
        mZczjAmount = (TextView) findViewById(R.id.zczjAmount);
        mCcykAmount = (TextView) findViewById(R.id.ccykAmount);
        mSzAmount = (TextView) findViewById(R.id.szAmount);
        mKyAmount = (TextView) findViewById(R.id.kyAmount);
        mKqAmount = (TextView) findViewById(R.id.kqAmount);
        mCcListView = (AutoListview) findViewById(R.id.ccListView);
        adapter = new MyAdapter();
        mCcListView.setAdapter(adapter);
        mCcListView.setEmptyView(iv_isEmpty);
        mCcListView.setOnItemClickListener(this);
        toConnect(false);
        initEvent();
    }

    private void initEvent() {
        mPullToRefreshScrollView.setFocusableInTouchMode(true);
        scroll_view = mPullToRefreshScrollView.getRefreshableView();   // 获取当前PullToRefreshScrollView的子view
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                toConnect(true);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mExpandedMenuPos) {
            mExpandedMenuPos = -1;
        } else {
            mExpandedMenuPos = position;
        }
        adapter.notifyDataSetChanged();
        if (beans.size()-1==position) {
            scroll_view.post(new Runnable() {
                @Override
                public void run() {
                    scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.detail_back) {
            finish();
        }
    }

    JSONArray tempArray = null;

    private void toConnect(final boolean isClean) {
        String mSession = SpUtils.getString(this, "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300201");
        map.put("token", mSession);
        map.put("parms", map2);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("MARKET", "");
        map2.put("SECU_CODE", "");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mPullToRefreshScrollView.onRefreshComplete();
                LogHelper.e(TAG, e.toString());
                if (isClean){
                    mExpandedMenuPos = -1;
                }
                CentreToast.showText(TakeAPositionActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                mPullToRefreshScrollView.onRefreshComplete();
                if (isClean){
                    mExpandedMenuPos = -1;
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        beans.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                if (jsonArray.getJSONObject(i).getString("ASSERT_VAL").equals("0")) {
                                    mZczjAmount.setText("0.00");
                                } else {
                                    mZczjAmount.setText(jsonArray.getJSONObject(i).getString("ASSERT_VAL"));
                                }


                                mSzAmount.setText(jsonArray.getJSONObject(i).getString("MKT_VAL"));
                                mKyAmount.setText(jsonArray.getJSONObject(i).getString("AVAILABLE"));
                                mKqAmount.setText(jsonArray.getJSONObject(i).getString("DRAW_AMT"));
                                String allProfitLoss = jsonArray.getJSONObject(i).getString("TOTAL_INCOME_BAL");
                                if (!TextUtils.isEmpty(allProfitLoss)) {
                                    if (allProfitLoss.contains("-")) {
                                        mCcykAmount.setText(TransitionUtils.string2doubleS(allProfitLoss));
                                        mBackground.setBackgroundColor(Color.parseColor("#27aa46"));
                                        RelativeLayout_color_1.setBackgroundColor(Color.parseColor("#27aa46"));
                                        RelativeLayout_color_2.setBackgroundColor(Color.parseColor("#27aa46"));
                                    } else if (allProfitLoss.equals("0.00")) {
                                        mCcykAmount.setText(TransitionUtils.string2doubleS(allProfitLoss));
                                        mBackground.setBackgroundColor(Color.parseColor("#999999"));
                                        RelativeLayout_color_1.setBackgroundColor(Color.parseColor("#999999"));
                                        RelativeLayout_color_2.setBackgroundColor(Color.parseColor("#999999"));

                                    } else {
                                        mCcykAmount.setText("+" + TransitionUtils.string2doubleS(allProfitLoss));
                                        mBackground.setBackgroundColor(Color.parseColor("#e84242"));
                                        RelativeLayout_color_1.setBackgroundColor(Color.parseColor("#e84242"));
                                        RelativeLayout_color_2.setBackgroundColor(Color.parseColor("#e84242"));
                                    }
                                } else {
                                    mCcykAmount.setText("0.00");
                                    mBackground.setBackgroundColor(Color.parseColor("#999999"));
                                    RelativeLayout_color_1.setBackgroundColor(Color.parseColor("#999999"));
                                    RelativeLayout_color_2.setBackgroundColor(Color.parseColor("#999999"));
                                }

                                JSONArray income_list = jsonArray.getJSONObject(i).getJSONArray("INCOME_LIST");
                                AddPosition.getInstance().setData(TAG,income_list);
                                tempArray = income_list;
                                saveMyHoldStkHandler.sendEmptyMessage(0);
                                if (income_list != null && income_list.length() > 0) {
                                    for (int j = 0; j < income_list.length(); j++) {
                                        TakeAPositionEntity _bean = new TakeAPositionEntity();
                                        _bean.setTransactionName(income_list.getJSONObject(j).getString("SECU_NAME"));                    //名称
                                        _bean.setTransactionNumber(income_list.getJSONObject(j).getString("MKT_VAL"));                    //市值
                                        _bean.setTransactionProfit(income_list.getJSONObject(j).getString("INCOME_AMT"));                  //盈亏
                                        _bean.setTransactionProfit1(income_list.getJSONObject(j).getString("PROFIT_RATIO"));               // /%
                                        _bean.setTransactionPositions(income_list.getJSONObject(j).getString("SHARE_QTY"));                //持仓
                                        _bean.setTransactionPositions1(income_list.getJSONObject(j).getString("SHARE_AVL"));               //可用
                                        _bean.setTransactionPositionsCurrentPrice(income_list.getJSONObject(j).getString("MKT_PRICE"));    //现价
                                        _bean.setTransactionPositionsCurrentPrice1(income_list.getJSONObject(j).getString("CURRENT_COST"));//成本
                                        if (income_list.getJSONObject(j).getString("MARKET").equals("2")) {
                                            _bean.setmCODE(Helper.getStockCode(income_list.getJSONObject(j).getString("SECU_CODE").toString(), "90"));
                                        } else {
                                            _bean.setmCODE(Helper.getStockCode(income_list.getJSONObject(j).getString("SECU_CODE").toString(), "83"));
                                        }
                                        beans.add(_bean);
                                    }
                                }
                            }
                        }
                        adapter.setData(beans);
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        startActivity(new Intent(TakeAPositionActivity.this, TransactionLoginActivity.class));
                    } else {
                        CentreToast.showText(TakeAPositionActivity.this,ConstantUtil.NETWORK_ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CentreToast.showText(TakeAPositionActivity.this,ConstantUtil.JSON_ERROR);
                }
            }
        });
    }

    Handler saveMyHoldStkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (tempArray != null) {
                try {
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
        return R.layout.activity_takeaposition;
    }


    class MyAdapter extends BaseAdapter {
        List<TakeAPositionEntity> mSetText;

        public void setData(List<TakeAPositionEntity> setText) {
            mSetText = setText;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mSetText != null && mSetText.size() > 0) {
                return mSetText.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mSetText.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler = null;
            if (convertView == null) {
                viewHodler = new ViewHodler();
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.takeaposition_item, null);
                viewHodler.takeapositionItem0 = (LinearLayout) convertView.findViewById(R.id.item_menu);
                viewHodler.takeapositionItem1 = (TextView) convertView.findViewById(R.id.takeaPositionItem_Name);
                viewHodler.takeapositionItem2 = (TextView) convertView.findViewById(R.id.takeaPosition_NameItem_Num);
                viewHodler.takeapositionItem3 = (TextView) convertView.findViewById(R.id.takeaPositionItem_YingKui);
                viewHodler.takeapositionItem4 = (TextView) convertView.findViewById(R.id.takeaPositionItem_Percent);
                viewHodler.takeapositionItem5 = (TextView) convertView.findViewById(R.id.takeaPositionItem_ChiCangKeYong);
                viewHodler.takeapositionItem6 = (TextView) convertView.findViewById(R.id.takeaPositionItem_ChiCangKeYong1);
                viewHodler.takeapositionItem7 = (TextView) convertView.findViewById(R.id.takeaPositionItem_XianJiaChengBen);
                viewHodler.takeapositionItem8 = (TextView) convertView.findViewById(R.id.takeaPositionItem_XianJiaChengBen1);

                viewHodler.takeapositionItemBut1 = (TextView) convertView.findViewById(R.id.item_Buy);
                viewHodler.takeapositionItemBut2 = (TextView) convertView.findViewById(R.id.item_Market);
                viewHodler.takeapositionItemBut3 = (TextView) convertView.findViewById(R.id.item_Sale);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            DecimalFormat mFormat1 = new DecimalFormat("#0.000");
            viewHodler.takeapositionItem0.setVisibility(position == mExpandedMenuPos ? View.VISIBLE : View.GONE);

            if (!TextUtils.isEmpty(mSetText.get(position).getTransactionName()) && !"0".equals(mSetText.get(position).getTransactionName())) {
                viewHodler.takeapositionItem1.setText(mSetText.get(position).getTransactionName());
            } else {
                viewHodler.takeapositionItem1.setText("0.00");
            }

            viewHodler.takeapositionItem2.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionNumber())));

            if (!TextUtils.isEmpty(mSetText.get(position).getTransactionProfit()) && !"0".equals(mSetText.get(position).getTransactionProfit())) {
                if (mSetText.get(position).getTransactionProfit().contains("-")) {
                    viewHodler.takeapositionItem3.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit())));   //三位小数
                    viewHodler.takeapositionItem3.setTextColor(Color.parseColor("#27aa46"));

                } else {
                    viewHodler.takeapositionItem3.setText("+" + mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit())));
                    viewHodler.takeapositionItem3.setTextColor(Color.parseColor("#e84242"));
                }
            } else {
                viewHodler.takeapositionItem3.setText("0.000");
                viewHodler.takeapositionItem3.setTextColor(Color.parseColor("#999999"));
            }


            if (!TextUtils.isEmpty(mSetText.get(position).getTransactionProfit1()) && !"0".equals(mSetText.get(position).getTransactionProfit1())) {
                if (mSetText.get(position).getTransactionProfit1().contains("-")) {
                    viewHodler.takeapositionItem4.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit1())) + "%");
                    viewHodler.takeapositionItem4.setTextColor(Color.parseColor("#27aa46"));
                } else {
                    viewHodler.takeapositionItem4.setText("+" + mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit1())) + "%");
                    viewHodler.takeapositionItem4.setTextColor(Color.parseColor("#e84242"));
                }
            } else {
                viewHodler.takeapositionItem4.setText("0.000%");
                viewHodler.takeapositionItem4.setTextColor(Color.parseColor("#999999"));
            }

            String TransactionPositions = mSetText.get(position).getTransactionPositions();
            if (!TextUtils.isEmpty(TransactionPositions) && !"0".equals(TransactionPositions)) {
                int idx = TransactionPositions.lastIndexOf(".");//查找小数点的位置
                String strNum = TransactionPositions.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
                viewHodler.takeapositionItem5.setText(strNum);
            } else {
                viewHodler.takeapositionItem5.setText("0");
            }


            String TransactionPositions1 = mSetText.get(position).getTransactionPositions1();
            if (!TextUtils.isEmpty(TransactionPositions1) && !"0".equals(TransactionPositions1)) {
                int idx = TransactionPositions1.lastIndexOf(".");//查找小数点的位置
                String strNum = TransactionPositions1.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
                viewHodler.takeapositionItem6.setText(strNum);
            } else {
                viewHodler.takeapositionItem6.setText("0");
            }

            String TransactionPositionsCurrentPrice = mSetText.get(position).getTransactionPositionsCurrentPrice();
            try {
                if (!TextUtils.isEmpty(TransactionPositionsCurrentPrice) && !"0".equals(TransactionPositionsCurrentPrice)) {
                    viewHodler.takeapositionItem7.setText(String.format("%.3f", Double.parseDouble(TransactionPositionsCurrentPrice)));
                } else {
                    viewHodler.takeapositionItem7.setText(TransitionUtils.string2doubleS3("0.000"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String TransactionPositionsCurrentPrice1 = mSetText.get(position).getTransactionPositionsCurrentPrice1();
            if (!TextUtils.isEmpty(TransactionPositionsCurrentPrice1) && !"0".equals(TransactionPositionsCurrentPrice1)) {
                viewHodler.takeapositionItem8.setText(TransactionPositionsCurrentPrice1);
            } else {
                viewHodler.takeapositionItem8.setText("0.000");
            }

            viewHodler.takeapositionItemBut1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(TakeAPositionActivity.this, BuyAndSellActivity.class);
                    intent.putExtra("stockcode", mSetText.get(position).getmCODE());
                    intent.putExtra("status", "买");
                    startActivity(intent);
                }
            });
            viewHodler.takeapositionItemBut2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(TakeAPositionActivity.this, StockDetailActivity.class);

                    StockDetailEntity data = new StockDetailEntity();
                    data.setStockName(mSetText.get(position).getTransactionName());
                    data.setStockCode(mSetText.get(position).getmCODE());
                    intent.putExtra("stockIntent", data);
                    startActivity(intent);
                }
            });
            viewHodler.takeapositionItemBut3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(TakeAPositionActivity.this, BuyAndSellActivity.class);
                    intent.putExtra("stockcode", mSetText.get(position).getmCODE());
                    intent.putExtra("status", "卖");
                    startActivity(intent);
                }
            });
            return convertView;
        }


        class ViewHodler {
            LinearLayout takeapositionItem0;
            TextView takeapositionItem1;
            TextView takeapositionItem2;
            TextView takeapositionItem3;
            TextView takeapositionItem4;
            TextView takeapositionItem5;
            TextView takeapositionItem6;
            TextView takeapositionItem7;
            TextView takeapositionItem8;
            TextView takeapositionItemBut1;
            TextView takeapositionItemBut2;
            TextView takeapositionItemBut3;
        }
    }

}