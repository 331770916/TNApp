package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 作者：刘泽鹏 on 2016/9/16 14:49
 * 热点模块   GridView 适配器
 */
public class HotPintsGridViewAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<InformationBean> list;

    public HotPintsGridViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationBean> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list != null && list.size()>0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null && list.size()>0 ){
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
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hot_pints_gridview,null);
            viewHolder.tvEventTitle = (TextView) convertView.findViewById(R.id.tvEventTitle);
            viewHolder.tvEventDate = (TextView) convertView.findViewById(R.id.tvEventDate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InformationBean entity = list.get(position);
        String publishTitle = entity.getPublishTitle();     //标题
        long time = entity.getTime();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
        String formatTime = sdf.format(date);               //时间

        viewHolder.tvEventTitle.setText(publishTitle);
        viewHolder.tvEventDate.setText(formatTime);

        return convertView;
    }

    class ViewHolder{
        TextView tvEventTitle;          //标题
        TextView tvEventDate;           //日期
    }
}
