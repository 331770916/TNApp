package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationEntity;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/9/27 14:11
 * 咨询 相同 Tab 标签 页  的适配器
 */
public class ZxTabAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationEntity> list;

    public ZxTabAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationEntity> list){
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

        try {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_tab_zixun,null);
                viewHolder.tvTabTitle = (TextView) convertView.findViewById(R.id.tvTabTitle);
                viewHolder.tvTabDate = (TextView) convertView.findViewById(R.id.tvTabDate);
                viewHolder.tvTabRelateStock = (TextView) convertView.findViewById(R.id.tvTabRelateStock);
                viewHolder.rlTabRelate = (RelativeLayout) convertView.findViewById(R.id.rlTabRelate);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position<list.size()){
                InformationEntity      informationBean = list.get(position);
                viewHolder.tvTabTitle.setText(informationBean.getTitle());
                String time = informationBean.getTime();
                viewHolder.tvTabDate.setText(time.length()==19?time.substring(0,time.length()-3):time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }

    class ViewHolder{
        TextView tvTabTitle;
        TextView tvTabDate;
        TextView tvTabRelateStock;
        RelativeLayout rlTabRelate;
    }
}
