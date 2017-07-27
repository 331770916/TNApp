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
 * 作者：刘泽鹏 on 2016/9/2 16:50
 * OTC 账户查询界面的适配器
 */
public class OTC_AccountQueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Map<String,String>> mDatas;

    public OTC_AccountQueryAdapter(Context context) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_otc_account_query, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<String, String> map = mDatas.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvOTC_OpenDate.setText(map.get("open_date"));
        viewHolder.tvOTC_Account.setText(map.get("secum_account"));
        viewHolder.tvOTC_company.setText(map.get("prodta_no"));
        viewHolder.tvOTC_AccountState.setText(map.get("prodholder_status"));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvOTC_OpenDate;
        TextView tvOTC_Account;
        TextView tvOTC_company;
        TextView tvOTC_AccountState;

        public ViewHolder(View itemView) {
            super(itemView);

            tvOTC_OpenDate = (TextView) itemView.findViewById(R.id.tvOTC_OpenDate);
            tvOTC_Account = (TextView) itemView.findViewById(R.id.tvOTC_Account);
            tvOTC_company = (TextView) itemView.findViewById(R.id.tvOTC_company);
            tvOTC_AccountState = (TextView) itemView.findViewById(R.id.tvOTC_AccountState);
        }
    }
}
