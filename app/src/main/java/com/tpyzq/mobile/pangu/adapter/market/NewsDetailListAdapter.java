package com.tpyzq.mobile.pangu.adapter.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationBean;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/10/7 14:57
 * 行情详情页    新闻  研报  公告  列表  适配器
 */
public class NewsDetailListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationBean> list;

    public NewsDetailListAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationBean> list){
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView  == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_detail,null);
            viewHolder.tvNewsDetailTitle = (TextView) convertView.findViewById(R.id.tvNewsDetailTitle);
            viewHolder.tvNewsDetailLaiYuanVolue = (TextView) convertView.findViewById(R.id.tvNewsDetailLaiYuanVolue);
            viewHolder.tvNewsDetailTime = (TextView) convertView.findViewById(R.id.tvNewsDetailTime);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InformationBean informationBean = list.get(position);
        viewHolder.tvNewsDetailTitle.setText(informationBean.getPublishTitle());
        viewHolder.tvNewsDetailTime.setText(informationBean.getTimes());
        viewHolder.tvNewsDetailLaiYuanVolue.setText("暂无数据");

        return convertView;
    }

    class ViewHolder{
        TextView tvNewsDetailTitle;             //标题
        TextView tvNewsDetailLaiYuanVolue;     //来源
        TextView tvNewsDetailTime;              //时间
    }
}
