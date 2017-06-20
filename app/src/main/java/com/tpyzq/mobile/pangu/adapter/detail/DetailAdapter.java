package com.tpyzq.mobile.pangu.adapter.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/6/29.
 * 详情拉伸Ad
 */
public class DetailAdapter extends BaseAdapter {

    private ArrayList<Map<String, Object>> mDatas;
    private int [] colors = {0,0};
    private int stkType = 1;
    private boolean isChangeColor = false;//个股详情，最高最低 需要变换颜色，其他值不变
    public void setStkType(int type){
        this.stkType = type;
    }
    public void setColors(int[] colors){
        this.colors = colors;
    }
    public void setDatas(ArrayList<Map<String, Object>> datas) {
        mDatas = datas;
        notifyDataSetChanged();
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
    public int getCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHodler viewHodler;
        if (convertView == null) {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.detail_grid_item, null);
            viewHodler = new CustomViewHodler(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (CustomViewHodler) convertView.getTag();
        }
        if(isChangeColor){
            if(stkType==0){
                if(position==1){
                    viewHodler.detailBottomTv.setTextColor(colors[0]);
                }else if(position==2){
                    viewHodler.detailBottomTv.setTextColor(colors[1]);
                }
            }else{
                if(position==0){
                    viewHodler.detailBottomTv.setTextColor(colors[0]);
                }else if(position==1){
                    viewHodler.detailBottomTv.setTextColor(colors[1]);
                }
            }
        }

        viewHodler.detailTopTv.setText((String)mDatas.get(position).get("top"));
        viewHodler.detailBottomTv.setText((String)mDatas.get(position).get("bottom"));

        return convertView;
    }

    class CustomViewHodler {

        TextView detailTopTv;
        TextView detailBottomTv;

        public CustomViewHodler(View view) {
            detailTopTv = (TextView) view.findViewById(R.id.item_top);
            detailBottomTv = (TextView) view.findViewById(R.id.item_bottom);
        }
    }
    public void setIsChangeColor(boolean isChangeColor){
        this.isChangeColor = isChangeColor;
    }
}
