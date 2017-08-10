package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/31.
 * 稳赢adapter
 */

public class SafeBetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<Map<String, String>> mData;

    public SafeBetAdapter(Context context) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_two, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder)holder;
            String proName = "-.-";
            if (!TextUtils.isEmpty(mData.get(position).get("holdProName"))) {
                proName = mData.get(position).get("holdProName");
            }
            viewHolder.proName.setText(proName);

            String no = mData.get(position).get("holdNo");
            if (!TextUtils.isEmpty(no)) {
                viewHolder.tvNo.setText(no);
                viewHolder.tvNo.setVisibility(View.VISIBLE);
                switch (no) {
                    case "No.1":
                        viewHolder.tvNo.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.mipmap.success01));
                        break;
                    case "No.2":
                        viewHolder.tvNo.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.mipmap.success02));
                        break;
                    case "No.3":
                        viewHolder.tvNo.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.mipmap.success03));
                        break;
                }
            }

            viewHolder.time.setVisibility(View.GONE);

            String prdio3 = "-.-";
            if (!TextUtils.isEmpty(prdio3)) {
                prdio3 = mData.get(position).get("holdPrdio");
            }

            viewHolder.prdio.setText(holder2SpannableString(prdio3, R.color.red));

            String day = "-.-";
            if (!TextUtils.isEmpty(mData.get(position).get("holdDay"))) {
                day = mData.get(position).get("holdDay");
            }
            viewHolder.day.setText(holder2SpannableString1(day));

            String price = "-.-";
            if (!TextUtils.isEmpty(mData.get(position).get("holdPrice"))) {
                price = mData.get(position).get("holdPrice");
            }
            viewHolder.price.setText(holder2SpannableString2(price));


            String holder3lable = mData.get(position).get("lable");
            viewHolder.layout.removeAllViews();

            if (TextUtils.isEmpty(holder3lable)) {
                return;
            }

            if (holder3lable.contains(",")) {
                String []holder3lables = holder3lable.split(",");
                for (String strLable : holder3lables) {
                    viewHolder.layout.addView(getLable(strLable));
                }
            } else {

                viewHolder.layout.addView(getLable(holder3lable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView getLable(String lable) {
        TextView textView = new TextView(mContext);
        textView.setText(lable);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        textView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.recommend_top_tvbg));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.texthint93));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margins = Helper.dip2px(mContext, 5);
        layoutParams.setMargins(0, margins, margins, margins);
        textView.setLayoutParams(layoutParams);

        return textView;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView proName;
        TextView tvNo;
        TextView time;
        TextView prdio;
        TextView day;
        TextView price;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);

            proName = (TextView) itemView.findViewById(R.id.recommendHolder2ProName);
            tvNo = (TextView)itemView.findViewById(R.id.recommendHolder2Top);
            time = (TextView) itemView.findViewById(R.id.recommendHolder2Time);
            prdio = (TextView) itemView.findViewById(R.id.recommendHolder2Prdio);
            day = (TextView) itemView.findViewById(R.id.recommendHolder2Day);
            price = (TextView) itemView.findViewById(R.id.recommendHolder2Price);
            layout = (LinearLayout) itemView.findViewById(R.id.recommendHolder2Layout);
        }
    }


    private SpannableString holder2SpannableString(String prdio, int color) {
        String precent = "%";
        String strPrdio = prdio + precent + "\n七日年化收益";
        SpannableString ss = new SpannableString(strPrdio);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, color)), 0, (prdio + precent).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(26, true), 0, prdio.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(18, true), prdio.length(), (prdio + precent).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
        ss.setSpan(styleSpan_B, 0, (prdio + precent).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.texthint93)), (prdio + precent).length(), strPrdio.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(12, true), (prdio + precent).length(), strPrdio.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private SpannableString holder2SpannableString1(String day) {
        String strDay = day + "\n期限";
        SpannableString ss = new SpannableString(strDay);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.amageModelColor)), 0, day.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.texthint93)), day.length(), strDay.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(18, true), 0, day.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(12, true), day.length(), strDay.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private SpannableString holder2SpannableString2(String price) {
        String strPrice = price + "\n起购金额";
        SpannableString ss = new SpannableString(strPrice);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.amageModelColor)), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.texthint93)), price.length(), strPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(18, true), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(12, true), price.length(), strPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
