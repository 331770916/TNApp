package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/17.
 */

public class OtherAdapter2 extends RecyclerView.Adapter<OtherAdapter2.OtherItemViewHodler> {


    private ArrayList<Map<String, Object>> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDatas(ArrayList<Map<String, Object>> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (mDatas != null && mDatas.size() >0) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(OtherItemViewHodler holder, final int position) {
        Drawable drawable = ContextCompat.getDrawable(CustomApplication.getContext(), (int) mDatas.get(position).get("img"));
        holder.title_ic.setImageDrawable(drawable);
        holder.title_tv.setText((String) mDatas.get(position).get("title"));
        holder.mViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });



    }

    @Override
    public OtherItemViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.other_item2, null);
        OtherItemViewHodler otherItemViewHodler = new OtherItemViewHodler(view);
        return otherItemViewHodler;
    }

    class OtherItemViewHodler extends RecyclerView.ViewHolder {

        private ImageView title_ic;
        private TextView  title_tv;
        private LinearLayout mViewGroup;

        public OtherItemViewHodler(View itemView) {
            super(itemView);
            title_ic = (ImageView) itemView.findViewById(R.id.item_image2);
            title_tv = (TextView) itemView.findViewById(R.id.item_text2);
            mViewGroup = (LinearLayout) itemView.findViewById(R.id.other_item2Layout);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }
}
