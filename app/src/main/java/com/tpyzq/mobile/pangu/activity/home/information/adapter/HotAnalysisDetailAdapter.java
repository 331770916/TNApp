package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/9/20 17:26
 */
public class HotAnalysisDetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StockInfoEntity> list;

    public HotAnalysisDetailAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<StockInfoEntity> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if( list!=null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if( list!=null && list.size()>0){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hot_analysis_detail,null);
            viewHolder.tvHotAnalysisStockName = (TextView) convertView.findViewById(R.id.tvHotAnalysisStockName);
            viewHolder.tvHotAnalysisStockCode = (TextView) convertView.findViewById(R.id.tvHotAnalysisStockCode);
            viewHolder.tvHotAnalysisPrice = (TextView) convertView.findViewById(R.id.tvHotAnalysisPrice);
            viewHolder.tvHotAnalysisProd = (TextView) convertView.findViewById(R.id.tvHotAnalysisProd);
            viewHolder.v_line =  convertView.findViewById(R.id.v_line);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StockInfoEntity map = list.get(position);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(viewHolder.v_line.getLayoutParams());
        lp.setMargins(TransitionUtils.dp2px(20,context), 0, 0, 0);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(viewHolder.v_line.getLayoutParams());
        lp2.setMargins(0, 0, 0, 0);
        if (position == list.size()-1){
            viewHolder.v_line.setLayoutParams(lp2);
        }else {
            viewHolder.v_line.setLayoutParams(lp);
        }
        String closeValue = map.getClose();
        String price = map.getNewPrice();
        if(price !=null && closeValue != null&&!price.equals("-")){
            double newPrice = Double.valueOf(price);
            double close = Double.valueOf(closeValue);
            double end = newPrice-close;
            double range = end/close;
            DecimalFormat format = new DecimalFormat("#0.00%");
            String ranges = format.format(range);
            if(end > 0){
                viewHolder.tvHotAnalysisProd.setTextColor(ContextCompat.getColor(context,R.color.red));
                viewHolder.tvHotAnalysisProd.setText("+"+ranges);
            }else if(end < 0){
                viewHolder.tvHotAnalysisProd.setTextColor(ContextCompat.getColor(context,R.color.green));
                viewHolder.tvHotAnalysisProd.setText(ranges);
            }else if (end==0){
                viewHolder.tvHotAnalysisProd.setTextColor(ContextCompat.getColor(context,R.color.text));
                viewHolder.tvHotAnalysisProd.setText(ranges);
            }
        }

        viewHolder.tvHotAnalysisStockName.setText(map.getStockName());
        viewHolder.tvHotAnalysisStockCode.setText(map.getStockNumber().substring(2));
        viewHolder.tvHotAnalysisPrice.setText(price);


        return convertView;
    }

    class  ViewHolder{
        TextView tvHotAnalysisStockName;            //名称
        TextView tvHotAnalysisStockCode;            //代码
        TextView tvHotAnalysisPrice;                //现价
        TextView tvHotAnalysisProd;                 //百分比
        View  v_line;
    }
}
