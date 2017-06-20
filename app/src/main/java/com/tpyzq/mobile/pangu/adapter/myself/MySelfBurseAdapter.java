package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;


/**
 * Created by 陈新宇 on 2016/9/18.
 */
public class MySelfBurseAdapter extends BaseAdapter {
    private Context context;
    private String[] money;

    public MySelfBurseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public void setContext(String[] money) {
        this.money = money;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_myself_burse, null);
            holder.tv_item_icon = (ImageView) convertView.findViewById(R.id.tv_item_icon);
            holder.tv_item_title = (TextView) convertView.findViewById(R.id.tv_item_title);
            holder.tv_item_content = (TextView) convertView.findViewById(R.id.tv_item_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_item_title.setText(DataUtils.myself_name[position]);
        holder.tv_item_icon.setImageResource(DataUtils.myself_icon[position]);
        if (money != null){
            if (!TextUtils.isEmpty(money[position])){
                holder.tv_item_content.setText(money[position]);
            }else {
                holder.tv_item_content.setText("-.--");
            }
        }else {
            holder.tv_item_content.setText("-.--");
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView tv_item_icon;
        public TextView tv_item_title;
        public TextView tv_item_content;
    }
}
