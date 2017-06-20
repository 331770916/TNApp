package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.CompileTabEntity;

import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/9/12 15:39
 */
public class CompileTabAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CompileTabEntity> list;
    private boolean isOk = true;

    public CompileTabAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<CompileTabEntity> list){
        this.list=list;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_compile_tab,null);
            viewHolder.ivGouXuan = (CheckBox) convertView.findViewById(R.id.ivGouXuan);
            viewHolder.ivPaiXu = (ImageView) convertView.findViewById(R.id.drag_handle);
            viewHolder.tvBiaoTi = (TextView) convertView.findViewById(R.id.tvBiaoTi);
            viewHolder.tvNeiRong = (TextView) convertView.findViewById(R.id.tvNeiRong);
            viewHolder.llItemCompileTab = (LinearLayout) convertView.findViewById(R.id.llItemCompileTab);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CompileTabEntity bean = list.get(position);

        viewHolder.ivPaiXu.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.gengduo));
        viewHolder.tvBiaoTi.setText(bean.getBiaoTi());
        viewHolder.tvNeiRong.setText(bean.getNeiRong());

        //给  勾选  添加监听
        viewHolder.ivGouXuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    list.get(position).setChecked(true);
                }else {
                    list.get(position).setChecked(false);
                }
            }
        });



        viewHolder.ivGouXuan.setChecked(list.get(position).isChecked());    //给  勾选设置状态

        return convertView;
    }


    /**
     * 删除指定位置的item
     * @param position
     */
    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }


    /**
     * 在指定位置插入item
     * @param item
     * @param i
     */
    public void insert(CompileTabEntity item, int i) {
        list.add(i, item);
        notifyDataSetChanged();
    }

    class ViewHolder{
        CheckBox ivGouXuan;        //勾选图片
        ImageView ivPaiXu;          //排序图片
        TextView tvBiaoTi;          //标题
        TextView tvNeiRong;         //内容
        LinearLayout llItemCompileTab;  //item 佈局
    }
}
