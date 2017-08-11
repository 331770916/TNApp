package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;

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
    public static final String TYPE_TITLE_HOT = "type_title_hot";
    public static final String TYPE_TITLE_SELL = "type_title_sell";

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
        } else if (TYPE_TITLE_HOT.equals(mData.get(position).get("type"))) {
            type = 3;
        } else if (TYPE_TITLE_SELL.equals(mData.get(position).get("type"))) {
            type = 4;
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
        } else if(viewType == 2){
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_two, parent, false);
            ViewHolder3 viewHolder3 = new ViewHolder3(view);
            return viewHolder3;
        } else if (viewType == 3) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_hold2title, parent, false);
            ViewHolder4 viewHolder4 = new ViewHolder4(view);
            return viewHolder4;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_hold2title, parent, false);
            ViewHolder5 viewHolder5 = new ViewHolder5(view);
            return viewHolder5;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type){
            case 0:
                ViewHolder1 viewHolder1 = (ViewHolder1)holder;
                initHold1(viewHolder1, position);
                break;
            case 1:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                initHold2(viewHolder2, position);
                break;
            case 2:
                ViewHolder3 viewHolder3 = (ViewHolder3)holder;
                initHold3(viewHolder3, position);
                break;
            case 3:
                ViewHolder4 viewHolder4 = (ViewHolder4)holder;
                viewHolder4.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CentreToast.showText(mContext, "more");
                    }
                });
                break;
            case 4:
                ViewHolder5 viewHolder5 = (ViewHolder5)holder;
                viewHolder5.more.setVisibility(View.GONE);
                viewHolder5.title.setText("热卖理财");
                viewHolder5.title.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                break;
        }
    }

    private void initHold1(ViewHolder1 viewHolder1, int position) {
        try {
            String proName = mData.get(position).get("hold1ProName");
            if (!TextUtils.isEmpty(proName)) {
                viewHolder1.proName.setText(proName);
            } else {
                viewHolder1.proName.setText("-.-");
            }

            String prdio = mData.get(position).get("hold1Prdio");
            if (!TextUtils.isEmpty(prdio)) {
                viewHolder1.prdio.setText(holder1SpannableString1(prdio));
            } else {
                viewHolder1.prdio.setText("-.-");
            }


            String day = mData.get(position).get("hold1Day");
            if (TextUtils.isEmpty(day)) {
                day = "-.-";
            }
            String strDay = day + "\n" + "期限";
            viewHolder1.recommend_day.setText(holder1SpannableString(day, strDay));


            String qgje = mData.get(position).get("hold1Qgje");
            if (TextUtils.isEmpty(qgje)) {
                qgje = "-.-";
            }
            String strQgje = qgje + "\n" + "起购金额";
            viewHolder1.recommend_qgje.setText(holder1SpannableString(qgje, strQgje));

            String lable = mData.get(position).get("lable");

            if (TextUtils.isEmpty(lable)) {
                return;
            }

            if (lable.contains(",")) {
                String []lables = lable.split(",");
                viewHolder1.labelLayout.removeAllViews();
                if (lables != null && lables.length > 0) {
                    for (String strLable : lables) {
                        viewHolder1.labelLayout.addView(creatLables(strLable));
                    }
                }
            } else {
                viewHolder1.labelLayout.addView(creatLables(lable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHold2(ViewHolder2 viewHolder2, int position) {
        try {

            String proName = mData.get(position).get("hold2ProName");
            if (!TextUtils.isEmpty(proName)) {
                viewHolder2.proName.setText(proName);
            } else {
                viewHolder2.proName.setText("-.-");
            }


            String time = "-.-";
            if (!TextUtils.isEmpty(mData.get(position).get("hold2Time"))) {
                time = mData.get(position).get("hold2Time");
            }
            viewHolder2.time.setText("距离结束："+ time);

            String prdio = "-.-";
            if (!TextUtils.isEmpty(mData.get(position).get("hold2Prdio"))) {
                prdio = mData.get(position).get("hold2Prdio");
            }

            viewHolder2.prdio.setText(holder2SpannableString(prdio, R.color.blue));

            String day = "-.-";
            if (!TextUtils.isEmpty(mData.get(position).get("hold2Day"))) {
                day = mData.get(position).get("hold2Day");
            }
            viewHolder2.day.setText(holder2SpannableString1(day));

            String price = "-.-";
            if (!TextUtils.isEmpty(mData.get(position).get("hold2Price"))) {
                price = mData.get(position).get("hold2Price");
            }
            viewHolder2.price.setText(holder2SpannableString2(price));

            String holder2lable = mData.get(position).get("lable");

            if (TextUtils.isEmpty(holder2lable)) {
                return;
            }
            viewHolder2.layout.removeAllViews();
            if (holder2lable.contains(",")) {
                String []holder2lables = holder2lable.split(",");
                for (String strLable : holder2lables) {
                    viewHolder2.layout.addView(creatLables(strLable));
                }
            } else {
                viewHolder2.layout.addView(creatLables(holder2lable));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initHold3(ViewHolder3 viewHolder3, int position) {
        String proName = "-.-";
        if (!TextUtils.isEmpty(mData.get(position).get("hold2ProName"))) {
            proName = mData.get(position).get("hold2ProName");
        }
        viewHolder3.proName.setText(proName);

        viewHolder3.time.setVisibility(View.GONE);

        String prdio3 = "-.-";
        if (!TextUtils.isEmpty(prdio3)) {
            prdio3 = mData.get(position).get("hold2Prdio");
        }
        viewHolder3.prdio.setText(holder2SpannableString(prdio3, R.color.red));

        String day = "-.-";
        if (!TextUtils.isEmpty(mData.get(position).get("hold2Day"))) {
            day = mData.get(position).get("hold2Day");
        }
        viewHolder3.day.setText(holder2SpannableString1(day));

        String price = "-.-";
        if (!TextUtils.isEmpty(mData.get(position).get("hold2Price"))) {
            price = mData.get(position).get("hold2Price");
        }
        viewHolder3.price.setText(holder2SpannableString2(price));

        String holder3lable = mData.get(position).get("lable");
        viewHolder3.layout.removeAllViews();

        if (TextUtils.isEmpty(holder3lable)) {
            return ;
        }

        if (holder3lable.contains(",")) {
            String []holder3lables = holder3lable.split(",");
            for (String strLable : holder3lables) {
                viewHolder3.layout.addView(creatLables(strLable));
            }
        } else {
            viewHolder3.layout.addView(creatLables(holder3lable));
        }
    }

    private TextView creatLables(String lable) {
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
        TextView proName;
        TextView time;
        TextView prdio;
        TextView day;
        TextView price;
        LinearLayout layout;
        public ViewHolder2(View itemView) {
            super(itemView);
            proName = (TextView) itemView.findViewById(R.id.recommendHolder2ProName);
            time = (TextView) itemView.findViewById(R.id.recommendHolder2Time);
            prdio = (TextView) itemView.findViewById(R.id.recommendHolder2Prdio);
            day = (TextView) itemView.findViewById(R.id.recommendHolder2Day);
            price = (TextView) itemView.findViewById(R.id.recommendHolder2Price);
            layout = (LinearLayout) itemView.findViewById(R.id.recommendHolder2Layout);
        }
    }

    private class ViewHolder3 extends RecyclerView.ViewHolder{
        TextView proName;
        TextView time;
        TextView prdio;
        TextView day;
        TextView price;
        LinearLayout layout;

        public ViewHolder3(View itemView) {
            super(itemView);
            proName = (TextView) itemView.findViewById(R.id.recommendHolder2ProName);
            time = (TextView) itemView.findViewById(R.id.recommendHolder2Time);
            prdio = (TextView) itemView.findViewById(R.id.recommendHolder2Prdio);
            day = (TextView) itemView.findViewById(R.id.recommendHolder2Day);
            price = (TextView) itemView.findViewById(R.id.recommendHolder2Price);
            layout = (LinearLayout) itemView.findViewById(R.id.recommendHolder2Layout);
        }
    }

    private class ViewHolder4 extends RecyclerView.ViewHolder{
        TextView title;
        TextView more;
        public ViewHolder4(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recommend_itemtitle);
            more = (TextView) itemView.findViewById(R.id.recommend_itemmore);
        }
    }

    private class ViewHolder5 extends RecyclerView.ViewHolder{
        TextView title;
        TextView more;
        public ViewHolder5(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recommend_itemtitle);
            more = (TextView) itemView.findViewById(R.id.recommend_itemmore);
        }
    }

    private SpannableString holder1SpannableString(String str, String strAll) {
        SpannableString ss = new SpannableString(strAll);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.amageModelColor)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.texthint93)), str.length(), strAll.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(18, true), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(12, true), str.length(), strAll.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString holder1SpannableString1(String str){
        String precent = str + "%";
        SpannableString ss = new SpannableString(precent);
        ss.setSpan(new AbsoluteSizeSpan(30, true), str.length(), precent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
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
