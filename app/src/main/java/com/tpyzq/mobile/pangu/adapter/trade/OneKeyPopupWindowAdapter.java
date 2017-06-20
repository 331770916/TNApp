package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeItem;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/8/10.
 */
public class OneKeyPopupWindowAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OneKeySubscribeItem> list;

    public OneKeyPopupWindowAdapter(Context context) {
        this.context=context;
    }

    public void setList(ArrayList list){
        this.list=list;
    }

    @Override
    public int getCount() {
        if(list != null && list.size() > 0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null && list.size() > 0 ){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if(view==null){
            viewHolder=new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_onekeypupopwindow, null);
            viewHolder.tvStockNameAffirmMsg= (TextView) view.findViewById(R.id.tvStockNameAffirmMsg);
            viewHolder.tvStockNameValueAffirmMsg= (TextView) view.findViewById(R.id.tvStockNameValueAffirmMsg);
            viewHolder.tvStockCodeAffirmMsg= (TextView) view.findViewById(R.id.tvStockCodeAffirmMsg);
            viewHolder.tvStockCodeValueAffirmMsg= (TextView) view.findViewById(R.id.tvStockCodeValueAffirmMsg);
            viewHolder.tvSubscribePrice= (TextView) view.findViewById(R.id.tvSubscribePrice);
            viewHolder.tvSubscribePriceValue= (TextView) view.findViewById(R.id.tvSubscribePriceValue);
            viewHolder.tvSubscribeNum= (TextView) view.findViewById(R.id.tvSubscribeNum);
            viewHolder.tvSubscribeNumValue= (TextView) view.findViewById(R.id.tvSubscribeNumValue);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }

        OneKeySubscribeItem item = list.get(position);
//        viewHolder.tvStockNameAffirmMsg.setText(hashMap.get("a").toString());
        viewHolder.tvStockNameValueAffirmMsg.setText(item.getName());
//        viewHolder.tvStockCodeAffirmMsg.setText(hashMap.get("b").toString());
        viewHolder.tvStockCodeValueAffirmMsg.setText(item.getCode());
//        viewHolder.tvSubscribePrice.setText(hashMap.get("c").toString());
        viewHolder.tvSubscribePriceValue.setText(item.getPrice());
//        viewHolder.tvSubscribeNum.setText(hashMap.get("d").toString());
        viewHolder.tvSubscribeNumValue.setText(item.getNum());
        return view;
    }

    private class ViewHolder{
        TextView tvStockNameAffirmMsg;
        TextView tvStockNameValueAffirmMsg;
        TextView tvStockCodeAffirmMsg;
        TextView tvStockCodeValueAffirmMsg;
        TextView tvSubscribePrice;
        TextView tvSubscribePriceValue;
        TextView tvSubscribeNum;
        TextView tvSubscribeNumValue;
    }
}
