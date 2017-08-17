package com.tpyzq.mobile.pangu.adapter.home;

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
 * Created by wangqi on 2017/2/13.
 * 首页资讯 Adapter
 */

public class NewInformationAdapter extends BaseAdapter {

    private ArrayList<InformationEntity> mDatas;
    private ViewHolder viewHolder = null;
    private Context context;
    private String type;

    public NewInformationAdapter(Context activity) {
        context = activity;
    }

    public void setDatas(ArrayList<InformationEntity> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void setType(String type){
        this.type= type;
    }

    @Override
    public int getCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.announcement_layout,null);
            viewHolder.tvNewAnnStudyTitle = (TextView) convertView.findViewById(R.id.tvAnnouncementTitle);
            viewHolder.tvNewAnnStudyTime = (TextView) convertView.findViewById(R.id.tvAnnouncementTime);
            viewHolder.tvNewAnnStudySource = (TextView) convertView.findViewById(R.id.tvAnnouncementSource);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InformationEntity info = mDatas.get(position);
//        viewHolder.image.setImageURI(info.getImage_url());
        viewHolder.tvNewAnnStudyTitle.setText(info.getTitle());  //+" | "+info.getDigest()
        viewHolder.tvNewAnnStudySource.setText(info.getTime());
        return convertView;
    }

    private class ViewHolder {
        TextView tvNewAnnStudyTitle;     //标题
        TextView tvNewAnnStudyTime;      //时间
        TextView tvNewAnnStudySource;      //来源
    }
}
