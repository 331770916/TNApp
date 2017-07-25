package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * 作者：刘泽鹏 on 2016/8/31 10:08
 */
public class OTC_RevokeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Map<String,String>> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public OTC_RevokeAdapter(Context context) {
        mContext = context;
    }

    public void setList(ArrayList<Map<String,String>> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_otc_revoke, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Map<String, String> map = mDatas.get(position);
        viewHolder.tvRevokeName.setText(map.get("prod_name"));
        viewHolder.tvRevokeCode.setText(map.get("prod_code"));
        viewHolder.tvRevokeData.setText(map.get("entrust_date"));
        viewHolder.tvRevokeTime.setText(map.get("entrust_time"));
        viewHolder.tvRevokeMoney.setText(map.get("entrust_amount"));
        viewHolder.tvRevokeState.setText(map.get("entrust_status_name"));
        viewHolder.tvRevokeType.setText(map.get("business_flag"));
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRevokeName;      //产品名称
        TextView tvRevokeCode;      //产品代码
        TextView tvRevokeData;      //日期
        TextView tvRevokeTime;      //时间
        TextView tvRevokeMoney;     //金额/份额
        TextView tvRevokeType;      //类型
        TextView tvRevokeState;     //状态
        LinearLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRevokeName= (TextView) itemView.findViewById(R.id.tvRevokeName);
            tvRevokeCode= (TextView) itemView.findViewById(R.id.tvRevokeCode);
            tvRevokeData= (TextView) itemView.findViewById(R.id.tvRevokeData);
            tvRevokeTime= (TextView) itemView.findViewById(R.id.tvRevokeTime);
            tvRevokeMoney= (TextView) itemView.findViewById(R.id.tvRevokeMoney);
            tvRevokeType= (TextView) itemView.findViewById(R.id.tvRevokeType);
            tvRevokeState= (TextView) itemView.findViewById(R.id.tvRevokeState);
            mLayout = (LinearLayout) itemView.findViewById(R.id.otcRevokeLayout);
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

}
