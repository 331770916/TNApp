package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockHolderInfoEntity;

import java.util.List;

/**
 * Created by wangqi on 2016/9/2.
 * 股东资料 Adapter
 */
public class StockHolderInfoAdapter extends BaseAdapter {
    Context mContext;
    List<StockHolderInfoEntity> mList;

    public StockHolderInfoAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<StockHolderInfoEntity> data) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stockholderinfo_listview, null);
            viewHodler.CustomerCode = (TextView) convertView.findViewById(R.id.CustomerCode);
            viewHodler.ShareholderSName = (TextView) convertView.findViewById(R.id.ShareholderSName);
            viewHodler.AccountType = (TextView) convertView.findViewById(R.id.AccountType);
            viewHodler.ShareholderSCode = (TextView) convertView.findViewById(R.id.ShareholderSCode);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.CustomerCode.setText(mList.get(position).getCustomerCode());
        viewHodler.ShareholderSName.setText(mList.get(position).getShareholderSName());
        String AccountType = mList.get(position).getAccountType();
        if (AccountType.equals("1")){
            viewHodler.AccountType.setText("沪市A股");
        }else if (AccountType.equals("2")){
            viewHodler.AccountType.setText("深市A股");
        }else if (AccountType.equals("9")){
            viewHodler.AccountType.setText("特转A");
        }else if (AccountType.equals("A")){
            viewHodler.AccountType.setText("特转B");
        }else if (AccountType.equals("D")){
            viewHodler.AccountType.setText("沪Ｂ");
        }else if (AccountType.equals("G")){
            viewHodler.AccountType.setText("沪HK");
        }else if (AccountType.equals("H")){
            viewHodler.AccountType.setText("深Ｂ");
        }

        viewHodler.ShareholderSCode.setText(mList.get(position).getShareholderSCode());

        return convertView;
    }

    class ViewHodler {
        TextView CustomerCode;
        TextView ShareholderSName;
        TextView AccountType;
        TextView ShareholderSCode;
    }
}
