package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.CapitalEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wangqi on 2016/8/11.
 * 交割单
 */
public class DeliveryListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private int mExpandedMenuPos = -1;
    List<CapitalEntity> mList;
    private Context mContext;

    public DeliveryListViewAdapter(Context context) {
        mContext = context;
    }


    public void setData(int ExpandedMenuPos) {
        mExpandedMenuPos = ExpandedMenuPos;
    }

    public void setData(List<CapitalEntity> List) {
        mList = List;
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
        if (mList.get(position) != null && mList.size() > 0) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHodler viewHodler = null;
        if (view == null) {
            viewHodler = new ViewHodler();
            view = LayoutInflater.from(mContext).inflate(R.layout.deliveryltoday_item, null);

            viewHodler.DeliveryItem = (LinearLayout) view.findViewById(R.id.LinearLayout);
            viewHodler.DeliveryItem0 = (LinearLayout) view.findViewById(R.id.deliveryItem_LL);
            viewHodler.DeliveryItem1 = (TextView) view.findViewById(R.id.delivery_Date);
            viewHodler.DeliveryItem2 = (TextView) view.findViewById(R.id.delivery_Name);
            viewHodler.DeliveryItem3 = (TextView) view.findViewById(R.id.delivery_Business);
            viewHodler.DeliveryItem4 = (TextView) view.findViewById(R.id.delivery_Money);
            viewHodler.DeliveryItem5 = (TextView) view.findViewById(R.id.delivery_Balance);
            viewHodler.DeliveryItem6 = (ImageView) view.findViewById(R.id.deliveryImageView);

            viewHodler.Iitem1 = (TextView) view.findViewById(R.id.deliveryTransactionPrice);
            viewHodler.Iitem2 = (TextView) view.findViewById(R.id.deliveryHbcjsl);
            viewHodler.Iitem3 = (TextView) view.findViewById(R.id.deliveryShareholder);
            viewHodler.Iitem4 = (TextView) view.findViewById(R.id.deliveryRemarks);
            viewHodler.Iitem5 = (TextView) view.findViewById(R.id.deliveryCommission);
            viewHodler.Iitem6 = (TextView) view.findViewById(R.id.deliveryStamps);
            viewHodler.Iitem7 = (TextView) view.findViewById(R.id.deliveryTransferFee);
            viewHodler.Iitem8 = (TextView) view.findViewById(R.id.deliveryOtherexpenses);

            view.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) view.getTag();
        }
        DecimalFormat mFormat1 = new DecimalFormat("#0.000");
        if (mExpandedMenuPos == position) {
            if (viewHodler.DeliveryItem.isSelected()) {
                viewHodler.DeliveryItem.setSelected(false);
                viewHodler.DeliveryItem0.setVisibility(View.GONE);
                mExpandedMenuPos = -1;
            } else {
                viewHodler.DeliveryItem.setSelected(true);
                viewHodler.DeliveryItem0.setVisibility(View.VISIBLE);
            }
        } else {
            viewHodler.DeliveryItem0.setVisibility(View.GONE);
            viewHodler.DeliveryItem.setSelected(false);
        }


        viewHodler.DeliveryItem1.setText(Helper.getMyDateY_M_D(mList.get(position).getDate()));
        viewHodler.DeliveryItem2.setText(mList.get(position).getName());
        viewHodler.DeliveryItem3.setText(mList.get(position).getStockCode());

        switch (mList.get(position).getTransaction()) {
            case "1":
                viewHodler.DeliveryItem4.setText("买入");
                break;
            case "2":
                viewHodler.DeliveryItem4.setText("卖出");
                break;
            default:
                viewHodler.DeliveryItem4.setText("--");
                break;
        }
        viewHodler.DeliveryItem5.setText(mFormat1.format(Double.parseDouble(mList.get(position).getBalance())));

        viewHodler.Iitem1.setText(mFormat1.format(Double.parseDouble(mList.get(position).getTransactionPrice())));

        if (!TextUtils.isEmpty(mList.get(position).getHbcjsl()) && !mList.get(position).getHbcjsl().equals("0")) {
            String str = String.valueOf(mList.get(position).getHbcjsl());//浮点变量a转换为字符串str
            int idx = str.lastIndexOf(".");//查找小数点的位置
            String strNum = str.substring(0, idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
            viewHodler.Iitem2.setText(strNum);
        } else {
            viewHodler.Iitem2.setText(mList.get(position).getHbcjsl());
        }


        viewHodler.Iitem3.setText(mList.get(position).getShareholder());
        viewHodler.Iitem4.setText(mList.get(position).getRemarks());
        viewHodler.Iitem5.setText(mFormat1.format(Double.parseDouble(mList.get(position).getCommission())));
        viewHodler.Iitem6.setText(mFormat1.format(Double.parseDouble(mList.get(position).getStamps())));
        viewHodler.Iitem7.setText(mFormat1.format(Double.parseDouble(mList.get(position).getTransferFee())));
        viewHodler.Iitem8.setText(mFormat1.format(Double.parseDouble(mList.get(position).getOtherexpenses())));


        viewHodler.DeliveryItem0.setOnClickListener(this);
        viewHodler.DeliveryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedMenuPos = position;//记录点击的position
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.deliveryItem_LL) {
            mExpandedMenuPos = -1;
            notifyDataSetChanged();
        }
    }

    private class ViewHodler {
        LinearLayout DeliveryItem;
        LinearLayout DeliveryItem0;
        TextView DeliveryItem1;
        TextView DeliveryItem2;
        TextView DeliveryItem3;
        TextView DeliveryItem4;
        TextView DeliveryItem5;
        ImageView DeliveryItem6;

        TextView Iitem1;
        TextView Iitem2;
        TextView Iitem3;
        TextView Iitem4;
        TextView Iitem5;
        TextView Iitem6;
        TextView Iitem7;
        TextView Iitem8;
    }
}

