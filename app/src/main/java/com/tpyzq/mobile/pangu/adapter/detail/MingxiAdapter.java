package com.tpyzq.mobile.pangu.adapter.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.chart.PankouItem;
import com.tpyzq.mobile.pangu.log.LogHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/24
 */
public class MingxiAdapter extends BaseAdapter {

    private ArrayList<PankouItem> mDatas;
    private Context mContext;

    public MingxiAdapter(Context context ) {
        mContext = context;
    }

    public void setData(ArrayList<PankouItem> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }
        return null;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.stk_detail_mingxi_item, null);
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.item_1);
                viewHolder.tv2 = (TextView) convertView.findViewById(R.id.item_2);
                viewHolder.tv3 = (TextView) convertView.findViewById(R.id.item_3);
                viewHolder.tv4 = (TextView) convertView.findViewById(R.id.item_4);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            try {
                PankouItem pi = mDatas.get(position);
                viewHolder.tv1.setText(pi.m_Str1);
                viewHolder.tv1.setTextColor(pi.m_Color1);

                viewHolder.tv2.setTextColor(pi.m_Color2);
                viewHolder.tv2.setText(pi.m_Str2);
                viewHolder.tv3.setTextColor(pi.m_Color3);
                viewHolder.tv3.setText(pi.m_Str3);

                viewHolder.tv4.setTextColor(pi.m_Color4);
                viewHolder.tv4.setText(pi.m_Str4);
            }catch (Exception e){
                LogHelper.e("--TPYMingxiAdapterException:",position+"");
            }
        return convertView;
    }


    private class ViewHolder{
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
    }

}
