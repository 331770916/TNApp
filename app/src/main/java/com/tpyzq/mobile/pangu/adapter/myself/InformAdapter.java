package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.InformEntity;

import java.util.List;


/**
 * Created by wangqi on 2016/10/12.
 * 我的消息 Adapter
 */
public class InformAdapter extends BaseAdapter {
    private List<InformEntity> mList;
    private Context mContext;

    public InformAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<InformEntity> data) {
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
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_text, null);
            viewHodler.textView1 = (TextView) convertView.findViewById(R.id.item_text1);
            viewHodler.textView2 = (TextView) convertView.findViewById(R.id.item_text2);
            viewHodler.textView3 = (TextView) convertView.findViewById(R.id.item_text3);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.textView1.setText(mList.get(position).getTitle());
        viewHodler.textView2.setText(mList.get(position).getPush_time());
        viewHodler.textView3.setText(mList.get(position).getContene());
        return convertView;
    }

    public class ViewHodler {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }
}
