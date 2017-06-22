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
 * 股票更多 Adapter
 */
public class StockPageListAdapter extends BaseAdapter {
    Context context;
    String[] titleName;
    int[] icon;

    public StockPageListAdapter(Context context, String[] titleName, int[] icon) {
        this.context = context;
        this.titleName = titleName;
        this.icon = icon;
    }

    @Override
    public int getCount() {
        return titleName.length;
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
        vh.tv_item_title.setText(titleName[position]);
        vh.iv_item_img.setImageResource(icon[position]);
        vh.tv_item_content.setVisibility(View.GONE);
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
