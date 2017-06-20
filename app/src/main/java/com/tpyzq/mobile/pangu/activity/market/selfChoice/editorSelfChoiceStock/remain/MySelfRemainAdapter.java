package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.SubSelfChoiceEntity;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/9/12.
 */
public class MySelfRemainAdapter extends BaseAdapter {

    private DeleteRemainListener mDeleteRemainListener;
    public MySelfRemainAdapter (DeleteRemainListener deleteRemainListener) {
        mDeleteRemainListener = deleteRemainListener;
    }

    private List<SubSelfChoiceEntity> mDatas;
    public void setDatas(List<SubSelfChoiceEntity> datas) {
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
    public Object getItem(int position) {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_remain_self, null);
            viewHodler.stockName = (TextView) convertView.findViewById(R.id.remain_stockName);
            viewHodler.stockNumber = (TextView) convertView.findViewById(R.id.remain_stockNumber);
            viewHodler.remainText = (TextView) convertView.findViewById(R.id.remain_price);
            viewHodler.deleteIv = (ImageView) convertView.findViewById(R.id.remain_deleteiv);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.stockName.setText(null);
        viewHodler.stockNumber.setText(null);
        viewHodler.remainText.setText(null);
        viewHodler.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteRemainListener.deleteRemain(position);
            }
        });


        if (!TextUtils.isEmpty(mDatas.get(position).getNAME())) {
            viewHodler.stockName.setText(mDatas.get(position).getNAME());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getCODE())){
            viewHodler.stockNumber.setText(mDatas.get(position).getCODE());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getRemainType())) {
            viewHodler.remainText.setText(mDatas.get(position).getRemainType());
        }

        return convertView;
    }

    private class ViewHodler {
        TextView stockName;
        TextView stockNumber;
        TextView remainText;
        ImageView deleteIv;
    }

    public interface DeleteRemainListener{
        void deleteRemain(int position);
    }
}
