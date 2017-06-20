package com.tpyzq.mobile.pangu.adapter.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.DetailNewsEntity;

import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/10/26 10:57
 * 详情也   新闻公告研报  里的     新闻view 的适配器
 */
public class NewsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DetailNewsEntity> list;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<DetailNewsEntity> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.news_detailnews,null);
            viewHolder.tvDetailNewsTitle = (TextView) convertView.findViewById(R.id.tvDetailNewsTitle);
            viewHolder.tvDetailNewsTime = (TextView) convertView.findViewById(R.id.tvDetailNewsTime);
            viewHolder.tvDetailNewsSource = (TextView) convertView.findViewById(R.id.tvDetailNewsSource);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DetailNewsEntity bean = list.get(position);
        int type = bean.getType();
        if(type == 1){
            viewHolder.tvDetailNewsSource.setVisibility(View.GONE);
        }
        viewHolder.tvDetailNewsSource.setText(bean.getSource());
        viewHolder.tvDetailNewsTitle.setText(bean.getTitle());
        viewHolder.tvDetailNewsTime.setText(bean.getTime());

        return convertView;
    }

    class ViewHolder{
        TextView tvDetailNewsTitle;     //标题
        TextView tvDetailNewsTime;      //时间
        TextView tvDetailNewsSource;      //时间
    }
}
