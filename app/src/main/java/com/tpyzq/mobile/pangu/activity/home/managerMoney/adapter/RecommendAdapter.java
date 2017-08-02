package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/31.
 * 推荐adapter
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TYPE_NEW = "type_new";//新人专享
    public static final String TYPE_HOT = "type_hot";//火爆预约
    public static final String TYPE_SELL = "type_sell";//热卖理财

    private Context mContext;
    private List<Map<String, String>> mData;

    public RecommendAdapter (Context context) {
        mContext = context;
    }

    public void setDatas(List<Map<String, String>> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mData != null && mData.size() > 0) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        int type = -1;
        if (TYPE_NEW.equals(mData.get(position).get("type"))) {
            type = 0;
        } else if (TYPE_HOT.equals(mData.get(position).get("type"))) {
            type = 1;
        } else if (TYPE_SELL.equals(mData.get(position).get("type"))){
            type = 2;
        }

        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_one, parent, false);
            ViewHolder1 viewHolder1 = new ViewHolder1(view);
            return viewHolder1;
        } else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_two, parent, false);
            ViewHolder2 viewHolder2 = new ViewHolder2(view);
            return viewHolder2;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_three, parent, false);
            ViewHolder3 viewHolder3 = new ViewHolder3(view);
            return viewHolder3;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 0:
                ViewHolder1 viewHolder1 = (ViewHolder1)holder;
                viewHolder1.proName.setText(mData.get(position).get("hold1ProName"));
                viewHolder1.prdio.setText(mData.get(position).get("hold1Prdio"));
                viewHolder1.recommend_day.setText(mData.get(position).get("hold1Day"));
                viewHolder1.recommend_qgje.setText(mData.get(position).get("hold1Qgje"));

//                viewHolder1.labelLayout.addView();
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView proName;
        private TextView prdio;
        private LinearLayout labelLayout;
        private TextView recommend_day;
        private TextView recommend_qgje;

        public ViewHolder1(View itemView) {
            super(itemView);
            proName = (TextView) itemView.findViewById(R.id.recommend_proName);
            prdio = (TextView) itemView.findViewById(R.id.recommend_prdio);
            labelLayout = (LinearLayout) itemView.findViewById(R.id.labelLayout);
            recommend_day = (TextView) itemView.findViewById(R.id.recommend_day);
            recommend_qgje = (TextView) itemView.findViewById(R.id.recommend_qgje);
        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder{
        public ViewHolder2(View itemView) {
            super(itemView);
        }
    }

    private class ViewHolder3 extends RecyclerView.ViewHolder{
        public ViewHolder3(View itemView) {
            super(itemView);
        }
    }
}
