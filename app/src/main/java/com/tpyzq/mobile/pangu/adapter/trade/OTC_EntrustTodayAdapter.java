package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import java.util.ArrayList;
import java.util.Map;

/**
 * 作者：刘泽鹏 on 2016/9/6 19:56
 */
public class OTC_EntrustTodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Map<String,String>> mDatas;

    public OTC_EntrustTodayAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(ArrayList<Map<String,String>> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_otc_entrust_today, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Map<String, String> map = mDatas.get(position);
        viewHolder.tv_OTCEntrustTodayStockName.setText(map.get("tv_stockName"));
        viewHolder.tv_OTCEntrustTodaystockCode.setText(map.get("tv_stockCode"));
        viewHolder.tv_OTCEntrustTodayData.setText(map.get("tv_Data"));
        viewHolder.tv_OTCEntrustTodayTime.setText(map.get("tv_Time"));
        viewHolder.tv_OTCEntrustTodayMoney.setText(map.get("tv_EntrustMoney"));
        viewHolder.tv_OTCEntrustTodayNumber.setText(map.get("tv_EntrustNumber"));
        viewHolder.tv_OTCEntrustTodayType.setText(map.get("tv_type"));
        viewHolder.tv_OTCEntrustTodayState.setText(map.get("tv_state"));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_OTCEntrustTodayStockName;       //名称
        TextView tv_OTCEntrustTodaystockCode;       //代码
        TextView tv_OTCEntrustTodayData;            //日期
        TextView tv_OTCEntrustTodayTime;            //时间
        TextView tv_OTCEntrustTodayMoney;           //金额
        TextView tv_OTCEntrustTodayNumber;          //份额
        TextView tv_OTCEntrustTodayType;            //类型
        TextView tv_OTCEntrustTodayState;           //状态

        public ViewHolder(View itemView) {
            super(itemView);

            tv_OTCEntrustTodayStockName = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodayStockName);
            tv_OTCEntrustTodaystockCode = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodaystockCode);
            tv_OTCEntrustTodayData = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodayData);
            tv_OTCEntrustTodayTime = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodayTime);
            tv_OTCEntrustTodayMoney = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodayMoney);
            tv_OTCEntrustTodayNumber = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodayNumber);
            tv_OTCEntrustTodayType = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodayType);
            tv_OTCEntrustTodayState = (TextView) itemView.findViewById(R.id.tv_OTCEntrustTodayState);
        }
    }

}
