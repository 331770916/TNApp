package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/9/26 17:50
 * 重大事件详情  里面的相关股票列表 的适配器    新闻详情里面的  相关股票也用的这个适配器
 */
public class EventDetailListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StockInfoEntity> list;

    public EventDetailListAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<StockInfoEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list != null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null && list.size()>0){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.item_event_detail_list,null);
            viewHolder.tvEventDeatilStockName = (TextView) convertView.findViewById(R.id.tvEventDeatilStockName);
            viewHolder.tvEventDeatilStockCode = (TextView) convertView.findViewById(R.id.tvEventDeatilStockCode);
            viewHolder.tvEventDeatilStockPrice = (TextView) convertView.findViewById(R.id.tvEventDeatilStockPrice);
            viewHolder.tvEventDeatilStockRange = (TextView) convertView.findViewById(R.id.tvEventDeatilStockRange);
            viewHolder.v_line =  convertView.findViewById(R.id.v_line);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StockInfoEntity stockInfoEntity = list.get(position);
        String stockNumber = stockInfoEntity.getStockNumber();
        if (!TextUtils.isEmpty(stockNumber)){
            String subStockNumber = stockNumber.substring(2, stockNumber.length());
            viewHolder.tvEventDeatilStockCode.setText(subStockNumber);
        }

        viewHolder.tvEventDeatilStockName.setText(stockInfoEntity.getStockName());
        viewHolder.tvEventDeatilStockPrice.setText(stockInfoEntity.getNewPrice());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(viewHolder.v_line.getLayoutParams());
        lp.setMargins(TransitionUtils.dp2px(20,context), 0, 0, 0);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(viewHolder.v_line.getLayoutParams());
        lp2.setMargins(0, 0, 0, 0);
        if (position == list.size()-1){
            viewHolder.v_line.setLayoutParams(lp2);
        }else {
            viewHolder.v_line.setLayoutParams(lp);
        }
        String price = stockInfoEntity.getNewPrice();
        String closeValue = stockInfoEntity.getClose();
        if(price !=null && closeValue != null){
            if(Helper.isDecimal(price)){
                double newPrice = Double.valueOf(price);
                double close = Double.valueOf(closeValue);
                double end = newPrice-close;
                double range = end/close;
                DecimalFormat format = new DecimalFormat("#0.00%");
                String ranges = format.format(range);
                if(end > 0){
                    viewHolder.tvEventDeatilStockRange.setTextColor(ContextCompat.getColor(context,R.color.red));
                    viewHolder.tvEventDeatilStockRange.setText("+"+ranges);
                }else if(end < 0){
                    viewHolder.tvEventDeatilStockRange.setTextColor(ContextCompat.getColor(context,R.color.green));
                    viewHolder.tvEventDeatilStockRange.setText(ranges);
                }else if (end==0){
                    viewHolder.tvEventDeatilStockRange.setTextColor(ContextCompat.getColor(context,R.color.texts));
                    viewHolder.tvEventDeatilStockRange.setText(ranges);
                }
            }else {
                viewHolder.tvEventDeatilStockRange.setTextColor(ContextCompat.getColor(context,R.color.texts));
                viewHolder.tvEventDeatilStockRange.setText("--");
            }
        }

        return convertView;
    }

    class ViewHolder{
        TextView tvEventDeatilStockName;
        TextView tvEventDeatilStockCode;
        TextView tvEventDeatilStockPrice;
        TextView tvEventDeatilStockRange;
        View  v_line;
    }
}
