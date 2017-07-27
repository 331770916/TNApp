package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;

import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/9/5 15:18
 */
public class OTC_ElectronicContractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<OTC_ElectronicContractEntity> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public OTC_ElectronicContractAdapter(Context context) {
        mContext = context;
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setList(ArrayList<OTC_ElectronicContractEntity> datas){
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_electronic_contract, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final OTC_ElectronicContractEntity intentBean = mDatas.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;

        viewHolder.tvProductContractName.setText(intentBean.getProd_name());
        viewHolder.tvProductContractCode.setText(intentBean.getProd_code());

        String is_sign = intentBean.getIs_sign();
        if(is_sign.equals("0")){
            viewHolder.tvDianJi.setText("已签约");
            viewHolder.tvDianJi.setTextColor(mContext.getResources().getColor(R.color.blue));
        }else if(is_sign.equals("1")){
            viewHolder.tvDianJi.setText("点击签约");
            viewHolder.tvDianJi.setTextColor(mContext.getResources().getColor(R.color.orange1));
        }

        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(intentBean);
                }
            }
        });

        viewHolder.tvDianJi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(intentBean);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductContractName;         //产品名称
        TextView tvProductContractCode;         //产品代码
        TextView tvDianJi;                       //点击签约
        RelativeLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            tvProductContractName = (TextView) itemView.findViewById(R.id.tvProductContractName);
            tvProductContractCode = (TextView) itemView.findViewById(R.id.tvProductContractCode);
            tvDianJi = (TextView) itemView.findViewById(R.id.tvDianJi);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.item_electronicLayout);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(OTC_ElectronicContractEntity electronicContractEntity);
    }
}
