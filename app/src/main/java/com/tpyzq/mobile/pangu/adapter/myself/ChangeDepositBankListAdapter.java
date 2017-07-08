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

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/5.
 */

public class ChangeDepositBankListAdapter extends BaseAdapter {

    private List<Map<String, String>> mDatas;
    private String mBankName;
    private Context mContext;
    private ViewHodlder mViewHodlder;

    public ChangeDepositBankListAdapter (Context context) {
        mContext = context;
    }

    public void setDatas (List<Map<String, String>> datas, String bankName) {
        mDatas = datas;
        mBankName = bankName;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            mViewHodlder = new ViewHodlder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_change_banklist, null);
            mViewHodlder.bankName = (TextView) convertView.findViewById(R.id.tv_Name);
            mViewHodlder.bankIv = (ImageView) convertView.findViewById(R.id.iv_Image);
            convertView.setTag(mViewHodlder);
        } else {
            mViewHodlder = (ViewHodlder) convertView.getTag();
        }

        String bankName = mDatas.get(position).get("BANK_NAME");
        mViewHodlder.bankName.setText(bankName);

        if (!TextUtils.isEmpty(mBankName) && mBankName.equals(bankName)) {
            mViewHodlder.bankIv.setImageResource(R.mipmap.duigou);
        }

        return convertView;
    }

    class ViewHodlder {
        TextView bankName;
        ImageView bankIv;
    }
}
