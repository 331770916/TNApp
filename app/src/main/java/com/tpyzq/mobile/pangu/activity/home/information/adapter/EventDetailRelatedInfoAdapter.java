package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/9/26 10:30
 */
public class EventDetailRelatedInfoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationBean> list;

    public EventDetailRelatedInfoAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationBean> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_important_event_detail_related_info,null);
            viewHolder.tvRelatedInfoTitle = (TextView) convertView.findViewById(R.id.tvRelatedInfoTitle);
            viewHolder.tvRelatedInfoDate = (TextView) convertView.findViewById(R.id.tvRelatedInfoDate);
            viewHolder.tvRelatedInfoValue = (TextView) convertView.findViewById(R.id.tvRelatedInfoValue);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InformationBean informationBean = list.get(position);
        List<String> stockNameList = informationBean.getSopCastList();
        StringBuilder sb = new StringBuilder();
        if(stockNameList != null && stockNameList.size()>0){
            if(stockNameList.size() > 3){
                for (int i = 0; i < 2; i++) {
                    String stockName = stockNameList.get(i);
                    sb.append(stockName).append("  ");
                }
            }else if(stockNameList.size() < 3){
                for (int i = 0; i < stockNameList.size(); i++) {
                    String stockName = stockNameList.get(i);
                    sb.append(stockName).append("  ");
                }
            }
        }
        viewHolder.tvRelatedInfoTitle.setText(informationBean.getPublishTitle());
        viewHolder.tvRelatedInfoDate.setText(informationBean.getTimes());
        viewHolder.tvRelatedInfoValue.setText(sb.toString());
        return convertView;
    }

    class ViewHolder{
        TextView tvRelatedInfoTitle;        //标题
        TextView tvRelatedInfoDate;         //日期
        TextView tvRelatedInfoValue;         //相关股票
    }

}
