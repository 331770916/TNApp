package com.tpyzq.mobile.pangu.adapter.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import java.util.ArrayList;

/**
 * Created by wangqi on 2017/2/13.
 * 首页资讯 Adapter
 */

public class NewHomeInformationAdapter extends BaseAdapter{

    private ArrayList<InformationEntity> mDatas;
    private ViewHolder viewHolder = null;
    private Context context;

    public NewHomeInformationAdapter(Context activity) {
        context = activity;
    }

    public void setDatas(ArrayList<InformationEntity> datas) {
        mDatas = datas;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_refcontent, null);
            viewHolder.image = (SimpleDraweeView) convertView.findViewById(R.id.iv_image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.time =  (TextView)convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InformationEntity info = mDatas.get(position);
        viewHolder.image.setImageURI(info.getImage_url());
        viewHolder.title.setText(info.getTitle());
        viewHolder.time.setText(info.getTime());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("requestId", info.getNewsno());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        SimpleDraweeView image;
        TextView title;
        TextView time;
    }
}
