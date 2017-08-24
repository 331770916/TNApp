package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.RevokeEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.List;


/**
 * Created by wangqi on 2016/9/9.
 * 撤单Adapter
 */
public class RevokeAdapter extends BaseAdapter {
    Context mContext;
    List<RevokeEntity> mList;

    public RevokeAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<RevokeEntity> data) {
        mList = data;
        notifyDataSetChanged();
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
        if (mList != null && mList.size() > 0) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_revoke_list, null);
            viewHodler = new ViewHodler();
            viewHodler.textView1 = (TextView) convertView.findViewById(R.id.tv_revoke_text1);
            viewHodler.textView2 = (TextView) convertView.findViewById(R.id.tv_revoke_text2);
            viewHodler.textView3 = (TextView) convertView.findViewById(R.id.tv_revoke_text3);
            viewHodler.textView4 = (TextView) convertView.findViewById(R.id.tv_revoke_text4);
            viewHodler.textView5 = (TextView) convertView.findViewById(R.id.tv_revoke_text5);
            viewHodler.textView6 = (TextView) convertView.findViewById(R.id.tv_revoke_text6);
            viewHodler.textView7 = (TextView) convertView.findViewById(R.id.tv_revoke_text7);
            viewHodler.textView8 = (TextView) convertView.findViewById(R.id.tv_revoke_text8);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.textView1.setText(mList.get(position).getName());

//        viewHodler.textView2.setText(Helper.formateDate(mList.get(position).getTitm().toString()));
        viewHodler.textView2.setText(mList.get(position).getCode());

        if (!"0".equals(mList.get(position).getPrice())) {
            viewHodler.textView3.setText(mList.get(position).getPrice());
        } else {
            viewHodler.textView3.setText("0.00");
        }

        if (!"0".equals(mList.get(position).getMatchedPrice())) {
            viewHodler.textView4.setText(mList.get(position).getMatchedPrice());
        } else {
            viewHodler.textView4.setText("0.00");
        }


        viewHodler.textView5.setText(mList.get(position).getWithdrawnQty());
        viewHodler.textView6.setText(mList.get(position).getMatchedQty());

        switch (mList.get(position).getEntrustStatus()) {
            case "0":
                viewHodler.textView7.setText("未报");
                break;
            case "1":
                viewHodler.textView7.setText("待报");
                break;
            case "2":
                viewHodler.textView7.setText("已报");
                break;
            case "3":
                viewHodler.textView7.setText("已报待撤");
                break;
            case "4":
                viewHodler.textView7.setText("部成待撤");
                break;
            case "7":
                viewHodler.textView7.setText("部成");
                break;
            case "8":
                viewHodler.textView7.setText("已成");
                break;
            case "9":
                viewHodler.textView7.setText("废单");
                break;
        }

        switch (mList.get(position).getEntrustBs()) {
            case "1":
                viewHodler.textView8.setText("买入");
                break;
            case "2":
                viewHodler.textView8.setText("卖出");
                break;
        }


        return convertView;
    }

    class ViewHodler {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView6;
        TextView textView7;
        TextView textView8;
    }
}
