package com.tpyzq.mobile.pangu.adapter.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationEntity;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/11/22 13:44
 * 公告 研报的 adapter
 */

public class AnnouncementAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationEntity> list;

    public AnnouncementAdapter(Context context) {
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
        AnnouncementAdapter.ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new AnnouncementAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.announcement_layout,null);
            viewHolder.tvNewAnnStudyTitle = (TextView) convertView.findViewById(R.id.tvAnnouncementTitle);
            viewHolder.tvNewAnnStudyTime = (TextView) convertView.findViewById(R.id.tvAnnouncementTime);
            viewHolder.tvNewAnnStudySource = (TextView) convertView.findViewById(R.id.tvAnnouncementSource);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (AnnouncementAdapter.ViewHolder) convertView.getTag();
        }

        InformationEntity bean = list.get(position);
        int type = bean.getType();
        if(type == 1){
            viewHolder.tvNewAnnStudySource.setVisibility(View.GONE);
        }
        viewHolder.tvNewAnnStudySource.setText(bean.getSource());
        viewHolder.tvNewAnnStudyTitle.setText(bean.getTitle());
        viewHolder.tvNewAnnStudyTime.setText(bean.getTime());

        return convertView;
    }

    class ViewHolder{
        TextView tvNewAnnStudyTitle;     //标题
        TextView tvNewAnnStudyTime;      //时间
        TextView tvNewAnnStudySource;      //来源
    }

}
