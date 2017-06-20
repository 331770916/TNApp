package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.ImportantEventValueEntity;

import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/9/16 16:21
 */
public class ImportantEventAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImportantEventValueEntity> list;

    public ImportantEventAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<ImportantEventValueEntity> list){
        this.list = list;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = list.get(position).getItemType();

        if(itemType == 0){
            return 0;
        }else if(itemType == 1){
            return 1;
        }

        return -1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImportantEventValueEntity importantEventValueBean = list.get(position);
        int itemType = importantEventValueBean.getItemType();

        ViewHolder1 viewHolder1;
        ViewHolder2 viewHolder2;

        switch (itemType){

            case 0:

                if(convertView == null){
                    viewHolder1 = new ViewHolder1();
                    //重大事件列表首页的 item1
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_important_event_title,null);
                    viewHolder1.tvDateItem1 = (TextView) convertView.findViewById(R.id.tvDateItem1);
                    convertView.setTag(viewHolder1);
                }else {
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                }

                String date = importantEventValueBean.getDate();

                if(position == 0){
                    viewHolder1.tvDateItem1.setText("今日");
                }else {
                    viewHolder1.tvDateItem1.setText(date);
                }


                break;

            case 1:

                if(convertView == null){
                    viewHolder2 = new ViewHolder2();
                    //重大事件列表首页的 item2
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_important_event_content,null);
                    viewHolder2.ivReDianShiJian = (ImageView) convertView.findViewById(R.id.ivReDianShiJian);
                    viewHolder2.tvReDianShiJianTiele = (TextView) convertView.findViewById(R.id.tvReDianShiJianTiele);
                    viewHolder2.tvReDianShiJianDate = (TextView) convertView.findViewById(R.id.tvReDianShiJianDate);
                    convertView.setTag(viewHolder2);
                }else {
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                }

                viewHolder2.tvReDianShiJianTiele.setText(importantEventValueBean.getTiele());
                viewHolder2.tvReDianShiJianDate.setText(importantEventValueBean.getTime());

                int imgType = importantEventValueBean.getImgType();

                switch (imgType){

                    case 1:
                        viewHolder2.ivReDianShiJian.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.gonggao));
                        break;

                    case 2:
                        viewHolder2.ivReDianShiJian.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.yanbao));
                        break;

                    case 3:
                        viewHolder2.ivReDianShiJian.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.redianshijiantwo));
                        break;
                }

                break;

        }

        return convertView;
    }

    class ViewHolder1{
        TextView tvDateItem1;
    }

    class ViewHolder2{
        ImageView ivReDianShiJian;
        TextView tvReDianShiJianTiele;
        TextView tvReDianShiJianDate;
    }
}
