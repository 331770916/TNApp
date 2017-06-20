package com.tpyzq.mobile.pangu.adapter.detail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.log.LogHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/24
 */
public class LandKlineToolsAdapter extends BaseAdapter {

    private ArrayList<String> mDatas;
    private Context mContext;
    private int focusIndex = 0;
    private int colorD,colorF;
    public LandKlineToolsAdapter(Context context)
    {
        mContext = context;
        colorF = ContextCompat.getColor(mContext, R.color.colorMyBlue);
        colorD = ContextCompat.getColor(mContext,R.color.colorMyGray);
    }

    public void setData(ArrayList<String> datas) {
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
    public long getItemId(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.stk_detail_landklinetools_item, null);
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.item_1);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                viewHolder.tv1.setText(mDatas.get(position));
//                viewHolder.tv1.setTextColor(pi.m_Color1);
                if(focusIndex==position){
                    viewHolder.tv1.setTextColor(colorF);
                }else{
                    viewHolder.tv1.setTextColor(colorD);
                }
            }catch (Exception e){
                LogHelper.e("--TPY:",position+"");
            }
        return  convertView;
    }

    public void setFocusIndex(int a_pos){
        focusIndex = a_pos;
    }
    private class ViewHolder{
        public TextView tv1;
    }

}
