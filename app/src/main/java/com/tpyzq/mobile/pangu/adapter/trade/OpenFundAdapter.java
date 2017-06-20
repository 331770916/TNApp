package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;


/**
 * Created by 陈新宇 on 2016/8/24.
 */
public class OpenFundAdapter extends BaseAdapter {
    Context context;

    public OpenFundAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return DataUtils.open_fund_name.length;
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
        final ListViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_open_fund_list, null);
            vh = new ListViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ListViewHolder) convertView.getTag();
        }
        vh.tv_item_title.setText(DataUtils.open_fund_name[position]);
        vh.iv_item_img.setImageResource(DataUtils.open_fund_icon[position]);
        switch (position){
            case 2:
                vh.tv_item_content.setVisibility(View.VISIBLE);
                vh.tv_item_content.setText("设置基金分红方式为现金分红或份额分红");
                break;
            case 3:
                vh.tv_item_content.setVisibility(View.VISIBLE);
                vh.tv_item_content.setText("将基金直接转换为相同基金公司的其他基金");
                break;
            default:
                vh.tv_item_content.setVisibility(View.GONE);
                break;
        }
        return convertView;
    }

    class ListViewHolder {
        View itemView;
        ImageView iv_item_img;
        TextView tv_item_title;
        TextView tv_item_content;

        public ListViewHolder(View itemView) {
            this.itemView = itemView;
            iv_item_img = (ImageView) itemView.findViewById(R.id.iv_item_img);
            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
            tv_item_content = (TextView) itemView.findViewById(R.id.tv_item_content);
        }
    }
}
