package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OtcShareEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import java.util.List;


/**
 * Created by zhangwenbo on 2017/7/24.
 * otc份额adapter
 */

public class OTC_ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<OtcShareEntity> mDatas;
    private boolean flag = true;
    private OtcShareClick mOtcShareClick;

    public OTC_ShareAdapter(Context context) {
        mContext = context;
    }

    public void setClick(OtcShareClick otcShareClick) {
        mOtcShareClick = otcShareClick;
    }

    public void setDatas (List<OtcShareEntity> datas) {
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
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_otc_shareheader, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_otcshare, parent, false);
            MyViewHolder2 myViewHolder2 = new MyViewHolder2(view);
            return myViewHolder2;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)){
            case 0:
                ((MyViewHolder)holder).tv_marketValue.setText(mDatas.get(position).getMarket_value());
                break;
            case 1:
                String proCode = "";
                if (!TextUtils.isEmpty(mDatas.get(position).getProd_code())) {
                    proCode = mDatas.get(position).getProd_code();
                }

                String proName = "";
                if (!TextUtils.isEmpty(mDatas.get(position).getProd_name())) {
                    proName = mDatas.get(position).getProd_name();
                }

                String marketValue = "";
                if (!TextUtils.isEmpty(mDatas.get(position).getCurrent_amount())) {
                    marketValue = mDatas.get(position).getCurrent_amount();
                }

                String buyDate = "";
                if (!TextUtils.isEmpty(mDatas.get(position).getBuy_date())) {
                    buyDate = mDatas.get(position).getBuy_date();
                    buyDate = Helper.formateDate1(buyDate);
                }

                ((MyViewHolder2)holder).tv_otcName.setText(proName + "(" + proCode + ")");
                ((MyViewHolder2)holder).tv_marketValue.setText(marketValue);
                ((MyViewHolder2)holder).tv_buyDate.setText("购入日期：" + buyDate);
                ((MyViewHolder2)holder).tv_sellDate.setText("");
                ((MyViewHolder2)holder).iv_plps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            flag = false;
                            ((MyViewHolder2)holder).tv_shuhuibtn.setVisibility(View.VISIBLE);
                            ((MyViewHolder2)holder).iv_plps.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.otcshare_up));
                        } else {
                            flag = true;
                            ((MyViewHolder2)holder).tv_shuhuibtn.setVisibility(View.GONE);
                            ((MyViewHolder2)holder).iv_plps.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.otcshare_down));
                        }
                    }
                });
                ((MyViewHolder2)holder).tv_shuhuibtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lineColor1));
                ((MyViewHolder2)holder).tv_shuhuibtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOtcShareClick != null) {
                            mOtcShareClick.onClick(position);
                        }
                    }
                });
                break;
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_marketValue;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_marketValue = (TextView) itemView.findViewById(R.id.itemotcShare_header_tv);
        }
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder{
        private TextView tv_otcName;
        private TextView tv_marketValue;
        private TextView tv_buyDate;
        private TextView tv_sellDate;
        private ImageView iv_plps;
        private TextView  tv_shuhuibtn;
        public MyViewHolder2(View itemView) {
            super(itemView);

            tv_otcName = (TextView) itemView.findViewById(R.id.otcShareStockInfo);
            tv_marketValue = (TextView) itemView.findViewById(R.id.otcSharePrice);
            tv_buyDate = (TextView) itemView.findViewById(R.id.otcShareIntoDate);
            tv_sellDate = (TextView) itemView.findViewById(R.id.otcShareOutDate);
            iv_plps = (ImageView) itemView.findViewById(R.id.otcUPdownIv1);
            tv_shuhuibtn = (TextView) itemView.findViewById(R.id.otcShareShuHuibtn);
        }
    }

    public interface OtcShareClick {
        void onClick(int position);
    }

}
