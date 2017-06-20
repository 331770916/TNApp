package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/10/7.
 */
public class ProductNoticeAdapter extends BaseAdapter{

    private ArrayList<CleverManamgerMoneyEntity> mEntities;
    private int mTextColor;

    public ProductNoticeAdapter(int textcolor) {
        mTextColor = textcolor;
    }

    public void setDatas(ArrayList<CleverManamgerMoneyEntity> entities) {
        mEntities = entities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mEntities != null && mEntities.size() > 0) {
            return mEntities.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mEntities != null && mEntities.size() > 0) {
            return mEntities.get(position);
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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_product_notice, null);
            viewHodler.mContent = (TextView) convertView.findViewById(R.id.productNoticeContent);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.mContent.setTextColor(mTextColor);

        viewHodler.mContent.setText(null);

        viewHodler.mContent.setText("《" + mEntities.get(position).getGeneralSituationContent() +"》");

        return convertView;
    }


    private class ViewHodler{
        TextView mContent;
    }

}
