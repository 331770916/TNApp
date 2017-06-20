package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.CompileTabEntity;

import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/9/13 19:52
 * 资讯 编辑 界面  上边的  listView 的适配器
 */
public class CompileTabUpAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CompileTabEntity> list;

    public CompileTabUpAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<CompileTabEntity> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list!=null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list!=null && list.size()>0){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_compile_tab_up,null);
            viewHolder.ivGouXuan2 = (CheckBox) convertView.findViewById(R.id.ivGouXuan2);
            viewHolder.ivPaiXu2 = (ImageView) convertView.findViewById(R.id.drag_handle2);
            viewHolder.tvBiaoTi2 = (TextView) convertView.findViewById(R.id.tvBiaoTi2);
            viewHolder.tvNeiRong2 = (TextView) convertView.findViewById(R.id.tvNeiRong2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CompileTabEntity bean = list.get(position);

//        viewHolder.ivGouXuan.setBackgroundResource(R.drawable.tab_compile_check);
        viewHolder.ivGouXuan2.setEnabled(false);
        viewHolder.ivPaiXu2.setImageDrawable(null);
        viewHolder.tvBiaoTi2.setText(bean.getBiaoTi());
        viewHolder.tvNeiRong2.setText(bean.getNeiRong());
        return convertView;
    }

    class ViewHolder{
        CheckBox ivGouXuan2;        //勾选图片
        ImageView ivPaiXu2;          //排序图片
        TextView tvBiaoTi2;          //标题
        TextView tvNeiRong2;         //内容
    }
}
