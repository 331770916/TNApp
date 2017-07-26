package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/9/17 16:06
 */
public class SopCastAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationEntity> list;

    public SopCastAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<InformationEntity> list){
        this.list=list;
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sopcast_pager,null);
            viewHolder.tvSopCastRiQi = (TextView) convertView.findViewById(R.id.tvSopCastRiQi);
            viewHolder.tvSopCastShiJian = (TextView) convertView.findViewById(R.id.tvSopCastShiJian);
            viewHolder.tvSopCastTitle = (TextView) convertView.findViewById(R.id.tvSopCastTitle);
            viewHolder.ivSopCasType = (ImageView) convertView.findViewById(R.id.ivSopCasType);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InformationEntity informationBean = list.get(position);
        String date = informationBean.getDate();                    //日期
        String times = informationBean.getTime();                  //时间
        String publishTitle = informationBean.getTitle();    //标题
        viewHolder.tvSopCastRiQi.setText(date);
        viewHolder.tvSopCastShiJian.setText(times);
        viewHolder.tvSopCastTitle.setText(publishTitle);
        return convertView;
    }

    class ViewHolder{
        TextView tvSopCastRiQi;             //日期
        TextView tvSopCastShiJian;          //时间
        TextView tvSopCastTitle;            //标题
        ImageView ivSopCasType;             //图片
    }
}
