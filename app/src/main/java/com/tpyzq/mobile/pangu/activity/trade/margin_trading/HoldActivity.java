package com.tpyzq.mobile.pangu.activity.trade.margin_trading;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BuyAndSellActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TakeAPositionActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.TakeAPositionEntity;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 14/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: 融资融券 持仓 activity_hold
 */

public class HoldActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout RelativeLayout_color_1, RelativeLayout_color_2;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private int mExpandedMenuPos = -1;
    private LinearLayout mBackground;
    private ImageView iv_isEmpty;
    private TextView wcdbbl,ccyk,zsz,jzc,fzze;
    private AutoListview listview;
    @Override
    public void initView() {
        findViewById(R.id.detail_back).setOnClickListener(this);
        mPullToRefreshScrollView  = (PullToRefreshScrollView) findViewById(R.id.svPullToRefresh);
        mBackground = (LinearLayout) findViewById(R.id.LL);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);
        RelativeLayout_color_1 = (RelativeLayout) findViewById(R.id.rl_top_bar);
        RelativeLayout_color_2 = (RelativeLayout) findViewById(R.id.RelativeLayout_color_2);
        ((TextView)findViewById(R.id.CC_Text1)).setText("维持担保比例");
        ((TextView)findViewById(R.id.CC_Text3)).setText("总市值");
        ((TextView)findViewById(R.id.CC_Text4)).setText("净资产");
        ((TextView)findViewById(R.id.CC_Text5)).setText("负债总额");
        wcdbbl = (TextView)findViewById(R.id.zczjAmount);
        ccyk = (TextView)findViewById(R.id.ccykAmount);
        zsz = (TextView)findViewById(R.id.szAmount);
        jzc = (TextView)findViewById(R.id.kyAmount);
        fzze = (TextView)findViewById(R.id.kqAmount);
        listview = (AutoListview)findViewById(R.id.ccListView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_back:
                finish();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_takeaposition;
    }

    class MyAdapter extends BaseAdapter {
        List<Map<String,String>> mSetText;

        public void setData(List<Map<String,String>> setText) {
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
                convertView.findViewById(R.id.item_menu).setVisibility(View.GONE);
                viewHodler.takeapositionItem0 = (LinearLayout) convertView.findViewById(R.id.item_menu1);
                viewHodler.takeapositionItem1 = (TextView) convertView.findViewById(R.id.takeaPositionItem_Name);
                viewHodler.takeapositionItem2 = (TextView) convertView.findViewById(R.id.takeaPosition_NameItem_Num);
                viewHodler.takeapositionItem3 = (TextView) convertView.findViewById(R.id.takeaPositionItem_YingKui);
                viewHodler.takeapositionItem4 = (TextView) convertView.findViewById(R.id.takeaPositionItem_Percent);
                viewHodler.takeapositionItem5 = (TextView) convertView.findViewById(R.id.takeaPositionItem_ChiCangKeYong);
                viewHodler.takeapositionItem6 = (TextView) convertView.findViewById(R.id.takeaPositionItem_ChiCangKeYong1);
                viewHodler.takeapositionItem7 = (TextView) convertView.findViewById(R.id.takeaPositionItem_XianJiaChengBen);
                viewHodler.takeapositionItem8 = (TextView) convertView.findViewById(R.id.takeaPositionItem_XianJiaChengBen1);

                viewHodler.deliveryImageView = (ImageView) convertView.findViewById(R.id.deliveryImageView);
                viewHodler.mjhkLayout = (LinearLayout) convertView.findViewById(R.id.mjhkLayout);
                viewHodler.dbpmcLayout = (LinearLayout) convertView.findViewById(R.id.dbpmcLayout);
                viewHodler.dbpzcLayout = (LinearLayout) convertView.findViewById(R.id.dbpzcLayout);
                viewHodler.rzmrLayout = (LinearLayout) convertView.findViewById(R.id.rzmrLayout);
                viewHodler.dbpmrLayout = (LinearLayout) convertView.findViewById(R.id.dbpmrLayout);
                viewHodler.dbpzrLayout = (LinearLayout) convertView.findViewById(R.id.dbpzrLayout);
                viewHodler.rqmcLayout = (LinearLayout) convertView.findViewById(R.id.rqmcLayout);
                viewHodler.hqLayout = (LinearLayout) convertView.findViewById(R.id.hqLayout);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            Map<String,String> bean = mSetText.get(position);
            DecimalFormat mFormat1 = new DecimalFormat("#0.000");
            viewHodler.takeapositionItem0.setVisibility(position == mExpandedMenuPos ? View.VISIBLE : View.GONE);
//            if (!TextUtils.isEmpty(mSetText.get(position).getTransactionName()) && !"0".equals(mSetText.get(position).getTransactionName())) {
//                viewHodler.takeapositionItem1.setText(mSetText.get(position).getTransactionName());
//            } else {
//                viewHodler.takeapositionItem1.setText("0.00");
//            }
//
//            viewHodler.takeapositionItem2.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionNumber())));
//
//            if (!TextUtils.isEmpty(mSetText.get(position).getTransactionProfit()) && !"0".equals(mSetText.get(position).getTransactionProfit())) {
//                if (mSetText.get(position).getTransactionProfit().contains("-")) {
//                    viewHodler.takeapositionItem3.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit())));   //三位小数
//                    viewHodler.takeapositionItem3.setTextColor(Color.parseColor("#27aa46"));
//
//                } else {
//                    viewHodler.takeapositionItem3.setText("+" + mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit())));
//                    viewHodler.takeapositionItem3.setTextColor(Color.parseColor("#e84242"));
//                }
//            } else {
//                viewHodler.takeapositionItem3.setText("0.000");
//                viewHodler.takeapositionItem3.setTextColor(Color.parseColor("#999999"));
//            }
//            if (!TextUtils.isEmpty(mSetText.get(position).getTransactionProfit1()) && !"0".equals(mSetText.get(position).getTransactionProfit1())) {
//                if (mSetText.get(position).getTransactionProfit1().contains("-")) {
//                    viewHodler.takeapositionItem4.setText(mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit1())) + "%");
//                    viewHodler.takeapositionItem4.setTextColor(Color.parseColor("#27aa46"));
//                } else {
//                    viewHodler.takeapositionItem4.setText("+" + mFormat1.format(Double.parseDouble(mSetText.get(position).getTransactionProfit1())) + "%");
//                    viewHodler.takeapositionItem4.setTextColor(Color.parseColor("#e84242"));
//                }
//            } else {
//                viewHodler.takeapositionItem4.setText("0.000%");
//                viewHodler.takeapositionItem4.setTextColor(Color.parseColor("#999999"));
//            }
//            String TransactionPositions = mSetText.get(position).getTransactionPositions();
//            if (!TextUtils.isEmpty(TransactionPositions) && !"0".equals(TransactionPositions)) {
//                int idx = TransactionPositions.lastIndexOf(".");//查找小数点的位置
//                String strNum = TransactionPositions.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
//                viewHodler.takeapositionItem5.setText(strNum);
//            } else {
//                viewHodler.takeapositionItem5.setText("0");
//            }
//            String TransactionPositions1 = mSetText.get(position).getTransactionPositions1();
//            if (!TextUtils.isEmpty(TransactionPositions1) && !"0".equals(TransactionPositions1)) {
//                int idx = TransactionPositions1.lastIndexOf(".");//查找小数点的位置
//                String strNum = TransactionPositions1.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
//                viewHodler.takeapositionItem6.setText(strNum);
//            } else {
//                viewHodler.takeapositionItem6.setText("0");
//            }
//
//            String TransactionPositionsCurrentPrice = mSetText.get(position).getTransactionPositionsCurrentPrice();
//            try {
//                if (!TextUtils.isEmpty(TransactionPositionsCurrentPrice) && !"0".equals(TransactionPositionsCurrentPrice)) {
//                    viewHodler.takeapositionItem7.setText(String.format("%.3f", Double.parseDouble(TransactionPositionsCurrentPrice)));
//                } else {
//                    viewHodler.takeapositionItem7.setText(TransitionUtils.string2doubleS3("0.000"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            String TransactionPositionsCurrentPrice1 = mSetText.get(position).getTransactionPositionsCurrentPrice1();
//            if (!TextUtils.isEmpty(TransactionPositionsCurrentPrice1) && !"0".equals(TransactionPositionsCurrentPrice1)) {
//                viewHodler.takeapositionItem8.setText(TransactionPositionsCurrentPrice1);
//            } else {
//                viewHodler.takeapositionItem8.setText("0.000");
//            }
            viewHodler.mjhkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,RepaymentActivity.class);
                    startActivity(intent);
                }
            });
            viewHodler.dbpmcLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,OrdinarySaleActivity.class);
                    startActivity(intent);
                }
            });
            viewHodler.dbpzcLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,CollateralActivity.class);
                    startActivity(intent);
                }
            });
            viewHodler.rzmrLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,MarginBuySellActivity.class);
                    startActivity(intent);
                }
            });
            viewHodler.dbpmrLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,OrdinarySaleActivity.class);
                    startActivity(intent);
                }
            });
            viewHodler.dbpzrLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,CollateralActivity.class);
                    startActivity(intent);
                }
            });
            viewHodler.rqmcLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,MarginBuySellActivity.class);
                    startActivity(intent);
                }
            });
            viewHodler.hqLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HoldActivity.this,StockDetailActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }


        class ViewHodler {
            LinearLayout takeapositionItem0;
            TextView takeapositionItem1,takeapositionItem2,takeapositionItem3,takeapositionItem4,takeapositionItem5,takeapositionItem6,takeapositionItem7,takeapositionItem8;
            ImageView deliveryImageView;
            LinearLayout mjhkLayout,dbpmcLayout,dbpzcLayout,rzmrLayout,dbpmrLayout,dbpzrLayout,rqmcLayout,hqLayout;
        }
    }
}
