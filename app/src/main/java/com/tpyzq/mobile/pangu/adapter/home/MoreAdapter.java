package com.tpyzq.mobile.pangu.adapter.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.FunctionEntity;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/10/19.
 * 更多Adapter
 */
public class MoreAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FunctionEntity> list;
    private boolean isChecked;
    private int point;

    public MoreAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<FunctionEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setPoint(int point) {
        this.point = point;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
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
        final ViewHolder viewHolder;
//        if (convertView == null) {
        viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_more, null);
        viewHolder.ivMoreIcon = (ImageView) convertView.findViewById(R.id.ivMoreIcon);
        viewHolder.ivMoreGouXuan = (CheckBox) convertView.findViewById(R.id.ivMoreGouXuan);
        viewHolder.drag_handle = (ImageView) convertView.findViewById(R.id.drag_handle);
        viewHolder.tvMoreTitle = (TextView) convertView.findViewById(R.id.tvMoreTitle);
        convertView.setTag(viewHolder);
//        }else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        FunctionEntity functionBean = list.get(position);
        viewHolder.ivMoreIcon.setImageResource(functionBean.getIconId());
        viewHolder.tvMoreTitle.setText(functionBean.getTitle());
        viewHolder.drag_handle.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.gengduo));

        viewHolder.ivMoreGouXuan.setChecked(list.get(position).isChild());

        //给  勾选  添加监听
        viewHolder.ivMoreGouXuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (point < 9) {
                        list.get(position).setChild(true);
                        point++;
                    } else {
                        Toast mToast = Toast.makeText(context, "达到上限", Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                        mToast.show();
                        viewHolder.ivMoreGouXuan.setChecked(false);
                    }
                } else {
                    list.get(position).setChild(false);
                    point--;
                }

            }
        });

        return convertView;
    }


    /**
     * 删除指定位置的item
     *
     * @param position
     */
    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }


    /**
     * 在指定位置插入item
     *
     * @param item
     * @param i
     */
    public void insert(FunctionEntity item, int i) {
        list.add(i, item);
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView ivMoreIcon, drag_handle;     //logo ,
        CheckBox ivMoreGouXuan;                 //勾选图片
        TextView tvMoreTitle;                    //标题
    }
}
