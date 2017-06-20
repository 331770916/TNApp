package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.List;

/**
 * Created by wangqi on 2016/9/16.
 * 职业选择 Adapter
 */
public class ProfessionAdapter extends BaseAdapter {
    List<String> nameList,codeList;
    Context mCntext;
    int point = -1;
    String mData;

    public ProfessionAdapter(Context context,List<String> nlist,List<String> clist) {
        this.mCntext = context;
        this.nameList = nlist;
        this.codeList = clist;
    }

    public void setPointVisibility(String  data){
        this.mData=data;
        notifyDataSetChanged();
    }

//    public void setData(List<String> data) {
//        this.mList = data;
//
//        notifyDataSetChanged();
//    }

    public void setPoint(int point) {
        this.point = point;
    }
    @Override
    public int getCount() {
        return nameList == null ? 0 :nameList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mCntext).inflate(R.layout.item_text_imag_listview, null);
            viewHolder.item = (TextView) convertView.findViewById(R.id.tv_Name);
            viewHolder.item_ic = (ImageView) convertView.findViewById(R.id.iv_Image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item.setText(nameList.get(position));


        if (point == position || mData.equals(codeList.get(position))) {
            viewHolder.item_ic.setVisibility(View.VISIBLE);
            viewHolder.item_ic.setImageResource(R.mipmap.duigou);
        } else {
            viewHolder.item_ic.setVisibility(View.GONE);
        }

//        if (mList.get(position).toString().equals(mData)){
//            viewHolder.item_ic.setVisibility(View.VISIBLE);
//            viewHolder.item_ic.setImageResource(R.mipmap.duigou);
//        }else {
//            viewHolder.item_ic.setVisibility(View.GONE);
//        }

        return convertView;
    }

    class ViewHolder {
        TextView item;
        ImageView item_ic;
    }

}
