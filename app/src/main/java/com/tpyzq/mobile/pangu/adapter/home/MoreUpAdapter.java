package com.tpyzq.mobile.pangu.adapter.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.FunctionEntity;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/10/21 14:11
 * 上面的  死的 listView 的   适配器
 */
public class MoreUpAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FunctionEntity> list;

    public MoreUpAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<FunctionEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list !=null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list !=null && list.size()>0){
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_more_up, null);
            viewHolder.ivMoreIcon_up = (ImageView) convertView.findViewById(R.id.ivMoreIcon_up);
//            viewHolder.ivMoreDrag_up = (ImageView) convertView.findViewById(R.id.ivMoreDrag_up);
            viewHolder.tvMoreTitle_up = (TextView) convertView.findViewById(R.id.tvMoreTitle_up);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FunctionEntity functionBean = list.get(position);
        viewHolder.ivMoreIcon_up.setImageResource(functionBean.getIconId());
//        viewHolder.ivMoreDrag_up.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.gengduo));
        viewHolder.tvMoreTitle_up.setText(functionBean.getTitle());

        return convertView;
    }


    class ViewHolder{
        ImageView ivMoreIcon_up,ivMoreDrag_up;                    //logo , 勾选图片
        TextView tvMoreTitle_up;                    //标题
    }
}
