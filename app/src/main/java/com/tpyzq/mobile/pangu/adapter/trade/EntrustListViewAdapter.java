package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.TodayEntrustEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wangqi on 2016/8/13.
 * 委托查询 Adapter
 */
public class EntrustListViewAdapter extends BaseAdapter {
    private List<TodayEntrustEntity> mList;
    private Context mContext;

    public EntrustListViewAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<TodayEntrustEntity> setText) {
        mList = setText;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.today_item, null);
            viewHodler.takeapositionItem1 = (TextView) convertView.findViewById(R.id.Today_Name);
            viewHodler.takeapositionItem2 = (TextView) convertView.findViewById(R.id.Today_Num);
            viewHodler.takeapositionItem3 = (TextView) convertView.findViewById(R.id.Today_Date);
            viewHodler.takeapositionItem4 = (TextView) convertView.findViewById(R.id.Today_Time);
            viewHodler.takeapositionItem5 = (TextView) convertView.findViewById(R.id.Today_Entrust);
            viewHodler.takeapositionItem6 = (TextView) convertView.findViewById(R.id.Today_Succeed);
            viewHodler.takeapositionItem7 = (TextView) convertView.findViewById(R.id.Today_Transaction);
            viewHodler.takeapositionItem8 = (TextView) convertView.findViewById(R.id.Today_Status);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        DecimalFormat mFormat1 = new DecimalFormat("#0.000");
        viewHodler.takeapositionItem1.setText(mList.get(position).getName());
        viewHodler.takeapositionItem2.setText(mFormat1.format(Double.parseDouble(mList.get(position).getNum())));
        viewHodler.takeapositionItem3.setText(Helper.getMyDateY_M_D(mList.get(position).getDate()));
        viewHodler.takeapositionItem4.setText(Helper.getMyDateHMS(mList.get(position).getTime()));
        viewHodler.takeapositionItem5.setText(mList.get(position).getEntrust());
        viewHodler.takeapositionItem6.setText(mList.get(position).getSucceed());
        switch (mList.get(position).getTransaction()) {
            case "1":
                viewHodler.takeapositionItem7.setText("买入");
                viewHodler.takeapositionItem7.setTextColor(Color.parseColor("#fa5050"));
                break;
            case "2":
                viewHodler.takeapositionItem7.setText("卖出");
                viewHodler.takeapositionItem7.setTextColor(Color.parseColor("#FF50BF6A"));
                break;
            case "3":
                viewHodler.takeapositionItem7.setText("撤单");
                viewHodler.takeapositionItem7.setTextColor(Color.parseColor("#f3ae28"));
                break;
        }
        viewHodler.takeapositionItem8.setText(mList.get(position).getStatus());

        return convertView;
    }

    class ViewHodler {
        TextView takeapositionItem1;
        TextView takeapositionItem2;
        TextView takeapositionItem3;
        TextView takeapositionItem4;
        TextView takeapositionItem5;
        TextView takeapositionItem6;
        TextView takeapositionItem7;
        TextView takeapositionItem8;
    }
}

