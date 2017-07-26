package com.tpyzq.mobile.pangu.adapter.home;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangqi on 2017/2/13.
 * 首页资讯 Adapter
 */

public class NewHomeInformationAdapter extends BaseAdapter{

    private ArrayList<InformationEntity> mDatas;
    private ViewHolder viewHolder = null;
    private Activity mActivity;

    public NewHomeInformationAdapter(Activity activity) {
        mActivity = activity;
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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.item_news_refcontent, null);
            viewHolder.image = (SimpleDraweeView) convertView.findViewById(R.id.iv_image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.time =  (TextView)convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InformationEntity info = mDatas.get(position);
        viewHolder.image.setImageURI(info.getImage_url());
        viewHolder.title.setText(info.getTitle()+" | "+info.getDigest());
        viewHolder.time.setText(Helper.getCurDate()+" "+info.getTime());
        return convertView;
    }

    private class ViewHolder {
        SimpleDraweeView image;
        TextView title;
        TextView time;
    }
}
