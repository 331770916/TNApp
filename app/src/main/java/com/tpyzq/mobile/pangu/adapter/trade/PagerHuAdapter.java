package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockInfoBean;
import com.tpyzq.mobile.pangu.util.ColorUtils;

import java.util.List;


/**
 * anthor:Created by tianchen on 2017/4/1.
 * email:963181974@qq.com
 */

public class PagerHuAdapter extends BaseAdapter {
    private Context context;
    private List<StockInfoBean> data;

    public static PagerHuAdapter getPagerHuAdapter(Context context) {
        PagerHuAdapter pagerHuAdapter = new PagerHuAdapter();
        pagerHuAdapter.context = context;
        return pagerHuAdapter;
    }

    public void setData(List<StockInfoBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
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
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transaction_8text, null);
            vh.setView(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_text4.setVisibility(View.GONE);
        vh.tv_text6.setVisibility(View.GONE);
        vh.tv_text8.setVisibility(View.GONE);
        String stockCode = data.get(position).stockCode;
        vh.tv_text1.setText(data.get(position).stockName2+"天期");
        vh.tv_text2.setText(stockCode.substring(2));
        vh.tv_text3.setText(data.get(position).yearIncome);
        vh.tv_text5.setText(data.get(position).wYuanIncome);
        vh.tv_text7.setText(data.get(position).tenwYuanDayIncome);
        vh.tv_text7.setTextColor(ColorUtils.RED);
        return convertView;
    }

    class ViewHolder {
        TextView tv_text1;
        TextView tv_text2;
        TextView tv_text3;
        TextView tv_text4;
        TextView tv_text5;
        TextView tv_text6;
        TextView tv_text7;
        TextView tv_text8;

        public void setView(View view) {
            tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
            tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
            tv_text3 = (TextView) view.findViewById(R.id.tv_text3);
            tv_text4 = (TextView) view.findViewById(R.id.tv_text4);
            tv_text5 = (TextView) view.findViewById(R.id.tv_text5);
            tv_text6 = (TextView) view.findViewById(R.id.tv_text6);
            tv_text7 = (TextView) view.findViewById(R.id.tv_text7);
            tv_text8 = (TextView) view.findViewById(R.id.tv_text8);
        }
    }
}
